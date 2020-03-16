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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NameChoiceActivity extends AppCompatActivity {
    private static final String TAG = "NameChoiceActivity";

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

        //Save the name of the activity and go to the next section of the creation of the activity
        next_btn = findViewById(R.id.nextBtn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameActivity = findViewById(R.id.edit_name);
                name = nameActivity.getText().toString();

                if (!name.matches("")) {
                    addActivity(name);
                    Intent i = new Intent(getBaseContext(), Create_Activity.class);
                    i.putExtra("NAME_ACTIVITY",name);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(NameChoiceActivity.this, R.string.name_activity_exist, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Go back to the previous page and close this activity
        back_btn = findViewById(R.id.backBtn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //Create the activity on the dataBase with all the necessary information
    private void addActivity(final String name) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            userId = firebaseUser.getUid();

            Log.d(TAG , "id user " + userId);
            mReference = FirebaseDatabase.getInstance().getReference("user").child(userId);
            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    pseudoUser = dataSnapshot.child("pseudo").getValue().toString();
                    Log.d(TAG , "onDataChange name user " + pseudoUser);
                    Activity activity = new Activity();

                    mReference = FirebaseDatabase.getInstance().getReference("Activities").child(name);
                    activity.setName(name);
                    activity.setLocation("");

                    ArrayList<String> nameContact = new ArrayList<>();
                    nameContact.add(pseudoUser);
                    activity.setFriends(nameContact);

                    activity.setDrinkChoice("");
                    activity.setFoodChoice("");
                    activity.setDate("");
                    mReference.setValue(activity);


                    String supplementText=" ";
                    DatabaseReference aReference = mReference.child("foodChoice");
                    Map<String, Object> foodUpdates = new HashMap<>();
                    foodUpdates.put("extra", supplementText);
                    aReference.updateChildren(foodUpdates);

                    DatabaseReference bReference = mReference.child("drinkChoice");
                    Map<String, Object> drinkUpdates = new HashMap<>();
                    drinkUpdates.put("extra", supplementText);
                    bReference.updateChildren(drinkUpdates);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            Log.d(TAG , "name user " + name);
        }
        else {
            Log.d(TAG , "name user " + "error");
        }


    }

}
