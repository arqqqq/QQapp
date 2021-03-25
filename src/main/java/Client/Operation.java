package Client;

import javax.swing.*;

public interface Operation {
    /*
    点击登录后的与服务器进行交互,如果登录成功返回ture
     */
    boolean Clink_Login_Operation(String account,String passwords);

    /**
     * 点击好友tag创建窗口
     * @param friend_name  //好友的特定标识
     * @param sender   //发送方的账号
     */
    void Clink_Friend_Operation(String friend_name,String sender);

    /**
     * 发送信息的操作
     */
    void Clink_Send_Operation();
}
