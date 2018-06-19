package br.ufs.dcomp.rabbitmq;

import java.io.IOException;
import java.util.Scanner;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.*;

public class UseMessenger{

    private static String QUEUE, USER, TIPO;
    
    public static void main (String[] argv) throws Exception{
        try{
          boolean rpt = true;
          
          System.out.print ("User: ");
          Scanner usr = new Scanner(System.in);
          USER = usr.nextLine();
          
          System.out.print (">> ");
          Scanner fila = new Scanner(System.in);
          QUEUE = fila.nextLine();
          
          if (QUEUE.indexOf("@") == 0){
            TIPO = "@";
            QUEUE = QUEUE.replace("@", "");
            
          } else if (QUEUE.indexOf("#") == 0){
            TIPO = "#";
            QUEUE = QUEUE.replace("#", "");
            
          } //TEM QUE COLOCAR UMA MENSAGEM DE ERRO CASO NÃO COLOQUE O @
          
          Messenger msg = new Messenger("sender", "abc123", "ec2-18-236-172-69.us-west-2.compute.amazonaws.com"); //inicia a classe com os dados do RABBITMQ
          msg.setDfUser(USER); //define usuário
          msg.setDfQueue(TIPO, QUEUE); //define tipo @ ou # e a fila

          while (rpt){ //repete até a função retornar false;
            msg.consume(); // inicia consumo das mensagens
            rpt = chat(msg);
          }
          
          System.out.println("\n\n\n ##### \t  CHAT FINALIZADO \t #####");
        }catch (Exception ex) {
          System.out.println("\n \n ##### Não foi possivel conectar ao servidor RABBITMQ ##### \n \n" + ex.toString());
        }
        
    }
    
    public static boolean chat(Messenger messenger) throws Exception{
        System.out.print(TIPO+QUEUE+">> ");
        Scanner msg = new Scanner(System.in);
        String message = msg.nextLine();
        
        //messenger.consume(); //recebe as mensagens
        
        try{
            if (message.equals("exit")){ //se escrever exit ele finaliza
                messenger.close();
                return false;
            } else if (message.indexOf("@") == 0){ //mudar de destinatário / fila
                TIPO = "@";
                QUEUE = message.replace("@", "");
                messenger.setDfQueue("@", QUEUE);
                
            } else if (message.indexOf("#") == 0){ //mudar de destinatário / fila
                TIPO = "#";
                QUEUE = message.replace("#", "");
                messenger.setDfQueue("#", QUEUE);
                
            } else if (message.indexOf("!") == 0){ // gerenciar grupo
                
                if (message.indexOf("addGroup") == 1){ //cria grupo caso não exista
                    String comando[] = message.trim().split(" ");
                    messenger.addGroup(comando[1]);
                    TIPO = "#";
                    QUEUE = comando[1];
                    messenger.setDfQueue("#", QUEUE);
                } else if (message.indexOf("addUser") == 1){ //adiciona usuário ao grupo
                    String comando[] = message.trim().split(" ");
                    messenger.addUser(comando[1], comando[2]);
                } else if (message.indexOf("delFromGroup") == 1){ //deleta usuário do grupo | !delFromGroup user group
                    String comando[] = message.trim().split(" ");
                    messenger.delFromGroup(comando[1], comando[2]);
                } else if (message.indexOf("removeGroup") == 1){ //deleta grupo
                    String comando[] = message.trim().split(" ");
                    messenger.removeGroup(comando[1]);
                } else if (message.indexOf("upload") == 1){
                    String comando[] = message.trim().split(" ");
                    messenger.upload(comando[1]);
                }/* else if (message.indexOf("listUsers") == 1){ //listar usuários
                  //teste de listar usuário
                    try {
                        //HttpURLConnectionExample http = new HttpURLConnectionExample();
                        
                        //Client c = new Client("http://ec2-18-236-172-69.us-west-2.compute.amazonaws.com:15672/api/", "guest", "guest");
                        //c.getQueues();
                        System.out.println("Testing 1 - Send Http GET request");
		                sendGet();
                  } catch(IOException ex){
                      System.out.println("\n##### Erro! Não foi possível realizar a requisição #####");
                  }
                    
                } /*else if (message.indexOf("listGroup") == 1){ //listar grupos
                    
                } */
            } else {
                if (TIPO.equals("@")){ //mensagem para usuário
                    messenger.queueMsg(QUEUE, message);
                } else if (TIPO.equals("#")){ //mensagem para grupo
                    messenger.groupMsg(QUEUE, message);
                }
            }
        }catch (ArrayIndexOutOfBoundsException ex){
            System.out.println("\n##### Erro! Verifique a falta de algum dado a ser informado #####");
        }
        
        return true;
    }
    
    /*// HTTP GET request
	private static void sendGet() throws Exception {

	    String urlToRead = "http://sender:abc123@ec2-18-236-172-69.us-west-2.compute.amazonaws.com:15672/api/queues/";
		
		
		URL obj = new URL(urlToRead);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", "Mozilla/5.0");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + urlToRead);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
	/*
		StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        int responseCode = conn.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        //return result.toString();
        System.out.println(result.toString());

	}*/
	
}