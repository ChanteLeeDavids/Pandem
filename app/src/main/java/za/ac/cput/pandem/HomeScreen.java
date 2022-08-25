package za.ac.cput.pandem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import za.ac.cput.pandem.R;

public class HomeScreen extends AppCompatActivity {

    RelativeLayout mapBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        mapBtn =  findViewById(R.id.map_icon_layout);

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(HomeScreen.this, );
//                startActivity(intent);
            }
        });

    }
}