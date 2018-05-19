package io.choerodon.event.store.service;

import feign.Param;
import feign.RequestLine;
import io.choerodon.core.event.EventBackCheckRecord;

/**
 * Created by zhipeng.zuo on 2017/10/16.
 * @author zhipeng.zuo
 */
public interface StatusQuery {

  /**
   * 向业务查询event状态的feign接口
   * @param uuid 要查询的event的uuid
   * @param type 要查询的event的type
   * @return 查询的结果
   */
  @RequestLine("GET /v1/events/{uuid}/{type}")
  EventBackCheckRecord getEventRecord(@Param("uuid")String uuid,
                                      @Param("type")String type);

}
