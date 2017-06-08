#!/bin/sh

case "$1" in
 start)
   java -Xms256m -Xmx512m -Dspring.profiles.active="$2" -jar ./admin-dashboard.jar >/dev/null 2>&1 &
    exit 1
 ;;
 stop)
  ps -ef|grep admin-dashboard.jar|awk '{print $2}'|while read pid
  do
    kill -15 $pid
  done
 ;;
 *)
  printf 'Usage: %s {start|stop}\n' "$prog"
  exit 1
 ;;
esac