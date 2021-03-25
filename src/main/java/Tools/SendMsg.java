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
     * 通过stmp协议给QQ邮箱发送随机验证码，并将改验证码发送给服务器，等待验证
     * @param qqnum 接收的qqNumber
     * @return 返回生成的验证码
     */
    public static String sendMessages(String qqnum) {
        try {
            //创建Properties 类用于记录邮箱的一些属性
            final Properties props = new Properties();
            //表示SMTP发送邮件，必须进行身份验证
            props.put("mail.smtp.auth", "true");
            //此处填写SMTP服务器
            props.put("mail.smtp.host", "smtp.qq.com");
            //端口号，QQ邮箱给出了两个端口，这里给出587
            props.put("mail.smtp.port", "587");
            //此处填写你的账号
            props.put("mail.user", "2058084624@qq.com");
            //此处的密码就是前面说的16位STMP口令
            //获取口令请百度
            props.put("mail.password", "mdiyelepwytybibg");
            //构建授权信息，用于进行SMTP进行身份验证
            Authenticator authenticator = new Authenticator() {

                protected PasswordAuthentication getPasswordAuthentication() {
                    // 用户名、密码
                    String userName = props.getProperty("mail.user");
                    String password = props.getProperty("mail.password");
                    return new PasswordAuthentication(userName, password);
                }
            };
            //使用环境属性和授权信息，创建邮件会话
            Session mailSession = Session.getInstance(props, authenticator);
            //创建邮件消息
            MimeMessage message = new MimeMessage(mailSession);
            //设置发件人
            InternetAddress form = new InternetAddress(props.getProperty("mail.user"));
            message.setFrom(form);

            //设置收件人的邮箱
            InternetAddress to = new InternetAddress(qqnum+"@qq.com");
            message.setRecipient(RecipientType.TO, to);

            //设置邮件标题
            message.setSubject("账号注册验证信息");

            //随机生成一个长度为4的动态验证码
            Random ran = new Random();
            String authcode = ""+ran.nextInt(9)+ran.nextInt(9)+ran.nextInt(9)+ran.nextInt(9);



            String msg = "仿qq聊天室:\n验证码:  "+authcode+"\n为保证您的账号安全\n请不要将验证码发送给他人";
            //html文件
            StringBuilder sb = new StringBuilder();
            sb.append("<h1>"+msg+"</h1>");
            //设置邮件的内容体
            message.setContent(sb.toString(), "text/html;charset=UTF-8");

            //最后当然就是发送邮件
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

