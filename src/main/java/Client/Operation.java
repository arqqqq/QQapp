package Client;

import javax.swing.*;

public interface Operation {
    /*
    �����¼�������������н���,�����¼�ɹ�����ture
     */
    boolean Clink_Login_Operation(String account,String passwords);

    /**
     * �������tag��������
     * @param friend_name  //���ѵ��ض���ʶ
     * @param sender   //���ͷ����˺�
     */
    void Clink_Friend_Operation(String friend_name,String sender);

    /**
     * ������Ϣ�Ĳ���
     */
    void Clink_Send_Operation();
}
