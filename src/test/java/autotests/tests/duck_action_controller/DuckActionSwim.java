package autotests.tests.duck_action_controller;

import autotests.clients.DuckActionsClient;
import autotests.payloads.BodyCreateDuck;
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
import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Epic("Тесты на duck-actions-controller")
@Feature("Плаванье уточки")
@Story("Эндпоинт /api/duck/action/swim")
public class DuckActionSwim extends DuckActionsClient {
    BodyCreateDuck duckProperties = new BodyCreateDuck() {{
        setColor("yellow");
        setHeight(0.03);
        setMaterial("rubber");
        setSound("quack");
        setWingsState(BodyCreateDuck.WingsState.ACTIVE);
    }};

    @Test(description = "Проверка плаванья уточки с существующим id")
    @CitrusTest
    public void successSwim(@Optional @CitrusResource TestCaseRunner runner) {
        String idDuck = "1234567";
        createDuckWithDatabase(runner, duckProperties, idDuck);
        duckSwim(runner, idDuck);
        // TODO: SHIFT-AQA-4 ({\n \"message\": \"I'm swimming\"\n}")
        validateResponseString(runner, HttpStatus.NOT_FOUND, "{\n \"message\": \"Paws are not found ((((\"\n}");
    }

    @Test(description = "Проверка плаванья уточки с несуществующим id")
    @CitrusTest
    public void invalidIdSwim(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=999999");
        } catch (Exception e) {
            runner.$(echo("Утка с ID = 999999 не существует"));
        }

        duckSwim(runner, "999999");
        validateResponseString(runner, HttpStatus.NOT_FOUND, "{\n \"message\": \"Paws are not found ((((\"\n}");
    }
}