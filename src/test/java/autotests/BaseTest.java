package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.actions.HttpClientRequestActionBuilder;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;

import java.util.Map;

import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.container.FinallySequence.Builder.doFinally;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class BaseTest extends TestNGCitrusSpringSupport {
    @Autowired
    protected HttpClient duckService;

    @Autowired
    protected SingleConnectionDataSource testDb;

    protected void getRequest(TestCaseRunner runner, HttpClient url, String path, Map<String, Object> queryParams) {
        HttpClientRequestActionBuilder builder = http()
                .client(url)
                .send()
                .get(path);

        for (Map.Entry <String, Object> qp : queryParams.entrySet()) {
            builder.queryParam(qp.getKey(), qp.getValue().toString());
        }

        runner.$(builder);
    }

    protected void putRequest(TestCaseRunner runner, HttpClient url, String path, Map<String, Object> queryParams) {
        HttpClientRequestActionBuilder builder = http()
                .client(url)
                .send()
                .put(path);

        for (Map.Entry <String, Object> qp : queryParams.entrySet()) {
            builder.queryParam(qp.getKey(), qp.getValue().toString());
        }

        runner.$(builder);
    }

    protected void postRequest(TestCaseRunner runner, HttpClient url, String path, String contentType, Object body) {
        runner.$(
                http()
                        .client(url)
                        .send()
                        .post(path)
                        .message()
                        .contentType(contentType)
                        .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper())));
    }

    protected void deleteRequest(TestCaseRunner runner, HttpClient url, String path, String queName, String queValue) {
        runner.$(
                http()
                        .client(url)
                        .send()
                        .delete(path)
                        .queryParam(queName, queValue));
    }

    protected void responseRequest(TestCaseRunner runner, HttpClient url, HttpStatus expectedStatus, String contentType, Object body) {
        if (body instanceof String) {
            runner.$(
                    http()
                            .client(url)
                            .receive()
                            .response(expectedStatus)
                            .message()
                            .contentType(contentType)
                            .body((String) body)
            );
        } else if (body instanceof ClassPathResource) {
            runner.$(
                    http()
                            .client(url)
                            .receive()
                            .response(expectedStatus)
                            .message()
                            .contentType(contentType)
                            .body((ClassPathResource) body)
            );
        } else if (body instanceof ObjectMappingPayloadBuilder) {
            runner.$(
                    http()
                            .client(url)
                            .receive()
                            .response(expectedStatus)
                            .message()
                            .contentType(contentType)
                            .body((ObjectMappingPayloadBuilder) body)
            );
        } else {
            throw new IllegalArgumentException("Неподдерживающийся тип body");
        }
    }

    protected void databaseUpdate(TestCaseRunner runner, String sql) {
        runner.$(sql(testDb)
                .statement(sql));
    }

    protected void deleteFromTable(TestCaseRunner runner, String tableName, String condition) {
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM " + tableName + " WHERE " + condition)));
    }

    protected void insertIntoTable(TestCaseRunner runner, String nameTable, String parameters, String values) {
        databaseUpdate(runner, "insert into " + nameTable + " (" + parameters + ")\n" +
                "values (" +
                values + "')");
    }
}