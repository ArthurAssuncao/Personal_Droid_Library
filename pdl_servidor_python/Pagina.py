
import urllib
from Constantes import URL_BIBLIOTECA_IFET

class Pagina(object):
    '''
    classdocs
    '''
    endereco = ''
    conteudo = ''
    texto = ''
    
    def __init__(self, endereco=''):
        '''
        Constructor
        '''
        self.setEndereco(endereco)
        self.conteudo = None

    """def __del__(self):
        self.conteudo.close()
    """
    
    def setEndereco(self, endereco):
        if endereco.find('http://') == -1:
            if endereco.find('https://') == -1:
                self.endereco = 'http://' + endereco
            else:
                self.endereco = endereco
        else:
            self.endereco = endereco
            
    def getTexto(self):
        return self.texto
    
    def abrirUrl(self):
        if self.endereco != '' and (self.endereco.find('http://') != -1 or self.endereco.find('https://') != -1):
            self.conteudo = urllib.urlopen(self.endereco)
            if (self.conteudo != None):
                self.texto = self.conteudo.read()
            return True
        return False
    
    """retorna primeira ocorrencia difinida por inicio e fim no conteudo"""
    '''def getTexto(self, chave, inicio, fim, incluiDominio=True): 
        for linha in self.texto.split('\n'):
            if linha.find(chave) != -1:
                tmp = linha.split(inicio, 1)[1]
                if incluiDominio == True:
                    texto = URL_BIBLIOTECA_IFET + tmp.split(fim, 1)[0]
                else:
                    texto = tmp.split(fim, 1)[0]
                return texto
        return ""
        '''
        
        
    
