package db
databaseChangeLog(logicalFilePath:'event_send_message.groovy') {
    changeSet(author: 'flyleft', id: '2018-04-04-event_send_message') {
        createTable(tableName: "event_send_message") {
            column(name: 'id', type: 'BIGINT UNSIGNED', autoIncrement: true, remarks: '角色ID') {
                constraints(primaryKey: true)
            }
            column(name: 'uuid', type: 'VARCHAR(50)', remarks: '所在event的uuid'){
                constraints(nullable(false))
            }
            column(name: 'topic', type: 'VARCHAR(100)', remarks: '要发送给消息队列的通道名'){
                constraints(nullable(false))
            }
            column(name: 'payload', type: 'LONGTEXT', remarks: '要发送给消息队列的消息内容'){
                constraints(nullable(false))
            }

            column(name: "object_version_number", type: "BIGINT UNSIGNED", defaultValue: "1") {
                constraints(nullable: true)
            }
            column(name: "created_by", type: "BIGINT UNSIGNED", defaultValue: "0") {
                constraints(nullable: true)
            }
            column(name: "creation_date", type: "DATETIME", defaultValueComputed: "CURRENT_TIMESTAMP")
            column(name: "last_updated_by", type: "BIGINT UNSIGNED", defaultValue: "0") {
                constraints(nullable: true)
            }
            column(name: "last_update_date", type: "DATETIME", defaultValueComputed: "CURRENT_TIMESTAMP")
        }
    }
}