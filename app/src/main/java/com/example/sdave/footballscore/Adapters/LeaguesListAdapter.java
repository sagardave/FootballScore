package com.example.sdave.footballscore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sdave.footballscore.Objects.League;
import com.example.sdave.footballscore.R;

/**
 * Created by sdave on 4/23/2015.
 */
public class LeaguesListAdapter  extends ArrayAdapter<League> {

    public LeaguesListAdapter(Context context) {
        super(context, 0);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_leagues, null);
        }
        String leagueCaption = getItem(position).getCaption();
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageLeagueButton);

        imageView.setImageResource(getContext().getResources().getIdentifier(getItem(position).getName().toLowerCase(), "drawable", getContext().getPackageName()));

        TextView tvLeagueName = (TextView) convertView.findViewById(R.id.leagueName);
        //TextView tvTeams = (TextView) convertView.findViewById(R.id.numOfTeams);
        //TextView tvGames = (TextView) convertView.findViewById(R.id.numOfGames);
        tvLeagueName.setText(leagueCaption.substring(0, leagueCaption.length()-8));
        //tvTeams.setText(getItem(position).getNumOfTeams() + " Teams");
        //tvGames.setText(getItem(position).getNumOfGame() + " Games");

        return convertView;

    }

}
