#!/bin/bash

CURRENT_PORT=$(cat /home/ec2-user/service_url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0

echo "> Current port of running JAR is ${CURRENT_PORT}."

if [ ${CURRENT_PORT} -eq 8080 ]; then
  TARGET_PORT=8081
elif [ ${CURRENT_PORT} -eq 8081 ]; then
  TARGET_PORT=8080
else
  echo "> No JAR is connected to nginx"
fi

TARGET_PID=${lsof -Fp -i TCP:${TARGET_PORT} | grep -Po 'p[0-9]+' | grep -Po '[0-9]+'}

echo "${TARGET_PID} 여기 숫자 나오면 Kill 해야함 !!"

if [ ! -z ${TARGET_PID} ]; then
  echo "> Kill JAR running at ${TARGET_PORT}."
  sudo kill -9 ${TARGET_PID}
fi

nohup java -jar -Dserver.port=${TARGET_PORT} -Dspring.profiles.active=dev /home/ec2-user/travel-zip/build/libs/* > /home/ec2-user/nohup.out 2>&1 &
echo "> Now new jar runs at ${TARGET_PORT}."
exit 0