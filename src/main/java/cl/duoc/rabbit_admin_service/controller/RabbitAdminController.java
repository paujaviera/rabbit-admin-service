package cl.duoc.rabbit_admin_service.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import cl.duoc.rabbit_admin_service.dto.BindingRequest;
import cl.duoc.rabbit_admin_service.dto.ExchangeRequest;
import cl.duoc.rabbit_admin_service.dto.QueueRequest;
import cl.duoc.rabbit_admin_service.service.RabbitAdminService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/rabbit")
public class RabbitAdminController {

    private final RabbitAdminService service;

    public RabbitAdminController(RabbitAdminService service) {
        this.service = service;
    }

    @PostMapping("/queues")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> crearCola(@Valid @RequestBody QueueRequest request) {
        service.crearCola(request);
        return Map.of("mensaje", "Cola creada", "name", request.name());
    }

    @DeleteMapping("/queues/{name}")
    public Map<String, String> eliminarCola(@PathVariable String name) {
        if (!service.eliminarCola(name)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La cola no existe: " + name);
        }
        return Map.of("mensaje", "Cola eliminada", "name", name);
    }

    @PostMapping("/exchanges")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> crearExchange(@Valid @RequestBody ExchangeRequest request) {
        service.crearExchange(request);
        return Map.of("mensaje", "Exchange creado", "name", request.name());
    }

    @DeleteMapping("/exchanges/{name}")
    public Map<String, String> eliminarExchange(@PathVariable String name) {
        if (!service.eliminarExchange(name)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El exchange no existe: " + name);
        }
        return Map.of("mensaje", "Exchange eliminado", "name", name);
    }

    @PostMapping("/bindings")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> crearBinding(@Valid @RequestBody BindingRequest request) {
        service.crearBinding(request);
        return Map.of("mensaje", "Binding creado");
    }

    @DeleteMapping("/bindings")
    public Map<String, String> eliminarBinding(@Valid @RequestBody BindingRequest request) {
        service.eliminarBinding(request);
        return Map.of("mensaje", "Binding eliminado");
    }
}