package olivetasks

import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class ThreadDemo {

    public static void my(String[] args) {

        String to = "sfjilani5dec@gmail.com";

        // Sender's email ID needs to be mentioned
        String from = "oliveinfo12345@gmail.com";
        final String username = "oliveinfo12345@gmail.com";
        final String password = "Olive@12345"//"Azz@Rah.Olive88";

        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.ssl", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");


        // Get the Session object.
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            // Set Subject: header field
            message.setSubject("Testing Subject");

            // Now set the actual message
            message.setText("Hello, this is sample for to check send " +
                    "email using JavaMailAPI ");

            // Send message
            System.out.println(Transport.send(message));

            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            //throw new RuntimeException(e);
            e.printStackTrace()
        }

        //new ThreadDemo().dateExample()

        //Non-blocking fun() example
        /*println("Going to Pool")

        GParsPool.withPool() {
            def p =  {new ThreadDemo().sample()}.asyncFun()
            p()
            println("Exiting pool without killing other thread!")
            return;
        }

        println("Out of Pool")*/
    }

    def dateExample() {
        Date date = new Date()
        println('Date is ' + date)

        date = DateUtil.addDays(date, 3);
        date.setHours(12)
        date.setMinutes(0)
        date.setSeconds(0)
        println('New Date is ' + date)
    }


    def sample() {
        println("Doing expensive calculations... (It wil run forever! Don't wait!)")
        Thread.sleep(4000)
        println("Still Executing")
    }
}
