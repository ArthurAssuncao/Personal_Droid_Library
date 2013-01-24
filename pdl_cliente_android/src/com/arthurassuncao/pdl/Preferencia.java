package com.arthurassuncao.pdl;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferencia {
	public static final String NOME_PREFERENCIAS = "PDL_Preferencias";
	private SharedPreferences settings;
	private Context contexto;

	public Preferencia(Context contexto) {
		//Restaura as preferencias gravadas
		settings = contexto.getSharedPreferences(NOME_PREFERENCIAS, 0);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getPreferencias(){
		if(settings != null){
			return (Map<String, String>)settings.getAll();
		}
		return null;
	}

	public void addPreferencia(String nome, String valor){
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(nome, valor);

		//Confirma a gravação dos dados
		editor.commit();
	}

	public static class Preferencias{
		public static final String MATRICULA = "matricula";
		public static final String SENHA = "senha";
	}
}
