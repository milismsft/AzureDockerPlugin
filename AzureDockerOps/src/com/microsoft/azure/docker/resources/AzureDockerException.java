package com.microsoft.azure.docker.resources;

public class AzureDockerException extends RuntimeException {
  public AzureDockerException(String message) {super(message);}
  public AzureDockerException(String message, Exception e) {super(message, e);}
}
