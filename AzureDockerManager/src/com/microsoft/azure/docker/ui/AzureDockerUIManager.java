package com.microsoft.azure.docker.ui;

import com.microsoft.azure.docker.resources.AzureDockerSubscription;
import com.microsoft.azure.management.Azure;

import java.util.List;

public class AzureDockerUIManager {
  public Azure azureMainClient;
  public List<AzureDockerSubscription> subscriptionList;
}
