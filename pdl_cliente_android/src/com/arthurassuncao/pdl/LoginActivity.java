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

	private EditText campoNumeroCarteirinha;
	private EditText campoSenha;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		campoNumeroCarteirinha = (EditText)findViewById(R.id.login_campo_numero_carteirinha);
		campoSenha = (EditText)findViewById(R.id.login_campo_senha);
		
		if(!Conexao.verificaConexao(this)){
			Toast msg = Toast.makeText(this, getResources().getString(R.string.activity_login_msg_sem_conexao), Toast.LENGTH_SHORT);
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

	public void login(View view){
		EditText campoNumeroCarteirinha = (EditText)findViewById(R.id.login_campo_numero_carteirinha);
		EditText campoSenha = (EditText)findViewById(R.id.login_campo_senha);
		String matricula = campoNumeroCarteirinha.getText().toString();
		String senha = campoSenha.getText().toString();

		
		if(verificaCampos()){
			if(Conexao.verificaConexao(this)){
				ProgressDialog progressDialog = new ProgressDialog(this);
				progressDialog.setMessage(getResources().getString(R.string.activity_login_msg_verificando_login));
				progressDialog.setCancelable(false);
	
				LoginUsuario loginUsuario = new LoginUsuario(this, progressDialog);
				loginUsuario.execute(matricula, senha);
			}
			else{
				Toast msg = Toast.makeText(this, getResources().getString(R.string.activity_login_msg_sem_conexao), Toast.LENGTH_SHORT);
				msg.show();
			}
		}
	}
	
	public void sair(View view){
		finish();
	}
	
	private boolean verificaCampos(){
		String matricula = campoNumeroCarteirinha.getText().toString();
		String senha = campoSenha.getText().toString();
		
		boolean erro = false;
		if(matricula.trim().equals("")){
			campoNumeroCarteirinha.setError(getResources().getString(R.string.activity_login_msg_digite_numero_carteirinha));
			erro = true;
		}
		else{
			campoNumeroCarteirinha.setError(null);
		}
		if(senha.trim().equals("")){
			campoSenha.setError(getResources().getString(R.string.activity_login_msg_digite_senha));
			erro = true;
		}
		else{
			campoSenha.setError(null);
		}
		return !erro;
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
