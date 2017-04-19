package com.oslowski.pogodynka;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.util.Calendar.*;

public class MainActivity extends AppCompatActivity {


    TextView temperaturaText;
    TextView wiatrSzybkoscText;
    TextView wiatrKierunekText;
    TextView dataOstatniegoOdswiezeniaText;

    Integer interwalOdwswiezania = 60*60; // sekundy
    Date dataOstatniegoOdswiezenia;


    //Integer licznik;
    static String temperatura;
    static String wiatrSzybkosc;
    static String wiatrKierunek;

    Date localTime = new Date();


    @Override
    protected void onPause()
    {
        super.onPause();


    }



    void odswiez()
    {


        temperaturaText.setText(temperatura);
        wiatrSzybkoscText.setText(wiatrSzybkosc);
        wiatrKierunekText.setText(wiatrKierunek);

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        odswiez();

    }



    @Override
    protected void onRestart()
    {
        super.onRestart();

        odswiez();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //android.os.StrictMode.ThreadPolicy policy = new android.os.StrictMode.ThreadPolicy.Builder().permitAll().build();
        //android.os.StrictMode.setThreadPolicy(policy);


        temperatura = "!";
        wiatrSzybkosc = "!";
        wiatrKierunek = "!";

        temperaturaText = (TextView) findViewById(R.id.temperaturaText);
        wiatrSzybkoscText = (TextView) findViewById(R.id.wiatrSzybkoscText);
        wiatrKierunekText = (TextView) findViewById(R.id.wiatrKierunekText);
        dataOstatniegoOdswiezeniaText = (TextView) findViewById(R.id.dataOstatniegoOdswiezeniaText);


        obliczaj();

    }

    public void onClick(View view) {

        //obliczaj();
    }

    private void obliczaj()
    {
        Date dataBiezaca = new Date();
        if (dataOstatniegoOdswiezenia == null)
        {
            Calendar c1 = Calendar.getInstance();
            c1.set(1990,10,1);
            dataOstatniegoOdswiezenia = c1.getTime();
        }
        long diff = dataBiezaca.getTime() - dataOstatniegoOdswiezenia.getTime();

        diff = diff / 1000;// % 60;
        dataOstatniegoOdswiezeniaText.setText(Long.toString(diff));
        if ((diff > interwalOdwswiezania)) {
            //obliczaj();


            ExecutorService executor = Executors.newFixedThreadPool(1);
            Callable<StringBuilder> watek = new Pobieracz();


            final Future<StringBuilder> result = executor.submit(watek);
            StringBuilder strona = new StringBuilder();
            try {
                strona = result.get();
            } catch (InterruptedException e) {

            } catch (Exception e) {

            }


            Integer pozycja = strona.indexOf("<div class=\"items poland\">");

            strona.delete(0, pozycja);
            Integer tempOd = strona.indexOf("<strong>");
            Integer tempDo = strona.indexOf("</strong>");
            temperatura = strona.substring(tempOd + 8, tempDo);

            strona.delete(0, tempDo);

            pozycja = strona.indexOf("<div class=\"label\">Wiatr</div>");
            strona.delete(0, pozycja);

            tempOd = strona.indexOf("<div class=\"value\">");
            tempDo = strona.indexOf("<img src=\"");

            wiatrSzybkosc = strona.substring(tempOd + 19, tempDo).replaceAll("(\\r|\\n|\\t)", "");
            //wiatrSzybkosc.replaceAll("(\\r|\\n)", "");
            //wiatrSzybkosc = wiatrSzybkosc.replace("\n", wiatrSzybkosc);
            //wiatrSzybkosc = wiatrSzybkosc.replace("\r", wiatrSzybkosc);

            strona.delete(0, tempDo);
            tempOd = strona.indexOf("alt=\"");
            strona.delete(0, tempOd + 5);
            tempDo = strona.indexOf("\"");

            wiatrKierunek = strona.substring(0, tempDo);

            //temperaturaText.setText( "pozycja="+pozycja.toString());
            //temperaturaText.setText("temperatura=["+temperatura+"]");
            //temperaturaText.setText( strona );


            dataOstatniegoOdswiezenia = new Date();
            dataOstatniegoOdswiezeniaText.setText(dataOstatniegoOdswiezenia.toString());
            onResume();
        }
    }



}
