package roeevakrat.betterme;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

import java.util.concurrent.Executor;

/**
 * Created by Administrator on 21/11/2017.
 */

public class FirebaseServerHandler extends ServerHandler {

    private FirebaseAuth serverAuth;
    private boolean isSucceeded;
    FirebaseStorage storage;

    public FirebaseServerHandler(Context context) {
        super(context);

        serverAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    @Override
    public boolean tryRegister(String username, String password) {

        serverAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {

                    isSucceeded = true;
                    feedback = "Register Successfully";

                } else {

                    isSucceeded = false;
                    feedback = task.getException().getMessage();
                }
            }
        });

        return isSucceeded;
    }

    @Override
    public boolean tryLogin(String username, String password) {

        serverAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {

                    isSucceeded = true;
                    feedback = "Logged in Successfully";

                } else {

                    isSucceeded = false;
                    feedback = task.getException().getMessage();
                }
            }
        });

        return isSucceeded;
    }

    @Override
    public boolean tryUploadData(BetterMeUserData data) {
        return false;
    }

    @Override
    public BetterMeUserData tryRetrieveData() {
        return null;
    }

    @Override
    public String getServerUID() {
        return serverAuth.getCurrentUser().getUid();
    }
}
