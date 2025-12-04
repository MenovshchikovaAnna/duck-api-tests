package autotests.duck_action_controller;

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

public class DuckActionQuack extends TestNGCitrusSpringSupport {
    String url = "http://localhost:2222";
    String idDuck;
    String[] parametersIdSound = new String[2];

    @Test(description = "Проверка кряканья утки (корректный нечетный id, корректный звук)")
    @CitrusTest
    public void quackDuckWithEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        parametersIdSound = createDuckIdEvenOrOdd(runner, false); //Генерация утки; получение параметров id [0] и sound [1]
        quackDuck(runner, parametersIdSound[0], 2, 3); //Запрос - крякать
        String expectedMessage = expectedMessage(parametersIdSound[1], 2, 3); //Генерация желаемого тела ответа
        validateResponseActiveWingsFly(runner, "{\n" +
                "  \"sound\": \"" + expectedMessage + "\"\n}");
    }

    @Test(description = "Проверка кряканья утки (корректный четный id, корректный звук)")
    @CitrusTest
    public void quackDuckWithOddId(@Optional @CitrusResource TestCaseRunner runner) {
        parametersIdSound = createDuckIdEvenOrOdd(runner, true); //Генерация утки; получение параметров id [0] и sound [1]
        quackDuck(runner, parametersIdSound[0], 2, 3); //Запрос - крякать
        String expectedMessage = expectedMessage(parametersIdSound[1], 2, 3); //Генерация желаемого тела ответа
        validateResponseActiveWingsFly(runner, "{\n" +
                "  \"sound\": \"" + expectedMessage + "\"\n}");
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

        runner.run(new AbstractTestAction() {
            @Override
            public void doExecute(TestContext context) {
                idDuck[0] = context.getVariable("duckId");
            }
        });

        return idDuck[0];
    }

    //Проверка, что id четный
    public boolean checkIdEven(String idDuck) {
        return Integer.parseInt(idDuck) % 2 == 0;
    }

    //создание утки с необходимым id (четным или нечетным)
    //desiredReminderOfDivision = true -> необходимо четное значение id, desiredReminderOfDivision = false -> нечетное значение id
    public String[] createDuckIdEvenOrOdd(TestCaseRunner runner, boolean desiredReminderOfDivision) {
        do {
            createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
            idDuck = getIdCreatedDuck(runner);
        } while (checkIdEven(idDuck) != desiredReminderOfDivision);

        return new String[]{idDuck, "quack"};
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

    //Генерация одиночного звука утки
    public String generationSingleDuckSound(String soundDuck, int repetitionCount) {
        StringBuilder singleDuckSoundBuilder = new StringBuilder();

        for (int i = 0; i < repetitionCount; i++) {
            if (i > 0) singleDuckSoundBuilder.append("-");
            singleDuckSoundBuilder.append(soundDuck);
        }

        return singleDuckSoundBuilder.toString();
    }

    //Генерация кряканья утки текстом
    public String expectedMessage(String soundDuck, int repetitionCount, int soundCount) {
        StringBuilder expectedBuilder = new StringBuilder();

        for (int i = 0; i < soundCount; i++) {
            if (i > 0) expectedBuilder.append(", ");
            expectedBuilder.append(generationSingleDuckSound(soundDuck, repetitionCount));
        }

        return expectedBuilder.toString();
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
