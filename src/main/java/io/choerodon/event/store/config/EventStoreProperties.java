package io.choerodon.event.store.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author zhipeng.zuo
 * Created on 17-11-9.
 */
@ConfigurationProperties(prefix = "choerodon.event.store")
public class EventStoreProperties {

    private int publishMsgThreadNum = 5;

    private int queryStatusThreadNum = 5;

    private String queueType = "kafka";

    @NestedConfigurationProperty
    private Rocketmq rocketmq = new Rocketmq();

    public Rocketmq getRocketmq() {
        return rocketmq;
    }

    public void setRocketmq(Rocketmq rocketmq) {
        this.rocketmq = rocketmq;
    }

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

    public static class Rocketmq {
        private String namesrvAddr = "127.0.0.1:9876";
        private String instanceName = "default";
        private String groupName = "default-group";
        private String defaultProducer = "defaultProducer";
        private String transactionProducer = "transactionProducer";
        private int maxMessageSize = 131072;
        private int sendMsgTimeout = 10000;


        public Rocketmq() {
            //
        }

        public String getNamesrvAddr() {
            return namesrvAddr;
        }

        public void setNamesrvAddr(String namesrvAddr) {
            this.namesrvAddr = namesrvAddr;
        }

        public String getInstanceName() {
            return instanceName;
        }

        public void setInstanceName(String instanceName) {
            this.instanceName = instanceName;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getDefaultProducer() {
            return defaultProducer;
        }

        public void setDefaultProducer(String defaultProducer) {
            this.defaultProducer = defaultProducer;
        }

        public String getTransactionProducer() {
            return transactionProducer;
        }

        public void setTransactionProducer(String transactionProducer) {
            this.transactionProducer = transactionProducer;
        }

        public int getMaxMessageSize() {
            return maxMessageSize;
        }

        public void setMaxMessageSize(int maxMessageSize) {
            this.maxMessageSize = maxMessageSize;
        }

        public int getSendMsgTimeout() {
            return sendMsgTimeout;
        }

        public void setSendMsgTimeout(int sendMsgTimeout) {
            this.sendMsgTimeout = sendMsgTimeout;
        }
    }
}
