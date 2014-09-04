package antworld.client;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail
{
  public static void main(String[] args)
  {
    final String username = "adammitchell93@gmail.com";
    final String password = "mangoone";
    
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");
    
    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication()
      {
        return new PasswordAuthentication(username, password);
      }
    });
    try
    
    {
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress("adammitchell93@gmail.com"));
      message.setRecipients(Message.RecipientType.TO,
          InternetAddress.parse("5054405932@messaging.sprintpcs.com"));
      message.setSubject("Test email");
      message.setContent("<h:body style=background-color:white;font-family:verdana;color:#0066CC;>"
          + "If you get this the email functionality worked!!! <br/><br/>"
          + "</body>","text/html; charset=utf-8");
      Transport.send(message);
      
      System.out.println("Email should have been sent.");
    }
    catch (MessagingException e)
    {
      throw new RuntimeException(e);
    }
  }
}
