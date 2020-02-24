package com.alexflex.soft.itipgu.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.alexflex.soft.itipgu.MyApp;
import com.alexflex.soft.itipgu.R;
import com.alexflex.soft.itipgu.logic.CommonMethods;
import com.alexflex.soft.itipgu.logic.MessageInConversation;
import com.daasuu.bl.ArrowDirection;
import com.daasuu.bl.BubbleLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class MessengerFragment extends Fragment {

    private ListView msgsLayout;
    private EditText message;
    private FloatingActionButton sender;
    private final int REQUEST_CODE = 0;
    private View fragmentView;
    private FirebaseListAdapter<MessageInConversation> adapter;

    public MessengerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_messenger, container, false);
        return fragmentView;
    }

    @Override
    public void onStart(){
        super.onStart();
        msgsLayout = fragmentView.findViewById(R.id.messages_list);
        message = fragmentView.findViewById(R.id.message);
        sender = fragmentView.findViewById(R.id.send_button);
        LinearLayout input = fragmentView.findViewById(R.id.input);

        if (CommonMethods.isNightHere()) {
            msgsLayout.setBackgroundColor(Color.BLACK);
            message.setTextColor(Color.WHITE);
            sender.setBackgroundColor(getResources().getColor(R.color.night_accent));
            input.setBackgroundColor(R.color.night_accent);
        }

        msgsLayout.setDivider(null);
        msgsLayout.setDividerHeight(15);

        if (CommonMethods.checkIfGooglePlayServicesAvailable(MyApp.getContext())) {
            CommonMethods.createAlertDialog(new AlertDialog.Builder(MyApp.getContext()),
                    null,
                    getResources().getString(R.string.warning),
                    getResources().getString(R.string.gms_not_found),
                    getResources().getString(R.string.no),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (getFragmentManager() != null) {
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        }
                    },
                    null,
                    null,
                    getResources().getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(getActivity());
                        }
                    },
                    false,
                    null).show();
        } else {
            if (FirebaseAuth.getInstance().getCurrentUser() == null){
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), REQUEST_CODE);
            } else {
                Toast.makeText(MyApp.getContext(), R.string.you_are_authorized, Toast.LENGTH_LONG).show();
                startMessaging();
            }
        }

        //listener для кнопки сообщений
        sender.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View view) {
                if (!message.getText().toString().equals("")) {
                    FirebaseDatabase.getInstance().getReference().push()
                            .setValue(new MessageInConversation(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                                    message.getText().toString(),
                                    new SimpleDateFormat("dd-MM-yyyy hh:mm").format(new Date())));
                    message.setText("");
                }
            }
        });

        msgsLayout.setAdapter(adapter);
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    public void onResume(){
        super.onResume();
        if (CommonMethods.checkIfGooglePlayServicesAvailable(MyApp.getContext())) {
            CommonMethods.createAlertDialog(new AlertDialog.Builder(MyApp.getContext()),
                    null,
                    getResources().getString(R.string.warning),
                    getResources().getString(R.string.gms_not_found),
                    getResources().getString(R.string.no),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    },
                    null,
                    null,
                    getResources().getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(getActivity());
                        }
                    },
                    false,
                    null).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getContext(), R.string.you_are_authorized, Toast.LENGTH_LONG).show();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_layout, new MessengerFragment());
            } else {
                Toast.makeText(getContext(), R.string.you_are_not_authorized, Toast.LENGTH_LONG).show();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
    }

    private void startMessaging(){
        FirebaseListOptions<MessageInConversation> options = new FirebaseListOptions.Builder<MessageInConversation>()
                .setQuery(FirebaseDatabase.getInstance().getReference(), MessageInConversation.class)
                .setLayout(R.layout.message_item).build();

        adapter = new FirebaseListAdapter<MessageInConversation>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull MessageInConversation model, int position){
                LinearLayout layout = v.findViewById(R.id.background);
                TextView username = v.findViewById(R.id.username);
                TextView text = v.findViewById(R.id.text);
                TextView date = v.findViewById(R.id.date);
                BubbleLayout bubble = v.findViewById(R.id.bubble);

                if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName().equals(model.getUsername())) {
                    layout.setGravity(Gravity.END);
                    bubble.setBubbleColor(getResources().getColor(R.color.bubble_mine));
                    bubble.setArrowDirection(ArrowDirection.RIGHT);
                } else {
                    layout.setGravity(Gravity.START);
                    bubble.setBubbleColor(getResources().getColor(R.color.bubble_not_mine));
                    bubble.setArrowDirection(ArrowDirection.LEFT);
                }

                bubble.setCornersRadius(40);

                username.setText(model.getUsername());
                text.setText(model.getText());
                date.setText(model.getDate());
            }
        };
    }
}
