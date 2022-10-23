package za.ac.cput.pandem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import javax.crypto.SecretKey;
import za.ac.cput.pandem.Login.LoginScreen;

public class RegisterScreen extends AppCompatActivity {

    Button signIn, regUser;
    ImageView logo;
    TextView welcome, signup_txt;
    TextInputLayout regName, regUsername, regPhoneNo, regPassword;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://pandem-8e8ab-default-rtdb.firebaseio.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register_screen);

        signIn = findViewById(R.id.signinBtn);
        regUser = findViewById(R.id.regUserBtn);
        logo = findViewById(R.id.logoImage);
        welcome = findViewById(R.id.welcome);
        signup_txt = findViewById(R.id.signup_txt);
        regName = findViewById(R.id.regName);
        regUsername = findViewById(R.id.regUsername);
        regPhoneNo = findViewById(R.id.regPhoneNo);
        regPassword = findViewById(R.id.regPassword);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterScreen.this, LoginScreen.class);

                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(logo, "logo_image");
                pairs[1] = new Pair<View, String>(welcome, "logo_txt");
                pairs[2] = new Pair<View, String>(signup_txt, "logo_desc");
                pairs[3] = new Pair<View, String>(regUsername, "username_trans");
                pairs[4] = new Pair<View, String>(regPassword, "password_trans");
                pairs[5] = new Pair<View, String>(regUser, "button_trans");
                pairs[6] = new Pair<View, String>(signIn, "login_signup_trans");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterScreen.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });

        regUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nametxt = regName.getEditText().getText().toString();
                String usernametxt = regUsername.getEditText().getText().toString();
                String phonetxt = regPhoneNo.getEditText().getText().toString();
                String passtxt = regPassword.getEditText().getText().toString();
                String etKey = "1Hbfh667adfDEJ78";

                
                if(nametxt.isEmpty() || usernametxt.isEmpty() || phonetxt.isEmpty() || passtxt.isEmpty()){
                    Toast.makeText(RegisterScreen.this, "Complete all fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    
                    reference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String encPasstxt = "";
                            if(snapshot.hasChild(usernametxt)){
                                Toast.makeText(RegisterScreen.this, "A user with that username already exists", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                reference.child("users").child(usernametxt).child("name").setValue(nametxt);
                                reference.child("users").child(usernametxt).child("phone").setValue(phonetxt);

                                try{
                                    SecretKey secretKey=MainActivity.generateKey(etKey);
                                    encPasstxt=MainActivity.encryptMsg(passtxt,secretKey);
                                    reference.child("users").child(usernametxt).child("password").setValue(encPasstxt);
                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }

                                Toast.makeText(RegisterScreen.this, "Registered Successfully", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(RegisterScreen.this, LoginScreen.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}