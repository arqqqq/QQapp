package Client;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class FrameTools {


    public static void registFrame(ConnectServer sever){
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
        final String[] vertyCode = {""};
        final Thread[] showThr = {null};
        //��Ӽ�����
        btn1.addActionListener(e -> {
            if(!isSend[0]){
                /**
                 * ��������Ͱ�ťʱ
                 * ���Ȼ�ȡqq��������qq�˺�
                 * ���qq�˺��Ƿ�Ϸ����Ϸ��Ļ�����qq�ŷ��͸���������������������֤��
                 */
                String qqnum = fil1.getText();
                if(qqnum.length()==0||qqnum==null){
                    JOptionPane.showConfirmDialog(null,"������qq�ţ�");
                    return;
                }
                if(qqnum.length()>12||qqnum.length()<8){
                    JOptionPane.showConfirmDialog(null,"��������ȷ��qq��");
                    return;
                }
                for(int i=0;i<qqnum.length();i++){
                    if(qqnum.charAt(i)-'0'<0||qqnum.charAt(i)-'0'>9){
                        JOptionPane.showConfirmDialog(null,"�������qq�ź��зǷ��ַ�������������");
                        return;
                    }
                }
                //���ִ��������Ĵ��뻹û�з��أ�˵��qq�Ǹ�ʽ�Ǵ��ڵ�
                sever.sendRegistMsg(qqnum);
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                vertyCode[0] = sever.verityCode();
                //��������һ���̣߳�����֤�����Ч�Խ��в���
                isSend[0] = true;
                showThr[0] = new Thread(){
                    int i=0;
                    @Override
                    public void run(){
                        while (i<120){
                            labels.setText(120-i+"�������·���");
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
                JOptionPane.showConfirmDialog(null,"��֤���ѷ��ͣ����Ժ����ԣ�");
                return;
            }

        });
        btn2.addActionListener(e -> {
            //˵���Ѿ��������֤��
            if(vertyCode[0]!=null&&!vertyCode[0].equals("")){
                String vetcode = fil2.getText();
                if(vetcode.equals(vertyCode[0])){
                    JOptionPane.showConfirmDialog(null,"��֤����ȷ�����������˻������룡");
                    fil3.setEnabled(true);
                    fil4.setEnabled(true);
                    showThr[0].interrupt();
                    labels.setText("��֤�ɹ���");
                    vertyCode[0] = "";
                }else {
                    JOptionPane.showConfirmDialog(null,"��֤�����");
                }
            }else {
                JOptionPane.showConfirmDialog(null,"�뷢����֤��");
            }

        });
        btn3.addActionListener(e -> {
           String account = fil3.getText();
           String password = new String(fil4.getPassword());
           if(account.length()!=5){
               JOptionPane.showConfirmDialog(null,"�˺ű���Ϊ��λ������");
               return;
           }
           for(int i=0;i<account.length();i++){
               if(account.charAt(i)-'0'<0||account.charAt(i)-'0'>9){
                   JOptionPane.showConfirmDialog(null,"��������˻����Ϸ�������Ϊ��λ������");
                   return;
               }
           }
           sever.sendAccountMsg(account,password);
           JOptionPane.showConfirmDialog(null,"ע��ɹ���");
           frames.dispose();
           return;
        });

        frames.setLayeredPane(frame);
        frames.setVisible(true);
    }



}
