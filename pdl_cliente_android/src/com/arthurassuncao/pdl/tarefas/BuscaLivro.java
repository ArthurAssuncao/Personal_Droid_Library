package com.arthurassuncao.pdl.tarefas;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.arthurassuncao.pdl.IActivityMostraErro;
import com.arthurassuncao.pdl.Livro;
import com.arthurassuncao.pdl.LivroListAdapter;
import com.arthurassuncao.pdl.util.Constantes;

public class BuscaLivro extends AsyncTask<String, Void, Integer> {

	private Context contexto;
	private ProgressDialog progressDialog;
	private ListView listaLivros;
	private int paginaAnterior;
	private int paginaProxima;
	private String texto;
	private String conteudo;

	public BuscaLivro(Context contexto, ProgressDialog progressDialog,
			ListView listaLivros, int paginaAnterior, int paginaProxima) {
		super();
		this.contexto = contexto;
		this.progressDialog = progressDialog;
		this.listaLivros = listaLivros;
		this.paginaAnterior = paginaAnterior;
		this.paginaProxima = paginaProxima;
	}

	@Override
	protected void onPreExecute() {
		progressDialog.show();
	}

	@Override
	protected Integer doInBackground(String... textos) 
	{
		this.texto = textos[0];
		int responseCode = 0;
		try 
		{
			HttpClient client = new DefaultHttpClient();
			//HttpPost httppost = new HttpPost(String.format("%s/busca", Constantes.URL_SERVIDOR));

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("texto", this.texto));
			nameValuePairs.add(new BasicNameValuePair("anterior", String.valueOf(paginaAnterior)));
			nameValuePairs.add(new BasicNameValuePair("proxima", String.valueOf(paginaProxima)));

			String parametros = URLEncodedUtils.format(nameValuePairs, "utf-8");
			String url = String.format("%s/busca?%s", Constantes.URL_SERVIDOR, parametros);
			HttpGet httppost = new HttpGet(url);
			
			Log.d(getClass().getName(), url);
			
			int executeCount = 0;
			HttpResponse response;
			do {
				executeCount++;
				progressDialog.setMessage("Buscando livros...");
				response = client.execute(httppost);
				responseCode = response.getStatusLine().getStatusCode();
				HttpEntity entity = response.getEntity();

				conteudo = EntityUtils.toString(entity, "UTF-8");
			} while (executeCount < 5 && responseCode != 200);
		}
		catch (Exception e) {
			responseCode = 404;
			e.printStackTrace();
			Log.d("http erro", e.toString());
		}
		return responseCode;
	}

	@Override
	protected void onPostExecute(Integer headerCode) {
		progressDialog.dismiss();
		if(headerCode == 200){
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
		else if(headerCode == 404){
			IActivityMostraErro activity = (IActivityMostraErro)contexto;
			activity.mostrarErros("Nenhuma informacao recebida");
		}
	}
}
