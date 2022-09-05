create sequence hibernate_sequence;

alter sequence hibernate_sequence owner to admin;

create table file
(
    id   bigint not null
        constraint file_pkey
            primary key,
    data bytea  not null,
    uuid uuid   not null
);

alter table file
    owner to admin;

create unique index file_uuid_uindex
    on file (uuid);

create table metadata
(
    extension  varchar(255)      not null,
    file_state varchar(255) check ( file_state in ('UPLOADING', 'READY', 'TO_BE_DELETED') ) not null,
    name       varchar(255)      not null,
    size       bigint            not null,
    file_id    bigint            not null
        constraint metadata_pkey
            primary key
        constraint metadata_file_fk
            references file
);

alter table metadata
    owner to admin;
