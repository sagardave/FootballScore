package com.example.sdave.fcleaguescores.Adapters;

import android.content.Context;
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
import com.example.sdave.fcleaguescores.Utils.SvgSoftwareLayerSetter;
import com.example.sdave.fcleaguescores.Objects.Team;
import com.example.sdave.myapplication.R;
import com.example.sdave.fcleaguescores.Utils.SvgDecoder;
import com.example.sdave.fcleaguescores.Utils.SvgDrawableTranscoder;

import java.io.InputStream;

/**
 * Created by sdave on 4/16/2015.
 */
public class TeamArrayAdapter extends ArrayAdapter<Team> {
    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;

    public TeamArrayAdapter(Context context) {
        super(context, 0);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_team, null);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageButton);

        String url = getItem(position).getImageUrl();

        if (!url.contains("File:")){
            displayImageToView(url, imageView);
        }
        else{
            imageView.setImageResource(R.drawable.abs__spinner_48_outer_holo);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.teamName);
        TextView tvShortName = (TextView) convertView.findViewById(R.id.teamShortName);
        TextView tvMarketVal = (TextView) convertView.findViewById(R.id.teamMarketValue);
        tvName.setText(getItem(position).getName());
        tvShortName.setText("");
        tvMarketVal.setText(getItem(position).getMarketValue());

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
                .placeholder(R.drawable.abs__spinner_48_outer_holo)
                .error(R.drawable.abs__spinner_48_outer_holo)
                .animate(android.R.anim.fade_in)
                .listener(new SvgSoftwareLayerSetter<Uri>());

        requestBuilder
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .load(uri)
                .into(imageView);
    }

}
