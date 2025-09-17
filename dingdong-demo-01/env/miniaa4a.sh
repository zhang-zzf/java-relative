#!/usr/bin/env bash

# MySQL
# 本机为 mini243b, 使用本机作为 ssh_server
# ssh -L :3306:192.168.56.20:8306 -N -f mini243b
#
# 使用目标服务器作为 ssh_server. 优势: 不用关心本机是什么, 劣势：ssh 到目标服务器需要输入密码
# ssh -L :3306:192.168.56.20:8306 -N -f admin@192.168.56.20
#
# 同局域网内优先使用
# 使用本机作为 ssh_server. 优势: 不关心本机是什么，无需 ssh 到目标服务器，劣势: 本机必须可以访问目标服务器的端口
# 实战中：我的 ~/.ssh/known_hosts 在不同机器上共享，导致 127.0.0.1 Host key verification failed
# ssh -L :3306:192.168.56.20:8306 -N -f 127.0.0.1
#
# 优化
# $(hostname) 一般指向本机 127.0.0.1
ssh -L :3306:192.168.56.20:8306 -N -f "$(hostname)"
# MySQL tz = UTC
ssh -L :3307:192.168.56.20:8307 -N -f "$(hostname)"

# Redis
ssh -L :6379:192.168.56.20:6379 -N -f "$(hostname)"