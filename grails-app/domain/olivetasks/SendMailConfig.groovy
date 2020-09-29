package olivetasks

class SendMailConfig {
    String username
    String password
    String host
    String port
    String senderName

    SendMailConfig() {}

    SendMailConfig(String senderName, String username, String password, String host, String port) {
        this.username = username
        this.password = password
        this.host = host
        this.port = port
        this.senderName = senderName
    }

    Properties getProperties() {
        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.ssl", "true");
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        return props
    }


    static constraints = {
        username nullable: false, blank: false
        password nullable: false, blank: false
    }

    static mapping = {
        password column: '`password`'
    }
}
