#coding: utf-8

import urllib, urllib2, cookielib

from Pagina import *
from Constantes import *
import re
from util import *

def extraiDadosLivro(htmlLivro):
    regexStatus = re.compile(r'Selecionar.*?<img src="/?.*?/(.*?)"')
    try:
        status = re.findall(regexStatus, htmlLivro)[0]
    except IndexError:
        status = None
    
    livro = {}
    #verifica estado do livro
    if status == IMG_STATUS_DISPONIVEL:
        livro['status'] = 'disponivel'
    elif status == IMG_STATUS_INDISPONIVEL:
        livro['status'] = 'indisponivel'
    elif status == IMG_STATUS_RESERVADO:
        livro['status'] = 'reservado'
    elif status == IMG_STATUS_CONSULTA_LOCAL:
        livro['status'] = 'consulta_local'
    elif status == IMG_STATUS_EXTRAVIADO:
        livro['status'] = 'extraviado'
    else:
        livro['status'] = 'nao fornecido'
    
    regexTabelaDadosLivro = re.compile(r'<table.*?>.*?</table>.*?<table.*?>.*?</table>.*?<table.*?>(.*?)</table>', re.DOTALL)
    tabelaDadosLivro = re.findall(regexTabelaDadosLivro, htmlLivro)[0]
    
    regexDadosLivros = re.compile(r'<td.*?>.*?</td><td.*?>(.*?)Palavra', re.DOTALL)
    try:
        dadosLivros = re.findall(regexDadosLivros, tabelaDadosLivro)[0]
    except IndexError:
        regexDadosLivros = re.compile(r'<td.*?>.*?</td><td.*?>(.*?)</td>', re.DOTALL)
        dadosLivros = re.findall(regexDadosLivros, tabelaDadosLivro)[0]
    
    dadosLivro = remove_tags(dadosLivros)
    
    livro['dados'] = dadosLivro.decode('ISO-8859-1').encode('UTF-8')
    
    return livro

def getHTMLResultado(texto):
    regexHTML = re.compile('<!-- FMX000093 -->.*?((<table .*?>.*?</table>){3})', re.DOTALL)
    resultado = re.findall(regexHTML, texto)
    resultados = []
    for r in resultado:
        resultados.append(r[0])
    return resultados

def buscaLivro(textoBusca, paginaAtual=1, proximaPagina=1):
    print "\n\n\t\tBusca\n\n"
    
    #textoBusca = raw_input('Digite um texto para buscar: ')
    #textoBusca = 'Usando linux'
    #textoBusca = 'aaaaaaaaaaaaaaaaaaaaaaa' #nao encontrado
    
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
    
    regexUrlPaginaBuscas = re.compile(r'href="(.*?)".*Buscas')
    urlPaginaBuscas = URL_BIBLIOTECA_IFET + re.findall(regexUrlPaginaBuscas, textoPaginaLogin)[0]
    
    temp = Pagina(urlPaginaBuscas)
    temp.abrirUrl()
    textoTemp = temp.getTexto()
    
    regexUrlTemp = re.compile(r'name="tmp" value="(.+?)"')
    urlTemp = re.findall(regexUrlTemp, textoTemp)[0]
    regexUrlIsisScript = re.compile(r'name="IsisScript" value="(.+?)"')
    urlIsisScript = re.findall(regexUrlIsisScript, textoTemp)[0]
    regexAnterior = re.compile(r'type="hidden" name="ante" value="(.*)"')
    regexProxima = re.compile(r'type="hidden" name="prox" value="(.*)"')
    regexUltima = re.compile(r'type="hidden" name="ulti" value="(.*)"')
    try:
        anterior = re.findall(regexAnterior, textoTemp)[0]
        proxima = re.findall(regexProxima, textoTemp)[0]
        ultima = re.findall(regexUltima, textoTemp)[0]
    except:
        anterior = ''
        proxima = ''
        ultima = ''
    anterior = paginaAtual
    proxima = int(proximaPagina) * NUMERO_RESULTADOS_PAGINA + 1
    ultima = '100000'
    #exp == campo texto

    tipoPesquisa = 'and' #and = Todas palavras; or = qualquer palavra; frase = frase exata/ exp = expressao
    dadosBusca = urllib.urlencode({'tmp' : urlTemp, 'exp' : textoBusca, 'bool' : tipoPesquisa, 'IsisScript' : urlIsisScript, 'acv' : '', 'cnt' : '', 'col' : '', 'idm' : '', 'ptf' : 'decorado', 'rpp' : str(NUMERO_RESULTADOS_PAGINA), 'spt' : '', 'stf' : '', 'tag' : '', 'tip' : '', 'idx' : '', 'ante' : anterior, 'prox' : proxima, 'ulti' : ultima, 'opc.x' : 378, 'opc.y' : 17})
    
    #voltar = 'opc.x' : 376, 'opc.y' : 12
    #proxima = 'opc.x' : 378, 'opc.y' : 17
    
    cookieJar = cookielib.CookieJar()
    opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(cookieJar))
    
    respostaBusca = opener.open(URL_BIBLIOTECA_IFET_POST, dadosBusca)
    textoRespostaBusca = respostaBusca.read()#.decode('ISO-8859-1').encode('UTF-8')
    #print textoRespostaBusca
    
    htmlResultado = getHTMLResultado(textoRespostaBusca)
    listaJson = []
    for x in htmlResultado:
        dados = extraiDadosLivro(x)
        listaJson.append(dados)
        print dados
        print ''
        
    return listaJson
    
    
if __name__ == '__main__':
    buscaLivro('linux', 1, 1)
