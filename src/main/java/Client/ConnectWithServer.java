package Client;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectWithServer{


    /**
     * 点击登陆按钮的远程登陆操作
     * @param account  account为账户
     * @param passwords  passwords为账户密码
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
     * 获取与服务器的连接
     * @return
     */
    public static Socket getConnect(){
        try {
            Socket soc = new Socket("192.168.31.70",8800);
            System.out.println("连接成功");
            return soc;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 一共五种情况
     * 1、服务器发送账号不存在的信息 10
     * 2、服务器发送账号存在、但密码错误的信息 11
     * 3、服务器发送账号存在、密码正确、但是该账号已经被登陆的信息 12
     * 4、服务器发送过来账号存在，密码正确，并且没有登陆的信息 13
     * 5、没有连接上服务器 14
     * @param account
     * @param password
     * @return
     */
    public static byte login(String account,String password){
        System.out.println("发送的账号为："+account+" 密码为："+password);
        Socket soc = getConnect();
        try {
            DataOutputStream dout = new DataOutputStream(soc.getOutputStream());
            DataInputStream  dint = new DataInputStream(soc.getInputStream());
            dout.write(1);
            //发送账号信息
            dout.writeUTF(account);
            dout.writeByte(255);
            dout.writeUTF(password);
            dout.writeByte(255);
            //接收服务器反馈
            byte fankui = dint.readByte();
            if(fankui==13){
                System.out.println("成功登陆！");
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
     * 接收服务器端发送过来的好友信息
     * @return
     */
    public static String waitForHaoYouMsg(){
        if(in==null){
            return null;
        }else {
            //接收服务器发送过来的好友信息
            try {
                System.out.println("进来了！");
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
     * 获取流对象
     */
    public DataOutputStream getDataOut(){
        return out;
    }
    public DataInputStream getDataIn(){
        return in;
    }

    /**
     * 给服务器发送客户退出的消息
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
     * 注册账号专用的与服务器进行沟通的方法
     * 1、向服务器申请验证qq号码是否已经被注册的请求
     * @return
     * 返回一个byte值 分别有三种情况
     *     1、当服务器检测到该qq号码已经被注册---->1
     *     2、服务器检测到该qq号码还没有被注册---->2
     *     3、连接不上服务器---->3
     */
    public static byte vertifyQQnum(String qq){
        Socket soc = getConnect();
        try {
            //获取输入输出流，跟服务器进行信息对接
            DataInputStream dint = new DataInputStream(soc.getInputStream());
            DataOutputStream dout = new DataOutputStream(soc.getOutputStream());
            dout.write(3);//发送注册账号的请求
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
     * 公共方法
     * @param msg
     */
    public static void sendStr(String msg){
        if(out==null){
            return;
        }else {
            try {
                out.writeUTF(msg);
                out.writeByte(255);
                System.out.println("发送成功！");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 用户注册时需要用到的信息发送方法
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
     * 发送用户注册的消息
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
     * 发送用户注册的信息
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
     * 对socket对象进行一个处理
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
     * 用于找回密码的方法
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
