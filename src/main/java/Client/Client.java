package Client;




import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class Client {

    DefaultMutableTreeNode root = new DefaultMutableTreeNode();
    DefaultMutableTreeNode friend = new DefaultMutableTreeNode("�ҵĺ���");
    DefaultMutableTreeNode stranger = new DefaultMutableTreeNode("İ����");
    DefaultMutableTreeNode blackname = new DefaultMutableTreeNode("������");
    DefaultMutableTreeNode bl = new DefaultMutableTreeNode("����");
    DefaultMutableTreeNode bl1 = new DefaultMutableTreeNode("����");
    JTree jtree = new JTree(root);

    ConnectWithServer connect;
//    public static void main(String[] args) {
//        Display.Client QQ = new Display.Client();
//        QQ.UI();
//    }
    public Client(ConnectWithServer connect){
        this.connect=connect;
    }

    public void expandTree(JTree jtree) {
        TreeNode root = (TreeNode) jtree.getModel().getRoot();
        jtree.expandPath(new TreePath(root));
    }

    public JTree setContactsTree() {
        // ���һ��չ��
        jtree.setToggleClickCount(1);
        jtree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    //������κ��ѣ������Ի���
                    DefaultMutableTreeNode treeNode=(DefaultMutableTreeNode)jtree.getLastSelectedPathComponent();
                    if(treeNode.toString().equals("����")){
                        connect.Clink_Friend_Operation("����");
                        new Chat(connect).Open();
                    }
                }

            }

        });
        root.add(friend);
        friend.add(bl);
        root.add(stranger);
        root.add(blackname);
        // ���ظ��ڵ�
        jtree.setRootVisible(false);
        // չ����
        expandTree(jtree);
        // ����͸��
        jtree.setOpaque(false);
        // ���ظ���
        jtree.setShowsRootHandles(false);
        jtree.setCellRenderer(new DefaultTreeCellRenderer() {

            // �����չ��ͼƬ����Ϊ������
            ImageIcon icon1 = new ImageIcon("����.png");
            ImageIcon icon2 = new ImageIcon("չ��.png");

            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                                                          boolean leaf, int row, boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                if (!expanded) {
                    setIcon(icon1);
                } else {
                    setIcon(icon2);
                }
                String str = value.toString();
                if (!str.equals("�ҵĺ���") && !str.equals("������") && !str.equals("İ����")) {
                    setIcon(new ImageIcon("����.png"));
                }
                setBackgroundNonSelectionColor(new Color(255, 255, 255, 150));
                setTextSelectionColor(Color.RED);
                setTextNonSelectionColor(Color.BLACK);
                setFont(new Font("����", 1, 20));
                return this;
            }

        });
        return jtree;
    }

    public void AddJSPane(JTabbedPane table, JScrollPane jsp, String str) {
        jsp.getViewport().setOpaque(false);
        jsp.setOpaque(false);
        jsp.setName(str);
        // ��ʾ������
        jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        table.add(jsp);
    }

    public void UI() {

//		JFrame.setDefaultLookAndFeelDecorated(true);
//		try {
////			UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
//			String lookAndFeel ="com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
//			UIManager.setLookAndFeel(lookAndFeel);
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (UnsupportedLookAndFeelException e) {
//			e.printStackTrace();
//		}
        JFrame jf = new JFrame();
        jf.setTitle("QQ����");
        jf.setSize(350, 750);
        jf.setLocation(1500, 100);
        // ���ڴ�С���ɸı�
        jf.setResizable(false);
        // �Զ��岼��
        jf.setLayout(null);
        jf.setDefaultCloseOperation(3);

        // ����ͼƬ
        JLabel back = new JLabel();
        Image background = new ImageIcon("background.jpg").getImage()
                .getScaledInstance(350, 750, JFrame.DO_NOTHING_ON_CLOSE);
        back.setIcon(new ImageIcon(background));
        // ���ò���λ��
        back.setBounds(0, 0, 350, 750);
        // JFrame��������壬������ͼƬ������ײ�
        jf.getLayeredPane().add(back, Integer.valueOf(Integer.MIN_VALUE));

        JPanel panel = (JPanel) jf.getContentPane();
        // ����������������������͸��
        panel.setOpaque(false);

        // ͷ��
        JLabel head = new JLabel();
        Image img = new ImageIcon("ͷ��.jpg").getImage().getScaledInstance(100, 100,
                JFrame.DO_NOTHING_ON_CLOSE);
        head.setIcon(new ImageIcon(img));
        head.setBounds(15, 15, 100, 100);
        panel.add(head);
        jf.setIconImage(img);
        // �س�
        JLabel name = new JLabel("�ǳ�");
        name.setForeground(Color.YELLOW);
        name.setBounds(150, 15, 60, 30);
        panel.add(name);

        // ����״̬
        JComboBox box = new JComboBox();
        box.addItem("����");
        box.addItem("����");
        box.addItem("����");
        box.addItem("�������");
        box.setBounds(150, 45, 80, 20);
        box.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                System.out.println(e.getItem());
            }
        });
        panel.add(box);

        // ����ǩ��
        JLabel says = new JLabel("����ǩ��");
        says.setForeground(Color.YELLOW);
        says.setBounds(150, 70, 100, 30);
        panel.add(says);

        // ����QQ����
        JTextField findNumber = new JTextField();
        findNumber.setBounds(5, 135, 230, 30);
        panel.add(findNumber);

        // ��Ӻ��ѹ���
        JButton jbu = new JButton("��Ӻ���");
        jbu.addActionListener(e -> {
//				friend.add(bl1);
//				jtree.updateUI();
        });
        jbu.setBounds(240, 135, 90, 30);
        jbu.setOpaque(false);
        panel.add(jbu);

        // ����ѡ�͸����������ڴ���֮ǰ��
        UIManager.put("TabbedPane.contentOpaque", false);
        // ����ѡ�
        JTabbedPane table = new JTabbedPane();
        // �����������
        JScrollPane jsp1 = new JScrollPane(setContactsTree());
        AddJSPane(table, jsp1, "��ϵ��");

        JScrollPane jsp2 = new JScrollPane();
        AddJSPane(table, jsp2, "Ⱥ��");

        table.setBounds(0, 180, 330, 500);

        panel.add(table);

        jf.setVisible(true);

    }
}
