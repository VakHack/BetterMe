package roeevakrat.betterme;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static roeevakrat.betterme.CloudSyncScreen.loginErrors.LOGIN_SUCCESS;
import static roeevakrat.betterme.CloudSyncScreen.loginErrors.NO_SUCH_USERNAME;
import static roeevakrat.betterme.CloudSyncScreen.registerErrors.REGISTER_SUCCESS;
import static roeevakrat.betterme.CloudSyncScreen.registerErrors.USERNAME_TAKEN;

public class CloudSyncScreen extends AppCompatActivity {

    private Button login;
    private Button register;

    private EditText username;
    private EditText password;

    String userPassword;
    String userUsername;

    TextView loginFeedback;

    //database
    SharedPreferences appMap;
    SharedPreferences.Editor appMapEditor;

    public enum loginErrors{

        NO_SUCH_USERNAME, PASSWORD_INCORRECT, LOGIN_SUCCESS
    }

    public enum registerErrors{

       USERNAME_TAKEN, PASSWORD_ILLEGAL, REGISTER_SUCCESS
    }

    loginErrors tryLogin(){

        return LOGIN_SUCCESS;
    }

    registerErrors tryRegister(){

        return REGISTER_SUCCESS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_sync_screen);

        //init activity views
        login = (Button)findViewById(R.id.loginButton);
        register = (Button)findViewById(R.id.registerButton);
        username = (EditText)findViewById(R.id.loginEmail);
        password = (EditText)findViewById(R.id.loginPassword);
        loginFeedback = (TextView)findViewById(R.id.loginFeedback);

        //initialize shared preferences
        appMap = getApplicationContext().getSharedPreferences(KeysDB.getInstance().SHARED_PREFS, MODE_PRIVATE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userUsername = username.getText().toString();
                userPassword = password.getText().toString();

                loginErrors feedback = tryLogin();

                if(feedback == LOGIN_SUCCESS){

                    loginFeedback.setText("Logged in successfully");

                    //add user details to sharedprefs
                    appMapEditor = appMap.edit();
                    appMapEditor.putBoolean(KeysDB.getInstance().LOGGED_IN_CLOUD, true);
                    appMapEditor.putString(KeysDB.getInstance().USER_PASSWORD, userPassword);
                    appMapEditor.putString(KeysDB.getInstance().USERNAME, userUsername);
                    appMapEditor.apply();

                } else if (feedback == NO_SUCH_USERNAME) {

                    loginFeedback.setText("No such username. If you have not register, please do so");

                } else {

                    loginFeedback.setText("Incorrect password. Please try again");
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userUsername = username.getText().toString();
                userPassword = password.getText().toString();

                registerErrors feedback = tryRegister();

                if(feedback == REGISTER_SUCCESS){

                    loginFeedback.setText("Registered and logged-in successfully");

                    //add user details to sharedprefs
                    appMapEditor = appMap.edit();
                    appMapEditor.putBoolean(KeysDB.getInstance().LOGGED_IN_CLOUD, true);
                    appMapEditor.putString(KeysDB.getInstance().USER_PASSWORD, userPassword);
                    appMapEditor.putString(KeysDB.getInstance().USERNAME, userUsername);
                    appMapEditor.apply();

                } else if (feedback == USERNAME_TAKEN) {

                    loginFeedback.setText("Username or email is already taken, please try another one");

                } else {

                    loginFeedback.setText("Password too short, should be at least 5 characters");
                }
            }
        });
    }
}
