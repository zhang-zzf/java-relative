select now();

CREATE TABLE events
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    -- 存储用户实际看到和输入的时间
    ds_local_time   DATETIME    NOT NULL,
    -- 存储该时间对应的时区（e.g., 'America/New_York', 'Asia/Shanghai'）
    tz              VARCHAR(64) NOT NULL,
    -- 可选：额外存储一个UTC时间，便于查询和计算
    ds_utc_time     DATETIME    NOT NULL,
    ts_utc_time     timestamp   NOT NULL,
    `ds_created_at` datetime  DEFAULT CURRENT_TIMESTAMP,
    `ds_updated_at` datetime  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `ts_created_at` timestamp DEFAULT CURRENT_TIMESTAMP,
    `ts_updated_at` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


select *
from events;

insert into events(ds_local_time, tz, ds_utc_time, ts_utc_time)
values ('2025-09-10 08:00:00', 'Asia/Shanghai', '2025-09-10 00:00:00', '2025-09-10 00:00:00');

insert into events(ds_local_time, tz, ds_utc_time, events.ts_utc_time)
values ('2025-09-11 08:00:00', 'Asia/Shanghai', '2025-09-11 00:00:00', '2025-09-11 00:00:00');

truncate events;
