package com.example.mustache.cadastrolivros.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mustache.cadastrolivros.model.Livro;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pinhas on 18/04/2017.
 */

public class CadastroLivroDAO extends SQLiteOpenHelper{

    public final static String DATABASE = "CadastroLivros";
    public final static int VERSION = 1;
    public final static String TABELA = "Livro";
    public final static String LOG = "SISTEMA";

    public CadastroLivroDAO(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE, factory, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.v(LOG, "DAO criado");
        String ddl = "CREATE TABLE " + TABELA
                + " (id INTEGER PRIMARY KEY, "
                + " isbn TEXT, "
                + " nome TEXT NOT NULL, "
                + " autor TEXT NOT NULL, "
                + " ano TEXT, "
                + " site TEXT, "
                + " nota REAL);";
        database.execSQL(ddl);
        Log.v(LOG, "Banco de dados criado com sucesso");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int versaoAntiga, int versaoNova) {
        String sql = "DROP TABLE IF EXISTS " + TABELA;
        database.execSQL(sql);
        Log.v(LOG, "Banco de dados excluído");
        onCreate(database);
    }

    public void insereLivro(Livro livro){
        ContentValues values = new ContentValues();

        values.put("isbn", livro.getIsbn());
        values.put("nome", livro.getNome());
        values.put("autor", livro.getAutor());
        values.put("ano", livro.getAno());
        values.put("site", livro.getSite());
        values.put("nota", livro.getNota());

        getWritableDatabase().insert(TABELA, null, values);
        Log.v(LOG, "Livro inserido com sucesso");
    }

    public void alteraLivro(Livro livro){
        ContentValues values = new ContentValues();

        values.put("id", livro.getId());
        values.put("isbn", livro.getIsbn());
        values.put("nome", livro.getNome());
        values.put("autor", livro.getAutor());
        values.put("ano", livro.getAno());
        values.put("site", livro.getSite());
        values.put("nota", livro.getNota());

        String[] idParaSerAlterado = {livro.getId().toString()};
        getWritableDatabase().update(TABELA, values, "id=?", idParaSerAlterado);
        Log.v(LOG, "Livro alterado com sucesso");
    }

    public List<Livro> getList(){
        SQLiteDatabase database = getReadableDatabase();

        ArrayList<Livro> livros = new ArrayList<Livro>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABELA + ";", null);

        while (cursor.moveToNext()){
            Livro livro = new Livro();

            livro.setId(cursor.getLong(cursor.getColumnIndex("id")));
            livro.setIsbn(cursor.getString(cursor.getColumnIndex("isbn")));
            livro.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            livro.setAutor(cursor.getString(cursor.getColumnIndex("autor")));
            livro.setAno(cursor.getString(cursor.getColumnIndex("ano")));
            livro.setSite(cursor.getString(cursor.getColumnIndex("site")));
            livro.setNota(cursor.getDouble(cursor.getColumnIndex("nota")));

            livros.add(livro);
        }

        cursor.close();

        if (livros.isEmpty())
            Log.v(LOG, "Lista vazia");
        else
            Log.v(LOG,"Retornando lista de livros cadastrados");

        return livros;
    }

    public void deleteLivro(Livro livro){

        String[] id = {livro.getId().toString()};
        getWritableDatabase().delete(TABELA, "id=?", id);
        Log.v(LOG, "Livro deletado");
    }

    public Livro getLivroPorId(Long id) {

        String[] args = {id.toString()};
        Cursor cursor = getWritableDatabase().rawQuery("SELECT * FROM " + TABELA + " WHERE id=?;", args);

        cursor.moveToFirst();

        Livro livro = new Livro();

        livro.setId(cursor.getLong(cursor.getColumnIndex("id")));
        livro.setIsbn(cursor.getString(cursor.getColumnIndex("isbn")));
        livro.setNome(cursor.getString(cursor.getColumnIndex("nome")));
        livro.setAutor(cursor.getString(cursor.getColumnIndex("autor")));
        livro.setAno(cursor.getString(cursor.getColumnIndex("ano")));
        livro.setSite(cursor.getString(cursor.getColumnIndex("site")));
        livro.setNota(cursor.getDouble(cursor.getColumnIndex("nota")));

        cursor.close();

        Log.v(LOG, "Retornando Livro selecionado");
        return livro;
    }
}
