FROM adoptopenjdk/openjdk11:ubi

RUN mkdir /opt/app
RUN npm install -g @angular/cli


COPY japp.jar /opt/app

CMD ["java", "-jar", "/opt/app/japp.jar"]