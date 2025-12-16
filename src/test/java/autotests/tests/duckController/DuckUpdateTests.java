package autotests.tests.duckController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.BodyCreateDuck;
import autotests.payloads.ResponseMessageDuck;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckUpdateTests extends DuckActionsClient {
    @Test(description = "Проверка изменения параметров уточки (color и height)")
    @CitrusTest
    public void updateDuckParametersColorHeight(@Optional @CitrusResource TestCaseRunner runner) {
        BodyCreateDuck duckProperties = new BodyCreateDuck()
                .color("yellow")
                .height(0.03)
                .material("rubber")
                .sound("quack")
                .wingsState(BodyCreateDuck.WingsState.ACTIVE);

        createDuck(runner, duckProperties);
        String idDuck = getId(runner);
        updateDuck(runner, idDuck, "red", 0.05, "rubber", "quack", "ACTIVE");

        ResponseMessageDuck expectedResponse = new ResponseMessageDuck()
                .message("Duck with id = " + idDuck + " is updated");

        validateResponsePayload(runner, HttpStatus.OK, expectedResponse);
    }

    @Test(description = "Проверка изменения параметров уточки (color и sound)")
    @CitrusTest
    public void updateDuckParametersColorSound(@Optional @CitrusResource TestCaseRunner runner) {
        BodyCreateDuck duckProperties = new BodyCreateDuck()
                .color("yellow")
                .height(0.03)
                .material("rubber")
                .sound("quack")
                .wingsState(BodyCreateDuck.WingsState.ACTIVE);

        createDuck(runner, duckProperties);
        String idDuck = getId(runner);
        updateDuck(runner, idDuck, "blue", 0.05, "rubber", "quack-quack", "ACTIVE");

        ResponseMessageDuck expectedResponse = new ResponseMessageDuck()
                .message("Duck with id = " + idDuck + " is updated");

        validateResponsePayload(runner, HttpStatus.OK, expectedResponse);
    }
}
