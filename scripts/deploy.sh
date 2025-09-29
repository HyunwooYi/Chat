#!/usr/bin/env bash
set -euo pipefail

REPOSITORY=/home/ubuntu/app
PORT=8080

echo "> 현재 구동 중인 애플리케이션 pid 확인"
CURRENT_PID=$(sudo lsof -t -i:$PORT || true)

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동 중인 애플리케이션이 없음"
else
  echo "> kill -9 $CURRENT_PID"
  kill -9 $CURRENT_PID
  sleep 5
fi

echo "> 새 애플리케이션 배포"

JAR_NAME=$REPOSITORY/deploy/app.jar
echo "> JAR NAME: $JAR_NAME"

echo "> 실행 권한 추가"
chmod +x $JAR_NAME

echo "> 애플리케이션 실행"
nohup java -Duser.timezone=Asia/Seoul -jar "$JAR_NAME" \
  --spring.config.location=$REPOSITORY/deploy/application.yml \
  >> "$REPOSITORY/nohup.out" 2>&1 &