package autotests.tests.duckActionController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.BodyCreateDuck;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckActionSwimTests extends DuckActionsClient {
    @Test(description = "Проверка плаванья уточки с существующим id")
    @CitrusTest
    public void successSwim(@Optional @CitrusResource TestCaseRunner runner) {
        BodyCreateDuck duckProperties = new BodyCreateDuck()
                .color("yellow")
                .height(0.03)
                .material("rubber")
                .sound("quack")
                .wingsState(BodyCreateDuck.WingsState.ACTIVE);
        createDuck(runner, duckProperties);
        duckSwim(runner, getId(runner));
        // TODO: SHIFT-AQA-4 ({\n \"message\": \"I'm swimming\"\n}")
        // TODO: SHIFT-AQA-5 HttpStatus.NOT_FOUND
        validateResponseString(runner, HttpStatus.NOT_FOUND, "{\n \"message\": \"Paws are not found ((((\"\n}");
    }

    @Test(description = "Проверка плаванья уточки с несуществующим id")
    @CitrusTest
    public void invalidIdSwim(@Optional @CitrusResource TestCaseRunner runner) {
        duckSwim(runner, "999999");
        // TODO: SHIFT-AQA-5 HttpStatus.NOT_FOUND
        validateResponseString(runner, HttpStatus.NOT_FOUND, "{\n \"message\": \"Paws are not found ((((\"\n}");
    }
}