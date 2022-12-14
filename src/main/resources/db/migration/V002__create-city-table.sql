create table city
(
    id         bigserial primary key,
    country_id bigint,
    name       varchar(255),
    foreign key (country_id) references country (id)
);