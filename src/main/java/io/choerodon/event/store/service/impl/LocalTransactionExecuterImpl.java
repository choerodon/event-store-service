package io.choerodon.event.store.service.impl;

import io.choerodon.event.store.domain.RocketStatus;
import org.apache.rocketmq.client.producer.LocalTransactionExecuter;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

/**
 * @author zhipeng.zuo
 * Created on 2017/10/25.
 * 业务代码
 */
public class LocalTransactionExecuterImpl implements LocalTransactionExecuter {
  private static final Logger LOGGER = LoggerFactory.getLogger(LocalTransactionExecuterImpl.class);
  @Async(value = "rocket-send")
  @Override
  public LocalTransactionState executeLocalTransactionBranch(Message msg, Object arg) {
    RocketStatus status = (RocketStatus)arg;
    while (status.getStatus().get () == RocketStatus.WAIT_INT){
      try {
        Thread.sleep (10);
      } catch (InterruptedException e) {
        LOGGER.info( "Interrupted:{}", e);
        Thread.currentThread().interrupt();
      }
    }
    if (status.getStatus().get() == RocketStatus.SUCCESS){
      return LocalTransactionState.COMMIT_MESSAGE;
    }
    return LocalTransactionState.ROLLBACK_MESSAGE;
  }

}
