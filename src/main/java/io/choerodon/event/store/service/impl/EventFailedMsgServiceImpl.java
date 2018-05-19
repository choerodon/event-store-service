package io.choerodon.event.store.service.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.event.store.domain.EventFailedMsg;
import io.choerodon.event.store.domain.EventSendMsg;
import io.choerodon.event.store.mapper.EventFailedMsgMapper;
import io.choerodon.event.store.mapper.EventSendMsgMapper;
import io.choerodon.event.store.service.EventFailedMsgService;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;

/**
 * @author zhipeng.zuo
 * @date 2018/2/6
 */
@Service
public class EventFailedMsgServiceImpl implements EventFailedMsgService {

    private EventFailedMsgMapper eventFailedMsgMapper;

    private EventSendMsgMapper eventSendMsgMapper;

    @Autowired
    public EventFailedMsgServiceImpl(EventFailedMsgMapper eventFailedMsgMapper, EventSendMsgMapper eventSendMsgMapper) {
        this.eventFailedMsgMapper = eventFailedMsgMapper;
        this.eventSendMsgMapper = eventSendMsgMapper;
    }

    @Override
    public void storeFailedMsg(EventFailedMsg eventFailedMsg) {
        if (eventFailedMsg.getCreateTime() == null) {
            eventFailedMsg.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        int num = eventFailedMsgMapper.selectCount(new EventFailedMsg(eventFailedMsg.getUuid()));
        if (num > 0) {
            return;
        }
        if (eventFailedMsgMapper.insert(eventFailedMsg) != 1) {
            throw new CommonException("error.failedMsg.insert");
        }
    }

    @Override
    public Page<EventFailedMsg> getFailedMsg(String topic, PageRequest page) {
        if (StringUtils.isEmpty(topic)) {
            PageHelper.doPage(page.getPage(), page.getSize(), () -> eventFailedMsgMapper.selectAll());
        }
        EventFailedMsg eventFailedMsg = new EventFailedMsg();
        eventFailedMsg.setTopic(topic);
        return PageHelper.doPage(page.getPage(), page.getSize(), () -> eventFailedMsgMapper.select(eventFailedMsg));
    }

    @Override
    public ResponseEntity<Void> resendMsg(String uuid) {
        EventFailedMsg eventFailedMsg = eventFailedMsgMapper.selectByPrimaryKey(uuid);
        if (eventFailedMsg == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        EventSendMsg eventSendMsg = new EventSendMsg();
        eventSendMsg.setUuid(uuid);
        eventSendMsg.setTopic(eventFailedMsg.getTopic());
        eventSendMsg.setPayload(eventFailedMsg.getPayload());
        if (eventSendMsgMapper.insert(eventSendMsg) != 1) {
            throw new CommonException("error.failedMsg.resend");
        }
        eventFailedMsgMapper.deleteByPrimaryKey(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
