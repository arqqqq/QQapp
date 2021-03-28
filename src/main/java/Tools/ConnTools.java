package Tools;

import TableClass.Account;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


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
     * ��֤�˺ŵ�½�����ĸ����
     * 1���˺Ų�����  10
     * 2���˺Ŵ��ڣ������������  11
     * 3���˺Ŵ��ڣ�������ȷ�����Ǹ��˺��Ѿ���½  12
     * 4���˺Ŵ��ڣ�������ȷ�����ҵ�½״̬��Ϊδ��½  13
     * @param acc
     * @param pass
     * @return
     */
    public static byte vertifyAccount(String acc,String pass)  {
        Connection conn = connectSQL();
        ResultSet rs = null;
        try {
            Statement stml = conn.createStatement();
            System.out.println("�˻���"+acc+"���룺"+pass);
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
                                System.out.println("����"+13);
                                rs.close();
                                stml.close();
                                return 13;
                            }
                            System.out.println("����"+12);
                            rs.close();
                            stml.close();
                            return 12;
                        }
                        System.out.println("����"+11);
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
        System.out.println("����"+10);
        return 10;
    }

    /**
     * �����˻���Ϊaccount���˻��ĵ�¼״̬
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
     * ��֤qq���Ƿ��Ѿ����ڣ��������
     * 1��qq���Ѿ���ע��---->true
     * 2��qq��û�б�ע�����qq�Ÿ���������---->false
     * @param qq
     * @return
     */
    public static boolean vertifyQQ(String qq){
        Connection conn = connectSQL();
        try {
            Statement stml = conn.createStatement();
            String sql = "select *from account where qqnum = "+qq;
            ResultSet rs = stml.executeQuery(sql);
            //ͣ��һ�룬ȷ����ȡ��mySQL�е�����
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
            //�ر�
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
     * �����жϿͻ��˷��͹������˺��Ƿ��Ѿ���ע��
     * 1���Ѿ���ע��----true
     * 2��û�б�ע��----false
     * @param account
     * @return
     */
    public static boolean vertifyAccount(String account){
        try {
            //��ȡ�����ݿ������
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
     * ���û�����д��mysql
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
     * �����ݿ��ȡ���˺ŵĺ�����Ϣ
     * @param account Ҫ��ѯ���˺�
     * @return
     * ���ڸ��˺ŵĻ������غ�����Ϣ
     * �����ڵĻ�������null
     */
    public static String getHaoYouMgs(String account){
        Connection conn = connectSQL(); //��ȡ�����ݿ������
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
                    System.out.println("��ѯ���:"+rs.getString(7));
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
     * ��ѯ�˺���Ϣ�Ͳ�ѯ�˺ź�qq����Ĺ������
     * 1���˺Ų�����---->20
     * 2���˺Ŵ��ڵ���qq���������---->21
     * 3���˺Ŵ��ڲ���qq�����������---->22
     * @param account
     * @param qqnum
     * @return
     */
    public static byte isAccountAndQQnumGuanlian(String account,String qqnum){
        //��ȡ�����ݿ������
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
     * �޸�����ķ���
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
