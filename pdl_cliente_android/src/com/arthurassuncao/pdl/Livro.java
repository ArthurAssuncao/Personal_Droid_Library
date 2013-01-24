package com.arthurassuncao.pdl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Livro {
	private String texto;
	private String status;
	
	public Livro(String texto, String status) {
		super();
		this.texto = texto;
		this.status = status;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static List<Livro> createLivros(String json) throws JSONException{
		List<Livro> livros = new ArrayList<Livro>();
		
		JSONArray arrayJson = new JSONArray(json);
		for (int i = 0; i < arrayJson.length(); i++){
			JSONObject objetoLivros = arrayJson.getJSONObject(i);
			String status = objetoLivros.getString("status");
			String texto = objetoLivros.getString("dados");
			livros.add(new Livro(texto, status));
		}
		
		return livros;
	}
}
