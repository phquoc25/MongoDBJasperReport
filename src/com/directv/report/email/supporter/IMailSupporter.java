package com.directv.report.email.supporter;

import javax.mail.Session;

public interface IMailSupporter {
	Session createSession();
	void sendMail();
}
