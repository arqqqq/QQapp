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
     * ���տͻ��˷��͵�
     */
    public void run(){
        while (true){
            try {
                int style = dint.readInt();
                switch (style){
                    case 1:{ //���յ��¼�Ϊ�������ͻ���
                       receiveMsg();
                    }
                    case 2:{
                        boolean isExites = dint.readBoolean();
                        if(!isExites){
                            //˵����qq�˺�û�б�ע��

                        }else {
                            //˵��qq�˺��Ѿ���ע��
                            JOptionPane.showConfirmDialog(null,"��qq���Ѿ���ע�ᣡ");
                        }
                    }
                    case 3:{

                    }
                    case 4:{
                        //�ı�������Ϣ
                        String senter = readStr();
                        String msg = readStr();
                        //���ı���Ϣ��ʾ��������......
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ���շ������������Ĺ���Զ�̵�½����Ϣ
     */
    private void receiveMsg() {
        try {
            boolean isRight = dint.readBoolean();
            if(isRight){
               //��ȡ���͹����ĺ�����Ϣ�����շ��͹����ĺ�����Ϣ��������
            }else {
                int aspect = dint.readInt();
                if(aspect==30){
                    JOptionPane.showConfirmDialog(null,"���˺��Ѿ��������ط���½��");
                }else if(aspect==0){
                    JOptionPane.showConfirmDialog(null,"�������������������˺Ų�����");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * �����ַ����ķ���
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
