package com.example.sdave.fcleaguescores.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;
import com.example.sdave.fcleaguescores.Objects.LeagueTableRow;
import com.example.sdave.myapplication.R;
import com.example.sdave.fcleaguescores.Utils.SvgDecoder;
import com.example.sdave.fcleaguescores.Utils.SvgDrawableTranscoder;
import com.example.sdave.fcleaguescores.Utils.SvgSoftwareLayerSetter;

import java.io.InputStream;

/**
 * Created by Sagar on 4/26/2015.
 */
public class LeagueTableAdapter extends ArrayAdapter<LeagueTableRow> {

    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;

    public LeagueTableAdapter(Context context) {
        super(context, 0);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_table_row, null);
        }

        ImageView imageViewTeam = (ImageView) convertView.findViewById(R.id.imageTeam);


        String teamUrl = getItem(position).getTeamUrl();


        if(teamUrl != null){
            if (!teamUrl.contains("File:")){
                displayImageToView(teamUrl, imageViewTeam);
            }
            else{
                imageViewTeam.setImageResource(R.drawable.abs__spinner_48_inner_holo);
            }
        }


        TextView tvPosition = (TextView) convertView.findViewById(R.id.position);
        TextView tvPoints = (TextView) convertView.findViewById(R.id.points);
        tvPoints.setTypeface(null, Typeface.BOLD);
        TextView tvPlayed = (TextView) convertView.findViewById(R.id.playedGames);

        TextView tvGoals = (TextView) convertView.findViewById(R.id.goals);
        TextView tvGoalsAgainst = (TextView) convertView.findViewById(R.id.goalsAgainst);
        TextView tvGoalDifference = (TextView) convertView.findViewById(R.id.goalDifference);
        TextView tvTeamName = (TextView) convertView.findViewById(R.id.tableTeamName);

        try{
            tvTeamName.setText(getItem(position).getTeamName());
            tvPosition.setText(String.valueOf(getItem(position).getPosition()));
        }
        catch(Exception e){
            String t = "f";
        }

        tvPoints.setText(String.valueOf(getItem(position).getPoints()));
        tvGoals.setText(String.valueOf(getItem(position).getGoals()));
        tvGoalsAgainst.setText(String.valueOf(getItem(position).getGoalsAgainst()));
        tvGoalDifference.setText(String.valueOf(getItem(position).getGoalDifference()));
        tvPlayed.setText(String.valueOf(getItem(position).getPlayedGames()));

        return convertView;
    }

    void displayImageToView(String url, ImageView imageView){
        Uri uri = Uri.parse(url);

        requestBuilder = Glide.with(getContext())
                .using(Glide.buildStreamModelLoader(Uri.class, getContext()), InputStream.class)
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

}
