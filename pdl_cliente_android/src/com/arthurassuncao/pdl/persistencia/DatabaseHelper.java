package com.arthurassuncao.pdl.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHelper {
	private SQLHelper sqlHelper;
	private SQLiteDatabase db;
	private static final String NOME_BANCO = "pdl";
	private static final int VERSAO = 1;

	private static DatabaseHelper banco = null;

	private static final String[] DATABASE_DELETE = {
		"DROP TABLE IF EXISTS Livros_usuario;",
	};

	private static final String[] DATABASE_CREATE = new String[]{
		"CREATE TABLE IF NOT EXISTS Livros_usuario (" +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"nome VARCHAR(200), " +
				"data_devolucao DATE, " +
				"url_renovacao VARCHAR(255) " +
				");"
	};

	public DatabaseHelper(Context context) {

		// cria o SQLHelper
		sqlHelper = new SQLHelper(context, NOME_BANCO, VERSAO, DATABASE_CREATE, DATABASE_DELETE);

		//abre o banco de dados para escrita e leitura
		db = sqlHelper.getWritableDatabase();
	}

	public static DatabaseHelper getInstance(Context context){
		if(banco == null || !banco.db.isOpen()){
			banco = new DatabaseHelper(context);
		}
		return banco;
	}

	public void fechar(){
		if(db != null && db.isOpen()){
			db.close();
		}
		if(sqlHelper != null){
			sqlHelper.close();
		}
	}

	public SQLiteDatabase getDb() {
		return db;
	}
}