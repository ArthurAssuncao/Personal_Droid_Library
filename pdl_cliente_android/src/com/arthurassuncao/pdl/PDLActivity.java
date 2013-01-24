package com.arthurassuncao.pdl;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.arthurassuncao.pdl.Preferencia.Preferencias;
import com.arthurassuncao.pdl.persistencia.RepositorioLivroRenovacao;
import com.arthurassuncao.pdl.tarefas.BuscaExtratoUsuario;
import com.arthurassuncao.pdl.tarefas.BuscaLivro;
import com.arthurassuncao.pdl.tarefas.RenovaLivro;

public class PDLActivity extends Activity implements IActivityMostraErro{

	private ListView listaLivros;
	private ListView listaLivrosUsuario;
	private TabHost tabHost;
	private int paginaAtual = 1;
	private int paginaProxima;
	private RepositorioLivroRenovacao repositorioLivroRenovacao;

	private static final int MENU_RENOVAR = 1;
	private static final int MENU_CANCELAR = 2;
	private static final int MENU_RESERVAR = 3;

	public static final int NOTIFICATION_ID = 123456;
	public static final int REQUEST_CODE = 654321;
	
	private String matricula;
	private String senha;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_pdl);

		listaLivros = (ListView)findViewById(R.id.aba_busca_lista_resultados_livros);
		listaLivrosUsuario = (ListView)findViewById(R.id.aba_extrato_lista_livros);

		registerForContextMenu(listaLivrosUsuario);
		registerForContextMenu(listaLivros);

		tabHost = (TabHost)findViewById(R.id.activity_pdl_tabhost);
		tabHost.setup();

		TabSpec spec1 = tabHost.newTabSpec("Busca");
		TabSpec spec2 = tabHost.newTabSpec("Extrato/Renovacao");

		spec1.setIndicator("Busca");
		spec2.setIndicator("Extrato/Renovacao");

		spec1.setContent(R.id.aba_busca);
		spec2.setContent(R.id.aba_extrato);

		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		
		repositorioLivroRenovacao = new RepositorioLivroRenovacao(this);
		Preferencia pref = new Preferencia(this);
		Map<String, String> prefs = pref.getPreferencias();
		matricula = prefs.get(Preferencias.MATRICULA);
		senha = prefs.get(Preferencias.SENHA);

		criarAlarme();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_pdl, menu);
		return true;
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	public void busca(View view){
		EditText campoBusca = (EditText)findViewById(R.id.aba_busca_campo_busca);

		//List<Livro> livros = new ArrayList<Livro>();

		try {
			String textoBusca = campoBusca.getText().toString();
			if(!textoBusca.trim().equals("")){

				ProgressDialog progressDialog = new ProgressDialog(this);
				progressDialog.setMessage("Buscando livros...");
				progressDialog.setCancelable(false);

				BuscaLivro busca = new BuscaLivro(this, progressDialog, listaLivros, 1, 0);
				busca.execute(textoBusca);

				//Conexao.buscaLivros(this, textoBusca, listaLivros, 1, 0);

				paginaAtual = 1;
				paginaProxima = 0;
			}
			else{
				Toast msg = Toast.makeText(this, "Digite algum termo para busca", Toast.LENGTH_SHORT);
				msg.show();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		//listaLivros.setAdapter(new LivroListAdapter(this, livros));
	}

	public void setCamposExtrato(Extrato extrato){
		TextView campoUsuario = (TextView)findViewById(R.id.aba_extrato_campo_usuario);
		TextView campoMatricula = (TextView)findViewById(R.id.aba_extrato_campo_matricula);
		TextView campoDataEmissao = (TextView)findViewById(R.id.aba_extrato_campo_data_emissao);

		campoUsuario.setText(extrato.getUsuario().getNome());
		campoMatricula.setText(extrato.getUsuario().getMatricula());
		campoDataEmissao.setText(extrato.getDataEmissao().replace(".", "/"));

		listaLivrosUsuario.setAdapter(new LivroUsuarioListAdapter(this, extrato.getLivros()));
		
		repositorioLivroRenovacao.deleteAll();
		List<LivroRenovacao> livros = extrato.getLivros();
		for (LivroRenovacao livro: livros){
			repositorioLivroRenovacao.insert(livro);
		}
	}

	public void atualizarExtrato(View componente){
		//Conexao.buscaExtrato(this);
		ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Atualizando extrato...");
		progressDialog.setCancelable(false);
		BuscaExtratoUsuario busca = new BuscaExtratoUsuario(this, progressDialog);
		busca.execute(matricula, senha);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.aba_extrato_lista_livros) {
			menu.add(Menu.NONE, MENU_RENOVAR, 0, "Renovar");
			menu.add(Menu.NONE, MENU_CANCELAR, 1, "Cancelar");
		}
		else if (v.getId() == R.id.aba_busca_lista_resultados_livros) {
			//menu.add(Menu.NONE, MENU_RESERVAR, 0, "Reservar");
			//menu.add(Menu.NONE, MENU_CANCELAR, 1, "Cancelar");
		}
	}

	public void proxima(View view){
		EditText campoBusca = (EditText)findViewById(R.id.aba_busca_campo_busca);
		try {
			String textoBusca = campoBusca.getText().toString();
			if(!textoBusca.trim().equals("")){
				paginaProxima++;
				//Conexao.buscaLivros(this, textoBusca, listaLivros, paginaAtual, paginaProxima);
				ProgressDialog progressDialog = new ProgressDialog(this);
				progressDialog.setMessage("Buscando livros...");
				progressDialog.setCancelable(false);

				BuscaLivro busca = new BuscaLivro(this, progressDialog, listaLivros, paginaAtual, paginaProxima);
				busca.execute(textoBusca);
				paginaAtual++;
			}
			else{
				Toast msg = Toast.makeText(this, "Digite algum termo para busca", Toast.LENGTH_SHORT);
				msg.show();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void anterior(View view){
		if(paginaAtual < 1 && paginaProxima < 1){
			Toast msg = Toast.makeText(this, "Esta é a primeira página", Toast.LENGTH_SHORT);
			msg.show();
			return;
		}
		EditText campoBusca = (EditText)findViewById(R.id.aba_busca_campo_busca);
		try {
			String textoBusca = campoBusca.getText().toString();
			if(!textoBusca.trim().equals("")){
				paginaAtual--;
				paginaProxima--;
				//Conexao.buscaLivros(this, textoBusca, listaLivros, paginaAtual, paginaProxima);
				ProgressDialog progressDialog = new ProgressDialog(this);
				progressDialog.setMessage("Buscando livros...");
				progressDialog.setCancelable(false);

				BuscaLivro busca = new BuscaLivro(this, progressDialog, listaLivros, paginaAtual, paginaProxima);
				busca.execute(textoBusca);
			}
			else{
				Toast msg = Toast.makeText(this, "Digite algum termo para busca", Toast.LENGTH_SHORT);
				msg.show();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		int indiceMenu = item.getItemId();

		LivroRenovacao livro = (LivroRenovacao)listaLivrosUsuario.getItemAtPosition(info.position);
		switch(indiceMenu){
		case MENU_RENOVAR:{
			boolean renovou = true;
			if(renovou){
				//Toast msg = Toast.makeText(this, "Renovação do livro realizada com sucesso", Toast.LENGTH_SHORT);
				//msg.show();
				Log.d(getClass().getName(), livro.getUrlRenovacao());
				//criarNotificacao("renovacao de livro", "o livro dom casmurro deve ser renovado ate amanha", PDLActivity.class);
				ProgressDialog progressDialog = new ProgressDialog(this);
				progressDialog.setMessage("Buscando livros...");
				progressDialog.setCancelable(false);
				RenovaLivro renovar = new RenovaLivro(this, progressDialog, matricula, senha); 
				renovar.execute(livro.getUrlRenovacao());
			}
			else{
				Toast msg = Toast.makeText(this, "Não foi possível renovar o livro", Toast.LENGTH_SHORT);
				msg.show();
			}
			break;
		}
		case MENU_RESERVAR:{
			boolean reservou = true;
			if(reservou){
				Toast msg = Toast.makeText(this, "Reserva realizada com sucesso", Toast.LENGTH_SHORT);
				msg.show();
			}
			else{
				Toast msg = Toast.makeText(this, "Não foi possível reservar o livro", Toast.LENGTH_SHORT);
				msg.show();
			}
			break;
		}
		case MENU_CANCELAR:{
			break;
		}
		}

		return true;
	}

	public void mostrarErros(String msgErro) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setPositiveButton(R.string.botao_ok, new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.setMessage(msgErro);
		AlertDialog alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}
	
	public void mostraMensagem(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	/* Usados metodos depreciados para funcionar na api 8
	 * http://www.androidcompetencycenter.com/2009/01/android-basics-notifications/
	 */
	/*public void criarNotificacao(CharSequence tituloNotificacao, CharSequence msgNotificacao, Class<?> activity) {

		NotificationManager manger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = new Notification(R.drawable.ic_launcher, getResources().getString(R.string.app_name), System.currentTimeMillis());

		// The PendingIntent will launch activity if the user selects this
		// notification
		PendingIntent contentIntent = PendingIntent.getActivity(this,	REQUEST_CODE, new Intent(this, activity), 0);
		notification.setLatestEventInfo(this, tituloNotificacao, msgNotificacao, contentIntent);
		manger.notify(NOTIFICATION_ID, notification);
	}*/

	// ainda nao funciona
	// alarm not found como erro
	public void criarAlarme(){
		AlarmeRenovacao.agendar(this, 5);
	}

}
