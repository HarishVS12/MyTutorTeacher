package com.mytutor.mytutorteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    public static final String COLLECTION_NAME = "teacher";

    private EditText ed_email, ed_password;
    private Button btn_signIn, btn_register;
    private String mail,password;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        declaration();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterAct.class));
            }
        });

        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail = ed_email.getText().toString();
                password = ed_password.getText().toString();
                if(!(TextUtils.isEmpty(mail) || TextUtils.isEmpty(password)))
                    getData(mail, password);
                else
                    Toast.makeText(LoginActivity.this, "Fill the details", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void declaration(){
        ed_email = findViewById(R.id.ed_MailId);
        ed_password = findViewById(R.id.ed_Pass);
        btn_signIn = findViewById(R.id.btn_signIn);
        btn_register = findViewById(R.id.btn_register);

        db = FirebaseFirestore.getInstance();

    }

    public void getData(String pmail, String ppass){
        db.collection(COLLECTION_NAME)
                .whereEqualTo("Email",pmail)
                .whereEqualTo("Password",ppass)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d("TAG",document.getId() + " => " + document.getData());
                                Toast.makeText(LoginActivity.this,"Signed in Successfully!", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(LoginActivity.this, "Account does Not Exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Account does Not Exists", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
