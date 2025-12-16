package autotests.tests.duckActionController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.BodyCreateDuck;
import autotests.payloads.ResponseMessageDuck;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckActionFlyTests extends DuckActionsClient {
    @Test(description = "Проверка полета уточки с крыльями Active")
    @CitrusTest
    public void activeWingsFly(@Optional @CitrusResource TestCaseRunner runner) {
        BodyCreateDuck duckProperties = new BodyCreateDuck()
                .color("yellow")
                .height(0.03)
                .material("rubber")
                .sound("quack")
                .wingsState(BodyCreateDuck.WingsState.ACTIVE);

        createDuck(runner, duckProperties);
        duckFly(runner, getId(runner));
        validateResponseString(runner, HttpStatus.OK, "{\n \"message\": \"I am flying :)\"\n}");
    }


    @Test(description = "Проверка полета уточки с крыльями Fixed")
    @CitrusTest
    public void fixedWingsFly(@Optional @CitrusResource TestCaseRunner runner) {
        BodyCreateDuck duckProperties = new BodyCreateDuck()
                .color("yellow")
                .height(0.03)
                .material("rubber")
                .sound("quack")
                .wingsState(BodyCreateDuck.WingsState.FIXED);

        createDuck(runner, duckProperties);
        duckFly(runner, getId(runner));

        ResponseMessageDuck expectedResponse = new ResponseMessageDuck()
                .message("I can not fly :C");

        validateResponsePayload(runner, HttpStatus.OK, expectedResponse);
    }

    @Test(description = "Проверка полета уточки с крыльями Undefined")
    @CitrusTest
    public void undefinedWingsFly(@Optional @CitrusResource TestCaseRunner runner) {
        BodyCreateDuck duckProperties = new BodyCreateDuck()
                .color("yellow")
                .height(0.03)
                .material("rubber")
                .sound("quack")
                .wingsState(BodyCreateDuck.WingsState.UNDEFINED);

        createDuck(runner, duckProperties);
        duckFly(runner, getId(runner));

        ResponseMessageDuck expectedResponse = new ResponseMessageDuck()
                .message("Wings are not detected :(");

        validateResponsePayload(runner, HttpStatus.OK, expectedResponse);
    }
}