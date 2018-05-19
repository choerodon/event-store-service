package io.choerodon.event.store.controller;

import io.choerodon.event.store.domain.EventRecord;
import io.choerodon.event.store.service.EventRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 事件记录接口
 *
 * @author zhipeng.zuo
 */
@RestController
@RequestMapping(value = "/v1/events")
public class EventRecordController {

    private EventRecordService eventRecordService;

    @Autowired
    public EventRecordController(EventRecordService eventRecordService) {
        this.eventRecordService = eventRecordService;
    }

    @PostMapping("")
    public void createEvent(@Valid @RequestBody EventRecord eventRecord) {
        eventRecordService.createEvent(eventRecord);
    }

    @PostMapping("/{uuid}/pre_confirm")
    public void preConfirmEvent(@PathVariable String uuid, @RequestBody String messages) {
        eventRecordService.preConfirmEvent(uuid, messages);
    }

    @PutMapping("/{uuid}/confirm")
    public void confirmEvent(@PathVariable String uuid) {
        eventRecordService.confirmEvent(uuid);
    }

    @PutMapping("/{uuid}/cancel")
    public void cancelEvent(@PathVariable String uuid) {
        eventRecordService.cancelEvent(uuid);
    }

}
