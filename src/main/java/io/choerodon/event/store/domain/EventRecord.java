package io.choerodon.event.store.domain;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author flyleft
 * @date 2018/4/4
 */
@VersionAudit
@ModifyAudit
@Entity
@Table(name = "event_record")
public class EventRecord extends AuditDomain {

    @Id
    @NotEmpty
    private String uuid;

    private Boolean isFinished;

    @NotEmpty
    private String type;

    @NotEmpty
    private String service;

    private Long createTime;

    private String messages;

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Boolean getFinished() {
        return isFinished;
    }

    public void setFinished(Boolean finished) {
        isFinished = finished;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public EventRecord() {
    }

    public EventRecord(String uuid, String type, String service) {
        this.uuid = uuid;
        this.type = type;
        this.service = service;
        this.createTime = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "EventRecord{" +
                "uuid='" + uuid + '\'' +
                ", isFinished=" + isFinished +
                ", type='" + type + '\'' +
                ", service='" + service + '\'' +
                ", createTime=" + createTime +
                ", messages='" + messages + '\'' +
                '}';
    }
}
