package lisa.duterte.partyshare;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class DrinkChoice extends AppCompatActivity {
    private static final String TAG = "DrinkChoice";

    private ArrayList<Food> food;
    private String nameActivity,supplementText=" ";
    private Integer update;
    private EditText supplement;
    private DatabaseReference aReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_choice);


        nameActivity = Objects.requireNonNull(getIntent().getExtras()).getString("NAME_ACTIVITY", "ERROR");
        Log.d(TAG, "name_activity récupéré" + nameActivity);
        update = Objects.requireNonNull(getIntent().getExtras()).getInt("UPDATE", -1);
        Log.d(TAG, "update récupéré" + update);


        aReference = FirebaseDatabase.getInstance().getReference("Activities").child(nameActivity).child("drinkChoice");


        initImageBitmaps();

        // Recover and display the supplement information
        supplement = findViewById(R.id.supplement);
        if (update == 1) {
            aReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String extraFood = dataSnapshot.child("extra").getValue().toString();
                    Log.d(TAG,"date " + extraFood);
                    supplement.setText(extraFood);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        //Save the information on the database
        Button validateBtn = findViewById(R.id.validateBtn);
        validateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                supplementText = supplement.getText().toString();

                Map<String, Object> activityUpdates = new HashMap<>();
                activityUpdates.put("extra", supplementText);
                aReference.updateChildren(activityUpdates);

                if (update == 0){
                    finish();
                }
                else {
                    Intent i = new Intent(DrinkChoice.this,View_Info_Activity.class);
                    i.putExtra("NAME_ACTIVITY",nameActivity);
                    startActivity(i);
                    finish();
                }

            }
        });
    }

    //Initialise the name and the picture of the RecyclerView
    private void initImageBitmaps(){
        Log.d(TAG,"initImageBitmaps: preparing bitmaps");
        food = new ArrayList<Food>();


        Food coca = new Food(
                R.string.coca,
                R.drawable.coca_logo,
                0
        );
        Food iceTea = new Food(
                R.string.icetea,
                R.drawable.icetea_logo,
                0
        );
        Food beer = new Food(
                R.string.beer,
                R.drawable.beer_logo,
                0
        );
        Food sevenUp = new Food(
                R.string.sevenUp,
                R.drawable.seven_logo,
                0
        );
        Food sprite = new Food(
                R.string.sprite,
                R.drawable.sprite_logo,
                0

        );
        Food redBull = new Food(
                R.string.redbull,
                R.drawable.redbull_logo,
                0
        );
        Food limonade = new Food(
                R.string.limonade,
                R.drawable.limonade_logo,
                0
        );
        Food canada_dry = new Food(
                R.string.canada_dry,
                R.drawable.canada_dry_logo,
                0

        );
        Food schweppes = new Food(
                R.string.schwheppes,
                R.drawable.schweppes_logo,
                0
        );

        food.add(coca);
        food.add(iceTea);
        food.add(beer);
        food.add(redBull);
        food.add(sevenUp);
        food.add(sprite);
        food.add(limonade);
        food.add(canada_dry);
        food.add(schweppes);

        initRecycleView();
    }

    //Initialise and launch the recyclerview
    private  void initRecycleView(){
        Log.d(TAG,"initRecycleView: init recyclerview");
        RecyclerView recyclerView = findViewById(R.id.recycler_view_drinks);
        FoodAdapter adapter = new FoodAdapter(DrinkChoice.this, R.layout.activity_food_adapter, food, nameActivity, update, 1);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(DrinkChoice.this));
    }
}
