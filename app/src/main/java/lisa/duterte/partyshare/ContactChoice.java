package lisa.duterte.partyshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Objects;

public class ContactChoice extends AppCompatActivity {

    private static final String TAG = "ContactChoice";

    EditText searchContact;
    Button validateBtn, addBtn;
    //Recuperer le nom de l'activity !!!!
    String pseudo,nameActivity ;
    DatabaseReference uReference;
    DatabaseReference mReference;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth ;
    FirebaseUser fUser;
    String userId;
    Integer check=0,already=0,update=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_choice);
        nameActivity = Objects.requireNonNull(getIntent().getExtras()).getString("NAME_ACTIVITY","Error");
        Log.d(TAG, "name récupéré " + nameActivity);
        update = Objects.requireNonNull(getIntent().getExtras()).getInt("UPDATE", -1);
        Log.d(TAG, "update récupéré" + update);



        searchContact = findViewById(R.id.searchText);
        addBtn = findViewById(R.id.checkLogo);
        validateBtn = findViewById(R.id.validateBtn);
        validateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (update == 0){
                    finish();
                }
                else {
                    Intent i = new Intent(ContactChoice.this,ViewActivity.class);
                    i.putExtra("NAME_ACTIVITY",nameActivity);
                    startActivity(i);

                }
            }
        });

        addContact();
    }

    private void addContact() {

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pseudo = searchContact.getText().toString();

                mAuth = FirebaseAuth.getInstance();
                mDatabase = FirebaseDatabase.getInstance();
                mReference = mDatabase.getReference("Activities");
                uReference = mDatabase.getReference("user");
                fUser = mAuth.getCurrentUser();
                userId = fUser.getUid();

                //Check if the field is not empty
                if (!pseudo.matches("")) {

                    //check if the pseudo exist on the bddUser + if the pseudo is or not already added on the activity
                    if (fUser != null) {
                        Log.d(TAG, "user != null");

                        uReference.addListenerForSingleValueEvent(new ValueEventListener() {
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
                                    Toast.makeText(ContactChoice.this, R.string.not_found_pseudo, Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "Pseudo not found on the dataBase");
                                }
                                else {
                                    Log.d(TAG,"Pseudo found on the database -> go check the rest");

                                    //if pseudo not equal to the pseudo of the Actual User
                                    String pseudoActifUser = dataSnapshot.child(userId).child("pseudo").getValue().toString();

                                    Log.d(TAG,"Own Pseudo : " + pseudoActifUser + " vs pseudo to add" + pseudo);
                                    if (pseudoActifUser.equals(pseudo)) {
                                        Log.d(TAG, "Pseudo = actual user. Stop to add");
                                        Toast.makeText(ContactChoice.this, R.string.pseudoEqual, Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Log.d(TAG, "Pseudo != actual user. Check if the activity already has this user");

                                        //Check if the activity already has this contact and add it or not
                                        final ArrayList<String> cL = new ArrayList<>();
                                        Query post = mReference.child(nameActivity).child("friends");
                                        post.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot child : dataSnapshot.getChildren()) {
                                                    String p = child.getValue(String.class);
                                                    if (p != null ) {
                                                        //check if the contact is or not already on his friendList
                                                        if (p.equals(pseudo)){
                                                            already = 1;
                                                            break;
                                                        }
                                                        cL.add(p);
                                                    }
                                                }

                                                //Activity don't already has the contact -> we can add it
                                                if (already == 0){
                                                    cL.add(pseudo);
                                                    mReference.child(nameActivity).child("friends").setValue(cL);
                                                    Toast.makeText(ContactChoice.this,R.string.contact_inserted,Toast.LENGTH_SHORT).show();

                                                }
                                                else {
                                                    Toast.makeText(ContactChoice.this,R.string.alreadyContact,Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ContactChoice.this,R.string.fill_pseudo_field,Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


}
