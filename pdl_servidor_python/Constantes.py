#.decode('ISO-8859-1').encode('UTF-8') converte string

#URL_SERVIDOR = '10.3.1.20'
URL_SERVIDOR = '192.168.2.100'
PORTA_SERVIDOR = 8888

URL_BIBLIOTECA_IFET = 'http://ifsmg.phlweb.com.br'
URL_BIBLIOTECA_IFET_COMPLETA = URL_BIBLIOTECA_IFET + '/cgi-bin/wxis.exe?IsisScript=phl82.xis&cipar=phl82.cip&lang=por'
URL_BIBLIOTECA_IFET_POST = URL_BIBLIOTECA_IFET + '/cgi-bin/wxis.exe'

IMG_STATUS_DISPONIVEL = 'img/002.gif'
IMG_STATUS_INDISPONIVEL = 'img/003.gif'
IMG_STATUS_RESERVADO = 'img/016.gif'
IMG_STATUS_CONSULTA_LOCAL = 'img/004.gif'
IMG_STATUS_EXTRAVIADO = 'img/005.gif'

NUMERO_RESULTADOS_PAGINA = 10 #padrao == 75

#expressoes regulares
import re
TAG_RE = re.compile(r'<[^>]+>')

