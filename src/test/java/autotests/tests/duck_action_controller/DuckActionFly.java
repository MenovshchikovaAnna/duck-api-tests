package autotests.tests.duck_action_controller;

import autotests.clients.DuckActionsClient;
import autotests.payloads.BodyCreateDuck;
import autotests.payloads.ResponseMessageDuck;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckActionFly extends DuckActionsClient {
    @Test(description = "Проверка полета уточки с крыльями Active")
    @CitrusTest
    public void activeWingsFly(@Optional @CitrusResource TestCaseRunner runner) {

        //BodyCreateDuck duckProperties = new BodyCreateDuck()
        BodyCreateDuck duckProperties = new BodyCreateDuck();
        duckProperties.setColor("yellow");
        duckProperties.setHeight(0.03);
        duckProperties.setMaterial("rubber");
        duckProperties.setSound("quack");
        duckProperties.setWingsState(BodyCreateDuck.WingsState.ACTIVE);

        createDuck(runner, duckProperties);
        duckFly(runner, getId(runner));

        //Передача ответа String'ой
        validateResponseString(runner, HttpStatus.OK, "{\n \"message\": \"I am flying :)\"\n}");
    }

    @Test(description = "Проверка полета уточки с крыльями Fixed")
    @CitrusTest
    public void fixedWingsFly(@Optional @CitrusResource TestCaseRunner runner) {
        BodyCreateDuck duckProperties = new BodyCreateDuck();
        duckProperties.setColor("yellow");
        duckProperties.setHeight(0.03);
        duckProperties.setMaterial("rubber");
        duckProperties.setSound("quack");
        duckProperties.setWingsState(BodyCreateDuck.WingsState.FIXED);

        createDuck(runner, duckProperties);
        duckFly(runner, getId(runner));

        ResponseMessageDuck expectedResponse = new ResponseMessageDuck();
        expectedResponse.setMessage("I can not fly :C");

        validateResponsePayload(runner, HttpStatus.OK, expectedResponse);
    }

    @Test(description = "Проверка полета уточки с крыльями Undefined")
    @CitrusTest
    public void undefinedWingsFly(@Optional @CitrusResource TestCaseRunner runner) {
        BodyCreateDuck duckProperties = new BodyCreateDuck();
        duckProperties.setColor("yellow");
        duckProperties.setHeight(0.03);
        duckProperties.setMaterial("rubber");
        duckProperties.setSound("quack");
        duckProperties.setWingsState(BodyCreateDuck.WingsState.UNDEFINED);

        createDuck(runner, duckProperties);
        duckFly(runner, getId(runner));

        ResponseMessageDuck expectedResponse = new ResponseMessageDuck();
        expectedResponse.setMessage("Wings are not detected :(");

        validateResponsePayload(runner, HttpStatus.OK, expectedResponse);
    }
}