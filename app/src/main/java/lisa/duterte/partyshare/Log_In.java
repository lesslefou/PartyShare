package lisa.duterte.partyshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//Reinitialiser un mdp
// https://www.youtube.com/watch?v=t8vUdt1eEzE&list=PLZocXbUzomEHma3HiBLW9RbOJhKaHgp70&index=3


public class Log_In extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log__in);


        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.edit_email);
        editTextPassword = findViewById(R.id.edit_password);

        progressBar = findViewById(R.id.progressBar);

        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),Welcome.class));
            finish();
        }

        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_log).setOnClickListener(this);
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError(getString(R.string.notEmail));
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError(getString(R.string.notPassword));
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Log_In.this, R.string.welcomeUser, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),Welcome.class));
                }else {
                    Toast.makeText(Log_In.this, R.string.error_log_in, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;

            case R.id.btn_log:
                userLogin();
                break;
        }
    }
}
