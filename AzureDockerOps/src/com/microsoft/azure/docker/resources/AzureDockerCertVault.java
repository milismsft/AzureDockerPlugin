package com.microsoft.azure.docker.resources;

import groovy.transform.Canonical;

@Canonical
public class AzureDockerCertVault {
  public String name;                   // Vault's name
  public String hostName;               // name of the host (Docker host) to store the credentials for
  public String availabilitySet;        // Azure availability set where to create the vault
  public String resourceGroupName;      // Azure resource group where to create the vault
  public String region;                 // Azure location where to create the vault
  public String servicePrincipalId;     // service principal's client id if user logged in with service principal credentials
  public String userName;               // current user name as logged in via Azure Auth
  public String vmUsername;             // username to be used fro VM (Docker host) login
  public String vmPwd;                  // password to be used for VM (Docker host) login
  public String sshKey;                 // see "id_rsa"
  public String sshPubKey;              // see "id_rsa.pub"
  public String tlsCACert;              // see "ca.pem"
  public String tlsCert;                // see "cert.pem"
  public String tlsClientKey;           // see "key.pem"
  public String tlsHostCert;            // see "server.pem"
  public String tlsServerKey;           // see "server-key.pem"
}
