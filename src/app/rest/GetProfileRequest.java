package app.rest;

import app.models.Profile;
import app.utils.JsonHelper;
import app.utils.TextEncryptor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by jesse on 9/21/15.
 */
public class GetProfileRequest {

    public static void getProfile(final String key, final IResponse callback) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                ProfileResponse response = new ProfileResponse();

                try {
                    String baseUrl = "http://localhost:8080/get?data=" //"http://8.22.13.218:8080/get?data="
                            + URLEncoder.encode(TextEncryptor.encrypt(key), "UTF-8");

                    URL url = new URL(baseUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");

                    if (conn.getResponseCode() != 200) {
                        response.success = false;
                        response.message = "Server Error";
                        if (callback != null) {
                            callback.onResponse(response);
                        }
                        return;
                    }

                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            (conn.getInputStream())));

                    StringBuilder sb = new StringBuilder();
                    String output;
                    while ((output = br.readLine()) != null) {
                        sb.append(output);
                    }

                    conn.disconnect();

                    RawResponse raw = JsonHelper.getInstance().json.fromJson(sb.toString(), RawResponse.class);
                    response.success = raw.success;
                    response.message = raw.message;

                    if (raw.data != null && raw.data.length() > 0) {
                        String json = TextEncryptor.decrypt(raw.data);
                        response.profile = JsonHelper.getInstance().json.fromJson(json, Profile.class);
                    }

                    if (callback != null) {
                        callback.onResponse(response);
                    }

                } catch (Exception e) {
                    response.success = false;
                    response.message = "Server Error";
                    if (callback != null) {
                        callback.onResponse(response);
                    }
                    return;
                }
            }
        });

        t.setDaemon(true);
        t.start();
    }

    private class RawResponse extends BasicResponse {
        public String data;
    }
}
