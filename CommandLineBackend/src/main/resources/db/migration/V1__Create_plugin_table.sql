create table Plugin (
    id         serial primary key,
    plugin_identifier varchar(100) not null unique,
    jar_content bytea not null
);