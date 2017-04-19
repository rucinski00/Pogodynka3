package com.oslowski.pogodynka;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;

/**
 * Created by Piotrek on 2017-02-28.
 */

public class Pobieracz implements Callable<StringBuilder> {



    private StringBuilder strona;



    public StringBuilder getStrona() {
        return strona;
    }

    private StringBuilder czytaj()
    {
        StringBuilder wszystko = new StringBuilder();
        try
        {
            URL url = new URL("http://www.twojapogoda.pl/polska/mazowieckie/sokolow-podlaski");


            URLConnection conn = url.openConnection();

            conn.setRequestProperty ( "User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)" );
            conn.connect();
            //BufferedReader in = new BufferedReader( new InputStreamReader(url.openStream()) );
            BufferedReader in = new BufferedReader( new InputStreamReader(conn.getInputStream()) );



            String inputLine;

            wszystko = new StringBuilder();

            System.setProperty("http.agent", "\"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)\"");

            //licznik = 0;
            while ((inputLine = in.readLine()) != null )
            {
                //licznik ++;
                wszystko.append( inputLine);

            }


        }
        catch(IOException e)
        {
            //temperaturaText.setText( "IOException:"+e.getMessage());
        }
        catch(Exception e)
        {
            //temperaturaText.setText( "Exception:"+e.toString());
        }


        return wszystko;
    }

    @Override
    public StringBuilder call() throws Exception {
        strona = czytaj();
        return strona;
    }
}
