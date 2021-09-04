package com.example.expensenotes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    // references to buttons and other controls on the layout
    Button btn_submit;
    Button btn_report;
    EditText et_amount;
    Spinner spin;
    ArrayAdapter<CharSequence> adapterSpin;
    RecyclerView rv;
    RvAdapter rvAdapter;
    RecyclerView.LayoutManager layoutManager;
    DBHelper db;
    TextView tvTotal;
    TextView tvDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(MainActivity.this);
        btn_submit = findViewById(R.id.submitButton);
        btn_report = findViewById(R.id.reportButton);
        et_amount = findViewById(R.id.amountInput);
        spin = findViewById(R.id.category_spinner);
        rv = findViewById(R.id.recyclerView);
        tvTotal = findViewById(R.id.totalView);
        tvDate = findViewById(R.id.dateView);

        // set current date view
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date todayDate = new Date();
        String dateStr = formatter.format(todayDate);
        tvDate.setText(dateStr);

        // set today's total
        //int sum = records[Integer.parseInt(md[0])][Integer.parseInt(md[1])];
        double total = db.getTodayTotal(dateStr.substring(0, 5));
        tvTotal.setText(String.format("%.2f", total));

        // set spinner for category view
        adapterSpin = ArrayAdapter.createFromResource(MainActivity.this, R.array.category_array, android.R.layout.simple_spinner_item);
        adapterSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapterSpin);

        // set recyclerView for transactions
        layoutManager = new LinearLayoutManager(MainActivity.this);
        rvAdapter = new RvAdapter(MainActivity.this, db.getAll());
        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(rvAdapter);

        // button listeners for the submit button
        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm");
                Date todayDate = new Date();
                String[] dateArray = (formatter.format(todayDate)).split(" ");
                // add new data to database
                Expense expense = new Expense(dateArray[0], dateArray[1], Integer.parseInt(et_amount.getText().toString()), spin.getSelectedItem().toString());
                db = new DBHelper(MainActivity.this);
                boolean success = db.addData(expense);
                Toast.makeText(MainActivity.this, "submitted = " + success, Toast.LENGTH_SHORT).show();
                // update recyclerView
                rv.setAdapter(new RvAdapter(MainActivity.this, db.getAll()));
                // reset textView for amount to hint value
                et_amount.setText(null);
                et_amount.setHint("00.00");
                // update total
                double total = db.getTodayTotal(dateArray[0]);
                tvTotal.setText(String.format("%.2f", total));
            }
        });

        // button listeners for the report button
        btn_report.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                SimpleDateFormat formatter = new SimpleDateFormat("MM");
                Date todayDate = new Date();
                String month = formatter.format(todayDate);
                db = new DBHelper(MainActivity.this);
                String report = db.getReport(month);
                dialog.setTitle("This month Expense report");
                dialog.setMessage(report);
                dialog.setNegativeButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });

        // spinner listener for category spinner
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // do something upon selection
                String category = parent.getItemAtPosition(position).toString();
                // Toast.makeText(parent.getContext(), category, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


}