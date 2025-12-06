package autotests.tests.duck_controller;

import autotests.clients.DuckActionsClient;
import autotests.payloads.BodyCreateDuck;
import autotests.payloads.ResponseMessageDuck;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckUpdate extends DuckActionsClient {
    String idDuck;

    @Test(description = "Проверка изменения параметров уточки (color и height)")
    @CitrusTest
    public void updateDuckParametersColorHeight(@Optional @CitrusResource TestCaseRunner runner) {
        BodyCreateDuck duckProperties = new BodyCreateDuck();
        duckProperties.setColor("yellow");
        duckProperties.setHeight(0.03);
        duckProperties.setMaterial("rubber");
        duckProperties.setSound("quack");
        duckProperties.setWingsState(BodyCreateDuck.WingsState.ACTIVE);

        createDuck(runner, duckProperties);
        idDuck = getId(runner);
        updateDuck(runner, idDuck, "red", 0.05, "rubber", "quack", "ACTIVE");

        ResponseMessageDuck expectedResponse = new ResponseMessageDuck();
        expectedResponse.setMessage("Duck with id = " + idDuck + " is updated");

        validateResponsePayload(runner, HttpStatus.OK, expectedResponse);
    }

    @Test(description = "Проверка изменения параметров уточки (color и sound)")
    @CitrusTest
    public void updateDuckParametersColorSound(@Optional @CitrusResource TestCaseRunner runner) {
        BodyCreateDuck duckProperties = new BodyCreateDuck();
        duckProperties.setColor("yellow");
        duckProperties.setHeight(0.03);
        duckProperties.setMaterial("rubber");
        duckProperties.setSound("quack");
        duckProperties.setWingsState(BodyCreateDuck.WingsState.ACTIVE);

        createDuck(runner, duckProperties);
        idDuck = getId(runner);
        updateDuck(runner, idDuck, "blue", 0.05, "rubber", "quack-quack", "ACTIVE");

        ResponseMessageDuck expectedResponse = new ResponseMessageDuck();
        expectedResponse.setMessage("Duck with id = " + idDuck + " is updated");

        validateResponsePayload(runner, HttpStatus.OK, expectedResponse);
    }
}
