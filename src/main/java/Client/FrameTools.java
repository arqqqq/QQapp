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
        frames.setTitle("ע�ᴰ��");
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


        JLabel lab1 = new JLabel("qq���룺");
        lab1.setForeground(Color.YELLOW);
        JLabel lab2 = new JLabel("��֤�룺");
        lab2.setForeground(Color.YELLOW);
        JLabel lab3 = new JLabel("�˺ţ�");
        lab3.setForeground(Color.YELLOW);
        JLabel lab4 = new JLabel("���룺");
        lab4.setForeground(Color.YELLOW);
        JTextField fil1 = new JTextField();
        fil1.setBackground(new Color(211,211,211));
        JTextField fil2 = new JTextField();
        fil2.setBackground(new Color(211,211,211));
        JTextField fil3 = new JTextField();
        fil3.setEnabled(false);//���ò��ɱ�չ
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

        //��Ӱ�ť
        JLabel labels = new JLabel("�뷢����֤��");
        labels.setForeground(Color.YELLOW);
        labels.setBounds(50,340,300,50);
        labels.setFont(new Font("΢���ź�",Font.BOLD,16));
        labels.setHorizontalAlignment(JLabel.CENTER);
        frame.add(labels,JLayeredPane.DRAG_LAYER);

        JButton btn1 = new JButton("����");
        JButton btn2 = new JButton("��֤");
        JButton btn3 = new JButton("ע��");
        btn1.setBounds(50,390,60,30);
        btn2.setBounds(170,390,60,30);
        btn3.setBounds(290,390,60,30);
        frame.add(btn1,JLayeredPane.DRAG_LAYER);
        frame.add(btn2,JLayeredPane.DRAG_LAYER);
        frame.add(btn3,JLayeredPane.DRAG_LAYER);
        final boolean[] isClice = {false};
        final String[] code = {""};
        final Thread[] thr = {null};
        //��Ӽ�����
        btn1.addActionListener(e -> {
           if(!isClice[0]){
               //�����жϻ�ȡ����qq�����Ƿ�Ϸ�
               String qqnum = fil1.getText();
               if(qqnum.length()>11||qqnum.length()<7){
                   //��qq����ĳ��ȴ���11λ����С��7λʱ
                   JOptionPane.showConfirmDialog(null,"�������qq���벻����Ҫ��");
                   return;
               }
               for(int i=0;i<qqnum.length();i++){
                   char temp = qqnum.charAt(i);
                   if(temp-'0'<0||temp-'0'>9){
                       JOptionPane.showConfirmDialog(null,"qq��������Ƿ��ַ���");
                       return;
                   }
               }
               //���жϳɹ�qq������������֮���������������˺��Ѿ���ע��
               byte byt = ConnectWithServer.vertifyQQnum(qqnum);
               if(byt==2){
                   //����qq�ŷ�����֤��
                   code[0] = SendMsg.sendMessages(qqnum);
                   System.out.println("��֤��Ϊ:"+code[0]);
                   if(code[0]==null){
                       JOptionPane.showConfirmDialog(null,"�Ҳ�����qq���룡");
                       ConnectWithServer.sendBool(false);
                       ConnectWithServer.sendVertifyMsg((byte) 5);
                       ConnectWithServer.setSocketToNull();
                       return;
                   }else {
                       thr[0] =new Thread(()->{
                           isClice[0] = true;
                           int i=0;
                           for(;i<120;i++){
                               labels.setText((120-i)+"������·���");
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
                   JOptionPane.showConfirmDialog(null,"���ӳ�ʱ��");
                   return;
               }else if(byt==1){
                   JOptionPane.showConfirmDialog(null,"��qq�����ѱ�ע�ᣡ");
                   return;
               }
           }
           else {
               JOptionPane.showConfirmDialog(null,"��֤���Ѿ����ͣ����Ժ�����");
               return;
           }
        });
        btn2.addActionListener(e -> {
            String vetcode = fil2.getText();
            if(vetcode.equals(code[0])){
                JOptionPane.showConfirmDialog(null,"��֤����ȷ��");
                //���˺��������������Ϊ�ɱ༭
                fil3.setEnabled(true);
                fil4.setEnabled(true);
                System.out.println("�༭�ɹ���");
                thr[0].interrupt();
                thr[0] = null;
                ConnectWithServer.sendBool(true);
            }else {
                JOptionPane.showConfirmDialog(null,"��֤�����");
            }
        });
        //Ϊע�ᰴť��Ӽ�����
        btn3.addActionListener(e->{
            if(fil3.isEditable()&&fil4.isEditable()){
                String account = fil3.getText();
                String password = new String(fil4.getPassword());
                if(account.length()!=5){
                    JOptionPane.showConfirmDialog(null,"�˺ű���Ϊ��λ������");
                    return;
                }
                for(int i=0;i<account.length();i++){
                    if(account.charAt(i)-'0'<0||account.charAt(i)-'0'>9){
                        JOptionPane.showConfirmDialog(null,"��������˺Ų��Ϸ���");
                        return;
                    }
                }
                if(password.getBytes().length>30){
                    JOptionPane.showConfirmDialog(null,"����λ�����࣡");
                    return;
                }
                //ͨ��ǰ�����֤�Ļ���������˺ŷ��ͽ׶�
                ConnectWithServer.sendStr(account);
                System.out.println("���ͳɹ���");
                boolean bool = ConnectWithServer.readBool();
                if(!bool){
                    ConnectWithServer.sendVertifyMsg((byte) 15);
                    ConnectWithServer.sendStr(password);
                    //�Ͽ�����
                    ConnectWithServer.sendVertifyMsg((byte) 5);
                    ConnectWithServer.setSocketToNull();//��socket�ÿ�
                    frames.dispose();//�رմ���

                }else {
                    JOptionPane.showConfirmDialog(null,"���˺��Ѿ���ע�ᣡ");
                    return;
                }
            }else {
                JOptionPane.showConfirmDialog(null,"�뷢����֤��");
                return;
            }
        });

        frames.setLayeredPane(frame);
        frames.setVisible(true);
    }


    public static void findOutPassFrame(){
        JFrame frame = new JFrame();
        frame.setTitle("�����һ�");
        frame.setSize(400,500);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        //������
        JLayeredPane jlp = new JLayeredPane();
        ImageIcon icon = new ImageIcon("Photo\\img.png");
        Image img = icon.getImage().getScaledInstance(400,500,1);
        JLabel lab = new JLabel(new ImageIcon(img));
        lab.setBounds(0,0,400,500);
        JPanel pan = new JPanel();
        pan.setBounds(0,0,400,500);
        pan.add(lab);
        jlp.add(pan,JLayeredPane.DEFAULT_LAYER);
        //������
        JLabel lab1 = new JLabel("�˺�:");
        JLabel lab2 = new JLabel("��qq��:");
        JLabel lab3 = new JLabel("��֤��:");
        JLabel lab4 = new JLabel("������:");
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

        //����ʾ��ǩ
        JLabel showlab = new JLabel("��������֤��");
        showlab.setHorizontalAlignment(JLabel.CENTER);
        showlab.setBounds(50,290,300,50);
        jlp.add(showlab,JLayeredPane.DRAG_LAYER);
        //��Ӱ�ť
        JButton btn1 = new JButton("����");
        JButton btn2 = new JButton("�޸�����");
        btn1.setBounds(50,370,100,30);
        btn2.setBounds(250,370,100,30);
        jlp.add(btn1,JLayeredPane.DRAG_LAYER);
        jlp.add(btn2,JLayeredPane.DRAG_LAYER);
        //��Ӽ�����
        final String[] vetcode = {null};
        final Thread[] showThr = {null};
        final boolean[] isSend = {false};
        btn1.addActionListener(e->{
            /**
             * ʵ�ֲ���
             * 1�����ȶԶ�ȡ�˺ſ��qq�˺ſ������
             * 2�����˺ź�qq�ĺϷ��Խ���һ��У��
             * 3������Ϣ���͸��������ˣ���֤�˺ŵĴ����Ժ͹�����
             * 4������Ϣ��������
             * 5���ͻ��˸�����Ϣ�������ݣ���ʹ�ÿͻ����н�����ͨ��gui
              */
             String account = accfil.getText();
             String qqnum = qqfil.getText();
             if(account.length()!=5){
                 JOptionPane.showConfirmDialog(null,"�˺ű���λ��λ������");
                 return;
             }
             if(qqnum.length()<8){
                 JOptionPane.showConfirmDialog(null,"qq���������8λ��11λ֮��");
                 return;
             }
             for(int i=0;i<account.length();i++){
                 if(account.charAt(i)-'0'<0||account.charAt(i)-'0'>9){
                     JOptionPane.showConfirmDialog(null,"��������˺Ų��Ϸ�");
                     accfil.setText("");
                     return;
                 }
             }
             for(int i=0;i<qqnum.length();i++){
                 if(qqnum.charAt(i)-'0'<0||qqnum.charAt(i)-'0'>9){
                     JOptionPane.showConfirmDialog(null,"�������qq�˺Ų��Ϸ�");
                     qqfil.setText("");
                     return;
                 }
             }
            if(!isSend[0]){
                //������ɸѡ֮�󣬽���Ϣ���͸�������
                byte stage = ConnectWithServer.sendAccountAndQQnumMsg(account,qqnum);
                switch (stage){
                    case 20:{
                        JOptionPane.showConfirmDialog(null,"��������˺Ų�����");
                        ConnectWithServer.sendBool(false);
                        ConnectWithServer.sendExitMsg();
                        ConnectWithServer.setSocketToNull();
                        return;
                    }
                    case 21:{
                        JOptionPane.showConfirmDialog(null,"��������ȷ�Ĺ���qq��");
                        ConnectWithServer.sendBool(false);
                        ConnectWithServer.sendExitMsg();
                        ConnectWithServer.setSocketToNull();
                        return;
                    }
                    case 22:{
                        showlab.setText("���ڷ�����֤��.....");
                        vetcode[0] = SendMsg.sendMessages(qqnum);
                        showlab.setText("��֤�뷢�ͳɹ���");
                        showThr[0] = new Thread(()->{
                            isSend[0] = true;
                            int i=0;
                            while (i<120){
                                showlab.setText((120-i)+"�������");
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
                        JOptionPane.showConfirmDialog(null,"���ӳ�ʱ");
                        return;
                    }
                }
            }else {
                JOptionPane.showConfirmDialog(null,"��֤���ѷ��ͣ����Ժ����ԣ�");
            }

        });
        //��Ӽ�����
        btn2.addActionListener(e->{
            //�����ж���֤���Ƿ���ȷ��Ȼ���ڻ�ȡ���뷢������
            String code = vertifyfil.getText();
            if(code.length()!=4){
                JOptionPane.showConfirmDialog(null,"��֤���ʽ����");
                return;
            }
            if(vetcode[0]==null){
                JOptionPane.showConfirmDialog(null,"�뷢����֤�����֤����ʧЧ");
                return;
            }
            if(vetcode[0].equals(code)){
                //��֤����ȷ���޸ĺ�����룬����ʾ�޸�����ɹ���
                ConnectWithServer.sendBool(true);
                ConnectWithServer.sendStr(new String(passfil.getPassword()));
                ConnectWithServer.sendExitMsg();
                ConnectWithServer.setSocketToNull();
                showlab.setText("�޸ĳɹ������潫��������˳�");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                frame.dispose();
            }else {
                JOptionPane.showConfirmDialog(null,"��֤�����");
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
