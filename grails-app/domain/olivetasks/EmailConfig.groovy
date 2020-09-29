package olivetasks

class EmailConfig {

    String username
    String password
    String host
    String port

    EmailConfig() {}

    EmailConfig(String username, String password, String host, String port) {
        this.username = username
        this.password = password
        this.host = host
        this.port = port
    }

    Properties getProperties() {
        Properties properties = new Properties();
        // properties.put("mail.debug", "true");
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", host);
        properties.put("mail.imaps.port", port);
        properties.put("mail.imaps.timeout", "10000");
        return  properties;
    }

    static constraints = {
        username nullable: false, blank: false
        password nullable: false, blank: false
    }

    static mapping = {
        password column: '`password`'
    }
}
