import java.net.*;
import java.io.*;
import java.util.Scanner;


//main class
public class csnetterminal {

    //main method
    public static void main(String[] args) {


	if ((args.length > 0) && (args.length < 4)) {

	    String a = args[0];
	    String b = args[1];
	    int c =  Integer.parseInt(args[2]);
	    ClientServerTCP obj = new ClientServerTCP(a,b,c);

	} else System.out.println("***[CSNETTERMINAL]***\nˇ->[ERROR]:invalid arguments lenght\n\n");
	    
    }
    
}	

/*class that can convert in
 a server or cliente*/
class ClientServerTCP {

    String HOST;
    int PORT;

    /*client*/
    private class Client {

	private Socket socket;
	private DataInputStream input = null;
	private DataOutputStream output = null;
	Scanner tecl = new Scanner(System.in);
	final String COMAND_FINISH = "salir()";


	//starting connection with the server
	public void up_connection(String ip, int port) {
	    /*Error comprovation*/
	    try {
		//deine the port and ip host of server
		socket = new Socket(ip,port);
		System.out.println("&&&[CLIENT]&&&\nConnection with " + socket.getInetAddress().getHostName());
	    } catch (IOException e) {
		System.out.println("Error up connection" + e.getMessage());
		e.printStackTrace();
	    }

	}

	/*open data flow for the system */
	public void open_flow() {
	    /*Error comprovation*/
	    try {

		input = new DataInputStream(socket.getInputStream());
		output = new DataOutputStream(socket.getOutputStream());
		output.flush();
		System.out.println("···[CLIENT]···\nOpen data flow correctly\n\n");
	    } catch (IOException e) {
		System.out.println("Error open flow" + e.getMessage());
		e.printStackTrace();
	    }

	}

	/*send data for the server system*/
	public void send_data(String s) {
	    /*Error comprovation*/
	    try {
		output.writeUTF(s);
		output.flush();
	    } catch (IOException e) {
		System.out.println("Error in send data" + e.getMessage());
		e.printStackTrace();
	    }

	    
	}
	/*close the socket system*/
	public void close_connection() {
	    /*Error comprovation*/
	    try {
		input.close();
		output.close();
		socket.close();
		System.out.println("$$$[CLIENT]$$$\nclose connection is correctly\n\n");
		
	    } catch (IOException e) {
		System.out.println("Error in close connection " + e.getMessage());
		e.printStackTrace();
	    } finally {
		System.exit(0);
	    }
	    
	}

	/*data resept*/
	public void resept_data() {
	    String st = "";
	    /*Error comprovation*/
	    try {
		/*loop for resept data*/
		do {
		    st = (String) input.readUTF();
		    System.out.println("×××[SERVER]×××\n" + st);		    
		} while(!st.equals(COMAND_FINISH));
		
	    }
	    catch (IOException e) {
		System.out.println("Error resept data" + e.getMessage());
		e.printStackTrace();
	    }

	}

	/*write data system*/
	public void write_data() {
	    String intp = "";

	    while(true) {
		System.out.println("÷÷÷[CLIENT]÷÷÷\nPlease write a message\n\n>>>> ");
		intp = tecl.nextLine();

		if(intp.length() > 0) {
		    send_data(intp);
		}
	    }
	    
	}

	/*exectution of the connection constructor*/
	public Client(String h, int p){
	    /*create a thread to run correctly orther*/
	    Thread hilo = new Thread(new Runnable() {
		    @Override
		    /*run the client system*/
		    public void run() {
			/*Error comprovation*/
			try {
			    up_connection(h,p);
			    open_flow();
			    resept_data();
			} finally {
			    close_connection();		     
			}

		    }
		});
	    hilo.start();
	    /*write data system*/
	    write_data();
	}	
    }

    /*server*/
    private class Server{

	/*atribute definitions*/
	private Socket socket;
	private ServerSocket serverSocket;
	private DataInputStream input = null;
	private DataOutputStream output =null;
	Scanner esca = new Scanner(System.in);
	final String COMAND_FINISH = "salir()";

	/*starting server conections*/
	public void up_connection(int p) {
	    /*Error comprovation*/
	    try {

		serverSocket = new ServerSocket(p);
		System.out.println("%%%[SERVER]%%%\nWaiting connection server in the port\n\n");
		socket = serverSocket.accept();
		System.out.println("$$$[SERVER]$$$\nConnection succesfully\n\n");
		
	    } catch (Throwable e) {
		System.out.println("Error up connection" + e.getMessage());
		e.printStackTrace();
	    }


	}

	/*definition to data flow input/output*/
	public void data_flow() {
	    /*Error comprovation*/
	    try {
		/*define the input/output objets*/
		input = new DataInputStream(socket.getInputStream());
		output = new DataOutputStream(socket.getOutputStream());
		/*This forces any buffered output
		  bytes to be written out to the stream*/
		output.flush();
	    } catch (Throwable e) {
		System.out.println("Error data flow" + e.getMessage());
		e.printStackTrace();
	    }

	}
	/*resept data from*/
	public void data_resept() {
	    String st ="";

	    /*Error comprovation*/
	    try {
		/*loop to retain connection*/
		do {
		    //convetion data type
		    st = (String) input.readUTF();
		    System.out.println(">>>CLIENT>>>\n" + st);
		    System.out.println("!!![SERVER]!!!\n\n");
		} while(!st.equals(COMAND_FINISH));
	    } catch (Throwable e) {
		System.out.println("Error data resept" + e.getMessage());
		e.printStackTrace();
	    }

	}

	/*send message system*/
	public void send_message(String s) {
	    /*Error comprovation*/
	    try {

		output.writeUTF(s);
		output.flush();
		
	    } catch (Throwable e) {
		System.out.println("Error send message" + e.getMessage());
		e.printStackTrace();
	    }

	    
	}

	/*write data system*/
	public void write_data() {
	    
	    /*loop to send messages*/
	    while(true) {

		System.out.println("···[SERVER]···\nsende message:\n\n");
		send_message(esca.nextLine());
	    }

	}
	/*close the connection system*/
	public void close_connection() {
	    /*Error comprovation*/
	    try {

		input.close();
		output.close();
		socket.close();
		
	    } catch (IOException e) {
		System.out.println("Error close connection" + e.getMessage());
		e.printStackTrace();
	    }

	    
	}

	/*constructor system*/
	public Server(int def_port){
	    /*create the thread for server system*/
	    Thread hilo = new Thread(new Runnable() {
		    /*exectution of server*/
		    @Override
		    public void run(){//function to run the server
			/*loop that repit the server connections*/
			while(true) {
			     /*Error comprovation*/
			    try {
				up_connection(def_port);
				data_flow();
				data_resept();
			    } finally {
				close_connection();
			    }
			}
		    }
		});
	    hilo.start();
	    /*write data system*/
	    write_data();
	}
	
    }
 
    public ClientServerTCP(String op,String host, int port) {

	HOST = host;	
	PORT = port;
		
	switch(op) {

	case "-s": Server obj = new Server(PORT); break;
	case "-c": Client obj2 = new Client(HOST,PORT); break;
	default: System.out.println("***ERORR^^^\ncann't doing your option");
	}
    }
}
 

