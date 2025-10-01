#!/usr/bin/env bash
set -euo pipefail

REPOSITORY=/home/ubuntu/app
PORT=8080

echo "> 현재 구동 중인 애플리케이션 pid 확인"
CURRENT_PID=$(sudo lsof -t -i:$PORT || true)
if [ -n "${CURRENT_PID}" ]; then
  echo "> kill -9 $CURRENT_PID"
  kill -9 "$CURRENT_PID"
  sleep 5
fi

echo "> 새 애플리케이션 배포"
JAR_NAME=$REPOSITORY/deploy/app.jar
chmod +x "$JAR_NAME"

echo "> 애플리케이션 실행"
nohup java \
  -Dfile.encoding=UTF-8 \
  -Duser.timezone=Asia/Seoul \
  -jar "$JAR_NAME" \
  --spring.config.location=classpath:/,file:$REPOSITORY/deploy/ \
  --spring.profiles.active=prod \
  >> "$REPOSITORY/nohup.out" 2>&1 &