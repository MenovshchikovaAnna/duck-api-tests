package autotests.tests.duck_controller;

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

@Epic("Тесты на duck-controller")
@Feature("Удаление уточки")
@Story("Эндпоинт /api/duck/delete")
public class DuckDelete extends DuckActionsClient {

    BodyCreateDuck duckProperties = new BodyCreateDuck() {{
        setColor("yellow");
        setHeight(0.03);
        setMaterial("rubber");
        setSound("quack");
        setWingsState(BodyCreateDuck.WingsState.ACTIVE);
    }};

    ResponseMessageDuck expectedResponse = new ResponseMessageDuck();

    @Test(description = "Проверка удаления уточки")
    @CitrusTest
    public void successDeleteDuck(@Optional @CitrusResource TestCaseRunner runner) {
        String idDuck = "1234567";
        createDuckWithDatabase(runner, duckProperties, idDuck);
        deleteDuck(runner, idDuck);
        expectedResponse.setMessage("Duck is deleted");
        validateResponsePayload(runner, HttpStatus.OK, expectedResponse);
    }
}