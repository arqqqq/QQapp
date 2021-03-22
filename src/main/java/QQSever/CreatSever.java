package QQSever;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.TransferQueue;

public class CreatSever {

    private java.net.ServerSocket sever = null;
    private LinkedList<ClientObj> cligroup = new LinkedList<>();
    public void createSever(){
        try {
            sever = new ServerSocket(8800);
            System.out.println("sever successfully create��");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ������һֱ�ȴ��ͻ��˵�����
     */
    public void waitClient(){
        if(sever==null){
            throw new RuntimeException("��������δ����");
        }else {
            while (true){
                try {
                    //�ȴ��ͻ��˵�����
                    Socket so = sever.accept();
                    ClientObj cli = new ClientObj(so,this);
                    cli.start();
                    cligroup.add(cli);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * �Ƴ����ߵĿͻ���
     */
    public void removeClient(ClientObj cli){
        cligroup.remove(cli);
    }

    /**
     * ��ȡĳ���ͻ��˵��̶߳���
     */
    public ClientObj getClient(String account){
        for(int i=0;i<cligroup.size();i++){
            if(account.equals(cligroup.get(i).getAccount())){
                return cligroup.get(i);
            }
        }
        return null;
    }


    public static void main(String[] args){
        CreatSever sever = new CreatSever();
        sever.createSever();
        sever.waitClient();
    }

}
