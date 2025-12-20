package autotests.tests.duckController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.BodyCreateDuck;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.CitrusParameters;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

@Epic("Тесты на duck-controller")
@Feature("Параметризированное создание уточки")
@Story("Эндпоинт /api/duck/create")
public class DuckCreateTests extends DuckActionsClient {
    BodyCreateDuck duckProperties1 = new BodyCreateDuck()
            .color("yellow")
            .height(0.08)
            .material("plastic")
            .sound("crya")
            .wingsState(BodyCreateDuck.WingsState.ACTIVE);
    BodyCreateDuck duckProperties2 = new BodyCreateDuck()
            .color("green")
            .height(0.05)
            .material("wood")
            .sound("quack-quack")
            .wingsState(BodyCreateDuck.WingsState.FIXED);
    BodyCreateDuck duckProperties3 = new BodyCreateDuck()
            .color("red")
            .height(0.60)
            .material("rubber")
            .sound("woof")
            .wingsState(BodyCreateDuck.WingsState.UNDEFINED);
    BodyCreateDuck duckProperties4 = new BodyCreateDuck()
            .color("blue")
            .height(0.09)
            .material("foam")
            .sound("moo")
            .wingsState(BodyCreateDuck.WingsState.ACTIVE);
    BodyCreateDuck duckProperties5 = new BodyCreateDuck()
            .color("black")
            .height(0.12)
            .material("iron")
            .sound("quack")
            .wingsState(BodyCreateDuck.WingsState.FIXED);

    @Test(dataProvider = "duckList", description = "Проверка создания уточки")
    @CitrusTest
    @CitrusParameters({"payload", "response", "runner"})
    public void successfulCreateDuck(BodyCreateDuck payload, String response, @Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, payload);
        String idDuck = validateResponseCreate(runner, response);

        selectCheckingDuckById(runner,
                idDuck,
                payload.color(),
                payload.height(),
                payload.material(),
                payload.sound(),
                payload.wingsState().name());

        deleteFromTable(runner,
                "DUCK",
                "ID = " + idDuck);
    }

    @DataProvider(name = "duckList")
    public Object[][] DuckProvider() {
        return new Object[][]{
                {duckProperties1, "DuckActionsTest/getDuckCreateTest/duckYellowCreate.json", null},
                {duckProperties2, "DuckActionsTest/getDuckCreateTest/duckGreenCreate.json", null},
                {duckProperties3, "DuckActionsTest/getDuckCreateTest/duckRedCreate.json", null},
                {duckProperties4, "DuckActionsTest/getDuckCreateTest/duckBlueCreate.json", null},
                {duckProperties5, "DuckActionsTest/getDuckCreateTest/duckBlackCreate.json", null}
        };
    }

    //валидация ответа отличается от остальных, поэтому отдельно и private
    private String validateResponseCreate(TestCaseRunner runner, String response) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(new ClassPathResource(response))
                        .extract(fromBody().expression("$.id", "idDuck")));

        final String[] idValue = new String[1];
        runner.$(context -> idValue[0] = context.getVariable("idDuck"));

        return idValue[0];
    }
}
