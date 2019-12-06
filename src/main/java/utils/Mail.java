package utils;

import entity.ContactPerson;
import entity.SkiTeacher;
import entity.Token;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
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
     * @param user
     */
    public void sendSetPasswordMail(Token token, SkiTeacher user) {

        Message message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(pl.prop.getProperty("mail.user")));

            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
            message.setSubject("Set your simplinize password");

            String mailBody = "Hello " + user.getFirstName() + " " +
                    user.getLastName() + "<br>" +
                    "Please set a password for your account! <br> Your token --> " +
                    token.getToken();

            //button url e.g https://simplinize.scharez.at/auth/setPassword/30945020293840949094

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(mailBody, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendConfirmation(Token token, ContactPerson person) {

        Message message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(pl.prop.getProperty("mail.user")));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(person.getEmail()));
            message.setSubject("Pleas confirm your Email");

            String mailBody = "Hello " + person.getFirstName() + " " + person.getLastName() +
                    "<br>" + "Please confirm your Email!" +
                    "<br> Token --> " + token.getToken();

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(mailBody, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
