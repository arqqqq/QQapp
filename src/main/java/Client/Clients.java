package Client;


import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class Clients {

    private final DefaultMutableTreeNode root = new DefaultMutableTreeNode();
    private final DefaultMutableTreeNode friend = new DefaultMutableTreeNode("�ҵĺ���");
    private final DefaultMutableTreeNode stranger = new DefaultMutableTreeNode("İ����");
    private final DefaultMutableTreeNode blacklist = new DefaultMutableTreeNode("������");
    JTree contacts_tree = new JTree(root);

    private ArrayList<String> haoyou;

    public Clients(ArrayList<String> haoyou) {
        this.haoyou = haoyou;
    }

    public void expandTree(JTree jtree) {
        TreeNode root = (TreeNode) jtree.getModel().getRoot();
        jtree.expandPath(new TreePath(root));
    }

    public JTree setContactsTree() {
        // ���һ��չ��
        contacts_tree.setToggleClickCount(1);
        contacts_tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Object node = contacts_tree.getLastSelectedPathComponent();
                String str = node.toString();
                if (!str.equals("�ҵĺ���") && !str.equals("������") && !str.equals("İ����") && e.getClickCount() == 2) {
                    //������κ��ѣ������Ի���
                    PeopleNode treeNode = (PeopleNode) node;
                    new Chat().Open(treeNode.getAccount());
                    System.out.println(treeNode.getAccount());

                }
            }
        });
        root.add(friend);
        for (int i = 0; i < haoyou.size(); i++) {
            PeopleNode node = new PeopleNode(haoyou.get(i), "user" + i, "Hello World", new ImageIcon("ͼ��.jpg"));
            friend.add(node);
        }
        root.add(stranger);
        root.add(blacklist);
        // ���ظ��ڵ�
        contacts_tree.setRootVisible(false);
        // չ����
        expandTree(contacts_tree);
        // ����͸��
        contacts_tree.setOpaque(false);
        // ���ظ���
        contacts_tree.setShowsRootHandles(false);
        contacts_tree.setCellRenderer(new DefaultTreeCellRenderer() {

            // �����չ��ͼƬ����Ϊ������
            final ImageIcon icon1 = new ImageIcon("����.png");
            final ImageIcon icon2 = new ImageIcon("չ��.png");

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
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                    PeopleNode people = (PeopleNode) node;
                    setIcon(people.getImageIcon());
                    setFont(new Font("����", Font.BOLD, 15));
                } else {
                    setFont(new Font("����", Font.BOLD, 20));
                }

                setBackgroundNonSelectionColor(new Color(255, 255, 255, 175));
                setBackgroundSelectionColor(new Color(255, 255, 255, 175));
                setTextSelectionColor(Color.RED);
                setTextNonSelectionColor(Color.BLACK);

                return this;
            }

        });

        return contacts_tree;
    }

    //Ⱥ���������ʱδ�༭
    public JTree SetGroupTree() {
        return new JTree();
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
        JFrame jf = new JFrame();
        jf.setTitle("QQ����");
        jf.setSize(350, 750);
        jf.setLocation(1000, 100);
        // ���ڴ�С���ɸı�
        jf.setResizable(false);
        // �Զ��岼��
        jf.setLayout(null);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ����ͼƬ
        JLabel back = new JLabel();
        Image background = new ImageIcon("ͷ��.jpg").getImage()
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
        String[] item = new String[]{"����", "����", "����", "�������"};
        var box = new JComboBox(item);
        box.setBounds(150, 45, 80, 20);

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

        JScrollPane jsp2 = new JScrollPane(SetGroupTree());
        AddJSPane(table, jsp2, "Ⱥ��");

        table.setBounds(0, 180, 330, 500);

        panel.add(table);

        jf.setVisible(true);

    }
}
