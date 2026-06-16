package mana.mana33.controller.contracts;

import mana.mana33.controller.http.HttpRequest;
import mana.mana33.controller.http.HttpResponse;
import mana.mana33.features.contracts.Validate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    private static class TestBody {
        String name;

        TestBody(String name) {
            this.name = name;
        }
    }

    private static class TestValidator implements Validate<TestBody> {
        @Override
        public void validate(TestBody input) {
            if (input == null || input.name == null || input.name.isEmpty()) {
                throw new IllegalArgumentException("Invalid body");
            }
        }
    }

    private static class TestControllerWithValidator extends Controller<String, TestBody, Void> {
        private final Validate<TestBody> validator;

        TestControllerWithValidator(Validate<TestBody> validator) {
            this.validator = validator;
        }

        @Override
        public HttpResponse<String> perform(HttpRequest<TestBody, Void> input) {
            HttpResponse<String> response = new HttpResponse<>();
            response.statusCode = 200;
            response.body = "Success";
            return response;
        }

        @Override
        public Validate<TestBody> getValidators() {
            return validator;
        }
    }

    private static class TestControllerWithoutValidator extends Controller<String, TestBody, Void> {
        @Override
        public HttpResponse<String> perform(HttpRequest<TestBody, Void> input) {
            HttpResponse<String> response = new HttpResponse<>();
            response.statusCode = 200;
            response.body = "Success";
            return response;
        }
    }

    private static class TestControllerWithError extends Controller<String, TestBody, Void> {
        @Override
        public HttpResponse<String> perform(HttpRequest<TestBody, Void> input) {
            throw new RuntimeException("Internal error");
        }
    }

    private static class TestControllerWithoutStatusCode extends Controller<String, TestBody, Void> {
        @Override
        public HttpResponse<String> perform(HttpRequest<TestBody, Void> input) {
            HttpResponse<String> response = new HttpResponse<>();
            response.body = "Success without status code";
            return response;
        }
    }

    @Test
    void shouldReturnDefaultEmptyValidator() {
        TestControllerWithoutValidator controller = new TestControllerWithoutValidator();

        Validate<TestBody> result = controller.getValidators();

        assertNotNull(result);
    }

    @Test
    void shouldNotThrowExceptionWithDefaultEmptyValidator() {
        TestControllerWithoutValidator controller = new TestControllerWithoutValidator();

        Validate<TestBody> validator = controller.getValidators();
        TestBody body = new TestBody("");

        assertDoesNotThrow(() -> {
            validator.validate(body);
        });
    }

    @Test
    void shouldReturnOverriddenValidator() {
        TestValidator validator = new TestValidator();
        TestControllerWithValidator controller = new TestControllerWithValidator(validator);

        Validate<TestBody> result = controller.getValidators();

        assertNotNull(result);
        assertEquals(validator, result);
    }

    @Test
    void shouldValidateInputUsingOverriddenValidator() {
        TestValidator validator = new TestValidator();
        TestControllerWithValidator controller = new TestControllerWithValidator(validator);

        Validate<TestBody> returnedValidator = controller.getValidators();
        TestBody validBody = new TestBody("Test");

        assertDoesNotThrow(() -> {
            returnedValidator.validate(validBody);
        });
    }

    @Test
    void shouldThrowExceptionWhenValidatingInvalidInput() {
        TestValidator validator = new TestValidator();
        TestControllerWithValidator controller = new TestControllerWithValidator(validator);

        Validate<TestBody> returnedValidator = controller.getValidators();
        TestBody invalidBody = new TestBody("");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            returnedValidator.validate(invalidBody);
        });

        assertEquals("Invalid body", exception.getMessage());
    }

    @Test
    void shouldCallHandlerMethod() {
        TestValidator validator = new TestValidator();
        TestControllerWithValidator controller = new TestControllerWithValidator(validator);

        HttpRequest<TestBody, Void> request = new HttpRequest<>();
        request.body = new TestBody("Test");

        HttpResponse<String> response = controller.handler(request);

        assertNotNull(response);
        assertEquals(200, response.statusCode);
        assertEquals("Success", response.body);
    }

    @Test
    void shouldCallValidatorBeforePerform() {
        TestValidator validator = new TestValidator();
        TestControllerWithValidator controller = new TestControllerWithValidator(validator);

        HttpRequest<TestBody, Void> request = new HttpRequest<>();
        request.body = new TestBody("Valid");

        assertDoesNotThrow(() -> {
            controller.handler(request);
        });
    }

    @Test
    void shouldReturn401WhenValidationFailsInHandler() {
        TestValidator validator = new TestValidator();
        TestControllerWithValidator controller = new TestControllerWithValidator(validator);

        HttpRequest<TestBody, Void> request = new HttpRequest<>();
        request.body = new TestBody("");

        HttpResponse<String> response = controller.handler(request);

        assertNotNull(response);
        assertEquals(401, response.statusCode);
        assertNull(response.body);
    }

    @Test
    void shouldNotThrowExceptionWithDefaultValidatorInHandler() {
        TestControllerWithoutValidator controller = new TestControllerWithoutValidator();

        HttpRequest<TestBody, Void> request = new HttpRequest<>();
        request.body = new TestBody("");

        assertDoesNotThrow(() -> {
            HttpResponse<String> response = controller.handler(request);
            assertNotNull(response);
            assertEquals(200, response.statusCode);
        });
    }

    @Test
    void shouldReturn401WhenBodyIsNull() {
        TestValidator validator = new TestValidator();
        TestControllerWithValidator controller = new TestControllerWithValidator(validator);

        HttpRequest<TestBody, Void> requestWithInvalidBody = new HttpRequest<>();
        requestWithInvalidBody.body = new TestBody(null);

        HttpResponse<String> response = controller.handler(requestWithInvalidBody);

        assertNotNull(response);
        assertEquals(401, response.statusCode);
    }

    @Test
    void shouldReturn401WhenBodyIsEmpty() {
        TestValidator validator = new TestValidator();
        TestControllerWithValidator controller = new TestControllerWithValidator(validator);

        HttpRequest<TestBody, Void> request = new HttpRequest<>();
        request.body = new TestBody("");

        HttpResponse<String> response = controller.handler(request);

        assertNotNull(response);
        assertEquals(401, response.statusCode);
    }

    @Test
    void shouldReturn200WhenValidationPasses() {
        TestValidator validator = new TestValidator();
        TestControllerWithValidator controller = new TestControllerWithValidator(validator);

        HttpRequest<TestBody, Void> request = new HttpRequest<>();
        request.body = new TestBody("Valid Body");

        HttpResponse<String> response = controller.handler(request);

        assertNotNull(response);
        assertEquals(200, response.statusCode);
        assertEquals("Success", response.body);
    }

    @Test
    void shouldReturn500WhenPerformThrowsException() {
        TestControllerWithError controller = new TestControllerWithError();

        HttpRequest<TestBody, Void> request = new HttpRequest<>();
        request.body = new TestBody("Valid Body");

        HttpResponse<String> response = controller.handler(request);

        assertNotNull(response);
        assertEquals(500, response.statusCode);
        assertNull(response.body);
    }

    @Test
    void shouldReturn500OnRuntimeExceptionInPerform() {
        TestControllerWithError controller = new TestControllerWithError();

        HttpRequest<TestBody, Void> request = new HttpRequest<>();
        request.body = new TestBody("Test");

        HttpResponse<String> response = controller.handler(request);

        assertNotNull(response);
        assertEquals(500, response.statusCode);
    }

    @Test
    void shouldNotExecutePerformWhenValidationFails() {
        TestValidator validator = new TestValidator();
        TestControllerWithValidator controller = new TestControllerWithValidator(validator);

        HttpRequest<TestBody, Void> request = new HttpRequest<>();
        request.body = new TestBody("");

        HttpResponse<String> response = controller.handler(request);

        assertEquals(401, response.statusCode);
        assertNull(response.body);
    }

    @Test
    void shouldReturn200WhenPerformDoesNotSetStatusCode() {
        TestControllerWithoutStatusCode controller = new TestControllerWithoutStatusCode();

        HttpRequest<TestBody, Void> request = new HttpRequest<>();
        request.body = new TestBody("Test");

        HttpResponse<String> response = controller.handler(request);

        assertNotNull(response);
        assertEquals(200, response.statusCode);
        assertEquals("Success without status code", response.body);
    }

    @Test
    void shouldReturn200WhenStatusCodeIsZero() {
        Controller<String, TestBody, Void> controller = new Controller<String, TestBody, Void>() {
            @Override
            public HttpResponse<String> perform(HttpRequest<TestBody, Void> input) {
                HttpResponse<String> response = new HttpResponse<>();
                response.statusCode = 0;
                response.body = "Test";
                return response;
            }
        };

        HttpRequest<TestBody, Void> request = new HttpRequest<>();
        request.body = new TestBody("Test");

        HttpResponse<String> response = controller.handler(request);

        assertEquals(200, response.statusCode);
    }

    @Test
    void shouldPreserveCustomStatusCode() {
        Controller<String, TestBody, Void> controller = new Controller<String, TestBody, Void>() {
            @Override
            public HttpResponse<String> perform(HttpRequest<TestBody, Void> input) {
                HttpResponse<String> response = new HttpResponse<>();
                response.statusCode = 201;
                response.body = "Created";
                return response;
            }
        };

        HttpRequest<TestBody, Void> request = new HttpRequest<>();
        request.body = new TestBody("Test");

        HttpResponse<String> response = controller.handler(request);

        assertEquals(201, response.statusCode);
        assertEquals("Created", response.body);
    }
}
