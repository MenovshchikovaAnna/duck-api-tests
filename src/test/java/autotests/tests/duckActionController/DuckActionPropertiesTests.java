package autotests.tests.duckActionController;

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

@Epic("Тесты на duck-actions-controller")
@Feature("Показ характеристик уточки")
@Story("Эндпоинт /api/duck/action/properties")
public class DuckActionPropertiesTests extends DuckActionsClient {

    @Test(description = "Проверка отображения характеристик уточки (с четным id)")
    @CitrusTest
    public void showPropertiesDuckEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        BodyCreateDuck duckProperties = new BodyCreateDuck()
                .color("yellow")
                .height(0.03) // TODO: SHIFT-AQA-3
                .material("wood")
                .sound("quack")
                .wingsState(BodyCreateDuck.WingsState.ACTIVE);

        String idDuck = "1234566";
        createDuckWithDatabase(runner, duckProperties, idDuck);
        showPropertiesDuck(runner, idDuck);
        validateResponseResourses(runner, HttpStatus.OK, "DuckActionsTest/getDuckPropertiesTest/validationDuckPropertiesWood.json"); // TODO: SHIFT-AQA-2
    }

    @Test(description = "Проверка отображения характеристик уточки (с нечетным id)")
    @CitrusTest
    public void showPropertiesDuckOddId(@Optional @CitrusResource TestCaseRunner runner) {
        BodyCreateDuck duckProperties = new BodyCreateDuck()
                .color("yellow")
                .height(0.03) // TODO: SHIFT-AQA-3
                .material("rubber")
                .sound("quack")
                .wingsState(BodyCreateDuck.WingsState.ACTIVE);

        String idDuck = "1234567";
        createDuckWithDatabase(runner, duckProperties, idDuck);
        showPropertiesDuck(runner, idDuck);
        validateResponseResourses(runner, HttpStatus.OK, "DuckActionsTest/getDuckPropertiesTest/validationDuckPropertiesRubber.json");
    }
}