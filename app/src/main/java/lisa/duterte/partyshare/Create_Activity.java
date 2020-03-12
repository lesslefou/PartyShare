
package lisa.duterte.partyshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class Create_Activity extends AppCompatActivity {

    String nameActivity;
    Button validateBtn;

    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<Integer> mImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_);
        nameActivity = Objects.requireNonNull(getIntent().getExtras()).getString("NAME_ACTIVITY","Error");
        Log.d("Create_Activity", "name récupéré " + nameActivity);

        TextView printNameField = findViewById(R.id.nameActivity);
        printNameField.setText(nameActivity);

        initCreateImage(nameActivity);

        validateBtn = findViewById(R.id.validateBtn);
        validateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Create_Activity.this, View_Info_Activity.class);
                i.putExtra("NAME_ACTIVITY",nameActivity);
                startActivity(i);
                finish();
            }
        });
    }

    private void initCreateImage(String nameActivity) {

        mImages.add(R.drawable.friends_logo);
        mImageNames.add(getString(R.string.friends));

        mImages.add(R.drawable.food_logo);
        mImageNames.add(getString(R.string.food));

        mImages.add(R.drawable.drink_logo);
        mImageNames.add(getString(R.string.drink));

        mImages.add(R.drawable.map_logo);
        mImageNames.add(getString(R.string.location));

        mImages.add(R.drawable.date_logo);
        mImageNames.add(getString(R.string.date));

        initCreateRecycleView(nameActivity);
    }

    private void initCreateRecycleView(String nameActivity) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_create);
        Create_Activity_Adapter adapterCreate = new Create_Activity_Adapter(mImageNames, mImages, this,nameActivity);
        recyclerView.setAdapter(adapterCreate);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
