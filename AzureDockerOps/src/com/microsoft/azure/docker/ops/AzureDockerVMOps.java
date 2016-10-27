package com.microsoft.azure.docker.ops;

import com.jcraft.jsch.*;
import com.microsoft.azure.docker.resources.*;
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.compute.ImageReference;
import com.microsoft.azure.management.compute.VirtualMachine;
import com.microsoft.azure.management.compute.VirtualMachineSizeTypes;
import com.microsoft.azure.management.network.NicIpConfiguration;
import com.microsoft.azure.management.network.PublicIpAddress;
import com.microsoft.azure.management.resources.fluentcore.arm.ResourceUtils;
import com.microsoft.azure.management.resources.fluentcore.utils.ResourceNamer;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.microsoft.azure.management.resources.fluentcore.utils.ResourceNamer.randomResourceName;

public class AzureDockerVMOps {

  public static VirtualMachine createDefaultDockerHostVM(Azure azureClient, AzureDockerCertVault certVault) throws AzureDockerException {

    try {
      VirtualMachine.DefinitionStages.WithLinuxCreate defLinuxCreateStage = azureClient.virtualMachines().define(certVault.hostName)
          .withRegion(certVault.region)
          .withNewResourceGroup(certVault.resourceGroupName)
          .withNewPrimaryNetwork("10.0.0.0/16")
          .withPrimaryPrivateIpAddressDynamic()
          .withNewPrimaryPublicIpAddress(certVault.hostName)
          .withSpecificLinuxImageVersion(KnownDockerVirtualMachineImage.UBUNTU_SERVER_14_04_LTS.imageReference())
          .withRootUserName(certVault.vmUsername);

      VirtualMachine.DefinitionStages.WithCreate defCreateStage =
          ((certVault.vmPwd != null && !certVault.vmPwd.isEmpty())
              ? defLinuxCreateStage.withPassword(certVault.vmPwd)
              : defLinuxCreateStage.withSsh(certVault.sshPubKey))
              .withSize(VirtualMachineSizeTypes.STANDARD_DS2_V2);
      if (certVault.availabilitySet != null && !certVault.availabilitySet.isEmpty()) {
        defCreateStage = defCreateStage.withNewAvailabilitySet(certVault.availabilitySet);
      }
      defCreateStage =
          ((certVault.tlsHostCert != null && !certVault.tlsHostCert.isEmpty())
              ? defCreateStage.withTag("port", "2376") /* Default Docker host port when TLS is enabled */
              : defCreateStage.withTag("port", "2375")) /* Defalt Docker host port when TLS is disabled */
              .withTag("hostType", "Docker");

      return defCreateStage.create();
    } catch (Exception e) {
      throw new AzureDockerException(e.getMessage(), e);
    }
  }

  public static VirtualMachine createDockerHostVM(Azure azureClient, AzureDockerCertVault certVault, ImageReference imgRef) throws AzureDockerException {
    try {
      VirtualMachine.DefinitionStages.WithLinuxCreate defLinuxCreateStage = azureClient.virtualMachines().define(certVault.hostName)
          .withRegion(certVault.region)
          .withNewResourceGroup(certVault.resourceGroupName)
          .withNewPrimaryNetwork("10.0.0.0/16")
          .withPrimaryPrivateIpAddressDynamic()
          .withNewPrimaryPublicIpAddress(certVault.hostName)
          .withSpecificLinuxImageVersion(imgRef)
          .withRootUserName(certVault.vmUsername);

      VirtualMachine.DefinitionStages.WithCreate defCreateStage =
          ((certVault.vmPwd != null && !certVault.vmPwd.isEmpty())
              ? defLinuxCreateStage.withPassword(certVault.vmPwd)
              : defLinuxCreateStage.withSsh(certVault.sshPubKey))
              .withSize(VirtualMachineSizeTypes.STANDARD_DS2_V2);
      if (certVault.availabilitySet != null && !certVault.availabilitySet.isEmpty()) {
        defCreateStage = defCreateStage.withNewAvailabilitySet(certVault.availabilitySet);
      }
      defCreateStage =
          ((certVault.tlsHostCert != null && !certVault.tlsHostCert.isEmpty())
              ? defCreateStage.withTag("port", "2376") /* Default Docker host port when TLS is enabled */
              : defCreateStage.withTag("port", "2375")) /* Default Docker host port when TLS is disabled */
              .withTag("hostType", "Docker");

      return defCreateStage.create();
    } catch (Exception e) {
      throw new AzureDockerException(e.getMessage(), e);
    }
  }

  public static AzureDockerVM getDockerVM(Azure azureClient, String resourceGroup, String hostName) {
    try {
      VirtualMachine vm = azureClient.virtualMachines().getByGroup(resourceGroup, hostName);
      AzureDockerVM dockerVM = new AzureDockerVM();
      PublicIpAddress publicIp = vm.getPrimaryPublicIpAddress();
      NicIpConfiguration nicIpConfiguration = publicIp.getAssignedNetworkInterfaceIpConfiguration();

      dockerVM.name = vm.name();
      dockerVM.resourceGroupName = vm.resourceGroupName();
      dockerVM.region = vm.regionName();
      dockerVM.availabilitySet = (vm.availabilitySetId() != null) ? ResourceUtils.nameFromResourceId(vm.availabilitySetId()) : null;
      dockerVM.publicIpName = publicIp.name();
      dockerVM.publicIp = publicIp.ipAddress();
      dockerVM.dnsName = publicIp.fqdn();
      dockerVM.vnetName = nicIpConfiguration.getNetwork().name();
      dockerVM.subnetName = nicIpConfiguration.subnetName();
      dockerVM.networkSecurityGroupName = (nicIpConfiguration.parent().networkSecurityGroupId() != null) ? ResourceUtils.nameFromResourceId(nicIpConfiguration.parent().networkSecurityGroupId()) : null;
      dockerVM.storageAccountName = vm.storageProfile().osDisk().vhd().uri().split("[.]")[0].split("/")[2];
      dockerVM.osDiskName = vm.storageProfile().osDisk().name();
      if (vm.storageProfile().imageReference() != null) {
        dockerVM.osHost = new AzureOSHost();
        dockerVM.osHost.publisher = vm.storageProfile().imageReference().publisher();
        dockerVM.osHost.offer = vm.storageProfile().imageReference().offer();
        dockerVM.osHost.sku = vm.storageProfile().imageReference().sku();
        dockerVM.osHost.version = vm.storageProfile().imageReference().version();
      }
      dockerVM.tags = vm.tags();

      return dockerVM;
    } catch (Exception e) {
      throw new AzureDockerException(e.getMessage(), e);
    }
  }

  public static VirtualMachine getVM(Azure azureClient, String resourceGroup, String hostName) throws  AzureDockerException {
    try {
      return azureClient.virtualMachines().getByGroup(resourceGroup, hostName);
    } catch (Exception e) {
      throw new AzureDockerException(e.getMessage(), e);
    }
  }

  public static DockerHost getDockerHost(AzureDockerCertVault certVault, AzureDockerVM hostVM) {
    if (certVault == null || hostVM == null) {
      throw new AzureDockerException("Unexpected param values; certVault and hostVM cannot be null");
    }

    DockerHost dockerHost = new DockerHost();
    dockerHost.name = hostVM.name;
    dockerHost.certVault = certVault;
    dockerHost.apiUrl = hostVM.dnsName;
    dockerHost.isTLSSecured = certVault.tlsHostCert != null && !certVault.tlsHostCert.isEmpty();
    dockerHost.port = (hostVM.tags != null) ? hostVM.tags.get("port") : (dockerHost.isTLSSecured) ? "2376" : "2375";
    dockerHost.hasDebugConfig = false;
    dockerHost.hasReleaseConfig = false;
    dockerHost.hostVM = hostVM;
    dockerHost.dockerImages = new ArrayList<DockerImage>();

    if (hostVM.osHost != null) {
      switch (hostVM.osHost.offer) {
        case "Ubuntu_Snappy_Core":
          dockerHost.hostOSType = DockerHost.DockerHostOSType.UBUNTU_SNAPPY_CORE;
          break;
        case "CoreOS":
          dockerHost.hostOSType = DockerHost.DockerHostOSType.COREOS;
          break;
        case "CentOS":
          dockerHost.hostOSType = DockerHost.DockerHostOSType.OPENLOGIC_CENTOS;
          break;
        case "UbuntuServer":
          dockerHost.hostOSType = hostVM.osHost.offer.equals("14.04.4-LTS") ? DockerHost.DockerHostOSType.UBUNTU_SERVER_14 : DockerHost.DockerHostOSType.UBUNTU_SERVER_16;
          break;
        default:
          dockerHost.hostOSType = DockerHost.DockerHostOSType.LINUX_OTHER;
          break;
      }
    }

    return dockerHost;
  }

  public static void installDocker(DockerHost dockerHost) {
    if (dockerHost == null) {
      throw new AzureDockerException("Unexpected param values; dockerHost cannot be null");
    }

    try {
      switch (dockerHost.hostOSType) {
        case UBUNTU_SERVER_14:
        case UBUNTU_SERVER_16:
          installDockerOnUbuntuServer(dockerHost);
          break;
        default:
          throw new AzureDockerException("Docker host OS type is not supported");
      }
    } catch (Exception e) {
      throw new AzureDockerException(e.getMessage(), e);
    }
  }

  public static void installDockerOnUbuntuServer(DockerHost dockerHost) {
    if (dockerHost == null) {
      throw new AzureDockerException("Unexpected param values; dockerHost cannot be null");
    }

    try {
      // Generate a random password to be used when creating the TLS certificates
      String certCAPwd = ResourceNamer.randomResourceName("", 15);
      String createTLScerts = CREATE_OPENSSL_TLS_CERTS_FOR_UBUNTU;
      createTLScerts = createTLScerts.replaceAll(CERT_CA_PWD_PARAM, certCAPwd);
      createTLScerts = createTLScerts.replaceAll(HOSTNAME, dockerHost.hostVM.name);
      createTLScerts = createTLScerts.replaceAll(FQDN_PARAM, dockerHost.hostVM.dnsName);
      createTLScerts = createTLScerts.replaceAll(DOMAIN_PARAM, dockerHost.hostVM.dnsName.substring(dockerHost.hostVM.dnsName.indexOf('.')));

      System.out.println("Script to run:");
      System.out.println(createTLScerts);

      Session session = AzureDockerSSHOps.createLoginInstance(dockerHost);
      System.out.println("Start executing ssh");
      Channel channel = session.openChannel("exec");
      ((ChannelExec)channel).setCommand("ls /");
      InputStream commandOutput = channel.getInputStream();
      channel.connect();
      byte[] tmp  = new byte[1024];
      while(true){
        while(commandOutput.available()>0){
          int i=commandOutput.read(tmp, 0, 1024);
          if(i<0)break;
          System.out.print(new String(tmp, 0, i));
        }
        if(channel.isClosed()){
          if(commandOutput.available()>0) continue;
          System.out.println("exit-status: "+channel.getExitStatus());
          break;
        }
        try{Thread.sleep(100);}catch(Exception ee){}
      }
      System.out.println("Done executing ssh");
      channel.disconnect();
      session.disconnect();

    } catch (Exception e) {
      throw new AzureDockerException(e.getMessage(), e);
    }
  }

  private static final String INSTALL_DOCKER_FOR_UBUNTU =
      "sudo apt-get update \n" +
      "sudo apt-get -y install docker.io \n" +
      "sudo groupadd docker \n" +
      "sudo usermod -aG docker $USER \n";

  private static final String CREATE_DEFAULT_DOCKER_OPTS_NO_TLS =
      "sudo service docker stop \n" +
      "sudo echo DOCKER_OPTS=\\\"--tls=false -H tcp://0.0.0.0:2375 -H unix:///var/run/docker.sock\\\" > ~/docker" +
      "cp -f ~/docker /etc/default/docker" +
      "sudo service docker start \n";

  /* Script that creates the TLS certs; must be run on the Docker host after the VM is provisioned
   * Values to be replaced via String.replace()
   *  "$CERT_CA_PWD_PARAM$" - some randomly generated password
   *  "$HOSTNAME$" - Docker host name
   *  "$FQDN_PARAM$" - fully qualified name of the Docker host
   *  "$DNS_PARAM$" - domain of the Docker host
   */
  private static final String CERT_CA_PWD_PARAM = "[$]CERT_CA_PWD_PARAM[$]";
  private static final String HOSTNAME = "[$]HOSTNAME[$]";
  private static final String FQDN_PARAM = "[$]FQDN_PARAM[$]";
  private static final String DOMAIN_PARAM = "[$]DOMAIN_PARAM[$]";
  private static final String CREATE_OPENSSL_TLS_CERTS_FOR_UBUNTU =
      "mkdir ~/.tls \n" +
      "cd ~/.tls \n" +
      // Generate CA certificate
      "openssl genrsa -passout pass:$CERT_CA_PWD_PARAM$ -aes256 -out ca-key.pem 2048 \n" +
      // Generate Server certificates
      "openssl req -passin pass:$CERT_CA_PWD_PARAM$ -subj '/CN=Docker Host CA/C=US' -new -x509 -days 365 -key ca-key.pem -sha256 -out ca.pem \n" +
      "openssl req -passin pass:$CERT_CA_PWD_PARAM$ -subj '/CN=$HOSTNAME$' -sha256 -new -key ca-key.pem -out server.csr \n" +
      "echo subjectAltName = DNS:$FQDN_PARAM$, DNS:*$DOMAIN_PARAM$, IP:127.0.0.1 > extfile.cnf \n" +
      "openssl x509 -req -passin pass:$CERT_CA_PWD_PARAM$ -days 365 -sha256 -in server.csr -CA ca.pem -CAkey ca-key.pem -CAcreateserial -out server.pem -extfile extfile.cnf \n" +
      // Generate Client certificates
      "openssl genrsa -passout pass:$CERT_CA_PWD_PARAM$ -out key.pem \n" +
      "openssl req -passin pass:$CERT_CA_PWD_PARAM$ -subj '/CN=client' -new -key key.pem -out client.csr \n" +
      "echo extendedKeyUsage = clientAuth,serverAuth > extfile.cnf \n" +
      "openssl x509 -req -passin pass:$CERT_CA_PWD_PARAM$ -days 365 -sha256 -in client.csr -CA ca.pem -CAkey ca-key.pem -CAcreateserial -out cert.pem -extfile extfile.cnf \n" +
      "cd ~\n";

  private static final String GET_DOCKERHOST_TLSCACERT_FOR_UBUNTU =
      "cat ~/.tls/ca.pem";
  private static final String GET_DOCKERHOST_TLSCERT_FOR_UBUNTU =
      "cat ~/.tls/cert.pem";
  private static final String GET_DOCKERHOST_TLSCLIENTKEY_FOR_UBUNTU =
      "cat ~/.tls/key.pem";
  private static final String GET_DOCKERHOST_TLSSERVERCERT_FOR_UBUNTU =
      "cat ~/.tls/server.pem";
  private static final String GET_DOCKERHOST_TLSSERVERKEY_FOR_UBUNTU =
      "cat ~/.tls/server-key.pem";

}
