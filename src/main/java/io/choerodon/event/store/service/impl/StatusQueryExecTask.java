package io.choerodon.event.store.service.impl;

import feign.Client;
import feign.hystrix.HystrixFeign;
import io.choerodon.core.event.EventBackCheckRecord;
import io.choerodon.core.event.EventStatus;
import io.choerodon.event.store.domain.EventRecord;
import io.choerodon.event.store.service.EventRecordService;
import io.choerodon.event.store.service.StatusQuery;
import io.choerodon.event.store.tools.JsonDecoder;
import io.choerodon.feign.FeignRequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by zhipeng.zuo on 2017/9/25.
 *
 * @author zhipeng.zuo
 */
@Component
public class StatusQueryExecTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusQueryExecTask.class);

    private EventRecordService eventRecordService;

    private Client client;

    private FeignRequestInterceptor feignRequestInterceptor;


    @Autowired
    public StatusQueryExecTask(EventRecordService eventRecordService,
                               Client client,
                               FeignRequestInterceptor feignRequestInterceptor) {
        this.eventRecordService = eventRecordService;
        this.client = client;
        this.feignRequestInterceptor = feignRequestInterceptor;
    }

    @Async("queryStatus-executor")
    void execQuery(EventRecord eventRecord, StatusQueryExecuter executer) {
        try {
            StatusQuery query = HystrixFeign
                    .builder()
                    .client(client)
                    .requestInterceptor(feignRequestInterceptor)
                    .decoder(new JsonDecoder())
                    .target(StatusQuery.class, "http://" + eventRecord.getService());
            EventBackCheckRecord result = query.getEventRecord(eventRecord.getUuid(), eventRecord.getType());
            LOGGER.info("execute back check get result, eventRecord {} httpResponse {}", eventRecord, result);
            if (result != null) {
                if (result.getStatus() == EventStatus.CONFIRMED) {
                    eventRecordService.confirmEvent(eventRecord.getUuid());
                }
                if (result.getStatus() == EventStatus.CANCELED) {
                    eventRecordService.cancelEvent(eventRecord.getUuid());
                }
            }
        } catch (Exception e) {
            LOGGER.warn("execute back check error, eventRecord {} error {}", eventRecord.toString(), e.toString());
        } finally {
            executer.onFinished();
        }
    }

}
