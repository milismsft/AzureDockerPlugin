package com.microsoft.azure.docker.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DockerImage {
  public String name;
  public String id;
  public String repository;
  public String tag;
  public long virtualSize;
  public String artifactName;    // .war or .jar output file path representing the application to be deployed and run
  public String ports;           // container᾿s port or a range of ports to the host to be published (i.e. "1234-1236:1234-1236/tcp")
  public String dockerfile;      // Dockerfile input from which the image will be created
  public String imageBase;       // see FROM directive
  public String exposeCMD;       // see EXPOSE directive
  public List<String> addCMDs;   // see ADD directive
  public List<String> runCMDs;   // see RUN directive
  public List<String> copyCMDs;  // see COPY directive
  public List<String> envCMDs;   // see ENV directive
  public List<String> workCMDs;  // see WORK directive

  public DockerHost dockerHost;  // parent Docker host

  public DockerImage() {}

  public DockerImage(String name, String customContent, String ports, String artifactName) {
    this.name = name;
    this.dockerfile = customContent;
    this.ports = ports;
    this.artifactName = artifactName;
  }

}
