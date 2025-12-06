package autotests.tests.duck_controller;

import autotests.clients.DuckActionsClient;
import autotests.payloads.BodyCreateDuck;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.JsonPathSupport.jsonPath;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckCreate extends DuckActionsClient {

    @Test(description = "Проверка создания уточки с материалом rubber")
    @CitrusTest
    public void createDuckMaterialRubber(@Optional @CitrusResource TestCaseRunner runner) {
        BodyCreateDuck duckProperties = new BodyCreateDuck();
        duckProperties.setColor("yellow");
        duckProperties.setHeight(0.03);
        duckProperties.setMaterial("rubber");
        duckProperties.setSound("quack");
        duckProperties.setWingsState(BodyCreateDuck.WingsState.ACTIVE);

        createDuck(runner, duckProperties);
        validateResponse(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
    }

    @Test(description = "Проверка создания уточки с материалом wood")
    @CitrusTest
    public void createDuckMaterialWood(@Optional @CitrusResource TestCaseRunner runner) {
        BodyCreateDuck duckProperties = new BodyCreateDuck();
        duckProperties.setColor("yellow");
        duckProperties.setHeight(0.03);
        duckProperties.setMaterial("wood");
        duckProperties.setSound("quack");
        duckProperties.setWingsState(BodyCreateDuck.WingsState.ACTIVE);

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
                                .expression("$.color", expectedColor)
                                .expression("$.height", String.valueOf(expectedHeight))
                                .expression("$.material", expectedMaterial)
                                .expression("$.sound", expectedSound)
                                .expression("$.wingsState", expectedWingsState)));
    }
}
