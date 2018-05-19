package io.choerodon.event.store.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by zhipeng.zuo on 2017/10/10.
 * @author zhipeng.zuo
 */
@ControllerAdvice
public class CtrlExceptionHandler {

  private static Logger logger = LoggerFactory.getLogger(CtrlExceptionHandler.class);

  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ExceptionHandler(EventNotExistException.class)
  public ResponseEntity<Void> handleEventNotExistException(EventNotExistException e) {
    if (e != null) {
      logger.warn(e.getMessage());
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ExceptionHandler({JsonProcessingException.class})
  public ResponseEntity<Void> handleJsonProcessingException(JsonProcessingException e) {
    if (e != null) {
      logger.warn(e.getMessage());
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler({Exception.class})
  public ResponseEntity<Void> handleException(JsonProcessingException e) {
    if (e != null) {
      logger.warn(e.getMessage());
    }
    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
