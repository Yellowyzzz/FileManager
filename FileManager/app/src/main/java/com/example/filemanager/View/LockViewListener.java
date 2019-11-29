package com.example.filemanager.View;

import java.util.List;

public interface LockViewListener {

    String onStart();

    void onComplete(List<Integer> selectedDotNumberList);
}
