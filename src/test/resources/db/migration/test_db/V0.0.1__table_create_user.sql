create table `user`
(
  `id`   bigint      not null auto_increment,
  `name` varchar(64) not null,
  `age`  smallint    not null default 0,
  primary key (`id`),
  unique key idx_n (name)
);
