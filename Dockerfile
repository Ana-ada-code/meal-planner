FROM openjdk:17-jdk-slim

COPY . /app

WORKDIR /app

RUN apt-get update && apt-get install -y maven

RUN mvn clean package -DskipTests

COPY start.sh /start.sh

RUN chmod +x /start.sh

EXPOSE 8080

CMD ["/start.sh"]