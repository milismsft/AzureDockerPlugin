package com.microsoft.azure.docker.ops;

public class AzureDockerUtils {
  public static Boolean isValid(String str) {
    return str != null && !str.isEmpty();
  }
}
