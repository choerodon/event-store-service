# Event Store Service
`event-store-service` is for data consistency support.
It is necessary to cooperate with [choerodon-starter-event-producer](https://github.com/choerodon/choerodon-starters/tree/master/choerodon-starter-event-producer) and [choerodon-starter-event-consumer](https://github.com/choerodon/choerodon-starters/tree/master/choerodon-starter-event-consumer) to implement data consistency. Currently, the message queue `kafka` is supported.

## Feature
- Complete the front page. After the failed message is sent back to `event-store-service`, manually click on Retry on the page to resend the message.
- `rabbitmq`, `redis`, and `rocketmq` may also be supported.

## Requirements
- This service is an eureka client service. The local operation needs to cooperate with [eureka-server](https://github.com/choerodon/eureka-server), and the online operation needs to cooperate with [go-register-server](https://github.com/choerodon/go-register-server).
- It must be used with [choerodon-starter-event-producer](https://github.com/choerodon/choerodon-starters/tree/master/choerodon-starter-event-producer) and [choerodon-starter-event-consumer](https://github.com/choerodon/choerodon-starters/tree/master/choerodon-starter-event-consumer) to achieve data consistency.

## Installation and Getting Started
1. Start up [eureka-server](https://github.com/choerodon/eureka-server)
2. Start up `kafka`
3. Create a `event_store_service` database in mysql：

    ```sql
    CREATE USER 'choerodon'@'%' IDENTIFIED BY "123456";
    CREATE DATABASE event_store_service DEFAULT CHARACTER SET utf8;
    GRANT ALL PRIVILEGES ON event_store_service.* TO choerodon@'%';
    FLUSH PRIVILEGES;
    ```
    New file of "init-local-database.sh" in the root directory of the event-store-service project：
    
    ```sh
    mkdir -p target
    if [ ! -f target/choerodon-tool-liquibase.jar ]
    then
        curl http://nexus.choerodon.com.cn/repository/choerodon-release/io/choerodon/choerodon-tool-liquibase/0.5.0.RELEASE/choerodon-tool-liquibase-0.5.0.RELEASE.jar -o target/choerodon-tool-liquibase.jar
    fi
    java -Dspring.datasource.url="jdbc:mysql://localhost/event_store_service?useUnicode=true&characterEncoding=utf-8&useSSL=false" \
     -Dspring.datasource.username=choerodon \
     -Dspring.datasource.password=123456 \
     -Ddata.drop=false -Ddata.init=true \
     -Ddata.dir=src/main/resources \
     -jar target/choerodon-tool-liquibase.jar
    ```
    
    And executed in the root directory of the `event-store-service` project：
    
    ```sh
    sh init-local-database.sh
    ```
4. Then run the project in the root directory of the project：

    ```sh
    mvn spring-boot:run
    ```

## Usage
- configuration

  ```yaml
  choerodon:
      event:
        store:
            queue-type: kafka # Message Queue Type
            publish-msg-thread-num: 5 # Number of threads sending messages to the message queue
            query-status-thread-num: 5 # The number of threads that perform the lookup of interface 
  ```

## Dependencies
- [go-register-server](https://github.com/choerodon/go-register-server): Register server
- [config-server](https://github.com/choerodon/config-server)：configure server
- `kafka`
- `mysql`：`event_store_service` Database

## Reporting Issues
If you find any shortcomings or bugs, please describe them in the [issue](https://github.com/choerodon/choerodon/issues/new?template=issue_template.md).

## How to Contribute
Pull requests are welcome! [Follow](https://github.com/choerodon/choerodon/blob/master/CONTRIBUTING.md) to know for more information on how to contribute.
