package com.microsoft.azure.docker.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DockerImage {
  public String name;
  public String artifactName;    // .war or .jar output file representing the application to be deployed and run
  public String ports;           // container᾿s port or a range of ports to the host to be published (i.e. "1234-1236:1234-1236/tcp")
  public String customContent;   // user input Dockerfile content
  public String imageBase;       // see FROM directive
  public String exposeCMD;       // see EXPOSE directive
  public List<String> addCMDs;   // see ADD directive
  public List<String> runCMDs;   // see RUN directive
  public List<String> copyCMDs;  // see COPY directive
  public List<String> envCMDs;   // see ENV directive
  public List<String> workCMDs;  // see WORK directive

  DockerImage() {}

  DockerImage(String name, String customContent, String ports, String artifactName) {
    this.name = name;
    this.customContent = customContent;
    this.ports = ports;
    this.artifactName = artifactName;
  }

  public static final Map<String, DockerImage> defaultImages;
  static {
    Map<String, DockerImage> tempMap = new HashMap<>();
    tempMap.put("WildFly", new DockerImage("defaultWildFly",
        "FROM jboss/wildfly\n" +
            "ADD ArtifactApp.war /opt/jboss/wildfly/standalone/deployments/\n",
        "8080:8080",
        "ArtifactApp.war"));
    tempMap.put("Tomcat8", new DockerImage("defaultTomcat8",
        "FROM tomcat:8.0.20-jre8\n" +
            "ADD ArtifactApp.war /usr/local/tomcat/webapps/\n",
        "8080:8080",
        "ArtifactApp.war"));
    defaultImages = tempMap;
  }
}
