create table if not exists customer
(
    id     bigint auto_increment
        primary key,
    name   varchar(60) null,
    email  varchar(60) null,
    mobile varchar(15) null
);
