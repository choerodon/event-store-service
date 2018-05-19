package io.choerodon.event.store.controller;

import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.event.store.domain.EventFailedMsg;
import io.choerodon.event.store.service.EventFailedMsgService;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * event-consume消费失败的消息回调
 *
 * @author zhipeng.zuo
 * @date 2018/2/6
 */
@RestController
@RequestMapping(value = "v1/messages/failed")
public class EventFailedMsgController {

    private EventFailedMsgService eventFailedMsgService;

    @Autowired
    public EventFailedMsgController(EventFailedMsgService eventFailedMsgService) {
        this.eventFailedMsgService = eventFailedMsgService;
    }

    @PostMapping
    public ResponseEntity<Void> storeFailedMsg(@Valid @RequestBody EventFailedMsg eventFailedMsg) {
        eventFailedMsgService.storeFailedMsg(eventFailedMsg);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Permission(level = ResourceLevel.SITE, roles = {"platformAdmin"})
    @GetMapping
    public ResponseEntity<Page<EventFailedMsg>> getFailedMsg(@RequestParam(required = false) String topic,
                                                             @SortDefault(value = "id", direction = Sort.Direction.ASC) PageRequest page) {
        return Optional.ofNullable(eventFailedMsgService.getFailedMsg(topic, page))
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElseThrow(() -> new CommonException("error.user.create"));
    }

    @Permission(level = ResourceLevel.SITE, roles = {"platformAdmin"})
    @PutMapping("/{uuid}")
    public ResponseEntity<Void> resendMsg(@PathVariable String uuid) {
        return eventFailedMsgService.resendMsg(uuid);
    }

}
