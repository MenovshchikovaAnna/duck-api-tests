package autotests.duckActionController;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.actions.AbstractTestAction;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

public class DuckActionQuackTests extends TestNGCitrusSpringSupport {
    String url = "http://localhost:2222";

    @Test(description = "Проверка кряканья утки (корректный нечетный id, корректный звук)")
    @CitrusTest
    public void quackDuckWithEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        String parametersIdSound = createDuckIdEvenOrOdd(runner, false); //Генерация утки; получение параметров id [0] и sound [1]
        quackDuck(runner, parametersIdSound, 2, 3); //Запрос - крякать
        validateResponseActiveWingsFly(runner, "{\n" +
                "  \"sound\": \"quack-quack, quack-quack, quack-quack\"\n}");
    }

    @Test(description = "Проверка кряканья утки (корректный четный id, корректный звук)")
    @CitrusTest
    public void quackDuckWithOddId(@Optional @CitrusResource TestCaseRunner runner) {
        String parametersIdSound = createDuckIdEvenOrOdd(runner, true); //Генерация утки; получение параметров id [0] и sound [1]
        quackDuck(runner, parametersIdSound, 2, 3); //Запрос - крякать
        // TODO: SHIFT-AQA-1
        validateResponseActiveWingsFly(runner, "{\n" +
                "  \"sound\": \"moo-moo, moo-moo, moo-moo\"\n}");
    }

    //создание уточки
    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        runner.$(
                http()
                        .client(url)
                        .send()
                        .post("/api/duck/create")
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body("{\n" +
                                "\"color\": \"" + color + "\",\n" +
                                "\"height\": " + height + ",\n" +
                                "\"material\": \"" + material + "\",\n" +
                                "\"sound\": \"" + sound + "\",\n" +
                                "\"wingsState\": \"" + wingsState + "\"}"));
    }

    //Получение id созданной уточки
    public String getIdCreatedDuck(TestCaseRunner runner) {
        final String[] idDuck = new String[1];

        runner.$(
                http()
                        .client(url)
                        .receive()
                        .response()
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .extract(fromBody().expression("$.id", "duckId")));

        runner.$(context -> idDuck[0] = context.getVariable("duckId"));

        return idDuck[0];
    }

    //создание утки с необходимым id (четным или нечетным)
    //desiredReminderOfDivision = true -> необходимо четное значение id, desiredReminderOfDivision = false -> нечетное значение id
    public String createDuckIdEvenOrOdd(TestCaseRunner runner, boolean desiredReminderOfDivision) {
        String idDuck;

        do {
            createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
            idDuck = getIdCreatedDuck(runner);
        } while (Integer.parseInt(idDuck) % 2 == 0 != desiredReminderOfDivision);

        return idDuck;
    }

    //Запрос - крякать
    public void quackDuck(TestCaseRunner runner, String idDuck, int repetitionCount, int soundCount) {
        runner.$(
                http()
                        .client(url)
                        .send()
                        .get("/api/duck/action/quack")
                        .queryParam("id", idDuck)
                        .queryParam("repetitionCount", String.valueOf(repetitionCount))
                        .queryParam("soundCount", String.valueOf(soundCount)));
    }

    //валидация ответа
    public void validateResponseActiveWingsFly(TestCaseRunner runner, String responseMessage) {
        runner.$(
                http()
                        .client(url)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(responseMessage));
    }
}
