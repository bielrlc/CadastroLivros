package com.example.mustache.cadastrolivros;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.mustache.cadastrolivros.dao.CadastroLivroDAO;
import com.example.mustache.cadastrolivros.model.Livro;

import java.io.File;

import static com.example.mustache.cadastrolivros.ListaLivrosActivity.LIVRO_SELECIONADO;
import static com.example.mustache.cadastrolivros.ListaLivrosActivity.LOG;

public class CadastroLivroActivity extends AppCompatActivity {

    private FormularioHelper helper;
    private Livro livroSelecionado;
    private String localArquivoFoto;
    private static final int TIRA_FOTO = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_livro);
        Log.v(LOG, "CadastroLivroActivity inicializada");

        final Intent intent = this.getIntent();
        livroSelecionado = (Livro) intent.getSerializableExtra(LIVRO_SELECIONADO);

        this.helper = new FormularioHelper(this);

        if (livroSelecionado == null)
            Log.w(LOG, "Nenhum livro selecioando");
        else {
            Log.v(LOG, "O livro selecionado foi o: " + livroSelecionado.getId());
            helper.setLivroNoFormulario(livroSelecionado);
        }

        Button button = helper.getFotoButton();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                localArquivoFoto = getExternalFilesDir(null) +
                        "/" + System.currentTimeMillis()+".jpg";
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri localFoto = Uri.fromFile(new File(localArquivoFoto));
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, localFoto);
                startActivityForResult(intentCamera, TIRA_FOTO);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == TIRA_FOTO) {
            if (requestCode == Activity.RESULT_OK) {
                helper.carregaImagem(this.localArquivoFoto);
            } else {
                this.localArquivoFoto = null;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cadastro, menu);
        Log.v(LOG, "Menu criado");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_formulario_ok:
                Log.v(LOG, "Item de menu clicado");
                if ((helper.temNome() && (helper.temAutor()))){

                    CadastroLivroDAO dao = new CadastroLivroDAO(CadastroLivroActivity.this, CadastroLivroDAO.DATABASE, null, CadastroLivroDAO.VERSION);
                    Livro livro = helper.getLivroFromFormulario();

                    if (livro.getId() == null)
                        dao.insereLivro(livro);
                    else
                        dao.alteraLivro(livro);

                    dao.close();
                    finish();
                    return false;
                }
                else {
                    helper.mostraErro();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
