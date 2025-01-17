package faang.school.analytics.messaging;

import faang.school.analytics.dto.fundRasing.FundRaisedEvent;
import faang.school.analytics.service.FundRaisedEventHandler;
import faang.school.analytics.util.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
@RequiredArgsConstructor
public class FundRaisedEventListener implements MessageListener {

    private final JsonMapper jsonMapper;
    private final FundRaisedEventHandler handler;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        FundRaisedEvent event = getEvent(message);
        log.info("Received FundRaisedEvent: {}", event);

        handler.save(event);
    }

    private FundRaisedEvent getEvent(Message message) {
        return jsonMapper
                .toObject(Arrays.toString(message.getBody()), FundRaisedEvent.class)
                .orElseThrow(() -> new IllegalArgumentException("Can not deserialize FundRaisedEvent"));
    }
}
