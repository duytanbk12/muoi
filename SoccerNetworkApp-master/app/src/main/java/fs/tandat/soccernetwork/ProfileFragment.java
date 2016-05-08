package fs.tandat.soccernetwork;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fs.tandat.soccernetwork.bean.User;
import fs.tandat.soccernetwork.helpers.UserHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    String username;
    EditText userName,phone,email,password;
    Button edit,logout;
    User user;
    UserHelper userHelper;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Your Profile");
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_profile, container, false);
        username  = getArguments().getString("username");
        userName=(EditText)v.findViewById(R.id.txt_UsernameProfile);
        phone=(EditText)v.findViewById(R.id.txt_PhoneProfile);
        email=(EditText)v.findViewById(R.id.txt_EmailProfile);
        password=(EditText)v.findViewById(R.id.txt_PasswordProfile);
        edit=(Button)v.findViewById(R.id.btn_EditProfile);
        logout=(Button)v.findViewById(R.id.btn_Logout);

        // get user information
        user=new User();
        userHelper=new UserHelper(getActivity());
        user=new User();
        user=userHelper.getUser(username);

        userName.setText(user.getUsername());
        email.setText(user.getEmail());
        phone.setText(user.getPhone());
        password.setText(user.getPassword());

        //edit information
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userHelper.updateUser(user.getUser_id(), userName.getText().toString(), password.getText().toString(),
                        email.getText().toString(), phone.getText().toString());
                Toast.makeText(getActivity(), "Edit succesfully", Toast.LENGTH_SHORT).show();
                return;


            }
        });

        //logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), LoginActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in);
            }
        });

        return v;
    }

}
