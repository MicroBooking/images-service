package streaming;

import com.kumuluz.ee.streaming.common.annotations.StreamProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

public class EventProducerStreaming {
    private static final Logger log = Logger.getLogger(EventProducerStreaming.class.getName());

    private static final String TOPIC_NAME = "image-upload";

    @Inject
    @StreamProducer
    private Producer producer;

    public Response produceMessage(String imageCloudinaryId, String imageUrl) {

        JSONObject obj = new JSONObject();
        obj.put("imageCloudinaryId", imageCloudinaryId);
        obj.put("imageUrl", imageUrl);
        obj.put("status", "unprocessed");

        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, imageCloudinaryId, obj.toString());

        producer.send(record,
                (metadata, e) -> {
                    if (e != null) {
                        e.printStackTrace();
                    } else {
                        log.info("The offset of the produced message record is: " + metadata.offset());
                    }
                });

        return Response.ok().build();

    }
}
