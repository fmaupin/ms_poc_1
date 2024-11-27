package com.fmaupin.mspoc1.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration de RabbitMQ
 * 
 * @author fmaupin, 26/08/2023
 *
 * @since 0.0.1-SNAPSHOT
 *
 *        mspoc1 is free software; you can redistribute it and/or
 *        modify it under the terms of the GNU Lesser General Public License as
 *        published by the Free Software Foundation; either version 3 of the
 *        License, or (at your option) any later version.
 *
 *        mspoc1 is distributed in the hope that it will be
 *        useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 *        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *        Lesser General Public License for more details.
 *
 *        You should have received a copy of the GNU Lesser General Public
 *        License along with this program; if not, write to the Free Software
 *        Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 *        02110-1301, USA.
 */
@EnableRabbit
@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private Integer port;

    @Value("${spring.rabbitmq.virtual-host}")
    private String vhost;

    @Value("${mspoc1.rabbitmq.in.consumerQueueName}")
    private String inQueueName;

    @Bean
    public Queue inQueue() {
        return QueueBuilder.durable(inQueueName)
                .withArgument("x-queue-type", "quorum")
                .withArgument("x-delivery-limit", 3)
                .withArgument("x-message-ttl", 3600000)
                .build();
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);

        if (vhost != null) {
            connectionFactory.setVirtualHost(vhost);
        } else {
            throw new NullPointerException();
        }

        if (username != null) {
            connectionFactory.setUsername(username);
        } else {
            throw new NullPointerException();
        }

        if (password != null) {
            connectionFactory.setPassword(password);
        } else {
            throw new NullPointerException();
        }

        return connectionFactory;
    }
}
