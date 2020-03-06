package lisa.duterte.partyshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//Send Request invite
//https://www.youtube.com/watch?v=4M5pWsrdTS4&list=PLxefhmF0pcPmtdoud8f64EpgapkclCllj&index=34

public class Add_Contact extends AppCompatActivity {
    private static final String TAG = "Add_Contact";

    TextView friend;
    String pseudo ;
    Button backBtn, addBtn;
    DatabaseReference mReference;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth ;
    FirebaseUser fUser;
    String userId;
    Integer check=0,already=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__contact);

        friend = findViewById(R.id.editText_pseudo);
        addBtn = findViewById(R.id.btn_add);
        backBtn = findViewById(R.id.btn_back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addContact();
    }

    private void addContact() {

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pseudo = friend.getText().toString();

                mAuth = FirebaseAuth.getInstance();
                mDatabase = FirebaseDatabase.getInstance();
                mReference = mDatabase.getReference("user");
                fUser = mAuth.getCurrentUser();
                userId = fUser.getUid();

                //Check if the field is not empty
                if (!pseudo.matches("")) {

                   //check if the pseudo exist on the bddUser + if the user has or has not the contact
                    if (fUser != null) {
                        Log.d(TAG, "user != null");

                        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.d(TAG, "onDataChange");
                                User user = new User();

                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    Log.d(TAG, "checkIfUserExist: datasnapshot " + ds);
                                    user.setPseudo(ds.getValue(User.class).getPseudo());


                                    //Check if the user (pseudo) exist
                                    if (user.getPseudo().equals(pseudo)) {
                                        Log.d(TAG, "Pseudo " + user.getPseudo() + " exist");
                                        check = 1;
                                        break;
                                    }
                                }

                                if (check == 0){
                                    Toast.makeText(Add_Contact.this, R.string.not_found_pseudo, Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "Pseudo not found on the dataBase");
                                }
                                else {
                                    Log.d(TAG,"Pseudo found on the database -> go check the rest");

                                    //if pseudo not equal to the pseudo of the Actual User
                                    String pseudoActifUser = dataSnapshot.child(userId).child("pseudo").getValue().toString();

                                    Log.d(TAG,"Own Pseudo : " + pseudoActifUser + " vs pseudo to add" + pseudo);
                                    if (pseudoActifUser.equals(pseudo)) {
                                        Log.d(TAG, "Pseudo = actual user. Stop to add");
                                        Toast.makeText(Add_Contact.this, R.string.pseudoEqual, Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Log.d(TAG, "Pseudo != actual user. Check if user already has this user");

                                        //Check if the user already has this contact and add it or not
                                        final ArrayList<String> cL = new ArrayList<>();
                                        Query post = mReference.child(userId).child("contactList");
                                        post.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot child : dataSnapshot.getChildren()) {
                                                    String p = child.getValue(String.class);
                                                    if (p != null ) {
                                                        //check if the contact is or not already on his contactList
                                                        if (p.equals(pseudo)){
                                                            already = 1;
                                                            break;
                                                        }
                                                        cL.add(p);
                                                    }
                                                }

                                                //User don't already has the contact -> we can add it
                                                if (already == 0){
                                                    cL.add(pseudo);
                                                    mReference.child(userId).child("contactList").setValue(cL);
                                                    Toast.makeText(Add_Contact.this,R.string.contact_inserted,Toast.LENGTH_SHORT).show();

                                                }
                                                else {
                                                    Toast.makeText(Add_Contact.this,R.string.alreadyContact,Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                    }


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }
                else{
                    Toast.makeText(Add_Contact.this,R.string.fill_pseudo_field,Toast.LENGTH_SHORT).show();
                }
            }
        });


    }



}
