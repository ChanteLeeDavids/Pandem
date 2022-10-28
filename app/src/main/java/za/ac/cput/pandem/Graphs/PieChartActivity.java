package za.ac.cput.pandem.Graphs;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import za.ac.cput.pandem.R;

public class PieChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        PieChart pieChart = findViewById(R.id.pieChart);

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(4004555,"TOTAL CASES",4004555));
        pieEntries.add(new PieEntry(4966,"ACTIVE CASES",4966));
        pieEntries.add(new PieEntry(3897607,"RECOVERIES",3897607));
        pieEntries.add(new PieEntry(101982,"TOTAL DEATHS",101982));

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "PAN DEM");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("PAN DEM");
        pieChart.animate();

    }
}