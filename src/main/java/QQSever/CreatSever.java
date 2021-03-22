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
            System.out.println("sever successfully create！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 服务器一直等待客户端的连接
     */
    public void waitClient(){
        if(sever==null){
            throw new RuntimeException("服务器还未创建");
        }else {
            while (true){
                try {
                    //等待客户端的连接
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
     * 移除下线的客户端
     */
    public void removeClient(ClientObj cli){
        cligroup.remove(cli);
    }

    /**
     * 获取某个客户端的线程对象
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
