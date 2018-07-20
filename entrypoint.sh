#! /bin/bash
##
# Due to some restrictions with the Azure platform we need to
# run filebeat alongside the application in the same container.
# In order to achieve this we need an entrypoint file

echo "cshr-scanning-service: In entrypoint.sh"

echo "entrypoint.sh: command passed: " ${1}

if [[ ${#} -eq 0 ]]; then
    # -E to preserve the environment
    echo "Starting filebeat"
    sudo -E filebeat -e -c /etc/filebeat/filebeat.yml &
    echo "Starting application"
    java -Djava.security.egd=file:/dev/./urandom \
        -Dav.service.hostname=${AV_SERVICE_HOSTNAME} \
        -Dav.service.port=${AV_SERVICE_PORT} \
        -Dserver.port=${SERVER_PORT} \
        -Dspring.security.user.password=${SPRING_SECURITY_SERVICE_PASSWORD} \
        -Dspring.security.user.name=${SPRING_SECURITY_SERVICE_USERNAME} \
        -jar /app/cshr-scanning-service.jar
else
    echo "Running command:"
    exec "$@"
fi
