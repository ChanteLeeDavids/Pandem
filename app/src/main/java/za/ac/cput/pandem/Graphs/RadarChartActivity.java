package za.ac.cput.pandem.Graphs;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

import za.ac.cput.pandem.R;

public class RadarChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar_chart);

        RadarChart radarChart = findViewById(R.id.radarChart);

        ArrayList<RadarEntry> radarEntries = new ArrayList<>();
        radarEntries.add(new RadarEntry(4004555,  "4004555"));
        radarEntries.add(new RadarEntry(3897607, "3897607"));
        radarEntries.add(new RadarEntry(4966, "4966"));
        radarEntries.add(new RadarEntry(101982, "101982"));

        RadarDataSet radarDataSet = new RadarDataSet(radarEntries, "TOTAL/ACTIVE CASES");
        radarDataSet.setColor(Color.RED);
        radarDataSet.setLineWidth(2f);
        radarDataSet.setValueTextColor(Color.RED);
        radarDataSet.setValueTextSize(14f);

        RadarData radarData = new RadarData();
        radarData.addDataSet(radarDataSet);

        String[] labels = {"TOTAL CASES","ACTIVE CASES", "RECOVERIES", "TOTAL DEATHS" };

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        radarChart.getDescription().setText("PAN DEM");
        radarChart.setData(radarData);
    }
}