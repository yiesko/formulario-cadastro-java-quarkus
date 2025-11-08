package xq.yiesko.forms.web;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@SuppressWarnings("SpellCheckingInspection")
@QuarkusTest
class FormPageResourceTest {

    @Test
    void shouldRenderFormPage() {
        given()
                .when().get("/")
                .then()
                .statusCode(200)
                .body(containsString("Formulário de cadastro"));
    }

    @Test
    void shouldPersistFormData() {
        given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("nomeCompleto", "Fulano de Tal")
                .formParam("endereco", "Rua A, 123")
                .formParam("rua", "Rua A")
                .formParam("numero", "123")
                .formParam("dataNascimento", "01-01-2000")
                .formParam("email", "fulano@example.com")
                .formParam("cidade", "Porto Alegre")
                .formParam("estado", "RS")
                .formParam("aceite", "true")
                .when().post("/")
                .then()
                .statusCode(200)
                .body(containsString("Cadastro realizado com sucesso"));
    }

    @Test
    void shouldReturnJsonListOfForms() {
        given()
                .when().get("/api/forms")
                .then()
                .statusCode(200)
                .contentType("application/json");
    }

}