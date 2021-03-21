package Client;

import javax.swing.*;

public interface Operation {
    /*
    点击登录后的与服务器进行交互,如果登录成功返回ture
     */
    public boolean Clink_Login_Operation(String account,String passwords);

    /*
    点击好友弹出聊天框
     */
    public void Clink_Friend_Operation(String friend_name);

    /*
    点击发送文字
     */
    public  void Clink_Send_Operation();
}
