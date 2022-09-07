docker run -d -e POSTGRES_USER=local -e POSTGRES_PASSWORD=root -e POSTGRES_DB=pizza_db -p 5432:5432  postgres

docker run -d -it --rm \
  -p 8161:8161 \
  -p 61616:61616 \
  vromero/activemq-artemis

docker run -d -p 9411:9411 openzipkin/zipkin