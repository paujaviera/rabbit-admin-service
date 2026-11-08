package cl.duoc.rabbit_admin_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ExchangeRequest(
        @NotBlank(message = "El nombre del exchange es obligatorio") String name,
        @NotBlank(message = "El tipo es obligatorio")
        @Pattern(regexp = "direct|topic|fanout|headers",
                 message = "Tipo inválido: use direct, topic, fanout o headers") String type,
        boolean durable) {}