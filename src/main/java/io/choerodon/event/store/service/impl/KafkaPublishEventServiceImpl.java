package io.choerodon.event.store.service.impl;

import io.choerodon.event.store.domain.EventSendMsg;
import io.choerodon.event.store.mapper.EventSendMsgMapper;
import io.choerodon.event.store.service.MessageFinishedListener;
import io.choerodon.event.store.service.PublishEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;


/**
 * @author zhipeng.zuo
 * Created on 2017/10/17.
 */
@Transactional
public class KafkaPublishEventServiceImpl implements PublishEventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaPublishEventServiceImpl.class);

    private KafkaTemplate kafkaTemplate;
    private EventSendMsgMapper messageMapper;


    public KafkaPublishEventServiceImpl(KafkaTemplate kafkaTemplate,
                                        EventSendMsgMapper messageMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.messageMapper = messageMapper;
    }

    @SuppressWarnings("unchecked")
    @Async("publish-executor")
    @Override
    public void publishMsg(List<EventSendMsg> eventSendMsgList, MessageFinishedListener listener) {
        try {
            for (int i = 0; i < eventSendMsgList.size(); i++) {
                EventSendMsg message = eventSendMsgList.get(i);
                kafkaTemplate.send(message.getTopic(), message.getUuid() + i, message.getPayload())
                        .addCallback(new ListenableFutureCallback() {
                            @Override
                            public void onFailure(Throwable ex) {
                                LOGGER.warn("kafka send message error, messageId: {} cause: {}",
                                        message.getId(), ex.getCause());
                            }

                            @Override
                            public void onSuccess(Object result) {
                                messageMapper.deleteByPrimaryKey(message.getId());
                                LOGGER.info("kafka send message success, payload {} topic {}.",
                                        message.getPayload(), message.getTopic());
                            }
                        });
            }
        } catch (Exception e) {
            LOGGER.info("publishMsg error: {}", e);
        } finally {
            listener.onFinished();
        }
    }

}
