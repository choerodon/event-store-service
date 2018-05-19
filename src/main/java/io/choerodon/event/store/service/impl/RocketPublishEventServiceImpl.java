package io.choerodon.event.store.service.impl;

import io.choerodon.event.store.domain.EventSendMsg;
import io.choerodon.event.store.mapper.EventSendMsgMapper;
import io.choerodon.event.store.service.MessageFinishedListener;
import io.choerodon.event.store.service.PublishEventService;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import java.util.List;


/**
 * @author zhipeng.zuo
 * Created on 2017/10/23.
 */
public class RocketPublishEventServiceImpl implements PublishEventService {

  private static final Logger LOGGER = LoggerFactory.getLogger (RocketPublishEventServiceImpl.class);

  private TransactionMQProducer producer;
  private EventSendMsgMapper messageMapper;

  public RocketPublishEventServiceImpl(TransactionMQProducer producer, EventSendMsgMapper messageMapper) {
    this.producer = producer;
    this.messageMapper = messageMapper;
  }

  @Async("publish-executor")
  @Override
  public void publishMsg(List<EventSendMsg> messageList, MessageFinishedListener listener){
    try {
      for (EventSendMsg message: messageList) {
        Message sendMsg = new Message (message.getTopic(), message.getTopic(), message.getPayload().getBytes ());
        SendResult result = producer.send(sendMsg);
        if (result.getSendStatus() == SendStatus.SEND_OK){
          messageMapper.deleteByPrimaryKey(message.getId());
        }
      }
    } catch (Exception e) {
      LOGGER.info("publishMsg error:{}",e);
    } finally {
      listener.onFinished();
    }
  }


}
