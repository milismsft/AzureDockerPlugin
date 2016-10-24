package com.microsoft.azure.docker.resources;

import com.microsoft.azure.management.Azure;

import java.util.List;

public class AzureSubscriptions {
  public String name;
  public String sid;
  public Azure azureClient;
  public List<String> locations;
}
