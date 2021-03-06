#coding: utf-8

import urllib, urllib2, cookielib

from Pagina import *
from LoginBibliotecaIfet import *
from settings import *
import re
import getpass

def verificaLogin(matricula, senha):
    print "\n\n\t\tLogin\n\n"

    login = LoginBibliotecaIfet(matricula, senha)
    paginaBarraCabecalho = Pagina(URL_BIBLIOTECA_IFET_COMPLETA)
    
    paginaBarraCabecalho.abrirUrl()

    textoBarraCabecalho = paginaBarraCabecalho.getTexto()
    regexUrlRenovacao = re.compile(r'name="cabecalho" .*src="(.*?)"')
    try:
        urlServicosRenovacaoReservas = URL_BIBLIOTECA_IFET + re.findall(regexUrlRenovacao, textoBarraCabecalho)[0]
    except IndexError as e:
        print e
    
    paginaLogin = Pagina(urlServicosRenovacaoReservas)
    paginaLogin.abrirUrl()
    textoPaginaLogin = paginaLogin.getTexto()
    
    regexUrlPaginaLogin = re.compile(r'href="(.*?)".*Servi')
    urlPaginaLogin = URL_BIBLIOTECA_IFET + re.findall(regexUrlPaginaLogin, textoPaginaLogin)[0]
    
    cookieJar = cookielib.CookieJar()
    opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(cookieJar))
    
    temp = Pagina(urlPaginaLogin)
    temp.abrirUrl()
    textoTemp = temp.getTexto()
    
    regexUrlTemp = re.compile(r'name="tmp" value="(.+?)"')
    urlTemp = re.findall(regexUrlTemp, textoTemp)[0]
    regexUrlIsisScript = re.compile(r'name="IsisScript" value="(.+?)"')
    urlIsisScript = re.findall(regexUrlIsisScript, textoTemp)[0]
    
    login.setNomeCampoIsisScript('IsisScript')
    login.setNomeCampoTmp('tmp')
    login.setIsisScript(urlIsisScript)
    login.setTmp(urlTemp)
    
    dadosLogin = urllib.urlencode({'IsisScript' : urlIsisScript, 'tmp' : urlTemp, 'login' : login.getUsuario(), 'pwd' : login.getSenha()})
    
    respostaMenu = opener.open(URL_BIBLIOTECA_IFET_POST, dadosLogin)
    textoRespostaMenu = respostaMenu.read()

    regexUrlExtrato = re.compile(r'href="(.*?)".*Extrato')
    try:
        urlExtrato = URL_BIBLIOTECA_IFET + re.findall(regexUrlExtrato, textoRespostaMenu)[0]
        json_login = {'login' : 'correto'}
    except IndexError as e:
        regexSenhaIncorreta = re.compile(r'<h2>(.*)</h2>')
        textoSenhaIncorreta = re.findall(regexSenhaIncorreta, textoRespostaMenu)[0]

        if textoSenhaIncorreta.decode('ISO-8859-1').encode('UTF-8') == 'Senha Inválida':
            json_login = {'login' : 'incorreto'}
    
    return json_login

if __name__ == '__main__':
    verificaLogin('123', '123asd')
