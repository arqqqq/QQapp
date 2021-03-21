package TableClass;

/**
 * �û��� -- �û���
 */
public class Account {

    /**
     * �û���
     * ����
     * �û��˺� account
     * �û���½���� password
     * �û����û��� IDname
     * �û��ĸ���ǩ�� priname
     * �û��Ĺ���qq�� qqnum
     */
    private String account;
    private String password;
    private String IDname;
    private String priname;
    private String qqnum;

    /**
     * ������
     * @param account
     * @param password
     * @param IDname
     * @param priname
     * @param qqnum
     */
    public Account(String account,String password,String IDname,String priname,String qqnum){
        this.account = account;
        this.password = password;
        this.IDname = IDname;
        this.priname = priname;
        this.qqnum = qqnum;
    }

    public Account theObj(){
        return this;
    }

    /**
     * �����ҵ��˻���
     * @return
     */
    public String getAccount(){
        return account;
    }

    /**
     * �����ҵ�����
     */
    public String getPassword(){
        return password;
    }

    /**
     * �����ҵ��û���
     * @return
     */
    public String getIDname(){
        return IDname;
    }

    /**
     * �����ҵĸ���ǩ��
     * @return
     */
    public String getPriname(){
        return priname;
    }

    /**
     * �����ҵĹ���qq��
     * @return
     */
    public String getQqnum(){
        return qqnum;
    }

}
