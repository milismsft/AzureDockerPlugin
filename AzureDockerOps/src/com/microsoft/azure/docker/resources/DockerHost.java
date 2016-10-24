package com.microsoft.azure.docker.resources;

import java.util.List;

public class DockerHost {
  public String name;
  public AzureDockerCertVault certVault;
  public String apiUrl;
  public String port;
  public Boolean hasReleaseConfig;
  public Boolean hasDebugConfig;
  public AzureDockerVM host;
  public List<DockerImage> dockerImages;
  public Boolean isTLSSecured;
}
