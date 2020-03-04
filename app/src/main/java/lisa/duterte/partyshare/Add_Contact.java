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
import java.util.HashMap;
import java.util.Map;

//Send Request invite
//https://www.youtube.com/watch?v=4M5pWsrdTS4&list=PLxefhmF0pcPmtdoud8f64EpgapkclCllj&index=34

public class Add_Contact extends AppCompatActivity {

    TextView friend;
    String pseudo ;
    Button backBtn, addBtn;
    DatabaseReference mReference;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth ;
    FirebaseUser fUser;
    String userId;
    Integer check=0;

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

                //Check si le user à entrer un pseudo
                if (!pseudo.matches("")) {
                   //check si le pseudo existe dans la bddUser + Si existe dans la ContactList du user


                    if (fUser != null) {
                        Log.d("Add_Contact", "user != null");


                        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.d("Add_Contact", "onDataChange");
                                User user = new User();

                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    Log.d("Add_Contact", "checkIfUserExist: datasnapshot " + ds);
                                    user.setPseudo(ds.getValue(User.class).getPseudo());


                                    //Check si le user correspondant au pseudo existe vraiment
                                    if (user.getPseudo().equals(pseudo)) {
                                        Log.d("Add_Contact", "Pseudo " + user.getPseudo() + " exist");
                                        check = 1;
                                        break;
                                    }
                                }

                                if (check == 0){
                                    Toast.makeText(Add_Contact.this, R.string.not_found_pseudo, Toast.LENGTH_SHORT).show();
                                    Log.d("Add_Contact", "Pseudo not found on the dataBase");
                                }
                                else {
                                    Log.d("Add_Contact","Pseudo found on the database -> go check the rest");

                                    //Check si le pseudo est différent de celui du user
                                    String pseudoActifUser = dataSnapshot.child(userId).child("pseudo").getValue().toString();

                                    Log.d("Add_Contact","Own Pseudo : " + pseudoActifUser + " vs " + pseudo);
                                    if (pseudoActifUser.equals(pseudo)) {
                                        Log.d("Add_Contact", "Pseudo = actual user. Stop the add");
                                        Toast.makeText(Add_Contact.this, R.string.pseudoEqual, Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Log.d("Add_Contact", "Pseudo != actual user. Check if user already have this user");

                                        //Check if the user already has this contact


                                            /*Map<String, Object> contactListUpdates = new HashMap<>();
                                            user.getContactList().add(pseudo);
                                            contactListUpdates.put(userId+"/contactList",user.getContactList());
                                            mReference.updateChildren(contactListUpdates);
*/


                                        /*ArrayList<String> contactList = user.getContactList();
                                        contactList.add(pseudo);
                                        user.setContactList(contactList);
                                        mReference.setValue(user);*/


                                        final ArrayList<String> cL = new ArrayList<>();
                                        Query post = mReference.child(userId).child("contactList");
                                        post.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot child : dataSnapshot.getChildren()) {
                                                    String p = child.getValue(String.class);
                                                    if (p != null ) {
                                                        cL.add(p);
                                                    }
                                                }

                                                cL.add(pseudo);
                                                mReference.child(userId).child("contactList").setValue(cL);
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
