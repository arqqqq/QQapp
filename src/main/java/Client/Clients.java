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
    private final DefaultMutableTreeNode friend = new DefaultMutableTreeNode("我的好友");
    private final DefaultMutableTreeNode stranger = new DefaultMutableTreeNode("陌生人");
    private final DefaultMutableTreeNode blacklist = new DefaultMutableTreeNode("黑名单");
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
        // 点击一次展开
        contacts_tree.setToggleClickCount(1);
        contacts_tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Object node = contacts_tree.getLastSelectedPathComponent();
                String str = node.toString();
                if (!str.equals("我的好友") && !str.equals("黑名单") && !str.equals("陌生人") && e.getClickCount() == 2) {
                    //点击两次好友，弹出对话框
                    PeopleNode treeNode = (PeopleNode) node;
                    new Chat().Open(treeNode.getAccount());
                    System.out.println(treeNode.getAccount());

                }
            }
        });
        root.add(friend);
        for (int i = 0; i < haoyou.size(); i++) {
            PeopleNode node = new PeopleNode(haoyou.get(i), "user" + i, "Hello World", new ImageIcon("图标.jpg"));
            friend.add(node);
        }
        root.add(stranger);
        root.add(blacklist);
        // 隐藏根节点
        contacts_tree.setRootVisible(false);
        // 展开树
        expandTree(contacts_tree);
        // 设置透明
        contacts_tree.setOpaque(false);
        // 隐藏根柄
        contacts_tree.setShowsRootHandles(false);
        contacts_tree.setCellRenderer(new DefaultTreeCellRenderer() {

            // 收起和展开图片设置为三角形
            final ImageIcon icon1 = new ImageIcon("收起.png");
            final ImageIcon icon2 = new ImageIcon("展开.png");

            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                                                          boolean leaf, int row, boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

                if (!expanded) {
                    setIcon(icon1);
                } else {
                    setIcon(icon2);
                }

                String str = value.toString();
                if (!str.equals("我的好友") && !str.equals("黑名单") && !str.equals("陌生人")) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                    PeopleNode people = (PeopleNode) node;
                    setIcon(people.getImageIcon());
                    setFont(new Font("宋体", Font.BOLD, 15));
                } else {
                    setFont(new Font("宋体", Font.BOLD, 20));
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

    //群组的树，暂时未编辑
    public JTree SetGroupTree() {
        return new JTree();
    }

    public void AddJSPane(JTabbedPane table, JScrollPane jsp, String str) {
        jsp.getViewport().setOpaque(false);
        jsp.setOpaque(false);
        jsp.setName(str);
        // 显示滚动条
        jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        table.add(jsp);
    }

    public void UI() {
        JFrame jf = new JFrame();
        jf.setTitle("QQ界面");
        jf.setSize(350, 750);
        jf.setLocation(1000, 100);
        // 窗口大小不可改变
        jf.setResizable(false);
        // 自定义布局
        jf.setLayout(null);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 背景图片
        JLabel back = new JLabel();
        Image background = new ImageIcon("头像.jpg").getImage()
                .getScaledInstance(350, 750, JFrame.DO_NOTHING_ON_CLOSE);
        back.setIcon(new ImageIcon(background));
        // 设置布局位置
        back.setBounds(0, 0, 350, 750);
        // JFrame有三层面板，将背景图片放在最底层
        jf.getLayeredPane().add(back, Integer.valueOf(Integer.MIN_VALUE));

        JPanel panel = (JPanel) jf.getContentPane();
        // 将放置组件的内容面板设置透明
        panel.setOpaque(false);

        // 头像
        JLabel head = new JLabel();
        Image img = new ImageIcon("头像.jpg").getImage().getScaledInstance(100, 100,
                JFrame.DO_NOTHING_ON_CLOSE);
        head.setIcon(new ImageIcon(img));
        head.setBounds(15, 15, 100, 100);
        panel.add(head);

        jf.setIconImage(img);
        // 呢称
        JLabel name = new JLabel("昵称");
        name.setForeground(Color.YELLOW);
        name.setBounds(150, 15, 60, 30);
        panel.add(name);

        // 在线状态
        String[] item = new String[]{"在线", "离线", "隐身", "请勿打扰"};
        var box = new JComboBox(item);
        box.setBounds(150, 45, 80, 20);

        panel.add(box);

        // 个性签名
        JLabel says = new JLabel("个性签名");
        says.setForeground(Color.YELLOW);
        says.setBounds(150, 70, 100, 30);
        panel.add(says);

        // 查找QQ号码
        JTextField findNumber = new JTextField();
        findNumber.setBounds(5, 135, 230, 30);
        panel.add(findNumber);

        // 添加好友功能
        JButton jbu = new JButton("添加好友");

        jbu.setBounds(240, 135, 90, 30);
        jbu.setOpaque(false);
        panel.add(jbu);

        // 设置选项卡透明（需放置在创建之前）
        UIManager.put("TabbedPane.contentOpaque", false);
        // 创建选项卡
        JTabbedPane table = new JTabbedPane();
        // 创建滚动面板
        JScrollPane jsp1 = new JScrollPane(setContactsTree());
        AddJSPane(table, jsp1, "联系人");

        JScrollPane jsp2 = new JScrollPane(SetGroupTree());
        AddJSPane(table, jsp2, "群组");

        table.setBounds(0, 180, 330, 500);

        panel.add(table);

        jf.setVisible(true);

    }
}
