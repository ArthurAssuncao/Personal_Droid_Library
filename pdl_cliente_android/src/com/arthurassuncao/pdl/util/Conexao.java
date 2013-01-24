package com.arthurassuncao.pdl.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class Conexao {
	public static boolean verificaConexao(Context contexto) {
		ConnectivityManager cn = (ConnectivityManager)contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo infoRede = cn.getActiveNetworkInfo();

		if(infoRede != null && infoRede.isConnected()){
			return true;
		}
		else{
			return false;
		}
	}
	/*
	public static void buscaLivros(final Context contexto, String texto, final ListView listaLivros, int paginaAnterior, int paginaProxima) throws IOException{
		AndroidHttpClient httpClient = new AndroidHttpClient(Constantes.URL_SERVIDOR);
        //httpClient.setMaxRetries(5);
        ParameterMap params = httpClient.newParams();
        params.add("texto", texto);
        params.add("anterior", String.valueOf(paginaAnterior));
        params.add("proxima", String.valueOf(paginaProxima));
        httpClient.setConnectionTimeout(Constantes.TIMEOUT);

        httpClient.get("/busca", params, new AsyncCallback() {
			@Override
			public void onComplete(HttpResponse httpResponse) {
				if(httpResponse.getStatus() == 200){
					String conteudo = httpResponse.getBodyAsString();
					List<Livro> livros = null;
					try {
						if(conteudo != null){
							livros = Livro.createLivros(conteudo);
							listaLivros.setAdapter(new LivroListAdapter(contexto, livros));
						}
						else{
							Log.d(getClass().getName(), "Nenhuma informacao recebida");
						}
					}
					catch (JSONException e) {
						e.printStackTrace();
					}
				}

			}
		});
	}

	public static void buscaExtrato(final Context contexto){
		AndroidHttpClient httpClient = new AndroidHttpClient(Constantes.URL_SERVIDOR);
        //httpClient.setMaxRetries(5);
        ParameterMap params = httpClient.newParams();
        params.add("matricula", "6947");
        params.add("senha", "16111392");
        httpClient.setConnectionTimeout(Constantes.TIMEOUT);

        httpClient.get("/extrato", params, new AsyncCallback() {
			@Override
			public void onComplete(HttpResponse httpResponse) {
				if(httpResponse.getStatus() == 200){
					String conteudo = httpResponse.getBodyAsString();
					Extrato extrato = null;
					try {
						if(conteudo != null){
							extrato = Extrato.createExtrato(conteudo);
							PDLActivity pdl = (PDLActivity)contexto;
							pdl.setCamposExtrato(extrato);
						}
						else{
							Log.d(getClass().getName(), "Nenhuma informacao recebida");
						}
					}
					catch (JSONException e) {
						e.printStackTrace();
					}
				}

			}
		});
	}

	public static void renovaLivro(final Context contexto, String url){
		AndroidHttpClient httpClient = new AndroidHttpClient(Constantes.URL_SERVIDOR);
        //httpClient.setMaxRetries(5);
        ParameterMap params = httpClient.newParams();
        params.add("matricula", "6947");
        httpClient.setConnectionTimeout(Constantes.TIMEOUT);

        httpClient.get("/extrato", params, new AsyncCallback() {
			@Override
			public void onComplete(HttpResponse httpResponse) {
				if(httpResponse.getStatus() == 200){
					String conteudo = httpResponse.getBodyAsString();
					Extrato extrato = null;
					try {
						if(conteudo != null){
							extrato = Extrato.createExtrato(conteudo);
							PDLActivity pdl = (PDLActivity)contexto;
							pdl.setCamposExtrato(extrato);
						}
						else{
							Log.d(getClass().getName(), "Nenhuma informacao recebida");
						}
					}
					catch (JSONException e) {
						e.printStackTrace();
					}
				}

			}
		});
	}
	 */
}
