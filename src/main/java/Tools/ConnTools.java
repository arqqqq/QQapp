package Tools;

import TableClass.Account;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * 与数据库进行沟通的类
 * 1、与数据库进行连接
 * 2、与数据库进行交互，实现代码操作数据库的zscg（增删查改）
 */
public class ConnTools {


    private static final String url = "jdbc:mysql://localhost:3306/zrs";
    private static final String user = "root";
    private static final String password = "root";

    /**
     * 构造器
     * 首先动态加载sql语句操作所需要的类jar包
     * 为后续数据库的处理工作做准备
     */
    public ConnTools(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接数据库。。。
     * 获得Connection对象
     * @return
     */
    private static Connection connectSQL(){
        try {
            Connection conn = DriverManager.getConnection(url,user,password);
            return conn;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * 验证账号登陆返回四个情况
     * 1、账号不存在  10
     * 2、账号存在，但是密码错误  11
     * 3、账号存在，密码正确，但是该账号已经登陆  12
     * 4、账号存在，密码正确，并且登陆状态还为未登陆  13
     * @param acc
     * @param pass
     * @return
     */
    public static byte vertifyAccount(String acc,String pass)  {
        Connection conn = connectSQL();
        ResultSet rs = null;
        try {
            Statement stml = conn.createStatement();
            System.out.println("账户："+acc+"密码："+pass);
            String sql = "select *from account where account="+acc+"&& password = "+pass;
            rs = stml.executeQuery(sql);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(rs!=null){
                while (rs.next()){
                    if(acc.equals(rs.getString(1))){
                        if(pass.equals(rs.getString(2))){
                            if(rs.getInt(6)!=1){
                                System.out.println("返回"+13);
                                rs.close();
                                stml.close();
                                return 13;
                            }
                            System.out.println("返回"+12);
                            rs.close();
                            stml.close();
                            return 12;
                        }
                        System.out.println("返回"+11);
                        rs.close();
                        stml.close();
                        return 11;
                    }
                }
            }
            rs.close();
            stml.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        System.out.println("返回"+10);
        return 10;
    }

    /**
     * 设置账户名为account的账户的等录状态
     * @param account
     * @return
     */
    public static boolean setLoginStage(String account,int stage){
           Connection conn = connectSQL();
           String sql = "UPDATE account SET stage =? where account ="+account;
        try {
            PreparedStatement prml = conn.prepareStatement(sql);
            prml.setInt(1,stage);
            prml.execute();
            prml.close();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 验证qq号是否已经存在，两种情况
     * 1、qq号已经被注册---->true
     * 2、qq号没有被注册或者qq号根本不存在---->false
     * @param qq
     * @return
     */
    public static boolean vertifyQQ(String qq){
        Connection conn = connectSQL();
        try {
            Statement stml = conn.createStatement();
            String sql = "select *from account where qqnum = "+qq;
            ResultSet rs = stml.executeQuery(sql);
            //停顿一秒，确保获取到mySQL中的数据
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (rs.next()){
                if(rs.getString(5).equals(qq)){
                    return true;
                }
            }
            //关闭
            rs.close();
            stml.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        return false;
    }

    /**
     * 用于判断客户端发送过来的账号是否已经被注册
     * 1、已经被注册----true
     * 2、没有被注册----false
     * @param account
     * @return
     */
    public static boolean vertifyAccount(String account){
        try {
            //获取与数据库的连接
            Connection conn = connectSQL();
            Statement stml = conn.createStatement();
            String sql = "select *from account where account ="+account;
            ResultSet rs = stml.executeQuery(sql);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (rs.next()){
                if(rs.getString(1).equals(account)){
                    return true;
                }
            }
            rs.close();
            stml.close();
            conn.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 将用户数据写入mysql
     * @param acc
     */
    public static void addAccount(Account acc){
        try {
            Connection conn = connectSQL();
            String sql = "insert into account values(?,?,?,?,?,?,?)";
            PreparedStatement prml = conn.prepareStatement(sql);
            prml.setString(1,acc.getAccount());
            prml.setString(2,acc.getPassword());
            prml.setString(3,acc.getIDname());
            prml.setString(4,acc.getPriname());
            prml.setString(5,acc.getQqnum());
            prml.setInt(6,0);
            prml.setString(7,"null");
            prml.execute();
            prml.close();
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    /**
     * 从数据库获取该账号的好友信息
     * @param account 要查询的账号
     * @return
     * 存在该账号的话，返回好友信息
     * 不存在的话，返回null
     */
    public static String getHaoYouMgs(String account){
        Connection conn = connectSQL(); //获取与数据库的连接
        try {
            Statement stml = conn.createStatement();
            String sql = "select *from account where account="+account;
            ResultSet rs = stml.executeQuery(sql);
            try {
                TimeUnit.SECONDS.sleep((long) 0.5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (rs.next()){
                if(rs.getString(1).equals(account)){
                    System.out.println("查询结果:"+rs.getString(7));
                    return rs.getString(7);
                }
            }
            rs.close();
            stml.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 查询账号信息和查询账号和qq号码的关联情况
     * 1、账号不存在---->20
     * 2、账号存在但是qq不与其关联---->21
     * 3、账号存在并且qq号是相关联的---->22
     * @param account
     * @param qqnum
     * @return
     */
    public static byte isAccountAndQQnumGuanlian(String account,String qqnum){
        //获取与数据库的连接
        Connection conn = connectSQL();
        try {
            Statement stml = conn.createStatement();
            String sql = "select *from account where account="+account+"&& qqnum="+qqnum;
            ResultSet rs = stml.executeQuery(sql);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (rs.next()){
                if(account.equals(rs.getString(1))){
                    if(qqnum.equals(rs.getString(5))){
                        rs.close();
                        stml.close();
                        return 22;
                    }
                    rs.close();
                    stml.close();
                    return 21;
                }
            }
            rs.close();
            stml.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
         return 20;
    }

    /**
     * 修改密码的方法
     * @param account
     * @param pass
     */
    public static void changPassByAccount(String account, String pass) {
        Connection conn = connectSQL();
        try {
            String sql = "UPDATE account set password =? where account =?";
            PreparedStatement stml = conn.prepareStatement(sql);
            stml.setString(1,pass);
            stml.setString(2,account);
            stml.execute();
            stml.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
