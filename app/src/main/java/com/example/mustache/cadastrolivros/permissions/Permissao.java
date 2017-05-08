package com.example.mustache.cadastrolivros.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.mustache.cadastrolivros.ListaLivrosActivity;

import java.util.ArrayList;

/**
 * Created by Pinhas on 03/05/2017.
 */

public class Permissao {

    private static final int CODE = 123;
    private static ArrayList<String> listaPermissoes = new ArrayList<String>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void fazPermissao(Activity activity) {
        String[] permissoes = {Manifest.permission.CALL_PHONE,
                Manifest.permission.INTERNET};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            for (String permissao : permissoes) {
                if (activity.checkSelfPermission(permissao)
                        != PackageManager.PERMISSION_GRANTED) {
                    listaPermissoes.add(permissao);
                }
            }
            request(activity);
        } else
            Log.e(ListaLivrosActivity.LOG, "Verificar versão do Android");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void request(Activity activity) {
        String[] array = listaPermissoes.toArray(new String[]{});
        if (listaPermissoes.size() > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(array, CODE);
            }
        } else
            Log.w(ListaLivrosActivity.LOG, "Nenhuma permissão foi concedida");
    }
}
