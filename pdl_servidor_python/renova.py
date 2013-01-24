#coding: utf-8

from Pagina import *
import extrato
import json
import urlparse
import Constantes
import cookielib
import urllib
import urllib2
import re

def renovaLivro(url):
    pagina = Pagina(Constantes.URL_BIBLIOTECA_IFET + url)
    pagina.abrirUrl()
    texto = pagina.getTexto()
    print texto
    regexEmprestimoJaFeito = re.compile(r'<h2>(.*?)</h2>')
    try:
        empresitmoJaFeito = re.findall(regexEmprestimoJaFeito, texto)[0].decode('ISO-8859-1').encode('UTF-8')
        if(empresitmoJaFeito == 'Empréstimo ou renovação efetuada no dia de hoje!'):
            print 'emprestimo ja feito hoje'
            return False
            #ja feito
        else:
            #nao feito
            pass
    except:
        pass
        #nao feito hoje
        
    
    #Empréstimo ou renovação efetuada no dia de hoje!
    return True
    
if __name__=='__main__':
    renovaLivro('/cgi-bin/wxis.exe?IsisScript=phl82/037.xis&mfn=38026&acv=001&tmp=/tmp/filelSdrYG')
    

"""def renovaLivro(matricula, senha, posicao, url):  
    path, tmp = url.split('?', 1)
    queryString = urlparse.parse_qs(tmp)
    print queryString
    
    cookieJar = cookielib.CookieJar()
    opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(cookieJar))
    
    dadosLogin = urllib.urlencode({'IsisScript' : queryString['IsisScript'][0], 'tmp' : queryString['tmp'][0], 'login' : matricula, 'pwd' : senha})
    
    respostaMenu = opener.open(Constantes.URL_BIBLIOTECA_IFET_POST, dadosLogin)
    textoRespostaMenu = respostaMenu.read()
    
    print 'AKI'
    print textoRespostaMenu
    print 'ATE AKI'
    
    regexUrlExtrato = re.compile(r'href="(.*?)".*Extrato')
    try:
        urlExtrato = Constantes.URL_BIBLIOTECA_IFET + re.findall(regexUrlExtrato, textoRespostaMenu)[0]
    except IndexError as e:
        regexSenhaIncorreta = re.compile(r'<h2>(.*)</h2>')
        textoSenhaIncorreta = re.findall(regexSenhaIncorreta, textoRespostaMenu)[0]
        print textoSenhaIncorreta
        if textoSenhaIncorreta.decode('ISO-8859-1').encode('UTF-8') == 'Senha Inválida':
            json_erro = {'erro' : 'senha_incorreta'}
            return json_erro

    extratoRenovacao = opener.open(urlExtrato)
    textoExtratoRenovacao = extratoRenovacao.read().decode('ISO-8859-1').encode('UTF-8')
    json_extrato = extrato.extraiExtrato(textoExtratoRenovacao)

    extrato = json.loads(json_extrato)
    link = extrato['livros'][posicao]['link_renovacao']
    
    dadosLogin = urllib.urlencode({'IsisScript' : queryString['IsisScript'][0], 'tmp' : queryString['tmp'][0], 'login' : login.getUsuario(), 'pwd' : login.getSenha()})
    
    dados = queryString
    
    opener.open(Constantes.URL_BIBLIOTECA_IFET_POST, dados)
    #opener.open(Constantes.URL_BIBLIOTECA_IFET + url)
    print Constantes.URL_BIBLIOTECA_IFET + url
    
    return True"""
    
