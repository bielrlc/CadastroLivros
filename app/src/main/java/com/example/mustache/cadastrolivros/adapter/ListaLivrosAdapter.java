package com.example.mustache.cadastrolivros.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mustache.cadastrolivros.ListaLivrosActivity;
import com.example.mustache.cadastrolivros.R;
import com.example.mustache.cadastrolivros.model.Livro;

import java.util.List;

/**
 * Created by Pinhas on 08/05/2017.
 */

public class ListaLivrosAdapter extends BaseAdapter {

    private final List<Livro> livros;
    private final Activity activity;

    public ListaLivrosAdapter(Activity activity, List<Livro> livros) {
        this.livros = livros;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return livros.size();
    }

    @Override
    public Object getItem(int position) {
        return livros.get(position);
    }

    @Override
    public long getItemId(int position) {
        return livros.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View layout = activity.getLayoutInflater().inflate(R.layout.item, parent, false);
        TextView nome = (TextView) layout.findViewById(R.id.item_nome);

        Livro livro = livros.get(position);
        nome.setText(livro.getNome());

        TextView autor = (TextView) layout.findViewById(R.id.item_autor);
        if (autor != null)
            autor.setText(livro.getAutor());

        TextView site = (TextView) layout.findViewById(R.id.item_site);
        if (site != null)
            site.setText(livro.getSite());

        Bitmap bm = null;
        if (livro.getCaminhoFoto() != null){
            bm = BitmapFactory.decodeFile(livro.getCaminhoFoto());
            bm = Bitmap.createScaledBitmap(bm, 100, 100, true);
        } else {
            bm = BitmapFactory.decodeResource(activity.getResources(),R.mipmap.item_list_default);
        }

        ImageView foto = (ImageView) layout.findViewById(R.id.item_foto);
        foto.setImageBitmap(bm);

        if (position % 2 == 0){
            layout.setBackgroundColor(activity.getResources().getColor(R.color.linha_par));
        } else {
            layout.setBackgroundColor(activity.getResources().getColor(R.color.linha_impar));
        }

        return layout;
    }
}
