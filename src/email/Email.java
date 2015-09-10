package email;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by jesse on 9/9/15.
 */
public class Email {

    public boolean sendEmail() {

        Properties props = new Properties();
        props.setProperty("mail.smtp.host", "mail.usu.edu");
        props.setProperty("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance(props, null);

        try {
            Transport transport = session.getTransport("smtp");
            transport.connect("A#", "password");
            Message message = new MimeMessage(session);
            message.setSubject("Test");
            message.setText("Hello :)");
            message.setFrom(new InternetAddress("email@usu.edu"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress("email@gmail.com"));
            transport.sendMessage(message, message.getAllRecipients());
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

        return true;
    }
}
