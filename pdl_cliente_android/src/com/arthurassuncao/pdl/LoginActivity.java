package com.arthurassuncao.pdl;

import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.arthurassuncao.pdl.Preferencia.Preferencias;
import com.arthurassuncao.pdl.tarefas.LoginUsuario;
import com.arthurassuncao.pdl.util.Conexao;

public class LoginActivity extends Activity implements IActivityMostraErro{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		if(!Conexao.verificaConexao(this)){
			Toast msg = Toast.makeText(this, "Sem conexao com a internet", Toast.LENGTH_SHORT);
			msg.show();
		}
		Map<String, String> prefs = new Preferencia(this).getPreferencias();
		if(prefs != null){
			String matricula = prefs.get(Preferencias.MATRICULA);
			String senha = prefs.get(Preferencias.SENHA);
			if(matricula != null && senha != null){
				abrePrograma(matricula, senha);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	public void entrar(View view){      
		String matricula = ((EditText)findViewById(R.id.login_campo_numero_carteirinha)).getText().toString();
		String senha = ((EditText)findViewById(R.id.login_campo_senha)).getText().toString();

		String erro = "";
		if(matricula.trim().equals("")){
			erro += "Digite o numero da sua carterinha da biblioteca\n";
		}
		if(senha.trim().equals("")){
			erro += "Digite sua senha";
		}
		if(!erro.equals("")){
			Toast msgErro = Toast.makeText(this, erro, Toast.LENGTH_SHORT);
			msgErro.show();
		}
		else{
			if(Conexao.verificaConexao(this)){
				ProgressDialog progressDialog = new ProgressDialog(this);
				progressDialog.setMessage("Verificando Login...");
				progressDialog.setCancelable(false);
	
				LoginUsuario loginUsuario = new LoginUsuario(this, progressDialog);
				loginUsuario.execute(matricula, senha);
			}
			else{
				Toast msg = Toast.makeText(this, "Sem conexao com a internet", Toast.LENGTH_SHORT);
				msg.show();
			}
		}
	}

	public void abrePrograma(String matricula, String senha){
		Preferencia prefs = new Preferencia(this);
		prefs.addPreferencia(Preferencias.MATRICULA, matricula);
		prefs.addPreferencia(Preferencias.SENHA, senha);
		Intent intent = new Intent("PRINCIPAL");
		startActivity(intent);
		finish();
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

}
