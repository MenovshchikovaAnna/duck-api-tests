package autotests.duck_action_controller;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

public class DuckActionFly extends TestNGCitrusSpringSupport {
    String url = "http://localhost:2222";

    @Test(description = "Проверка полета уточки с крыльями Active")
    @CitrusTest
    public void activeWingsFly(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
        duckFly(runner, getIdCreatedDuck(runner));
        validateResponse(runner, "{\n \"message\": \"I am flying :)\"\n}");
    }

    @Test(description = "Проверка полета уточки с крыльями Fixed")
    @CitrusTest
    public void fixedWingsFly(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "FIXED");
        duckFly(runner, getIdCreatedDuck(runner));
        validateResponse(runner, "{\n \"message\": \"I can not fly :C\"\n}");
    }

    @Test(description = "Проверка полета уточки с крыльями Undefined")
    @CitrusTest
    public void undefinedWingsFly(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "UNDEFINED");
        duckFly(runner, getIdCreatedDuck(runner));
        validateResponse(runner, "{\n \"message\": \"Wings are not detected :(\"\n}");
    }

    //создание уточки
    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        runner.$(
                http()
                        .client(url)
                        .send()
                        .post("/api/duck/create")
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body("{\n" +
                                "\"color\": \"" + color + "\",\n" +
                                "\"height\": " + height + ",\n" +
                                "\"material\": \"" + material + "\",\n" +
                                "\"sound\": \"" + sound + "\",\n" +
                                "\"wingsState\": \"" + wingsState + "\"}"));
    }

    //Получение id созданной уточки
    public String getIdCreatedDuck(TestCaseRunner runner) {
        runner.$(
                http()
                        .client(url)
                        .receive()
                        .response()
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .extract(fromBody().expression("$.id", "duckId"))
        );
        return "${duckId}";
    }

    //Запрос - лететь
    public void duckFly(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client(url)
                        .send()
                        .get("/api/duck/action/fly")
                        .queryParam("id", id));
    }

    //валидация ответа
    public void validateResponse(TestCaseRunner runner, String responseMessage) {
        runner.$(
                http()
                        .client(url)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(responseMessage));
    }
}