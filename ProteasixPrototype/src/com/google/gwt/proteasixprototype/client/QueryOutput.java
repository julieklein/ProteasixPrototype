package com.google.gwt.proteasixprototype.client;

import com.google.gwt.user.client.rpc.IsSerializable;
public class QueryOutput implements IsSerializable{

	public ProteaseData protease;
	public PeptideData peptide;
	public SubstrateData substrate;
	public CleavageSiteData cleavagesite;
	public String confidence;
	public String SPSL;
	public String SS;
	public String HP;
	public String P;
	public String mm1;
	public String mm2;
	public String mm3;
	public String MatrixDB;
	public String test;

	
	public ProteaseData getProtease() {
		return protease;
	}




	public void setProtease(ProteaseData protease) {
		this.protease = protease;
	}




	public PeptideData getPeptide() {
		return peptide;
	}




	public void setPeptide(PeptideData peptide) {
		this.peptide = peptide;
	}




	public SubstrateData getSubstrate() {
		return substrate;
	}




	public void setSubstrate(SubstrateData substrate) {
		this.substrate = substrate;
	}




	public CleavageSiteData getCleavagesite() {
		return cleavagesite;
	}




	public void setCleavagesite(CleavageSiteData cleavagesite) {
		this.cleavagesite = cleavagesite;
	}




	/**
     * constructor
     */
    public QueryOutput() {
            // nothing to do
    }

	
}
