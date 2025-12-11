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

import static com.consol.citrus.actions.EchoAction.Builder.echo;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;

@Epic("Тесты на duck-controller")
@Feature("Обновление характеристик уточки")
@Story("Эндпоинт /api/duck/update")
public class DuckUpdate extends DuckActionsClient {
    BodyCreateDuck duckProperties = new BodyCreateDuck() {{
        setColor("yellow");
        setHeight(0.03);
        setMaterial("rubber");
        setSound("quack");
        setWingsState(BodyCreateDuck.WingsState.ACTIVE);
    }};

    ResponseMessageDuck expectedResponse = new ResponseMessageDuck();

    @Test(description = "Проверка изменения параметров уточки (color и height)")
    @CitrusTest
    public void updateDuckParametersColorHeight(@Optional @CitrusResource TestCaseRunner runner) {
        String idDuck = "1234567";

        createDuckWithDatabase(runner, duckProperties, idDuck);

        updateDuck(runner,
                idDuck,
                "red",
                0.05,
                duckProperties.getMaterial(),
                duckProperties.getSound(),
                duckProperties.getWingsState().name());

        expectedResponse.setMessage("Duck with id = " + idDuck + " is updated");
        validateResponsePayload(runner, HttpStatus.OK, expectedResponse);

        selectCheckingDuckById(runner,
                idDuck,
                "red",
                0.05,
                duckProperties.getMaterial(),
                duckProperties.getSound(),
                duckProperties.getWingsState().name());
    }

    @Test(description = "Проверка изменения параметров уточки (color и sound)")
    @CitrusTest
    public void updateDuckParametersColorSound(@Optional @CitrusResource TestCaseRunner runner) {
        String idDuck = "1234567";

        createDuckWithDatabase(runner, duckProperties, idDuck);

        updateDuck(runner,
                idDuck,
                "blue",
                duckProperties.getHeight(),
                duckProperties.getMaterial(),
                "quack-quack",
                duckProperties.getWingsState().name());

        expectedResponse.setMessage("Duck with id = " + idDuck + " is updated");
        validateResponsePayload(runner, HttpStatus.OK, expectedResponse);

        selectCheckingDuckById(runner,
                idDuck,
                "blue",
                duckProperties.getHeight(),
                duckProperties.getMaterial(),
                "quack-quack",
                duckProperties.getWingsState().name());
    }
}