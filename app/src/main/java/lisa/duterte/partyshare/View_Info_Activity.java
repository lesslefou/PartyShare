package lisa.duterte.partyshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

public class View_Info_Activity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__info_);


        toolbar = findViewById(R.id.toolbarActivity);
        setSupportActionBar(toolbar);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        FriendFragment startFragment = new FriendFragment();

        transaction.add(R.id.view_activity_place,startFragment);
        transaction.commit();
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
            check = 1;
        }else if (item.getItemId() == R.id.foodToolBar){
            newFragment = new FoodFragment();
            check = 1;
        }else if (item.getItemId() == R.id.drinkToolBar){
            newFragment = new DrinkFragment();
            check = 1;
        }else if (item.getItemId() == R.id.locationToolBar){
            newFragment = new LocationFragment();
            check = 1;
        }else if (item.getItemId() == R.id.dateToolBar){
            newFragment = new DateFragment();
            check = 1;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.view_activity_place, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        if (check == 1 ) return true;
        else return super.onOptionsItemSelected(item);
    }
}
