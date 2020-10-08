package olivetasks

import grails.gorm.transactions.Transactional

import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class MyMailService {

    def newTaskHtml(Task task) {
        def html
        html = '<html><head>\n' +
                '<style>\n' +
                '#customers {\n' +
                '  font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;\n' +
                '  border-collapse: collapse;\n' +
                '  width: 100%;\n' +
                '}\n' +
                '\n' +
                '#customers td, #customers th {\n' +
                '  border: 1px solid #ddd;\n' +
                '  padding: 8px;\n' +
                '}\n' +
                '\n' +
                '#customers tr:nth-child(even){background-color: #f2f2f2;}\n' +
                '\n' +
                '#customers tr:hover {background-color: #ddd;}\n' +
                '\n' +
                '#customers th {\n' +
                '  padding-top: 12px;\n' +
                '  padding-bottom: 12px;\n' +
                '  text-align: left;\n' +
                '  background-color: #4CAF50;\n' +
                '  color: white;\n' +
                '}\n' +
                '</style>\n' +
                '<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">' +
                '</head>' + '<body>'
        html += '<div class="card shadow mb-4">'

            html += '<div class="card-header py-3">'
                html += '<h4 class="m-0 font-weight-bold text-primary">'
                html += 'Hi, ' + task.employee.fullname + '!</h4>' +
                        '<h4>Here is a new task assigned to you!</h4><br>';
            html += '</div>';

            html += '<div class="card-body">';
                html += '<div class="myclass">';
                    html += '<div id="customers" class="content scaffold-list" role="main">';
                        html += '<table>';
                            html += '<thead>';
                                html += '<tr>';
                                    html += '<th>Task</th> <th>' + task.taskName + '</th>';
                            html += '</tr></thead><tbody>';
                                html += '<tr>'
                                    html += '<td>Description</td>';
                                    html += '<td>' + task.description + '</td>';
                                html += '</tr>'
        html += '<tr>'
            html += '<td>Creation Date</td>';
            html += '<td>' + task.creationDate + '</td>';
        html += '</tr>'
        html += '<tr>'
            html += '<td>Deadline</td>';
            html += '<td>' + task.deadline + '</td>';
        html += '</tr>'
        html+= '</tbody></table></div></div></div></div>' +
                '<br><h4>Regards,</h4>' +
                '<h4>Admin</h4>' +
                '<h4>Olive Software Solution</h4>' +
                '</body></html>';

        return html
    }

    @Transactional
    def taskDoneHtml(Task task) {
        def html
        html = '<html><head>\n' +
                '<style>\n' +
                '#customers {\n' +
                '  font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;\n' +
                '  border-collapse: collapse;\n' +
                '  width: 100%;\n' +
                '}\n' +
                '\n' +
                '#customers td, #customers th {\n' +
                '  border: 1px solid #ddd;\n' +
                '  padding: 8px;\n' +
                '}\n' +
                '\n' +
                '#customers tr:nth-child(even){background-color: #f2f2f2;}\n' +
                '\n' +
                '#customers tr:hover {background-color: #ddd;}\n' +
                '\n' +
                '#customers th {\n' +
                '  padding-top: 12px;\n' +
                '  padding-bottom: 12px;\n' +
                '  text-align: left;\n' +
                '  background-color: #4CAF50;\n' +
                '  color: white;\n' +
                '}\n' +
                '</style>\n' +
                '<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">' +
                '</head>' + '<body>'
        html += '<div class="card shadow mb-4">'

        html += '<div class="card-header py-3">'
        html += '<h4 class="m-0 font-weight-bold text-primary">'
        html += 'Hi, ' + (Employee.get(task.assignee))?.fullname + '!</h4>' +
                '<h4> '+ task.employee.fullname + ' has completed a task!</h4><br>';
        html += '</div>';

        html += '<div class="card-body">';
        html += '<div class="myclass">';
        html += '<div id="customers" class="content scaffold-list" role="main">';
        html += '<table>';
        html += '<thead>';
        html += '<tr>';
        html += '<th>Task</th> <th>' + task.taskName + '</th>';
        html += '</tr></thead><tbody>';
        html += '<tr>'
        html += '<td>Description</td>';
        html += '<td>' + task.description + '</td>';
        html += '</tr>'
        html += '<tr>'
        html += '<td>Creation Date</td>';
        html += '<td>' + task.creationDate + '</td>';
        html += '</tr>'

        html += '<tr>'
        html += '<td>Completion Date</td>';
        html += '<td>' + task.endDate + '</td>';
        html += '</tr>'

        html += '<tr>'
        html += '<td>Total Time Spent</td>';
        html += '<td>' + task.timeSpent + '</td>';
        html += '</tr>'

        html += '<tr>'
        html += '<td>Deadline</td>';
        html += '<td>' + task.deadline + '</td>';
        html += '</tr>'
        html+= '</tbody></table></div></div></div></div>' +
                '<br><h4>Regards,</h4>' +
                '<h4>Admin</h4>' +
                '<h4>Olive Software Solution</h4>' +
                '</body></html>';

        return html
    }

    def pendingTaskHtml(Employee employee, List<Task> tasks) {
        def html
        html = '<html><head>\n' +
                '<style>\n' +
                '#customers {\n' +
                '  font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;\n' +
                '  border-collapse: collapse;\n' +
                '  width: 100%;\n' +
                '}\n' +
                '\n' +
                '#customers td, #customers th {\n' +
                '  border: 1px solid #ddd;\n' +
                '  padding: 8px;\n' +
                '}\n' +
                '\n' +
                '#customers tr:nth-child(even){background-color: #f2f2f2;}\n' +
                '\n' +
                '#customers tr:hover {background-color: #ddd;}\n' +
                '\n' +
                '#customers th {\n' +
                '  padding-top: 12px;\n' +
                '  padding-bottom: 12px;\n' +
                '  text-align: left;\n' +
                '  background-color: #4CAF50;\n' +
                '  color: white;\n' +
                '}\n' +
                '</style>\n' +
                '<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">' +
                '</head>' + '<body>'
        html += '<div class="card shadow mb-4">'

        html += '<div class="card-header py-3">'
        html += '<h4 class="m-0 font-weight-bold text-primary">'
        html += 'Hi, ' + employee.fullname + '!</h4>' +
                '<h4>These tasks are pending!</h4><br>';
        html += '</div>';

        html += '<div class="card-body">';
        html += '<div class="myclass">';
        html += '<div id="customers" class="content scaffold-list" role="main">';

        for(int i=0; i<tasks.size(); i++) {
            html += '<br>'
            html += '<table>';
            html += '<thead>';
            html += '<tr>';
            html += '<th>Task</th> <th>' + tasks.get(i).taskName + '</th>';
            html += '</tr></thead><tbody>';
            html += '<tr>'
            html += '<td>Description</td>';
            html += '<td>' + tasks.get(i).description + '</td>';
            html += '</tr>'
            html += '<tr>'
            html += '<td>Creation Date</td>';
            html += '<td>' + tasks.get(i).creationDate + '</td>';
            html += '</tr>'
            html += '<tr>'
            html += '<td>Deadline</td>';
            html += '<td>' + tasks.get(i).deadline + '</td>';
            html += '</tr>'
            html += '</tbody></table>'
        }

        html += '</div></div></div></div>' +
                '<br><h4>Regards,</h4>' +
                '<h4>Admin</h4>' +
                '<h4>Olive Software Solution</h4>' +
                '</body></html>';

        return html
    }

    public static void sendEmail(String to, String subject, String content, List<String> ccList) {

        String username //= "no-reply@olivesofts.com";//"sayyedfj15.comp@coep.ac.in";//change accordingly
        String password //= "Olive@12345";//change accordingly
        String from
        String host
        Properties props
        String Sender
        String[] cc



        if(SendMailConfig.count() > 0) {
            SendMailConfig config = SendMailConfig.last(sort: 'id')
            username = config.getUsername()
            password = config.getPassword()
            from = username;
            Sender = config.getSenderName()
            host = config.getHost()
            props = config.getProperties()

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
                message.setFrom(new InternetAddress(from, Sender));

                // Set To: header field of the header.
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(to));

                if(ccList != null && !ccList.isEmpty()) {
                    cc = ccList.toArray()
                    if(cc.size() > 0) {
                        InternetAddress[] ccAddress = new InternetAddress[cc.length];

                        //TODO : Optimize for loops - use only one loop
                        // To get the array of ccaddresses
                        for( int i = 0; i < cc.length; i++ ) {
                            ccAddress[i] = new InternetAddress(cc[i]);
                        }

                        // Set cc: header field of the header.
                        for( int i = 0; i < ccAddress.length; i++) {
                            message.addRecipient(Message.RecipientType.CC, ccAddress[i]);
                        }
                    }
                }

                // Set Subject: header field
                message.setSubject(subject);

                // Now set the actual message
                message.setContent(content, "text/html");

                // Send message
                System.out.println(Transport.send(message));

                System.out.println("Sent message successfully....");

            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            println("sendEmail() Failed: Email Account for sending emails is not configured.");
        }
    }
}
