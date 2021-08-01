create table people
(
    id     bigint auto_increment,
    name   varchar(255) not null,
    sex    varchar(255) not null,
    weight smallint     not null,
    primary key (id)
)