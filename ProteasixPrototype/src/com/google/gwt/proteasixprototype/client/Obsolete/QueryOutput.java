package com.google.gwt.proteasixprototype.client.Obsolete;
import com.google.gwt.proteasixprototype.client.CleavageSiteData;
import com.google.gwt.proteasixprototype.client.PeptideData;
import com.google.gwt.proteasixprototype.client.ProteaseData;
import com.google.gwt.proteasixprototype.client.SubstrateData;
import com.google.gwt.user.client.rpc.IsSerializable;

public class QueryOutput implements IsSerializable{

	public ProteaseData protease;
	public PeptideData peptide;
	public SubstrateData substrate;
	public CleavageSiteData cleavagesite;
	
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
