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

public class DuckActionSwim extends TestNGCitrusSpringSupport {
    String url = "http://localhost:2222";

    @Test(description = "Проверка плаванья уточки с существующим id")
    @CitrusTest
    public void successSwim(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
        duckSwim(runner, getIdCreatedDuck(runner));
        validateResponseSuccessSwim(runner, "{\n \"message\": \"I'm swimming\"\n}");
    }

    @Test(description = "Проверка плаванья уточки с несуществующим id")
    @CitrusTest
    public void invalidIdSwim(@Optional @CitrusResource TestCaseRunner runner) {
        duckSwim(runner, "999999");
        validateResponseInvalidIdSwim(runner, "{\n \"message\": \"Paws are not found ((((\"\n}");
    }

    //Создание уточки
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

    //Запрос - плыть
    public void duckSwim(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client(url)
                        .send()
                        .get("/api/duck/action/swim")
                        .queryParam("id", id));
    }

    //Валидация ответа с несуществующим id
    public void validateResponseInvalidIdSwim(TestCaseRunner runner, String responseMessage) {
        runner.$(
                http()
                        .client(url)
                        .receive()
                        .response(HttpStatus.NOT_FOUND)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(responseMessage));
    }

    //Валидация ответа с существующим id
    public void validateResponseSuccessSwim(TestCaseRunner runner, String responseMessage) {
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