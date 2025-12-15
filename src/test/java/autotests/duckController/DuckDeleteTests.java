package autotests.duckController;

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

public class DuckDeleteTests extends TestNGCitrusSpringSupport {
    String url = "http://localhost:2222";

    @Test(description = "Проверка удаления уточки")
    @CitrusTest
    public void successDeleteDuck(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
        deleteDuck(runner, getIdCreatedDuck(runner));
        validateResponseActiveWingsFly(runner, "{\n \"message\": \"Duck is deleted\"\n}");
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

    //Удаление уточки
    public void deleteDuck(TestCaseRunner runner, String idDuck) {
        runner.$(
                http()
                        .client(url)
                        .send()
                        .delete("/api/duck/delete")
                        .queryParam("id", idDuck));
    }

    //валидация ответа
    public void validateResponseActiveWingsFly(TestCaseRunner runner, String responseMessage) {
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
