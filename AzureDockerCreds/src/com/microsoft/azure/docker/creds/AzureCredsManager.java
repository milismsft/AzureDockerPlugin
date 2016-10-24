package com.microsoft.azure.docker.creds;

import com.microsoft.aad.adal4j.AuthenticationResult;
import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import com.microsoft.azure.keyvault.authentication.KeyVaultCredentials;
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.management.resources.Subscription;
import com.microsoft.rest.credentials.ServiceClientCredentials;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.microsoft.azure.management.Azure.configure;

public class AzureCredsManager {

  private static Azure.Authenticated spAuthenticator;
  private static Azure.Authenticated adAuthenticator;
  private static AzureCredsManager azureCredsManager;
  private static KeyVaultClient spKeyVaultClient;
  private static KeyVaultClient adKeyVaultClient;

  private AzureCredsManager() {
  }

  private static void createADCreds() {
    adAuthenticator = null; // cleanup local cache
    if (azureCredsManager == null) {
      azureCredsManager = new AzureCredsManager();
    }
    adAuthenticator = configure().authenticate(new AppTokenCreds(TENANT_ID));
    adKeyVaultClient = createADKeyVaultClient();
  }

  private static void createSPCreds(ApplicationTokenCredentials applicationTokenCredentials) {
    spAuthenticator = null; // cleanup local cache
    if (azureCredsManager == null) {
      azureCredsManager = new AzureCredsManager();
    }
    spAuthenticator = configure().authenticate(applicationTokenCredentials);
    spKeyVaultClient = createSPKeyVaultClient(applicationTokenCredentials.getClientId(), applicationTokenCredentials.getSecret());
  }

  public static KeyVaultClient getADKeyVaultClient() {
    if (adAuthenticator == null) {
      createADCreds();
    }

    return adKeyVaultClient;
  }

  public static KeyVaultClient getSPKeyVaultClient() {
    if (spAuthenticator == null) {
      createSPCreds(getDefaultTokenCredentials());
    }

    return spKeyVaultClient;
  }

  public static KeyVaultClient getSPKeyVaultClient(ApplicationTokenCredentials applicationTokenCredentials) {
    if (spAuthenticator == null) {
      createSPCreds(applicationTokenCredentials);
    }

    return spKeyVaultClient;
  }

  private static KeyVaultClient createADKeyVaultClient() {
    ServiceClientCredentials creds = new KeyVaultCredentials() {
      @Override
      public String doAuthenticate(String authorization, String resource, String scope) {
        try {
          AuthenticationResult authResult = getADAccessToken(authorization, resource);
          return authResult.getAccessToken();
        } catch (Exception ex) {
          throw new RuntimeException(ex);
        }
      }
    };

    return new KeyVaultClient(creds);
  }

  private static AuthenticationResult getADAccessToken(String authorization, String resource) throws Exception {
    AuthenticationResult result = AppTokenCreds.acquireADAccessToken(resource);

    if (result == null) {
      throw new RuntimeException("authentication result was null");
    }

    return result;
  }

  private static KeyVaultClient createSPKeyVaultClient(String clientId, String appKey) {
    ServiceClientCredentials creds = new KeyVaultCredentials() {
      @Override
      public String doAuthenticate(String authorization, String resource, String scope) {
        try {
          AuthenticationResult authResult = AppTokenCreds.acquireSPAccessToken(authorization, resource, clientId, appKey);
          return authResult.getAccessToken();
        } catch (Exception ex) {
          throw new RuntimeException(ex);
        }
      }
    };

    return new KeyVaultClient(creds);
  }

  public static List<Subscription> getADSubscriptions() {
    if (adAuthenticator == null) {
      createADCreds();
    }

    return adAuthenticator.subscriptions().list();
  }

  public static Subscription getADSubscription(String sid) {
    for (Subscription subscription : getADSubscriptions()) {
      if (subscription.subscriptionId().equals(sid)) {
        return subscription;
      }
    }

    return null;
  }

  public static List<Subscription> getSPSubscriptions() {
    if (spAuthenticator == null) {
      createSPCreds(getDefaultTokenCredentials());
    }

    return spAuthenticator.subscriptions().list();
  }

  public static List<Subscription> getSPSubscriptions(ApplicationTokenCredentials applicationTokenCredentials) {
    if (spAuthenticator == null) {
      createSPCreds(applicationTokenCredentials);
    }

    return spAuthenticator.subscriptions().list();
  }

  public static List<Azure> getAzureADClients() {
    List<Azure> result = new ArrayList<Azure>();

    for (Subscription subscription : getADSubscriptions()) {
      result.add( adAuthenticator.withSubscription(subscription.subscriptionId()));
    }

    return result;
  }

  public static List<Azure> getAzureSPClients(ApplicationTokenCredentials applicationTokenCredentials) {
    List<Azure> result = new ArrayList<Azure>();

    for (Subscription subscription : getSPSubscriptions(applicationTokenCredentials)) {
      result.add( spAuthenticator.withSubscription(subscription.subscriptionId()));
    }

    return result;
  }

  public static List<Azure> getAzureSPClients() {
    List<Azure> result = new ArrayList<Azure>();

    for (Subscription subscription : getSPSubscriptions()) {
      result.add( spAuthenticator.withSubscription(subscription.subscriptionId()));
    }

    return result;
  }

  public static Azure getAzureADClient(String sid) {
    for (Subscription subscription : getADSubscriptions()) {
      if (subscription.subscriptionId().equals(sid)) {
        return adAuthenticator.withSubscription(sid);
      }
    }

    return null;
  }

  public static Azure createAuthLoginDefaultAzureClient () {
    if (azureCredsManager == null) {
      createADCreds();
    }

    return azureCredsManager.getAzureADClient(SUBSCRIPTION_ID);
  }

  public static Azure getAzureSPClient(String sid) {
    for (Subscription subscription : getSPSubscriptions()) {
      if (subscription.subscriptionId().equals(sid)) {
        return spAuthenticator.withSubscription(sid);
      }
    }

    return null;
  }

  public static Azure getAzureSPClient(ApplicationTokenCredentials applicationTokenCredentials, String sid) {
    for (Subscription subscription : getSPSubscriptions(applicationTokenCredentials)) {
      if (subscription.subscriptionId().equals(sid)) {
        return spAuthenticator.withSubscription(sid);
      }
    }

    return null;
  }

  public static Azure createSPDefaultAzureClient () {
    return
        configure()
            .authenticate(getDefaultTokenCredentials())
            .withSubscription(SUBSCRIPTION_ID);
  }

  public static void drop() {
    azureCredsManager = null;
    adAuthenticator = null; // cleanup local cache
    spAuthenticator = null; // cleanup local cache
    adKeyVaultClient = null;
    spKeyVaultClient = null;
  }

  private static ApplicationTokenCredentials getDefaultTokenCredentials() {
    return new ApplicationTokenCredentials(CLIENT_ID, TENANT_ID, APPKEY, AzureEnvironment.AZURE);
  }

  public static Collection<String> getADTenantSubscriptionsList() {
    if (azureCredsManager == null) {
      createADCreds();
    }

    List<String> result = new ArrayList<String>();
    for (Subscription subscription : azureCredsManager.getADSubscriptions()) {
      result.add(subscription.subscriptionId());
    }

    return result;
  }

  public static Collection<String> getSPTenantSubscriptionsList() {
    if (azureCredsManager == null) {
      createSPCreds(getDefaultTokenCredentials());
    }

    List<String> result = new ArrayList<String>();
    for (Subscription subscription : azureCredsManager.getSPSubscriptions()) {
      result.add(subscription.subscriptionId());
    }

    return result;
  }

  public static Collection<String> getDefaultSubscriptionsList() {
    List<String> result = new ArrayList<String>();
    List<Subscription> subs = Azure
        .configure()
        .authenticate(new ApplicationTokenCredentials(CLIENT_ID, TENANT_ID, APPKEY, AzureEnvironment.AZURE))
        .subscriptions()
        .list();

    for (Subscription subscription : subs) {
      result.add(subscription.subscriptionId());
    }

    return result;
  }

}
