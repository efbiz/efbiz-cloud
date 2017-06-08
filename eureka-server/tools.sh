#!/bin/sh

case "$1" in
 start)
  case "$2" in
   test | demo)
    java -Xms256m -Xmx512m -Dspring.profiles.active="$2" -jar ./eureka-server.jar >/dev/null 2>&1 &
   ;;
   prod)
    java -Xms256m -Xmx512m -Dspring.profiles.active="$2" -jar ./eureka-server.jar >/dev/null 2>&1 &
   ;;
   *)
    printf 'append active profile: %s {test|demo|prod}\n' "$prog"
    exit 1
   ;;
  esac
 ;;
 stop)
  ps -ef|grep eureka-server.jar|awk '{print $2}'|while read pid
  do
    kill -15 $pid
  done
 ;;
 *)
  printf 'Usage: %s {start|stop}\n' "$prog"
  exit 1
 ;;
esac