package pro.khodoian.gotit.client;

import android.util.Log;

import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.io.IOUtils;

import java.util.ArrayList;
import java.util.List;

import pro.khodoian.gotit.retrofit.AccessPoint;
import pro.khodoian.gotit.view.LoginActivity;
import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.FormUrlEncodedTypedOutput;

public class GetTokenRequest {

    public static final String TAG = GetTokenRequest.class.getCanonicalName();

    public static final String CLIENT_ID = AccessPoint.CLIENT_ID;
    public static final String CLIENT_SECRET = AccessPoint.CLIENT_SECRET;
    public static final String ENDPOINT = AccessPoint.ENDPOINT;
    public static final String TOKEN_PATH = AccessPoint.TOKEN_PATH;

    public interface Listener {
        void onGetTokenSucess(String token);
        void onGetTokenUnauthorized();
        void onGetTokenFailure();
    }

    public static void getAccessToken(Client client, String username, String password,
                                      final Listener callback) {
        try {
            // This code below programmatically builds an OAuth 2.0 password
            // grant request and sends it to the server.

            // Encode the username and password into the body of the request.
            FormUrlEncodedTypedOutput to = new FormUrlEncodedTypedOutput();
            to.addField("username", username);
            to.addField("password", password);

            // Add the client ID and client secret to the body of the request.
            to.addField("client_id", CLIENT_ID);
            to.addField("client_secret", CLIENT_SECRET);

            // Indicate that we're using the OAuth Password Grant Flow
            // by adding grant_type=password to the body
            to.addField("grant_type", "password");

            // The password grant requires BASIC authentication of the client.
            // In order to do BASIC authentication, we need to concatenate the
            // client_id and client_secret values together with a colon and then
            // Base64 encode them. The final value is added to the request as
            // the "Authorization" header and the value is set to "Basic "
            // concatenated with the Base64 client_id:client_secret value described
            // above.
            String base64Auth = BaseEncoding.base64()
                    .encode(new String(CLIENT_ID + ":" + CLIENT_SECRET).getBytes());
            // Add the basic authorization header
            List<Header> headers = new ArrayList<Header>();
            headers.add(new Header("Authorization", "Basic " + base64Auth));

            // Create the actual password grant request using the data above
            Request req = new Request("POST", ENDPOINT + TOKEN_PATH, headers, to);

            // Request the password grant.
            Response resp = client.execute(req);

            if (resp == null) {
                Log.e(TAG, "resp is null");
                callback.onGetTokenFailure();
                return;
            }
            int status = resp.getStatus();

            // Make sure the server responded with 200 OK
            if (status >= 200 && status < 300) {
                Log.e(TAG, "getToken response code is okay");
                // Extract the string body from the response
                final String body = IOUtils.toString(resp.getBody().in());

                // Extract the access_token (bearer token) from the response so that we
                // can add it to future requests.
                if (callback instanceof LoginActivity)
                    ((LoginActivity) callback).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onGetTokenSucess(new Gson().fromJson(body, JsonObject.class)
                                    .get("access_token").getAsString());
                        }
                    });
            } else if (status == HttpStatus.SC_UNAUTHORIZED
                    || status == HttpStatus.SC_BAD_REQUEST) {
                Log.e(TAG, "getToken response code is 401");
                // Incorrect credentials
                if (callback instanceof LoginActivity)
                    ((LoginActivity) callback).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onGetTokenUnauthorized();
                        }
                    });
            } else {
                // Other error
                Log.e(TAG, "getToken response code - other");
                if (callback instanceof LoginActivity)
                    ((LoginActivity) callback).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((LoginActivity) callback).onGetTokenFailure();
                        }
                    });
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception caught");
            Log.e(TAG, e.toString());
            if (callback instanceof LoginActivity)
                ((LoginActivity) callback).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onGetTokenFailure();
                    }
                });
        }
    }
}