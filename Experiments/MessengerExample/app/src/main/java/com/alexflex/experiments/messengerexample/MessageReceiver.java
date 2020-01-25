package com.alexflex.experiments.messengerexample;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MessageReceiver extends FirebaseMessagingService {

    private FirebaseInstanceId instanceId = FirebaseInstanceId.getInstance();
    private final String LOG_TAG = "MessageReceiver";
    private String my_token = null;

    @Override
    public void onCreate(){
        instanceId.getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(task.isCanceled()){
                    Log.w(LOG_TAG, "The \"get id\" task is cancelled!");
                } else if(!(task.isSuccessful())){
                    Log.e(LOG_TAG, "The task failed");
                } else {
                    task.isSuccessful();
                    try {
                        my_token = task.getResult().getToken();

                    } catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onNewToken(@NonNull String token){
        my_token = token;

    }
}
