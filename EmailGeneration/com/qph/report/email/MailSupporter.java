package com.qph.report.email;

import java.io.UnsupportedEncodingException;
import java.util.Date;
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


public class MailSupporter {
	public static final int GMAIL = 1;
	public static final int WORKLOAD = 2;
	public static final int OUTLOOK = 3;
	Session session;

	public MailSupporter(int provider, String fromEmail, String password) {
		session = createSessionFor(provider, fromEmail, password);
	}

	private Session createSessionFor(int mailProvider, final String fromEmail,
			final String password) {
		if(mailProvider == GMAIL){
			System.out.println("SSLEmail Start");
			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");

			Authenticator auth = new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(fromEmail, password);
				}
			};

			session = Session.getDefaultInstance(props, auth);
			System.out.println("Session created");
			return session;
		}else if (mailProvider == WORKLOAD) {
			System.out.println("Email Start");
			Properties props = new Properties();
			props.put("mail.smtp.host", "workload.vn");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "587");

			Authenticator auth = new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(fromEmail, password);
				}
			};

			session = Session.getDefaultInstance(props, auth);
			System.out.println("Session created");
			return session;
		}else if (mailProvider == OUTLOOK) {
			System.out.println("Outlook Email Start");
			Properties props = new Properties();
			props.put("mail.smtp.host", "mail.fsoft.com.vn");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "587");

			Authenticator auth = new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(fromEmail, password);
				}
			};

			session = Session.getDefaultInstance(props, auth);
			System.out.println("Session created");
			return session;
		}

		return null;
	}

	public void sendEmail(final String fromEmail, final String replyEmail,
			final String toEmail, final String subject, final String body) {

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					MimeMessage msg = new MimeMessage(session);
					// set message headers
					msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
					msg.addHeader("format", "flowed");
					msg.addHeader("Content-Transfer-Encoding", "8bit");

					msg.setFrom(new InternetAddress(fromEmail, "NoReply"));

					msg.setReplyTo(InternetAddress.parse(replyEmail, false));

					msg.setSubject(subject, "UTF-8");

					msg.setText(body, "UTF-8", "html");

					msg.setSentDate(new Date());

					msg.setRecipients(Message.RecipientType.TO,
							InternetAddress.parse(toEmail, false));
					
					System.out.println("Message is ready");
					Transport.send(msg);
					System.out.println("EMail Sent Successfully!!");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
	}
	
	public void sendEmail(final String fromEmail, final String replyEmail,
			final String toEmail, final String ccEmails, final String subject, final String body) {

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					MimeMessage msg = new MimeMessage(session);
					// set message headers
					msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
					msg.addHeader("format", "flowed");
					msg.addHeader("Content-Transfer-Encoding", "8bit");

					msg.setFrom(new InternetAddress(fromEmail, "NoReply"));

					msg.setReplyTo(InternetAddress.parse(replyEmail, false));

					msg.setSubject(subject, "UTF-8");

					msg.setText(body, "UTF-8", "html");

					msg.setSentDate(new Date());

					msg.addRecipients(Message.RecipientType.TO,
							InternetAddress.parse(toEmail, false));
					msg.addRecipients(Message.RecipientType.CC,
							InternetAddress.parse(ccEmails, false));
					
					System.out.println("Message is ready");
					Transport.send(msg);
					System.out.println("EMail Sent Successfully!!");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
	}

	public void sendAttachmentEmail(final String fromEmail,
			final String replyEmail, final String toEmail,
			final String subject, final String body, final String attachmentName) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					MimeMessage msg = new MimeMessage(session);
					msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
					msg.addHeader("format", "flowed");
					msg.addHeader("Content-Transfer-Encoding", "8bit");

					msg.setFrom(new InternetAddress(fromEmail, "NoReply"));

					msg.setReplyTo(InternetAddress.parse(replyEmail, false));

					msg.setSubject(subject, "UTF-8");

					msg.setSentDate(new Date());

					msg.setRecipients(Message.RecipientType.TO,
							InternetAddress.parse(toEmail, false));

					// Create the message body part
					BodyPart messageBodyPart = new MimeBodyPart();

					// Fill the message
					messageBodyPart.setText(body);

					// Create a multipart message for attachment
					Multipart multipart = new MimeMultipart();

					// Set text message part
					multipart.addBodyPart(messageBodyPart);

					// Second part is attachment
					messageBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource(attachmentName);
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(attachmentName);
					multipart.addBodyPart(messageBodyPart);

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

	public void sendImageEmail(final String fromEmail, final String replyEmail,
			final String toEmail, final String subject, final String body, final String fileFullPath) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					MimeMessage msg = new MimeMessage(session);
					msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
					msg.addHeader("format", "flowed");
					msg.addHeader("Content-Transfer-Encoding", "8bit");

					msg.setFrom(new InternetAddress(fromEmail, "NoReply"));

					msg.setReplyTo(InternetAddress.parse(replyEmail, false));

					msg.setSubject(subject, "UTF-8");

					msg.setSentDate(new Date());

					msg.setRecipients(Message.RecipientType.TO,
							InternetAddress.parse(toEmail, false));

					// Create the message body part
					BodyPart messageBodyPart = new MimeBodyPart();

					messageBodyPart.setText(body);

					// Create a multipart message for attachment
					Multipart multipart = new MimeMultipart();

					// Set text message part
					multipart.addBodyPart(messageBodyPart);

					// Second part is image attachment
					messageBodyPart = new MimeBodyPart();
					String filename = fileFullPath;
					DataSource source = new FileDataSource(filename);
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(filename);
					// Trick is to add the content-id header here
					messageBodyPart.setHeader("Content-ID", "image_id");
					multipart.addBodyPart(messageBodyPart);

					// third part for displaying image in the email body
					messageBodyPart = new MimeBodyPart();
					messageBodyPart.setContent("<h1>Attached Image</h1>"
							+ "<img src='cid:image_id'>", "text/html");
					multipart.addBodyPart(messageBodyPart);

					// Set the multipart message to the email message
					msg.setContent(multipart);

					// Send message
					Transport.send(msg);
					System.out.println("EMail Sent Successfully with image!!");
				} catch (MessagingException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
	}
}