package com.palteam.shabbik.webservice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.palteam.shabbik.utils.IConstants;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Scanner;

public class WebService implements IConstants {

    private static final int CONNECTION_TIMEOUT = 0;
    private static final int DATARETRIEVAL_TIMEOUT = 0;
    private static final char PARAMETER_DELIMITER = '&';
    private static final char PARAMETER_EQUALS_CHAR = '=';

    public static boolean isUrlLAvilable(String url) {
        try {
            InetAddress.getByName(url).isReachable(3);
            return true;
        } catch (UnknownHostException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * To check if there is an internet connection
     * @param context
     * @return
     */
    public static boolean isConnectedToWeb(Context context) {

        ConnectivityManager con = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetInfo = con
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean wifi = wifiNetInfo != null && wifiNetInfo.isAvailable()
                && wifiNetInfo.isConnectedOrConnecting();

        NetworkInfo mobileNetInfo = con
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean internet = mobileNetInfo != null && mobileNetInfo.isAvailable()
                && mobileNetInfo.isConnectedOrConnecting();

        return wifi || internet;
    }

    @SuppressWarnings("deprecation")
    public static String createQueryStringForParameters(
            Map<String, String> parameters) {

        StringBuilder parametersAsQueryString = new StringBuilder();
        if (parameters != null) {
            boolean firstParameter = true;

            for (String parameterName : parameters.keySet()) {

                if (!firstParameter) {
                    parametersAsQueryString.append(PARAMETER_DELIMITER);
                }

                parametersAsQueryString
                        .append(parameterName)
                        .append(PARAMETER_EQUALS_CHAR)
                        .append(URLEncoder.encode(parameters.get(parameterName)));

                firstParameter = false;
            }
        }
        return parametersAsQueryString.toString();

    }

    public static String requestWebService(String serviceUrl,
                                           String postParameters) /*
     * throws MalformedURLException
     */ {

        Log.i("url", serviceUrl);
        if (postParameters != null)
            Log.i("param", postParameters);

        disableConnectionReuseIfNecessary();

        HttpURLConnection urlConnection = null;
        try {
            // create connection
            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);

            // handle POST parameters

            if (postParameters != null) {

                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setFixedLengthStreamingMode(postParameters
                        .getBytes().length);
                urlConnection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");

                // send the POST out
                PrintWriter out = new PrintWriter(
                        urlConnection.getOutputStream());
                out.print(postParameters);
                out.close();
            }

            // handle issues
            int statusCode = urlConnection.getResponseCode();

            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // handle unauthorized (if service requires user login)
                // through some exception
                Log.e("error Web service", "http not authorized");


            } else if (statusCode != HttpURLConnection.HTTP_OK) {
                // handle any other errors, like 404, 500,..
                Log.e("error Web service", "statusCode  Not Ok");
                // through some exception

            }

            InputStream in = new BufferedInputStream(
                    urlConnection.getInputStream());
            return getResponseText(in);

        } catch (MalformedURLException e) {
            // URL is invalid
            Log.e("WebService", "URL is invalid");

        } catch (SocketTimeoutException e) {
            // data retrieval or connection timed out
            Log.e("WebService", "data retrieval or connection timed out");

        } catch (IOException e) {
            // could not read response body
            // (could not create input stream)
            Log.e("WebService", "could not create input stream");
            Log.e("WebService", e.getMessage());
            Log.e("WebService", e.toString());

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }

    /**
     * required in order to prevent issues in earlier Android version.
     */
    private static void disableConnectionReuseIfNecessary() {
        // see HttpURLConnection API doc
        if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    private static String getResponseText(InputStream inStream) {
        // very nice trick from
        // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
        return new Scanner(inStream).useDelimiter("\\A").next();
    }

}
