package roeevakrat.betterme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CloudSyncScreen extends AppCompatActivity {

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

    //server auth
    private FirebaseAuth serverAuth;

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

    void tryRegister(){

        serverAuth.createUserWithEmailAndPassword(userUsername, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {

                    loginFeedbackView.setText("Registered successfully");
                    addUserDataToSharedprefs();

                } else {

                    loginFeedbackView.setText(task.getException().getMessage());
                }
            }
        });
    }

    void tryLogin(){

        serverAuth.signInWithEmailAndPassword(userUsername, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {

                    loginFeedbackView.setText("Logged-in successfully");
                    addUserDataToSharedprefs();

                } else {

                    loginFeedbackView.setText(task.getException().getMessage());
                }
            }
        });
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
        title.setTypeface(Typeface.createFromAsset(getAssets(), AppFontsDB.getInstance().getTitleFont()));
        body.setTypeface(Typeface.createFromAsset(getAssets(), AppFontsDB.getInstance().getBodyFont()));
        loginFeedbackView.setTypeface(Typeface.createFromAsset(getAssets(), AppFontsDB.getInstance().getBodyFont()));

        //initialize shared preferences
        appMap = getApplicationContext().getSharedPreferences(KeysDB.getInstance().SHARED_PREFS, MODE_PRIVATE);

        //initialize auth object
        serverAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userUsername = username.getText().toString();
                userPassword = password.getText().toString();

                tryRegister();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userUsername = username.getText().toString();
                userPassword = password.getText().toString();

                tryLogin();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent counterScreenIntent = new Intent(CloudSyncScreen.this, CounterScreen.class);
                startActivity(counterScreenIntent);

            }
        });

    }
}
