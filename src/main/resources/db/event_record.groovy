package db
databaseChangeLog(logicalFilePath:'event_record.groovy') {
    changeSet(author: 'flyleft', id: '2018-04-04-event_record') {
        createTable(tableName: "event_record") {
            column(name: 'uuid', type: 'VARCHAR(50)', autoIncrement: false, remarks: 'uuid') {
                constraints(primaryKey: true)
            }
            column(name: 'is_finished', type: 'TINYINT(1)', defaultValue: "0", remarks: '事件是否已完成。1表示已完成，0表示未完成'){
                constraints(nullable(false))
            }
            column(name: 'service', type: 'VARCHAR(64)', remarks: '发布事件的服务id'){
                constraints(nullable(false))
            }
            column(name: 'type', type: 'VARCHAR(64)', remarks: 'event的类型'){
                constraints(nullable(false))
            }
            column(name: 'messages', type: 'LONGTEXT', remarks: '要发送到消息队列的topic和payload')

            column(name: 'create_time', type: 'BIGINT', remarks: 'event的创建时间'){
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