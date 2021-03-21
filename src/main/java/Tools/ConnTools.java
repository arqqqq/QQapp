package Tools;

import TableClass.Account;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
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
     * ע���˺�
     */
    public static void registAccount(String qqnum){
        JFrame newframe = new JFrame();
        newframe.setTitle("�˺�ע��");
        newframe.setSize(400,400);
        newframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        newframe.setLocationRelativeTo(null);

        newframe.setLayout(null);
        //������ ����ӱ�ǩ
        JLabel label1 = new JLabel("�˺�:");
        JLabel label2 = new JLabel("����");
        JLabel label3 = new JLabel("ȷ������");
        label1.setBounds(50,30,100,30);
        label2.setBounds(50,130,100,30);
        label3.setBounds(50,230,100,30);
        newframe.add(label1);
        newframe.add(label2);
        newframe.add(label3);
        //����������
        JTextField text = new JTextField();
        JPasswordField pass1 = new JPasswordField();
        JPasswordField pass2 = new JPasswordField();
        text.setBounds(150,30,200,30);
        pass1.setBounds(150,130,200,30);
        pass2.setBounds(150,230,200,30);
        newframe.add(text);
        newframe.add(pass1);
        newframe.add(pass2);

        //����newFrame�ɼ�
        newframe.setVisible(true);


        //��Ӱ�ť
        JButton btn1 = new JButton("ע��");
        JButton btn2 = new JButton("ȡ��");
        btn1.setBounds(70,320,100,30);
        btn2.setBounds(230,320,100,30);
        newframe.add(btn1);
        newframe.add(btn2);

        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String account = text.getText();
                String pass = new String(pass1.getPassword());
                String repass = new String(pass2.getPassword());
                if(account.length()!=5){
                    JOptionPane.showConfirmDialog(null,"�˻�����Ϊ��λ������");
                    text.setText("");
                    pass1.setText("");
                    pass2.setText("");
                    return;
                }
                if(!pass.equals(repass)){
                    JOptionPane.showConfirmDialog(null,"������������벻��ȷ");
                    return;
                }

                Account acc = new Account(account,pass,"qquser","helloworld",qqnum);
                creatAccount(acc);
                System.out.println("ע��ɹ�");
                newframe.dispose();
                return;

            }
        });
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!(JOptionPane.showConfirmDialog(null,"ȷ���˳���")==JOptionPane.CANCEL_OPTION)){
                    newframe.dispose();
                }
            }
        });
    }

    /**
     * ��֤��Ϣ�׶�
     */
    public static void verifyFrame(){
        final boolean[] isExites = {false};
        final String[] originalVerity = {""};
        JFrame verityframe = new JFrame();
        //���û�������
        verityframe.setTitle("��֤����");
        verityframe.setSize(400,270);
        verityframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        verityframe.setLocationRelativeTo(null);

        //������
        verityframe.setLayout(null);
        JLabel lab1 = new JLabel("qq����");
        JLabel lab2 = new JLabel("��֤��");
        JTextField field1 = new JTextField();
        JTextField field2 = new JTextField();
        //����λ��
        lab1.setBounds(50,50,100,30);
        lab2.setBounds(50,110,100,30);
        field1.setBounds(150,50,200,30);
        field2.setBounds(150,110,200,30);
        verityframe.add(lab1);
        verityframe.add(lab2);
        verityframe.add(field1);
        verityframe.add(field2);

        JButton btn = new JButton("����");
        JButton btn1 = new JButton("ȷ��");
        final JLabel lab3 = new JLabel();
        btn.setBounds(50,170,70,30);
        lab3.setBounds(120,170,160,30);
        btn1.setBounds(250,170,70,30);
        verityframe.add(btn);
        verityframe.add(btn1);
        verityframe.add(lab3);

        //���ÿɼ�
        verityframe.setVisible(true);

        //���final�߳��������ڷ����ʼ��ɹ�����ʾ��֤����Ч�ڶ������趨��
        final Thread[] showThr = {null};
        //��Ӽ�����
        btn.addActionListener(new ActionListener() {
            /**
             * �����·�����֤��İ�ť�󣬷�������ͻ��˷���һ����֤��Ϣ
             * ���������֤
             * ��ȡqq����������������Ϣ����������Ƿ���ȷ
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!isExites[0]){
                    //�Ȼ�ȡqq��������������Text��Ϣ
                    String qqmail = field1.getText();

                    //�����ж����qq���Ƿ���sql�д���
                    if(numIsExist(qqmail)){
                        JOptionPane.showConfirmDialog(null,"��qq�˺��Ѿ�ע�ᣡ");
                        return;
                    }

                    System.out.println(qqmail);
                    if(qqmail.equals("")){
                        JOptionPane.showConfirmDialog(null,"�����qq����Ϊ��");
                        return;
                    }else{
                        String verityCode = SendMsg.sendMessages(qqmail);
                        if(verityCode==null){
                            JOptionPane.showConfirmDialog(null,"�����qq����Ƿ�������������������");
                            return;
                        }else {
                            originalVerity[0] = verityCode;
                            //����һ���̣߳���ʾ����
                            showThr[0] = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    int i=0;
                                    isExites[0] = true;
                                    while (i<120){
                                        try {
                                            lab3.setText((120-i)+"�������·���");
                                            i++;
                                            TimeUnit.SECONDS.sleep(1);
                                        } catch (InterruptedException interruptedException) {
                                            interruptedException.printStackTrace();
                                            break;
                                        }
                                    }
                                    isExites[0] = false;
                                }
                            },"��ǩ�߳�");
                            showThr[0].start();
                            return;
                        }
                    }
                }else {
                    JOptionPane.showConfirmDialog(null,"��120������·��ͣ�");
                }
            }
        });

        //Ϊȷ�ϰ�ť��Ӽ�����
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(originalVerity[0]==null||showThr[0]==null){
                    JOptionPane.showConfirmDialog(null,"�뷢����֤�룡");
                    return;
                }else if(!showThr[0].isAlive()){
                    JOptionPane.showConfirmDialog(null,"��֤���Ѿ����ڣ������·���");
                    return;
                }else {
                    String code = field2.getText();
                    if(originalVerity[0].equals(code)){
                        //�����µĴ���
                        showThr[0].interrupt();
                        lab3.setText("");
                        verityframe.dispose();
                        registAccount(field1.getText());
                        return;
                    }else {
                        JOptionPane.showConfirmDialog(null,"��֤�����");
                        return;
                    }
                }
            }
        });

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
     * �һ�����Ľ���demo
     * ���û�����һ�����Ĵ���ʱ��ִ���һ�����ķ���
     */
    public static void retPassword(){

        JFrame retPass = new JFrame();
        retPass.setTitle("�һ�����");
        retPass.setSize(new Dimension(400,400));
        retPass.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        retPass.setLocationRelativeTo(null);

        //������
        JLayeredPane layP = new JLayeredPane();

        //��ӱ���ͼƬ
        ImageIcon icon = new ImageIcon("search.jpg");
        Image image = icon.getImage().getScaledInstance(400,400,1);
        JLabel lab1 = new JLabel(new ImageIcon(image));
        lab1.setBounds(0,0,400,430);
        JPanel pan = new JPanel();
        pan.setBounds(0,0,400,400);
        pan.add(lab1);
        layP.add(pan,JLayeredPane.DEFAULT_LAYER);

        //��ӽ����ϵ����
        JLabel lab2 = new JLabel("����qq��:");
        lab2.setForeground(Color.YELLOW);
        JLabel lab3 = new JLabel("��֤��:");
        lab3.setForeground(Color.YELLOW);
        JLabel lab4 = new JLabel("������:");
        lab4.setForeground(Color.YELLOW);
        JLabel lab5 = new JLabel("ȷ��������:");
        lab5.setForeground(Color.YELLOW);
        JTextField field1 = new JTextField();
        JTextField field2 = new JTextField();
        JPasswordField field3 = new JPasswordField();
        JPasswordField field4 = new JPasswordField();
        lab2.setBounds(50,50,100,30);
        lab3.setBounds(50,130,100,30);
        lab4.setBounds(50,210,100,30);
        lab5.setBounds(50,290,100,30);
        field1.setBounds(150,50,200,30);
        field2.setBounds(150,130,200,30);
        field3.setBounds(150,210,200,30);
        field4.setBounds(150,290,200,30);
        layP.add(lab2,JLayeredPane.DRAG_LAYER);
        layP.add(lab3,JLayeredPane.DRAG_LAYER);
        layP.add(lab4,JLayeredPane.DRAG_LAYER);
        layP.add(lab5,JLayeredPane.DRAG_LAYER);
        layP.add(field1,JLayeredPane.DRAG_LAYER);
        layP.add(field2,JLayeredPane.DRAG_LAYER);
        layP.add(field3,JLayeredPane.DRAG_LAYER);
        layP.add(field4,JLayeredPane.DRAG_LAYER);
        JButton btn = new JButton("����");
        JButton btn1 = new JButton("ȷ���޸�");
        btn.setBounds(50,330,100,30);
        btn1.setBounds(250,330,100,30);
        layP.add(btn1,JLayeredPane.DRAG_LAYER);
        layP.add(btn,JLayeredPane.DRAG_LAYER);

        retPass.setVisible(true);
        retPass.setLayeredPane(layP);
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
