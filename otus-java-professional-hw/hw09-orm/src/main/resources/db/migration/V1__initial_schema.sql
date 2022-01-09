create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

create table manager
(
    no   BIGSERIAL NOT NULL PRIMARY KEY,
    label VARCHAR(50),
    param1 VARCHAR(50)
);