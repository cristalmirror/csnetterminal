#include<iostream>
#include<string>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h> 
#include <sys/socket.h>
#include <netdb.h>
#include <netinet/in.h>
#include <sys/time.h>
#include <errno.h>
#include <arpa/inet.h>
#include <stdbool.h>
/*numero de puerto y de
  clientes maximos conectados*/
#define PORT 8080
#define BACKLOG 10

using namespace std;


//clase del objeto
class ServerClient {
private:
  
  /*estos atributos resiven los parametros de la funcion main*/
  char *opt, *ip;

  int client();
  int server();
public:
  //constructor 
  ServerClient(char **opc,char **ip);
  void printer();
  
  
};

/*se cargan los atributos con los
  datos de los cargurmentos argv*/
ServerClient::ServerClient(char **opc,char **ip) {
  this->opt = *opc;
  this->ip= *ip;
 

  if (strcmp(this->opt,"-s") == 0) {
    server();
  } else  if (strcmp(this->opt,"-c") == 0) {
    client();
  } else {

    cout << "[ERROR]" << endl << "argv not valid or not correct input "<< endl << endl;
    printer();
  }
}

/*imprimen los atributos*/
int ServerClient::client() {

  /*reseptores y transmisores de informacion*/
  char buffer_tx[4096],buffer_rx[50000];

  /*definicion del socket*/
  int sockdf;
  struct sockaddr_in servaddr;

  /*creacion del socket*/
  sockdf = socket(AF_INET,SOCK_STREAM,0);
  if (sockdf == -1) {/*determina si ocurio un error o se creo correctamente*/

    cout << stderr << "***[CLIENT ERROR]***" << endl <<"socket creation failed. "<< errno << strerror(errno) << endl <<endl;
    
    return -1;
  } else {

    cout << "***[CLIENT]***"<< endl <<" socket client are created " << endl << endl;

  }

  memset(&servaddr,0,sizeof(servaddr));

  /*asignacion de ip y puerto*/
  servaddr.sin_family = AF_INET;
  servaddr.sin_addr.s_addr = inet_addr(this->ip);
  servaddr.sin_port = htons(PORT);

  /*instenta estableser
    la coneccion con el servidor*/
  if (connect(sockdf,(struct sockaddr*)&servaddr,sizeof(servaddr)) != 0) {

    cout << "+++[CLIENT ERROR]+++" << endl << "connection with the server isn't posible" << endl << endl;
    return -1;
  }

  cout << "+++[CLIENT]+++" << endl << "conection successfully created" << endl << endl;

  char option_char;
  
  /*envio y resepcion de mensajes al servidor*/
  do {

    cout << "~~~[CLIENT]~~~"<< endl << "send a comand ==>>" ;
    cin.getline(buffer_tx,1024);
    
    write(sockdf,buffer_tx,sizeof(buffer_tx));
    read(sockdf,buffer_rx,sizeof(buffer_rx));
    cout << "###[CLIENT]###" << endl << "Received" << endl << endl;

    cout << "^^^[CLIENT]^^^"<< endl << "send new comand: S/n? >>" << "\n"; cin>>option_char;
 
  
  } while (option_char == 's');

  close(sockdf);
  return 0;
}

int ServerClient::server() {

  /*escucha del socket yel descriptor de archivos
    de la coneccion de sockets*/
  int sockfd, connfd;
  /*tamaño de la direccion del cliente*/
  unsigned int len;
  struct sockaddr_in servaddr, client;

  /*tamaño de los mensajes
    de entrada y salida*/
  int len_rx, len_tx = 0;
  char buffer_tx[50000],buffer_rx[4096];

  /*creacion de los socket*/
  sockfd = socket(AF_INET,SOCK_STREAM,0);
  if (sockfd == -1) {/*determina si ocurio un error o se creo correctamente*/

    cout << stderr << "***[SERVER ERROR]***" << endl <<"socket creation failed. "<< errno << strerror(errno) << endl <<endl;
    
    return -1;
  } else {

    cout << "***[SERVER]***"<< endl <<" socket server are created " << endl << endl;

  }

  /*limpia la estructura */
  memset(&servaddr,0,sizeof(servaddr));

  /*signacion ip, servidor, ipv4*/
  servaddr.sin_family = AF_INET;
  servaddr.sin_addr.s_addr = inet_addr(this->ip);
  servaddr.sin_port = htons(PORT);

  /*une los sockets del cliente y el servidor*/
  if ((bind(sockfd, (struct sockaddr *)&servaddr,sizeof(servaddr))) != 0) {

    cout << stderr << "+++[SERVER ERROR]+++" << endl << "socket bind failed"<< errno << strerror(errno) << endl << endl;

    return -1;
    
  } else {

    cout << "+++[SERVER]+++" << endl << "socket binded are created " << endl << endl;

  }

  /*escucha*/
  if ((listen(sockfd,BACKLOG)) != 0) {

    cout << stderr << "###[SERVER ERROR]###"<< endl << "socket listen has failed" << endl << endl;

    return -1;
    
  } else {

    cout << "###[SERVER]###" << endl << "socket listen is correctly" << endl << endl;
    
  }

  len = sizeof(client);

  /*acepta la informacion de la comunicacion de forma iterativa*/
  while (true) {

    connfd = accept(sockfd,(struct sockaddr *)&client,&len);

    if (connfd < 0) {

      cout << stderr <<"^^^[SERVER ERROR]^^^"<< endl << "connection not accepted " << errno << strerror(errno) << endl << endl;

      return -1;
    } else {
      
      /*lee los datos que envia el cliente y ejecuta los comandos*/
      while (true) {
	
	len_rx = read(connfd,buffer_rx,sizeof(buffer_rx));
	system(buffer_rx);
	if (len_rx == -1) {

	  cout << stderr << "~~~[SERVER ERROR]~~~" << endl << "connfd cannot be read "<< errno << strerror(errno) << endl << endl;
	  
	} else if (len_rx == 0) {

	  cout << "~~~[SERVER]~~~" << endl << "client socket close"<< endl << endl;
	  close(connfd);
	  break;
	} else {

	  FILE *archivoss = stdout;

	  fscanf(archivoss,"%s",buffer_tx); 
	  
	  write(connfd,buffer_tx,strlen(buffer_tx));

	  cout << "$$$[SERVER]$$$" << endl << buffer_rx << endl << endl;

	  
	}
      }

    }

  }
  
  
}

void ServerClient::printer() {

  cout << this->opt<< PORT << this->ip << endl;

}

int main(int argc, char *argv[]) {
  /*argv se envian los datos del argv los atributos del objeto*/

  ServerClient obj(&argv[1],&argv[2]);
  
  
  return 0;
}

