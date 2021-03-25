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
     * �Ƴ��ͻ��ζ���
     * @param cli
     * @return
     */
    public boolean removeCli(ClientObj cli){
        return cligroup.remove(cli);
    }


    public static void main(String[] args){
        CreatSever sever = new CreatSever();
        sever.createSever();
        sever.waitClient();
    }



}
