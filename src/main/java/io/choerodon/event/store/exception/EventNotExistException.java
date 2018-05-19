package io.choerodon.event.store.exception;

/**
 * Created by zhipeng.zuo on 2017/9/29.
 * @author zhipeng.zuo
 */
public class EventNotExistException extends RuntimeException {

  public EventNotExistException(String uuid) {
    super(uuid + " is not exist!");
  }

  public EventNotExistException() {
    this("");
  }
}
