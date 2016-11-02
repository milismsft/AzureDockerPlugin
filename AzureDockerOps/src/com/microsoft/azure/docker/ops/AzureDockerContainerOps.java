package com.microsoft.azure.docker.ops;

import com.microsoft.azure.docker.resources.AzureDockerException;
import com.microsoft.azure.docker.resources.DockerContainer;
import com.microsoft.azure.docker.resources.DockerHost;
import com.microsoft.azure.docker.resources.DockerImage;

import java.util.List;

public class AzureDockerContainerOps {
  public static DockerContainer get(String name, DockerHost dockerHost) {
    DockerContainer container = new DockerContainer();
    container.dockerImage = new DockerImage();
    container.dockerImage.dockerHost = dockerHost;

    return get(container);
  }

  public static DockerContainer get(DockerContainer dockerContainer) {
    if (dockerContainer == null || dockerContainer.dockerImage == null || dockerContainer.dockerImage.dockerHost == null) {
      throw new AzureDockerException("Unexpected argument values; dockerContainer, dockerImage, dockerHost can not be null");
    }
    try {
      DockerHost dockerHost = dockerContainer.dockerImage.dockerHost;
      if (dockerHost.session == null || !dockerHost.session.isConnected()) {
        dockerHost.session = AzureDockerSSHOps.createLoginInstance(dockerHost);
      }

      return null;
    } catch (Exception e) {
      throw new AzureDockerException(e.getMessage(), e);
    }
  }

  public static String getDetails(String name, DockerHost dockerHost) {
    DockerContainer container = new DockerContainer();
    container.dockerImage = new DockerImage();
    container.dockerImage.dockerHost = dockerHost;

    return getDetails(container);
  }

  public static String getDetails(DockerContainer dockerContainer) {
    if (dockerContainer == null || dockerContainer.dockerImage == null || dockerContainer.dockerImage.dockerHost == null) {
      throw new AzureDockerException("Unexpected argument values; dockerContainer, name/id, dockerImage, dockerHost can not be null");
    }
    try {
      DockerHost dockerHost = dockerContainer.dockerImage.dockerHost;
      if (dockerHost.session == null || !dockerHost.session.isConnected()) {
        dockerHost.session = AzureDockerSSHOps.createLoginInstance(dockerHost);
      }

      String dockerContainerName = AzureDockerUtils.isValid(dockerContainer.id) ? dockerContainer.id : dockerContainer.name;

      return AzureDockerSSHOps.executeCommand("docker inspect " + dockerContainerName, dockerHost.session, false);
    } catch (Exception e) {
      throw new AzureDockerException(e.getMessage(), e);
    }
  }

  public static void start(DockerContainer dockerContainer) {
    try {
    } catch (Exception e) {
      throw new AzureDockerException(e.getMessage(), e);
    }
  }

  public static void stop(DockerContainer dockerContainer) {
    try {
    } catch (Exception e) {
      throw new AzureDockerException(e.getMessage(), e);
    }
  }

  public static void delete(DockerContainer dockerContainer) {
    try {
    } catch (Exception e) {
      throw new AzureDockerException(e.getMessage(), e);
    }
  }

  public static DockerContainer create(DockerContainer dockerContainer) {
    try {
      return null;
    } catch (Exception e) {
      throw new AzureDockerException(e.getMessage(), e);
    }
  }

  public static List<DockerContainer> list(DockerContainer dockerContainer) {
    try {
      return null;
    } catch (Exception e) {
      throw new AzureDockerException(e.getMessage(), e);
    }
  }

  public static List<DockerContainer> list(DockerImage dockerImage) {
    try {
      return null;
    } catch (Exception e) {
      throw new AzureDockerException(e.getMessage(), e);
    }
  }
}
