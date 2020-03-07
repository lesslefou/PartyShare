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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class NameChoiceActivity extends AppCompatActivity {

    DatabaseReference mReference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String name,userId,pseudoUser;
    Button next_btn, back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_choice);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        next_btn = findViewById(R.id.nextBtn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameActivity = findViewById(R.id.edit_name);
                name = nameActivity.getText().toString();

                if (!name.matches("")) {
                    //if (!checkIfNameExist(name)){
                        addActivity(name);
                        Intent i = new Intent(getBaseContext(), Create_Activity.class);
                        i.putExtra("NAME_ACTIVITY",name);
                        startActivity(i);
                       //Toast.makeText(NameChoiceActivity.this,"Activity Added",Toast.LENGTH_SHORT).show();
                    //}
                } else {
                    Toast.makeText(NameChoiceActivity.this, R.string.name_activity_exist, Toast.LENGTH_SHORT).show();
                }
            }
        });

        back_btn = findViewById(R.id.backBtn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addActivity(final String name) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            userId = firebaseUser.getUid();

            Log.d("NameChoiceActivity" , "id user " + userId);
            mReference = FirebaseDatabase.getInstance().getReference("user").child(userId);
            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    pseudoUser = dataSnapshot.child("pseudo").getValue().toString();
                    Log.d("NameChoiceActivity" , "onDataChange name user " + pseudoUser);
                    Activity activity = new Activity();

                    mReference = FirebaseDatabase.getInstance().getReference("Activities").child(name);
                    activity.setName(name);
                    activity.setLocation("");

                    ArrayList<String> nameContact = new ArrayList<>();
                    nameContact.add(pseudoUser);
                    activity.setFriends(nameContact);

                    mReference.setValue(activity);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            Log.d("NameChoiceActivity" , "name user " + name);
        }
        else {
            Log.d("NameChoiceActivity" , "name user " + "error");
        }


    }

}


//Prefetch Data //Extraire les data
//Likelihood = probabilité