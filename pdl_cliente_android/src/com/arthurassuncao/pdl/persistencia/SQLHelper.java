package com.arthurassuncao.pdl.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper {

    private String[] create;
    private String[] delete;
    
    public SQLHelper(Context context, String name, int version, String[] create, String[] delete) {
            super(context, name, null, version);
            
            this.create = create;
            this.delete = delete;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
            for(int i = 0; i < create.length; i++){
                    db.execSQL(create[i]);
            }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //deleta todos os registros
            for(int i = 0; i < create.length; i++){
                    db.execSQL(delete[i]);
            }
            
            //cria uma nova base de dados
            onCreate(db);
    }

}
