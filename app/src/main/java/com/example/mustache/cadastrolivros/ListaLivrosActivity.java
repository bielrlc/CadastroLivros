package com.example.mustache.cadastrolivros;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mustache.cadastrolivros.dao.CadastroLivroDAO;
import com.example.mustache.cadastrolivros.model.Livro;
import com.example.mustache.cadastrolivros.permissions.Permissao;

import java.util.List;

public class ListaLivrosActivity extends AppCompatActivity {

    public final static String LOG = "SISTEMA";
    public final static String LIVRO_SELECIONADO = "livroSelecionado";
    private ListView listaLivros;
    private Button botao;
    private List<Livro> livros;
    private Livro livroSelecionado;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_livros);
        Log.v(LOG, "ListaLivrosActivity inicializada");

        Permissao.fazPermissao(this);
        Log.v(LOG, "Permissões concedidas");

        this.botao = (Button) findViewById(R.id.lista_livros_floating_button);
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(LOG, "Botão clicado, chamando CadastroLivroActivity");
                Intent intent =  new Intent(ListaLivrosActivity.this, CadastroLivroActivity.class);

                startActivity(intent);
            }
        });

        this.listaLivros = (ListView) findViewById(R.id.lista_livros);
        registerForContextMenu(listaLivros);

        /*
        listaLivros.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int posicao, long id) {

                livroSelecionado = (Livro) adapter.getItemAtPosition(posicao);

                ContextActionBar actionBar = new ContextActionBar(ListaLivrosActivity.this, livroSelecionado);
                ListaLivrosActivity.this.startSupportActionMode(actionBar);

                return true;
            }
        });*/

        listaLivros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int posicao, long id) {

                livroSelecionado = (Livro) adapter.getItemAtPosition(posicao);
                CadastroLivroDAO dao = new CadastroLivroDAO(ListaLivrosActivity.this, CadastroLivroDAO.DATABASE, null, CadastroLivroDAO.VERSION);
                livroSelecionado = dao.getLivroPorId(livroSelecionado.getId());

                if (livroSelecionado == null)
                    Log.e(LOG, "Nenhum livro selecionado");
                else {
                    Intent edicao = new Intent(ListaLivrosActivity.this, CadastroLivroActivity.class);
                    edicao.putExtra(LIVRO_SELECIONADO, livroSelecionado);
                    startActivity(edicao);
                }
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        livroSelecionado = (Livro) listaLivros.getAdapter().getItem(info.position);

        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                new AlertDialog.Builder(ListaLivrosActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Deletar")
                        .setMessage("Deseja mesmo deletar?")
                        .setPositiveButton("Sim",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        CadastroLivroDAO dao = new CadastroLivroDAO(ListaLivrosActivity.this, CadastroLivroDAO.DATABASE, null, CadastroLivroDAO.VERSION);
                                        dao.deleteLivro(livroSelecionado);
                                        dao.close();
                                        Log.v(CadastroLivroDAO.LOG, "Livro excluido do banco de dados");

                                        carregaLista();
                                    }
                                }).setNegativeButton("Não", null).show();
                return false;
            }
        });

        MenuItem ligar = menu.add("Ligar p/");
        Intent intentLigar = new Intent(Intent.ACTION_DIAL);
        ligar.setIntent(intentLigar);

        MenuItem mapa = menu.add("Livraria Cultura no Mapa");
        Intent intentMapa = new Intent((Intent.ACTION_VIEW));
        intentMapa.setData(Uri.parse("geo:0,0?z=14&q=" + "R. Palestra Itália, 500"));
        mapa.setIntent(intentMapa);

        MenuItem site = menu.add("Acessar Site da Editora");
        Intent intentSite = new Intent(Intent.ACTION_VIEW);
        String linkSite = livroSelecionado.getSite();
        if (!linkSite.startsWith("http://")){
            linkSite = "http://" + linkSite;
        }
        intentSite.setData(Uri.parse(linkSite));
        site.setIntent(intentSite);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.carregaLista();
    }

    public void carregaLista(){
        CadastroLivroDAO dao = new CadastroLivroDAO(this, CadastroLivroDAO.DATABASE, null, CadastroLivroDAO.VERSION);
        livros = dao.getList();
        dao.close();

        ArrayAdapter<Livro> adapter = new ArrayAdapter<Livro>(this, android.R.layout.simple_list_item_1, livros);

        this.listaLivros.setAdapter(adapter);
        if (listaLivros == null)
            Log.i(LOG, "Lista sem registros");
    }
}