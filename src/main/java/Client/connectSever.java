package Client;

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
     * ���տͻ��˷��͵�
     */
    public void run(){
        while (true){
            try {
                int style = dint.readInt();
                switch (style){
                    case 1:{ //���յ��¼�Ϊ�������ͻ���

                    }
                    case 2:{

                    }
                    case 3:{

                    }
                    case 4:{

                    }
                    case 5:{

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public



}
