FROM openjdk:8-jdk

ARG CACHE_DATE=2017-07-23

# copy jar file
ADD target/Rapido-0.0.1.jar /

ADD src/main/webapp /src/main/webapp

EXPOSE 8080
