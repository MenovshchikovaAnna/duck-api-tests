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

public class DuckUpdateTests extends TestNGCitrusSpringSupport {
    String url = "http://localhost:2222";

    @Test(description = "Проверка изменения параметров уточки (color и height)")
    @CitrusTest
    public void updateDuckParametersColorHeight(@Optional @CitrusResource TestCaseRunner runner) {
        String idDuck;

        createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
        idDuck = getIdCreatedDuck(runner);
        updateDuck(runner, idDuck, "red", 0.05, "rubber", "quack", "ACTIVE");
        validateResponseActiveWingsFly(runner, "{\n  \"message\": \"Duck with id = " + idDuck + " is updated\"\n}");
    }

    @Test(description = "Проверка изменения параметров уточки (color и sound)")
    @CitrusTest
    public void updateDuckParametersColorSound(@Optional @CitrusResource TestCaseRunner runner) {
        String idDuck;

        createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
        idDuck = getIdCreatedDuck(runner);
        updateDuck(runner, idDuck, "blue", 0.05, "rubber", "quack-quack", "ACTIVE");
        validateResponseActiveWingsFly(runner, "{\n  \"message\": \"Duck with id = " + idDuck + " is updated\"\n}");
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

    public void updateDuck(TestCaseRunner runner, String idDuck, String changeableColor, double changeableHeight, String changeableMaterial, String changeableSound, String changeableWingsState) {
        runner.$(
                http()
                        .client(url)
                        .send()
                        .put("/api/duck/update")
                        .queryParam("id", idDuck)
                        .queryParam("color", changeableColor)
                        .queryParam("height", String.valueOf(changeableHeight))
                        .queryParam("material", changeableMaterial)
                        .queryParam("sound", changeableSound)
                        .queryParam("wingsState", changeableWingsState));
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
