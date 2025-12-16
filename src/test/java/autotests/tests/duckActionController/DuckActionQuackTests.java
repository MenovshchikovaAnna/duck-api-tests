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
        BodyCreateDuck duckProperties = new BodyCreateDuck()
                .color("yellow")
                .height(0.03)
                .material("rubber")
                .sound("quack")
                .wingsState(BodyCreateDuck.WingsState.ACTIVE);
        String idDuck = "1234567";
        createDuckWithDatabase(runner, duckProperties, idDuck);
        quackDuck(runner, idDuck, 2, 3); //Запрос - крякать
        validateResponseString(runner, HttpStatus.OK, "{\n  \"sound\": \"quack-quack, quack-quack, quack-quack\"\n}");}

    @Test(description = "Проверка кряканья утки (корректный четный id, корректный звук)")
    @CitrusTest
    public void quackDuckWithOddId(@Optional @CitrusResource TestCaseRunner runner) {
        BodyCreateDuck duckProperties = new BodyCreateDuck()
                .color("yellow")
                .height(0.03)
                .material("rubber")
                .sound("quack")
                .wingsState(BodyCreateDuck.WingsState.ACTIVE);

        String idDuck = "1234566";
        createDuckWithDatabase(runner, duckProperties, idDuck);
        quackDuck(runner, idDuck, 2, 3); //Запрос - крякать
        // TODO: SHIFT-AQA-1
        validateResponseString(runner, HttpStatus.OK, "{\n  \"sound\": \"moo-moo, moo-moo, moo-moo\"\n}");
    }
}
