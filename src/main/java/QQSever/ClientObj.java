package QQSever;

import Client.Client;
import Tools.ConnTools;
import Tools.SendMsg;

import java.io.*;
import java.net.Socket;

import static java.lang.System.*;

public class ClientObj extends Thread{


    /**
     * 与服务器唯一对应的输出流对象 dout
     * 与服务器唯一对应的输出流对象 dint
     */
    private String account;
    private DataOutputStream dout;
    private DataInputStream dint;
    private static int Count=0;
    private CreatSever mysever;
    private boolean isAlive = true;

    /**
     * 构造器
     */
    public ClientObj(Socket soc,CreatSever mysever){
        super("客户端"+Count++);
        //获取流的对象
        try {
            OutputStream out = soc.getOutputStream();
            dout = new DataOutputStream(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            InputStream in = soc.getInputStream();
            dint = new DataInputStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.mysever = mysever;
    }


    /**
     * 设置ClientObj的标签
     * @param account
     */
    public void setAccount(String account){
        this.account = account;
    }

    /**
     * 返回account标签对象
     * @return
     */
    public String getAccount(){
        return account;
    }


    /**
     *
     */
    @Override
    public void run(){
        //进行接收
        while (true){
            if(isAlive){
                try {
                    int style = dint.read();//获取发送的数据类型
                    switch (style){
                        //接收的报文头为1，则接收信息为远程登陆阶段
                        case 1:{
                            if(vertifyAccount()==30){
                                //发送登陆成功协议，并切调用获取好友信息的方法，发送好友信息
                                sendSuccLoginMsg();
                            }else if(vertifyAccount()==20){
                                sendLoginError(); //发送账号已经在其它地方登陆的信息
                            }else if(vertifyAccount()==0){
                                sendPassError();  //发送密码错误的信息
                            }
                        }
                        case 2:{
                            regestAccount();//注册账号
                        }
                        case 3:{

                        }
                        case 4:{
                            String sendAcc = readString();
                            String sendDes = readString();
                            String msg = readString();
                            if(mysever.getClient(sendDes)!=null){
                                mysever.getClient(sendDes).sendMsg(sendAcc,msg);
                            }
                        }
                        case 5:{
                            isAlive = false;
                            mysever.removeClient(this);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                break;
            }
        }

    }


    /**
     * 验证登陆信息
     * @return
     */
    public int vertifyAccount(){
        try {
            //获取客户端发送过来的登陆信息 账户和密码
            int accLen = dint.readInt();
            byte[] accbyt = new byte[accLen];
            dint.read(accbyt);
            String account = new String(accbyt);
            this.account = account;
            int passlen = dint.readInt();
            byte[] passbyt = new byte[passlen];
            dint.read(passbyt);
            String pass = new String(passbyt);
            //1、如果账号密码正确但是该账号已经被在其它地方登陆 返回20
            if(ConnTools.verityLog(account,pass)&&ConnTools.isLogin(account)){
                return 20;
            }else if(ConnTools.verityLog(account,pass)) {
                setAccount(account);
                return 30;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 发送密码错误日志
     */
    public void sendPassError(){
        try {
            dout.write(1);
            dout.writeBoolean(false);
            dout.writeInt(10);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送账号已经登陆日志
     */
    public void sendLoginError(){
        try {
            dout.write(1);
            dout.writeBoolean(false);
            dout.writeInt(11);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送密码验证成功日志
     */
    public void sendSuccLoginMsg(){
        try {
            dout.write(1);
            dout.writeBoolean(true);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 执行注册账号的操作
     */
    public void regestAccount(){
        /**
         * 1、先接收qq号码
         * 2、判断qq是否已经注册 如果没有注册的话发送一个验证码
         * 3、接收验证码正确信息
         * 4、创建账号
         */
        try {
            //接收qq账号
            int len = dint.readInt();
            byte[] qqbyt = new byte[len];
            dint.read(qqbyt);
            String qqnum = new String(qqbyt);

            //判断qq是否已经注册
            if(ConnTools.checkQQIsExits(qqnum)){
                //向客户机反馈qq号已经被注册的报文消息
                dout.write(2);
                dout.writeBoolean(true);
                return;
            }else {
                //通过pop3和stmp协议给qq邮箱发送一个信息
                String vertcode = SendMsg.sendMessages(qqnum);
                dout.write(2);
                dout.writeBoolean(false);
                writeString(vertcode);
            }
            //进行信息接收
            boolean isRight = dint.readBoolean();
            //信息正确的话，接收账号、密码信息将信息写入数据库
            if(isRight){
                String account = readString();
                String pass = readString();
                ConnTools.createAccount(account,pass,qqnum);
            }else {
                return;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 写字符串的操作
     * @param str
     */
    private void writeString(String str){
        int len = str.length();
        try {
            dout.writeInt(len);
            byte[] strbyt = str.getBytes();
            dout.write(strbyt);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取字符串的操作
     * @return
     */
    private String readString(){
        try {
            int len = dint.readInt();
            byte[] strbyt = new byte[len];
            dint.read(strbyt);
            String str = new String(strbyt);
            return str;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public void sendMsg(String sendAcc,String msg){
        try {
            dout.write(4);
            writeString(sendAcc);
            writeString(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }







}
