package david.msabot.discordbot.Repository;


import david.msabot.discordbot.Entity.LongMessage.DiscordMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LongMessageRepository extends MongoRepository<DiscordMessage, String> {
    DiscordMessage findDiscordMessageById(String id);
}
