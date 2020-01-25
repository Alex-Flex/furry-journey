package com.alexflex.experiments.messengerexample;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.alexflex.experiments.messengerexample.logic.Message;
import com.daasuu.bl.ArrowDirection;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MyChat extends AppCompatActivity {

    private ListView msgsLayout;
    private EditText message;
    private Button sender;
    private final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), REQUEST_CODE);
        } else {
            Toast.makeText(this, R.string.you_are_authorized, Toast.LENGTH_LONG).show();
            showMessages();
        }
        msgsLayout = findViewById(R.id.messages_list);
        message = findViewById(R.id.message);
        sender = findViewById(R.id.send_button);

        //listener для кнопки сообщений
        sender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!message.getText().toString().equals("")) {
                    FirebaseDatabase.getInstance().getReference().push()
                            .setValue(new Message(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                    message.getText().toString(),
                                    Color.GREEN, ArrowDirection.RIGHT));
                }
                message.setText("");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, R.string.you_are_authorized, Toast.LENGTH_LONG).show();
                showMessages();
            } else {
                Toast.makeText(this, R.string.you_are_not_authorized, Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void showMessages(){
        msgsLayout.setAdapter(new FirebaseListAdapter<Message>(this,
                Message.class,
                R.id.messages_list,
                FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, Message model, int position){
                LinearLayout layout = new LinearLayout(v.getContext());
                layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                msgsLayout.addView(layout);
                if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(model.getUsername())){
                    layout.setGravity(Gravity.END);
                    new Message(model.getUsername(), model.getText(), Color.GREEN, ArrowDirection.RIGHT).drawIt(msgsLayout);
                } else {
                    layout.setGravity(Gravity.START);
                    new Message(model.getUsername(), model.getText(), Color.BLUE, ArrowDirection.LEFT).drawIt(msgsLayout);
                }
            }
        });
    }
}
