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
     * ���տͻ��˷��͵�
     */
    public void run(){
        while (true){
            try {
                int style = dint.read();
                switch (style){
                    case 1:{ //���յ��¼�Ϊ�������ͻ���
                       receiveMsg();
                    }
                    case 2:{
                        boolean isExites = dint.readBoolean();
                        if(!isExites){
                            //˵����qq�˺�û�б�ע�ᣬ������֤��
                            code = readStr();
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
                        doc.setText(msg);
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
            isLogin = isRight;
            if(isRight){
               //��ȡ���͹����ĺ�����Ϣ�����շ��͹����ĺ�����Ϣ��������
            }else {
                int aspect = dint.readInt();
                if(aspect==30){
                    JOptionPane.showConfirmDialog(null,"���˺��Ѿ��������ط���½��");
                    return;
                }else if(aspect==0){
                    JOptionPane.showConfirmDialog(null,"�������������������˺Ų�����");
                    return;
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
     *���͹ر���Ϣ
     */
    public void sendNoOnlineMsg(){
        try {
            dout.write(5);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ���ؿͻ��˷��͹�������֤��
     * @return
     */
    public String verityCode(){
        return code;
    }


    /**
     * ����ע���˻�����Ϣ
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
     * ������������˻���Ϣ
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
