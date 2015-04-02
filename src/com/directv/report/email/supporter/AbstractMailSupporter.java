package com.directv.report.email.supporter;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public abstract class AbstractMailSupporter implements IMailSupporter{
	
	protected Session session;
	String sender;
	String password;
	Properties props;
	String receivers;
	String ccReceivers;
	String subject;
	String body;

	List<String> attachments;
	
	public AbstractMailSupporter(String sender, String password, Properties props, String receivers) {
		this.sender = sender;
		this.password = password;
		this.props = props;
		this.receivers = receivers;
		createSession();
	}
	
	public Session createSession() {

		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(sender, password);
			}
		};

		session = Session.getDefaultInstance(props, auth);
		return session;
	}
	
	@Override
	public void sendMail() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					MimeMessage msg = new MimeMessage(session);
					msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
					msg.addHeader("format", "flowed");
					msg.addHeader("Content-Transfer-Encoding", "8bit");

					msg.setFrom(new InternetAddress(sender, "NoReply"));

					msg.setSubject(subject, "UTF-8");

					msg.setSentDate(new Date());

					msg.setRecipients(Message.RecipientType.TO,
							InternetAddress.parse(receivers, false));
					if(!ccReceivers.isEmpty()){
						msg.setRecipients(Message.RecipientType.CC,
								InternetAddress.parse(ccReceivers, false));
					}
					// Create the message body part
					BodyPart messageBodyPart = new MimeBodyPart();

					// Fill the message
					messageBodyPart.setText(body);

					// Create a multipart message for attachment
					Multipart multipart = new MimeMultipart();

					// Set text message part
					multipart.addBodyPart(messageBodyPart);

					// Second part is attachment
					if(!attachments.isEmpty()){
						DataSource source = null;
						for (Iterator<String> iterator = attachments.iterator(); iterator
								.hasNext();) {
							String attachmentName = iterator.next();
							messageBodyPart = new MimeBodyPart();
							source = new FileDataSource(attachmentName);
							messageBodyPart.setDataHandler(new DataHandler(source));
							messageBodyPart.setFileName(attachmentName);
							multipart.addBodyPart(messageBodyPart);
						}
						
					}

					// Send the complete message parts
					msg.setContent(multipart);

					// Send message
					Transport.send(msg);
					System.out
							.println("EMail Sent Successfully with attachment!!");
				} catch (MessagingException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
		
	}
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	public String getReceivers() {
		return receivers;
	}

	public void setReceivers(String receivers) {
		this.receivers = receivers;
	}

	public String getCcReceivers() {
		return ccReceivers;
	}

	public void setCcReceivers(String ccReceivers) {
		this.ccReceivers = ccReceivers;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public List<String> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<String> attachments) {
		this.attachments = attachments;
	}

}
