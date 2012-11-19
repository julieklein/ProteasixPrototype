package com.google.gwt.proteasixprototype.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * I use this class to store my mysql recordset in an object that is an array.
 * This will give an example of how I pass data from the server to client in an object, 
 * one of my favourites for its simplicity.
 * 
 * @author branflake2267
 *
 */


public class ProteaseData implements IsSerializable{
	
	//fields to store data
	public String P_NL_Name;
	public String P_Symbol;
	public String P_Uniprotid;
	public String P_Ecnumber;
	public String P_OMIM;
	
	public int totalSPSL;
	public int totalSS;
	public int totalHP;
	public int totalP;
	public int total1mm;
	public int total2mm;
	public int total3mm;
	
	
	/**
     * constructor
     */
    public ProteaseData() {
            // nothing to do
    }

	

}
