package Client;

import javax.swing.*;

public interface Operation {
    /*
    �����¼�������������н���,�����¼�ɹ�����ture
     */
    public boolean Clink_Login_Operation(String account,String passwords);

    /*
    ������ѵ��������
     */
    public void Clink_Friend_Operation(String friend_name);

    /*
    �����������
     */
    public  void Clink_Send_Operation();
}
