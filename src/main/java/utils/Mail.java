package utils;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

public class Mail {

    private Session session;

    private PropertyLoader pl = new PropertyLoader();

    public Mail() {

        Properties prop = System.getProperties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "mail.scharez.at");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.ssl.trust", "mail.scharez.at");

        session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(pl.prop.getProperty("mail.user"), pl.prop.getProperty("mail.password"));
            }
        });

    }

    /**
     * Send an email to the teacher who was created by the admin to assign a password
     *
     * @param token
     */
    public void sendRegisterEmail() {

    }

    /**
     * Send an email to the admin when the teacher has finished the registration process
     *
     * @param token
     */
    public void sendRegisterConfirmation() {

    }
}
