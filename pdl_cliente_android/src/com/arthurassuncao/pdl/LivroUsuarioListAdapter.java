package com.arthurassuncao.pdl;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LivroUsuarioListAdapter extends BaseAdapter {

	private List<LivroRenovacao> listaLivros;
	private Context contexto;

	public LivroUsuarioListAdapter(Context contexto, List<LivroRenovacao> listaLivros) {
		this.listaLivros = listaLivros;
		this.contexto = contexto;
	}

	public int getCount() {
		return listaLivros.size();
	}

	public LivroRenovacao getItem(int position) {
		if(position < listaLivros.size())
			return listaLivros.get(position);
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View view, ViewGroup group) {
		LivroRenovacao livro = listaLivros.get(position);

		LayoutInflater inflater = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View v = inflater.inflate(R.layout.item_livro, null);

		TextView texto = (TextView) v.findViewById(R.id.item_livro_texto_livro);
		texto.setText(livro.getLivro().getTexto());
		TextView dataDevolucao = (TextView) v.findViewById(R.id.item_livro_texto_data_devolucao);
		dataDevolucao.setText(livro.getDataDevolucao());

		return v;
	}

}