package fs.tandat.soccernetwork;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import fs.tandat.soccernetwork.bean.Field;
import fs.tandat.soccernetwork.bean.Match;
import fs.tandat.soccernetwork.bean.User;
import fs.tandat.soccernetwork.helpers.FieldHelper;
import fs.tandat.soccernetwork.helpers.MatchHelper;
import fs.tandat.soccernetwork.helpers.SlotHelper;
import fs.tandat.soccernetwork.helpers.UserHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class MatchDetailFragment extends Fragment{
    private String username;
    private int match_id;
    private int user_id;
    private double lattitude;
    private double longitude;

    private TextView txtFieldName;
    private TextView txtAddress;
    private TextView txtHostPlayer;
    private TextView txtMaxPlayers;
    private TextView txtPrice;
    private TextView txtStartTime;
    private TextView txtEndTime;
    private TextView txtRemainingSlots;
    private com.getbase.floatingactionbutton.FloatingActionButton fabJoinMatch;
    private com.getbase.floatingactionbutton.FloatingActionButton fabSetUpMatch;

    SupportMapFragment sMapFragment;

    public MatchDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Match Details");
        sMapFragment = SupportMapFragment.newInstance();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_match_detail, container, false);
        // get Attribute
        username = getArguments().getString("username");
        user_id = (new UserHelper(getActivity())).getUser(username).getUser_id();
        match_id = getArguments().getInt("match_id");
        // get map fragment
        FragmentManager fm = getChildFragmentManager();
        fm.beginTransaction().add(R.id.mapMatch, sMapFragment).commit();
        sMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng latLng = new LatLng(lattitude, longitude);
                googleMap.addMarker(new MarkerOptions().position(latLng).title(txtFieldName.getText().toString()));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
        });

        // get floating action button


        // get widgets
        txtFieldName = (TextView)view.findViewById(R.id.txtFieldName);
        txtAddress = (TextView)view.findViewById(R.id.txtAddress);
        txtHostPlayer = (TextView)view.findViewById(R.id.txtHostPlayer);
        txtMaxPlayers = (TextView)view.findViewById(R.id.txtMaxPlayers);
        txtPrice = (TextView)view.findViewById(R.id.txtPrice);
        txtStartTime = (TextView)view.findViewById(R.id.txtStartTime);
        txtEndTime = (TextView)view.findViewById(R.id.txtEndTime);
        txtRemainingSlots = (TextView) view.findViewById(R.id.txtRemainingSlots);
        fabJoinMatch = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.fabJoinMatch);
        fabSetUpMatch = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.fabSetUpMatch);


        // get data from database
        final MatchHelper matchHelper = new MatchHelper(getActivity());
        FieldHelper fieldHelper = new FieldHelper(getActivity());
        final Match m = matchHelper.getMatch(match_id);
        User u;
        if(m!= null) {
            Field f = fieldHelper.getField(m.getField_id());
            if (f != null) {
                u = (new UserHelper(getActivity())).getUserByUserId(m.getHost_id());

                int remainingSlots = matchHelper.getRemainingSlots(match_id);
                txtRemainingSlots.setText(remainingSlots + "");
                txtFieldName.setText(f.getField_name());
                txtAddress.setText(f.getAddress());
                txtHostPlayer.setText(u.getUsername());
                txtMaxPlayers.setText(m.getMaximum_players() + "");
                txtPrice.setText(m.getPriceString());
                txtStartTime.setText(m.getStart_time());
                txtEndTime.setText(m.getEnd_time());
                lattitude = f.getLatitude();
                longitude = f.getLongitude();
            }


            // button join match
            fabJoinMatch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder aDialogBulder = new AlertDialog.Builder(getActivity());
                    aDialogBulder.setTitle("Join this match?");
                    final EditText input = new EditText(getActivity());
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    input.setHint("How many slots do you want to reserve?");
                    aDialogBulder.setView(input);
                    aDialogBulder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    aDialogBulder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    final AlertDialog dialog = aDialogBulder.create();
                    dialog.show();
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int num_slots = Integer.parseInt(input.getText().toString());
                            int remainingSlots = matchHelper.getRemainingSlots(match_id);
                            if (num_slots > remainingSlots) {
                                input.setError("Does not have enough slots.");
                            } else {
                                SlotHelper slotHelper = new SlotHelper(getActivity());
                                User u = new UserHelper(getActivity()).getUser(username);
                                if (slotHelper.addSlots(match_id, u.getUser_id(), num_slots)) {
                                    Toast.makeText(getActivity(), "Join match succesfully", Toast.LENGTH_SHORT).show();
                                }

                                // load lai fragment
                                MatchDetailFragment fragment = new MatchDetailFragment();
                                // set Arguments
                                Bundle args = new Bundle();
                                args.putString("username", username);
                                args.putInt("match_id", m.getMatch_id());
                                fragment.setArguments(args);

                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.replace(R.id.fragment_container, fragment);
                                ft.commit();
                                dialog.dismiss();
                            }
                        }
                    });
                }
            });

            // if login user is the host of the match, fabSetupMatch --> editMatch
            if (user_id == m.getHost_id()){
                fabSetUpMatch.setTitle("Edit this match");
                fabSetUpMatch.setIcon(R.drawable.update_icon);
                fabSetUpMatch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditMatchFragment editMatchFragment = new EditMatchFragment();
                        Bundle args = new Bundle();
                        args.putString("username", username);
                        args.putInt("match_id", match_id);
                        editMatchFragment.setArguments(args);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, editMatchFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
            }
            // fabSetupMatch --> create new match
            else{
                fabSetUpMatch.setTitle("Create new match");
                fabSetUpMatch.setIcon(R.drawable.new_icon);
                fabSetUpMatch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CreateAMatchFragment setUpMatch = new CreateAMatchFragment();
                        Bundle args = new Bundle();
                        args.putString("username", username);
                        args.putInt("match_id", match_id);
                        setUpMatch.setArguments(args);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, setUpMatch);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
            }
        }
        return view;
    }
}
