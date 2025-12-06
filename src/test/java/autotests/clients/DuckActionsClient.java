package autotests.clients;

import autotests.tests.BaseTest;
import autotests.tests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.actions.AbstractTestAction;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckActionsClient extends BaseTest {

    @Autowired
    protected HttpClient duckService;

    //Создание уточки
    public void createDuck(TestCaseRunner runner, Object duckProperties) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .post("/api/duck/create")
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(new ObjectMappingPayloadBuilder(duckProperties, new ObjectMapper())));
    }

    //Запрос - плыть
    public void duckSwim(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .get("/api/duck/action/swim")
                        .queryParam("id", id));
    }

    //Запрос - лететь
    public void duckFly(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .get("/api/duck/action/fly")
                        .queryParam("id", id));
    }

    //Запрос - крякать
    public void quackDuck(TestCaseRunner runner, String idDuck, int repetitionCount, int soundCount) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .get("/api/duck/action/quack")
                        .queryParam("id", idDuck)
                        .queryParam("repetitionCount", String.valueOf(repetitionCount))
                        .queryParam("soundCount", String.valueOf(soundCount)));
    }

    //Запрос - показать характеристики
    public void showPropertiesDuck(TestCaseRunner runner, String idDuck) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .get("/api/duck/action/properties")
                        .queryParam("id", idDuck));
    }

    public void updateDuck(TestCaseRunner runner, String idDuck, String changeableColor, double changeableHeight, String changeableMaterial, String changeableSound, String changeableWingsState) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .put("/api/duck/update")
                        .queryParam("id", idDuck)
                        .queryParam("color", changeableColor)
                        .queryParam("height", String.valueOf(changeableHeight))
                        .queryParam("material", changeableMaterial)
                        .queryParam("sound", changeableSound)
                        .queryParam("wingsState", changeableWingsState));
    }

    //Удаление уточки
    public void deleteDuck(TestCaseRunner runner, String idDuck) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .delete("/api/duck/delete")
                        .queryParam("id", idDuck));
    }

    //Валидация ответа
    public void validateResponseString(TestCaseRunner runner, HttpStatus expectedStatus, String responseMessage) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(expectedStatus)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(responseMessage));
    }

    //Валидация ответа
    public void validateResponseResourses(TestCaseRunner runner, HttpStatus expectedStatus, String resourcePath) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(expectedStatus)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(new ClassPathResource(resourcePath)));
    }

    //Валидация ответа
    public void validateResponsePayload(TestCaseRunner runner, HttpStatus expectedStatus, Object expectedResponse) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(expectedStatus)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(new ObjectMappingPayloadBuilder(expectedResponse, new ObjectMapper())));
    }

    //Получение id
    public String getId(TestCaseRunner runner) {
        final String[] idValue = new String[1];

        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response()
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .extract(fromBody().expression("$.id", "valueId")));

        runner.run(new AbstractTestAction() {
            @Override
            public void doExecute(TestContext context) {
                idValue[0] = context.getVariable("valueId");
            }
        });

        return idValue[0];
    }

    //Проверка чётности id
    public boolean checkIdEven(String idValue) {
        return Integer.parseInt(idValue) % 2 == 0;
    }
}