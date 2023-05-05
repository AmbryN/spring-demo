FROM tomcat:8.5
COPY target/demo-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/demo-0.0.1-SNAPSHOT.war
ENTRYPOINT ["/bin/bash", "/usr/local/tomcat/bin/catalina.sh", "run"]

