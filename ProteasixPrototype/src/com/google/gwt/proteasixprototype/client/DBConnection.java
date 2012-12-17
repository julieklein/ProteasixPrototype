package com.google.gwt.proteasixprototype.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.List;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.google.gwt.proteasixprototype.client.QueryInput;
import com.google.gwt.proteasixprototype.client.QueryOutput;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBConnection extends RemoteService {
	/**
     * get protease info
     * 
     * @return
	 * @throws Throwable 
     */
    public QueryOutput[] getResultInfo(QueryInput[] inputObject);
    
    public void setProperties(String from, String host);

    public void setSession(final String user, final String pass, String from, String host);

    public void setSession(String user, String pass);

    public void sendMail(String code, String to, String subject, String body, QueryOutput[] queryCSOut, QueryOutput[] queryProtOut);

    public void setDebug(boolean debug);
    
    public void runMailer(String code, String to, String subject, String body, QueryOutput[] queryCSOut, QueryOutput[] queryProtOut);


}
