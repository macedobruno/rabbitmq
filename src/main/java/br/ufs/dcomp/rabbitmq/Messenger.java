package br.ufs.dcomp.rabbitmq;

import com.rabbitmq.client.*;
import java.io.*;
import com.google.protobuf.util.JsonFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class Messenger{
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private String user, password, host;
    private String dfQueue, dfUser, tipo;
    private Consumer consumer;
    
    //Inicia a classe
    public Messenger (String user, String password, String host) throws Exception{
        this.user = user;
        this.password = password;
        this.host = host;
        this.factory = new ConnectionFactory();
        this.factory.setUri("amqp://"+this.user+":"+this.password+"@"+this.host+":5672");
        this.connection = factory.newConnection();
        this.channel = connection.createChannel();
    }
    
    //Inicia a classe informando usuário e fila padrão
    public Messenger (String user, String password, String host, String dfuser, String dfqueue) throws Exception{
        this.user = user;
        this.password = password;
        this.host = host;
        this.dfUser = dfuser;
        this.dfQueue = dfqueue;
        this.factory = new ConnectionFactory();
        this.factory.setUri("amqp://"+this.user+":"+this.password+"@"+this.host+":5672");
        this.connection = factory.newConnection();
        this.channel = connection.createChannel();
    }
    
    //MENSAGEM PARA UM ÚNICO DESTINATÁRIO
    public void queueMsg(String user, String queue, String msg) throws Exception{ 
        this.tipo = "@";
        this.dfQueue = queue;
        this.channel.queueDeclare(queue, false, false, false, null);
        String editedMsg = ("(" +getDate()+ " às " + getTime() + ") " +user + " diz: " + msg);
        this.channel.basicPublish("", queue, null, editedMsg.getBytes("UTF-8"));
    }
    
    //MENSAGEM PARA UM ÚNICO DESTINATÁRIO
    public void queueMsg(String queue, String msg) throws Exception{ 
        this.tipo = "@";
        this.dfQueue = queue;
        this.channel.queueDeclare(queue, false, false, false, null);
        String editedMsg = ("(" +getDate()+ " às " + getTime() + ") " +this.dfUser + " diz: " + msg);
        this.channel.basicPublish("", queue, null, editedMsg.getBytes("UTF-8"));
    }
    
    //MENSAGEM PARA UM ÚNICO DESTINATÁRIO
    public void queueMsg(String msg) throws Exception{
        this.tipo = "@";
        this.channel.queueDeclare(this.dfQueue, false, false, false, null);
        String editedMsg = ("(" +getDate()+ " às " + getTime() + ") " +this.dfUser + " diz: " + msg);
        this.channel.basicPublish("", this.dfQueue, null, editedMsg.getBytes("UTF-8"));
    }
    
    //MENSAGEM PARA UM EXCHANGE
    public void groupMsg(String user, String exchange, String msg) throws Exception{
        this.tipo = "#";
        this.dfQueue = exchange;
        String editedMsg = ("(" +getDate()+ " às " + getTime() + ") " +user+" #"+exchange+" diz: " + msg);
        this.channel.basicPublish(exchange, "", null, editedMsg.getBytes("UTF-8"));
    }
    
    //MENSAGEM PARA UM EXCHANGE
    public void groupMsg(String exchange, String msg) throws Exception{
        this.tipo = "#";
        this.dfQueue = exchange;
        String editedMsg = ("(" +getDate()+ " às " + getTime() + ") " +this.dfUser+" #"+exchange+" diz: " + msg);
        this.channel.basicPublish(exchange, "", null, editedMsg.getBytes("UTF-8"));
    }
    
    //MENSAGEM PARA UM EXCHANGE USANDO USUÁRIO E GRUPO PADRAO
    public void groupMsg(String msg) throws Exception{
        this.tipo = "#";
        String editedMsg = ("(" +getDate()+ " às " + getTime() + ") " +this.dfUser+" #"+this.dfQueue+" diz: " + msg);
        this.channel.basicPublish(this.dfQueue, "", null, editedMsg.getBytes("UTF-8"));
    }
    
    //MENSAGEM PARA UM ÚNICO DESTINATÁRIO
    public void protoMsg(String msg) throws Exception{
        this.tipo = "@";
        this.channel.queueDeclare(this.dfQueue, false, false, false, null);
        String editedMsg = ("(" +getDate()+ " às " + getTime() + ") " +this.dfUser + " diz: " + msg);
        this.channel.basicPublish("", this.dfQueue, null, editedMsg.getBytes("UTF-8"));
        
    }
    
    //FAZ UPLOAD DE ARQUIVO
    public void upload(String path) throws Exception{
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        fis.read(buffer);
        fis.close();
        //protoFile(buffer);
    }
    
    //RECEBE MENSAGENS USANDO O USUÁRIO PADRÃO
    public void consume() throws Exception{
        this.channel.basicConsume(this.dfUser, true, new DefaultConsumer(this.channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException{
                String mensagem = new String(body, "UTF-8");
                System.out.print("\n"+mensagem+"\n"+getTipo()+getDfQueue()+">> ");
                //adiante dá pra fazer uma funcao que receba a string, algo tipo setIncomingMsg() dentro de consume
                //ainda dentro de consume colocar um return da string getIncomingMsg() e o programa receber só a string pura
            }
        });
    }
    
    //RECEBE MENSAGENS USANDO UM USUÁRIO INFORMADO
    public void consume(String user) throws Exception{
        this.channel.basicConsume(user, true, new DefaultConsumer(this.channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException{
                String mensagem = new String(body, "UTF-8");
                System.out.print("\n"+mensagem+"\n"+getTipo()+getDfQueue()+">> ");
            }
        });
    }
    
    //FECHAR TUDO
    public void close() throws Exception{
        this.channel.close();
        this.connection.close();
    }
    
    //Retorna o channel aberto
    public Channel getChannel (){
        return this.channel;
    }
    
    //Define um usuário padrão
    public void setDfUser(String user){
        this.dfUser = user;
    }
    
    //Retorna o usuário padrão
    public String getDfUser(){
        return this.dfUser;
    }
    
    //Define uma fila padrão
    public void setDfQueue(String tipo, String queue){
        this.tipo = tipo;
        this.dfQueue = queue;
    }
    
    //Retorna a fila padrão
    public String getDfQueue(){
        return this.dfQueue;
    }
    
    //Define um tipo
    public void setTipo(String tp){
        this.tipo = tp;
    }
    
    //Retorna o tipo
    public String getTipo(){
        return this.tipo;
    }
    
    //Cria novo grupo
    public void addGroup(String exchange) throws Exception{
        this.channel.exchangeDeclare(exchange, "fanout");
    }
    
    //Adiciona usuário ao grupo
    public void addUser(String user, String exchange) throws Exception{
        this.channel.queueBind(user, exchange, "");
    }
    
    //Remove usuário do grupo
    public void delFromGroup(String user, String exchange) throws Exception{
        this.channel.queueUnbind(user, exchange, "");
    }
    
    //Remove grupo
    public void removeGroup(String exchange) throws Exception{
        this.channel.exchangeDelete(exchange);
    }
    
    //Retorna data
    public String getDate()throws Exception{
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date today = Calendar.getInstance().getTime();
        return df.format(today);
    }
    
    //Retorna hora
    public String getTime()throws Exception{
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        return df.format(today);
    }
    
    public void protoSender(String mensagem){
        MsgProto.Conteudo.Builder content = MsgProto.Conteudo.newBuilder();
        content.setType("text/plain");
        content.setBody(mensagem.getBytes());
        
        MsgProto.Messenger.Builder msg = MsgProto.newBuilder();
        msg.setSender(this.dfUser);
        msg.setDate(getDate());
        msg.setTime(getTime());
        
        if (this.tipo.equals("#")){
            msg.setGroup(this.dfQueue);
        }
        
        msg.setConteudo(content);
        
        MsgProto.Messenger messenger = builderMessenger.build();
        //msg.build();

    }
    
    public void protoFile(byte[] file) throws Exception{
        
    }
}