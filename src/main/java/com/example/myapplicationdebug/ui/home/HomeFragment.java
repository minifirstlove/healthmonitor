package com.example.myapplicationdebug.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import com.example.myapplicationdebug.R;
import com.github.mikephil.charting.data.Entry;
import android.graphics.Color; // 导入Color类
import com.github.mikephil.charting.components.XAxis; // 导入XAxis类
import com.github.mikephil.charting.data.LineData; // 导入LineData类
import com.github.mikephil.charting.data.LineDataSet; // 导入LineDataSet
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.github.mikephil.charting.charts.LineChart;
import com.example.myapplicationdebug.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        // 设置健康建议的固定标题
        final TextView healthAdviceTitleTextView = binding.tvHealthAdviceTitle;
        healthAdviceTitleTextView.setText("健康建议标题"); // 这是固定的

        // 动态设置健康建议的内容
        final TextView healthAdviceTextView = binding.healthAdviceTextView;
        // 假设 homeViewModel 有一个 LiveData<String> 属性叫做 healthAdvice
        homeViewModel.getHealthAdvice().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String advice) {
                healthAdviceTextView.setText(advice); // 动态更新内容
            }
        });
        final TextView textView = binding.healthAdviceTextView;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        // 初始化健康数据列表
        List<HealthEntry> healthEntries = new ArrayList<>();
        healthEntries.add(new HealthEntry("2024-04-20", 5000, 80, 8.5f));
        healthEntries.add(new HealthEntry("2024-04-21", 6000, 75, 7.5f));

        // 找到 LineChart 组件
        LineChart healthDataChart = rootView.findViewById(R.id.healthDataChart);

        // 创建一个数据集合，用于存储步数、心率和睡眠质量数据
        List<Entry> stepEntries = new ArrayList<>();
        List<Entry> heartRateEntries = new ArrayList<>();
        List<Entry> sleepQualityEntries = new ArrayList<>();
        for (int i = 0; i < healthEntries.size(); i++) {
            HealthEntry entry = healthEntries.get(i);
            stepEntries.add(new Entry(i, entry.getSteps()));
            heartRateEntries.add(new Entry(i, entry.getHeartRate()));
            sleepQualityEntries.add(new Entry(i, entry.getSleepQuality()));
        }

        // 创建数据集并为其设置数据
        LineDataSet stepDataSet = new LineDataSet(stepEntries, "步数");
        stepDataSet.setColor(Color.BLUE);
        LineDataSet heartRateDataSet = new LineDataSet(heartRateEntries, "心率");
        heartRateDataSet.setColor(Color.RED);
        LineDataSet sleepQualityDataSet = new LineDataSet(sleepQualityEntries, "睡眠质量");
        sleepQualityDataSet.setColor(Color.GREEN);

        // 将数据集合添加到 LineData 中
        LineData lineData = new LineData(stepDataSet, heartRateDataSet, sleepQualityDataSet);
        healthDataChart.setData(lineData);

// 样式设置
        healthDataChart.getDescription().setEnabled(false);
        healthDataChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        healthDataChart.getAxisRight().setEnabled(false);

// 刷新图表
        healthDataChart.invalidate();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}