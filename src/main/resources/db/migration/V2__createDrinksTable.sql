create table drinks
(
    id                bigint auto_increment,
    alcohol_percent   decimal(19, 2) not null,
    amount_of_alcohol decimal(19, 2) not null,
    name              varchar(255)   not null,
    time              timestamp      not null,
    volume            decimal(19, 2) not null,
    person_id         bigint,
    primary key (id)
);
alter table drinks
    add constraint FK8PersonDrinks foreign key (person_id) references people (id);

