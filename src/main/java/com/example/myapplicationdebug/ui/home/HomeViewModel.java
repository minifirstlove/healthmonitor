package com.example.myapplicationdebug.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<String> mHealthAdvice;  // 增加一个存储健康建议的LiveData

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("T2");

        mHealthAdvice = new MutableLiveData<>();
        mHealthAdvice.setValue("Initial Health Advice"); // 设置一个初始值或根据需要加载具体内容
    }

    public LiveData<String> getHealthAdvice() {
        return mHealthAdvice;  // 现在返回一个已初始化的LiveData对象
    }

    public LiveData<String> getText() {
        return mText;
    }
}
