package com.example.mustache.cadastrolivros.lib;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mustache.cadastrolivros.ListaLivrosActivity;
import com.example.mustache.cadastrolivros.dao.LivroDAO;
import com.example.mustache.cadastrolivros.model.Livro;

/**
 * Created by Pinhas on 26/04/2017.
 */

public class ContextActionBar implements ActionMode.Callback {

    private ListaLivrosActivity activity;
    private Livro livroSelecionado;

    public ContextActionBar(ListaLivrosActivity activity, Livro livroSelecionado){
        this.activity = activity;
        this.livroSelecionado = livroSelecionado;
    }

    @Override
    public boolean onCreateActionMode(final ActionMode mode, Menu menu) {
        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(activity)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Deletar")
                        .setMessage("Deseja mesmo deletar?")
                        .setPositiveButton("Sim",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        LivroDAO dao = new LivroDAO(activity);
                                        dao.deleteLivro(livroSelecionado);
                                        Log.v(LivroDAO.LOG, "Livro excluido do banco de dados");
                                        dao.close();

                                        activity.carregaLista();
                                        mode.finish();
                                    }
                                }).setNegativeButton("Não", null).show();
                return false;
            }
        });

        Menu moreOptions = menu.addSubMenu("...");
        moreOptions.add("Mais opções");

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }
}
