import com.microsoft.azure.docker.creds.AzureCredsManager
import com.microsoft.azure.docker.ops.AzureDockerCertVaultOps
import com.microsoft.azure.docker.resources.AzureDockerCertVault
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
      LogManager.getLogManager().reset();

      System.out.println("Tenant Subscription list:")
      Collection<String> tenantSubscriptions = AzureCredsManager.getADTenantSubscriptionsList()
      tenantSubscriptions.each {subscription -> System.out.println(subscription)}



////      KeyVaultClient keyVaultClient = AzureCredsManager.getADKeyVaultClient();
////      Azure azureClient = AzureCredsManager.createAuthLoginDefaultAzureClient()
//      KeyVaultClient keyVaultClient = AzureCredsManager.getSPKeyVaultClient();
//      Azure azureClient = AzureCredsManager.createSPDefaultAzureClient();
//      azureClient.vaults()
//
      AzureDockerCertVault certVault; // = new AzureDockerCertVault()

      String path = "/Volumes/SharedDisk/workspace/docker/certs/";
      String path2 = "/Volumes/SharedDisk/workspace/docker/certs2/";
      certVault = AzureDockerCertVaultOps.createFromLocalFiles(path);

//      // certVault.name = AzureDockerCertVaultOps.generateUniqueKeyVaultName("dockervault2")
//      certVault.name = "dockervault2629442"
//      certVault.hostName = "dockerhost2"
//      certVault.region = "centralus"
//      certVault.resourceGroupName = "temp-centralus"
//      certVault.userName = "adrianmi@microsoft.com"
//      certVault.servicePrincipalId = AzureCredsManager.CLIENT_ID
////      certVault.vmUsername = "ubuntu"
////      certVault.vmPwd = "azurecon@1"
////      certVault.sshKey = "sshkey"
////      certVault.sshPubKey = "sshpubkey"
////      certVault.tlsCACert = "tlscacert"
////      certVault.tlsCert = "tlscert"
////      certVault.tlsClientKey = "tlsclientkey"
////      certVault.tlsHostCert = "tlshostcert"
////      certVault.tlsServerKey = "tlsserverkey"
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
      ObjectMapper mapper = new ObjectMapper()
          .configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
          .configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
      System.out.println(mapper.writeValueAsString(certVault));

//      getSSHKeys()
      TestNotNull(null)

      println("Passed")
    } catch (Exception e) {
      println("Failed: " + e.getMessage())
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