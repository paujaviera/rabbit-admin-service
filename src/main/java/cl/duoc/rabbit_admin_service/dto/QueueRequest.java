package cl.duoc.rabbit_admin_service.dto;

import jakarta.validation.constraints.NotBlank;

public record QueueRequest(
        @NotBlank(message = "El nombre de la cola es obligatorio") String name,
        boolean durable,
        String deadLetterExchange,
        String deadLetterRoutingKey) {}