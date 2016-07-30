package com.birulia.newsstand20.net;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class NewsClient {
    private static final String API_BASE_URL = "https://api.nytimes.com/svc/search/v2/";
    private static final String API_KEY = "6ca8607210cb441b8fbe383b7ae79c77";
    private AsyncHttpClient client;

    public NewsClient() {
        this.client = new AsyncHttpClient();
        this.client.setTimeout(20 * 1000);
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }

    // Method for accessing the search API
    public void getNews(String query, Integer page, TextHttpResponseHandler handler) {
        try {
            String url = getApiUrl("articlesearch.json");
            RequestParams params = new RequestParams();
            params.put("page", page);
            params.put("api-key", API_KEY);
            if (query.length() > 0) {
                params.put("q", URLEncoder.encode(query, "utf-8"));
            }
            client.get(url, params, handler);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
