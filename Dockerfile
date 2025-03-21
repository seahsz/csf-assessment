# Build the client
FROM node:23 AS ngBuilder

WORKDIR /ngSrc

# Install Angular
RUN npm i -g @angular/cli

# Copy Angular source
COPY client/*.json .
COPY client/public public
COPY client/src src

# Run npm to install node_modules -> package.json
RUN npm ci && ng build

# Build from Spring Boot
FROM eclipse-temurin:23-jdk AS jBuilder

WORKDIR /javaSrc

COPY server/.mvn .mvn
COPY server/src src
COPY server/mvnw .
COPY server/pom.xml .

# Copy Angular files over to static
# Remove the * from browser/* because * only copies direct files, not the subdirectories!!!
COPY --from=ngBuilder /ngSrc/dist/client/browser/ src/main/resources/static/

RUN chmod a+x mvnw && ./mvnw package -Dmaven.test.skip-true

# Copy the JAR file over to the final container
FROM eclipse-temurin:23-jre

WORKDIR /myapp

# TO EDIT: Get the name of the jar file after running mvn package -Dmaven.test.skip=true
COPY --from=jBuilder /javaSrc/target/server-0.0.1-SNAPSHOT.jar app.jar

ENV PORT=3000

# TO EDIT: Other environment variables
ENV PAYMENT_PAYEE=

ENV SPRING_DATA_MONGODB_URI=

ENV SPRING_DATSOURCE_URL=
ENV SPRING_DATASOURCE_USERNAME=
ENV SPRING_DATASOURCE_PASSWORD=

EXPOSE ${PORT}

SHELL [ "/bin/sh", "-c" ]
ENTRYPOINT SERVERPORT=${PORT} java -jar app.jar
