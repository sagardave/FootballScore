package com.example.sdave.fcleaguescores.Adapters;

import android.graphics.Typeface;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;
import com.example.sdave.fcleaguescores.Objects.Fixture;
import com.example.sdave.myapplication.R;
import com.example.sdave.fcleaguescores.Utils.SvgDecoder;
import com.example.sdave.fcleaguescores.Utils.SvgDrawableTranscoder;
import com.example.sdave.fcleaguescores.Utils.SvgSoftwareLayerSetter;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by sdave on 4/22/2015.
 */
public class FixtureArrayAdapter extends BaseExpandableListAdapter {

    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;
    private FragmentActivity activity;
    private Map<String, ArrayList<Fixture>> fixturesCollections;
    private List<String> dates;

    public FixtureArrayAdapter(FragmentActivity activity, List<String> dates, Map<String, ArrayList<Fixture>> fixturesCollections) {
        this.activity = activity;
        this.fixturesCollections = fixturesCollections;
        this.dates = dates;
    }

    public View getChildView(final int groupPosition,final int position,boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_fixture, null);
        }

        ImageView imageViewHome = (ImageView) convertView.findViewById(R.id.homeTeamImg);
        ImageView imageViewAway = (ImageView) convertView.findViewById(R.id.awayTeamImg);
        Fixture currentFixture = fixturesCollections.get(dates.get(groupPosition)).get(position);

        String urlHomeTeam = currentFixture.getHomeTeamNameUrl();

        String urlAwayTeam = currentFixture.getAwayTeamNameUrl();

        if(urlHomeTeam != null && urlAwayTeam != null){
            if (!urlHomeTeam.contains("File:")){
                displayImageToView(urlHomeTeam, imageViewHome);
            }
            else{
                imageViewHome.setImageResource(R.drawable.abs__spinner_48_inner_holo);
            }

            if (!urlAwayTeam.contains("File:")){
                displayImageToView(urlAwayTeam, imageViewAway);
            }
            else{
                imageViewAway.setImageResource(R.drawable.abs__spinner_48_inner_holo);
            }
        }

        String result = currentFixture.getGoalsHomeTeam() + " - " + currentFixture.getGoalsAwayTeam();

        TextView tvResult = (TextView) convertView.findViewById(R.id.result);
        tvResult.setTypeface(null, Typeface.BOLD);


        if(result.equals("-1 - -1")){
            result = getLocalDateString(currentFixture.getDate());
        }

        TextView tvHomeName = (TextView) convertView.findViewById(R.id.homeTeamName);
        TextView tvAwayName = (TextView) convertView.findViewById(R.id.awayTeamName);
        //TextView tvDate = (TextView) convertView.findViewById(R.id.date);

        tvHomeName.setText(currentFixture.getHomeTeamName());
        tvAwayName.setText(currentFixture.getAwayTeamName());
        tvResult.setText(result);
        //tvDate.setText(getItem(position).getDate());

        return convertView;

    }

    @Override
    public int getGroupCount() {
        return dates.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return fixturesCollections.get(dates.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return dates.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return fixturesCollections.get(dates.get(groupPosition));
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String date = dates.get(groupPosition);

        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.fixture_date_item, null);
        }
        TextView dateView = (TextView) convertView.findViewById(R.id.date_header);
        dateView.setTypeface(null, Typeface.BOLD);
        dateView.setText(date);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    void displayImageToView(String url, ImageView imageView){
        Uri uri = Uri.parse(url);

        requestBuilder = Glide.with(activity)
                .using(Glide.buildStreamModelLoader(Uri.class, activity), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder()))
                .decoder(new SvgDecoder())
                .placeholder(R.drawable.abs__spinner_48_inner_holo)
                .error(R.drawable.abs__spinner_48_inner_holo)
                .animate(android.R.anim.fade_in)
                .listener(new SvgSoftwareLayerSetter<Uri>());

        requestBuilder
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        // SVG cannot be serialized so it's not worth to cache it
                .load(uri)
                .into(imageView);

    }

    String getLocalDateString(String date){

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date d = null;
        try {
            d = df.parse(date);
        }
        catch(Exception e){
        }
        String time = new SimpleDateFormat("h:mm a").format(d);

        return time;
    }

}