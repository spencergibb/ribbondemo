# Ribbon Demo

## Setup
* run three instances (`--server.port=9092`, `--server.port=9093`)
* see http://localhost:9091 rotate through

## service wide server list filter
* `http --form POST :9091/env demo.filterPattern="*:*2"`
* see http://localhost:9091 only 9092
* `http --form POST :9091/env demo.filterPattern=""`
* see http://localhost:9091 rotate through again

## request based filter

* visit http://localhost:9091/?X-Ribbon-Server=*:*3
* only 9093