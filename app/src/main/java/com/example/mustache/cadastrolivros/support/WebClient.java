package com.example.mustache.cadastrolivros.support;

import android.util.Log;

import com.example.mustache.cadastrolivros.ListaLivrosActivity;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Pinhas on 11/05/2017.
 */

public class WebClient {

    public String post(String json) throws IOException {

        try {

            URL url = new URL("https://www.caelum.com.br/mobile");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-type", "application/json");

            connection.setDoInput(true);
            connection.setDoOutput(true);

            PrintStream saida = new PrintStream(connection.getOutputStream());
            saida.println(json);

            connection.connect();

            String jsonDeResposta = new Scanner(connection.getInputStream()).next();
            return jsonDeResposta;

        } catch (MalformedURLException e) {
            Log.e(ListaLivrosActivity.LOG, "Problema com a URL informada ");
            throw new MalformedURLException();
        } catch (ProtocolException e) {
            Log.e(ListaLivrosActivity.LOG, "Problema com o Procotolo de Comunicação ");
            throw new ProtocolException();
        } catch (IOException e) {
            Log.e(ListaLivrosActivity.LOG, "Problemas com o WebClient ");
            throw new IOException();
        }
    }
}
