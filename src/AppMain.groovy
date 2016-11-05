import com.microsoft.azure.docker.creds.AzureCredsManager
import com.microsoft.azure.docker.ops.AzureDockerCertVaultOps
import com.microsoft.azure.docker.ops.AzureDockerVMOps
import com.microsoft.azure.docker.resources.AzureDockerCertVault
import com.microsoft.azure.docker.resources.AzureDockerVM
import com.microsoft.azure.docker.resources.DockerHost
import com.microsoft.azure.docker.resources.KnownDockerVirtualMachineImage
import com.microsoft.azure.docker.ui.utils.PluginUtil
import com.microsoft.azure.management.Azure
import com.microsoft.azure.management.compute.ImageReference
import com.microsoft.azure.management.compute.VirtualMachine
import com.microsoft.azure.management.network.IPVersion
import com.microsoft.azure.management.network.NicIpConfiguration
import com.microsoft.azure.management.resources.ResourceGroup
import com.sun.istack.internal.NotNull
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper
import org.codehaus.jackson.map.SerializationConfig

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target
import java.util.logging.LogManager

import com.jcraft.jsch.JSch
import com.jcraft.jsch.KeyPair

class AppMain {
  public static void main(String[] args) {
    try {
      def loggerNames = LogManager.logManager.loggerNames
      loggerNames.each {
        println("Found logger: " + it)
      }

      PluginUtil.resetLoggers()

      ObjectMapper mapper = new ObjectMapper()
          .configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
          .configure(SerializationConfig.Feature.INDENT_OUTPUT, true);

      System.out.println("Tenant Subscription list:")
      Collection<String> tenantSubscriptions = AzureCredsManager.getADTenantSubscriptionsList()
      tenantSubscriptions.each {subscription -> System.out.println(subscription)}



////      KeyVaultClient keyVaultClient = AzureCredsManager.getADKeyVaultClient();
////      Azure azureClient = AzureCredsManager.createAuthLoginDefaultAzureClient()
//      KeyVaultClient keyVaultClient = AzureCredsManager.getSPKeyVaultClient();
      Azure azureClient = AzureCredsManager.createSPDefaultAzureClient();

      AzureDockerPluginStart dialog = new AzureDockerPluginStart();
      dialog.pack();
      dialog.setVisible(true);

      AzureDockerCertVault certVault; // = new AzureDockerCertVault()

      String path = "/Volumes/SharedDisk/workspace/docker/certs/";
      String path2 = "/Volumes/SharedDisk/workspace/docker/certs2/";
      certVault = AzureDockerCertVaultOps.getTLSCertsFromLocalFile(path);
      certVault = AzureDockerCertVaultOps.getSSHKeysFromLocalFile(path);

//      // certVault.name = AzureDockerCertVaultOps.generateUniqueKeyVaultName("dockervault2")
//      ImageReference imgRef = KnownDockerVirtualMachineImage.OPENLOGIC_CENTOS_7_2.imageReference()
//      certVault.name = "dockercentos11"
//      ImageReference imgRef = KnownDockerVirtualMachineImage.COREOS_STABLE_LATEST.imageReference()
//      certVault.name = "dockercoreos12"
//      ImageReference imgRef = KnownDockerVirtualMachineImage.UBUNTU_SNAPPY_CORE_15_04.imageReference()
//      certVault.name = "dockerubuntu15"
      ImageReference imgRef = KnownDockerVirtualMachineImage.UBUNTU_SERVER_14_04_LTS.imageReference()
      certVault.name = "dockerubuntu14"
//      ImageReference imgRef = KnownDockerVirtualMachineImage.UBUNTU_SERVER_16_04_LTS.imageReference()
//      certVault.name = "dockerubuntu16"
      certVault.hostName = certVault.name
      certVault.region = "centralus"
//      certVault.availabilitySet = certVault.name
      certVault.resourceGroupName = certVault.hostName + "-centralus"
      certVault.userName = "adrianmi@microsoft.com"
      certVault.servicePrincipalId = AzureCredsManager.CLIENT_ID
      certVault.vmUsername = "ubuntu"
      certVault.vmPwd = "azureconsole@1"
//      certVault.sshKey = "sshkey"
//      certVault.sshPubKey = "sshpubkey"
//      certVault.tlsCACert = "tlscacert"
//      certVault.tlsCAKey = "tlscakey"
//      certVault.tlsClientCert = "tlsclientcert"
//      certVault.tlsClientKey = "tlsclientkey"
//      certVault.tlsServerCert = "tlsservercert"
//      certVault.tlsServerKey = "tlsserverkey"
//
//      AzureDockerCertVaultOps.createOrUpdateVault(azureClient, certVault, keyVaultClient)
//
//      System.out.println("KeyVault secrets list:")
//      List<SecretItem> secrets = keyVaultClient.listSecrets("https://dockervault2629442.vault.azure.net/");
//
//      for (SecretItem secret : secrets ) {
//        System.out.println(secret.id())
//      }
//
//      AzureDockerCertVault outputVault = AzureDockerCertVaultOps.getVault(azureClient, certVault.name, certVault.resourceGroupName, keyVaultClient);
//      AzureDockerCertVaultOps.saveToLocalFiles(path2, outputVault);

      System.out.println("Dump current vault:");
      System.out.println(mapper.writeValueAsString(certVault));

//      getSSHKeys()

      System.out.println("Resource Groups:")
      for (ResourceGroup rg : azureClient.resourceGroups().list()) {
        System.out.println("\t" + rg.name())
      }

//      System.out.println("Create VM")
//      System.out.println("\tStarting at: " + new Date(System.currentTimeMillis()).toString())
//      AzureDockerVMOps.createDefaultDockerHostVM(azureClient, certVault, imgRef)
//      System.out.println("\tFinished at: " + new Date(System.currentTimeMillis()).toString())

//      AzureDockerVM vm = AzureDockerVMOps.getDockerVM(azureClient, certVault.resourceGroupName, certVault.hostName)
//      DockerHost dockerHost = AzureDockerVMOps.getDockerHost(certVault, vm);
//      dockerHost.port = "2376";
//      System.out.println("Dump new Virtual Machine:");
//      System.out.println(mapper.writeValueAsString(dockerHost));
//
//      AzureDockerVMOps.installDocker(dockerHost);


//      System.out.println("Delete VM")
//      System.out.println("\tStarting at: " + new Date(System.currentTimeMillis()).toString())
//      azureClient.resourceGroups().delete(certVault.resourceGroupName)
//      azureClient.resourceGroups().delete("docker3-centralus")
//      System.out.println("\tFinished at: " + new Date(System.currentTimeMillis()).toString())

      println("Passed")
    } catch (Exception e) {
      PluginUtil.displayErrorDialogAndLog("AzureDockerPlugin", e.getMessage(), e)
      println("Failed: " + e.getMessage())
      e.printStackTrace()
    } finally {
      System.exit(0);
    }
  }

  static void getTLSKeys() {
  }

  static void TestNotNull(@NotNull String str) {
    System.out.println(str);
  }

  static void getSSHKeys() {
    JSch jsch = new JSch()

    KeyPair keyPair = KeyPair.genKeyPair(jsch, KeyPair.RSA)

    ByteArrayOutputStream privateKeyBuff = new ByteArrayOutputStream(2048)
    ByteArrayOutputStream publicKeyBuff = new ByteArrayOutputStream(2048)

    keyPair.writePrivateKey(privateKeyBuff)
    keyPair.writePublicKey(publicKeyBuff, "comment")

    System.out.println("Print SSH RSA keys:")
    System.out.println("\tPublic:\t" + publicKeyBuff.toString())
    System.out.println("\tPrivate:\t" + privateKeyBuff.toString())

  }
}



/*
// For generating SSL certs

import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Date;

import sun.security.x509.CertAndKeyGen;
import sun.security.x509.X500Name;

public class UseKeyTool {

    private static final int keysize = 1024;
    private static final String commonName = "www.test.de";
    private static final String organizationalUnit = "IT";
    private static final String organization = "test";
    private static final String city = "test";
    private static final String state = "test";
    private static final String country = "DE";
    private static final long validity = 1096; // 3 years
    private static final String alias = "tomcat";
    private static final char[] keyPass = "changeit".toCharArray();

    // copied most ideas from sun.security.tools.KeyTool.java

    @SuppressWarnings("restriction")
    public static void main(String[] args) throws Exception {

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);

        CertAndKeyGen keypair = new CertAndKeyGen("RSA", "SHA1WithRSA", null);

        X500Name x500Name = new X500Name(commonName, organizationalUnit, organization, city, state, country);

        keypair.generate(keysize);
        PrivateKey privKey = keypair.getPrivateKey();

        X509Certificate[] chain = new X509Certificate[1];

        chain[0] = keypair.getSelfCertificate(x500Name, new Date(), (long) validity * 24 * 60 * 60);

        keyStore.setKeyEntry(alias, privKey, keyPass, chain);

        keyStore.store(new FileOutputStream(".keystore"), keyPass);



    }
}
*/