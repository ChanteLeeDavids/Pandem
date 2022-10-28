package za.ac.cput.pandem.Graphs;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import za.ac.cput.pandem.R;

public class MainActivity extends AppCompatActivity {

      EditText xYears, yInfections;
      Button btnBarChart, btnPieChart, btnRadarChart;
      FirebaseDatabase database;
      DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_graph);

        //xYears= (EditText) findViewById(R.id.x_Years);
        //yInfections=(EditText) findViewById(R.id.);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("ChartTable");

        btnBarChart = (Button)findViewById(R.id.buttonBarChart);
        btnPieChart = (Button)findViewById(R.id.buttonPieChart);
        btnRadarChart = (Button)findViewById(R.id.buttonRadarChart);

        //findViewById(R.id.buttonBarChart)
        btnBarChart.setOnClickListener(view -> {
//                     String id= reference.push().getKey();
//
//                     int x = Integer.parseInt(xYears.getText().toString());
//                     int y = Integer.parseInt(xYears.getText().toString());
//
//                     PointValue pointValue = new PointValue(x,y);
//                     reference.child(id).setValue(pointValue);
//            reference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot snapshot) {
//                    String value = snapshot.getValue(String.class);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError error) {
//
//                }
//            });
            startActivity(new Intent(getApplicationContext(), BarChartActivity.class));
        });

        //findViewById(R.id.buttonPieChart)
        btnPieChart.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), PieChartActivity.class)));

        //findViewById(R.id.buttonRadarChart)
        btnRadarChart.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), RadarChartActivity.class)));

    }
}