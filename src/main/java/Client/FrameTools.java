package Client;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class FrameTools {


    public static void registFrame(ConnectServer sever){
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
        final String[] vertyCode = {""};
        final Thread[] showThr = {null};
        //添加监听器
        btn1.addActionListener(e -> {
            if(!isSend[0]){
                /**
                 * 当点击发送按钮时
                 * 首先获取qq输入栏的qq账号
                 * 检测qq账号是否合法，合法的话，将qq号发送给服务器，服务器传回验证码
                 */
                String qqnum = fil1.getText();
                if(qqnum.length()==0||qqnum==null){
                    JOptionPane.showConfirmDialog(null,"请输入qq号！");
                    return;
                }
                if(qqnum.length()>12||qqnum.length()<8){
                    JOptionPane.showConfirmDialog(null,"请输入正确的qq号");
                    return;
                }
                for(int i=0;i<qqnum.length();i++){
                    if(qqnum.charAt(i)-'0'<0||qqnum.charAt(i)-'0'>9){
                        JOptionPane.showConfirmDialog(null,"您输入的qq号含有非法字符，请重新输入");
                        return;
                    }
                }
                //如果执行了上面的代码还没有返回，说明qq是格式是存在的
                sever.sendRegistMsg(qqnum);
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                vertyCode[0] = sever.verityCode();
                //并切启动一个线程，对验证码的有效性进行操作
                isSend[0] = true;
                showThr[0] = new Thread(){
                    int i=0;
                    @Override
                    public void run(){
                        while (i<120){
                            labels.setText(120-i+"秒后可重新发送");
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                                break;
                            }
                            i++;
                        }
                        isSend[0] = false;
                        vertyCode[0] = "";
                    }
                };
                showThr[0].start();
            }else {
                JOptionPane.showConfirmDialog(null,"验证码已发送，请稍后重试！");
                return;
            }

        });
        btn2.addActionListener(e -> {
            //说明已经获得了验证码
            if(vertyCode[0]!=null&&!vertyCode[0].equals("")){
                String vetcode = fil2.getText();
                if(vetcode.equals(vertyCode[0])){
                    JOptionPane.showConfirmDialog(null,"验证码正确，请输入新账户和密码！");
                    fil3.setEnabled(true);
                    fil4.setEnabled(true);
                    showThr[0].interrupt();
                    labels.setText("验证成功！");
                    vertyCode[0] = "";
                }else {
                    JOptionPane.showConfirmDialog(null,"验证码错误！");
                }
            }else {
                JOptionPane.showConfirmDialog(null,"请发送验证码");
            }

        });
        btn3.addActionListener(e -> {
           String account = fil3.getText();
           String password = new String(fil4.getPassword());
           if(account.length()!=5){
               JOptionPane.showConfirmDialog(null,"账号必须为五位的数字");
               return;
           }
           for(int i=0;i<account.length();i++){
               if(account.charAt(i)-'0'<0||account.charAt(i)-'0'>9){
                   JOptionPane.showConfirmDialog(null,"您输入的账户不合法！必须为五位的数字");
                   return;
               }
           }
           sever.sendAccountMsg(account,password);
           JOptionPane.showConfirmDialog(null,"注册成功！");
           frames.dispose();
           return;
        });

        frames.setLayeredPane(frame);
        frames.setVisible(true);
    }



}
