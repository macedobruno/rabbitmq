package br.ufs.dcomp.rabbitmq;

//import com.rabbitmq.client.*;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Scanner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Emissor {

  private final static String QUEUE_NAME = "minha-fila2";
  private static String QUEUE, USER;

  public static void main(String[] argv) {

    /*try {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setUri("amqp://sender:abc123@ec2-18-236-172-69.us-west-2.compute.amazonaws.com:5672");
      Connection connection = factory.newConnection();
      Channel channel = connection.createChannel();
  
      channel.queueDeclare(QUEUE_NAME, false, false, false, null);
      String message = "Olá!!!";
      channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
      System.out.println(" [x] Mensagem enviada: '" + message + "'");
  
      channel.close();
      connection.close();
    } catch(Exception ex) {
      System.out.println("\n \n ##### Não foi possivel conectar ao servidor RABBITMQ ##### \n \n" + ex.toString());
    }*/
    /*try { //teste de mensagem personalizada
      ConnectionFactory factory = new ConnectionFactory();
      factory.setUri("amqp://sender:abc123@ec2-18-236-172-69.us-west-2.compute.amazonaws.com:5672");
      Connection connection = factory.newConnection();
      Channel channel = connection.createChannel();
  
      channel.queueDeclare(QUEUE_NAME, false, false, false, null);
      //Scanner entrada = new Scanner(System.in);
      //String message = "Olá!!!";
      System.out.print (">> ");
      Scanner in = new Scanner(System.in);
      String message = in.nextLine();
      //String message = input.next();
      
      //channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
      channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
      System.out.println(" [x] Mensagem enviada: '" + message + "'");
      //System.out.println(" [x] Mensagem enviada: '" + entrada.nextLine() + "'");
  
      channel.close();
      connection.close();
    } catch(Exception ex) {
      System.out.println("\n \n ##### Não foi possivel conectar ao servidor RABBITMQ ##### \n \n" + ex.toString());
    }*/
    
    // OUTRO TESTE!!!
    
    try{ //executa o outro teste
      boolean rpt = true;
      
      System.out.print ("User: ");
      Scanner usr = new Scanner(System.in);
      USER = usr.nextLine();
      //System.out.println (USER);
      
      System.out.print (">> ");
      Scanner fil = new Scanner(System.in);
      QUEUE = fil.nextLine();
      //System.out.println (QUEUE);
      
      while (rpt){ //repete até a função retornar false;
        //System.out.println ("while ");
        rpt = testeChat2();
      }
      
      System.out.println("\n\n\n ##### \t  CHAT FINALIZADO \t ##### \n\n\n");
    }catch (Exception ex) {
      System.out.println("\n \n ##### Não foi possivel conectar ao servidor RABBITMQ ##### \n \n" + ex.toString());
    }
    
  }
  
  public static boolean testeChat2 () throws Exception{
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUri("amqp://sender:abc123@ec2-18-236-172-69.us-west-2.compute.amazonaws.com:5672");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    channel.queueDeclare(QUEUE, false, false, false, null); // NÃO ESQUECER, SENÃO NÃO CRIA A FILA, CASO ELA NÃO EXISTA
    
    System.out.print("@"+QUEUE+">> ");
    Scanner msg = new Scanner(System.in);
    String message = msg.nextLine();
    
    if (message.equals("exit")){ //se escrever exit ele finaliza
      channel.close();
      connection.close();
      //messenger.close();
      return false;
    
    } else if (message.indexOf("@") == 0){ //mudar de destinatário / fila
      System.out.println("ACHOU O @");
      QUEUE = message.replace("@", "");
      
    } else{ //senao segue o baile
      
      /*DateFormat shortDf = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
      shortDf = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);*/
      
      String editedMsg = ("(DD/MM/AAAA às HH:MM:SS) " +USER + " diz: " + message);
      //System.out.println(editedMsg);
      channel.basicPublish("", QUEUE, null, editedMsg.getBytes("UTF-8"));
    
    }
    
    

    channel.close();
    connection.close();
    
    return true;
  }
  
  //public static int checkMsg(String msg){
    //
  //}
  
  /*private static String getDateTime() { 
	  DateFormat dateFormat = new DateFormat("dd/MM/yyyy HH:mm:ss"); 
	  Date date = new Date(); 
	  return dateFormat.format(date); 
  }*/
}

/*
public class Conector {
  private ConnectionFactory factory;
  private Connection connection;
  private Channel channel;
  private String user, passwd, host;
  
  public Conector(String usr, String psw, String hst){
    user = usr;
    passwd = psw;
    host = hst;
    factory = new ConnectionFactory();
    factory.setUri("amqp://"+user+":"+passwd+"@"+host+":5672");
    connection = factory.newConnection();
    channel = connection.createChannel();
    channel.queueDeclare(QUEUE, false, false, false, null);
    
  }
  
  public void setUsr(String usr){
    user = usr;
  }
  
  public void setPsw(String psw){
    passwd = psw;
  }
  
  public void setHst(String hst){
    host = hst;
  }
  */
  /*
  
  ConnectionFactory factory = new ConnectionFactory();
    factory.setUri("amqp://sender:abc123@ec2-18-236-172-69.us-west-2.compute.amazonaws.com:5672");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    channel.queueDeclare(QUEUE, false, false, false, null); // NÃO ESQUECER, SENÃO NÃO CRIA A FILA, CASO ELA NÃO EXISTA
  
  */
  
//}