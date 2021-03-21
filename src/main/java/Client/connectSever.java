package Client;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class connectSever extends Thread{

    private DataInputStream dint;
    private DataOutputStream dout;

    public void connSever(){
        try {
            Socket soc = new Socket("loaclhost",8800);
            InputStream in = soc.getInputStream();
            OutputStream out = soc.getOutputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 接收客户端发送的
     */
    public void run(){
        while (true){
            try {
                int style = dint.readInt();
                switch (style){
                    case 1:{ //接收的事件为服务器客户端
                       receiveMsg();
                    }
                    case 2:{
                        boolean isExites = dint.readBoolean();
                        if(!isExites){
                            //说明该qq账号没有被注册

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
            if(isRight){
               //读取发送过来的好友信息，接收发送过来的好友信息。。。。
            }else {
                int aspect = dint.readInt();
                if(aspect==30){
                    JOptionPane.showConfirmDialog(null,"该账号已经在其他地方登陆！");
                }else if(aspect==0){
                    JOptionPane.showConfirmDialog(null,"输入的密码错误或输入的账号不存在");
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
     *
     */



}
