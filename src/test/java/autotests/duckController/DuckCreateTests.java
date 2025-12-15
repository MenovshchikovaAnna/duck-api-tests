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
import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

public class DuckCreateTests extends TestNGCitrusSpringSupport {
    String url = "http://localhost:2222";

    @Test(description = "Проверка создания уточки с материалом rubber")
    @CitrusTest
    public void createDuckMaterialRubber(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
        validateResponseActiveWingsFly(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
    }

    @Test(description = "Проверка создания уточки с материалом wood")
    @CitrusTest
    public void createDuckMaterialWood(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "wood", "quack", "ACTIVE");
        validateResponseActiveWingsFly(runner, "yellow", 0.03, "wood", "quack", "ACTIVE");
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

    //валидация ответа
    public void validateResponseActiveWingsFly(TestCaseRunner runner, String expectedColor, double expectedHeight, String expectedMaterial, String expectedSound, String expectedWingsState) {
        runner.$(
                http()
                        .client(url)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .validate(jsonPath()
                                .expression("$.id", "@isNumber()@")
                                .expression("$.color", expectedColor)
                                .expression("$.height", String.valueOf(expectedHeight))
                                .expression("$.material", expectedMaterial)
                                .expression("$.sound", expectedSound)
                                .expression("$.wingsState", expectedWingsState)));
    }
}
