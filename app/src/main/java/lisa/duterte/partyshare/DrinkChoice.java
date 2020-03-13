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


public class DrinkChoice extends AppCompatActivity {
    private static final String TAG = "DrinkChoice";

    private ArrayList<String> mImageNames = new ArrayList<>(), mQuantity = new ArrayList<>();
    private ArrayList<Integer> mImages = new ArrayList<>();
    private ArrayList<Food> food;
    private String nameActivity;
    private Integer update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_choice);


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
                    Intent i = new Intent(DrinkChoice.this,Create_Activity.class);
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

    private  void initRecycleView(){
        Log.d(TAG,"initRecycleView: init recyclerview");
        RecyclerView recyclerView = findViewById(R.id.recycler_view_drinks);
        FoodAdapter adapter = new FoodAdapter(DrinkChoice.this, R.layout.activity_food_adapter, food, nameActivity, update, 1);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(DrinkChoice.this));
    }
}
