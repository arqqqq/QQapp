package Tools;

import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMsg {

    /**
     * ͨ��stmpЭ���QQ���䷢�������֤�룬��������֤�뷢�͸����������ȴ���֤
     * @param qqnum ���յ�qqNumber
     * @return �������ɵ���֤��
     */
    public static String sendMessages(String qqnum) {
        try {
            //����Properties �����ڼ�¼�����һЩ����
            final Properties props = new Properties();
            //��ʾSMTP�����ʼ���������������֤
            props.put("mail.smtp.auth", "true");
            //�˴���дSMTP������
            props.put("mail.smtp.host", "smtp.qq.com");
            //�˿ںţ�QQ��������������˿ڣ��������587
            props.put("mail.smtp.port", "587");
            //�˴���д����˺�
            props.put("mail.user", "2058084624@qq.com");
            //�˴����������ǰ��˵��16λSTMP����
            //��ȡ������ٶ�
            props.put("mail.password", "mdiyelepwytybibg");
            //������Ȩ��Ϣ�����ڽ���SMTP���������֤
            Authenticator authenticator = new Authenticator() {

                protected PasswordAuthentication getPasswordAuthentication() {
                    // �û���������
                    String userName = props.getProperty("mail.user");
                    String password = props.getProperty("mail.password");
                    return new PasswordAuthentication(userName, password);
                }
            };
            //ʹ�û������Ժ���Ȩ��Ϣ�������ʼ��Ự
            Session mailSession = Session.getInstance(props, authenticator);
            //�����ʼ���Ϣ
            MimeMessage message = new MimeMessage(mailSession);
            //���÷�����
            InternetAddress form = new InternetAddress(props.getProperty("mail.user"));
            message.setFrom(form);

            //�����ռ��˵�����
            InternetAddress to = new InternetAddress(qqnum+"@qq.com");
            message.setRecipient(RecipientType.TO, to);

            //�����ʼ�����
            message.setSubject("�˺�ע����֤��Ϣ");

            //�������һ������Ϊ4�Ķ�̬��֤��
            Random ran = new Random();
            String authcode = ""+ran.nextInt(9)+ran.nextInt(9)+ran.nextInt(9)+ran.nextInt(9);



            String msg = "��qq������:\n��֤��:  "+authcode+"\nΪ��֤�����˺Ű�ȫ\n�벻Ҫ����֤�뷢�͸�����";
            //html�ļ�
            StringBuilder sb = new StringBuilder();
            sb.append("<h1>"+msg+"</h1>");
            //�����ʼ���������
            message.setContent(sb.toString(), "text/html;charset=UTF-8");

            //���Ȼ���Ƿ����ʼ�
            Transport.send(message);

            return authcode;
        } catch (AddressException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args){

    }
}

