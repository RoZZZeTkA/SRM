create table usr (
                    id  serial not null,
                    first_name varchar(255),
                    second_name varchar(255),
                    role varchar(255),
                    username varchar(255),
                    password varchar(255),
                    primary key (id)
);

create table department (
                    id  serial not null,
                    name varchar(255),
                    primary key (id)
);