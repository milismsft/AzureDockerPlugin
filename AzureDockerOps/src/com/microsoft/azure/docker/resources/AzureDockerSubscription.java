package com.microsoft.azure.docker.resources;

import com.microsoft.azure.management.Azure;

import java.util.List;

public class AzureDockerSubscription {
  public String name;
  public String id;
  public Azure azureClient;
  public List<String> locations;
  public Boolean selected;
}
