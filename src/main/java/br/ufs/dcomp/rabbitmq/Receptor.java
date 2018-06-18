package br.ufs.dcomp.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Receptor {

  private final static String QUEUE_NAME = "macedo";

  public static void main(String[] argv) throws Exception {
    try {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setUri("amqp://sender:abc123@ec2-18-236-172-69.us-west-2.compute.amazonaws.com:5672");
      Connection connection = factory.newConnection();
      Channel channel = connection.createChannel();
  
      channel.queueDeclare(QUEUE_NAME, false, false, false, null);
      System.out.println(" [*] Esperando recebimento de mensagens...");
  
      /*Consumer consumer = new DefaultConsumer(channel) {
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
            throws IOException {
          String message = new String(body, "UTF-8");
          //System.out.println(" [x] Mensagem recebida: '" + message + "'");
          //System.out.println("");
          System.out.println(message);
        }
      };
      channel.basicConsume(QUEUE_NAME, true, consumer);
    */
    
    channel.basicConsume(QUEUE_NAME, false, new DefaultConsumer(channel) {
         @Override
         public void handleDelivery(String consumerTag,
                                    Envelope envelope,
                                    AMQP.BasicProperties properties,
                                    byte[] body) throws IOException{
             String message = new String(body, "UTF-8");
             System.out.println(message);
         }
     });
      
    } catch(Exception ex) {
      System.out.println("\n \n ##### Não foi possivel conectar ao servidor RABBITMQ ##### \n \n" + ex.toString());
    }
  }
}