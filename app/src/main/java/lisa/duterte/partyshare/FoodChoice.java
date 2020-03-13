package lisa.duterte.partyshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Objects;

public class FoodChoice extends AppCompatActivity {

    private static final String TAG = "FoodChoice";

    private ArrayList<String> mImageNames = new ArrayList<>(), mQuantity = new ArrayList<>();
    private ArrayList<Integer> mImages = new ArrayList<>();
    private ArrayList<Food> food;
    private String nameActivity;
    private Integer update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_choice);


        nameActivity = Objects.requireNonNull(getIntent().getExtras()).getString("NAME_ACTIVITY", "ERROR");
        Log.d(TAG, "name_activity récupéré" + nameActivity);
        update = Objects.requireNonNull(getIntent().getExtras()).getInt("UPDATE", -1);
        Log.d(TAG, "update récupéré" + update);


        initImageBitmaps();

        Button validateBtn = findViewById(R.id.validateBtn);


        validateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                0 // Recupère quantity
        );
        Food paysanne = new Food(
                R.string.paysanne_chips,
                R.drawable.paysanne_chips_logo,
                0 // Recupère quantity
        );
        Food barbec = new Food(
                R.string.barbuc_chips,
                R.drawable.barbec_chips_logo,
                0 // Recupère quantity
        );
        Food saucisson = new Food(
                R.string.salami,
                R.drawable.saucisson_logo,
                0 // Recupère quantity
        );
        Food brownie = new Food(
                R.string.brownie,
                R.drawable.brownie_logo,
                0 // Recupère quantity
        );


        food.add(socca);
        food.add(paysanne);
        food.add(barbec);
        food.add(saucisson);
        food.add(brownie);

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
