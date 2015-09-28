package app.rest;

import app.models.ProfileInfo;
import app.utils.JsonHelper;
import app.utils.TextEncryptor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by jesse on 9/17/15.
 */
public class CreateProfileRequest {

    public static void sendRequest(final ProfileInfo info, final IResponse callback) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                BasicResponse response = new BasicResponse();

                try {
                    String baseUrl = "http://8.22.13.218:8080/create?data="
                            + URLEncoder.encode(TextEncryptor
                            .encrypt(JsonHelper.getInstance()
                                    .json.toJson(info)), "UTF-8");

                    URL url = new URL(baseUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
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

                    response = JsonHelper.getInstance().json.fromJson(sb.toString(), BasicResponse.class);
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
}
