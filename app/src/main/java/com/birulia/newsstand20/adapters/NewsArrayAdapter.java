package com.birulia.newsstand20.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.birulia.newsstand20.R;
import com.birulia.newsstand20.WebActivity;
import com.birulia.newsstand20.models.NewsArticle;

import java.util.List;

public class NewsArrayAdapter extends RecyclerView.Adapter<NewsArrayAdapter.ViewHolder> {

    // Store a member variable for the contacts
    private List<NewsArticle> newsArticles;
    // Store the context for easy access
    private Context newsContext;

    // Pass in the contact array into the constructor
    public NewsArrayAdapter(Context context, List<NewsArticle> _newsArticles) {
        newsArticles = _newsArticles;
        newsContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return newsContext;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView ivThumbnail;
        public TextView tvTitle;
        public String webURL;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            ivThumbnail = (ImageView) itemView.findViewById(R.id.ivThumbnail);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            webURL = "";
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // We can access the data within the views
            if (webURL != ""){
                Intent i = new Intent(view.getContext(), WebActivity.class);
                i.putExtra("weburl", webURL);
                view.getContext().startActivity(i);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View newsView = inflater.inflate(R.layout.news_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(newsView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Get the data model based on position
        NewsArticle newsArticle = newsArticles.get(position);
        // Set item views based on your views and data model
        TextView tvTitle = viewHolder.tvTitle;

        String headLine = newsArticle.getHeadline();
        if (headLine.length() > 40){
            trimHeadline(headLine, 40);
        }
        tvTitle.setText(newsArticle.getHeadline());
        ImageView ivTHumbnail = viewHolder.ivThumbnail;
        viewHolder.webURL = newsArticle.getWebUrl();

        String imageUrl = newsArticle.getMultimediaUrl();
//        Picasso.with(getContext()).load(imageUrl)
//                .error(R.drawable.nyt_empty)
//                .placeholder(R.drawable.nyt_empty)
//                .into(ivTHumbnail);
    }

    @Override
    public int getItemCount() {
        return newsArticles.size();
    }

    public static String trimHeadline(final String text, int length)
    {
        // The letters [iIl1] are slim enough to only count as half a character.
        length += Math.ceil(text.replaceAll("[^iIl]", "").length() / 2.0d);

        if (text.length() > length)
        {
            return text.substring(0, length - 3) + "...";
        }

        return text;
    }
}
