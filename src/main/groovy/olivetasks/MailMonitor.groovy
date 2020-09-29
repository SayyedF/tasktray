package olivetasks

import com.sun.mail.imap.IMAPFolder
import com.sun.mail.imap.IMAPStore

import javax.mail.*
import javax.mail.event.MessageCountAdapter
import javax.mail.event.MessageCountEvent
import javax.mail.internet.InternetAddress

class MailMonitor {

    public static isRunning = false

    static IdleThread threadCopy = null;
    static IMAPStore store = null;
    static Folder inbox = null;
    static Session session

    private static String username //= "olivedhule@gmail.com" //"sayyedfkj@gmail.com";
    private static String password //= "eloxzoqdhjwhpnzp"  //"dbbeoilwyllpedkp";


    public static void monitorMailbox(String user, String pass, Properties properties) {
        isRunning = true

        username = user
        password = pass

        session = Session.getInstance(properties); // not getDefaultInstance

        try {
            store = (IMAPStore) session.getStore("imaps");
            store.connect(username, password);
            if (!store.hasCapability("IDLE")) {
                throw new RuntimeException("IDLE not supported");
            }

            inbox = (IMAPFolder) store.getFolder("INBOX");
            inbox.addMessageCountListener(new MessageCountAdapter() {

                @Override
                public void messagesAdded(MessageCountEvent event) {
                    Message[] messages = event.getMessages();

                    for (Message message : messages) {
                        try {
                            String content = "";
                            String word = "[TASK]";
                            String subject = message.getSubject();
                            Address[] froms = message.getFrom();
                            Address[] recipients = message.getRecipients(Message.RecipientType.TO)
                            Address[] ccs = message.getRecipients(Message.RecipientType.CC)

                            String senderId = froms == null ? null : ((InternetAddress) froms[0]).getAddress();
                            String receiverId = recipients == null ? null : ((InternetAddress) recipients[0]).getAddress();
                            String cc = ccs == null ? null : ((InternetAddress) ccs[0]).getAddress();

                            if (subject.contains(word)) {

                                subject = subject.replaceAll("\\[TASK\\]", "");
                                subject = subject.trim();

                                System.out.println("From:- " + senderId);
                                System.out.println("To:- " + receiverId);
                                System.out.println("CC:- " + cc);
                                System.out.println("Subject:- " + subject);

                                content = ParseUtil.getTextFromMessage(message);
                                System.out.println("Mail Content: "+ content);

                                System.out.println("Saving task...");

                                new TaskUtil().saveTask(senderId, receiverId, subject, content)
                                //System.out.println("Task saved!")
                            } else
                                println("New email is irrelevant (Token " + word +  " not found in Mail Subject)")

                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            IdleThread idleThread = new IdleThread(inbox);
            threadCopy = idleThread
            idleThread.setDaemon(false);
            idleThread.start();

            idleThread.join();

            // idleThread.kill(); //to terminate from another thread

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            close(inbox);
            close(store);
        }
    }

    private static class IdleThread extends Thread {
        private final Folder folder;
        private volatile boolean running = true;

        public IdleThread(Folder folder) {
            super();
            this.folder = folder;
        }

        public synchronized void kill() {

            if (!running)
                return;
            this.running = false;
            isRunning = false;
        }

        @Override
        public void run() {
            while (running) {

                try {
                    ensureOpen(folder);
                    System.out.println("enter idle");
                    ((IMAPFolder) folder).idle();
                } catch (Exception e) {
                    // something went wrong
                    // wait and try again
                    e.printStackTrace();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e1) {
                        // ignore
                    }
                }

            }
        }
    }

    public static void close(final Folder folder) {
        try {
            if (folder != null && folder.isOpen()) {
                folder.close(false);
            }
        } catch (final Exception e) {
            // ignore
        }

    }

    public static void close(final Store store) {
        try {
            if (store != null && store.isConnected()) {
                store.close();
            }
        } catch (final Exception e) {
            // ignore
        }

    }

    public static void stopMonitor() {
        if(threadCopy != null) {
            threadCopy.kill();
            close(inbox);
            close(store);
            threadCopy = null;
            println("Mail Monitor Stopped.")
        }
        else println("Monitor is not running")
    }

    public static void ensureOpen(final Folder folder) throws MessagingException {

        if (folder != null) {
            Store store = folder.getStore();
            if (store != null && !store.isConnected()) {
                store.connect(username, password);
            }
        } else {
            throw new MessagingException("Unable to open a null folder");
        }

        if (folder.exists() && !folder.isOpen() && (folder.getType() & Folder.HOLDS_MESSAGES) != 0) {
            System.out.println("open folder " + folder.getFullName());
            folder.open(Folder.READ_ONLY);
            if (!folder.isOpen())
                throw new MessagingException("Unable to open folder " + folder.getFullName());
        }

    }
}

/* Mail Monitor Backup
class MailMonitor {

    public static isRunning = false

    static IdleThread threadCopy = null;
    static IMAPStore store = null;
    static Folder inbox = null;
    static Session session

    private static final String username = "olivedhule@gmail.com" //"sayyedfkj@gmail.com";
    private static final String password = "eloxzoqdhjwhpnzp"  //"dbbeoilwyllpedkp";


    public static void monitorMailbox() {
        isRunning = true
        Properties properties = new Properties();
        // properties.put("mail.debug", "true");
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", "imap.gmail.com");
        properties.put("mail.imaps.port", "993");
        properties.put("mail.imaps.timeout", "10000");

        session = Session.getInstance(properties); // not getDefaultInstance

        try {
            store = (IMAPStore) session.getStore("imaps");
            store.connect(username, password);
            if (!store.hasCapability("IDLE")) {
                throw new RuntimeException("IDLE not supported");
            }

            inbox = (IMAPFolder) store.getFolder("INBOX");
            inbox.addMessageCountListener(new MessageCountAdapter() {

                @Override
                public void messagesAdded(MessageCountEvent event) {
                    Message[] messages = event.getMessages();

                    for (Message message : messages) {
                        try {
                            String content = "";
                            String word = "[TASK]";
                            String subject = message.getSubject();
                            Address[] froms = message.getFrom();
                            Address[] recipients = message.getRecipients(Message.RecipientType.TO)
                            Address[] ccs = message.getRecipients(Message.RecipientType.CC)

                            String senderId = froms == null ? null : ((InternetAddress) froms[0]).getAddress();
                            String receiverId = recipients == null ? null : ((InternetAddress) recipients[0]).getAddress();
                            String cc = ccs == null ? null : ((InternetAddress) ccs[0]).getAddress();

                            if (subject.contains(word)) {

                                subject = subject.replaceAll("\\[TASK\\]", "");
                                subject = subject.trim();

                                System.out.println("From:- " + senderId);
                                System.out.println("To:- " + receiverId);
                                System.out.println("CC:- " + cc);
                                System.out.println("Subject:- " + subject);

                                content = ParseUtil.getTextFromMessage(message);
                                System.out.println("Mail Content: "+ content);

                                System.out.println("Saving task...");

                                new TaskUtil().saveTask(senderId, receiverId, subject, content)
                                //System.out.println("Task saved!")
                            } else
                                println("New email is irrelevant (Token " + word +  " not found in Mail Subject)")

                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            IdleThread idleThread = new IdleThread(inbox);
            threadCopy = idleThread
            idleThread.setDaemon(false);
            idleThread.start();

            idleThread.join();

            // idleThread.kill(); //to terminate from another thread

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            close(inbox);
            close(store);
        }
    }

    private static class IdleThread extends Thread {
        private final Folder folder;
        private volatile boolean running = true;

        public IdleThread(Folder folder) {
            super();
            this.folder = folder;
        }

        public synchronized void kill() {

            if (!running)
                return;
            this.running = false;
            isRunning = false;
        }

        @Override
        public void run() {
            while (running) {

                try {
                    ensureOpen(folder);
                    System.out.println("enter idle");
                    ((IMAPFolder) folder).idle();
                } catch (Exception e) {
                    // something went wrong
                    // wait and try again
                    e.printStackTrace();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e1) {
                        // ignore
                    }
                }

            }
        }
    }

    public static void close(final Folder folder) {
        try {
            if (folder != null && folder.isOpen()) {
                folder.close(false);
            }
        } catch (final Exception e) {
            // ignore
        }

    }

    public static void close(final Store store) {
        try {
            if (store != null && store.isConnected()) {
                store.close();
            }
        } catch (final Exception e) {
            // ignore
        }

    }

    public static void stopMonitor() {
        if(threadCopy != null) {
            threadCopy.kill();
            close(inbox);
            close(store);
            threadCopy = null;
            println("Mail Monitor Stopped.")
        }
        else println("Monitor is not running")
    }

    public static void ensureOpen(final Folder folder) throws MessagingException {

        if (folder != null) {
            Store store = folder.getStore();
            if (store != null && !store.isConnected()) {
                store.connect(username, password);
            }
        } else {
            throw new MessagingException("Unable to open a null folder");
        }

        if (folder.exists() && !folder.isOpen() && (folder.getType() & Folder.HOLDS_MESSAGES) != 0) {
            System.out.println("open folder " + folder.getFullName());
            folder.open(Folder.READ_ONLY);
            if (!folder.isOpen())
                throw new MessagingException("Unable to open folder " + folder.getFullName());
        }

    }
}



* */