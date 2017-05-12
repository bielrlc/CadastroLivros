package com.example.mustache.cadastrolivros.converter;

import android.util.Log;

import com.example.mustache.cadastrolivros.ListaLivrosActivity;
import com.example.mustache.cadastrolivros.model.Livro;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.List;

/**
 * Created by Pinhas on 11/05/2017.
 */

public class LivroConverter {

    public String toJson(List<Livro> livros){

        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object().key("list").array().object().key("livro").array();
            for (Livro livro :livros) {
                jsonStringer.object()
                        .key("id").value(livro.getId())
                        .key("isbn").value(livro.getIsbn())
                        .key("nome").value(livro.getNome())
                        .key("autor").value(livro.getAutor())
                        .key("ano").value(livro.getAno())
                        .key("site").value(livro.getSite())
                        .key("nota").value(livro.getNota())
                        .key("caminhoFoto").value(livro.getCaminhoFoto())
                .endObject();
            }
            return jsonStringer.endArray().endObject().endArray().endObject().toString();
        } catch (JSONException e) {
            Log.e(ListaLivrosActivity.LOG, "Erro no conversor de JSON");
            throw new RuntimeException();
        }
    }
}
