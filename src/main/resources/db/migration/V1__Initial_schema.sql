create table if not exists customer
(
    id     bigint auto_increment
        primary key,
    name   varchar(50) null,
    email  varchar(20) null,
    mobile varchar(15) null
);
