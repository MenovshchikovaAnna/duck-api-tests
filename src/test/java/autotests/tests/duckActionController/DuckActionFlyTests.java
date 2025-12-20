package autotests.tests.duckActionController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.BodyCreateDuck;
import autotests.payloads.ResponseMessageDuck;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

@Epic("Тесты на duck-actions-controller")
@Feature("Полет уточки")
@Story("Эндпоинт /api/duck/action/fly")
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

        String idDuck = "1234567";
        createDuckWithDatabase(runner, duckProperties, idDuck);
        duckFly(runner, idDuck);

        ResponseMessageDuck expectedResponse = new ResponseMessageDuck()
                .message("I am flying :)");

        validateResponsePayload(runner, HttpStatus.OK, expectedResponse);
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

        String idDuck = "1234567";
        createDuckWithDatabase(runner, duckProperties, idDuck);
        duckFly(runner, idDuck);

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

        String idDuck = "1234567";
        createDuckWithDatabase(runner, duckProperties, idDuck);
        duckFly(runner, idDuck);

        ResponseMessageDuck expectedResponse = new ResponseMessageDuck()
                .message("Wings are not detected :(");

        validateResponsePayload(runner, HttpStatus.OK, expectedResponse);
    }
}