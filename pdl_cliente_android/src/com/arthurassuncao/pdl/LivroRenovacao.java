package com.arthurassuncao.pdl;

import android.provider.BaseColumns;

import com.arthurassuncao.pdl.util.Constantes;
import com.arthurassuncao.pdl.util.Data;

public class LivroRenovacao {
	private int id;
	private Livro livro;
	private Data dataDevolucao;
	private String urlRenovacao;
	private final String FORMATO_DATA = "##-##-##";
	private final String FORMATO_DATA_ANO_COMPLETO = "##/##/####";
	
	public LivroRenovacao() {
		// TODO Auto-generated constructor stub
	}
	
	public LivroRenovacao(Livro livro, String dataDevolucao, String urlRenovacao) {
		super();
		this.livro = livro;
		this.dataDevolucao = new Data(dataDevolucao, FORMATO_DATA);
		this.urlRenovacao = urlRenovacao;
	}

	public Livro getLivro() {
		return livro;
	}
	
	public void setLivro(Livro livro) {
		this.livro = livro;
	}
	
	public String getUrlRenovacaoCompleta() {
		return Constantes.URL_BIBLIOTECA_IFET + urlRenovacao;
	}
	
	public String getUrlRenovacao() {
		return urlRenovacao;
	}
	
	public void setUrlRenovacao(String urlRenovacao) {
		this.urlRenovacao = urlRenovacao;
	}

	public String getDataDevolucao() {
		return dataDevolucao.getDataString();
	}

	public void setDataDevolucao(String dataDevolucao) {
		if(dataDevolucao.length() == 8){
		this.dataDevolucao = new Data(dataDevolucao, FORMATO_DATA);
		}
		else if(dataDevolucao.length() == 10){
			this.dataDevolucao = new Data(dataDevolucao, FORMATO_DATA_ANO_COMPLETO);
		}
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public static class LivrosRenovacao implements BaseColumns{
		public static final String NOME = "nome";
        public static final String DATA_DEVOLUCAO = "data_devolucao";
        public static final String URL_RENOVACAO = "url_renovacao";
        
        public static final String[] COLUNAS = {NOME, DATA_DEVOLUCAO, URL_RENOVACAO};
	}
	
	
}
