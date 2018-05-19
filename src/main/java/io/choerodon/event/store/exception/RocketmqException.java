package io.choerodon.event.store.exception;

/**
 * @author zhipeng.zuo
 * Created on 2017/10/23.
 */
public class RocketmqException extends Exception {

  public RocketmqException() {
    super();
  }

  public RocketmqException(String message) {
    super(message);
  }

  public RocketmqException(String message, Throwable cause) {
    super(message, cause);
  }

  public RocketmqException(Throwable cause) {
    super(cause);
  }
}
