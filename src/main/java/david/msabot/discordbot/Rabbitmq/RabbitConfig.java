package david.msabot.discordbot.Rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.impl.AMQBasicProperties;
import com.rabbitmq.client.impl.AMQContentHeader;
import com.sun.tools.jconsole.JConsoleContext;
import david.msabot.discordbot.Rabbitmq.Consumer.MessageHandler;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_NAME = "myExchange";
    public static final String QUEUE_NAME = "myQueue";
    public static final String TEST_EXCHANGE = "topic_logs";
    public static final String TEST_QUEUE = "testQ";
    public static final String JENKINS_EXCHANGE = "jenkins";
    public static final String JENKINS_QUEUE = "jChannel";


    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

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
    Queue createJenkinsQueue(){
        return new Queue(JENKINS_QUEUE, true);
    }
    @Bean
    TopicExchange exchangeJenkins(){
        return new TopicExchange(JENKINS_EXCHANGE);
    }
    @Bean
    Binding bindJenkins(){
        return BindingBuilder.bind(createJenkinsQueue()).to(exchangeJenkins()).with("jenkins.*");
    }



    @Bean
    MessageListenerAdapter listenerAdapter(MessageHandler handler){
        return new MessageListenerAdapter(handler, "handleMessage");
    }
    @Bean
    MessageListenerAdapter jenkinsListener(MessageHandler handler){
        return new MessageListenerAdapter(handler, "handleJenkinsMessage");
    }

    // consumer settings
    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory/*, MessageListenerAdapter adapter*/){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
//        container.setMessageListener(adapter);
        container.setMessageListener(listenerAdapter(new MessageHandler()));

        return container;
    }

    @Bean
    SimpleMessageListenerContainer jenkinsContainer(ConnectionFactory connectionFactory){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(JENKINS_QUEUE);
        container.setMessageListener(jenkinsListener(new MessageHandler()));


        return container;
    }
}
