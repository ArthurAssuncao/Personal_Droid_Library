
import urllib;

class Login(object):
    '''
    classdocs
    '''
    
    usuario = '';
    senha = '';
    nomeCampoLogin = 'login';
    nomeCampoSenha = 'pwd';

    def __init__(self, usuario='', senha=''):
        '''
        Constructor
        '''
        self.setUsuario(usuario);
        self.setSenha(senha);
        
        self.nomeCampoLogin = 'login';
        self.nomeCampoSenha = 'pwd';
    
    def setUsuario(self, usuario):
        self.usuario = usuario;
    
    def setSenha(self, senha):
        self.senha = senha;
        
    def getUsuario(self):
        return self.usuario;
    
    def getSenha(self):
        return self.senha;
    
    def setNomeCampoLogin(self, nome):
        self.nomeCampoLogin = nome;
    
    def setNomeCampoSenha(self, nome):
        self.nomeCampoSenha = nome;
        
    def getNomeCampoLogin(self):
        return self.nomeCampoLogin;
    
    def getNomeCampoSenha(self):
        return self.nomeCampoSenha;
    
    def toUrlEncode(self, nomeCampoLogin = '', nomeCampoSenha = ''):
        if (nomeCampoLogin != ''):
            self.setNomeCampoLogin(nomeCampoLogin);
        if (nomeCampoSenha != ''):
            self.setNomeCampoSenha(nomeCampoSenha);
        return urllib.urlencode({nomeCampoLogin : self.getUsuario(), nomeCampoSenha : self.getSenha()});
    
    
    
    
    
    