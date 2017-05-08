package com.example.mustache.cadastrolivros;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.mustache.cadastrolivros.model.Livro;

import static com.example.mustache.cadastrolivros.ListaLivrosActivity.LOG;

/**
 * Created by Pinhas on 18/04/2017.
 */

public class FormularioHelper {

    private ImageView foto;
    private Button fotoButton;
    private EditText isbn;
    private EditText nome;
    private EditText autor;
    private EditText ano;
    private EditText site;
    private RatingBar nota;
    private Livro livro;

    public FormularioHelper(CadastroLivroActivity activity) {

        this.foto = (ImageView) activity.findViewById(R.id.foto_usuario);
        this.fotoButton = (Button) activity.findViewById(R.id.foto_button);
        this.isbn = (EditText) activity.findViewById(R.id.cadastro_isbn);
        this.nome = (EditText) activity.findViewById(R.id.cadastro_nome);
        this.autor = (EditText) activity.findViewById(R.id.cadastro_autor);
        this.ano = (EditText) activity.findViewById(R.id.cadastro_ano);
        this.site = (EditText) activity.findViewById(R.id.cadastro_editora);
        this.nota = (RatingBar) activity.findViewById(R.id.cadastro_nota);

        livro = new Livro();
    }

    public void setLivroNoFormulario(Livro livro){
        isbn.setText(livro.getIsbn());
        nome.setText(livro.getNome());
        autor.setText(livro.getAutor());
        ano.setText(livro.getAno());
        site.setText(livro.getSite());
        nota.setProgress(livro.getNota().intValue());

        this.livro = livro;
    }

    public Livro getLivroFromFormulario(){

        this.livro.setIsbn(isbn.getText().toString());
        this.livro.setNome(nome.getText().toString());
        this.livro.setAutor(autor.getText().toString());
        this.livro.setAno(ano.getText().toString());
        this.livro.setSite(site.getText().toString());
        this.livro.setNota(Double.valueOf(nota.getProgress()));

        return livro;
    }

    public Button getFotoButton() {
        return fotoButton;
    }

    public boolean temNome(){
        return !nome.getText().toString().isEmpty();
    }

    public boolean temAutor(){
        return !autor.getText().toString().isEmpty();
    }

    public void mostraErroNome(){
        nome.setError("Campo nome não pode ficar vazio");
    }

    public void mostraErroAutor(){
        autor.setError("Campo autor não pode ficar vazio");
    }

    public void mostraErro(){
        if (!temNome()) {
            mostraErroNome();
            Log.e(LOG, "Campo nome não preenchido");
        }
        if (!temAutor()) {
            mostraErroAutor();
            Log.e(LOG, "Campo autor não preenchido");
        }
        else {
            Log.e(LOG, "Campos obrigatórios não preenchidos");
        }
    }

    public void carregaImagem(String localArquivoFoto) {
        Bitmap imagemFoto = BitmapFactory.decodeFile(localArquivoFoto);
        Bitmap imagemFotoReduzida = Bitmap.createScaledBitmap(
                imagemFoto, imagemFoto.getWidth(), 300, true);
        foto.setScaleType(ImageView.ScaleType.FIT_XY);
        foto.setImageBitmap(imagemFotoReduzida);
        foto.setTag(localArquivoFoto);
    }
}
