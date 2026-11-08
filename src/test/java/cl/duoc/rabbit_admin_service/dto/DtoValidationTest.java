package cl.duoc.rabbit_admin_service.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

class DtoValidationTest {

    private final Validator v = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void colaConNombreVacioEsInvalida() {
        assertFalse(v.validate(new QueueRequest("", true, null, null)).isEmpty());
    }

    @Test
    void colaConNombreEsValida() {
        assertTrue(v.validate(new QueueRequest("c", true, null, null)).isEmpty());
    }

    @Test
    void colaConDlqIncompletaEsInvalida() {
        assertFalse(v.validate(new QueueRequest("c", true, "dlx", null)).isEmpty());
    }

    @Test
    void colaConDlqCompletaEsValida() {
        assertTrue(v.validate(new QueueRequest("c", true, "dlx", "dlq.key")).isEmpty());
    }

    @Test
    void exchangeConTipoInvalidoEsInvalido() {
        assertFalse(v.validate(new ExchangeRequest("ex", "otro", true)).isEmpty());
    }

    @Test
    void bindingSinColaEsInvalido() {
        assertFalse(v.validate(new BindingRequest("", "ex", "k")).isEmpty());
    }
}