package cl.duoc.rabbit_admin_service.service;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.stereotype.Service;

import cl.duoc.rabbit_admin_service.dto.BindingRequest;
import cl.duoc.rabbit_admin_service.dto.ExchangeRequest;
import cl.duoc.rabbit_admin_service.dto.QueueRequest;

@Service
public class RabbitAdminService {

    private final RabbitAdmin admin;

    public RabbitAdminService(ConnectionFactory connectionFactory) {
        this.admin = new RabbitAdmin(connectionFactory);
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
        String key = r.routingKey() == null ? "" : r.routingKey();
        admin.declareBinding(new Binding(r.queue(), Binding.DestinationType.QUEUE, r.exchange(), key, null));
    }

    public void eliminarBinding(BindingRequest r) {
        String key = r.routingKey() == null ? "" : r.routingKey();
        admin.removeBinding(new Binding(r.queue(), Binding.DestinationType.QUEUE, r.exchange(), key, null));
    }
}