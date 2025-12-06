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

public class DuckDelete extends DuckActionsClient {

    @Test(description = "Проверка удаления уточки")
    @CitrusTest
    public void successDeleteDuck(@Optional @CitrusResource TestCaseRunner runner) {
        BodyCreateDuck duckProperties = new BodyCreateDuck();
        duckProperties.setColor("yellow");
        duckProperties.setHeight(0.03);
        duckProperties.setMaterial("rubber");
        duckProperties.setSound("quack");
        duckProperties.setWingsState(BodyCreateDuck.WingsState.ACTIVE);

        createDuck(runner, duckProperties);
        deleteDuck(runner, getId(runner));

        ResponseMessageDuck expectedResponse = new ResponseMessageDuck();
        expectedResponse.setMessage("Duck is deleted");

        validateResponsePayload(runner, HttpStatus.OK, expectedResponse);
    }
}
