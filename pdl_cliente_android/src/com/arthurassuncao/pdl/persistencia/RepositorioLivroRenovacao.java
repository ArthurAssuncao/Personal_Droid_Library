package com.arthurassuncao.pdl.persistencia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.arthurassuncao.pdl.Livro;
import com.arthurassuncao.pdl.LivroRenovacao;
import com.arthurassuncao.pdl.LivroRenovacao.LivrosRenovacao;

public class RepositorioLivroRenovacao {
	private static final String NOME_TABELA = "Livros_usuario";

	private SQLiteDatabase db;

	public RepositorioLivroRenovacao() {
		// TODO Auto-generated constructor stub
	}

	public RepositorioLivroRenovacao(Context context) {
		//pega o banco de dados
		db = DatabaseHelper.getInstance(context).getDb();
	}

	private ContentValues createContentValues(LivroRenovacao livroRenovacao){
		ContentValues valores = new ContentValues();
		valores.put(LivrosRenovacao.NOME, livroRenovacao.getLivro().getTexto());
		valores.put(LivrosRenovacao.DATA_DEVOLUCAO, livroRenovacao.getDataDevolucao());
		valores.put(LivrosRenovacao.URL_RENOVACAO, livroRenovacao.getUrlRenovacao());
		return valores;
	}

	public long insert(LivroRenovacao livroRenovacao){
		ContentValues valores = createContentValues(livroRenovacao);

		return db.insert(NOME_TABELA, "", valores);
	}

	public int delete(LivroRenovacao livroRenovacao){
		return db.delete(NOME_TABELA, String.format("%s=?", LivrosRenovacao._ID),  new String[]{String.valueOf(livroRenovacao.getId())});
	}
	
	public int deleteAll(){
		return db.delete(NOME_TABELA, "1",  null);
	}

	public int update(LivroRenovacao livroRenovacao){
		ContentValues valores = createContentValues(livroRenovacao);
		return db.update(NOME_TABELA, valores, String.format("%s=?", LivrosRenovacao._ID), new String[]{String.valueOf(livroRenovacao.getId())});
	}

	public Cursor getCursor(){
		try{
			return db.query(NOME_TABELA, LivrosRenovacao.COLUNAS, null, null, null, null, null);
		}catch(SQLException e){
			return null;
		}
	}

	private Map<String, Integer> getIndicesLivroRenovacao(Cursor cursor){
		Map<String, Integer> indices = new HashMap<String, Integer>();

		int indiceNome = cursor.getColumnIndex(LivrosRenovacao.NOME);
		int indiceDataDevolucao = cursor.getColumnIndex(LivrosRenovacao.DATA_DEVOLUCAO);
		int indiceURL = cursor.getColumnIndex(LivrosRenovacao.URL_RENOVACAO);

		indices.put(LivrosRenovacao.NOME, indiceNome);
		indices.put(LivrosRenovacao.DATA_DEVOLUCAO, indiceDataDevolucao);
		indices.put(LivrosRenovacao.URL_RENOVACAO, indiceURL);

		return indices;
	}

	private LivroRenovacao createLivroRenovacao(Cursor cursor){
		Map<String, Integer> indices = getIndicesLivroRenovacao(cursor);

		LivroRenovacao livroRenovacao = new LivroRenovacao();
		Livro livro = new Livro(cursor.getString(indices.get(LivrosRenovacao.NOME)), "indefinido"); 

		livroRenovacao.setLivro(livro);
		livroRenovacao.setDataDevolucao(cursor.getString(indices.get(LivrosRenovacao.DATA_DEVOLUCAO)));
		livroRenovacao.setUrlRenovacao(cursor.getString(indices.get(LivrosRenovacao.URL_RENOVACAO)));

		return livroRenovacao;
	}

	public List<LivroRenovacao> listarLivrosRenovacao(){
		Cursor cursor = getCursor();
		List<LivroRenovacao> livrosRenovacao = new ArrayList<LivroRenovacao>();

		if(cursor.moveToFirst()){
			do{
				LivroRenovacao livroRenovacao = createLivroRenovacao(cursor);
				livrosRenovacao.add(livroRenovacao);

			}while(cursor.moveToNext());
		}

		return livrosRenovacao;
	}

	public void fecharDB(){

		if(db != null)
			db.close();
	}
}
