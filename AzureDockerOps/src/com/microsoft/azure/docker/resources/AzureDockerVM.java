package com.microsoft.azure.docker.resources;

import java.util.Map;

public class AzureDockerVM {
  public String name;
  public String resourceGroupName;
  public String region;
  public String availabilitySet;
  public String vnetName;
  public String subnetName;
  public String networkSecurityGroupName;
  public String publicIpName;
  public String publicIp;
  public String dnsName;
  public String storageAccountName;
  public String osDiskName;
  public AzureOSHost osHost;
  public Map<String, String> tags;
}
