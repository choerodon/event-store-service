package io.choerodon.event.store.domain;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author zhipeng.zuo
 * @date 2018/2/6
 */
@VersionAudit
@ModifyAudit
@Table(name = "event_failed_message")
@Entity
public class EventFailedMsg extends AuditDomain {

    @Id
    @GeneratedValue
    private String id;

    private String uuid;

    private String topic;

    private String payload;

    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public EventFailedMsg() {
    }

    public EventFailedMsg(String uuid) {
        this.uuid = uuid;
    }

}
