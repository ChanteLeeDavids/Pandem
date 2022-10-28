package za.ac.cput.pandem.Graphs;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import za.ac.cput.pandem.R;

public class BarChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        BarChart barChart = findViewById(R.id.barChart);

        ArrayList<BarEntry> visitors = new ArrayList<>();
        visitors.add(new BarEntry(2018, 4004555, "TOTAL CASES"));
        visitors.add(new BarEntry(2019, 4966, "RECOVERIES"));
        visitors.add(new BarEntry(2020, 3897607, "TOTAL DEATHS"));
        visitors.add(new BarEntry(2021, 101982, "ACTIVE CASES"));


        BarDataSet barDataSet = new BarDataSet(visitors, "");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData();
        barData.addDataSet(barDataSet);

        String[] labels = {"TOTAL CASES", "RECOVERIES", "TOTAL DEATHS", "ACTIVE CASES"};

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        barChart.setFitBars(true);
        barChart.getDescription().setText("PAN DEM");
        barChart.setData(barData);
        barChart.animateY(2000);

    }
}