package csnetterminal;
//this is the impotations headers
import java.net.*;
import java.io.*;
import java.util.Scanner;
import ClientServerTCP.ClientServerTCP;
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
