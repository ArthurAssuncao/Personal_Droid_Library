package com.arthurassuncao.pdl;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LivroListAdapter extends BaseAdapter {

	private List<Livro> listaLivros;
	private Context contexto;

	public LivroListAdapter(Context contexto, List<Livro> listaLivros) {
		this.listaLivros = listaLivros;
		this.contexto = contexto;
	}

	public int getCount() {
		return listaLivros.size();
	}

	public Livro getItem(int position) {
		return listaLivros.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View view, ViewGroup group) {
		Livro livro = listaLivros.get(position);

		LayoutInflater inflater = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View v = inflater.inflate(R.layout.item_livro_busca, null);

		TextView texto = (TextView) v.findViewById(R.id.item_livro_busca_texto_livro);
		texto.setText(livro.getTexto());
		ImageView imagem = (ImageView)v.findViewById(R.id.item_livro_busca_imagem_status);
		int idImagem = 0;
		if(livro.getStatus().equalsIgnoreCase("disponivel")){
			idImagem = R.drawable.disponivel;
		}
		else if(livro.getStatus().equalsIgnoreCase("indisponivel")){
			idImagem = R.drawable.indisponivel;
		}
		else if(livro.getStatus().equalsIgnoreCase("reservado")){
			idImagem = R.drawable.reservado;
		}
		else if(livro.getStatus().equalsIgnoreCase("consulta_local")){
			idImagem = R.drawable.consulta_local;
		}
		else if(livro.getStatus().equalsIgnoreCase("extraviado")){
			idImagem = R.drawable.extraviado;
		}
		else{
			idImagem = R.drawable.indisponivel;
		}
		
		imagem.setImageDrawable(contexto.getResources().getDrawable(idImagem));

		return v;
	}

}