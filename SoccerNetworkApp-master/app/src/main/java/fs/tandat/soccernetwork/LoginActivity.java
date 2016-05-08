package fs.tandat.soccernetwork;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import fs.tandat.soccernetwork.helpers.UserHelper;

public class LoginActivity extends AppCompatActivity {
    EditText txtUsername;
    EditText txtPassword;
    Button btLogin;
    Button btRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Login");
        setContentView(R.layout.activity_login);


        txtUsername = (EditText) findViewById(R.id.txtUssername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btLogin = (Button) findViewById(R.id.btLogin);
        btRegister = (Button) findViewById(R.id.btRegister);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(in);
            }
        });

    }

    private void checkLogin(){
        UserHelper userHelper = new UserHelper(LoginActivity.this);
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        if(username.trim().isEmpty() || password.trim().isEmpty()){
            if(username.trim().isEmpty()){
                txtUsername.setError("Username is empty");
            }
            if(password.trim().isEmpty()){
                txtPassword.setError("Password is empty");
            }
        }
        else{
            if (userHelper.checkUser(username, password)){
                Log.d("LOGIN", username + " " + password);
                Intent in = new Intent(LoginActivity.this, MainActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                in.putExtra("username", username);
//                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                SharedPreferences.Editor editor = prefs.edit();
//                editor.putString("username", username);
                startActivity(in);
            }
        }

    }
}
