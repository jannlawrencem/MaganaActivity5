package com.example.maganaactivity5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.maganaactivity5.Retrofit.RetrofitBuilder;
import com.example.maganaactivity5.Retrofit.RetrofitInterface;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    Button button_convert;
    EditText text_initial_currency, text_ansresultcurrency;
    Spinner spnnr_fromcurrency, spnnr_tocurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_convert = (Button) findViewById(R.id.btn_convert);
        text_initial_currency = (EditText) findViewById(R.id.txt_initial_currency);
        text_ansresultcurrency = (EditText) findViewById(R.id.txt_ansresultcurrency);
        spnnr_fromcurrency = (Spinner)findViewById(R.id.spinner_from);
        spnnr_tocurrency = (Spinner)findViewById(R.id.spinner_to);


        String [] dropDownList = {"USD","EUR","KRW","JPY","PHP"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,dropDownList);
        spnnr_fromcurrency.setAdapter(adapter);
        spnnr_tocurrency.setAdapter(adapter);

        button_convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitInterface retrofitInterface = RetrofitBuilder.getRetrofitInstance().create(RetrofitInterface.class);
                Call<JsonObject> call = retrofitInterface.getExchangeCurrency(spnnr_fromcurrency.getSelectedItem().toString());
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.d("response",String.valueOf(response.body()));
                        Toast.makeText(MainActivity.this,"Computed",Toast.LENGTH_SHORT).show();
                        JsonObject res = response.body();
                        JsonObject rates = res.getAsJsonObject("conversion_rates");
                        Double currency = Double.valueOf(text_initial_currency.getText().toString());
                        Double multiplier = Double.valueOf(rates.get(spnnr_tocurrency.getSelectedItem().toString()).toString());
                        Double result = currency * multiplier;
                        text_ansresultcurrency.setText(String.valueOf(result));
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
            }
        });
    }
}