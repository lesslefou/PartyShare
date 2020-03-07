package lisa.duterte.partyshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private RecyclerView recyclerView;
    private FoodAdapter adapter;
    private ArrayList<Food> food;
    private String nameActivity;
    private Integer update;
    private Button validateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_choice);


        nameActivity = Objects.requireNonNull(getIntent().getExtras()).getString("NAME_ACTIVITY", "ERROR");
        Log.d("DrinkChoice", "name_activity récupéré" + nameActivity);
        update = Objects.requireNonNull(getIntent().getExtras()).getInt("UPDATE", -1);
        Log.d("DrinkChoice", "update récupéré" + update);


        initImageBitmaps();

        validateBtn = findViewById(R.id.validateBtn);
        validateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //Permettre la sauvegarde


            /*for (int i=0; i < getItemCount() ; i++){

             }*/
            }
        });
    }

    private void initImageBitmaps(){
        Log.d(TAG,"initImageBitmaps: preparing bitmaps");
        food = new ArrayList<Food>();


        Food socca = new Food(
                R.string.socca_chips,
                R.drawable.socca_chips_logo,
                1 // Recupère quantity
        );
        Food paysanne = new Food(
                R.string.paysanne_chips,
                R.drawable.paysanne_chips_logo,
                3 // Recupère quantity
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
        recyclerView = findViewById(R.id.recycler_view_drinks);
        adapter = new FoodAdapter(FoodChoice.this,R.layout.activity_drink_adapter,food,nameActivity);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(FoodChoice.this));
    }
}
