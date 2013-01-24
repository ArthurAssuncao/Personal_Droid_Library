package com.arthurassuncao.pdl;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.arthurassuncao.pdl.util.Constantes;

public class AlarmeRenovacao{
	private static final int codigoRequerimento = 614574984;
	private static final int tempoRepetir = Constantes.TEMPO_VERIFICAR_DATA_DEVOLUCAO * 60 * 1000; //N segundos
	
	//segundos == tempo para iniciar na primeira vez
	public static void agendar(Context contexto, int segundos){
		// Intent para disparar o BroadcastReceiver = ReceberAlarmeRenovacao
		Intent intent = new Intent(contexto, ReceberAlarmeRenovacao.class);
		PendingIntent p = PendingIntent.getBroadcast(contexto, codigoRequerimento, intent, 0);
		
		// para executar o alarme em N segundos a partir de agora
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.SECOND, segundos);
		
		// Agenda alarme
		AlarmManager alarme = (AlarmManager)contexto.getSystemService(Activity.ALARM_SERVICE);
		long time = calendar.getTimeInMillis();
		
		// repetir a cada N tempo
		alarme.setRepeating(AlarmManager.RTC_WAKEUP, time, tempoRepetir, p);
		//alarme.setInexactRepeating(AlarmManager.RTC_WAKEUP, time, tempoRepetir, p);
		Log.d(AlarmeRenovacao.class.getName(), String.format("Alarme agendado para daqui a %d segundos. Repetir a cada %d segundos", segundos, tempoRepetir/1000));
		
	}
	
	public static void cancelaAlarme(Context contexto){
		Intent intent = new Intent("EXECUTAR_ALARME_RENOVACAO");
		PendingIntent p = PendingIntent.getBroadcast(contexto, 0, intent, 0);
		
		AlarmManager alarme = (AlarmManager)contexto.getSystemService(Activity.ALARM_SERVICE);
		alarme.cancel(p);
		
	}
	
	/* Usados metodos depreciados para funcionar na api 8
	 * http://www.androidcompetencycenter.com/2009/01/android-basics-notifications/
	 */
	public static void criarNotificacao(Context contexto, CharSequence tituloNotificacao, CharSequence msgNotificacao, Class<?> activity) {

		NotificationManager manger = (NotificationManager) contexto.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = new Notification(R.drawable.ic_launcher, contexto.getResources().getString(R.string.app_name), System.currentTimeMillis());

		// The PendingIntent will launch activity if the user selects this
		// notification
		PendingIntent contentIntent = PendingIntent.getActivity(contexto, PDLActivity.REQUEST_CODE, new Intent(contexto, activity), 0);
		notification.setLatestEventInfo(contexto, tituloNotificacao, msgNotificacao, contentIntent);
		manger.notify(PDLActivity.NOTIFICATION_ID, notification);
	}
	
	
}
