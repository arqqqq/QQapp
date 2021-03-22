package Client;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ConnectServer extends Thread implements Operation{

    private DataInputStream dint;
    private DataOutputStream dout;
    private String code;
    private JTextPane doc;
    public void connSever(){
        try {
            Socket soc = new Socket("localhost",8800);
            InputStream in = soc.getInputStream();
            OutputStream out = soc.getOutputStream();
            dint = new DataInputStream(in);
            dout = new DataOutputStream(out);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setJTextPane(JTextPane doc){
        this.doc = doc;
    }
    /**
     * 接收客户端发送的
     */
    public void run(){
        while (true){
            try {
                int style = dint.read();
                switch (style){
                    case 1:{ //接收的事件为服务器客户端
                       receiveMsg();
                    }
                    case 2:{
                        boolean isExites = dint.readBoolean();
                        if(!isExites){
                            //说明该qq账号没有被注册，接收验证码
                            code = readStr();
                        }else {
                            //说明qq账号已经被注册
                            JOptionPane.showConfirmDialog(null,"该qq号已经被注册！");
                        }
                    }
                    case 3:{

                    }
                    case 4:{
                        //文本聊天信息
                        String senter = readStr();
                        String msg = readStr();
                        //将文本信息显示到界面上......
                        doc.setText(msg);
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 接收服务器发过来的关于远程登陆的信息
     */
    private void receiveMsg() {
        try {
            boolean isRight = dint.readBoolean();
            isLogin = isRight;
            if(isRight){
               //读取发送过来的好友信息，接收发送过来的好友信息。。。。
            }else {
                int aspect = dint.readInt();
                if(aspect==30){
                    JOptionPane.showConfirmDialog(null,"该账号已经在其他地方登陆！");
                    return;
                }else if(aspect==0){
                    JOptionPane.showConfirmDialog(null,"输入的密码错误或输入的账号不存在");
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 接收字符串的方法
     * @return
     */
    public String readStr(){
        try {
            int len = dint.readInt();
            byte strbyt[] = new byte[len];
            dint.read(strbyt);
            String str = new String(strbyt);
            return str;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     *发送关闭消息
     */
    public void sendNoOnlineMsg(){
        try {
            dout.write(5);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回客户端发送过来的验证码
     * @return
     */
    public String verityCode(){
        return code;
    }


    /**
     * 发送注册账户的消息
     * @param qqnum
     */
    public void sendRegistMsg(String qqnum) {
        try {
            dout.write(2);
            int len = qqnum.length();
            dout.writeInt(len);
            dout.write(qqnum.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向服务器发送账户信息
     * @param account
     * @param password
     */
    public void sendAccountMsg(String account,String password){
        try {
            dout.writeBoolean(true);
            int len1 = account.length();
            dout.writeInt(len1);
            dout.write(account.getBytes());
            int len2 = password.length();
            dout.writeInt(len2);
            dout.write(password.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isLogin;

    @Override
    public boolean Clink_Login_Operation(String account, String passwords) {
        sendLoginMsg(account,passwords);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return isLogin;
    }

    /**
     *
     * @param account
     * @param password
     */
    private void sendLoginMsg(String account,String password){
        try {
            dout.write(1);
            writeStr(account);
            writeStr(password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeStr(String str){
        int len = str.length();
        try {
            dout.writeInt(len);
            dout.write(str.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void Clink_Friend_Operation(String friend_name,String sender) {
        new Chat(this,"10000",sender).Open();
    }

    @Override
    public void Clink_Send_Operation() {

    }


    public void sendTextMsg(String sender,String destaccount,String msg) {
        try {
            dout.write(4);
            writeStr(sender);
            writeStr(destaccount);
            writeStr(msg);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
