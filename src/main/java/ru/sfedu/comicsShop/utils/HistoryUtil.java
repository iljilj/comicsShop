package ru.sfedu.comicsShop.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import ru.sfedu.comicsShop.provider.DataProviderCsv;

import static org.bson.Document.parse;
import static ru.sfedu.comicsShop.Constants.*;
import static ru.sfedu.comicsShop.utils.ConfigurationUtil.getConfigurationEntry;


public class HistoryUtil {
    private static final Logger log = LogManager.getLogger(DataProviderCsv.class.getName());
    public static void saveHistory(HistoryContent historyContent)  {
        try {
            System.out.println(historyContent);
//            ObjectMapper objectMapper = new ObjectMapper();
//            Document historyContentDocument = parse(objectMapper.writeValueAsString(historyContent));
//            MongoClient mongoClient = new MongoClient(new MongoClientURI(getConfigurationEntry(MONGO_URI)));
//            MongoDatabase mongoDatabase = mongoClient.getDatabase(getConfigurationEntry(MONGO_DATABASE));
//            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(getConfigurationEntry(MONGO_COLLECTION));
//            mongoCollection.insertOne(historyContentDocument);
        }
        catch (Exception e){
            log.error(e);
        }
    }
}
