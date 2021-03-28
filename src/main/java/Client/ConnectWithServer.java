package Client;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectWithServer{


    /**
     * �����½��ť��Զ�̵�½����
     * @param account  accountΪ�˻�
     * @param passwords  passwordsΪ�˻�����
     * @return
     */
    private static DataOutputStream out = null;
    private static DataInputStream in = null;


    public static DataInputStream getInPutStream(){
        if(in==null){
            return null;
        }else {
            return in;
        }
    }
    /**
     * ��ȡ�������������
     * @return
     */
    public static Socket getConnect(){
        try {
            Socket soc = new Socket("192.168.31.70",8800);
            System.out.println("���ӳɹ�");
            return soc;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * һ���������
     * 1�������������˺Ų����ڵ���Ϣ 10
     * 2�������������˺Ŵ��ڡ�������������Ϣ 11
     * 3�������������˺Ŵ��ڡ�������ȷ�����Ǹ��˺��Ѿ�����½����Ϣ 12
     * 4�����������͹����˺Ŵ��ڣ�������ȷ������û�е�½����Ϣ 13
     * 5��û�������Ϸ����� 14
     * @param account
     * @param password
     * @return
     */
    public static byte login(String account,String password){
        System.out.println("���͵��˺�Ϊ��"+account+" ����Ϊ��"+password);
        Socket soc = getConnect();
        try {
            DataOutputStream dout = new DataOutputStream(soc.getOutputStream());
            DataInputStream  dint = new DataInputStream(soc.getInputStream());
            dout.write(1);
            //�����˺���Ϣ
            dout.writeUTF(account);
            dout.writeByte(255);
            dout.writeUTF(password);
            dout.writeByte(255);
            //���շ���������
            byte fankui = dint.readByte();
            if(fankui==13){
                System.out.println("�ɹ���½��");
                out = dout;
                in = dint;
                dout.writeBoolean(true);
                return fankui;
            }
            dout.writeBoolean(false);
            sendMsgExit();
            return fankui;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 14;
    }

    /**
     * ���շ������˷��͹����ĺ�����Ϣ
     * @return
     */
    public static String waitForHaoYouMsg(){
        if(in==null){
            return null;
        }else {
            //���շ��������͹����ĺ�����Ϣ
            try {
                System.out.println("�����ˣ�");
                String msg = in.readUTF();
                byte end = in.readByte();
                return msg;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * ��ȡ������
     */
    public DataOutputStream getDataOut(){
        return out;
    }
    public DataInputStream getDataIn(){
        return in;
    }

    /**
     * �����������Ϳͻ��˳�����Ϣ
     */
    public static boolean sendExitMsg(){
        return sendMsgExit();
    }

    private static boolean sendMsgExit(){
        if(out==null){
            return false;
        }else {
            try {
                out.write(5);
                return false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * ע���˺�ר�õ�����������й�ͨ�ķ���
     * 1���������������֤qq�����Ƿ��Ѿ���ע�������
     * @return
     * ����һ��byteֵ �ֱ����������
     *     1������������⵽��qq�����Ѿ���ע��---->1
     *     2����������⵽��qq���뻹û�б�ע��---->2
     *     3�����Ӳ��Ϸ�����---->3
     */
    public static byte vertifyQQnum(String qq){
        Socket soc = getConnect();
        try {
            //��ȡ�������������������������Ϣ�Խ�
            DataInputStream dint = new DataInputStream(soc.getInputStream());
            DataOutputStream dout = new DataOutputStream(soc.getOutputStream());
            dout.write(3);//����ע���˺ŵ�����
            int len = qq.length();
            dout.writeInt(len);
            dout.write(qq.getBytes());
            boolean stage = dint.readBoolean();
            if(stage){
                return 1;
            }else {
                out = dout;
                in = dint;
                return 2;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 3;
    }



    /**
     * ��������
     * @param msg
     */
    public static void sendStr(String msg){
        if(out==null){
            return;
        }else {
            try {
                out.writeUTF(msg);
                out.writeByte(255);
                System.out.println("���ͳɹ���");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * �û�ע��ʱ��Ҫ�õ�����Ϣ���ͷ���
     * @return
     */
    public static boolean readBool(){
        if(in==null){
            return false;
        }else {
            try {
                boolean stage = in.readBoolean();
                return stage;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    /**
     * �����û�ע�����Ϣ
     */
    public static void sendBool(boolean bool){
        if(out==null){
            return;
        }else {
            try {
                out.writeBoolean(bool);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * �����û�ע�����Ϣ
     * @param stage
     */
    public static void sendVertifyMsg(byte stage){
        if(out==null){
            return;
        }else {
            try {
                out.writeByte(stage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ��socket�������һ������
     */
    public static void setSocketToNull(){
        if(out==null&&in==null){
            return;
        }else {
            try {
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            out = null;
            in = null;
        }
    }

    /**
     * �����һ�����ķ���
     * @param acc
     * @param qq
     * @return
     */
    public static byte sendAccountAndQQnumMsg(String acc,String qq){
        Socket soc = getConnect();
        try {
            DataInputStream dint = new DataInputStream(soc.getInputStream());
            DataOutputStream dout = new DataOutputStream(soc.getOutputStream());
            dout.writeByte(2);
            dout.writeUTF(acc);
            dout.writeByte(255);
            dout.writeUTF(qq);
            dout.writeByte(255);
            byte byt = dint.readByte();
            if(byt==22){
                out = dout;
                in = dint;
            }
            return byt;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 23;
    }










}
