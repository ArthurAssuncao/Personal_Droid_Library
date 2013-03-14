
import urllib, urllib2, cookielib

from Pagina import *
from LoginBibliotecaIfet import *
from settings import *
import getpass

if __name__ == '__main__':
    
    
    print "\n\n\t\tCOMECA AKIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII\n\n"
 
    login = LoginBibliotecaIfet('6947', getpass.getpass())
    #login = LoginBibliotecaIfet('1', '1')
    paginaBarraCabecalho = Pagina(URL_BIBLIOTECA_IFET_COMPLETA)
    
    paginaBarraCabecalho.abrirUrl()
    
    urlServicosRenovacaoReservas = paginaBarraCabecalho.getTexto('cabecalho', 'src="', '" target')
    urlResultado = URL_BIBLIOTECA_IFET + paginaBarraCabecalho.getTexto('name="result"', 'src="', '" target')
    urlMenu = URL_BIBLIOTECA_IFET + paginaBarraCabecalho.getTexto('name="menu"', 'src="', '" margin')
    
    #paginaBarraCabecalho.fechaUrl()
    
    paginaLogin = Pagina(urlServicosRenovacaoReservas)

    paginaLogin.abrirUrl()
    urlPaginaLogin = paginaLogin.getTexto('Reservas', 'href="', '" target')

    paginaLogin.fechaUrl()
    
    cookieJar = cookielib.CookieJar()
    opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(cookieJar))
    
    temp = Pagina(urlPaginaLogin)
    temp.abrirUrl()
    urlTemp = temp.getTexto('name="tmp"', 'value="', '">', False)
    urlIsisScript = temp.getTexto('name="IsisScript"', 'value="', '">', False)
    
    login.setNomeCampoIsisScript('IsisScript')
    login.setNomeCampoTmp('tmp')
    login.setIsisScript(urlIsisScript)
    login.setTmp(urlTemp)
    
    dadosLogin = urllib.urlencode({'IsisScript' : urlIsisScript, 'tmp' : urlTemp, 'login' : login.getUsuario(), 'pwd' : login.getSenha()})
    
    resposta = opener.open(URL_BIBLIOTECA_IFET_POST, dadosLogin)
    #resposta = opener.open(URL_BIBLIOTECA_IFET_POST, login.toUrlEncode())
    
    #print resposta.read()
    
    for linha in resposta:
        if linha.find('Extrato') != -1:
            tmp = linha.split('href="', 1)[1]
            urlExtrato = URL_BIBLIOTECA_IFET + tmp.split('" target', 1)[0]

    try:
        extratoRenovacao = opener.open(urlExtrato)
        print extratoRenovacao.read()
    except Exception as e:
        print "Nao Conseguiu Logar no Site"
        print e
    
    exit(0)
    
