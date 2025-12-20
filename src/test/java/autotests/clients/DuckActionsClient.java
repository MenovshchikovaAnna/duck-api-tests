package autotests.clients;

import autotests.BaseTest;
import autotests.payloads.BodyCreateDuck;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static com.consol.citrus.actions.EchoAction.Builder.echo;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;

public class DuckActionsClient extends BaseTest {

    @Step("Создаём уточку")
    public void createDuck(TestCaseRunner runner, Object duckProperties) {
        postRequest(runner,
                duckService,
                "/api/duck/create",
                MediaType.APPLICATION_JSON_VALUE,
                duckProperties);
    }

    @Step("Заставляем уточку крякать")
    public void quackDuck(TestCaseRunner runner, String idDuck, int repetitionCount, int soundCount) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("id", idDuck);
        queryParams.put("repetitionCount", repetitionCount);
        queryParams.put("soundCount", soundCount);

        getRequest(runner,
                duckService,
                "/api/duck/action/quack",
                queryParams);
    }

    @Step("Обновляем характеристики уточки")
    public void updateDuck(TestCaseRunner runner, String idDuck, String changeableColor, double changeableHeight, String changeableMaterial, String changeableSound, String changeableWingsState) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("id", idDuck);
        queryParams.put("color", changeableColor);
        queryParams.put("height", String.valueOf(changeableHeight));
        queryParams.put("material", changeableMaterial);
        queryParams.put("sound", changeableSound);
        queryParams.put("wingsState", changeableWingsState);

        putRequest(runner,
                duckService,
                "/api/duck/update",
                queryParams);
    }

    @Step("Удаляем уточку")
    public void deleteDuck(TestCaseRunner runner, String idDuck) {
        deleteRequest(runner,
                duckService,
                "/api/duck/delete",
                "id", idDuck);
    }

    @Step("Заставляем уточку плыть")
    public void duckSwim(TestCaseRunner runner, String id) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("id", id);

        getRequest(runner,
                duckService,
                "/api/duck/action/swim",
                queryParams);
    }

    @Step("Заставляем уточку лететь")
    public void duckFly(TestCaseRunner runner, String id) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("id", id);

        getRequest(runner,
                duckService,
                "/api/duck/action/fly",
                queryParams);
    }

    @Step("Заставляем уточку показать характеристики")
    public void showPropertiesDuck(TestCaseRunner runner, String idDuck) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("id", idDuck);

        getRequest(runner,
                duckService,
                "/api/duck/action/properties",
                queryParams);
    }

    @Step("Валидация ответа String")
    public void validateResponseString(TestCaseRunner runner, HttpStatus expectedStatus, String responseMessage) {
        responseRequest(runner,
                duckService,
                expectedStatus,
                MediaType.APPLICATION_JSON_VALUE,
                responseMessage);
    }

    @Step("Валидация ответа Resourses")
    public void validateResponseResourses(TestCaseRunner runner, HttpStatus expectedStatus, String resourcePath) {
        responseRequest(runner,
                duckService,
                expectedStatus,
                MediaType.APPLICATION_JSON_VALUE,
                new ClassPathResource(resourcePath));
    }

    @Step("Валидация ответа Payload")
    public void validateResponsePayload(TestCaseRunner runner, HttpStatus expectedStatus, Object expectedResponse) {
        responseRequest(runner,
                duckService,
                expectedStatus,
                MediaType.APPLICATION_JSON_VALUE,
                new ObjectMappingPayloadBuilder(expectedResponse, new ObjectMapper()));
    }

    @Step("Создание утки с помощью SQL запросов к БД")
    public void createDuckWithDatabase(TestCaseRunner runner, BodyCreateDuck duckProperties, String duckId) {
        deleteFromTable(runner,
                "DUCK",
                "ID = " + duckId);

        String values = duckId + ", '" +
                duckProperties.color() + "', " +
                duckProperties.height() + ", '" +
                duckProperties.material() + "', '" +
                duckProperties.sound() + "', '" +
                duckProperties.wingsState().name();

        try {
            insertIntoTable(runner,
                    "DUCK",
                    "id, color, height, material, sound, wings_state",
                    values);
        } catch (Exception e) {
            runner.$(echo("Утка с ID = " + duckId + " уже существует"));
        }
    }

    @Step("Проверка, что уточка действительно создалась в БД")
    public void selectCheckingDuckById(TestCaseRunner runner, String idDuck, String exceptColor, double exceptHeight, String exceptMaterial, String exceptSound, String exceptWingsState) {

        String conditions = "ID = " + idDuck +
                " AND COLOR = '" + exceptColor +
                "' AND HEIGHT = " + exceptHeight +
                " AND MATERIAL = '" + exceptMaterial +
                "' AND SOUND = '" + exceptSound +
                "' AND WINGS_STATE = '" + exceptWingsState + "'";

        String sqlSelect = "SELECT id " +
                "FROM DUCK " +
                "WHERE " + conditions;

        runner.$(
                query(testDb)
                        .statement(sqlSelect)
                        .validate("ID", idDuck)
        );

        runner.$(echo("Утка с ID = " + idDuck + " найдена в БД"));
    }

    @Step("Проверка, что уточка действительно удалена из БД")
    public void selectVerifyDuckNotInDatabase(TestCaseRunner runner, String idDuck) {
        String sqlSelect = "SELECT COUNT(*) AS count " +
                "FROM DUCK " +
                "WHERE ID = " + idDuck;

        runner.$(
                query(testDb)
                        .statement(sqlSelect)
                        .validate("count", "0")
        );

        runner.$(echo("Утка с ID = " + idDuck + " успешно удалена из БД"));
    }
}