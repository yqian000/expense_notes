package com.example.expensenotes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RvHolder extends RecyclerView.ViewHolder {
    TextView transRow;

    public RvHolder(@NonNull View itemView) {
        super(itemView);
        transRow = itemView.findViewById(R.id.row);
    }
}
