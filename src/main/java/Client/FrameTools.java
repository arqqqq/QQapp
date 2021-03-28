package Client;

import Tools.SendMsg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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


    public static void findOutPassFrame(){
        JFrame frame = new JFrame();
        frame.setTitle("密码找回");
        frame.setSize(400,500);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        //添加组件
        JLayeredPane jlp = new JLayeredPane();
        ImageIcon icon = new ImageIcon("Photo\\img.png");
        Image img = icon.getImage().getScaledInstance(400,500,1);
        JLabel lab = new JLabel(new ImageIcon(img));
        lab.setBounds(0,0,400,500);
        JPanel pan = new JPanel();
        pan.setBounds(0,0,400,500);
        pan.add(lab);
        jlp.add(pan,JLayeredPane.DEFAULT_LAYER);
        //添加组件
        JLabel lab1 = new JLabel("账号:");
        JLabel lab2 = new JLabel("绑定qq号:");
        JLabel lab3 = new JLabel("验证码:");
        JLabel lab4 = new JLabel("新密码:");
        JTextField accfil = new JTextField();
        accfil.setBackground(new Color(255,245,238));
        accfil.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                String t = accfil.getText();
                if(t.length()>4) e.consume();
            }
        });
        JTextField qqfil = new JTextField();
        qqfil.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                String t = qqfil.getText();
                if(t.length()>11) e.consume();
            }
        });
        qqfil.setBackground(new Color(255,245,238));
        JTextField vertifyfil = new JTextField();
        vertifyfil.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                String t = vertifyfil.getText();
                if(t.length()>3) e.consume();
            }
        });
        vertifyfil.setBackground(new Color(255,245,238));
        JPasswordField passfil = new JPasswordField();
        passfil.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                String t = new String(passfil.getPassword());
                if(t.length()>29) e.consume();
            }
        });
        passfil.setBackground(new Color(255,245,238));
        lab1.setBounds(50,50,100,30);
        lab2.setBounds(50,110,100,30);
        lab3.setBounds(50,170,100,30);
        lab4.setBounds(50,230,100,30);
        accfil.setBounds(150,50,200,30);
        qqfil.setBounds(150,110,200,30);
        vertifyfil.setBounds(150,170,200,30);
        passfil.setBounds(150,230,200,30);
        jlp.add(lab1,JLayeredPane.DRAG_LAYER);
        jlp.add(lab2,JLayeredPane.DRAG_LAYER);
        jlp.add(lab3,JLayeredPane.DRAG_LAYER);
        jlp.add(lab4,JLayeredPane.DRAG_LAYER);
        jlp.add(accfil,JLayeredPane.DRAG_LAYER);
        jlp.add(qqfil,JLayeredPane.DRAG_LAYER);
        jlp.add(vertifyfil,JLayeredPane.DRAG_LAYER);
        jlp.add(passfil,JLayeredPane.DRAG_LAYER);

        //和显示标签
        JLabel showlab = new JLabel("请输入验证码");
        showlab.setHorizontalAlignment(JLabel.CENTER);
        showlab.setBounds(50,290,300,50);
        jlp.add(showlab,JLayeredPane.DRAG_LAYER);
        //添加按钮
        JButton btn1 = new JButton("发送");
        JButton btn2 = new JButton("修改密码");
        btn1.setBounds(50,370,100,30);
        btn2.setBounds(250,370,100,30);
        jlp.add(btn1,JLayeredPane.DRAG_LAYER);
        jlp.add(btn2,JLayeredPane.DRAG_LAYER);
        //添加监听器
        final String[] vetcode = {null};
        final Thread[] showThr = {null};
        final boolean[] isSend = {false};
        btn1.addActionListener(e->{
            /**
             * 实现步骤
             * 1、首先对读取账号款和qq账号框的内容
             * 2、对账号和qq的合法性进行一个校验
             * 3、将信息发送给服务器端，验证账号的存在性和关联性
             * 4、将信息反馈回来
             * 5、客户端根据信息处理数据，与使用客户进行交互，通过gui
              */
             String account = accfil.getText();
             String qqnum = qqfil.getText();
             if(account.length()!=5){
                 JOptionPane.showConfirmDialog(null,"账号必须位五位的数字");
                 return;
             }
             if(qqnum.length()<8){
                 JOptionPane.showConfirmDialog(null,"qq号码必须在8位到11位之间");
                 return;
             }
             for(int i=0;i<account.length();i++){
                 if(account.charAt(i)-'0'<0||account.charAt(i)-'0'>9){
                     JOptionPane.showConfirmDialog(null,"您输入的账号不合法");
                     accfil.setText("");
                     return;
                 }
             }
             for(int i=0;i<qqnum.length();i++){
                 if(qqnum.charAt(i)-'0'<0||qqnum.charAt(i)-'0'>9){
                     JOptionPane.showConfirmDialog(null,"您输入的qq账号不合法");
                     qqfil.setText("");
                     return;
                 }
             }
            if(!isSend[0]){
                //经历完筛选之后，将信息发送给服务器
                byte stage = ConnectWithServer.sendAccountAndQQnumMsg(account,qqnum);
                switch (stage){
                    case 20:{
                        JOptionPane.showConfirmDialog(null,"您输入的账号不存在");
                        ConnectWithServer.sendBool(false);
                        ConnectWithServer.sendExitMsg();
                        ConnectWithServer.setSocketToNull();
                        return;
                    }
                    case 21:{
                        JOptionPane.showConfirmDialog(null,"请输入正确的关联qq号");
                        ConnectWithServer.sendBool(false);
                        ConnectWithServer.sendExitMsg();
                        ConnectWithServer.setSocketToNull();
                        return;
                    }
                    case 22:{
                        showlab.setText("正在发送验证码.....");
                        vetcode[0] = SendMsg.sendMessages(qqnum);
                        showlab.setText("验证码发送成功！");
                        showThr[0] = new Thread(()->{
                            isSend[0] = true;
                            int i=0;
                            while (i<120){
                                showlab.setText((120-i)+"秒后重试");
                                try {
                                    TimeUnit.SECONDS.sleep(1);
                                } catch (InterruptedException interruptedException) {
                                    interruptedException.printStackTrace();
                                    break;
                                }
                                i++;
                            }
                            if(i==119){
                                ConnectWithServer.sendBool(false);
                                ConnectWithServer.sendExitMsg();
                                ConnectWithServer.setSocketToNull();
                                isSend[0] = false;
                                vetcode[0] = null;
                            }

                        });
                        showThr[0].start();
                        return;
                    }
                    case 23:{
                        JOptionPane.showConfirmDialog(null,"连接超时");
                        return;
                    }
                }
            }else {
                JOptionPane.showConfirmDialog(null,"验证码已发送，请稍后重试！");
            }

        });
        //添加监听器
        btn2.addActionListener(e->{
            //首先判断验证码是否正确，然后在获取密码发送密码
            String code = vertifyfil.getText();
            if(code.length()!=4){
                JOptionPane.showConfirmDialog(null,"验证码格式错误");
                return;
            }
            if(vetcode[0]==null){
                JOptionPane.showConfirmDialog(null,"请发送验证码或验证码已失效");
                return;
            }
            if(vetcode[0].equals(code)){
                //验证码正确，修改后的密码，并提示修改密码成功！
                ConnectWithServer.sendBool(true);
                ConnectWithServer.sendStr(new String(passfil.getPassword()));
                ConnectWithServer.sendExitMsg();
                ConnectWithServer.setSocketToNull();
                showlab.setText("修改成功，界面将于两秒后退出");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                frame.dispose();
            }else {
                JOptionPane.showConfirmDialog(null,"验证码错误");
                return;
            }
        });

        frame.setLayeredPane(jlp);
        frame.setVisible(true);
    }


    public static void main(String[] args){
        FrameTools.findOutPassFrame();
    }

}
