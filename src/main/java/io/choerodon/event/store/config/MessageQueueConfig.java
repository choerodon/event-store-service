package io.choerodon.event.store.config;

import io.choerodon.event.store.mapper.EventSendMsgMapper;
import io.choerodon.event.store.service.impl.KafkaPublishEventServiceImpl;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;

/**
 * @author zhipeng.zuo
 * Created on 2017/10/17.
 */
@Configuration
@EnableKafka
@EnableConfigurationProperties({KafkaProperties.class, EventStoreProperties.class})
public class MessageQueueConfig {

  private EventStoreProperties eventStoreProperties;

  @Autowired
  public MessageQueueConfig(EventStoreProperties eventStoreProperties) {
    this.eventStoreProperties = eventStoreProperties;
  }

  @Bean(name = "publish-executor")
  public AsyncTaskExecutor publishTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setThreadNamePrefix("publish-executor");
    executor.setMaxPoolSize(eventStoreProperties.getPublishMsgThreadNum());
    return executor;
  }

  @Bean(name = "queryStatus-executor")
  public AsyncTaskExecutor queryStatusTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setThreadNamePrefix("queryStatus-executor");
    executor.setMaxPoolSize(eventStoreProperties.getQueryStatusThreadNum());
    return executor;
  }


  @ConditionalOnProperty(prefix = "choerodon.event.store", name = "queue-type", havingValue = "kafka", matchIfMissing = true)
  static class Kafka {
    @Bean
    public ProducerFactory<Object, Object> kafkaProducerFactory(KafkaProperties properties) {
      Map<String, Object> map = properties.buildProducerProperties();
      map.put(ProducerConfig.RETRIES_CONFIG, 3);
      return new DefaultKafkaProducerFactory<>(map);
    }
    @Bean
    public KafkaPublishEventServiceImpl kafkaPublishEventService(KafkaTemplate<byte[], byte[]> kafkaTemplate,
                                                                 EventSendMsgMapper messageMapper){
      return new KafkaPublishEventServiceImpl(kafkaTemplate, messageMapper);
    }
  }


}
