package fs.tandat.soccernetwork;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fs.tandat.soccernetwork.bean.User;
import fs.tandat.soccernetwork.helpers.UserHelper;

public class RegisterActivity extends AppCompatActivity {
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    EditText txtUsername;
    EditText txtPassword;
    EditText txtEmail;
    EditText txtPhone;
    EditText txtAddress;
    Button btRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtUsername = (EditText) findViewById(R.id.txtRegisterUsername);
        txtPassword = (EditText) findViewById(R.id.txtRegisterPassword);
        txtEmail =(EditText) findViewById(R.id.txtRegisterEmail);
        txtAddress = (EditText)findViewById(R.id.txtRegisterAddress);
        txtPhone = (EditText)findViewById(R.id.txtRegisterPhone);
        btRegister = (Button)findViewById(R.id.btRegister);

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRegister();
            }
        });
    }

    private void doRegister(){
        boolean validate = true;
        String username = txtUsername.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String address = txtAddress.getText().toString().trim();
        String phone = txtPhone.getText().toString().trim();

        // validate input
        if(username.isEmpty()){
            txtUsername.setError("Username is empty");
            validate = false;
        }
        if(password.isEmpty()){
            txtPassword.setError("Password is empty");
            validate = false;
        }
        if(!email.matches(EMAIL_PATTERN)){
            txtEmail.setError("Email is not correct");
            validate = false;
        }
        if(address.isEmpty()){
            txtAddress.setError("Address is empty");
            validate = false;
        }
        if(phone.isEmpty()){
            txtPhone.setError("Phone Number is empty");
            validate = false;
        }

        // do register
        if(validate= true){
            User newUser = new User(username, password, email, phone);
            UserHelper userHelper = new UserHelper(RegisterActivity.this);
            if(userHelper.addUser(newUser)){
                Toast.makeText(RegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(in);
            } else{
                Toast.makeText(RegisterActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
