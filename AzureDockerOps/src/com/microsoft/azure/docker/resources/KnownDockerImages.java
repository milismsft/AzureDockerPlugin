package com.microsoft.azure.docker.resources;

import static com.microsoft.azure.docker.resources.KnownDockerImages.KnownDefaultDockerfiles.JBOSS_WILDFLY_DEFAULT_DOCKERFILE;

public enum KnownDockerImages {

  JBOSS_WILDFLY(JBOSS_WILDFLY_DEFAULT_DOCKERFILE);

  private final String dockerfileContent;

  KnownDockerImages(String dockerfile) {
    this.dockerfileContent = dockerfile;
  }

  /*
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
  */
  public static class KnownDefaultDockerfiles {
    public static final String JBOSS_WILDFLY_DEFAULT_DOCKERFILE =
        "FROM jboss/wildfly\n" +
        "ADD ArtifactApp.war /opt/jboss/wildfly/standalone/deployments/\n";

  }
}

