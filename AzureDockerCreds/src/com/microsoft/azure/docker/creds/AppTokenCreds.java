package com.microsoft.azure.docker.creds;

import com.microsoft.aad.adal4j.AuthenticationResult;
import com.microsoft.aad.adal4j.ClientCredential;
import com.microsoft.auth.AuthContext;
import com.microsoft.auth.PromptBehavior;
import com.microsoft.auth.TokenCache;
import com.microsoft.auth.TokenFileStorage;
import com.microsoft.auth.UserInfo;
import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.AzureTokenCredentials;
import com.microsoft.azure.credentials.AzureTokenCredentialsInterceptor;
import com.microsoft.rest.credentials.TokenCredentials;
import okhttp3.OkHttpClient;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AppTokenCreds extends TokenCredentials implements AzureTokenCredentials {
  static String authority = "https://login.windows.net";
  static String tenant = "common";
  static String resource = "https://management.core.windows.net/";
  static String clientId = "61d65f5a-6e3b-468b-af73-a033f5098c5c";
  static String redirectUri = "https://msopentech.com/";

  ObjectMapper mapper;

  private Map<String, AuthenticationResult> tokens;
  private AzureEnvironment environment;
  private String defaultSubscription;
  private String domain;

  public AppTokenCreds(String tenantId) {
    super((String) null, (String) null);
    this.tokens = new HashMap<String, AuthenticationResult>();
    this.environment = AzureEnvironment.AZURE;
    this.mapper = new ObjectMapper().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    this.domain = tenantId;

    acquireAccessToken(resource);
  }

  public String defaultSubscriptionId() {
    return this.defaultSubscription;
  }

  public AppTokenCreds withDefaultSubscriptionId(String subscriptionId) {
    this.defaultSubscription = subscriptionId;
    return this;
  }

  public String getDomain() {
    return this.domain;
  }

  public AzureEnvironment getEnvironment() {
    return this.environment;
  }

  public String getToken(String resource) throws IOException {
    com.microsoft.aad.adal4j.AuthenticationResult authenticationResult = (com.microsoft.aad.adal4j.AuthenticationResult) this.tokens.get(resource);
    if (authenticationResult == null || authenticationResult.getExpiresOnDate().before(new Date())) {
      authenticationResult = this.acquireAccessToken(resource);
    }

    if (authenticationResult != null) {
      return authenticationResult.getAccessToken();
    } else {
      throw new IOException("Can't get access token");
    }
  }

  private com.microsoft.aad.adal4j.AuthenticationResult acquireAccessToken(String resource) {
    try {
      com.microsoft.aad.adal4j.AuthenticationResult result = null;

      final TokenFileStorage tokenFileStorage = new TokenFileStorage(null);
      final TokenCache cache = new TokenCache();

      byte[] data = tokenFileStorage.read();
      cache.deserialize(data);

      AuthContext ac = new AuthContext(String.format("%s/%s", authority, tenant), cache);
      com.microsoft.auth.AuthenticationResult authResult = ac.acquireToken(resource, clientId, redirectUri, PromptBehavior.Auto, null);
      AuthResult auth = mapper.readValue(mapper.writeValueAsString(authResult), AuthResult.class);
      String userData = "{" +
          "\"uniqueId\":\"" + auth.userInfo.getUniqueId() + "\"," +
          "\"displayableId\":\"" + auth.userInfo.getDisplayableId() + "\"," +
          "\"givenName\":\"" + auth.userInfo.getGivenName() + "\"," +
          "\"familyName\":\"" + auth.userInfo.getFamilyName() + "\"," +
          "\"identityProvider\":\"" + auth.userInfo.getIdentityProvider() + "\"," +
          "\"passwordChangeUrl\":\"" + auth.userInfo.getPasswordChangeUrl() + "\"" +
          "}";
      com.microsoft.aad.adal4j.UserInfo userInfo = mapper.readValue(userData, com.microsoft.aad.adal4j.UserInfo.class);
      result = new com.microsoft.aad.adal4j.AuthenticationResult(auth.accessTokenType, auth.accessToken, auth.refreshToken, auth.expiresOn, auth.idToken, userInfo, false);

      if (cache.getHasStateChanged()) {
        tokenFileStorage.write(cache.serialize());
        cache.setHasStateChanged(false);
      }

      return result;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public void applyCredentialsFilter(OkHttpClient.Builder clientBuilder) {
    clientBuilder.interceptors().add(new AzureTokenCredentialsInterceptor(this));
  }

  static class AuthResult {
    public String resource;
    public String accessTokenType;
    public String accessToken;
    public String refreshToken;
    public long expiresOn;
    public String tenantId;
    public UserInfo userInfo;
    public String idToken;
    public boolean isMultipleResourceRefreshToken;
    public String userAssertionHash;

    public AuthResult() {
    }
  }

  public static com.microsoft.aad.adal4j.AuthenticationResult acquireADAccessToken(String resource) {
    try {
      com.microsoft.aad.adal4j.AuthenticationResult result = null;

      final TokenFileStorage tokenFileStorage = new TokenFileStorage(null);
      final TokenCache cache = new TokenCache();

      byte[] data = tokenFileStorage.read();
      cache.deserialize(data);

      AuthContext ac = new AuthContext("https://login.windows.net/common", cache);
      com.microsoft.auth.AuthenticationResult authResult = ac.acquireToken(resource, clientId, redirectUri, PromptBehavior.Auto, null);
      ObjectMapper mapper = new ObjectMapper().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      AuthResult auth = mapper.readValue(mapper.writeValueAsString(authResult), AuthResult.class);
      String userData = "{" +
          "\"uniqueId\":\"" + auth.userInfo.getUniqueId() + "\"," +
          "\"displayableId\":\"" + auth.userInfo.getDisplayableId() + "\"," +
          "\"givenName\":\"" + auth.userInfo.getGivenName() + "\"," +
          "\"familyName\":\"" + auth.userInfo.getFamilyName() + "\"," +
          "\"identityProvider\":\"" + auth.userInfo.getIdentityProvider() + "\"," +
          "\"passwordChangeUrl\":\"" + auth.userInfo.getPasswordChangeUrl() + "\"" +
          "}";
      com.microsoft.aad.adal4j.UserInfo userInfo = mapper.readValue(userData, com.microsoft.aad.adal4j.UserInfo.class);
      result = new com.microsoft.aad.adal4j.AuthenticationResult(auth.accessTokenType, auth.accessToken, auth.refreshToken, auth.expiresOn, auth.idToken, userInfo, false);

      if (cache.getHasStateChanged()) {
        tokenFileStorage.write(cache.serialize());
        cache.setHasStateChanged(false);
      }

      return result;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public static com.microsoft.aad.adal4j.AuthenticationResult acquireSPAccessToken(String authorization, String resource, String clientId, String appKey) throws Exception {
    com.microsoft.aad.adal4j.AuthenticationResult result = null;
    ExecutorService service = null;
    try {
      service = Executors.newFixedThreadPool(1);
      com.microsoft.aad.adal4j.AuthenticationContext context = new com.microsoft.aad.adal4j.AuthenticationContext(authorization, false, service);
      ClientCredential credentials = new ClientCredential(clientId, appKey);
      Future<com.microsoft.aad.adal4j.AuthenticationResult> future = context.acquireToken(resource, credentials, null);
      result = future.get();
    } finally {
      if (service != null) {
        service.shutdown();
      }
    }

    if (result == null) {
      throw new RuntimeException("authentication result was null");
    }
    return result;
  }

}