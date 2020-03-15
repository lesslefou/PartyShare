package lisa.duterte.partyshare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;


public class ConversationFragment extends Fragment {
    private static final String TAG = "ConversationFragment";

    private Button sendBtn,backBtn;
    private EditText typeText;
    private TextView chat_conversation;
    private String userId,user_name,activityName,date;
    private DatabaseReference mReference ;
    private String temp_key;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_conversation, container, false);

        backBtn = v.findViewById(R.id.back_activity_btn);
        sendBtn = v.findViewById(R.id.btn_send);
        typeText = v.findViewById(R.id.textSend);
        chat_conversation = v.findViewById(R.id.textView);

        final Bundle b = getArguments();
        activityName = Objects.requireNonNull(b).getString("NAME_ACTIVITY","ERROR");
        Log.d(TAG, "nameActivity " + activityName);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment nextFragment = new LocationFragment();
                Bundle data = new Bundle();
                data.putString("NAME_ACTIVITY",activityName);
                nextFragment.setArguments(data);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.view_activity_place, nextFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });



        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            userId = firebaseUser.getUid();

            Log.d(TAG, "id user " + userId);
            mReference = FirebaseDatabase.getInstance().getReference("user").child(userId);
            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    user_name = dataSnapshot.child("name").getValue().toString();
                    Log.d(TAG, "onDataChange name user " + user_name);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        mReference = FirebaseDatabase.getInstance().getReference("Activities").child(activityName).child("Conversation");

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date d = new Date();
                SimpleDateFormat f = new SimpleDateFormat("MM/dd | HH:mm");
                date = f.format(d);

                Map<String,Object> map = new HashMap<String, Object>();
                temp_key = mReference.push().getKey();
                mReference.updateChildren(map);

                DatabaseReference message_root = mReference.child(temp_key);
                Map<String,Object> map2 = new HashMap<String, Object>();
                map2.put("name",user_name);
                map2.put("msg",typeText.getText().toString());
                map2.put("date",date);

                typeText.getText().clear();

                message_root.updateChildren(map2);

            }

        });



        mReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return v;

    }



    private String chat_msg,chat_user_name,date_msg;

    private void append_chat_conversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()){

            date_msg = (String) ((DataSnapshot)i.next()).getValue();
            chat_msg = (String) ((DataSnapshot)i.next()).getValue();
            chat_user_name = (String) ((DataSnapshot)i.next()).getValue();

            chat_conversation.append(chat_user_name + " (" + date_msg + ") : "+chat_msg +" \n");
        }


    }


}
