package com.mytutor.mytutorteacher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Button;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class RegisterAct extends AppCompatActivity {

    private static final int PICK_RESUME_CODE = 1000;
    private EditText ed_mail, ed_pass, ed_confirmPass, ed_Name, ed_AreaSpec, ed_PrefTime, ed_CostPerSess;
    private Button btn_register, btn_resume;
    private Spinner spinner; private String str_amOrPm;
    private String mail, pass, confPass, name, areaSpec, prefTime, costPerSess;
    private String urlForResume;
    private static final String COLLECTION_NAME = "teacher";

    private FirebaseFirestore db;
    private StorageReference storageReference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        declaration();

        ArrayList<String> amOrpm = new ArrayList<>();
        amOrpm.add("AM");
        amOrpm.add("PM");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,amOrpm);
        spinner.setAdapter(dataAdapter);
//        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0)
                    str_amOrPm = "AM";
                else
                    str_amOrPm = "PM";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_resume.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                mail = ed_mail.getText().toString();
                pass = ed_pass.getText().toString();
                confPass = ed_confirmPass.getText().toString();
                name = ed_Name.getText().toString();
                areaSpec = ed_AreaSpec.getText().toString();
                prefTime = ed_PrefTime.getText().toString();
                costPerSess = ed_CostPerSess.getText().toString();



                if (!(TextUtils.isEmpty(mail) &&
                        TextUtils.isEmpty(pass) &&
                        TextUtils.isEmpty(name))) {
                    if (mail.contains("@")) {
                        if (pass.equals(confPass)) {
                            storageReference = FirebaseStorage.getInstance().getReference(mail);
                            Intent intent = new Intent();
                            intent.setType("image/*|application/pdf|audio/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent,"Upload your resume"),PICK_RESUME_CODE);
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


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mail = ed_mail.getText().toString();
                pass = ed_pass.getText().toString();
                confPass = ed_confirmPass.getText().toString();
                name = ed_Name.getText().toString();
                areaSpec = ed_AreaSpec.getText().toString();
                prefTime = ed_PrefTime.getText().toString();
                costPerSess = ed_CostPerSess.getText().toString();


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_RESUME_CODE){
            UploadTask uploadTask = storageReference.putFile(data.getData());
            Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        Toast.makeText(RegisterAct.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        btn_resume.setText("RESUME UPLOADED");
                        urlForResume = task.getResult().toString();
                        Log.d("DIRECTLINK", "onComplete: " + urlForResume);
                    }
                }
            });
        }
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
        ed_AreaSpec = findViewById(R.id.ed_specArea);
        ed_PrefTime = findViewById(R.id.ed_prefTime);
        ed_CostPerSess = findViewById(R.id.ed_costPerSess);
        spinner = findViewById(R.id.spinner);
        btn_resume = findViewById(R.id.btn_resume);


        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }


    private void addData() {




    /*    db.collection(COLLECTION_NAME)
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
        finish();
*/
        auth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Map<String, Object> user = new HashMap<>();
                    user.put("uuid",auth.getUid());
                    user.put("Name", name);
                    user.put("Email", mail);
                    user.put("Password", pass);
                    user.put("Specialized Area",areaSpec);
                    user.put("Preferred Time",prefTime + " " + str_amOrPm);
                    user.put("Cost per session",costPerSess);
                    user.put("Rating",5.0f);
                    user.put("Wallet Amount", 0);
                    user.put("ResumeURL", urlForResume);
                    user.put("Already_Appointed",false);

                db.collection(COLLECTION_NAME)
                        .document(auth.getUid())
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(RegisterAct.this, DashboardActivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

                }
            }
        });

    }
}
