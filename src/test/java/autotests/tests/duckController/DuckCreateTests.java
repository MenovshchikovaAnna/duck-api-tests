package autotests.tests.duckController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.BodyCreateDuck;
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

public class DuckCreateTests extends DuckActionsClient {
    @Test(description = "Проверка создания уточки с материалом rubber")
    @CitrusTest
    public void createDuckMaterialRubber(@Optional @CitrusResource TestCaseRunner runner) {
        BodyCreateDuck duckProperties = new BodyCreateDuck()
                .color("yellow")
                .height(0.03)
                .material("rubber")
                .sound("quack")
                .wingsState(BodyCreateDuck.WingsState.ACTIVE);

        createDuck(runner, duckProperties);
        validateResponse(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
    }

    @Test(description = "Проверка создания уточки с материалом wood")
    @CitrusTest
    public void createDuckMaterialWood(@Optional @CitrusResource TestCaseRunner runner) {
        BodyCreateDuck duckProperties = new BodyCreateDuck()
                .color("yellow")
                .height(0.03)
                .material("wood")
                .sound("quack")
                .wingsState(BodyCreateDuck.WingsState.ACTIVE);

        createDuck(runner, duckProperties);
        validateResponse(runner, "yellow", 0.03, "wood", "quack", "ACTIVE");
    }

    //валидация ответа для create (отличается от остальных, поэтому отдельно и private)
    private void validateResponse(TestCaseRunner runner, String expectedColor, double expectedHeight, String expectedMaterial, String expectedSound, String expectedWingsState) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .validate(jsonPath()
                                .expression("$.id", "@isNumber()@")
                                .expression("$.id", "@greaterThan(0)@")
                                .expression("$.id", "@notEmpty()@")
                                .expression("$.id", "@notNull()@")

                                .expression("$.color", expectedColor)
                                .expression("$.height", String.valueOf(expectedHeight))
                                .expression("$.material", expectedMaterial)
                                .expression("$.sound", expectedSound)
                                .expression("$.wingsState", expectedWingsState)));
    }
}
