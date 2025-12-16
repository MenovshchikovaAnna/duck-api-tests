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
import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

public class DuckActionPropertiesTests extends TestNGCitrusSpringSupport {
    String url = "http://localhost:2222";

    @Test(description = "Проверка отображения характеристик уточки (с четным id)")
    @CitrusTest
    public void showPropertiesDuckEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        String idDuckActual = createDuckIdEvenOrOdd(runner, true, "wood");
        showPropertiesDuck(runner, idDuckActual);
        String bodyDuck = "{}"; //TODO: SHIFT-AQA-2
        validateResponseActiveWingsFly(runner, bodyDuck);
    }

    @Test(description = "Проверка отображения характеристик уточки (с нечетным id)")
    @CitrusTest
    public void showPropertiesDuckOddId(@Optional @CitrusResource TestCaseRunner runner) {
        String idDuckActual = createDuckIdEvenOrOdd(runner, false, "rubber");
        showPropertiesDuck(runner, idDuckActual);

         String bodyDuck = "{\n" +
                "  \"color\": \"yellow\",\n" +
                "  \"height\": 3.0,\n" + // TODO: SHIFT-AQA-3
                "  \"material\": \"rubber\",\n" +
                "  \"sound\": \"quack\",\n" +
                "  \"wingsState\": \"ACTIVE\"\n" +
                "}";
        validateResponseActiveWingsFly(runner, bodyDuck);
    }

    //создание утки
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
    public String createDuckIdEvenOrOdd(TestCaseRunner runner, boolean desiredReminderOfDivision, String expectedMaterial) {
        String idDuck;

        do {
            createDuck(runner, "yellow", 0.03, expectedMaterial, "quack", "ACTIVE");
            idDuck = getIdCreatedDuck(runner);
        } while ((Integer.parseInt(idDuck) % 2 == 0) != desiredReminderOfDivision);

        return idDuck;
    }

    //Запрос - показать характеристики
    public void showPropertiesDuck(TestCaseRunner runner, String idDuck) {
        runner.$(
                http()
                        .client(url)
                        .send()
                        .get("/api/duck/action/properties")
                        .queryParam("id", idDuck));
    }

    //валидация ответа
    public void validateResponseActiveWingsFly(TestCaseRunner runner, String bodyDuck) {
        runner.$(
                http()
                        .client(url)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(bodyDuck));
    }
}