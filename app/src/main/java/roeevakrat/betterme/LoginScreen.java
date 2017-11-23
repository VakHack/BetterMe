package roeevakrat.betterme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginScreen extends AppCompatActivity {

    private TextView title;
    private TextView body;
    private TextView loginFeedbackView;
    private ImageView backButton;

    private Button login;
    private Button register;

    private EditText username;
    private EditText password;

    private String userPassword;
    private String userUsername;

    //server
    private ServerHandler server;

    //database
    private SharedPreferences appMap;
    private SharedPreferences.Editor appMapEditor;

    private void addUserDataToSharedprefs(){

        appMapEditor = appMap.edit();
        appMapEditor.putBoolean(KeysDB.getInstance().LOGGED_IN_CLOUD, true);
        appMapEditor.putString(KeysDB.getInstance().USER_PASSWORD, userPassword);
        appMapEditor.putString(KeysDB.getInstance().USERNAME, userUsername);

        appMapEditor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_sync_screen);

        //initialize activity views
        login = (Button)findViewById(R.id.loginButton);
        register = (Button)findViewById(R.id.registerButton);
        username = (EditText)findViewById(R.id.loginEmail);
        password = (EditText)findViewById(R.id.loginPassword);
        loginFeedbackView = (TextView)findViewById(R.id.loginFeedback);
        title = (TextView)findViewById(R.id.syncScreenTitle);
        body = (TextView)findViewById(R.id.syncScreenText);
        backButton = (ImageView)findViewById(R.id.syncScreenBackButton);

        //set fonts
        title.setTypeface(Typeface.createFromAsset(getAssets(), FontsDB.getInstance().getTitleFont()));
        body.setTypeface(Typeface.createFromAsset(getAssets(), FontsDB.getInstance().getBodyFont()));
        loginFeedbackView.setTypeface(Typeface.createFromAsset(getAssets(), FontsDB.getInstance().getBodyFont()));

        //initialize shared preferences
        appMap = getApplicationContext().getSharedPreferences(KeysDB.getInstance().SHARED_PREFS, MODE_PRIVATE);

        //initialize auth object
        server = new FirebaseServerHandler(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userUsername = username.getText().toString();
                userPassword = password.getText().toString();

                server.tryRegister(userUsername, userPassword);

                loginFeedbackView.setText(server.getLogFeedback());
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userUsername = username.getText().toString();
                userPassword = password.getText().toString();

                if(server.tryLogin(userUsername, userPassword)){

                    addUserDataToSharedprefs();
                }

                while(server.getLogFeedback() == null);
                loginFeedbackView.setText(server.getLogFeedback());
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent counterScreenIntent = new Intent(LoginScreen.this, CounterScreen.class);
                startActivity(counterScreenIntent);

            }
        });

    }
}
