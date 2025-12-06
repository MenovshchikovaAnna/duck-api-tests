package autotests.tests.duck_action_controller;

import autotests.clients.DuckActionsClient;
import autotests.payloads.BodyCreateDuck;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckActionSwim extends DuckActionsClient {

    @Test(description = "Проверка плаванья уточки с существующим id")
    @CitrusTest
    public void successSwim(@Optional @CitrusResource TestCaseRunner runner) {

        BodyCreateDuck duckProperties = new BodyCreateDuck();
        duckProperties.setColor("yellow");
        duckProperties.setHeight(0.03);
        duckProperties.setMaterial("rubber");
        duckProperties.setSound("quack");
        duckProperties.setWingsState(BodyCreateDuck.WingsState.ACTIVE);

        createDuck(runner, duckProperties);
        duckSwim(runner, getId(runner));
        // TODO: SHIFT-AQA-4 ({\n \"message\": \"I'm swimming\"\n}")
        validateResponseString(runner, HttpStatus.NOT_FOUND, "{\n \"message\": \"Paws are not found ((((\"\n}");
    }

    @Test(description = "Проверка плаванья уточки с несуществующим id")
    @CitrusTest
    public void invalidIdSwim(@Optional @CitrusResource TestCaseRunner runner) {
        duckSwim(runner, "999999");
        validateResponseString(runner, HttpStatus.NOT_FOUND, "{\n \"message\": \"Paws are not found ((((\"\n}");
    }
}