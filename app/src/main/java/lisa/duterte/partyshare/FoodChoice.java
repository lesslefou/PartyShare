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

public class FoodChoice extends AppCompatActivity {

    private static final String TAG = "FoodChoice";

    private ArrayList<String> mImageNames = new ArrayList<>(), mQuantity = new ArrayList<>();
    private ArrayList<Integer> mImages = new ArrayList<>();
    private ArrayList<Food> food;
    private String nameActivity,supplementText=" ";
    private Integer update;
    private EditText supplement;
    private DatabaseReference aReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_choice);

        nameActivity = Objects.requireNonNull(getIntent().getExtras()).getString("NAME_ACTIVITY", "ERROR");
        Log.d(TAG, "name_activity récupéré" + nameActivity);
        update = Objects.requireNonNull(getIntent().getExtras()).getInt("UPDATE", -1);
        Log.d(TAG, "update récupéré" + update);

        aReference = FirebaseDatabase.getInstance().getReference("Activities").child(nameActivity).child("foodChoice");

        initImageBitmaps();

        Button validateBtn = findViewById(R.id.validateBtn);

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

        validateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supplementText = supplement.getText().toString();

                Map<String, Object> activityUpdates = new HashMap<>();
                activityUpdates.put("extra", supplementText);
                aReference.updateChildren(activityUpdates);

                if (update == 0){
                    Intent i = new Intent(FoodChoice.this,Create_Activity.class);
                    i.putExtra("NAME_ACTIVITY",nameActivity);
                    startActivity(i);
                    finish();
                }

                else {
                    finish();
                }

            }
        });


    }

    private void initImageBitmaps(){
        Log.d(TAG,"initImageBitmaps: preparing bitmaps");
        food = new ArrayList<Food>();


        Food socca = new Food(
                R.string.socca_chips,
                R.drawable.socca_chips_logo,
                0
        );
        Food paysanne = new Food(
                R.string.paysanne_chips,
                R.drawable.paysanne_chips_logo,
                0
        );
        Food barbec = new Food(
                R.string.barbuc_chips,
                R.drawable.barbec_chips_logo,
                0
        );
        Food saucisson = new Food(
                R.string.salami,
                R.drawable.saucisson_logo,
                0
        );
        Food pizza = new Food(
                R.string.pizza,
                R.drawable.pizza_logo,
                0
        );
        Food poulet = new Food(
                R.string.poulet,
                R.drawable.manchons_de_poulet_logo,
                0
        );
        Food brownie = new Food(
                R.string.brownie,
                R.drawable.brownie_logo,
                0
        );
        Food bonbon = new Food(
                R.string.bonbon,
                R.drawable.bonbon_logo,
                0
        );



        food.add(socca);
        food.add(paysanne);
        food.add(barbec);
        food.add(pizza);
        food.add(poulet);
        food.add(saucisson);
        food.add(brownie);
        food.add(bonbon);

        initRecycleView();
    }

    private  void initRecycleView(){
        Log.d(TAG,"initRecycleView: init recyclerview");
        RecyclerView recyclerView = findViewById(R.id.recycler_view_drinks);
        Integer foodActivity = 0;
        FoodAdapter adapter = new FoodAdapter(FoodChoice.this, R.layout.activity_food_adapter, food, nameActivity, update, 0);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(FoodChoice.this));
    }
}
