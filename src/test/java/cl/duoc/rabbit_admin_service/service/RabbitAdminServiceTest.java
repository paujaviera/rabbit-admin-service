package cl.duoc.rabbit_admin_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;

import cl.duoc.rabbit_admin_service.dto.BindingRequest;
import cl.duoc.rabbit_admin_service.dto.ExchangeRequest;
import cl.duoc.rabbit_admin_service.dto.QueueRequest;

class RabbitAdminServiceTest {

    private final AmqpAdmin amqpAdmin = mock(AmqpAdmin.class);
    private final RabbitAdminService service = new RabbitAdminService(amqpAdmin);

    @Test
    void crearColaDeclaraLaCola() {
        service.crearCola(new QueueRequest("cola.test", true, null, null));
        verify(amqpAdmin).declareQueue(any(Queue.class));
    }

    @Test
    void crearColaConDlxAgregaArgumentos() {
        service.crearCola(new QueueRequest("cola.dlx", true, "pedidos.dlx", "k"));
        verify(amqpAdmin).declareQueue(argThat(q ->
                "pedidos.dlx".equals(q.getArguments().get("x-dead-letter-exchange"))
                && "k".equals(q.getArguments().get("x-dead-letter-routing-key"))));
    }

    @Test
    void eliminarColaRetornaResultadoDelAdmin() {
        when(amqpAdmin.deleteQueue("c")).thenReturn(true);
        assertTrue(service.eliminarCola("c"));
    }

    @Test
    void crearExchangeTopicDeclaraExchange() {
        service.crearExchange(new ExchangeRequest("ex", "topic", true));
        verify(amqpAdmin).declareExchange(argThat((Exchange e) ->
                "topic".equals(e.getType()) && "ex".equals(e.getName())));
    }

    @Test
    void crearYEliminarBinding() {
        BindingRequest r = new BindingRequest("c", "ex", "k.#");
        service.crearBinding(r);
        service.eliminarBinding(r);
        verify(amqpAdmin).declareBinding(any(Binding.class));
        verify(amqpAdmin).removeBinding(any(Binding.class));
    }
}