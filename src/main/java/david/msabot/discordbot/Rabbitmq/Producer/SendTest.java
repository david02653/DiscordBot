package david.msabot.discordbot.Rabbitmq.Producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SendTest {

    public void send() throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://guest@36.229.104.218");
        factory.setConnectionTimeout(30000);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("myQueue", false, false, false, null);

        channel.basicPublish("myExchange", "cat.nail", null, "hello cat".getBytes());
    }
}
