package cl.duoc.rabbit_admin_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;

public record QueueRequest(
        @NotBlank(message = "El nombre de la cola es obligatorio") String name,
        boolean durable,
        String deadLetterExchange,
        String deadLetterRoutingKey) {

    @JsonIgnore
    @AssertTrue(message = "deadLetterExchange y deadLetterRoutingKey deben informarse juntos")
    public boolean isDlqConsistente() {
        boolean ex = deadLetterExchange != null && !deadLetterExchange.isBlank();
        boolean rk = deadLetterRoutingKey != null && !deadLetterRoutingKey.isBlank();
        return ex == rk;
    }
}