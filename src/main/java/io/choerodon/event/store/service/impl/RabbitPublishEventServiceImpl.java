package io.choerodon.event.store.service.impl;

import io.choerodon.event.store.domain.EventSendMsg;
import io.choerodon.event.store.mapper.EventSendMsgMapper;
import io.choerodon.event.store.service.MessageFinishedListener;
import io.choerodon.event.store.service.PublishEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhipeng.zuo
 * Created on 2017/10/17.
 */
public class RabbitPublishEventServiceImpl implements PublishEventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitPublishEventServiceImpl.class);

    private RabbitTemplate rabbitTemplate;
    private EventSendMsgMapper messageMapper;

    public RabbitPublishEventServiceImpl(RabbitTemplate rabbitTemplate,
                                         EventSendMsgMapper messageMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.messageMapper = messageMapper;
    }

    @Async("publish-executor")
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void publishMsg(List<EventSendMsg> messageList, MessageFinishedListener listener) {
        for (EventSendMsg message : messageList) {
            boolean result = true;
            try {
                rabbitTemplate.convertAndSend(message.getTopic(), message.getPayload());
            } catch (AmqpException e) {
                LOGGER.warn("rabbit send message error, messageId: {} cause: {}",
                        message.getId(), e.getCause());
                result = false;
            }
            if (result) {
                messageMapper.deleteByPrimaryKey(message.getId());
            }
        }
        listener.onFinished();
    }

}
