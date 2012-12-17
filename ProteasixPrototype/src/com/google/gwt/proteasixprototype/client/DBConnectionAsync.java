package com.google.gwt.proteasixprototype.client;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.List;
import com.google.gwt.proteasixprototype.client.QueryInput;
import com.google.gwt.proteasixprototype.client.QueryOutput;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBConnectionAsync {
	/**
	 * get protease info
	 * 
	 * @return
	 * @throws Throwable
	 */
	public void getResultInfo(QueryInput[] inputObject,
			AsyncCallback<QueryOutput[]> callback);

	void sendMail(String code, String to, String subject, String body, QueryOutput[] queryCSOut, QueryOutput[] queryProtOut,
			AsyncCallback callback);

	void setDebug(boolean debug, AsyncCallback callback);

	void setProperties(String from, String host, AsyncCallback callback);

	void setSession(String user, String pass, String from, String host,
			AsyncCallback callback);

	void setSession(String user, String pass, AsyncCallback callback);

	void runMailer(String code, String to, String subject, String body, QueryOutput[] queryCSOut, QueryOutput[] queryProtOut,
			AsyncCallback callback);

}
