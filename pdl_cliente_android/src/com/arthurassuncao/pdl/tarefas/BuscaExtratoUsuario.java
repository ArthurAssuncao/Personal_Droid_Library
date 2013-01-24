package com.arthurassuncao.pdl.tarefas;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.arthurassuncao.pdl.Extrato;
import com.arthurassuncao.pdl.IActivityMostraErro;
import com.arthurassuncao.pdl.PDLActivity;
import com.arthurassuncao.pdl.util.Constantes;

public class BuscaExtratoUsuario extends AsyncTask<String, Void, Integer> {

	private Context contexto;
	private ProgressDialog progressDialog;
	private String conteudo;

	public BuscaExtratoUsuario(Context contexto, ProgressDialog progressDialog) {
		super();
		this.contexto = contexto;
		this.progressDialog = progressDialog;
	}

	@Override
	protected void onPreExecute() {
		progressDialog.show();
	}

	@Override
	protected Integer doInBackground(String... dados) 
	{
		String matricula = dados[0];
		String senha = dados[1];
		int responseCode = 0;
		try 
		{
			HttpClient client = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(String.format("%s/extrato", Constantes.URL_SERVIDOR));

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("matricula", matricula));
			nameValuePairs.add(new BasicNameValuePair("senha", senha));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			int executeCount = 0;
			HttpResponse response;
			do {
				executeCount++;
				progressDialog.setMessage("Buscando Extrato...");
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
			Extrato extrato = null;
			try {
				if(conteudo != null){
					extrato = Extrato.createExtrato(conteudo);
					PDLActivity pdl = (PDLActivity)contexto;
					pdl.setCamposExtrato(extrato);
				}
				else{
					Log.d(getClass().getName(), "Nenhuma informacao recebida");
					IActivityMostraErro activity = (IActivityMostraErro)contexto;
					activity.mostrarErros("Nenhuma informacao recebida");
				}
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
		}
		else if(headerCode == 401){
			Log.d(getClass().getName(), "Login Incorreto");
			IActivityMostraErro activity = (IActivityMostraErro)contexto;
			activity.mostrarErros("Login Incorreto");
		}
		else if(headerCode == 404){
			Log.d(getClass().getName(), "Nenhuma informacao recebida");
			IActivityMostraErro activity = (IActivityMostraErro)contexto;
			activity.mostrarErros("Nenhuma informacao recebida");
		}
	}
}
