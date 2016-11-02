package com.microsoft.azure.docker.resources;

public class DockerContainer {
  public String id;
  public String name;
  public DockerContainerStatus status;
  public long size;

  public DockerImage dockerImage; // parent docker image

  public enum DockerContainerStatus {
    UNKNOWN,
    STOPPED,
    RUNNING,
    PAUSED
  }
}
