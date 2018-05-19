package io.choerodon.event.store.service.impl;

import io.choerodon.event.store.domain.EventSendMsg;
import io.choerodon.event.store.mapper.EventSendMsgMapper;
import io.choerodon.event.store.service.MessageFinishedListener;
import io.choerodon.event.store.service.PublishEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * @author zhipeng.zuo
 * Created on 2017/10/23.
 */
public class RedisPublishEventServiceImpl implements PublishEventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisPublishEventServiceImpl.class);

    private RedisTemplate redisTemplate;
    private EventSendMsgMapper messageMapper;

    @SuppressWarnings("unchecked")
    public RedisPublishEventServiceImpl(RedisTemplate redisTemplate, EventSendMsgMapper messageMapper) {
        this.redisTemplate = redisTemplate;
        this.messageMapper = messageMapper;
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
    }

    @Async("publish-executor")
    @Override
    public void publishMsg(List<EventSendMsg> messageList, MessageFinishedListener listener) {
        for (EventSendMsg message : messageList) {
            boolean result = true;
            try {
                redisTemplate.convertAndSend(message.getTopic(), message.getPayload());
            } catch (Exception e) {
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
