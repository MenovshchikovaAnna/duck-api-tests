package autotests.tests.duckActionController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.BodyCreateDuck;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckActionQuackTests extends DuckActionsClient {

    @Test(description = "Проверка кряканья утки (корректный нечетный id, корректный звук)")
    @CitrusTest
    public void quackDuckWithEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        String parameterId = createDuckIdEvenOrOdd(runner, false, "quack"); //Генерация утки; получение параметров id [0] и sound [1]
        quackDuck(runner, parameterId, 2, 3); //Запрос - крякать
        validateResponseString(runner, HttpStatus.OK, "{\n  \"sound\": \"quack-quack, quack-quack, quack-quack\"\n}");
    }

    @Test(description = "Проверка кряканья утки (корректный четный id, корректный звук)")
    @CitrusTest
    public void quackDuckWithOddId(@Optional @CitrusResource TestCaseRunner runner) {
        // TODO: SHIFT-AQA-1
        String parameterId = createDuckIdEvenOrOdd(runner, true, "moo"); //Генерация утки; получение параметров id [0] и sound [1]
        quackDuck(runner, parameterId, 2, 3); //Запрос - крякать
        // TODO: SHIFT-AQA-1
        validateResponseString(runner, HttpStatus.OK, "{\n  \"sound\": \"moo-moo, moo-moo, moo-moo\"\n}");
    }

    //создание утки с необходимым id (четным или нечетным)
    //desiredReminderOfDivision = true -> необходимо четное значение id, desiredReminderOfDivision = false -> нечетное значение id
    private String createDuckIdEvenOrOdd(TestCaseRunner runner, boolean desiredReminderOfDivision, String sound) {
        String idDuck;

        do {
            BodyCreateDuck duckProperties = new BodyCreateDuck()
                    .color("yellow")
                    .height(0.03)
                    .material("rubber")
                    .sound(sound)
                    .wingsState(BodyCreateDuck.WingsState.ACTIVE);

            createDuck(runner, duckProperties);
            idDuck = getId(runner);
        } while ((Integer.parseInt(idDuck) % 2 == 0) != desiredReminderOfDivision);

        return idDuck;
    }
}
