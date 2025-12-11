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

@Epic("Тесты на duck-actions-controller")
@Feature("Кряканье уточки")
@Story("Эндпоинт /api/duck/action/quack")
public class DuckActionQuack extends DuckActionsClient {
    String idDuck;

    BodyCreateDuck duckProperties = new BodyCreateDuck() {{
        setColor("yellow");
        setHeight(0.03);
        setMaterial("rubber");
        setSound("quack");
        setWingsState(BodyCreateDuck.WingsState.ACTIVE);
    }};

    @Test(description = "Проверка кряканья утки (корректный нечетный id, корректный звук)")
    @CitrusTest
    public void quackDuckWithEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        String idDuck = "1234567";
        createDuckWithDatabase(runner, duckProperties, idDuck);
        quackDuck(runner, idDuck, 2, 3); //Запрос - крякать
        String expectedMessage = expectedMessage(duckProperties.getSound(), 2, 3); //Генерация желаемого тела ответа
        validateResponseString(runner, HttpStatus.OK, "{\n  \"sound\": \"" + expectedMessage + "\"\n}");
    }

    @Test(description = "Проверка кряканья утки (корректный четный id, корректный звук)")
    @CitrusTest
    public void quackDuckWithOddId(@Optional @CitrusResource TestCaseRunner runner) {
        String soundDuck = "moo"; // TODO: SHIFT-AQA-1
        String idDuck = "1234566";
        createDuckWithDatabase(runner, duckProperties, idDuck);
        quackDuck(runner, idDuck, 2, 3); //Запрос - крякать
        String expectedMessage = expectedMessage(soundDuck, 2, 3); //Генерация желаемого тела ответа
        validateResponseString(runner, HttpStatus.OK, "{\n  \"sound\": \"" + expectedMessage + "\"\n}");
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
