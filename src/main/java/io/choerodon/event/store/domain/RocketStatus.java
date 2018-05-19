package io.choerodon.event.store.domain;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhipeng.zuo
 * Created on 2017/10/25.
 */
public class RocketStatus {

  public static final int SUCCESS = 0;
  public static final int WAIT_INT = 1;
  public static final int FAIL = 2;
  private AtomicBoolean successBoolean;
  private AtomicInteger status;

  public RocketStatus() {
    status = new AtomicInteger (WAIT_INT);
    successBoolean = new AtomicBoolean(true);
  }

  public AtomicInteger getStatus() {
    return status;
  }

  public AtomicBoolean getSuccessBoolean() {
    return successBoolean;
  }

}
