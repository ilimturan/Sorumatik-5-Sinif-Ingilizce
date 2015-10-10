package zargidigames.com.sorumatik.ingilizce5.zapi;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.*;

import zargidigames.com.sorumatik.ingilizce5.config.AppConfig;

/**
 * Created by ilimturan on 06/10/15.
 */
public class RestClient {

    private static Integer LESSON_ID = AppConfig.appId;
    private static String API_URL =  AppConfig.apiUrl;
    private static String API_KEY = AppConfig.apiKey;

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void getUnits(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

        client.addHeader("api_key", API_KEY);
        client.get(getAbsoluteUrlForUnits(url), params, responseHandler);
    }

    public static void getWords(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

        client.addHeader("api_key", API_KEY);
        client.get(getAbsoluteUrlForWords(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

        client.addHeader("api_key", API_KEY);
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {

        return API_URL + relativeUrl;
    }


    private static String getAbsoluteUrlForUnits(String relativeUrl) {

        return API_URL + relativeUrl + LESSON_ID;
    }


    private static String getAbsoluteUrlForWords(String relativeUrl) {

        return API_URL + relativeUrl;
    }

}
