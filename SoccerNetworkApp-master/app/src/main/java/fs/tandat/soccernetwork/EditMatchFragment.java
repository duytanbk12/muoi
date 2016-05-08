package fs.tandat.soccernetwork;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Calendar;

import fs.tandat.soccernetwork.bean.Field;
import fs.tandat.soccernetwork.bean.Match;
import fs.tandat.soccernetwork.bean.User;
import fs.tandat.soccernetwork.helpers.FieldHelper;
import fs.tandat.soccernetwork.helpers.MatchHelper;
import fs.tandat.soccernetwork.helpers.UserHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditMatchFragment extends Fragment {
    private String username;
    private int match_id;
    private double lattitude;
    private double longitude;

    private TextView txtAddress;
    private TextView txtHostPlayer;
    private EditText txtMaxPlayers;
    private EditText txtPrice;
    private Spinner fieldSpn;
    private Button btn_date_start, btn_time_start, btn_date_end, btn_time_end;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private com.getbase.floatingactionbutton.FloatingActionButton fabDelete, fabUpdate;
    SupportMapFragment sMapFragment;
    Match match;
    ArrayList<Field> fieldLst;

    UserHelper userHelper;
    MatchHelper matchHelper;
    int field_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Edit match");
        View view = inflater.inflate(R.layout.fragment_edit_match, container, false);


        // get Attribute
        username = getArguments().getString("username");
        match_id = getArguments().getInt("match_id");

        //load map
        sMapFragment = SupportMapFragment.newInstance();
        FragmentManager fm = getChildFragmentManager();
        fm.beginTransaction().add(R.id.mapMatch, sMapFragment).commit();


        //get current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // Get Current Time
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // get widgets
        fieldSpn = (Spinner)view.findViewById(R.id.fieldSpn);
        txtAddress = (TextView)view.findViewById(R.id.addressTxt);
        txtHostPlayer = (TextView)view.findViewById(R.id.hostUserTxt);
        txtMaxPlayers = (EditText) view.findViewById(R.id.maxPlayerTxt);
        txtPrice = (EditText) view.findViewById(R.id.priceTxt);
        btn_date_start = (Button) view.findViewById(R.id.btn_date_start);
        btn_time_start = (Button)view.findViewById(R.id.btn_time_start);
        btn_date_end = (Button) view.findViewById(R.id.btn_date_end);
        btn_time_end = (Button)view.findViewById(R.id.btn_time_end);
        fabUpdate = (FloatingActionButton) view.findViewById(R.id.fabUpdate);
        fabDelete = (FloatingActionButton) view.findViewById(R.id.fabDelete);


        // get data from database
        MatchHelper matchHelper = new MatchHelper(getActivity());
        FieldHelper fieldHelper = new FieldHelper(getActivity());

        //get data and set for spinner
        fieldLst = fieldHelper.getListField();
        ArrayList<String> fieldNameLst = new ArrayList<>();


        Match m = matchHelper.getMatch(match_id);
        int index = 0;
        if(m!= null){
            Field f = fieldHelper.getField(m.getField_id());

            if(f!=null){
                for(int i=0; i<fieldLst.size(); i++){
                    fieldNameLst.add(fieldLst.get(i).getField_name());
                    if(fieldLst.get(i).getField_id() == m.getField_id()){
                        index = i;
                    }
                }

                ArrayAdapter<String> fieldAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item,fieldNameLst);
                fieldAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                fieldSpn.setAdapter(fieldAdapter);
                fieldSpn.setSelection(index);

                //txtFieldName.setText(f.getField_name());
                txtAddress.setText(f.getAddress());
                txtHostPlayer.setText(username);
                txtMaxPlayers.setText(m.getMaximum_players()+"");
                txtPrice.setText(m.getPrice()+"");

                //resovle datetime_start
                String datetime_start = m.getStart_time();
                String date_start;
                String time_start;
                if(!datetime_start.isEmpty()){
                    String[] arr_datetime_start = datetime_start.split(" ");
                    date_start = arr_datetime_start[0];
                    time_start = arr_datetime_start[1];
                    btn_date_start.setText(date_start);
                    btn_time_start.setText(time_start);
                }


                //resovle datetime_end
                String datetime_end = m.getEnd_time();
                String date_end;
                String time_end;
                if(!datetime_end.isEmpty()){
                    String[] arr_datetime_end = datetime_start.split(" ");
                    date_end = arr_datetime_end[0];
                    time_end = arr_datetime_end[1];
                    btn_date_end.setText(date_end);
                    btn_time_end.setText(time_end);
                }


                lattitude = f.getLatitude();
                longitude = f.getLongitude();
            }

            //set edit field
            fieldSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                   @Override
                   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                       String fieldSelected = (String) parent.getItemAtPosition(position);

                       for (int i = 0; i < fieldLst.size(); i++) {
                           final Field f = fieldLst.get(i);
                           if (fieldSelected.equals(f.getField_name())) {
                               txtAddress.setText(f.getAddress());
                               txtAddress.setTextColor(Color.GREEN);
                               field_id = f.getField_id();

                               sMapFragment.getMapAsync(new OnMapReadyCallback() {
                                   @Override
                                   public void onMapReady(GoogleMap googleMap) {
                                       LatLng latLng = new LatLng(f.getLatitude(), f.getLongitude());
                                       googleMap.addMarker(new MarkerOptions().position(latLng).title(f.getField_name()));
                                       googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                                   }
                               });
                           }
                       }
                   }

                   @Override
                   public void onNothingSelected(AdapterView<?> parent) {

                   }
               }
            );

            //set datetime on Timepicker and DatePicker
            btn_date_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            btn_date_start.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            });

            btn_time_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            btn_time_start.setText(hourOfDay + ":" + minute);
                        }
                    },mHour,mMinute,false);
                    timePickerDialog.show();
                }
            });

            btn_date_end.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            btn_date_end.setText(dayOfMonth + "-" + (monthOfYear+1) + "-" +year);
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            });

            btn_time_end.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            btn_time_end.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
                    timePickerDialog.show();
                }
            });


        }

        fabUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMatch();
                Toast.makeText(getActivity(), "Update successully", Toast.LENGTH_SHORT).show();
                Bundle args = new Bundle();
                args.putString("username", username);
                args.putInt("match_id", match_id);
                MatchDetailFragment f = new MatchDetailFragment();
                f.setArguments(args);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, f);
                fragmentTransaction.commit();
            }
        });


        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMatch();
                Toast.makeText(getActivity(), "Delete successfully", Toast.LENGTH_SHORT).show();

                Bundle args = new Bundle();
                args.putString("username", username);
                YourMatchesFragment yourMatchesFragment = new YourMatchesFragment();
                yourMatchesFragment.setArguments(args);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, yourMatchesFragment);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    public void updateMatch(){
        match = new Match();
        int maxPlayer = Integer.parseInt(txtMaxPlayers.getText().toString());
        int price = Integer.parseInt(txtPrice.getText().toString());
        String start_Time = btn_date_start.getText().toString() + " " + btn_time_start.getText().toString();
        String end_Time = btn_date_end.getText().toString() + " " + btn_time_end.getText().toString();

        User user = new User();
        userHelper = new UserHelper(getActivity());
        user = userHelper.getUser(username);
        int host_id = user.getUser_id();

        matchHelper = new MatchHelper(getActivity());
        match.setMatch_id(match_id);
        match.setField_id(field_id);
        match.setHost_id(host_id);
        match.setMaximum_players(maxPlayer);
        match.setPrice(price);
        match.setStart_time(start_Time);
        match.setEnd_time(end_Time);
        match.setUpdated("");
        match.setCreated("");
        match.setDeleted("");

        matchHelper.updateMatch(match);
    }

    public void deleteMatch(){
        matchHelper = new MatchHelper(getActivity());
        Log.d("match_id before", match_id+"");
        Match match = new Match();
        match.setMatch_id(match_id);
        matchHelper.deleteMatch(match);
        Log.d("match_id after", match_id+"");

    }
}
