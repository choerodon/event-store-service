package io.choerodon.event.store.service.impl;

import io.choerodon.core.exception.CommonException;
import io.choerodon.event.store.domain.EventRecord;
import io.choerodon.event.store.domain.EventSendMsg;
import io.choerodon.event.store.exception.EventNotExistException;
import io.choerodon.event.store.mapper.EventSendMsgMapper;
import io.choerodon.event.store.service.EventRecordService;
import io.choerodon.event.store.tools.EventConvert;
import io.choerodon.mybatis.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by zhipeng.zuo on 2017/9/29.
 *
 * @author zhipeng.zuo
 */
@Service
public class EventRecordServiceImpl extends BaseServiceImpl<EventRecord> implements EventRecordService {


    private EventConvert eventConvert;

    private EventSendMsgMapper messageMapper;

    @Autowired
    public EventRecordServiceImpl(EventConvert eventConvert,
                                  EventSendMsgMapper messageMapper) {
        this.eventConvert = eventConvert;
        this.messageMapper = messageMapper;
    }

    @Override
    public void createEvent(EventRecord eventRecord) {
        eventRecord.setFinished(false);
        eventRecord.setCreateTime(System.currentTimeMillis());
        if (insert(eventRecord) != 1) {
            throw new CommonException("error.eventRecord.create");
        }
    }

    @Override
    public void preConfirmEvent(String uuid, String messages) {
        if (StringUtils.isEmpty(messages)) {
            throw new CommonException("error.eventRecord.preConfirmEvent");
        }
        EventRecord eventRecord = selectByPrimaryKey(uuid);
        if (eventRecord == null) {
            throw new EventNotExistException(uuid);
        }
        EventRecord newEvent = new EventRecord();
        newEvent.setCreateTime(System.currentTimeMillis());
        newEvent.setUuid(uuid);
        newEvent.setMessages(messages.trim());
        newEvent.setObjectVersionNumber(eventRecord.getObjectVersionNumber());
        if (updateByPrimaryKeySelective(newEvent) != 1) {
            throw new CommonException("error.eventRecord.preConfirmEvent");
        }
    }

    @Transactional(noRollbackFor = EventNotExistException.class)
    @Override
    public void confirmEvent(String uuid) {
        EventRecord eventRecord = selectByPrimaryKey(uuid);
        if (eventRecord == null || StringUtils.isEmpty(eventRecord.getMessages())) {
            throw new EventNotExistException(uuid);
        }
        List<EventSendMsg> sendMsgList = eventConvert.convertSendMsg(eventRecord.getMessages());
        sendMsgList.forEach(t -> {
            t.setUuid(uuid);
            if (messageMapper.insert(t) != 1) {
                throw new CommonException("error.eventRecord.confirm.insert");
            }
        });
        EventRecord newEvent = new EventRecord();
        newEvent.setUuid(eventRecord.getUuid());
        newEvent.setObjectVersionNumber(eventRecord.getObjectVersionNumber());
        newEvent.setFinished(true);
        if (updateByPrimaryKeySelective(newEvent) != 1) {
            throw new CommonException("error.eventRecord.confirm.update");
        }
    }

    @Override
    public void cancelEvent(String uuid) {
        if (deleteByPrimaryKey(uuid) != 1 && selectByPrimaryKey(uuid) != null) {
            throw new CommonException("error.eventRecord.cancel");
        }
    }

}
