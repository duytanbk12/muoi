package fs.tandat.soccernetwork;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import fs.tandat.soccernetwork.bean.Match;
import fs.tandat.soccernetwork.helpers.MatchHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingMatchesFragment extends Fragment {

    private String username;
    private TableLayout tblUpcoming;
    private FragmentTransaction ft;
    public UpcomingMatchesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // set Action Bar Title
        getActivity().setTitle("Upcoming matches");
        username = getArguments().getString("username");
        View view = inflater.inflate(R.layout.fragment_upcoming_matches, container, false);
        tblUpcoming =(TableLayout) view.findViewById(R.id.tblUpcomingMatches);

        ft = getFragmentManager().beginTransaction();
        loadUpcomingMatches();
        return view;
    }

    private void loadUpcomingMatches() {
        MatchHelper matchHelper = new MatchHelper(getActivity());
        ArrayList<Match> matches = matchHelper.getUpcommingMatches(username);

        TableLayout.LayoutParams tlParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams trParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        trParams.setMargins(0, 8, 0, 8);
        if(matches != null){
            for(int i=0; i<matches.size(); i++){
                final Match m = matches.get(i);

                TableRow tr = new TableRow(getActivity());

                int textSize = 16;
                TextView txtMatchID = new TextView(getActivity());
                txtMatchID.setText(m.getMatch_id() + "");
                txtMatchID.setLayoutParams(trParams);
                txtMatchID.setGravity(Gravity.CENTER);
                txtMatchID.setTextSize(textSize);
                tr.addView(txtMatchID);

                TextView txtFieldName = new TextView(getActivity());
                txtFieldName.setText(m.getField_name());
                txtFieldName.setLayoutParams(trParams);
                txtFieldName.setGravity(Gravity.CENTER);
                txtFieldName.setTextSize(textSize);
                tr.addView(txtFieldName);

                TextView txtHostName = new TextView(getActivity());
                txtHostName.setText(m.getUsername());
                txtHostName.setLayoutParams(trParams);
                txtHostName.setGravity(Gravity.CENTER);
                txtHostName.setTextSize(textSize);
                tr.addView(txtHostName);

                TextView txtMaxPlayers = new TextView(getActivity());
                txtMaxPlayers.setText(m.getMaximum_players() + "");
                txtMaxPlayers.setLayoutParams(trParams);
                txtMaxPlayers.setGravity(Gravity.CENTER);
                txtMaxPlayers.setTextSize(textSize);
                tr.addView(txtMaxPlayers);

                TextView txtPrice = new TextView(getActivity());
                txtPrice.setText(m.getPriceString());
                txtPrice.setLayoutParams(trParams);
                txtPrice.setGravity(Gravity.CENTER);
                txtPrice.setTextSize(textSize);
                tr.addView(txtPrice);

                TextView txtStartTime = new TextView(getActivity());
                txtStartTime.setText(m.getStart_time().replace(" ","\n"));
                txtStartTime.setLayoutParams(trParams);
                txtStartTime.setTextSize(textSize);
                txtStartTime.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tr.addView(txtStartTime);

                TextView txtEndTime = new TextView(getActivity());
                txtEndTime.setText(m.getEnd_time().replace(" ", "\n"));
                txtEndTime.setLayoutParams(trParams);
                txtEndTime.setTextSize(textSize);
                txtEndTime.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tr.addView(txtEndTime);

                tr.setGravity(Gravity.CENTER);
                tr.setLayoutParams(tlParams);
                if(i%2==1) tr.setBackgroundColor(getResources().getColor(R.color.chromeish03));
                else tr.setBackgroundColor(getResources().getColor(R.color.chromeish04));
                tr.setClickable(true);
                tr.setHovered(true);
                final AlphaAnimation trClick = new AlphaAnimation(1F, 0.5F);
                tr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.startAnimation(trClick);

                        MatchDetailFragment fragment = new MatchDetailFragment();
                        // set Arguments
                        Bundle args = new Bundle();
                        args.putString("username", username);
                        args.putInt("match_id", m.getMatch_id());
                        fragment.setArguments(args);

                        ft.replace(R.id.fragment_container, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                });
                tblUpcoming.addView(tr);
            }
        }
    }

}
