package Tools;

import TableClass.Account;


import java.sql.*;


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
     * 登陆验证 防止sql注入
     * @param account
     * @param password
     * @return
     */
    public static boolean verityLog(String account,String password){
        Connection conn = connectSQL();
        try {
            Statement stml = conn.createStatement();
            String sql = "select *from account where account = "+account+"&& password ="+password;
            ResultSet rs = stml.executeQuery(sql);
            int moderindex = 0;
            while (rs.next()){
                if(account.equals(rs.getString(1))&&password.equals(rs.getString(2))){
                    return true;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    /**
     * 判断该账号状态是否时已经登陆
     * @param account
     * @return
     */
    public static boolean isLogin(String account){
        Connection conn = connectSQL();
        try {
            Statement stml = conn.createStatement();
            String sql = "select *from account where account ="+account;
            ResultSet rs = stml.executeQuery(sql);
            while (rs.next()){
                if(account.equals(rs.getString(1))&&rs.getInt(6)!=0){
                    return true;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }


    /**
     * 向数据库中添加新的用户元素
     * @param account
     */
    public static void creatAccount(Account account){
        Connection conn = connectSQL();
        try {

            String sql = "INSERT INTO account values(?,?,?,?,?)";
            PreparedStatement prml =conn.prepareStatement(sql);

            //传入参数
            prml.setString(1,account.getAccount());
            prml.setString(2,account.getPassword());
            prml.setString(3,account.getIDname());
            prml.setString(4,account.getPriname());
            prml.setString(5,account.getQqnum());

            prml.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    /**
     * 诊断表格中是否存在相同的qq号码
     */
    private static boolean numIsExist(String qqnum){
        Connection conn = connectSQL();
        try {
            Statement stml = conn.createStatement();
            String sql = "select *from account where qqnum = "+qqnum;
            ResultSet rs = stml.executeQuery(sql);
            while (rs.next()){
                if(rs.getString(5).equals(qqnum)){
                    return true;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }




    /**
     * 向数据库请求查询qq数据，检测qq账号是否已经被注册
     * @param qqnum
     * @return  返回一个布尔值
     */
    public static boolean checkQQIsExits(String qqnum){
        Connection conn = connectSQL();
        Statement stml = null;
        try {
            stml = conn.createStatement();
            String sql = "select *from account where qqnum = "+qqnum;
            ResultSet rs = stml.executeQuery(sql);
            while (rs.next()){
                if(qqnum.equals(rs.getString(5))){
                    return true;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    /**
     * 创建账户，将账户数据写入mysql中
     * @param account
     * @param password
     */
    public static void createAccount(String account,String password,String qqnum){
        Connection conn = connectSQL();
        try {
            Account acc = new Account(account,password,"qquser","helloword",qqnum);
            String sql = "insert into account values(?,?,?,?,?,?)";
            PreparedStatement prml = conn.prepareStatement(sql);
            prml.setString(1,acc.getAccount());
            prml.setString(2,acc.getPassword());
            prml.setString(3,acc.getIDname());
            prml.setString(4,acc.getPriname());
            prml.setString(5,acc.getQqnum());
            prml.setInt(6,0);
            prml.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}
