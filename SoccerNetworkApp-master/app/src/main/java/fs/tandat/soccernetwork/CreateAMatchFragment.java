package fs.tandat.soccernetwork;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
public class CreateAMatchFragment extends Fragment {
    Spinner fieldSpn;
    TextView addressTxt, hostUserTxt;
    EditText maxPlayerEdit, priceEdit;
    Button createBtn, btn_time_start, btn_date_start, btn_time_end, btn_date_end;
    FieldHelper fieldHelper = new FieldHelper(getActivity());
    UserHelper userHelper = new UserHelper(getActivity());
    MatchHelper matchHelper = new MatchHelper(getActivity());
    ArrayList<Field> fieldLst = new ArrayList<>();
    int field_id = 0;
    int host_id = 0;
    Match match;
    View view;
    String username;
    private int mYear, mMonth, mDay, mHour, mMinute;

    SupportMapFragment sMapFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Create new match");
        view = inflater.inflate(R.layout.fragment_create_a_match, container, false);


        username = getArguments().getString("username");
        sMapFragment = SupportMapFragment.newInstance();
        // get map fragment
        FragmentManager fm = getChildFragmentManager();
        fm.beginTransaction().add(R.id.mapMatch, sMapFragment).commit();


        maxPlayerEdit = (EditText) view.findViewById(R.id.maxPlayerTxt);
        priceEdit = (EditText) view.findViewById(R.id.priceTxt);
        addFields(view);
        setHostUser(view);

        //get current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // Get Current Time
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        //get date time
        btn_time_start = (Button) view.findViewById(R.id.btn_time_start);
        btn_date_start = (Button) view.findViewById(R.id.btn_date_start);
        btn_date_end = (Button) view.findViewById(R.id.btn_date_end);
        btn_time_end = (Button) view.findViewById(R.id.btn_time_end);

        btn_date_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        btn_date_start.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" +year);
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
                        btn_date_end.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" +year);
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
                },mHour,mMinute,false);
                timePickerDialog.show();
            }
        });

        createBtn = (Button)view.findViewById(R.id.createBtn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertMatch();
                Bundle args = new Bundle();
                args.putString("username",username);
                YourMatchesFragment yourMatchesFragment = new YourMatchesFragment();
                yourMatchesFragment.setArguments(args);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, yourMatchesFragment);
                fragmentTransaction.commit();
                Log.d("inserted", "inserted");
            }
        });
        return view;
    }

    public void addFields(View view){
        fieldSpn = (Spinner) view.findViewById(R.id.fieldSpn);
        addressTxt = (TextView) view.findViewById(R.id.addressTxt);

        fieldLst = fieldHelper.getListField();
        ArrayList<String> fieldNameLst = new ArrayList<>();
        for(int i=0; i<fieldLst.size(); i++){
            fieldNameLst.add(fieldLst.get(i).getField_name());
        }

        ArrayAdapter<String> fieldAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,fieldNameLst);
        fieldAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldSpn.setAdapter(fieldAdapter);
        fieldSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               String fieldSelected = (String) parent.getItemAtPosition(position);
               for (int i = 0; i < fieldLst.size(); i++) {
                   final Field f = fieldLst.get(i);
                   if (fieldSelected.equals(f.getField_name())) {
                       addressTxt.setText(f.getAddress());
                       field_id = fieldLst.get(i).getField_id();
                       // get google map
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

    }

    public void setHostUser(View view){
        hostUserTxt = (TextView)view.findViewById(R.id.hostUserTxt);

        Log.d("username", username);
        User user = new User();
        user = userHelper.getUser(username);
        host_id = user.getUser_id();
        hostUserTxt.setText(username);
    }
    //
    public void insertMatch(){
        int match_id = matchHelper.getFinallyMatch() + 1;
        Log.d("match_id",match_id+"");
        match = new Match();

        int maxPlayer = Integer.parseInt(maxPlayerEdit.getText().toString());
        int price = Integer.parseInt(priceEdit.getText().toString());
        String start_Time = btn_date_start.getText().toString() + " " + btn_time_start.getText().toString();
        String end_Time = btn_date_end.getText().toString() + " " + btn_time_end.getText().toString();

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

        matchHelper.createMatch(match);
    }

}
