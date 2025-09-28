#!/usr/bin/env bash
set -euo pipefail

APP_DIR=/home/ubuntu/app
SERVICE_NAME=chat
SERVICE_FILE=/etc/systemd/system/${SERVICE_NAME}.service

# JAR 경로 선택: (1) 고정 파일명, (2) Gradle 산출물에서 최신 SNAPSHOT
JAR_CANDIDATE=""
if [[ -f "${APP_DIR}/Chat-0.0.1-SNAPSHOT.jar" ]]; then
  JAR_CANDIDATE="${APP_DIR}/Chat-0.0.1-SNAPSHOT.jar"
else
  JAR_CANDIDATE="$(ls -1 ${APP_DIR}/build/libs/*SNAPSHOT.jar 2>/dev/null | tail -n1 || true)"
fi

# 표준 실행 파일명으로 복사
if [[ -n "${JAR_CANDIDATE}" ]]; then
  cp -f "${JAR_CANDIDATE}" "${APP_DIR}/Chat.jar"
fi

# systemd 유닛 파일(최초 1회 생성)
if [[ ! -f "${SERVICE_FILE}" ]]; then
  cat <<'EOF' > "/tmp/chat.service"
[Unit]
Description=Chat Spring Boot
After=network.target

[Service]
User=ubuntu
WorkingDirectory=/home/ubuntu/app
ExecStart=/usr/bin/java -Xms256m -Xmx512m -jar /home/ubuntu/app/Chat.jar
Environment=JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8
Environment=SPRING_PROFILES_ACTIVE=default
Restart=always
RestartSec=5
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
EOF
  mv /tmp/chat.service "${SERVICE_FILE}"
  systemctl daemon-reload
  systemctl enable "${SERVICE_NAME}"
fi

# 애플리케이션 재기동
systemctl stop "${SERVICE_NAME}" || true
systemctl start "${SERVICE_NAME}"

# 기동 확인(최대 60초 대기) — 필요한 엔드포인트로 바꿔도 됨
for i in {1..30}; do
  if curl -fsS http://127.0.0.1:8080/chat.html >/dev/null; then
    echo "App is up"
    exit 0
  fi
  sleep 2
done

echo "App failed to start. Recent logs:"
journalctl -u "${SERVICE_NAME}" --no-pager -n 200 || true
exit 1