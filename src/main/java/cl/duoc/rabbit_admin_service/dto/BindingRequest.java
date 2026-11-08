package cl.duoc.rabbit_admin_service.dto;

import jakarta.validation.constraints.NotBlank;

public record BindingRequest(
        @NotBlank(message = "La cola es obligatoria") String queue,
        @NotBlank(message = "El exchange es obligatorio") String exchange,
        String routingKey) {}