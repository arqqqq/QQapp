package QQSever;

import Tools.ConnTools;
import Tools.SendMsg;

import java.io.*;
import java.net.Socket;

public class ClientObj extends Thread{


    /**
     * �������Ψһ��Ӧ����������� dout
     * �������Ψһ��Ӧ����������� dint
     */
    private String account;
    private DataOutputStream dout;
    private DataInputStream dint;
    private static int Count=0;
    private CreatSever mysever;
    private boolean isAlive = true;

    /**
     * ������
     */
    public ClientObj(Socket soc,CreatSever mysever){
        super("�ͻ���"+Count++);
        //��ȡ���Ķ���
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
     * ����ClientObj�ı�ǩ
     * @param account
     */
    public void setAccount(String account){
        this.account = account;
    }

    /**
     * ����account��ǩ����
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
        //���н���
        while (true){
            if(isAlive){
                try {
                    int style = dint.readInt();//��ȡ���͵���������
                    switch (style){
                        //���յı���ͷΪ1���������ϢΪԶ�̵�½�׶�
                        case 1:{
                            if(vertifyAccount()==20){
                                //���͵�½�ɹ�Э�飬���е��û�ȡ������Ϣ�ķ��������ͺ�����Ϣ
                            }else if(vertifyAccount()==30){
                                sendLoginError(); //�����˺��Ѿ��������ط���½����Ϣ
                            }else if(vertifyAccount()==0){
                                sendPassError();  //��������������Ϣ
                            }
                        }
                        case 2:{
                            regestAccount();//ע���˺�
                        }
                        case 3:{

                        }
                        case 4:{
                            String sendAcc = readString();
                            String sendDes = readString();
                            String msg = readString();
                            if(mysever.getClient(sendDes)!=null){
                                mysever.getClient(sendDes).sendMsg(sendAcc,msg);
                            };

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
     * ��֤��½��Ϣ
     * @return
     */
    public int vertifyAccount(){
        try {
            //��ȡ�ͻ��˷��͹����ĵ�½��Ϣ �˻�������
            int accLen = dint.readInt();
            byte[] accbyt = new byte[accLen];
            dint.read(accbyt);
            String account = new String(accbyt);
            int passlen = dint.readInt();
            byte[] passbyt = new byte[passlen];
            dint.read(passbyt);
            String pass = new String(passbyt);
            //1������˺�������ȷ���Ǹ��˺��Ѿ����������ط���½ ����20
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
     * �������������־
     */
    public void sendPassError(){
        try {
            dout.writeInt(1);
            dout.writeBoolean(false);
            dout.writeInt(10);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * �����˺��Ѿ���½��־
     */
    public void sendLoginError(){
        try {
            dout.writeInt(1);
            dout.writeBoolean(false);
            dout.writeInt(11);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ִ��ע���˺ŵĲ���
     */
    public void regestAccount(){
        /**
         * 1���Ƚ���qq����
         * 2���ж�qq�Ƿ��Ѿ�ע�� ���û��ע��Ļ�����һ����֤��
         * 3��������֤����ȷ��Ϣ
         * 4�������˺�
         */
        try {
            //����qq�˺�
            int len = dint.readInt();
            byte[] qqbyt = new byte[len];
            dint.read(qqbyt);
            String qqnum = new String(qqbyt);

            //�ж�qq�Ƿ��Ѿ�ע��
            if(ConnTools.checkQQIsExits(qqnum)){
                //��ͻ�������qq���Ѿ���ע��ı�����Ϣ
            }else {
                //ͨ��pop3��stmpЭ���qq���䷢��һ����Ϣ

                String vertcode = SendMsg.sendMessages(qqnum);
                dout.writeInt(2);
                writeString(vertcode);
            }
            //������Ϣ����
            boolean isRight = dint.readBoolean();
            //��Ϣ��ȷ�Ļ��������˺š�������Ϣ����Ϣд�����ݿ�
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
     * д�ַ����Ĳ���
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
     * ��ȡ�ַ����Ĳ���
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
            dout.writeInt(4);
            writeString(sendAcc);
            writeString(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






}
