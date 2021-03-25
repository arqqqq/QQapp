package Client;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ConnectServer extends Thread{

    private DataInputStream dint;
    private DataOutputStream dout;

    public void connSever(DataInputStream dint,DataOutputStream dout){
        this.dint = dint;
        this.dout = dout;
    }

    /**
     * 接收客户端发送的
     */
    public void run(){
        while (true){
            try {
                int style = dint.read();
                switch (style){
                    case 1:{

                    }
                    case 2:{

                    }
                    case 3:{

                    }
                    case 4:{

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    private void writeStr(String str){
        int len = str.length();
        try {
            dout.writeInt(len);
            dout.write(str.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
