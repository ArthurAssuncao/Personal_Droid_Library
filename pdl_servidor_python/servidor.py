#coding: utf-8
#!/usr/bin/env python

from BaseHTTPServer import BaseHTTPRequestHandler, HTTPServer
import urlparse
import json

import busca
import extrato
import verifica_login
import renova
import settings
import cgi
#from cgi import parse_header, parse_multipart, parse_qs

class PdlHTTPRequestHandler(BaseHTTPRequestHandler):
    
    def do_GET(self):
        print 'conectou GET'
        print self.path
        if 'busca?' in self.path:
            self.busca()
        else:
            self.send_response(404)
            self.end_headers()
    
    def do_POST(self):
        print 'conectou POST'
        print self.path
        if '/extrato' == self.path:
            self.extrato()
        elif '/login' == self.path:
            self.login()
        elif '/renova' == self.path:
            self.renova()
        else:
            self.send_response(404)
            self.end_headers()
        return
        
    def busca(self):
        queryString = {}
        self.send_response(200)
        self.send_header('Content-Type', 'aplication/json; charset=utf-8')
        self.end_headers()
        self.path, tmp = self.path.split('?', 1)
        queryString = urlparse.parse_qs(tmp)
        resultado = busca.buscaLivro(queryString['texto'][0], queryString['anterior'][0], queryString['proxima'][0])
        jsonResultado = json.dumps(resultado)
        self.wfile.write('{}\n'.format(jsonResultado))
        #self.wfile.close()
        self.wfile.write('\n')
        return
        
    def extrato(self):
        queryString = self.obter_parametros_post()

        print queryString
        resultado = extrato.buscaExtrato(queryString['matricula'][0], queryString['senha'][0])
        jsonResultado = json.dumps(resultado)
        
        if resultado['erro'] == 'senha_incorreta':
            self.send_response(401)
            self.end_headers()
        else:
            self.send_response(200)
            self.send_header('Content-Type', 'aplication/json; charset=utf-8')
            self.end_headers()
        
            self.wfile.write('{}\n'.format(jsonResultado))
            #self.wfile.close()
            self.wfile.write('\n')
            
    def login(self):
        self.send_response(200)
        self.send_header('Content-Type', 'aplication/json; charset=utf-8')
        self.end_headers()
        queryString = self.obter_parametros_post()

        print queryString
        resultado = verifica_login.verificaLogin(queryString['matricula'][0], queryString['senha'][0])
        jsonResultado = json.dumps(resultado)
        
        if resultado['login'] == 'incorreto':
            print 'login incorreto'
            self.wfile.write('{}\n'.format(jsonResultado))
            self.wfile.write('\n')
            #self.send_response(401)
            #self.end_headers()
        else:
            print 'login correto'
            self.wfile.write('{}\n'.format(jsonResultado))
            self.wfile.write('\n')
            #self.send_response(202)
            #self.end_headers()
    
    def renova(self):
        self.send_response(200)
        self.send_header('Content-Type', 'aplication/json; charset=utf-8')
        self.end_headers()
        queryString = self.obter_parametros_post()
        print queryString
        url = queryString['url'][0]
        resultado = renova.renovaLivro(url)
        if resultado:
            jsonResultado = {'renovacao' : resultado}
        else:
            jsonResultado = {'renovacao' : resultado}
        self.wfile.write('{}\n'.format(jsonResultado))
        #self.wfile.close()
        self.wfile.write('\n')

    def obter_parametros_post(self):
        tipo, pdict = cgi.parse_header(self.headers['content-type'])
        if tipo == 'multipart/form-data':
            postvars = cgi.parse_multipart(self.rfile, pdict)
        elif tipo == 'application/x-www-form-urlencoded':
            tamanho = int(self.headers['content-length'])
            parametros = cgi.parse_qs(self.rfile.read(tamanho), keep_blank_values=1)
        else:
            parametros = {}
        return parametros
    
def main():
    print 'servidor iniciando...'

    server_address = (settings.URL_SERVIDOR, settings.PORTA_SERVIDOR)
    #PdlHTTPRequestHandler.protocol_version = "HTTP/1.1"
    httpd = HTTPServer(server_address, PdlHTTPRequestHandler)
    print 'servidor esta rodando...'
    httpd.serve_forever()
    
if __name__ == '__main__':
    main()
