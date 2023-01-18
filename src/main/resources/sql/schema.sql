create table usr (
                    id  serial not null,
                    role varchar(255),
                    username varchar(255) unique,
                    password varchar(255),
                    primary key (id)
);

create table department (
                    id  serial not null,
                    name varchar(255) unique,
                    primary key (id)
);