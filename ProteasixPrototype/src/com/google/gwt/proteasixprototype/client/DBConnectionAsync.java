package com.google.gwt.proteasixprototype.client;

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
    public void getResultInfo(QueryInput[] inputObject, AsyncCallback<QueryOutput[]> callback);

}
