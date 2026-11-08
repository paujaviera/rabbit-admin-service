package cl.duoc.rabbit_admin_service.service;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.stereotype.Service;

import cl.duoc.rabbit_admin_service.dto.BindingRequest;
import cl.duoc.rabbit_admin_service.dto.ExchangeRequest;
import cl.duoc.rabbit_admin_service.dto.QueueRequest;

@Service
public class RabbitAdminService {

    private final AmqpAdmin admin;

    public RabbitAdminService(AmqpAdmin admin) {
        this.admin = admin;
    }

    public void crearCola(QueueRequest r) {
        QueueBuilder qb = r.durable() ? QueueBuilder.durable(r.name()) : QueueBuilder.nonDurable(r.name());
        if (r.deadLetterExchange() != null && !r.deadLetterExchange().isBlank()) {
            qb.deadLetterExchange(r.deadLetterExchange());
            if (r.deadLetterRoutingKey() != null && !r.deadLetterRoutingKey().isBlank()) {
                qb.deadLetterRoutingKey(r.deadLetterRoutingKey());
            }
        }
        admin.declareQueue(qb.build());
    }

    public boolean eliminarCola(String name) {
        return admin.deleteQueue(name);
    }

    public void crearExchange(ExchangeRequest r) {
        Exchange ex = switch (r.type()) {
            case "direct" -> new DirectExchange(r.name(), r.durable(), false);
            case "topic" -> new TopicExchange(r.name(), r.durable(), false);
            case "fanout" -> new FanoutExchange(r.name(), r.durable(), false);
            default -> new HeadersExchange(r.name(), r.durable(), false);
        };
        admin.declareExchange(ex);
    }

    public boolean eliminarExchange(String name) {
        return admin.deleteExchange(name);
    }

    public void crearBinding(BindingRequest r) {
        admin.declareBinding(toBinding(r));
    }

    public void eliminarBinding(BindingRequest r) {
        admin.removeBinding(toBinding(r));
    }

    private Binding toBinding(BindingRequest r) {
        String key = r.routingKey() == null ? "" : r.routingKey();
        return new Binding(r.queue(), Binding.DestinationType.QUEUE, r.exchange(), key, null);
    }
}