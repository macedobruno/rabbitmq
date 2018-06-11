package br.ufs.dcomp.rabbitmq;

//import com.rabbitmq.client.*;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

public class Emissor {

  private final static String QUEUE_NAME = "minha-fila2";

  public static void main(String[] argv) {

    try {
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
    }
  }
}