package za.ac.cput.pandem.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import za.ac.cput.pandem.HomeScreen;
import za.ac.cput.pandem.MainActivity;
import za.ac.cput.pandem.R;
import za.ac.cput.pandem.RegisterScreen;

public class LoginScreen extends AppCompatActivity {

    Button signUp, loginBtn;
    ImageView logo;
    TextView welcome, signin_txt;
    TextInputLayout username;
    TextInputLayout password;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://pandem-8e8ab-default-rtdb.firebaseio.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login_screen);

        signUp = findViewById(R.id.signupBtn);
        loginBtn = findViewById(R.id.loginBtn);
        logo = findViewById(R.id.logoImage);
        welcome = findViewById(R.id.welcome);
        signin_txt = findViewById(R.id.signin_txt);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginScreen.this, RegisterScreen.class);

                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(logo, "logo_image");
                pairs[1] = new Pair<View, String>(welcome, "logo_txt");
                pairs[2] = new Pair<View, String>(signin_txt, "logo_desc");
                pairs[3] = new Pair<View, String>(username, "username_trans");
                pairs[4] = new Pair<View, String>(password, "password_trans");
                pairs[5] = new Pair<View, String>(loginBtn, "button_trans");
                pairs[6] = new Pair<View, String>(signUp, "login_signup_trans");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginScreen.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernametxt = username.getEditText().getText().toString();
                String passwordtxt = password.getEditText().getText().toString();
                String etKey = "1Hbfh667adfDEJ78";


                if(usernametxt.isEmpty() || passwordtxt.isEmpty()){
                    Toast.makeText(LoginScreen.this, "Please complete all fields", Toast.LENGTH_LONG).show();
                }
                else{
                    reference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String decPasstxt = "";

                            if(snapshot.hasChild(usernametxt)){
                                String getPassword = snapshot.child(usernametxt).child("password").getValue(String.class); //encrypted version

                                try{
                                    SecretKey secretKey= MainActivity.generateKey(etKey);
                                    decPasstxt=MainActivity.decryptMsg(getPassword,secretKey); //decrypted version

                                }catch(Exception e){
                                    e.printStackTrace();
                                }

                                if(decPasstxt.equals(passwordtxt)){
//                                    Toast.makeText(LoginScreen.this, "Logging in", Toast.LENGTH_LONG).show();

                                    startActivity(new Intent(LoginScreen.this, HomeScreen.class));
                                    finish();

                                }
                                else{
                                    Toast.makeText(LoginScreen.this, "Incorrect password", Toast.LENGTH_LONG).show();
                                }
                            }
                            else{
                                Toast.makeText(LoginScreen.this, "User does not exist", Toast.LENGTH_LONG).show();
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