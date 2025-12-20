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
@Feature("Обновление характеристик уточки")
@Story("Эндпоинт /api/duck/update")
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

        String idDuck = "1234567";

        createDuckWithDatabase(runner, duckProperties, idDuck);

        updateDuck(runner,
                idDuck,
                "red",
                0.05,
                duckProperties.material(),
                duckProperties.sound(),
                duckProperties.wingsState().name());

        ResponseMessageDuck expectedResponse = new ResponseMessageDuck()
                .message("Duck with id = " + idDuck + " is updated");
        validateResponsePayload(runner, HttpStatus.OK, expectedResponse);

        selectCheckingDuckById(runner,
                idDuck,
                "red",
                0.05,
                duckProperties.material(),
                duckProperties.sound(),
                duckProperties.wingsState().name());
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

        String idDuck = "1234567";

        createDuckWithDatabase(runner, duckProperties, idDuck);

        updateDuck(runner,
                idDuck,
                "blue",
                duckProperties.height(),
                duckProperties.material(),
                "quack-quack",
                duckProperties.wingsState().name());

        ResponseMessageDuck expectedResponse = new ResponseMessageDuck()
                .message("Duck with id = " + idDuck + " is updated");
        validateResponsePayload(runner, HttpStatus.OK, expectedResponse);

        selectCheckingDuckById(runner,
                idDuck,
                "blue",
                duckProperties.height(),
                duckProperties.material(),
                "quack-quack",
                duckProperties.wingsState().name());

    }
}
