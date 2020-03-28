package com.mytutor.mytutorteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class RegisterAct extends AppCompatActivity {

    private EditText ed_mail, ed_pass, ed_confirmPass, ed_Name;
    private Button btn_register;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private String mail, pass, confPass, name;
    private static final String COLLECTION_NAME = "teacher";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        declaration();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mail = ed_mail.getText().toString();
                pass = ed_pass.getText().toString();
                confPass = ed_confirmPass.getText().toString();
                name = ed_Name.getText().toString();

//                Toast.makeText(RegisterAct.this, "mail : " + mail, Toast.LENGTH_SHORT).show();

                if (!(TextUtils.isEmpty(mail) &&
                        TextUtils.isEmpty(pass) &&
                        TextUtils.isEmpty(name))) {
                    if (mail.contains("@")) {
                        if (pass.equals(confPass)) {
                            MailExists(mail);
                        } else {
                            Toast.makeText(RegisterAct.this, "Password Does not match!", Toast.LENGTH_SHORT).show();
                        }
                    } else
                        Toast.makeText(RegisterAct.this, "Incorrect Mail ID!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterAct.this, "Kindly fill the details", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void MailExists(final String pmail){

        db.collection(COLLECTION_NAME)
                .whereEqualTo("Email",pmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int found = 0;
                        try{
                            for(QueryDocumentSnapshot document : task.getResult()){
                                if(pmail.equals(document.get("Email"))){
//                                Toast.makeText(RegisterAct.this, "pmail : " + pmail+" doc : " + document, Toast.LENGTH_SHORT).show();
                                    found=1;
                                }
                            }
                        }catch(NullPointerException e){
                            Toast.makeText(RegisterAct.this, "No Account!", Toast.LENGTH_SHORT).show();
                        }
                        if(found==0) addData();
                        else Toast.makeText(RegisterAct.this, "E-Mail Already Registered!", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void declaration() {
        ed_mail = findViewById(R.id.ed_email);
        ed_pass = findViewById(R.id.ed_pass);
        ed_Name = findViewById(R.id.ed_name);
        ed_confirmPass = findViewById(R.id.ed_confPass);
        btn_register = findViewById(R.id.btn_register);


        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection(COLLECTION_NAME);
    }

    private void addData() {

        Map<String, Object> user = new HashMap<>();
        user.put("Email", mail);
        user.put("Name", name);
        user.put("Password", pass);

        db.collection(COLLECTION_NAME)
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(RegisterAct.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
//                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterAct.this, "Registration failed!", Toast.LENGTH_SHORT).show();
            }
        });

//        finish();

    }
}
