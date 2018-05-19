package io.choerodon.event.store.tools;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.choerodon.event.store.domain.EventSendMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author zhipeng.zuo
 * Created on 2017/10/18.
 */
@Component
public class EventConvert {

  private static final Logger LOGGER = LoggerFactory.getLogger(EventConvert.class);

  private ObjectMapper mapper = new ObjectMapper();

  public List<EventSendMsg> convertSendMsg(String messages) {

    List<EventSendMsg> messageList = Collections.emptyList();
    try {
      messageList = mapper.readValue(messages, new TypeReference<List<EventSendMsg>>(){});
    } catch (IOException e) {
      LOGGER.info("error.EventConvert.convertSendMsg");
    }
    return messageList;
  }

}
