package david.msabot.discordbot.Rabbitmq;

import david.msabot.discordbot.Rabbitmq.Consumer.MessageHandler;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_NAME = "myExchange";
    public static final String QUEUE_NAME = "myQueue";
    public static final String TEST_EXCHANGE = "topic_logs";
    public static final String TEST_QUEUE = "testQ";

    // bind: myExchange - <"dog.#"> --> (myQueue)
    //         exchange   routingKey    queue(channel)
    @Bean
    Queue createQueue(){
        return new Queue(QUEUE_NAME, true);
    }
    @Bean
    TopicExchange exchange(){
        return new TopicExchange(EXCHANGE_NAME);
    }
//    @Bean
//    Binding binding(Queue q, TopicExchange topicExchange){
//        return BindingBuilder.bind(q).to(topicExchange).with("dog.#");
//    }
    @Bean
    Binding binding(){
        return BindingBuilder.bind(createQueue()).to(exchange()).with("dog.#");
    }

    @Bean
    Queue createQueueA(){
        return new Queue(TEST_QUEUE, true);
    }
    @Bean
    TopicExchange exchangeA(){
        return new TopicExchange(TEST_EXCHANGE);
    }
    @Bean
    Binding bindingA(){
        return BindingBuilder.bind(createQueueA()).to(exchangeA()).with("rat.#");
    }


    @Bean
    MessageListenerAdapter listenerAdapter(MessageHandler handler){
        return new MessageListenerAdapter(handler, "handleMessage");
    }

    // consumer settings
    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter adapter){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        container.setMessageListener(adapter);

        return container;
    }
}
