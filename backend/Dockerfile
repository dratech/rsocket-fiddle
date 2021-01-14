FROM gradle:6.8.0-jdk15
EXPOSE 8080 8085

COPY . /home/rsocket-fiddle
WORKDIR /home/rsocket-fiddle

CMD ["gradle", "bootrun"]