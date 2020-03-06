package lisa.duterte.partyshare;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import java.util.ArrayList;
import java.util.Objects;


public class DrinkChoice extends AppCompatActivity {

    private RecyclerView drinkRecycler;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    private Button validateBtn;
    private String nameActivity;
    private Integer check=0,update=0;
    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<Integer> mImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_choice);


        nameActivity = Objects.requireNonNull(getIntent().getExtras()).getString("NAME_ACTIVITY", "ERROR");
        Log.d("DrinkChoice", "name_activity récupéré" + nameActivity);
        update = Objects.requireNonNull(getIntent().getExtras()).getInt("UPDATE", -1);
        Log.d("DrinkChoice", "update récupéré" + update);


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
        initImage();

        }


        private void initImage() {

            mImages.add(R.drawable.coca_logo);
            mImageNames.add(getString(R.string.coca));

            mImages.add(R.drawable.icetea_logo);
            mImageNames.add(getString(R.string.icetea));

            mImages.add(R.drawable.beer_logo);
            mImageNames.add(getString(R.string.beer));

            mImages.add(R.drawable.seven_logo);
            mImageNames.add(getString(R.string.sevenUp));

            mImages.add(R.drawable.sprite_logo);
            mImageNames.add(getString(R.string.sprite));

            initRecycleView();
        }

        private void initRecycleView() {
            RecyclerView recyclerView = findViewById(R.id.recycler_view_drinks);
            DrinkAdapter adapterChoice = new DrinkAdapter(mImageNames, mImages, this,nameActivity);
            recyclerView.setAdapter(adapterChoice);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
}
