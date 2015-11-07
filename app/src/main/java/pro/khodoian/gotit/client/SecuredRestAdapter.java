package pro.khodoian.gotit.client;

/*
 **
 ** Copyright 2014, Jules White
 **
 **
 */

import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.io.IOUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import retrofit.Endpoint;
import retrofit.ErrorHandler;
import retrofit.Profiler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RestAdapter.Log;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.Client;
import retrofit.client.Client.Provider;
import retrofit.client.Header;
import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.converter.Converter;
import retrofit.mime.FormUrlEncodedTypedOutput;

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

    private class OAuthHandler implements RequestInterceptor {

        private boolean loggedIn;
        private Client client;
        private String tokenIssuingEndpoint;
        private String username;
        private String password;
        private String clientId;
        private String clientSecret;
        private String accessToken;

        public OAuthHandler(Client client, String accessToken) {
            super();
            this.client = client;
            this.accessToken = accessToken;
        }

        /**
         * Every time a method on the client interface is invoked, this method is
         * going to get called. The method checks if the client has previously obtained
         * an OAuth 2.0 bearer token. If not, the method obtains the bearer token by
         * sending a password grant request to the server.
         *
         * Once this method has obtained a bearer token, all future invocations will
         * automatically insert the bearer token as the "Authorization" header in
         * outgoing HTTP requests.
         *
         */
        @Override
        public void intercept(RequestFacade request) {
            // Add the access_token that we previously obtained to this request as
            // the "Authorization" header.
            request.addHeader("Authorization", "Bearer " + accessToken);
        }

    }

    private String loginUrl;
    private Client client;
    private String token;

    public SecuredRestAdapter setLoginEndpoint(String endpoint){
        loginUrl = endpoint;
        return this;
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
        this.token = token;
        return this;
    }

    @Override
    public RestAdapter build() {
        if (this.token == null || this.token.equals(""))
            throw new SecuredRestAdapterException(
                    "Token must be provided, when calling SecuredRestAdapter");

        if (client == null) {
            client = new OkClient();
        }
        OAuthHandler hdlr = new OAuthHandler(client, token);
        setRequestInterceptor(hdlr);

        return super.build();
    }
}