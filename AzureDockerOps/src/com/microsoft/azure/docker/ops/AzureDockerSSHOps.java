package com.microsoft.azure.docker.ops;

import com.jcraft.jsch.*;
import com.microsoft.azure.docker.resources.AzureDockerException;
import com.microsoft.azure.docker.resources.DockerHost;

import java.io.*;
import java.net.URI;

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

  public static String executeCommand(String command, Session session, Boolean getExitStatus) {
    String result = "";
    try {
      Channel channel = session.openChannel("exec");
      ((ChannelExec)channel).setCommand(command);
      InputStream commandOutput = channel.getInputStream();
      channel.connect();
      byte[] tmp  = new byte[1024];
      while(true){
        while(commandOutput.available()>0){
          int i=commandOutput.read(tmp, 0, 1024);
          if(i<0)break;
          result += new String(tmp, 0, i);
        }
        if(channel.isClosed()){
          if(commandOutput.available()>0) continue;
          if (getExitStatus)
            result += "exit-status: "+channel.getExitStatus();
          break;
        }
        try{Thread.sleep(100);}catch(Exception ee){}
      }
      channel.disconnect();

      return result;
    } catch (Exception e) {
      throw new AzureDockerException(e.getMessage(), e);
    }
  }

  public static String download(String fileName, String fromPath, Session session) {
    try {
      ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
      channel.connect();
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      BufferedOutputStream buff = new BufferedOutputStream(outputStream);
      channel.cd(fromPath);
      channel.get(fileName, buff);

      channel.disconnect();

      return outputStream.toString();
    } catch (Exception e) {
      throw new AzureDockerException(e.getMessage(), e);
    }
  }

  public static void download(String fileName, String fromPath, String toPath, Session session) {
    try {
      ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
      channel.connect();
      File toFile = new File(toPath, fileName);
      OutputStream outputStream = new FileOutputStream(toFile);
      BufferedOutputStream buff = new BufferedOutputStream(outputStream);
      channel.cd(fromPath);
      channel.get(fileName, buff);

      channel.disconnect();
    } catch (Exception e) {
      throw new AzureDockerException(e.getMessage(), e);
    }
  }

  public static void upload(InputStream from, String fileName, String toPath, Session session) {
    try {
      ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
      channel.connect();
      channel.cd(toPath);
      channel.put(from, fileName);

      channel.disconnect();
    } catch (Exception e) {
      throw new AzureDockerException(e.getMessage(), e);
    }
  }

  public static void upload(String fileName, String fromPath, String toPath, Session session) {
    try {
      ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
      channel.connect();
      FileInputStream inputStream = new FileInputStream(fromPath + File.separator + fileName);
      channel.cd(toPath);
      channel.put(inputStream, fileName);

      channel.disconnect();
    } catch (Exception e) {
      throw new AzureDockerException(e.getMessage(), e);
    }
  }
}
