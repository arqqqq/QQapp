package Client;



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Login {

    private boolean Can_Drag = false;


    /**
     * ������
     * ��ʼ��connectSever����
     * ����Sever
     */
    public Login(){

    }

    public static void main(String[] args) {
        Login login = new Login();
        login.UI();
    }

    public void UI() {
        JFrame frame = new JFrame();
        frame.setSize(450, 330);
        //���ò��ɸı��С
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        //��������������ʾ��ͼ��
        frame.setIconImage(new ImageIcon("ͼ��.jpg").getImage());
        //ȥ�߿�
        frame.setUndecorated(true);
        frame.setLayout(null);

        Image image = new ImageIcon("��¼����.jpg").getImage().getScaledInstance(450, 330, JFrame.DO_NOTHING_ON_CLOSE);
        JLabel background = new JLabel(new ImageIcon(image));
        background.setBounds(0, 0, 450, 330);
        //��������ǩ�������м��
        frame.getLayeredPane().add(background, Integer.valueOf(Integer.MIN_VALUE));
        JPanel panel = (JPanel) frame.getContentPane();
        //�����ϲ�����͸��
        panel.setOpaque(false);

        // ���ô����϶�Ч��
        background.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Can_Drag = false;
            }

        });

        background.addMouseMotionListener(new MouseMotionListener() {
            int StartX, StartY, EndX, EndY;

            @Override
            public void mouseDragged(MouseEvent e) {
                if (!Can_Drag) {
                    StartX = e.getX();
                    StartY = e.getY();
                    Can_Drag = true;
                }
                EndX = e.getX();
                EndY = e.getY();
                frame.setLocation(frame.getX() + EndX - StartX, frame.getY() + EndY - StartY);
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });

        JButton minimize = new JButton(new ImageIcon("��С��.png"));
        //����ť����͸��
        minimize.setContentAreaFilled(false);
        //����ť����Ϊ�ޱ߿�
        minimize.setBorderPainted(false);
        minimize.setBounds(390, 0, 30, 30);
        minimize.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //��С������
                frame.setExtendedState(1);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                //ʹ��ť���ֻ�ɫ
                minimize.setContentAreaFilled(true);
                minimize.setBackground(Color.GRAY);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                minimize.setContentAreaFilled(false);
            }
        });
        frame.add(minimize);

        JButton close = new JButton(new ImageIcon("�ر�.png"));
        //����ť����͸��
        close.setContentAreaFilled(false);
        //����ť����Ϊ�ޱ߿�
        close.setBorderPainted(false);
        close.setBounds(420, 0, 30, 30);
        close.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ConnectWithServer.sendExitMsg();
                System.exit(0);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                //ʹ��ť���ֺ�ɫ
                close.setContentAreaFilled(true);
                close.setBackground(Color.RED);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                close.setContentAreaFilled(false);
            }
        });
        frame.add(close);

        Image QQ_Head = new ImageIcon("ͷ��.jpg").getImage().getScaledInstance(100, 100, JFrame.DO_NOTHING_ON_CLOSE);
        JLabel head = new JLabel(new ImageIcon(QQ_Head));
        head.setBounds(20, 120, 100, 100);
        frame.add(head);

        JTextField account = new JTextField();
        account.setBounds(135, 120, 200, 30);
        frame.add(account);

        JPasswordField password = new JPasswordField();
        password.setBounds(135, 160, 200, 30);
        frame.add(password);

        JCheckBox auto_login = new JCheckBox("�Զ���¼");
        auto_login.setBounds(135, 200, 80, 20);
        auto_login.setOpaque(false);
        frame.add(auto_login);

        JCheckBox remember = new JCheckBox("��ס����");
        remember.setBounds(240, 200, 80, 20);
        remember.setOpaque(false);
        frame.add(remember);

        Font font = new Font("����", Font.BOLD | Font.ITALIC, 16);

        JButton sign_up = new JButton("ע���˺�");
        sign_up.setBounds(330, 120, 120, 30);
        sign_up.setForeground(Color.yellow);
        sign_up.setFont(font);
        sign_up.setContentAreaFilled(false);
        sign_up.setBorderPainted(false);
        sign_up.addActionListener(e -> {});
        frame.add(sign_up);
        sign_up.addActionListener(e -> {
            FrameTools.registFrame();
        });

        JButton forget = new JButton("�һ�����");
        forget.setBounds(330, 160, 120, 30);
        forget.setForeground(Color.yellow);
        forget.setFont(font);
        forget.setContentAreaFilled(false);
        forget.setBorderPainted(false);
        forget.addActionListener(e -> {
            FrameTools.findOutPassFrame();
        });
        frame.add(forget);

        JButton sign_in = new JButton("��        ¼");
        sign_in.setFont(new Font("����", Font.BOLD, 15));
        sign_in.setBounds(135, 250, 180, 40);
        sign_in.addActionListener(e -> {
            //�ж��Ƿ�Ϸ�
            String acc = account.getText();
            String pass = new String(password.getPassword());
            if(acc.length()!=5){
                JOptionPane.showConfirmDialog(null,"��������˺Ŵ����˺���Ϊ��λ����");
                account.setText("");
                password.setText("");
                return;
            }
            for(int i=0;i<acc.length();i++){
                if(acc.charAt(i)-'0'<0||acc.charAt(i)-'0'>9){
                    JOptionPane.showConfirmDialog(null,"��������˺Ű����Ƿ��ַ�!");
                    account.setText("");
                    password.setText("");
                    return;
                }
            }
            //�����˺���֤��
            final byte[] byts = {14};
            final Object obj = new Object();
            byts[0] = ConnectWithServer.login(acc,pass);
            System.out.println("���չ�����ֵΪ"+byts[0]);
            switch (byts[0]){
                case 10: {
                    JOptionPane.showConfirmDialog(null,"�˺Ų�����");
                    sign_in.setText("��        ¼");
                    return;
                }
                case 11: {
                    JOptionPane.showConfirmDialog(null,"�������");
                    sign_in.setText("��        ¼");
                    return;
                }
                case 12: {
                    JOptionPane.showConfirmDialog(null,"�˺�������ȷ��");
                    sign_in.setText("��        ¼");
                    return;
                }
                case 13: {
                    JOptionPane.showConfirmDialog(null,"��½�ɹ���");
                    //�ȴ����ն���ĺ�������
                    String haoyoumsg = ConnectWithServer.waitForHaoYouMsg();
                    ArrayList<String> haoyou = new ArrayList<>();
                    String temps = "";
                    for(int i=0;i<haoyoumsg.length();i++){
                        char temp = haoyoumsg.charAt(i);
                        if(temp==','||i==haoyoumsg.length()-1){
                            haoyou.add(temps);
                            temps = "";
                        }else {
                            temps = temps+temp;
                        }
                    }
                    System.out.println("������Ϣ:"+haoyou.toString());
                    //��������msg
                    System.out.println("������Ϣ��"+haoyoumsg);
                    //�������������Ϣ��̬�Ĵ���һ��һ����̬�ĺ��Ѵ���
                    new Clients(haoyou).UI();
                    frame.dispose();
                    sign_in.setText("��        ¼");
                    return;//��½�ɹ�!
                }
                case 14: {
                    JOptionPane.showConfirmDialog(null,"���ӳ�ʱ");
                    sign_in.setText("��        ¼");
                    return;
                }
            }

        });
        frame.add(sign_in);

        frame.setVisible(true);
    }
}
