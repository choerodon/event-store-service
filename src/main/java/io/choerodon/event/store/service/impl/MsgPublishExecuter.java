package io.choerodon.event.store.service.impl;

import io.choerodon.event.store.domain.EventSendMsg;
import io.choerodon.event.store.mapper.EventSendMsgMapper;
import io.choerodon.event.store.service.MessageFinishedListener;
import io.choerodon.event.store.service.PublishEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.groupingBy;

/**
 * Created by zhipeng.zuo on 2017/10/13.
 *
 * @author zhipeng.zuo
 */
@Service
public class MsgPublishExecuter implements MessageFinishedListener {

    private static final long QUERY_INTERVAL = 300;

    private PublishEventService publishEventService;
    private EventSendMsgMapper messageMapper;
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    @Autowired
    public MsgPublishExecuter(PublishEventService publishEventService,
                              EventSendMsgMapper messageMapper) {
        this.publishEventService = publishEventService;
        this.messageMapper = messageMapper;
    }

    @Scheduled(fixedDelay = QUERY_INTERVAL)
    public void scheduledQueryJob() {
        if (atomicInteger.get() > 0) {
            return;
        }
        Collection<List<EventSendMsg>> messagesList = messageMapper.selectAll().stream()
                .collect(groupingBy(EventSendMsg::getUuid))
                .values();
        if (messagesList.isEmpty()) {
            return;
        }
        atomicInteger.set(messagesList.size());
        messagesList.forEach(t -> publishEventService.publishMsg(t, this));
    }

    @Override
    public void onFinished() {
        this.atomicInteger.decrementAndGet();
    }

}
