#import Login
from Login import *

class LoginBibliotecaIfet(Login):
    '''
    classdocs
    '''
    IsisScript = ''
    tmp = ''
    
    nomeCampoIsisScript = ''
    nomeCampoTmp = ''

    def __init__(self, usuario='', senha=''):
        '''
        Constructor
        '''
        super(LoginBibliotecaIfet, self).__init__(usuario, senha)
        self.IsisScript = ''
        self.tmp = ''
        
        self.nomeCampoIsisScript = ''
        self.nomeCampoTmp = ''
        
    def setIsisScript(self, IsisScript):
        self.IsisScript = IsisScript
        
    def setTmp(self, tmp):
        self.tmp = tmp
        
    def getIsisScript(self):
        return self.IsisScript
        
    def getTmp(self):
        return self.tmp
    
    def setNomeCampoIsisScript(self, nome):
        self.nomeCampoIsisScript = nome
    
    def setNomeCampoTmp(self, nome):
        self.nomeCampoTmp = nome
        
    def getNomeCampoIsisScript(self):
        return self.nomeCampoIsisScript
    
    def getNomeCampoTmp(self):
        return self.nomeCampoTmp
    
    def toUrlEncode(self, nomeCampoIsisScript = '', nomeCampoTmp = '', nomeCampoLogin = '', nomeCampoSenha = ''):
        if (nomeCampoLogin != ''):
            self.setNomeCampoLogin(nomeCampoLogin)
        if (nomeCampoSenha != ''):
            self.setNomeCampoSenha(nomeCampoSenha)
        if (nomeCampoIsisScript != ''):
            self.setNomeCampoIsisScript(nomeCampoIsisScript)
        if (nomeCampoTmp != ''):
            self.setNomeCampoTmp(nomeCampoTmp)
        return urllib.urlencode({nomeCampoIsisScript : self.getIsisScript(), nomeCampoTmp : self.getTmp(), nomeCampoLogin : self.getUsuario(), nomeCampoSenha : self.getSenha()})
    
    
    
    
