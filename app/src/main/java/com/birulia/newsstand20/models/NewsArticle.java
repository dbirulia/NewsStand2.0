package com.birulia.newsstand20.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsArticle  {

    public static final String DOMAIN_NAME = "https://nytimes.com/";

    private String newsId;
    private String webUrl;
    private String headline;
    private String snippet;

    public String getNewsId() {
        return newsId;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getSnippet() {
        return snippet;
    }

    private String thumbnailURL;

    public String getMultimediaUrl() {
        return DOMAIN_NAME + thumbnailURL;
    }

    public NewsArticle(JSONObject jsonArticle){
        try {
            this.newsId = jsonArticle.getString("_id");
            this.webUrl = jsonArticle.getString("web_url");
            this.headline = jsonArticle.getJSONObject("headline").getString("main");
            this.snippet = jsonArticle.getString("snippet");

            JSONArray multimediaArray = jsonArticle.getJSONArray("multimedia");
            if (multimediaArray.length() > 0){
                this.thumbnailURL = multimediaArray.getJSONObject(0).getString("url");
            }
            else{
                this.thumbnailURL = "";
            }

        }
        catch (JSONException ex){
            ex.printStackTrace();
        }
    }

    public static ArrayList<NewsArticle> fromJSONArray  (JSONArray jsonArticles) {
        ArrayList<NewsArticle> newsArticles = new ArrayList<>();

        for (int i = 0; i < jsonArticles.length(); i++) {
            try{
                newsArticles.add(new NewsArticle(jsonArticles.getJSONObject(i)));
            }
            catch (JSONException ex){
                ex.printStackTrace();
            }
        }
        return newsArticles;
    }
}
