package io.choerodon.event.store.service;

import io.choerodon.event.store.domain.EventRecord;

/**
 * Created by zhipeng.zuo on 2017/9/29.
 * @author zhipeng.zuo
 */
public interface EventRecordService {

  /**
   * 创建事件
   * @param eventRecord 业务服务传入的event
   * @return event的uuid
   */
  void createEvent(EventRecord eventRecord);

  /**
   * 预确认事件，更新要发送到消息队列的消息
   * @param uuid 事件的uuid
   * @param messages 包含发送到kafka的topic和payload信息的json列表
   */
  void preConfirmEvent(String uuid, String messages);


  /**
   * 确认消息
   * @param uuid 事件的uuid
   */
  void confirmEvent(String uuid);

  /**
   * 取消事件
   * @param uuid 事件的uuid
   */
  void cancelEvent(String uuid);

}
