package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Chat {

    boolean first = true;

    public void Open() {
        JFrame chat_frame = new JFrame("����");
        chat_frame.setSize(800, 700);
        chat_frame.setLocationRelativeTo(null);
        chat_frame.setLayout(null);
        chat_frame.setResizable(false);
//        chat.setUndecorated(true);
        chat_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //���������¼
        JPanel context = new JPanel();
//        context.setBackground(Color.BLACK);
        context.setBounds(0, 0, 650, 480);
        context.setLayout(null);
        chat_frame.add(context);

        JTextArea context_jta = new JTextArea();
        //�����¼���ɱ༭
        context_jta.setEditable(false);
        context_jta.setOpaque(false);

        JScrollPane context_jsp = new JScrollPane(context_jta);
        context_jsp.setOpaque(false);
        context_jsp.getViewport().setOpaque(false);
        context_jsp.setBounds(0, 0, 650, 480);
        context.add(context_jsp);

        //������
        JPanel tool = new JPanel();
        tool.setBounds(0, 480, 650, 40);

        JButton emotion = new JButton("����");
        tool.add(emotion);
        chat_frame.add(tool);

        Image people = new ImageIcon("man.png").getImage().getScaledInstance(150, 700, JFrame.DO_NOTHING_ON_CLOSE);
        JLabel QQ_show = new JLabel(new ImageIcon(people));
        QQ_show.setBounds(650, 0, 150, 700);
        chat_frame.add(QQ_show);

        //��Ҫ���͵���Ϣ
        JPanel send_context = new JPanel();
        send_context.setLayout(null);
        send_context.setBounds(0, 520, 650, 180);

        //��ťҪ������JTexAreaǰ������ûЧ��(ԭ����ʱδ֪)
        JButton close = new JButton("��  ��");
        close.setBounds(490, 112, 80, 30);
        close.addActionListener(e -> System.exit(0));
        send_context.add(close);

        JTextArea send_context_jta = new JTextArea();
        send_context_jta.setOpaque(false);
        //�س���������Ϣ
        send_context_jta.addKeyListener(new KeyListener() {
            String str;

            boolean press_ctrl = false;

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (!send_context_jta.getText().equals("") && !send_context_jta.getText().equals("\n")) {
                        if (first) {
                            first = false;
                            str = send_context_jta.getText();
                        } else {
                            str = "\n" + send_context_jta.getText();
                        }
                        context_jta.append(str);
                    }
                    send_context_jta.setText("");
                }
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    press_ctrl = true;
                    System.out.println("press");
                }
                if (press_ctrl && e.getKeyCode() == KeyEvent.VK_V) {
                    send_context_jta.setText("====");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    send_context_jta.setText("");
                }
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    press_ctrl = false;
                    System.out.println("release");
                }
            }
        });

        //������Ϣ��ť
        JButton send = new JButton("��  ��");
        send.setBounds(570, 112, 79, 30);
        send_context.add(send);
        send.addActionListener(new ActionListener() {
            String str;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!send_context_jta.getText().equals("")) {
                    if (send_context_jta.getText().equals("\n")) {
                        System.out.println("====");
                    }
                    if (first) {
                        first = false;
                        str = send_context_jta.getText();
                    } else {
                        str = "\n" + send_context_jta.getText();
                    }
                    send_context_jta.setText("");
                    context_jta.append(str);
                }
                send_context_jta.requestFocus();
            }
        });

        JScrollPane send_context_jsp = new JScrollPane(send_context_jta);
        send_context_jsp.setOpaque(false);
        send_context_jsp.getViewport().setOpaque(false);
        send_context_jsp.setBounds(0, 0, 650, 180);
        send_context.add(send_context_jsp);

        chat_frame.add(send_context);
        chat_frame.setVisible(true);
        send_context_jta.requestFocus();
    }
}
