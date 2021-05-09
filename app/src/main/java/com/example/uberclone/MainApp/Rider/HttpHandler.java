package com.example.uberclone.MainApp.Rider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpHandler {

    public HttpHandler(){}

    public String getHttpResponse(String requestURL){

        URL url;
        String response = "";

        try {
            url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK){
                String line_to_read;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line_to_read = bufferedReader.readLine()) != null){
                    response += line_to_read;
                }
            }
            else{
                response = "";
            }
        }
        catch (MalformedURLException mfe){
            mfe.printStackTrace();
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
        return response;
    }
}
