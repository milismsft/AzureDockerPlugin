package com.microsoft.azure.docker.resources;

import com.jcraft.jsch.Session;

import java.util.List;

public class DockerHost {
  public String name;
  public AzureDockerCertVault certVault;
  public String apiUrl;
  public String port;
  public Boolean hasReleaseConfig;
  public Boolean hasDebugConfig;
  public AzureDockerVM hostVM;
  public List<DockerImage> dockerImages;
  public Boolean isTLSSecured;
  public DockerHostOSType hostOSType;
  public Session session;

  public enum DockerHostOSType {
    UBUNTU_SERVER_14,
    UBUNTU_SERVER_16,
    UBUNTU_SNAPPY_CORE,
    COREOS,
    OPENLOGIC_CENTOS,
    LINUX_OTHER
  }
}
