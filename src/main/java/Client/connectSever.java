package Client;

import java.io.*;
import java.net.Socket;

public class connectSever {

    private DataInputStream dint;
    private DataOutputStream dout;

    public void connSever(){
        try {
            Socket soc = new Socket("loaclhost",8800);
            InputStream in = soc.getInputStream();
            OutputStream out = soc.getOutputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
