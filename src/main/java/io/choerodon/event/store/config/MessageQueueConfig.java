package io.choerodon.event.store.config;

import io.choerodon.event.store.mapper.EventSendMsgMapper;
import io.choerodon.event.store.service.impl.KafkaPublishEventServiceImpl;
import io.choerodon.event.store.service.impl.RabbitPublishEventServiceImpl;
import io.choerodon.event.store.service.impl.RedisPublishEventServiceImpl;
import io.choerodon.event.store.service.impl.RocketPublishEventServiceImpl;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionCheckListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
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


  @ConditionalOnProperty(prefix = "choerodon.event.store", name = "queue-type", havingValue = "rabbitmq")
  static class Rabbitmq {
    @Bean(name = "eventRabbitTemplate")
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
      RabbitTemplate rabbitTemplate = new RabbitTemplate (connectionFactory);
      rabbitTemplate.setChannelTransacted(true);
      return rabbitTemplate;
    }
    @Bean
    public RabbitTransactionManager rabbitTransactionManager(@Autowired ConnectionFactory connectionFactory) {
      return new RabbitTransactionManager(connectionFactory);
    }
    @Bean
    public RabbitPublishEventServiceImpl rabbitPublishEventService(@Qualifier("eventRabbitTemplate") RabbitTemplate rabbitTemplate,
                                                                   EventSendMsgMapper messageMapper) {
      return new RabbitPublishEventServiceImpl(rabbitTemplate, messageMapper);
    }
  }

  @ConditionalOnProperty(prefix = "choerodon.event.store", name = "queue-type", havingValue = "kafka")
  static class Kafka {
    @Bean
    public ProducerFactory<Object, Object> kafkaProducerFactory(KafkaProperties properties) {
      Map<String, Object> map = properties.buildProducerProperties();
      map.put(ProducerConfig.RETRIES_CONFIG, 3);
      return new DefaultKafkaProducerFactory<>(map);
    }
    @Bean
    public KafkaPublishEventServiceImpl kafkaPublishEventService(KafkaTemplate kafkaTemplate,
                                                                 EventSendMsgMapper messageMapper){
      return new KafkaPublishEventServiceImpl(kafkaTemplate, messageMapper);
    }
  }

  @ConditionalOnProperty(prefix = "choerodon.event.store", name = "queue-type", havingValue = "redis")
  static class Redis {
    @Bean
    public RedisPublishEventServiceImpl redisPublishEventServiceImpl(RedisTemplate redisTemplate,
                                                                     EventSendMsgMapper messageMapper){
      return new RedisPublishEventServiceImpl (redisTemplate, messageMapper);
    }
  }

  @ConditionalOnProperty(prefix = "choerodon.event.store", name = "queue-type", havingValue = "rocketmq")
  static class Rocketmq {
    @Bean
    public RocketPublishEventServiceImpl rocketPublishEventServiceImpl(TransactionMQProducer transactionMQProducer,
                                                                       EventSendMsgMapper messageMapper){
      return new RocketPublishEventServiceImpl(transactionMQProducer, messageMapper);
    }

    @Bean(name = "rocket-send")
    public AsyncTaskExecutor queryStatusTaskExecutor() {
      ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
      executor.setThreadNamePrefix("rocket-send-executor");
      executor.setMaxPoolSize(5);
      return executor;
    }
    @Bean(name = "trans")
    public TransactionMQProducer transactionMQProducer(EventStoreProperties properties) throws MQClientException {
      TransactionMQProducer producer = new TransactionMQProducer(properties.getRocketmq().getTransactionProducer());
      producer.setNamesrvAddr(properties.getRocketmq().getNamesrvAddr());
      producer.setInstanceName(properties.getRocketmq().getInstanceName());
      producer.setProducerGroup (properties.getRocketmq().getGroupName());
      producer.setMaxMessageSize(properties.getRocketmq().getMaxMessageSize());
      producer.setSendMsgTimeout(properties.getRocketmq().getMaxMessageSize());
      producer.setTransactionCheckListener(checkListener());
      // 事务回查最小并发数
      producer.setCheckThreadPoolMinSize(2);
      // 事务回查最大并发数
      producer.setCheckThreadPoolMaxSize(5);
      // 队列数
      producer.setCheckRequestHoldMax(2000);
      producer.start();
      return producer;
    }
    @Bean
    public TransactionCheckListener checkListener(){
      return (MessageExt msg) -> LocalTransactionState.UNKNOW;
    }
  }

}
