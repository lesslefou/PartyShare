package lisa.duterte.partyshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Sign_Up extends AppCompatActivity implements View.OnClickListener {

    EditText editTextEmail, editTextPassword, editTextName, editTextSurname, editTextPseudo;

    boolean validPassword = false;
    boolean hasNumbers = false;
    boolean hasLowerCase = false;
    boolean hasUpperCase = false;

    FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    ProgressBar progressBar;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up);

        editTextName =findViewById(R.id.edit_name);
        editTextSurname =findViewById(R.id.edit_surname);
        editTextPseudo =findViewById(R.id.edit_pseudo);
        editTextEmail =findViewById(R.id.edit_email);
        editTextPassword =findViewById(R.id.edit_password);

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        progressBar = findViewById(R.id.progressBar);


        findViewById(R.id.btn_sign).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);

    }

    private void registerUser() {
        final String name = editTextName.getText().toString().trim();
        final String surname = editTextSurname.getText().toString().trim();
        final String pseudo = editTextPseudo.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if (name.isEmpty() ){
            editTextName.setError(getString(R.string.notName));
            editTextName.requestFocus();
        }
        if (surname.isEmpty() ){
            editTextSurname.setError(getString(R.string.notSurname));
            editTextSurname.requestFocus();
        }
        if (pseudo.isEmpty() ){
            editTextPseudo.setError(getString(R.string.notPseudo));
            editTextPseudo.requestFocus();
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError(getString(R.string.wrongEmail));
            editTextEmail.requestFocus();
        }

        validPassword = false;
        hasNumbers = false;
        hasLowerCase = false;
        hasUpperCase = false;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i))) {
                hasNumbers = true;
            } else if (Character.isLowerCase(password.charAt(i))) {
                hasLowerCase = true;
            } else if (Character.isUpperCase(password.charAt(i))) {
                hasUpperCase = true;
            }
        }
        if (hasNumbers && hasLowerCase && hasUpperCase && (password.length() >= 8)) {
            validPassword = true;
        }

        if (!validPassword) {
            editTextPassword.setError((getString(R.string.wrongPassword)));
            editTextPassword.requestFocus();
        }


        if (!name.isEmpty() || !surname.isEmpty()|| !pseudo.isEmpty() || !email.isEmpty() || !password.isEmpty()) {

            progressBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        FirebaseUser fuser = mAuth.getCurrentUser();
                        Objects.requireNonNull(fuser).sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Sign_Up.this,R.string.emailSent,Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Sign_Up", "onFailure: Email not sent" + e.getMessage());
                            }
                        });

                        ArrayList<String> arrayList = new ArrayList<>();
                        String userId = mAuth.getCurrentUser().getUid();
                        mReference = FirebaseDatabase.getInstance().getReference("user").child(userId);
                        user = new User();
                        user.setName(name);
                        user.setSurname(surname);
                        user.setPseudo(pseudo);
                        user.setEmail(email);
                        user.setContactList(arrayList);
                        mReference.setValue(user);

                        startActivity(new Intent(Sign_Up.this, Welcome.class));
                    } else {
                        Toast.makeText(Sign_Up.this, R.string.error + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
        else {
            Toast.makeText(Sign_Up.this,R.string.notInfo,Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign:
                registerUser();
                break;

            case R.id.btn_back:
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}
