package autotests.tests.duckController;

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
public class DuckDeleteTests extends DuckActionsClient {
    @Test(description = "Проверка удаления уточки")
    @CitrusTest
    public void successDeleteDuck(@Optional @CitrusResource TestCaseRunner runner) {
        BodyCreateDuck duckProperties = new BodyCreateDuck()
                .color("yellow")
                .height(0.03)
                .material("rubber")
                .sound("quack")
                .wingsState(BodyCreateDuck.WingsState.ACTIVE);

        String idDuck = "1234567";
        createDuckWithDatabase(runner, duckProperties, idDuck);
        deleteDuck(runner, idDuck);

        ResponseMessageDuck expectedResponse = new ResponseMessageDuck()
                .message("Duck is deleted");

        selectVerifyDuckNotInDatabase(runner, idDuck);
        validateResponsePayload(runner, HttpStatus.OK, expectedResponse);
    }
}
