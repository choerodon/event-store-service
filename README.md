# event-store-service
> event-store-service for data consistency support.
It is necessary to cooperate with choerodon-starter-event-producer and choerodon-starter-event-consumer to implement data consistency. Currently, the message queue kafka is supported.

## Feature
- Complete the front page. After the failed message is sent back to event-store-service, manually click on Retry on the page to resend the message.
- In addition to kafka, rabbitmq, redis, and rocketmq may also be supported.

## Requirements
- This project is an eureka client project. The local operation needs to cooperate with register-server, and the online operation needs to cooperate with go-register-server.
- It must be used with choerodon-starter-event-producer and choerodon-starter-event-consumer to achieve data consistency.

## To get the code

```
git clone https://github.com/choerodon/event-store-service.git
```

## Installation and Getting Started
1. Start up register-server
2.Create the event_store_service database in the local mysql, execute sh init-local-database.sh in the project directory to initialize the data table.
3. Start up kafka
4.Go to the project directory and run `mvn spring-boot:run` or `idea` ,`EventStore Application`.

## Usage
- File configuration

  ```yaml
  choerodon:
  event:
    store:
        queue-type: kafka # Message Queue Type
        publish-msg-thread-num: 5 # Number of threads sending messages to the message queue
        query-status-thread-num: 5 # The number of threads that perform the lookup of interface 
        rocketmq: # The related configuration of Rocketmq
          group-name: event-store-group
          send-msg-timeout: 10000
          max-message-size: 131072
          default-producer: defaultProducer
          transaction-producer: transactionProducer
          instance-name: event-store
          namesrv-addr: 127.0.0.1:9876
  ```
- Build mirror

   Pull source code to execute mvn clean install, generate app.jar in the target directory, copy it to src/main/docker directory, there are dockerfile, perform docker build as a mirror.
- The usage of existing mirror

- After creating a mirror, create a new deployment on k8s. Refer the chart directory deployment file in the code to write.

## Dependencies
- go-register-server: Register server
- config-server：configure server
- kafka
- mysql：event_store_service Database
    