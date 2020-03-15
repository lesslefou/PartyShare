package lisa.duterte.partyshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import java.util.Objects;

public class View_Info_Activity extends AppCompatActivity {
    private static final String TAG = "View_Info_Activity";

    Toolbar toolbar;
    String activityName;
    TextView activityTitle;
    Button back,update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__info_);

        activityName = Objects.requireNonNull(getIntent().getExtras()).getString("NAME_ACTIVITY");
        Log.d(TAG, "activity_name récupéré " + activityName);


        activityTitle = findViewById(R.id.titleActivity);
        activityTitle.setText(activityName);

        toolbar = findViewById(R.id.toolbarActivity);
        setSupportActionBar(toolbar);

        back = findViewById(R.id.backBtn);


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        LocationFragment startFragment = new LocationFragment();
        Bundle data = new Bundle();
        data.putString("NAME_ACTIVITY",activityName);
        startFragment.setArguments(data);
        transaction.add(R.id.view_activity_place,startFragment);
        transaction.commit();
        back.setVisibility(View.VISIBLE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viewactivity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Integer check=0;
        Fragment newFragment = new Fragment();

        if (item.getItemId() == R.id.friendToolBar) {
            newFragment = new FriendFragment();
            back.setVisibility(View.VISIBLE);
            check = 1;
        }else if (item.getItemId() == R.id.foodToolBar){
            newFragment = new FoodFragment();
            back.setVisibility(View.VISIBLE);
            check = 1;
        }else if (item.getItemId() == R.id.drinkToolBar){
            newFragment = new DrinkFragment();
            back.setVisibility(View.VISIBLE);
            check = 1;
        }else if (item.getItemId() == R.id.locationToolBar){
            newFragment = new LocationFragment();
            back.setVisibility(View.VISIBLE);
            check = 1;
        }else if (item.getItemId() == R.id.dateToolBar){
            newFragment = new DateFragment();
            back.setVisibility(View.VISIBLE);
            check = 1;
        }else if (item.getItemId() == R.id.messageToolBar){
            newFragment = new ConversationFragment();
            back.setVisibility(View.GONE);
            check = 1;
        }

        if (item.getItemId() == R.id.dateToolBar || item.getItemId() == R.id.locationToolBar || item.getItemId() == R.id.drinkToolBar
                    || item.getItemId() == R.id.foodToolBar || item.getItemId() == R.id.friendToolBar) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();
                }
            });
        }
        Bundle data = new Bundle();
        data.putString("NAME_ACTIVITY",activityName);
        newFragment.setArguments(data);
        getSupportFragmentManager().popBackStack();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.view_activity_place, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();


        if (check == 1 ) return true;
        else return super.onOptionsItemSelected(item);
    }
}
