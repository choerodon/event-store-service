package io.choerodon.event.store.service;

import io.choerodon.event.store.domain.EventSendMsg;

import java.util.List;

/**
 * Created by zhipeng.zuo on 2017/9/29.
 * @author zhipeng.zuo
 */
public interface PublishEventService {

  /**
   * 将消息发送到消息队列
   * @param messageList  要发送的消息列表
   * @param listener 用于finish的回调
   */
  void publishMsg(List<EventSendMsg> messageList, MessageFinishedListener listener);
}
