package autotests.tests.duck_action_controller;

import autotests.clients.DuckActionsClient;
import autotests.payloads.BodyCreateDuck;
import autotests.payloads.ResponseMessageDuck;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckActionQuack extends DuckActionsClient {
    String idDuck;
    String[] parametersIdSound = new String[2];

    @Test(description = "Проверка кряканья утки (корректный нечетный id, корректный звук)")
    @CitrusTest
    public void quackDuckWithEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        parametersIdSound = createDuckIdEvenOrOdd(runner, false, "quack"); //Генерация утки; получение параметров id [0] и sound [1]
        quackDuck(runner, parametersIdSound[0], 2, 3); //Запрос - крякать
        String expectedMessage = expectedMessage(parametersIdSound[1], 2, 3); //Генерация желаемого тела ответа

        validateResponseString(runner, HttpStatus.OK, "{\n  \"sound\": \"" + expectedMessage + "\"\n}");
    }

    @Test(description = "Проверка кряканья утки (корректный четный id, корректный звук)")
    @CitrusTest
    public void quackDuckWithOddId(@Optional @CitrusResource TestCaseRunner runner) {
        // TODO: SHIFT-AQA-1
        parametersIdSound = createDuckIdEvenOrOdd(runner, true, "moo"); //Генерация утки; получение параметров id [0] и sound [1]
        quackDuck(runner, parametersIdSound[0], 2, 3); //Запрос - крякать
        String expectedMessage = expectedMessage(parametersIdSound[1], 2, 3); //Генерация желаемого тела ответа

        validateResponseString(runner, HttpStatus.OK, "{\n  \"sound\": \"" + expectedMessage + "\"\n}");
    }

    //создание утки с необходимым id (четным или нечетным)
    //desiredReminderOfDivision = true -> необходимо четное значение id, desiredReminderOfDivision = false -> нечетное значение id
    private String[] createDuckIdEvenOrOdd(TestCaseRunner runner, boolean desiredReminderOfDivision, String sound) {
        do {
            BodyCreateDuck duckProperties = new BodyCreateDuck();
            duckProperties.setColor("yellow");
            duckProperties.setHeight(0.03);
            duckProperties.setMaterial("rubber");
            duckProperties.setSound(sound);
            duckProperties.setWingsState(BodyCreateDuck.WingsState.ACTIVE);

            createDuck(runner, duckProperties);
            idDuck = getId(runner);
        } while (checkIdEven(idDuck) != desiredReminderOfDivision);

        return new String[]{idDuck, sound};
    }

    //Генерация одиночного звука утки
    private String generationSingleDuckSound(String soundDuck, int repetitionCount) {
        StringBuilder singleDuckSoundBuilder = new StringBuilder();

        for (int i = 0; i < repetitionCount; i++) {
            if (i > 0) singleDuckSoundBuilder.append("-");
            singleDuckSoundBuilder.append(soundDuck);
        }

        return singleDuckSoundBuilder.toString();
    }

    //Генерация кряканья утки текстом
    private String expectedMessage(String soundDuck, int repetitionCount, int soundCount) {
        StringBuilder expectedBuilder = new StringBuilder();

        for (int i = 0; i < soundCount; i++) {
            if (i > 0) expectedBuilder.append(", ");
            expectedBuilder.append(generationSingleDuckSound(soundDuck, repetitionCount));
        }

        return expectedBuilder.toString();
    }
}
