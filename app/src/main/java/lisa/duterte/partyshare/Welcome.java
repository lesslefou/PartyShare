package lisa.duterte.partyshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Welcome extends AppCompatActivity {
    private static Welcome singleInstance;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore fStore;
    private String userId;
    private TextView verifyMail;
    private Button resendCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = mAuth.getCurrentUser().getUid();

        resendCode = findViewById(R.id.verifyBtn);
        verifyMail = findViewById(R.id.notVerified);

        user = mAuth.getCurrentUser();
        if (!user.isEmailVerified()) {
            resendCode.setVisibility(View.VISIBLE);
            verifyMail.setVisibility(View.VISIBLE);

            resendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Welcome.this, R.string.emailSent, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Welcome", "onFailure: Email not sent" + e.getMessage());
                        }
                    });
                }
            });
        }


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        StartFragment startFragment = new StartFragment();
        transaction.add(R.id.fragment_place, startFragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                return true;
            case R.id.setting:
                Intent i = new Intent(Welcome.this, Setting.class);
                startActivity(i);
                return true;
            case R.id.aboutUs:
                Intent i1 = new Intent(Welcome.this, AboutUs.class);
                startActivity(i1);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    public void onSelectFragment(View view) {
        Fragment newFragment = new Fragment();

        if (view == findViewById(R.id.activityBtn)) {
            newFragment = new ActivityFragment();

        } else if (view == findViewById(R.id.contactBtn)) {
            newFragment = new ContactFragment();
        } else if (view == findViewById(R.id.gameBtn)) {
            newFragment = new GameFragment();
        }

        getSupportFragmentManager().popBackStack();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_place, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }


}
