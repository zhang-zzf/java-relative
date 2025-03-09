#!/usr/bin/env bash

# MySQL
ssh -L :3306:10.0.9.18:8306 -N -f 127.0.0.1
# Redis
ssh -L :6379:10.0.9.18:6379 -N -f 127.0.0.1
# start consul in background
bash ~/Workspace/consul/start_agent_in_mac.sh