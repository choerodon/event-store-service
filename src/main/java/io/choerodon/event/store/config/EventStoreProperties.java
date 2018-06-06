package io.choerodon.event.store.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhipeng.zuo
 * Created on 17-11-9.
 */
@ConfigurationProperties(prefix = "choerodon.event.store")
public class EventStoreProperties {

    private int publishMsgThreadNum = 5;

    private int queryStatusThreadNum = 5;

    private String queueType = "kafka";

    public int getPublishMsgThreadNum() {
        return publishMsgThreadNum;
    }

    public void setPublishMsgThreadNum(int publishMsgThreadNum) {
        this.publishMsgThreadNum = publishMsgThreadNum;
    }

    public int getQueryStatusThreadNum() {
        return queryStatusThreadNum;
    }

    public void setQueryStatusThreadNum(int queryStatusThreadNum) {
        this.queryStatusThreadNum = queryStatusThreadNum;
    }

    public String getQueueType() {
        return queueType;
    }

    public void setQueueType(String queueType) {
        this.queueType = queueType;
    }
}
