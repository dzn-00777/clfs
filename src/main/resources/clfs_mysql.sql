create database if not exists clfs character set utf8mb4 collate utf8mb4_unicode_ci;

create table class_metadata
(
    id              int auto_increment primary key,
    group_name      varchar(32) not null unique comment '组名 如果不分组则跟qualified_name一致',
    qualified_name  varchar(128) not null comment '全类名 com.dzn.Test',
    source_code     text(65535) not null comment '源码',
    byte_code       blob(65535) not null comment '字节码',
    create_time     datetime default current_timestamp,
    update_time     datetime default current_timestamp on update current_timestamp
) comment '类元数据' engine = innodb;

# insert into class_metadata (group_name, qualified_name, source_code, byte_code) value (?, ?, ?, ?)
# 4KB 2G