package email;

import app.Main;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Created by jesse on 9/9/15.
 */
public class Email {

    public static boolean sendEmail(String body, String password) {

        Properties props = new Properties();
        props.setProperty("mail.smtp.host", "mail.usu.edu");
        props.setProperty("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance(props, null);

        Transport transport = null;
        Store store = null;

        try {
            transport = session.getTransport("smtp");
            transport.connect("A" + Main.getInstance().getProfile().aNumber, password);
            Message message = new MimeMessage(session);

            message.setSubject("Report - "
                    + new SimpleDateFormat("EEEE").format(new Date())
                    + " "
                    + new SimpleDateFormat("MM/dd/yyyy").format(new Date()));

            message.setText(body);
            message.setFrom(new InternetAddress(Main.getInstance().getProfile().email));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress("ivcreport-l@lists.usu.edu"));
            transport.sendMessage(message, message.getAllRecipients());

            props = new Properties();
            props.setProperty("mail.imap.host", "owa.usu.edu");
            props.setProperty("mail.imap.auth", "true");
            props.put("mail.imap.port", "993");
            props.put("mail.imap.ssl.enable", "true");
            session = Session.getInstance(props, null);

            store = session.getStore("imap");
            store.connect("A" + Main.getInstance().getProfile().aNumber, password);

            Folder folder = (Folder) store.getFolder("Sent Items");
            if (!folder.exists()) {
                folder.create(Folder.HOLDS_MESSAGES);
            }
            folder.open(Folder.READ_WRITE);
            folder.appendMessages(new Message[]{message});

        } catch (Exception e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                store.close();
            } catch (Exception e) {
            }
            try {
                transport.close();
            } catch (Exception e) {

            }
        }

        return true;
    }
}
