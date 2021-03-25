package QQSever;


import TableClass.Account;
import Tools.ConnTools;
import java.io.*;
import java.net.Socket;
import static java.lang.System.*;

public class ClientObj extends Thread{


    /**
     * 与服务器唯一对应的输出流对象 dout
     * 与服务器唯一对应的输出流对象 dint
     */
    private String account;
    private DataOutputStream dout;
    private DataInputStream dint;
    private static int Count=0;
    private CreatSever mysever;
    private boolean isAlive = true;

    /**
     * 构造器
     */
    public ClientObj(Socket soc,CreatSever mysever){
        super("客户端"+Count++);
        //获取流的对象
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
     * 为ClientThread设置一个唯一的标识
     */
    public void setAccount(String acc){
        this.account = acc;
    }

    /**
     * 获得account
     */
    public String getAccount(){
        return account;
    }

    @Override
    public void run(){
        //进行接收
        while (true){
            if(isAlive){
                try {
                    byte stage = dint.readByte();
                    switch (stage){
                        case 1: login();
                        case 3: vertifyQQ();
                        case 5:{
                            isAlive = false;
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
     * 进行账号注册的方法
     * 两种情况
     * 1、该qq号已经被注册---->true
     * 2、该qq号还没有被注册或者根本不存在该qq号---->false
     */
    private void vertifyQQ() {
        String qqnum = readStr();
        boolean bool = ConnTools.vertifyQQ(qqnum);  //检索mysql，确定是否存在
        try {
            dout.writeBoolean(bool);//发送处理结果
            boolean huifu = dint.readBoolean(); //接收结果
            out.println("接收到了："+huifu);
            if(huifu){
                //接收用户数据
                String account = readStr();
                //检查用户账号是否已经被注册
                boolean isVertify = ConnTools.vertifyAccount(account);
                dout.writeBoolean(isVertify);
                if(!isVertify){
                    //说明账户可以被注册,接收账户发送过来的账号信息
                    /*
                    1、用户接收到反馈，并发送过来了密码---->15
                    2、用户接收到反馈，说服务器我是跟你闹着玩呢---->16
                     */
                    byte temp = dint.readByte();
                    if(temp==15){
                        out.println("接收到了！");
                        //接收用户的密码信息，将用户信息写入mysql
                        String pass = readStr();
                        Account AccCli = new Account(account,pass,"qquser","helloword",qqnum);
                        ConnTools.addAccount(AccCli);
                        return;
                    }else {
                        return;
                    }
                }else {
                    //直接返回
                    return;
                }
            }else {
                //直接返回
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 读取String的方法
     * @return
     */
    private String readStr(){
        try {
            int len = dint.readInt();
            byte byt[] = new byte[len];
            dint.read(byt);
            String msg = new String(byt);
            return msg;
        }catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 发送String字符串的方法
     * @param str
     */
    private void writeStr(String str){
        if(str==null||str.length()==0) return;
        try {
            int len = str.length();
            dout.writeInt(len);
            byte[] byt = str.getBytes();
            dout.write(byt);
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    /**
     * 远程登陆方法
     */
    private void login() {
        //接收发送过来的账号和密码
        String account = readStr();
        String password = readStr();
        out.println("接收到的账号为："+account+" 密码为："+password);
        //验证账号和密码的正确性
        byte stage = ConnTools.vertifyAccount(account,password);
        out.println("处理结果为："+stage);
        try {
            dout.write(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //等代接收Client的反馈反馈为一个boolean值
        try {
            boolean isTrue = dint.readBoolean();
            if(isTrue){
                setAccount(account);
                //并设置account的登陆状态为已登录
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
