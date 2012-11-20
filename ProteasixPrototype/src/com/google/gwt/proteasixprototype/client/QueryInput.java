package com.google.gwt.proteasixprototype.client;

import com.google.gwt.user.client.rpc.IsSerializable;


public class QueryInput implements IsSerializable {


	public ProteaseData protease;
	public PeptideData peptide;
	public SubstrateData substrate;
	public CleavageSiteData cleavagesite;
	public boolean onlyNtermcheckbox;


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


public QueryInput() {
	
}

}
