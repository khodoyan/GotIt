package pro.khodoian.gotit.client;

/*
 **
 ** Copyright 2014, Jules White
 **
 **
 */

import java.util.concurrent.Executor;

import retrofit.Endpoint;
import retrofit.ErrorHandler;
import retrofit.Profiler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RestAdapter.Log;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;
import retrofit.client.Client;
import retrofit.client.Client.Provider;
import retrofit.converter.Converter;

/**
 * A Builder class for a Retrofit REST Adapter. Extends the default implementation by providing logic to
 * handle an OAuth 2.0 password grant login flow. The RestAdapter that it produces uses an interceptor
 * to automatically obtain a bearer token from the authorization server and insert it into all client
 * requests.
 *
 * You can use it like this:
 *
 private VideoSvcApi videoService = new SecuredRestBuilder()
 .setLoginEndpoint(TEST_URL + VideoSvcApi.TOKEN_PATH)
 .setUsername(USERNAME)
 .setPassword(PASSWORD)
 .setClientId(CLIENT_ID)
 .setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
 .setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
 .create(VideoSvcApi.class);
 *
 * @author Jules, Mitchell
 *
 */
public class SecuredRestAdapter extends RestAdapter.Builder {

    // TODO: implement token refresh

    public static final String TAG = SecuredRestAdapter.class.getCanonicalName();

    private class OAuthHandler implements RequestInterceptor {

        private Client client;
        private String accessToken;

        public OAuthHandler(Client client, String accessToken) {
            super();
            this.client = client;
            this.accessToken = accessToken;
        }

        /**
         * Every time RestAdapter is called, access token is added to header of the request
         */
        @Override
        public void intercept(RequestFacade request) {
            // Add the access_token that we previously obtained to this request as
            // the "Authorization" header.
            request.addHeader("Authorization", "Bearer " + accessToken);
        }

    }

    private Client client;
    private String token;

    public SecuredRestAdapter setLoginEndpoint(String endpoint){
        return (SecuredRestAdapter) super.setEndpoint(endpoint);
    }

    @Override
    public SecuredRestAdapter setEndpoint(String endpoint) {
        return (SecuredRestAdapter) super.setEndpoint(endpoint);
    }

    @Override
    public SecuredRestAdapter setEndpoint(Endpoint endpoint) {
        return (SecuredRestAdapter) super.setEndpoint(endpoint);
    }

    @Override
    public SecuredRestAdapter setClient(Client client) {
        this.client = client;
        return (SecuredRestAdapter) super.setClient(client);
    }

    @Override
    public SecuredRestAdapter setClient(Provider clientProvider) {
        client = clientProvider.get();
        return (SecuredRestAdapter) super.setClient(clientProvider);
    }

    @Override
    public SecuredRestAdapter setErrorHandler(ErrorHandler errorHandler) {

        return (SecuredRestAdapter) super.setErrorHandler(errorHandler);
    }

    @Override
    public SecuredRestAdapter setExecutors(Executor httpExecutor,
                                           Executor callbackExecutor) {

        return (SecuredRestAdapter) super.setExecutors(httpExecutor,
                callbackExecutor);
    }

    @Override
    public SecuredRestAdapter setRequestInterceptor(
            RequestInterceptor requestInterceptor) {

        return (SecuredRestAdapter) super
                .setRequestInterceptor(requestInterceptor);
    }

    @Override
    public SecuredRestAdapter setConverter(Converter converter) {

        return (SecuredRestAdapter) super.setConverter(converter);
    }

    @Override
    public SecuredRestAdapter setProfiler(@SuppressWarnings("rawtypes") Profiler profiler) {

        return (SecuredRestAdapter) super.setProfiler(profiler);
    }

    @Override
    public SecuredRestAdapter setLog(Log log) {

        return (SecuredRestAdapter) super.setLog(log);
    }

    @Override
    public SecuredRestAdapter setLogLevel(LogLevel logLevel) {

        return (SecuredRestAdapter) super.setLogLevel(logLevel);
    }

    public SecuredRestAdapter setToken(String token) {
        if (token == null)
            throw new SecuredRestAdapterException(
                    "Token must be provided, when calling SecuredRestAdapter");
        this.token = token;
        return this;
    }

    @Override
    public RestAdapter build() {
        if (this.token == null || this.token.equals(""))
            throw new SecuredRestAdapterException(
                    "Token must be provided, when calling SecuredRestAdapter");

        if (client == null) {
            client = new ApacheClient();
        }
        OAuthHandler handler = new OAuthHandler(client, token);
        setRequestInterceptor(handler);

        android.util.Log.v(TAG, "Token is: " + token);

        return super.build();
    }
}