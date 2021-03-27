package Client;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public class PeopleNode extends DefaultMutableTreeNode {

    private String account;
    private String name;
    private String says;
    private ImageIcon head;
    private String Kind;

    public String getAccount() {
        return account;
    }

    public String getName(){
        return name;
    }

    public ImageIcon getImageIcon(){
        return head;
    }

    public PeopleNode(String account, String name, String says, ImageIcon head) {
        super(name+"   \n"+says);
        this.account = account;
        this.name = name;
        this.says = says;
        Image im=head.getImage().getScaledInstance(50,50,1);
        this.head = new ImageIcon(im);
    }
}
