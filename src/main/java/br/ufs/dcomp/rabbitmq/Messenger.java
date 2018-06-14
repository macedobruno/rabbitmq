package br.ufs.dcomp.rabbitmq;

//import com.rabbitmq.client.*;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

public class Messenger{
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private String user, password, host;
    
    public Messenger (String user, String password, String host) {
        this.user = user;
        this.password = password;
        this.host = host;
        this.factory = new ConnectionFactory;
        this.factory.setURI("amqp://"+this.user+":"+this.password+"@"+this.host+":5672");
        this.connection = factory.newConnection();
        this.channel = connection.createChannel();
    }
    
    public boolean queueMsg(String user, String queue, String msg) throws Exception{
        this.channel.queueDeclare(queue, false, false, false, null);
        String editedMsg = ("(DD/MM/AAAA às HH:MM:SS) " +user + " diz: " + msg);
        this.channel.basicPublish("", queue, null, editedMsg.getBytes("UTF-8");
        return true;
    }
    
    public void close(){
        this.channel.close();
        this.connection.close();
    }
}