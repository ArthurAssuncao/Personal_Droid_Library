package com.arthurassuncao.pdl.util;

public abstract class Constantes {
	//.decode('ISO-8859-1').encode('UTF-8') converte string
	//public static String URL_SERVIDOR = "http://10.3.1.20:8888";
	public static String URL_SERVIDOR = "http://192.168.2.25:8888";
	public static final int TIMEOUT = 3000;
	public static final int TEMPO_VERIFICAR_DATA_DEVOLUCAO = 60*6; //minutos
	public static final int DIAS_VERIFICAR_LIVRO = 7;

	public static final String URL_BIBLIOTECA_IFET = "http://ifsmg.phlweb.com.br";
	//public static final String URL_BIBLIOTECA_IFET_COMPLETA = "/cgi-bin/wxis.exe?IsisScript=phl82.xis&cipar=phl82.cip&lang=por";
	public static final String URL_BIBLIOTECA_IFET_COMPLETA = "/cgi-bin/wxis.exe";
	public static final String URL_BIBLIOTECA_IFET_POST = "/cgi-bin/wxis.exe";

	public static final String IMG_STATUS_DISPONIVEL = "img/002.gif";
	public static final String IMG_STATUS_INDISPONIVEL = "img/003.gif";
	public static final String IMG_STATUS_RESERVADO = "img/016.gif";
	public static final String IMG_STATUS_CONSULTA_LOCAL = "img/004.gif";
	public static final String IMG_STATUS_EXTRAVIADO = "img/005.gif";
}
