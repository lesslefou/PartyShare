package lisa.duterte.partyshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DateChoice extends AppCompatActivity {
    private static final String TAG = "DateChoice";

    DatePicker datePicker;
    String nameActivity,date;
    Integer update =0;
    DatabaseReference mReference;
    TextView dateField;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_choice);

        nameActivity = Objects.requireNonNull(getIntent().getExtras()).getString("NAME_ACTIVITY", "Error");
        Log.d(TAG, "name récupéré " + nameActivity);
        update = Objects.requireNonNull(getIntent().getExtras()).getInt("UPDATE", -1);
        Log.d(TAG, "update récupéré" + update);


        if (update == 1){
            relativeLayout = findViewById(R.id.relative);
            relativeLayout.setVisibility(View.VISIBLE);

            dateActivityRecover(nameActivity);
        }

        Button validate_btn = findViewById(R.id.validateBtn);
        validate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker = findViewById(R.id.datePicker);

                date = datePicker.getDayOfMonth()+"/"+ (datePicker.getMonth() + 1) +"/"+datePicker.getYear();
                insertDate(date,nameActivity);
                Toast.makeText(DateChoice.this,R.string.date_inserted,Toast.LENGTH_SHORT).show();

                if (update == 0){
                    finish();
                }
                else {
                    Intent i = new Intent(DateChoice.this,ViewActivity.class);
                    i.putExtra("NAME_ACTIVITY",nameActivity);
                    startActivity(i);
                }
            }
        });
    }

    private void insertDate(String date, String nameActivity) {

        mReference = FirebaseDatabase.getInstance().getReference("Activities").child(nameActivity);

        Map<String, Object> activityUpdates = new HashMap<>();
        activityUpdates.put("date", date);
        mReference.updateChildren(activityUpdates);


    }



    private void dateActivityRecover(String nameActivity) {
        mReference = FirebaseDatabase.getInstance().getReference("Activities").child(nameActivity);
        dateField = findViewById(R.id.dateField);

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String address = dataSnapshot.child("date").getValue().toString();
                dateField.setText(address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
