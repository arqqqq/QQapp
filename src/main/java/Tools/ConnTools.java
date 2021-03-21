package Tools;

import TableClass.Account;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
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
     * 注册账号
     */
    public static void registAccount(String qqnum){
        JFrame newframe = new JFrame();
        newframe.setTitle("账号注册");
        newframe.setSize(400,400);
        newframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        newframe.setLocationRelativeTo(null);

        newframe.setLayout(null);
        //添加组件 先添加标签
        JLabel label1 = new JLabel("账号:");
        JLabel label2 = new JLabel("密码");
        JLabel label3 = new JLabel("确认密码");
        label1.setBounds(50,30,100,30);
        label2.setBounds(50,130,100,30);
        label3.setBounds(50,230,100,30);
        newframe.add(label1);
        newframe.add(label2);
        newframe.add(label3);
        //再添加输入框
        JTextField text = new JTextField();
        JPasswordField pass1 = new JPasswordField();
        JPasswordField pass2 = new JPasswordField();
        text.setBounds(150,30,200,30);
        pass1.setBounds(150,130,200,30);
        pass2.setBounds(150,230,200,30);
        newframe.add(text);
        newframe.add(pass1);
        newframe.add(pass2);

        //设置newFrame可见
        newframe.setVisible(true);


        //添加按钮
        JButton btn1 = new JButton("注册");
        JButton btn2 = new JButton("取消");
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
                    JOptionPane.showConfirmDialog(null,"账户必须为五位的数字");
                    text.setText("");
                    pass1.setText("");
                    pass2.setText("");
                    return;
                }
                if(!pass.equals(repass)){
                    JOptionPane.showConfirmDialog(null,"两次输入的密码不正确");
                    return;
                }

                Account acc = new Account(account,pass,"qquser","helloworld",qqnum);
                creatAccount(acc);
                System.out.println("注册成功");
                newframe.dispose();
                return;

            }
        });
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!(JOptionPane.showConfirmDialog(null,"确认退出？")==JOptionPane.CANCEL_OPTION)){
                    newframe.dispose();
                }
            }
        });
    }

    /**
     * 验证信息阶段
     */
    public static void verifyFrame(){
        final boolean[] isExites = {false};
        final String[] originalVerity = {""};
        JFrame verityframe = new JFrame();
        //设置基本属性
        verityframe.setTitle("验证窗口");
        verityframe.setSize(400,270);
        verityframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        verityframe.setLocationRelativeTo(null);

        //添加组件
        verityframe.setLayout(null);
        JLabel lab1 = new JLabel("qq号码");
        JLabel lab2 = new JLabel("验证码");
        JTextField field1 = new JTextField();
        JTextField field2 = new JTextField();
        //设置位置
        lab1.setBounds(50,50,100,30);
        lab2.setBounds(50,110,100,30);
        field1.setBounds(150,50,200,30);
        field2.setBounds(150,110,200,30);
        verityframe.add(lab1);
        verityframe.add(lab2);
        verityframe.add(field1);
        verityframe.add(field2);

        JButton btn = new JButton("发送");
        JButton btn1 = new JButton("确认");
        final JLabel lab3 = new JLabel();
        btn.setBounds(50,170,70,30);
        lab3.setBounds(120,170,160,30);
        btn1.setBounds(250,170,70,30);
        verityframe.add(btn);
        verityframe.add(btn1);
        verityframe.add(lab3);

        //设置可见
        verityframe.setVisible(true);

        //这个final线程组是用于发送邮件成功后，显示验证码有效期而特意设定的
        final Thread[] showThr = {null};
        //添加监听器
        btn.addActionListener(new ActionListener() {
            /**
             * 当按下发送验证码的按钮后，服务器向客户端发送一个验证消息
             * 进行身份验证
             * 获取qq邮箱输入框里面的信息，检测邮箱是否正确
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!isExites[0]){
                    //先获取qq邮箱输入框里面的Text信息
                    String qqmail = field1.getText();

                    //首先判断这个qq号是否在sql中存在
                    if(numIsExist(qqmail)){
                        JOptionPane.showConfirmDialog(null,"该qq账号已经注册！");
                        return;
                    }

                    System.out.println(qqmail);
                    if(qqmail.equals("")){
                        JOptionPane.showConfirmDialog(null,"输入的qq邮箱为空");
                        return;
                    }else{
                        String verityCode = SendMsg.sendMessages(qqmail);
                        if(verityCode==null){
                            JOptionPane.showConfirmDialog(null,"输入的qq号码非法！或者请检测网络连接");
                            return;
                        }else {
                            originalVerity[0] = verityCode;
                            //启动一个线程，显示秒数
                            showThr[0] = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    int i=0;
                                    isExites[0] = true;
                                    while (i<120){
                                        try {
                                            lab3.setText((120-i)+"秒后可重新发送");
                                            i++;
                                            TimeUnit.SECONDS.sleep(1);
                                        } catch (InterruptedException interruptedException) {
                                            interruptedException.printStackTrace();
                                            break;
                                        }
                                    }
                                    isExites[0] = false;
                                }
                            },"标签线程");
                            showThr[0].start();
                            return;
                        }
                    }
                }else {
                    JOptionPane.showConfirmDialog(null,"请120秒后重新发送！");
                }
            }
        });

        //为确认按钮添加监听器
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(originalVerity[0]==null||showThr[0]==null){
                    JOptionPane.showConfirmDialog(null,"请发送验证码！");
                    return;
                }else if(!showThr[0].isAlive()){
                    JOptionPane.showConfirmDialog(null,"验证码已经过期，请重新发送");
                    return;
                }else {
                    String code = field2.getText();
                    if(originalVerity[0].equals(code)){
                        //弹出新的窗口
                        showThr[0].interrupt();
                        lab3.setText("");
                        verityframe.dispose();
                        registAccount(field1.getText());
                        return;
                    }else {
                        JOptionPane.showConfirmDialog(null,"验证码错误！");
                        return;
                    }
                }
            }
        });

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
     * 找回密码的界面demo
     * 当用户点击找回密码的窗口时，执行找回密码的方法
     */
    public static void retPassword(){

        JFrame retPass = new JFrame();
        retPass.setTitle("找回密码");
        retPass.setSize(new Dimension(400,400));
        retPass.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        retPass.setLocationRelativeTo(null);

        //添加组件
        JLayeredPane layP = new JLayeredPane();

        //添加背景图片
        ImageIcon icon = new ImageIcon("search.jpg");
        Image image = icon.getImage().getScaledInstance(400,400,1);
        JLabel lab1 = new JLabel(new ImageIcon(image));
        lab1.setBounds(0,0,400,430);
        JPanel pan = new JPanel();
        pan.setBounds(0,0,400,400);
        pan.add(lab1);
        layP.add(pan,JLayeredPane.DEFAULT_LAYER);

        //添加界面上的组件
        JLabel lab2 = new JLabel("关联qq号:");
        lab2.setForeground(Color.YELLOW);
        JLabel lab3 = new JLabel("验证码:");
        lab3.setForeground(Color.YELLOW);
        JLabel lab4 = new JLabel("新密码:");
        lab4.setForeground(Color.YELLOW);
        JLabel lab5 = new JLabel("确认新密码:");
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
        JButton btn = new JButton("发送");
        JButton btn1 = new JButton("确认修改");
        btn.setBounds(50,330,100,30);
        btn1.setBounds(250,330,100,30);
        layP.add(btn1,JLayeredPane.DRAG_LAYER);
        layP.add(btn,JLayeredPane.DRAG_LAYER);

        retPass.setVisible(true);
        retPass.setLayeredPane(layP);
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
