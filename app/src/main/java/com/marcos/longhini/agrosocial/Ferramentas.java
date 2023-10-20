package com.marcos.longhini.agrosocial;

import android.content.Context;
import android.widget.Toast;

public class Ferramentas {
    public  static void mensagem_Tela(Context ctx, String msg){
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();

    }
}
