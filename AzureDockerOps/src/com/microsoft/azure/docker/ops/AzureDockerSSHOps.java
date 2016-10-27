package com.microsoft.azure.docker.ops;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.microsoft.azure.docker.resources.AzureDockerException;
import com.microsoft.azure.docker.resources.DockerHost;

public class AzureDockerSSHOps {
  public static Session createLoginInstance(DockerHost dockerHost) {
    if (dockerHost == null) {
      throw new AzureDockerException("Unexpected param values; dockerHost cannot be null");
    }

    try {
      JSch jsch = new JSch();
      Session session = jsch.getSession(dockerHost.certVault.vmUsername, dockerHost.hostVM.dnsName);
      session.setPassword(dockerHost.certVault.vmPwd);
      session.setConfig("StrictHostKeyChecking", "no");
      session.connect();

      return session;
    } catch (Exception e) {
      throw new AzureDockerException(e.getMessage(), e);
    }
  }

  public static void executeCommand() {
    try {

    } catch (Exception e) {
      throw new AzureDockerException(e.getMessage(), e);
    }
  }
}
