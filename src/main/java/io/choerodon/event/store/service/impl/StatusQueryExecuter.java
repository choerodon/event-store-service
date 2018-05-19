package io.choerodon.event.store.service.impl;

import io.choerodon.event.store.domain.EventRecord;
import io.choerodon.event.store.mapper.EventRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhipeng.zuo on 2017/9/29.
 *
 * @author zhipeng.zuo
 */
@Component
public class StatusQueryExecuter {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusQueryExecuter.class);

    private static final long QUERY_INTERVAL = 5000L;

    //todo time 180000L
    private static final long BACK_CHECK_INTERVAL = 20000L;

    private EventRecordMapper eventRecordMapper;

    private StatusQueryExecTask task;

    private AtomicInteger eventNum = new AtomicInteger(0);

    @Autowired
    public StatusQueryExecuter(EventRecordMapper eventRecordMapper,
                               StatusQueryExecTask task) {
        this.eventRecordMapper = eventRecordMapper;
        this.task = task;
    }

    @Scheduled(fixedDelay = QUERY_INTERVAL)
    public void scheduledQueryJob() {
        if (eventNum.get() > 0) {
            return;
        } else {
            eventNum.set(0);
        }
        if (eventNum.get() < 1) {
            List<EventRecord> backCheckEventList = eventRecordMapper.selectInitEvent();
            if (backCheckEventList.isEmpty()) {
                return;
            }
            for (EventRecord eventRecord : backCheckEventList) {
                if ((System.currentTimeMillis() - eventRecord.getCreateTime()) < BACK_CHECK_INTERVAL) {
                    continue;
                }
                LOGGER.info("execute back check, eventRecord {}", eventRecord);
                eventNum.incrementAndGet();
                task.execQuery(eventRecord, this);
            }
        }
    }

    public void onFinished() {
        this.eventNum.decrementAndGet();
    }

}
