FROM @JRE@

USER root
WORKDIR /root/docker-compile

RUN mkdir /root/docker-stage

# update packages and install dependencies: wget
RUN if type "apt-get" > /dev/null; then \
      apt-get update && apt-get install -y wget procps; \
    else \
      yum install -y wget procps; \
    fi

ENV TOMCAT_MAJOR_VERSION 7
ENV TOMCAT_FULL_VERSION 7.0.94

# install tomcat
RUN wget https://archive.apache.org/dist/tomcat/tomcat-$TOMCAT_MAJOR_VERSION/v$TOMCAT_FULL_VERSION/bin/apache-tomcat-$TOMCAT_FULL_VERSION.tar.gz \
    && wget https://archive.apache.org/dist/tomcat/tomcat-$TOMCAT_MAJOR_VERSION/v$TOMCAT_FULL_VERSION/bin/apache-tomcat-$TOMCAT_FULL_VERSION.tar.gz.sha512 \
    && sha512sum --check apache-tomcat-$TOMCAT_FULL_VERSION.tar.gz.sha512 \
    && tar xzvf apache-tomcat-$TOMCAT_FULL_VERSION.tar.gz \
    && mv ./apache-tomcat-$TOMCAT_FULL_VERSION /opt/apache-tomcat-$TOMCAT_FULL_VERSION

ENV CATALINA_HOME /opt/apache-tomcat-$TOMCAT_FULL_VERSION
ENV CATALINA_BASE /opt/apache-tomcat-$TOMCAT_FULL_VERSION

ADD ./*.sh /root/docker-stage/

# agent related stuff
RUN mkdir /root/docker-stage/aiagent
ENV AGENT_JAR_NAME @AGENT_JAR_NAME@
ADD ./aiagent/ /root/docker-stage/aiagent/
ADD ./*_AI-Agent.xml /root/docker-stage/

EXPOSE 8080

WORKDIR /root/docker-stage
CMD ./startServer.sh