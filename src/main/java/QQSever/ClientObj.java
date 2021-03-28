package QQSever;


import TableClass.Account;
import Tools.ConnTools;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.System.*;

public class ClientObj extends Thread{


    /**
     * �������Ψһ��Ӧ����������� dout
     * �������Ψһ��Ӧ����������� dint
     */
    private String account;
    private DataOutputStream dout;
    private DataInputStream dint;
    private static int Count=0;
    private ConcurrentHashMap<String,DataInputStream> dataInGroup;
    private ConcurrentHashMap<String , DataOutputStream> dataOutGroup;
    private boolean isAlive = true;

    /**
     * ������
     */
    public ClientObj(Socket soc,ConcurrentHashMap<String,DataInputStream> dataInGroup,ConcurrentHashMap<String , DataOutputStream> dataOutGroup){
        super("�ͻ���"+Count++);
        this.dataInGroup = dataInGroup;
        this.dataOutGroup = dataOutGroup;
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
    }

    /**
     * ΪClientThread����һ��Ψһ�ı�ʶ
     */
    public void setAccount(String acc){
        this.account = acc;
    }

    /**
     * ���account
     */
    public String getAccount(){
        return account;
    }

    @Override
    public void run(){
        //���н���
        while (true){
            if(isAlive){
                try {
                    byte stage = dint.readByte();
                    switch (stage){
                        case 1: {
                            login();
                            break;
                        }
                        case 2: {
                            findOutPassword();
                            break;
                        }
                        case 3: {
                            vertifyQQ();
                            break;
                        }
                        case 4:{
                            sendMsg();
                            break;
                        }
                        case 5:{
                            isAlive = false;
                            break;
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

    private void sendMsg() {
        String desAccount = readStr();
        out.println("���͸���"+desAccount);
        String msg = readStr();
        DataOutputStream dou = dataOutGroup.get(desAccount);
        if(dou!=null){
            try {
                dou.writeByte(4);
                dou.writeUTF(getAccount());
                dou.writeByte(255);
                dou.writeUTF(msg);
                dou.writeByte(255);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    /**
     * �һ�����ķ�����һ���������
     * 1���˺Ų�����---->20
     * 2���˺Ŵ��ڵ���qq���벻����---->21
     * 3���˺Ŵ��ڲ�����qq�������---->22
     */
    private void findOutPassword() {
        String account = readStr();
        String qqnum = readStr();
        byte stage = ConnTools.isAccountAndQQnumGuanlian(account,qqnum);
        try {
            dout.writeByte(stage);
            boolean isTrue = dint.readBoolean();
            if(isTrue){
                //�ȴ���������
                String pass = readStr();
                //������洢�����ݿ�
                ConnTools.changPassByAccount(account,pass);
                return;
            }else {
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * �����˺�ע��ķ���
     * �������
     * 1����qq���Ѿ���ע��---->true
     * 2����qq�Ż�û�б�ע����߸��������ڸ�qq��---->false
     */
    private void vertifyQQ() {
        String qqnum = readStr();
        boolean bool = ConnTools.vertifyQQ(qqnum);  //����mysql��ȷ���Ƿ����
        try {
            dout.writeBoolean(bool);//���ʹ�����
            boolean huifu = dint.readBoolean(); //���ս��
            out.println("���յ��ˣ�"+huifu);
            if(huifu){
                //�����û�����
                String account = readStr();
                //����û��˺��Ƿ��Ѿ���ע��
                boolean isVertify = ConnTools.vertifyAccount(account);
                dout.writeBoolean(isVertify);
                if(!isVertify){
                    //˵���˻����Ա�ע��,�����˻����͹������˺���Ϣ
                    /*
                    1���û����յ������������͹���������---->15
                    2���û����յ�������˵���������Ǹ�����������---->16
                     */
                    byte temp = dint.readByte();
                    if(temp==15){
                        out.println("���յ��ˣ�");
                        //�����û���������Ϣ�����û���Ϣд��mysql
                        String pass = readStr();
                        Account AccCli = new Account(account,pass,"qquser","helloword",qqnum);
                        ConnTools.addAccount(AccCli);
                        return;
                    }else {
                        return;
                    }
                }else {
                    //ֱ�ӷ���
                    return;
                }
            }else {
                //ֱ�ӷ���
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * ��ȡString�ķ���
     * @return
     */
    private String readStr(){
        try {
            String msg = dint.readUTF();
            byte a = dint.readByte();
            return msg;
        }catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }

    /**
     * ����String�ַ����ķ���
     * @param str
     */
    private void writeStr(String str){
        if(str==null||str.length()==0) return;
        try {
            dout.writeUTF(str);
            dout.writeByte(255);
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    /**
     * Զ�̵�½����
     */
    private void login() {
        //���շ��͹������˺ź�����
        String account = readStr();
        String password = readStr();
        out.println("���յ����˺�Ϊ��"+account+" ����Ϊ��"+password);
        //��֤�˺ź��������ȷ��
        byte stage = ConnTools.vertifyAccount(account,password);
        out.println("������Ϊ��"+stage);
        try {
            dout.write(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //�ȴ�����Client�ķ�������Ϊһ��booleanֵ
        try {
            boolean isTrue = dint.readBoolean();
            if(isTrue){
                dataInGroup.put(account,dint);
                dataOutGroup.put(account,dout);
                setAccount(account);
                //������account�ĵ�½״̬Ϊ�ѵ�¼
                ConnTools.setLoginStage(account,1);
                String haoYouMsg = ConnTools.getHaoYouMgs(account);
                writeStr(haoYouMsg);
                return;
            }else {
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }



    @Override
    public void finalize(){
        try {
            dout.close();
            dint.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
