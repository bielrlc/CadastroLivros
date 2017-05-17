package com.example.mustache.cadastrolivros.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.mustache.cadastrolivros.ListaLivrosActivity;
import com.example.mustache.cadastrolivros.converter.LivroConverter;
import com.example.mustache.cadastrolivros.dao.LivroDAO;
import com.example.mustache.cadastrolivros.model.Livro;
import com.example.mustache.cadastrolivros.support.WebClient;

import java.io.IOException;
import java.util.List;

/**
 * Created by Pinhas on 16/05/2017.
 */

public class EnviaLivroTask extends AsyncTask<Object, Object, String> {

    private Context context;
    private ProgressDialog pd;

    public EnviaLivroTask(Context context) {
        Log.v(ListaLivrosActivity.LOG, "Task acionada");
        this.context = context;
    }

    @Override
    protected String doInBackground(Object[] objects) {

        LivroDAO dao = new LivroDAO(context);
        List<Livro> livros = dao.getLista();
        dao.close();

        String json = new LivroConverter().toJson(livros);

        WebClient webClient = new WebClient();
        String resposta = null;
        try {
            resposta = webClient.post(json);
        } catch (IOException e) {
            Log.e(ListaLivrosActivity.LOG, "Problemas com o WebClient em tempo de execução");
            e.printStackTrace();
        }
        return resposta;
    }

    @Override
    protected void onPostExecute(String resposta) {
        pd.dismiss();
        if (resposta == null)
            Toast.makeText(context, "Nenhum retorno disponível", Toast.LENGTH_LONG).show();
        else {
            Toast.makeText(context, "SUCESSO", Toast.LENGTH_LONG).show();
            Log.v(ListaLivrosActivity.LOG, resposta);
        }
    }

    @Override
    protected void onPreExecute() {
        pd = ProgressDialog.show(context, "Aguarde...", "Enviando Dados", true, false);
    }
}
