package com.google.gwt.proteasixprototype.server;

import com.google.gwt.proteasixprototype.client.QueryOutput;
import com.google.gwt.proteasixprototype.client.DBConnection;
import com.google.gwt.proteasixprototype.client.ProteaseData;
import com.google.gwt.proteasixprototype.client.PrototypeEntryPoint;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.proteasixprototype.client.QueryInput;
/**
 * server side class for Async calls
 * 
 * 
 * Make sure you add a reference library (external jar in build path) JDBC Connector - 
 * You will see I put it in /opt/classpath/mysql-connector-java-5.1.5/mysql-connector-java-5.1.5-bin.jar
 * 
 * @author branflake2267
 *
 */

public class MySQLConnection extends RemoteServiceServlet implements DBConnection {
	  /**
     * constructor
     */
    public MySQLConnection() {
    	
    }

	@Override
	public QueryOutput[] getResultInfo(QueryInput[] queryIn) throws Throwable {
		
		// TODO Auto-generated method stub
		DB_Protease db = new DB_Protease();

		QueryOutput[] queryOut = db.getResultbySubstrateInfo(queryIn);
		return queryOut;
	}

}
