package com.arthurassuncao.pdl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Extrato {
	private Usuario usuario;
	private String dataEmissao;
	private List<LivroRenovacao> livros;

	public Extrato(Usuario usuario, String dataEmissao, List<LivroRenovacao> livros) {
		super();
		this.usuario = usuario;
		this.dataEmissao = dataEmissao;
		this.livros = livros;
	}

	public String getDataEmissao() {
		return dataEmissao;
	}

	public List<LivroRenovacao> getLivros() {
		return livros;
	}

	public Usuario getUsuario() {
		return usuario;
	}
	
	public static Extrato createExtrato(String json) throws JSONException{
		Extrato extrato = null;
		
		JSONObject objetoJson = new JSONObject(json);
		String dataEmissao = objetoJson.getString("data_emissao");
		String matricula = objetoJson.getString("matricula");
		String nome = objetoJson.getString("nome");
		
		JSONArray arrayLivros = objetoJson.getJSONArray("livros");
		int quantidadeLivros = arrayLivros.length();

		List<LivroRenovacao> livrosRenovacao = new ArrayList<LivroRenovacao>();
		for (int i = 0; i < quantidadeLivros; i++){
			JSONObject objetoLivro = arrayLivros.getJSONObject(i);
			String livro = objetoLivro.getString("livro");
			String linkRenovacao = objetoLivro.getString("link_renovacao");
			String dataDevolucao = objetoLivro.getString("data_devolucao");
					
			LivroRenovacao livroRenovacao = new LivroRenovacao(new Livro(livro, "nao definido"), dataDevolucao, linkRenovacao);
			livrosRenovacao.add(livroRenovacao);
		}
		
		Usuario usuario = new Usuario(matricula, nome);
		
		extrato = new Extrato(usuario, dataEmissao, livrosRenovacao);
		
		return extrato;
	}
	
}
