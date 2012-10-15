package com.google.gwt.proteasixprototype.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DBConnection extends RemoteService {
	/**
     * get protease info
     * 
     * @return
	 * @throws Throwable 
     */
    public QueryOutput[] getResultInfo(QueryInput[] inputObject) throws Throwable;

}
