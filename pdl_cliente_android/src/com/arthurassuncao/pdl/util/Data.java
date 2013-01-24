package com.arthurassuncao.pdl.util;

import java.util.Calendar;
import java.util.Date;

public class Data {
	
	public Date date;
	
	public Data() {
		date = new Date(Calendar.getInstance().getTimeInMillis());
	}
	
	public Data(String data){
		this(data, "##/##/####");
	}
	
	public Data(String data, String formato){
		if(formato.equals("##-##-##")){
			String dia = data.substring(0, 2);
			String mes = data.substring(3, 5);
			String ano = data.substring(6, 8);
			Calendar c = Calendar.getInstance();
			c.set(Calendar.DAY_OF_MONTH, Integer.valueOf(dia));
			c.set(Calendar.MONTH, Integer.valueOf(mes) - 1);
			c.set(Calendar.YEAR, Integer.valueOf(20+ano));
			date = new Date(c.getTimeInMillis());
		}
		else if(formato.equals("##/##/####")){
			String dia = data.substring(0, 2);
			String mes = data.substring(3, 5);
			String ano = data.substring(6, 10);
			Calendar c = Calendar.getInstance();
			c.set(Calendar.DAY_OF_MONTH, Integer.valueOf(dia));
			c.set(Calendar.MONTH, Integer.valueOf(mes) - 1);
			c.set(Calendar.YEAR, Integer.valueOf(ano));
			date = new Date(c.getTimeInMillis());
		}
		else{
			throw new IllegalArgumentException("Formato invalido");
		}
	}
	
	public String getDataString(){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dia = c.get(Calendar.DAY_OF_MONTH);
		int mes = c.get(Calendar.MONTH) + 1;
		int ano = c.get(Calendar.YEAR);
		
		String sDia = dia < 10 ? "0" + dia : dia + "";
		String sMes = mes < 10 ? "0" + mes : mes + "";
		String sAno = String.valueOf(ano);
		
		return String.format("%s/%s/%s", sDia, sMes, sAno);
	}
	
	public Date getDate(){
		return date;
	}
	
	@Override
	public String toString() {
		return this.getDataString();
	}
}
