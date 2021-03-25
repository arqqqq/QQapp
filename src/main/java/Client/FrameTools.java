package Client;

import Tools.ConnTools;
import Tools.SendMsg;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class FrameTools {


    public static void registFrame(){
        final boolean[] isSend = {false};
        JFrame frames = new JFrame();
        frames.setTitle("注册窗口");
        frames.setSize(new Dimension(400,500));
        frames.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frames.setLocationRelativeTo(null);

        JLayeredPane frame = new JLayeredPane();

        ImageIcon icon = new ImageIcon("search.jpg");
        Image img = icon.getImage().getScaledInstance(400,500,1);
        JLabel lab = new JLabel(new ImageIcon(img));
        JPanel pan = new JPanel();
        lab.setBounds(0,0,400,500);
        pan.setBounds(0,0,400,500);
        pan.add(lab);
        frame.add(pan,JLayeredPane.DEFAULT_LAYER);


        JLabel lab1 = new JLabel("qq号码：");
        lab1.setForeground(Color.YELLOW);
        JLabel lab2 = new JLabel("验证码：");
        lab2.setForeground(Color.YELLOW);
        JLabel lab3 = new JLabel("账号：");
        lab3.setForeground(Color.YELLOW);
        JLabel lab4 = new JLabel("密码：");
        lab4.setForeground(Color.YELLOW);
        JTextField fil1 = new JTextField();
        fil1.setBackground(new Color(211,211,211));
        JTextField fil2 = new JTextField();
        fil2.setBackground(new Color(211,211,211));
        JTextField fil3 = new JTextField();
        fil3.setEnabled(false);//设置不可编展
        fil3.setBackground(new Color(211,211,211));
        JPasswordField fil4 = new JPasswordField();
        fil4.setBackground(new Color(211,211,211));
        fil4.setEnabled(false);
        lab1.setBounds(50,70,100,30);
        lab2.setBounds(50,150,100,30);
        lab3.setBounds(50,230,100,30);
        lab4.setBounds(50,310,100,30);
        fil1.setBounds(150,70,200,30);
        fil2.setBounds(150,150,200,30);
        fil3.setBounds(150,230,200,30);
        fil4.setBounds(150,310,200,30);
        frame.add(lab1,JLayeredPane.DRAG_LAYER);
        frame.add(lab2,JLayeredPane.DRAG_LAYER);
        frame.add(lab3,JLayeredPane.DRAG_LAYER);
        frame.add(lab4,JLayeredPane.DRAG_LAYER);
        frame.add(fil1,JLayeredPane.DRAG_LAYER);
        frame.add(fil2,JLayeredPane.DRAG_LAYER);
        frame.add(fil3,JLayeredPane.DRAG_LAYER);
        frame.add(fil4,JLayeredPane.DRAG_LAYER);

        //添加按钮
        JLabel labels = new JLabel("请发送验证码");
        labels.setForeground(Color.YELLOW);
        labels.setBounds(50,340,300,50);
        labels.setFont(new Font("微软雅黑",Font.BOLD,16));
        labels.setHorizontalAlignment(JLabel.CENTER);
        frame.add(labels,JLayeredPane.DRAG_LAYER);

        JButton btn1 = new JButton("发送");
        JButton btn2 = new JButton("验证");
        JButton btn3 = new JButton("注册");
        btn1.setBounds(50,390,60,30);
        btn2.setBounds(170,390,60,30);
        btn3.setBounds(290,390,60,30);
        frame.add(btn1,JLayeredPane.DRAG_LAYER);
        frame.add(btn2,JLayeredPane.DRAG_LAYER);
        frame.add(btn3,JLayeredPane.DRAG_LAYER);
        final boolean[] isClice = {false};
        final String[] code = {""};
        final Thread[] thr = {null};
        //添加监听器
        btn1.addActionListener(e -> {
           if(!isClice[0]){
               //首先判断获取到的qq号码是否合法
               String qqnum = fil1.getText();
               if(qqnum.length()>11||qqnum.length()<7){
                   //当qq号码的长度大于11位或者小于7位时
                   JOptionPane.showConfirmDialog(null,"您输入的qq号码不符合要求");
                   return;
               }
               for(int i=0;i<qqnum.length();i++){
                   char temp = qqnum.charAt(i);
                   if(temp-'0'<0||temp-'0'>9){
                       JOptionPane.showConfirmDialog(null,"qq号码包含非法字符！");
                       return;
                   }
               }
               //当判断成功qq号码满足条件之后，向服务器请求该账号已经被注册
               byte byt = ConnectWithServer.vertifyQQnum(qqnum);
               if(byt==2){
                   //给该qq号发送验证码
                   code[0] = SendMsg.sendMessages(qqnum);
                   System.out.println("验证码为:"+code[0]);
                   if(code[0]==null){
                       JOptionPane.showConfirmDialog(null,"找不到该qq号码！");
                       ConnectWithServer.sendBool(false);
                       ConnectWithServer.sendVertifyMsg((byte) 5);
                       ConnectWithServer.setSocketToNull();
                       return;
                   }else {
                       thr[0] =new Thread(()->{
                           isClice[0] = true;
                           int i=0;
                           for(;i<120;i++){
                               labels.setText((120-i)+"秒后重新发送");
                               try {
                                   TimeUnit.SECONDS.sleep(1);
                               } catch (InterruptedException interruptedException) {
                                   interruptedException.printStackTrace();
                                   break;
                               }
                           }
                           isClice[0] = false;
                           if(i==119){
                               ConnectWithServer.sendBool(false);
                               ConnectWithServer.sendVertifyMsg((byte) 5);
                               ConnectWithServer.setSocketToNull();
                           }
                       });
                       thr[0].start();
                   }
               }else if(byt==3){
                   JOptionPane.showConfirmDialog(null,"连接超时！");
                   return;
               }else if(byt==1){
                   JOptionPane.showConfirmDialog(null,"该qq号码已被注册！");
                   return;
               }
           }
           else {
               JOptionPane.showConfirmDialog(null,"验证码已经发送！请稍后重试");
               return;
           }
        });
        btn2.addActionListener(e -> {
            String vetcode = fil2.getText();
            if(vetcode.equals(code[0])){
                JOptionPane.showConfirmDialog(null,"验证码正确！");
                //将账号密码输入框设置为可编辑
                fil3.setEnabled(true);
                fil4.setEnabled(true);
                System.out.println("编辑成功！");
                thr[0].interrupt();
                thr[0] = null;
                ConnectWithServer.sendBool(true);
            }else {
                JOptionPane.showConfirmDialog(null,"验证码错误！");
            }
        });
        //为注册按钮添加监听器
        btn3.addActionListener(e->{
            if(fil3.isEditable()&&fil4.isEditable()){
                String account = fil3.getText();
                String password = new String(fil4.getPassword());
                if(account.length()!=5){
                    JOptionPane.showConfirmDialog(null,"账号必须为五位的数字");
                    return;
                }
                for(int i=0;i<account.length();i++){
                    if(account.charAt(i)-'0'<0||account.charAt(i)-'0'>9){
                        JOptionPane.showConfirmDialog(null,"您输入的账号不合法！");
                        return;
                    }
                }
                if(password.getBytes().length>30){
                    JOptionPane.showConfirmDialog(null,"密码位数过多！");
                    return;
                }
                //通过前面的验证的话，则进入账号发送阶段
                ConnectWithServer.sendStr(account);
                System.out.println("发送成功！");
                boolean bool = ConnectWithServer.readBool();
                if(!bool){
                    ConnectWithServer.sendVertifyMsg((byte) 15);
                    ConnectWithServer.sendStr(password);
                    //断开连接
                    ConnectWithServer.sendVertifyMsg((byte) 5);
                    ConnectWithServer.setSocketToNull();//将socket置空
                    frames.dispose();//关闭窗口

                }else {
                    JOptionPane.showConfirmDialog(null,"该账号已经被注册！");
                    return;
                }
            }else {
                JOptionPane.showConfirmDialog(null,"请发送验证码");
                return;
            }
        });

        frames.setLayeredPane(frame);
        frames.setVisible(true);
    }



}
