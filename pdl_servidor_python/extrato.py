#coding: utf-8

import urllib, urllib2, cookielib

from Pagina import *
from LoginBibliotecaIfet import *
from settings import *
import re
import getpass

def extraiExtrato(texto):
    nome = ''
    matricula = ''
    dataEmissao = ''
    regexNome = r'Nome: (.+?)<br>'
    regexMatricula = r'Matrícula: (.+?)<br>' # \xef\xbf\xbd == í
    regexDataEmissao = r'Emitido em: (.+?)<br>'
    nome = re.findall(regexNome, texto)[0]
    matricula = re.findall(regexMatricula, texto)[0]
    dataEmissao = re.findall(regexDataEmissao, texto)[0]
   
    dados = {'erro' : 'nenhum', 'nome': nome, 'matricula': matricula, 'data_emissao': dataEmissao}
    regexLivros = re.compile(r'href="(/cgi-bin/wxis\.exe.*)".+ Devolver em: (.+?)</td>')#.*<td width="95%" align="left">(.*?)</td>')
    regexRenovacoes = re.compile(r'href="(/cgi-bin/wxis\.exe.*)"')
    regexDatasDevolucao = re.compile(r'Devolver em: (.+?)</td>')
    regexLivros = re.compile(r'''<td wdith="5%"></td><td width="95%" align="left">
(.*?)
</td>''')

    renovacoes = re.findall(regexRenovacoes, texto)
    datasDevolucao = re.findall(regexDatasDevolucao, texto)
    livros = re.findall(regexLivros, texto)
    
    dados['livros'] = []

    for r, d, l in map(None, renovacoes, datasDevolucao, livros):
        dados['livros'].append({'livro': l, 'link_renovacao' : r, 'data_devolucao' : d})

    return dados

def buscaExtrato(matricula, senha):
    print "\n\n\t\tExtrato\n\n"

    #login = LoginBibliotecaIfet('6947', getpass.getpass())
    login = LoginBibliotecaIfet(matricula, senha)
    #login = LoginBibliotecaIfet('1', '1')
    paginaBarraCabecalho = Pagina(URL_BIBLIOTECA_IFET_COMPLETA)
    
    paginaBarraCabecalho.abrirUrl()

    textoBarraCabecalho = paginaBarraCabecalho.getTexto()
    regexUrlRenovacao = re.compile(r'name="cabecalho" .*src="(.*?)"')
    #regexUrlResultado = re.compile(r'name="result" .*src="(.*?)"')
    #regexUrlMenu = re.compile(r'name="menu" .*src="(.*?)"')
    try:
        urlServicosRenovacaoReservas = URL_BIBLIOTECA_IFET + re.findall(regexUrlRenovacao, textoBarraCabecalho)[0]
        #urlResultado = URL_BIBLIOTECA_IFET + re.findall(regexUrlResultado, textoBarraCabecalho)[0]
        #urlMenu = URL_BIBLIOTECA_IFET + re.findall(regexUrlMenu, textoBarraCabecalho)[0]
    except IndexError as e:
        print e
    
    paginaLogin = Pagina(urlServicosRenovacaoReservas)
    paginaLogin.abrirUrl()
    textoPaginaLogin = paginaLogin.getTexto()
    
    #regexUrlPaginaBuscas = re.compile(r'href="(.*?)".*Buscas')
    #urlPaginaBuscas = URL_BIBLIOTECA_IFET + re.findall(regexUrlPaginaBuscas, textoPaginaLogin)[0]
    
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
    except IndexError as e:
        regexSenhaIncorreta = re.compile(r'<h2>(.*)</h2>')
        textoSenhaIncorreta = re.findall(regexSenhaIncorreta, textoRespostaMenu)[0]
        print textoSenhaIncorreta
        if textoSenhaIncorreta.decode('ISO-8859-1').encode('UTF-8') == 'Senha Inválida':
            json_erro = {'erro' : 'senha_incorreta'}
            return json_erro
    
    extratoRenovacao = opener.open(urlExtrato)
    textoExtratoRenovacao = extratoRenovacao.read().decode('ISO-8859-1').encode('UTF-8')
    extrato = extraiExtrato(textoExtratoRenovacao)
    
    print extrato
    return extrato

if __name__ == '__main__':
    buscaExtrato('123', '123asd')
