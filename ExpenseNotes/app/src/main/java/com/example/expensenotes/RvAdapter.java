package com.example.expensenotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RvAdapter extends RecyclerView.Adapter<RvHolder> {
    private Context context;
    private ArrayList<Expense> expenseList;

    public RvAdapter(Context context, ArrayList<Expense> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public RvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View transView = inflater.inflate(R.layout.text_row_item, parent, false);
        return new RvHolder(transView);
    }

    @Override
    public void onBindViewHolder(@NonNull RvHolder holder, int position) {
        Expense expense = expenseList.get(position);
        String line = String.format("%-8s%-10s$ %-12s%s", expense.getDate(), expense.getTime(), expense.getAmount(), expense.getCategory());
        holder.transRow.setText(line);
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }
}
