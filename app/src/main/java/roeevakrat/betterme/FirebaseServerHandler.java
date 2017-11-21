package roeevakrat.betterme;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.lang3.SerializationUtils;

import java.util.concurrent.Executor;

/**
 * Created by Administrator on 21/11/2017.
 */

public class FirebaseServerHandler extends ServerHandler {

    private FirebaseAuth serverAuth;
    private boolean isSucceeded;
    private FirebaseStorage storage;
    private byte[] downloadedData;

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
    public boolean tryUploadData(UserData data) {

        byte[] serializedData = SerializationUtils.serialize(data);

        UploadTask uploadTask = storage.getReference(getServerUID()).putBytes(serializedData);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        isSucceeded = true;
                        feedback = "data uploaded successfully";
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        isSucceeded = false;
                        feedback = e.getMessage();
                    }
                });

        return isSucceeded;
    }

    @Override
    public UserData tryRetrieveData() {

        final long ONE_MEGABYTE = 1024 * 1024;
        StorageReference storageReference = storage.getReference(getServerUID());


        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                downloadedData = bytes;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                downloadedData = null;
            }
        });

        return SerializationUtils.deserialize(downloadedData);
    }

    private String getServerUID() {
        return serverAuth.getCurrentUser().getUid();
    }
}
