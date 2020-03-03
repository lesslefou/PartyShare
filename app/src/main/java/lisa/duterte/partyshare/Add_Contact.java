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
import com.google.firebase.database.ValueEventListener;

//Send Request invite
//https://www.youtube.com/watch?v=4M5pWsrdTS4&list=PLxefhmF0pcPmtdoud8f64EpgapkclCllj&index=34

public class Add_Contact extends AppCompatActivity {

    TextView friend;
    String pseudo;
    Button backBtn, addBtn;
    DatabaseReference mReference;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth ;
    FirebaseUser user;
    String userId, nameUSer;
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

                //Check si le user à entrer un pseudo
                if (!pseudo.matches("")) {
                   //check si le pseudo existe dans la bddUser + Si existe dans la ContactList du user

                    mAuth = FirebaseAuth.getInstance();
                    mDatabase = FirebaseDatabase.getInstance();
                    mReference = mDatabase.getReference("user");
                    user = mAuth.getCurrentUser();

                    if (user != null) {
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

                                        //Check si le nom est différent de celui du user
                                        if (!compareName(user.getPseudo())) {
                                            //Check si le user possède déjà le pseudo



                                        } else {
                                            Toast.makeText(Add_Contact.this,"You can't added your own pseudo",Toast.LENGTH_SHORT).show();
                                        }



                                    } else {
                                        Toast.makeText(Add_Contact.this, R.string.not_found_pseudo, Toast.LENGTH_SHORT).show();
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

        /*
        if (isInserted)
            Toast.makeText(Add_Contact.this, R.string.contact_inserted, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(Add_Contact.this, R.string.already_pseudo, Toast.LENGTH_SHORT).show();

         */
    }

    public boolean compareName(final String contact) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            userId = firebaseUser.getUid();

            mReference = FirebaseDatabase.getInstance().getReference("user").child(userId);
            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    if (name.equals(contact)) {
                        check = 1;
                    }
                    else {
                        check = 0;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        if (check == 1) {
            return true;
        }
        else {
            return false;
        }
    }


}
