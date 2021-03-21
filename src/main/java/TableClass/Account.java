package TableClass;

/**
 * 用户类 -- 用户表
 */
public class Account {

    /**
     * 用户类
     * 属性
     * 用户账号 account
     * 用户登陆密码 password
     * 用户的用户名 IDname
     * 用户的个性签名 priname
     * 用户的关联qq号 qqnum
     */
    private String account;
    private String password;
    private String IDname;
    private String priname;
    private String qqnum;

    /**
     * 构造器
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
     * 返回我的账户名
     * @return
     */
    public String getAccount(){
        return account;
    }

    /**
     * 返回我的密码
     */
    public String getPassword(){
        return password;
    }

    /**
     * 返回我的用户名
     * @return
     */
    public String getIDname(){
        return IDname;
    }

    /**
     * 返回我的个性签名
     * @return
     */
    public String getPriname(){
        return priname;
    }

    /**
     * 返回我的关联qq号
     * @return
     */
    public String getQqnum(){
        return qqnum;
    }

}
