package io.choerodon.event.store.service;

/**
 * @author zhipeng.zuo
 * Created on 17-12-7.
 */
public interface MessageFinishedListener {
    /**
     * 每次消息发送完成的回调方法
     */
    void onFinished();
}
