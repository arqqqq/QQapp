package Client;


public class ConnectWithServer implements Operation{




    @Override
    public boolean Clink_Login_Operation(String account, String passwords) {
        new Client(this).UI();

        return true;
    }

    @Override
    public void Clink_Friend_Operation(String friend_name) {

    }

    @Override
    public void Clink_Send_Operation() {

    }
}
