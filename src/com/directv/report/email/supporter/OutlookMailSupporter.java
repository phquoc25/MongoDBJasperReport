package com.directv.report.email.supporter;

import java.util.Properties;

public class OutlookMailSupporter extends AbstractMailSupporter{

	public OutlookMailSupporter(String sender, String password,
			Properties props, String receivers) {
		super(sender, password, props, receivers);
	}

}
