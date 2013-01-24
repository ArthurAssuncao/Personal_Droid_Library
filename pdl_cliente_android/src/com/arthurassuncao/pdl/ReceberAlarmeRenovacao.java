package com.arthurassuncao.pdl;

import java.util.Calendar;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.arthurassuncao.pdl.persistencia.RepositorioLivroRenovacao;
import com.arthurassuncao.pdl.util.Constantes;
import com.arthurassuncao.pdl.util.Data;

public class ReceberAlarmeRenovacao extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(getClass().getName(), "Alarme disparado");
		RepositorioLivroRenovacao repositorioLivroRenovacao = new RepositorioLivroRenovacao(context);
		List<LivroRenovacao> livros = repositorioLivroRenovacao.listarLivrosRenovacao();
		if(livros.size() > 0){
			Calendar dataAtual = Calendar.getInstance();
			String erro = "";
			for(LivroRenovacao livro : livros){
				Calendar dataLivro = Calendar.getInstance();
				dataLivro.setTime(new Data(livro.getDataDevolucao()).getDate() );
				if(dataLivro.get(Calendar.DAY_OF_MONTH) - dataAtual.get(Calendar.DAY_OF_MONTH) <= Constantes.DIAS_VERIFICAR_LIVRO ){
					String msg = String.format("Livro %s deve ser renovado", livro.getLivro().getTexto());
					erro += msg + "\n";
					//Toast.makeText(context, String.format("Livro %s deve ser renovado", livro.getLivro().getTexto()), Toast.LENGTH_SHORT).show();
				}
			}
			if(!erro.equals("")){
				//AlarmeRenovacao.criarNotificacao(context, "Renovar Livro", erro, PDLActivity.class);
				AlarmeRenovacao.criarNotificacao(context, "Renovar Livro", "Há livros para renovar", PDLActivity.class);
			}
		}
	}
}
