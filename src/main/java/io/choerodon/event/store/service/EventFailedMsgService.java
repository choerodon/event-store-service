package io.choerodon.event.store.service;

import io.choerodon.core.domain.Page;
import io.choerodon.event.store.domain.EventFailedMsg;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.springframework.http.ResponseEntity;

/**
 * @author zhipeng.zuo
 * @date 2018/2/6
 */
public interface EventFailedMsgService {

    /**
     * 保存消费失败的消息
     * event consume消费失败后调用feign回调
     *
     * @param eventFailedMsg 要存储的失败消息
     */
    void storeFailedMsg(EventFailedMsg eventFailedMsg);

    /**
     * 分页获取失败的消息
     *
     * @param topic 主题名，可为空，为空查询所有
     * @param page  分页信息
     * @return 失败的消息列表
     */
    Page<EventFailedMsg> getFailedMsg(String topic, PageRequest page);

    /**
     * 重新发送event consume消费失败的消息
     * @param uuid 消息的uuid
     * @return HTTP响应
     */
    ResponseEntity<Void> resendMsg(String uuid);

}
