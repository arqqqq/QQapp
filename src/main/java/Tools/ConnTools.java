package Tools;

import TableClass.Account;


import java.sql.*;


/**
 * �����ݿ���й�ͨ����
 * 1�������ݿ��������
 * 2�������ݿ���н�����ʵ�ִ���������ݿ��zscg����ɾ��ģ�
 */
public class ConnTools {


    private static final String url = "jdbc:mysql://localhost:3306/zrs";
    private static final String user = "root";
    private static final String password = "root";

    /**
     * ������
     * ���ȶ�̬����sql����������Ҫ����jar��
     * Ϊ�������ݿ�Ĵ�������׼��
     */
    public ConnTools(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * �������ݿ⡣����
     * ���Connection����
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
     * ��½��֤ ��ֹsqlע��
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
     * �жϸ��˺�״̬�Ƿ�ʱ�Ѿ���½
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
     * �����ݿ�������µ��û�Ԫ��
     * @param account
     */
    public static void creatAccount(Account account){
        Connection conn = connectSQL();
        try {

            String sql = "INSERT INTO account values(?,?,?,?,?)";
            PreparedStatement prml =conn.prepareStatement(sql);

            //�������
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
     * ��ϱ�����Ƿ������ͬ��qq����
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
     * �����ݿ������ѯqq���ݣ����qq�˺��Ƿ��Ѿ���ע��
     * @param qqnum
     * @return  ����һ������ֵ
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
     * �����˻������˻�����д��mysql��
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
