package autotests.tests.duckActionController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.BodyCreateDuck;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

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

        String idDuckActual = createDuckIdEvenOrOdd(runner, true, duckProperties);
        showPropertiesDuck(runner, idDuckActual);

        //Валидация ответа из папки Resources
        // TODO: SHIFT-AQA-2
        validateResponseResourses(runner, HttpStatus.OK, "duckActionsTest/validationDuckPropertiesWood.json");
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

        String idDuckActual = createDuckIdEvenOrOdd(runner, false, duckProperties);
        showPropertiesDuck(runner, idDuckActual);

        //Валидация ответа из папки Resources
        validateResponseResourses(runner, HttpStatus.OK, "duckActionsTest/validationDuckPropertiesRubber.json");
    }

    //создание записи с необходимым id (четным или нечетным)
    //desiredReminderOfDivision = true -> необходимо четное значение id, desiredReminderOfDivision = false -> нечетное значение id
    private String createDuckIdEvenOrOdd(TestCaseRunner runner, boolean desiredReminderOfDivision, BodyCreateDuck duckProperties) {
        String idDuck;

        do {
            createDuck(runner, duckProperties);
            idDuck = getId(runner);
        } while ((Integer.parseInt(idDuck) % 2 == 0) != desiredReminderOfDivision);

        return idDuck;
    }
}