package com.microsoft.azure.docker.ops;

import com.microsoft.azure.docker.resources.AzureDockerException;
import com.microsoft.azure.docker.resources.DockerImage;

public class AzureDockerImageOps {

  public static void delete(DockerImage dockerImage) {
    try {
    } catch (Exception e) {
      throw new AzureDockerException(e.getMessage(), e);
    }
  }

  public static DockerImage create(DockerImage dockerImage) {
    try {
      return null;
    } catch (Exception e) {
      throw new AzureDockerException(e.getMessage(), e);
    }
  }

  public static DockerImage get(DockerImage dockerImage) {
    try {
      return null;
    } catch (Exception e) {
      throw new AzureDockerException(e.getMessage(), e);
    }
  }

  public static String getDetails(DockerImage dockerImage) {
    try {
      return null;
    } catch (Exception e) {
      throw new AzureDockerException(e.getMessage(), e);
    }
  }

}
