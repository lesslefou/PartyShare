package lisa.duterte.partyshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LocationChoice extends AppCompatActivity {
    String nameActivity,address,location;
    Integer update =0;
    TextView addressField;
    FirebaseAuth mAuth;
    DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_choice);
        nameActivity = Objects.requireNonNull(getIntent().getExtras()).getString("NAME_ACTIVITY", "ERROR");
        Log.d("LocationChoice", "name_activity récupéré" + nameActivity);
        update = Objects.requireNonNull(getIntent().getExtras()).getInt("UPDATE", -1);
        Log.d("LocationChoice", "update récupéré" + update);


        if (update == 1){
            locationActivityRecover(nameActivity);
        }

        Button validate_btn = findViewById(R.id.validateBtn);
        validate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressField = findViewById(R.id.adressField);
                address = addressField.getText().toString();
                insertAddress(address,nameActivity);
                Toast.makeText(LocationChoice.this,R.string.address_inserted,Toast.LENGTH_SHORT).show();


                if (update == 0){
                    finish();
                }
                else {
                    Intent i = new Intent(LocationChoice.this,View_Info_Activity.class);
                    i.putExtra("NAME_ACTIVITY",nameActivity);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    private void insertAddress(String address, String nameActivity) {

        Activity activity = new Activity();
        mReference = FirebaseDatabase.getInstance().getReference("Activities").child(nameActivity);

        Map<String, Object> activityUpdates = new HashMap<>();
        activityUpdates.put("location", address);
        mReference.updateChildren(activityUpdates);

    }



    private void locationActivityRecover(String nameActivity) {
        mReference = FirebaseDatabase.getInstance().getReference("Activities").child(nameActivity);
        addressField = findViewById(R.id.adressField);

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String address = dataSnapshot.child("location").getValue().toString();
                addressField.setText(address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
