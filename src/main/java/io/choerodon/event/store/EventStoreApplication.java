package io.choerodon.event.store;

import io.choerodon.resource.annoation.EnableChoerodonResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 运行主类
 * @author zhipeng.zuo
 */
@EnableEurekaClient
@EnableChoerodonResourceServer
@EnableScheduling
@EnableAsync
@SpringBootApplication
public class EventStoreApplication {

  public static void main(String[] args) {
    SpringApplication.run(EventStoreApplication.class, args);
  }

}
