package com.google.gwt.proteasixprototype.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.proteasixprototype.client.CsJava_1;

import com.google.gwt.proteasixprototype.client.CleavageSiteData;
import com.google.gwt.proteasixprototype.client.Loop;
import com.google.gwt.proteasixprototype.client.Mismatch;
import com.google.gwt.proteasixprototype.client.XPathNodeUniprot;
import com.google.gwt.proteasixprototype.client.XPathUniprotPep;
import com.google.gwt.proteasixprototype.client.ParseUniprotPep;
import com.google.gwt.proteasixprototype.client.PeptideData;
import com.google.gwt.proteasixprototype.client.QueryOutput;
import com.google.gwt.proteasixprototype.client.SubstrateData;
import com.google.gwt.proteasixprototype.client.ProteaseData;
import com.google.gwt.proteasixprototype.client.QueryInput;
import com.sun.source.tree.NewClassTree;

import org.apache.commons.digester.rss.RSSDigester;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

/**
 * I extend the DB_Conn abstract class, then I don't have to rewrite code
 * 
 * @author branflake2267
 * 
 */

public class DB_Protease extends DB_Conn {

	/**
	 * constructor - nothing to do
	 */
	public DB_Protease() {
		// nothing to do
	}

	public CsJava_1[] getCsMISMATCHinSql(String queryCleavagesite,
			String substrateuni, String terminus, String nOrC, String pepId, String pepSeq) throws Throwable {
		Connection connection2 = getConn();
		Statement s2 = connection2.createStatement();
		CsJava_1[] csjava = null;
		try {

			System.out.println(s2);
			ResultSet result2 = s2.executeQuery(queryCleavagesite);
			// init object into the size we need, like a recordset
			int rsSize2 = getResultSetSize(result2);
			System.out.println(rsSize2 + "avant");

			int i = 0;

			csjava = new CsJava_1[rsSize2];
			while (result2.next()) {
				if (!result2.getString("CleavageSite_Sequence")
						.equals("------")) {
					csjava[i] = new CsJava_1();
					String cleavageSite_Sequence = result2
							.getString("CleavageSite_Sequence");
					csjava[i].CS_ExternalLink = result2
							.getString("External_link");
					csjava[i].CS_Pmid = result2.getString("PMID");
					csjava[i].P_UniprotId = result2.getString("P_UniprotId");
					csjava[i].CS_InSeq = terminus;
					csjava[i].CS_NorC = nOrC;
					csjava[i].Pep_Id = pepId;
					csjava[i].Pep_Seq = pepSeq;
					int j = 0;

					String searchedsplit[] = terminus.split("");
					String foundsplit[] = cleavageSite_Sequence.split("");
					
					if (!searchedsplit[1].equals(foundsplit[1])) {
						j++;
					}
					if (!searchedsplit[2].equals(foundsplit[2])) {
						j++;
					}
					if (!searchedsplit[3].equals(foundsplit[3])) {
						j++;
					}
					if (!searchedsplit[4].equals(foundsplit[4])) {
						j++;
					}
					if (!searchedsplit[5].equals(foundsplit[5])) {
						j++;
					}
					if (!searchedsplit[6].equals(foundsplit[6])) {
						j++;
					}
					if (!searchedsplit[7].equals(foundsplit[7])) {
						j++;
					}
					if (!searchedsplit[8].equals(foundsplit[8])) {
						j++;
					}
					String mismatch = Integer.toString(j);
					csjava[i].CS_mismatch = mismatch;
					csjava[i].CS_OutSeq = cleavageSite_Sequence;
					i++;
				}
			}
			// clean up
			result2.close();
			s2.close();
			connection2.close();
		} catch (Throwable ignore) {
			System.err.println("Mysql Statement Error: " + queryCleavagesite);
			ignore.printStackTrace();
		}
		return csjava;
	}

	public String getProteaseSql(String queryProtease, String puni)
			throws Throwable {
		String output = null;
		String proteasename = null;
		String proteasesymbol = null;
		String proteaseEc = null;
		String proteasetaxon = null;
		String necessaryP1 = null;
		String necessaryP2 = null;
		String necessaryP3 = null;
		String necessaryP1prime = null;
		String necessaryP2prime = null;
		String necessaryP3prime = null;
		String excludedP1 = null;
		String excludedP2 = null;
		String excludedP3 = null;
		String excludedP1prime = null;
		String excludedP2prime = null;
		String excludedP3prime = null;
		
		Connection connection3 = getConn();
		PreparedStatement ps3 = connection3.prepareStatement(queryProtease);
		try {
			ps3.setString(1, puni);
			System.out.println(ps3);
			ResultSet result3 = ps3.executeQuery();
			while (result3.next()) {
				proteasename = result3.getString("P_NL_Name");
				proteasesymbol = result3.getString("P_Symbol");
				proteaseEc = result3.getString("P_EC_Number");
				proteasetaxon = result3.getString("P_Taxon");
				necessaryP1 = result3.getString("Necessary_P1");
				necessaryP2 = result3.getString("Necessary_P2");
				necessaryP3 = result3.getString("Necessary_P3");
				necessaryP1prime = result3.getString("Necessary_P1prime");
				necessaryP2prime = result3.getString("Necessary_P2prime");
				necessaryP3prime = result3.getString("Necessary_P3prime");
				excludedP1 = result3.getString("Excluded_P1");
				excludedP2 = result3.getString("Excluded_P2");
				excludedP3 = result3.getString("Excluded_P3");
				excludedP1prime = result3.getString("Excluded_P1prime");
				excludedP2prime = result3.getString("Excluded_P2prime");
				excludedP3prime = result3.getString("Excluded_P3prime");
			}
			output = proteasename + "\n" + proteasesymbol + "\n" + proteaseEc
					+ "\n" + proteasetaxon + "\n" + necessaryP1 + "\n" + necessaryP2+ "\n" + necessaryP3+ "\n" + necessaryP1prime+ "\n" + necessaryP2prime+ "\n" + necessaryP3prime + "\n" + excludedP1 + "\n" + excludedP2+ "\n" + excludedP3+ "\n" + excludedP1prime+ "\n" + excludedP2prime+ "\n" + excludedP3prime;
			result3.close();
			ps3.clearParameters();
			ps3.close();
			connection3.close();

		} catch (Throwable ignore) {

			System.err.println("Mysql Statement Error: " + queryProtease);
			ignore.printStackTrace();

		}
		return output;
	}

	public QueryOutput[] getResultbySubstrateInfo(QueryInput[] queryIn)
			throws Throwable {

		int k = 0;
		int kLast = 0;
		int kIntN = 0;
		int kIntC = 0;

		QueryOutput[] lastcapacityarray = new QueryOutput[k];

		for (QueryInput searchReq : queryIn) {

			int rsSize1 = 0;

			QueryOutput[] intermediatecapacityarrayNterm = null;
			QueryOutput[] intermediatecapacityarrayCterm = null;

			NodeList getNodeListbyXPath = null;

			SubstrateData substrate = new SubstrateData();
			PeptideData peptide = new PeptideData();

			String fullsequence = null;
			String pepsequence = null;
			String nTerm = null;
			String cTerm = null;
			String substratename = null;

			String pepSubstrateId = searchReq.getPeptideInUniprot();
			String pepNumber = searchReq.getPeptideInId();
			int pepStart = searchReq.getPeptideInStart();
			int pepEnd = searchReq.getPeptideInEnd();

			substrate.S_Uniprotid = pepSubstrateId;
			peptide.Pep_Id = pepNumber;
			peptide.Pep_Start = pepStart;
			peptide.Pep_End = pepEnd;

			int mismatch = searchReq.getPeptideInMism();
			String proteasespecies = "Human";
			String substratespecies = "Human";

			// Retrieve PEPTIDE SEQUENCE IN UNIPROT
			Document xml = checkSubUniprot(pepSubstrateId);

			XPathUniprotPep XPather4 = new XPathUniprotPep();
			String xpathQuery4 = "/uniprot/entry/protein/recommendedName/fullName/text()";
			NodeList getNodeListbyXPath4 = XPather4.getNodeListByXPath(
					xpathQuery4, xml);

			if (getNodeListbyXPath4.getLength() > 0) {
				XPathNodeUniprot XPathNoder4 = new XPathNodeUniprot();
				String xpathQueryNode4 = "/uniprot/entry/protein/recommendedName/fullName/text()";
				Loop l1 = new Loop();
				for (int j1 = 0; j1 < getNodeListbyXPath4.getLength(); j1++) {
					NodeList getNodeListByXPathNoder4 = XPathNoder4
							.getNodeListByXPath(xpathQueryNode4,
									getNodeListbyXPath4.item(j1));
					LinkedList<String> stringfromNodelist4 = l1
							.getStringfromNodelist(getNodeListByXPathNoder4);

					if (!(stringfromNodelist4.isEmpty())) {
						substrate.S_NL_Name = stringfromNodelist4.getFirst();
						substratename = substrate.S_NL_Name;
					} else {
						substrate.S_NL_Name = "---";
						substratename = substrate.S_NL_Name;
					}
				}
			}
			System.out.println(substratename);
			//

			XPathUniprotPep XPather5 = new XPathUniprotPep();
			String xpathQuery5 = "/uniprot/entry/gene/name[@type='primary']/text()";
			NodeList getNodeListbyXPath5 = XPather5.getNodeListByXPath(
					xpathQuery5, xml);

			if (getNodeListbyXPath4.getLength() > 0) {
				XPathNodeUniprot XPathNoder5 = new XPathNodeUniprot();
				String xpathQueryNode5 = "/uniprot/entry/gene/name[@type='primary']/text()";
				Loop l1 = new Loop();
				for (int j1 = 0; j1 < getNodeListbyXPath4.getLength(); j1++) {
					NodeList getNodeListByXPathNoder5 = XPathNoder5
							.getNodeListByXPath(xpathQueryNode5,
									getNodeListbyXPath5.item(j1));
					LinkedList<String> stringfromNodelist5 = l1
							.getStringfromNodelist(getNodeListByXPathNoder5);
					if (!(stringfromNodelist5.isEmpty())) {
						substrate.S_Symbol = stringfromNodelist5.getFirst();
					} else {
						substrate.S_Symbol = substratename;
					}
				}
			}
			
			XPathUniprotPep XPather = new XPathUniprotPep();
			String xpathQuery = "/uniprot/entry/sequence/text()";
			NodeList getNodeListbyXPath2 = XPather.getNodeListByXPath(
					xpathQuery, xml);

			if (getNodeListbyXPath2.getLength() > 0) {
				XPathNodeUniprot XPathNoder2 = new XPathNodeUniprot();
				String xpathQueryNode2 = "/uniprot/entry/sequence/text()";
				Loop l1 = new Loop();
				for (int j1 = 0; j1 < getNodeListbyXPath2.getLength(); j1++) {
					NodeList getNodeListByXPathNoder2 = XPathNoder2
							.getNodeListByXPath(xpathQueryNode2,
									getNodeListbyXPath2.item(j1));
					LinkedList<String> stringfromNodelist2 = l1
							.getStringfromNodelist(getNodeListByXPathNoder2);
					fullsequence = stringfromNodelist2.getFirst();
					fullsequence = fullsequence.replaceAll("\n", "");
					pepsequence = fullsequence.substring(pepStart - 1, pepEnd);
					peptide.Pep_sequence = pepsequence;

					// Retrieve N-term & C-term CS
					if (!(pepStart - 5 < 0)
							&& !(pepEnd + 4 > fullsequence.length())) {
						nTerm = fullsequence.substring(pepStart - 5,
								pepStart + 3);
						cTerm = fullsequence.substring(pepEnd - 4, pepEnd + 4);
					} else if (!(pepStart - 5 < 0)
							&& (pepEnd + 4 > fullsequence.length())) {
						nTerm = fullsequence.substring(pepStart - 5,
								pepStart + 3);
						cTerm = fullsequence.substring(pepEnd - 4, fullsequence.length());
						for (int i = fullsequence.length(); i < pepEnd + 4; i++) {
							cTerm = cTerm + "-";
						}
					} else if ((pepStart - 5 < 0)
							&& !(pepEnd + 4 > fullsequence.length())) {
						cTerm = fullsequence.substring(pepEnd - 4, pepEnd + 4);
						nTerm = fullsequence.substring(0, pepStart + 3);
						for (int i = pepStart - 5; i < 0; i++) {
							nTerm = "-" + nTerm;
						}
					} else if ((pepStart - 5 < 0)
							&& (pepEnd + 4 > fullsequence.length())) {
						nTerm = fullsequence.substring(0, pepStart + 3);
						
						for (int i = pepStart - 5; i < 0; i++) {
							nTerm = "-" + nTerm;
						}
						cTerm = fullsequence.substring(pepEnd - 4, fullsequence.length());
						for (int i = fullsequence.length(); i < pepEnd + 4; i++) {
							cTerm = cTerm + "-";
						}
					}

				}
			}
			System.out.println(nTerm);
			System.out.println(cTerm);
			String n1 = nTerm.substring(0, 1);
			String n2 = nTerm.substring(1, 2);
			String n3 = nTerm.substring(2, 3);
			String n4 = nTerm.substring(3, 4);
			String n5 = nTerm.substring(4, 5);
			String n6 = nTerm.substring(5, 6);
			String n7 = nTerm.substring(6, 7);
			String n8 = nTerm.substring(7, 8);

			String c1 = cTerm.substring(0, 1);
			String c2 = cTerm.substring(1, 2);
			String c3 = cTerm.substring(2, 3);
			String c4 = cTerm.substring(3, 4);
			String c5 = cTerm.substring(4, 5);
			String c6 = cTerm.substring(5, 6);
			String c7 = cTerm.substring(6, 7);
			String c8 = cTerm.substring(7, 8);

			Mismatch mismN0[] = new Mismatch[1];
			int numismN0 = 1;
			for (int i = 0; i < numismN0; i++) {
				mismN0[i] = new Mismatch();
			}

			Mismatch mismC0[] = new Mismatch[1];
			int numismC0 = 1;
			for (int i = 0; i < numismC0; i++) {
				mismC0[i] = new Mismatch();
			}

			Mismatch mismN1[] = new Mismatch[8];
			int numismN1 = 8;
			for (int i = 0; i < numismN1; i++) {
				mismN1[i] = new Mismatch();
			}

			Mismatch mismC1[] = new Mismatch[8];
			int numismC1 = 8;
			for (int i = 0; i < numismC1; i++) {
				mismC1[i] = new Mismatch();
			}

			Mismatch mismN2[] = new Mismatch[28];
			int numismN2 = 28;
			for (int i = 0; i < numismN2; i++) {
				mismN2[i] = new Mismatch();
			}

			Mismatch mismC2[] = new Mismatch[28];
			int numismC2 = 28;
			for (int i = 0; i < numismC2; i++) {
				mismC2[i] = new Mismatch();
			}

			Mismatch mismN3[] = new Mismatch[55];
			int numismN3 = 55;
			for (int i = 0; i < numismN3; i++) {
				mismN3[i] = new Mismatch();
			}

			Mismatch mismC3[] = new Mismatch[55];
			int numismC3 = 55;
			for (int i = 0; i < numismC3; i++) {
				mismC3[i] = new Mismatch();
			}

			mismN0[0].setCS_Pattern(n1 + n2 + n3 + n4 + n5 + n6 + n7 + n8);
			mismN0[0].setCS_NorCterm("NTerm");
			mismN0[0].setCS_SubstrateSequence(nTerm);
			mismC0[0].setCS_Pattern(c1 + c2 + c3 + c4 + c5 + c6 + c7 + c8);
			mismC0[0].setCS_NorCterm("CTerm");
			mismC0[0].setCS_SubstrateSequence(cTerm);

			mismN1[0].setCS_Pattern("_" + n2 + n3 + n4 + n5 + n6 + n7 + n8);
			mismN1[1].setCS_Pattern(n1 + "_" + n3 + n4 + n5 + n6 + n7 + n8);
			mismN1[2].setCS_Pattern(n1 + n2 + "_" + n4 + n5 + n6 + n7 + n8);
			mismN1[3].setCS_Pattern(n1 + n2 + n3 + "_" + n5 + n6 + n7 + n8);
			mismN1[4].setCS_Pattern(n1 + n2 + n3 + n4 + "_" + n6 + n7 + n8);
			mismN1[5].setCS_Pattern(n1 + n2 + n3 + n4 + n5 + "_" + n7 + n8);
			mismN1[6].setCS_Pattern(n1 + n2 + n3 + n4 + n5 + n6 + "_" + n8);
			mismN1[7].setCS_Pattern(n1 + n2 + n3 + n4 + n5 + n6 + n7 + "_");
			for (int i = 0; i < 8; i++) {
				mismN1[i].setCS_NorCterm("NTerm");
				mismN1[i].setCS_SubstrateSequence(nTerm);
			}

			mismC1[0].setCS_Pattern("_" + c2 + c3 + c4 + c5 + c6 + c7 + c8);
			mismC1[1].setCS_Pattern(c1 + "_" + c3 + c4 + c5 + c6 + c7 + c8);
			mismC1[2].setCS_Pattern(c1 + c2 + "_" + c4 + c5 + c6 + c7 + c8);
			mismC1[3].setCS_Pattern(c1 + c2 + c3 + "_" + c5 + c6 + c7 + c8);
			mismC1[4].setCS_Pattern(c1 + c2 + c3 + c4 + "_" + c6 + c7 + c8);
			mismC1[5].setCS_Pattern(c1 + c2 + c3 + c4 + c5 + "_" + c7 + c8);
			mismC1[6].setCS_Pattern(c1 + c2 + c3 + c4 + c5 + c6 + "_" + c8);
			mismC1[7].setCS_Pattern(c1 + c2 + c3 + c4 + c5 + c6 + c7 + "_");
			for (int i = 0; i < 8; i++) {
				mismC1[i].setCS_NorCterm("CTerm");
				mismC1[i].setCS_SubstrateSequence(cTerm);
			}

			mismN2[0].setCS_Pattern("_" + "_" + n3 + n4 + n5 + n6 + n7 + n8);
			mismN2[1].setCS_Pattern("_" + n2 + "_" + n4 + n5 + n6 + n7 + n8);
			mismN2[2].setCS_Pattern("_" + n2 + n3 + "_" + n5 + n6 + n7 + n8);
			mismN2[3].setCS_Pattern("_" + n2 + n3 + n4 + "_" + n6 + n7 + n8);
			mismN2[4].setCS_Pattern("_" + n2 + n3 + n4 + n5 + "_" + n7 + n8);
			mismN2[5].setCS_Pattern("_" + n2 + n3 + n4 + n5 + n6 + "_" + n8);
			mismN2[6].setCS_Pattern("_" + n2 + n3 + n4 + n5 + n6 + n7 + "_");

			mismN2[7].setCS_Pattern(n1 + "_" + "_" + n4 + n5 + n6 + n7 + n8);
			mismN2[8].setCS_Pattern(n1 + "_" + n3 + "_" + n5 + n6 + n7 + n8);
			mismN2[9].setCS_Pattern(n1 + "_" + n3 + n4 + "_" + n6 + n7 + n8);
			mismN2[10].setCS_Pattern(n1 + "_" + n3 + n4 + n5 + "_" + n7 + n8);
			mismN2[11].setCS_Pattern(n1 + "_" + n3 + n4 + n5 + n6 + "_" + n8);
			mismN2[12].setCS_Pattern(n1 + "_" + n3 + n4 + n5 + n6 + n7 + "_");

			mismN2[13].setCS_Pattern(n1 + n2 + "_" + "_" + n5 + n6 + n7 + n8);
			mismN2[14].setCS_Pattern(n1 + n2 + "_" + n4 + "_" + n6 + n7 + n8);
			mismN2[15].setCS_Pattern(n1 + n2 + "_" + n4 + n5 + "_" + n7 + n8);
			mismN2[16].setCS_Pattern(n1 + n2 + "_" + n4 + n5 + n6 + "_" + n8);
			mismN2[17].setCS_Pattern(n1 + n2 + "_" + n4 + n5 + n6 + n7 + "_");

			mismN2[18].setCS_Pattern(n1 + n2 + n3 + "_" + "_" + n6 + n7 + n8);
			mismN2[19].setCS_Pattern(n1 + n2 + n3 + "_" + n5 + "_" + n7 + n8);
			mismN2[20].setCS_Pattern(n1 + n2 + n3 + "_" + n5 + n6 + "_" + n8);
			mismN2[21].setCS_Pattern(n1 + n2 + n3 + "_" + n5 + n6 + n7 + "_");

			mismN2[22].setCS_Pattern(n1 + n2 + n3 + n4 + "_" + "_" + n7 + n8);
			mismN2[23].setCS_Pattern(n1 + n2 + n3 + n4 + "_" + n6 + "_" + n8);
			mismN2[24].setCS_Pattern(n1 + n2 + n3 + n4 + "_" + n6 + n7 + "_");

			mismN2[25].setCS_Pattern(n1 + n2 + n3 + n4 + n5 + "_" + "_" + n8);
			mismN2[26].setCS_Pattern(n1 + n2 + n3 + n4 + n5 + "_" + n7 + "_");

			mismN2[27].setCS_Pattern(n1 + n2 + n3 + n4 + n5 + n6 + "_" + "_");

			for (int i = 0; i < 28; i++) {
				mismN2[i].setCS_NorCterm("NTerm");
				mismN2[i].setCS_SubstrateSequence(nTerm);
			}

			mismC2[0].setCS_Pattern("_" + "_" + c3 + c4 + c5 + c6 + c7 + c8);
			mismC2[1].setCS_Pattern("_" + c2 + "_" + c4 + c5 + c6 + c7 + c8);
			mismC2[2].setCS_Pattern("_" + c2 + c3 + "_" + c5 + c6 + c7 + c8);
			mismC2[3].setCS_Pattern("_" + c2 + c3 + c4 + "_" + c6 + c7 + c8);
			mismC2[4].setCS_Pattern("_" + c2 + c3 + c4 + c5 + "_" + c7 + c8);
			mismC2[5].setCS_Pattern("_" + c2 + c3 + c4 + c5 + c6 + "_" + c8);
			mismC2[6].setCS_Pattern("_" + c2 + c3 + c4 + c5 + c6 + c7 + "_");

			mismC2[7].setCS_Pattern(c1 + "_" + "_" + c4 + c5 + c6 + c7 + c8);
			mismC2[8].setCS_Pattern(c1 + "_" + c3 + "_" + c5 + c6 + c7 + c8);
			mismC2[9].setCS_Pattern(c1 + "_" + c3 + c4 + "_" + c6 + c7 + c8);
			mismC2[10].setCS_Pattern(c1 + "_" + c3 + c4 + c5 + "_" + c7 + c8);
			mismC2[11].setCS_Pattern(c1 + "_" + c3 + c4 + c5 + c6 + "_" + c8);
			mismC2[12].setCS_Pattern(c1 + "_" + c3 + c4 + c5 + c6 + c7 + "_");

			mismC2[13].setCS_Pattern(c1 + c2 + "_" + "_" + c5 + c6 + c7 + c8);
			mismC2[14].setCS_Pattern(c1 + c2 + "_" + c4 + "_" + c6 + c7 + c8);
			mismC2[15].setCS_Pattern(c1 + c2 + "_" + c4 + c5 + "_" + c7 + c8);
			mismC2[16].setCS_Pattern(c1 + c2 + "_" + c4 + c5 + c6 + "_" + c8);
			mismC2[17].setCS_Pattern(c1 + c2 + "_" + c4 + c5 + c6 + c7 + "_");

			mismC2[18].setCS_Pattern(c1 + c2 + c3 + "_" + "_" + c6 + c7 + c8);
			mismC2[19].setCS_Pattern(c1 + c2 + c3 + "_" + c5 + "_" + c7 + c8);
			mismC2[20].setCS_Pattern(c1 + c2 + c3 + "_" + c5 + c6 + "_" + c8);
			mismC2[21].setCS_Pattern(c1 + c2 + c3 + "_" + c5 + c6 + c7 + "_");

			mismC2[22].setCS_Pattern(c1 + c2 + c3 + c4 + "_" + "_" + c7 + c8);
			mismC2[23].setCS_Pattern(c1 + c2 + c3 + c4 + "_" + c6 + "_" + c8);
			mismC2[24].setCS_Pattern(c1 + c2 + c3 + c4 + "_" + c6 + c7 + "_");

			mismC2[25].setCS_Pattern(c1 + c2 + c3 + c4 + c5 + "_" + "_" + c8);
			mismC2[26].setCS_Pattern(c1 + c2 + c3 + c4 + c5 + "_" + c7 + "_");

			mismC2[27].setCS_Pattern(c1 + c2 + c3 + c4 + c5 + c6 + "_" + "_");

			for (int i = 0; i < 28; i++) {
				mismC2[i].setCS_NorCterm("CTerm");
				mismC2[i].setCS_SubstrateSequence(cTerm);
			}

			mismN3[0].setCS_Pattern("_" + "_" + "_" + n4 + n5 + n6 + n7 + n8);
			mismN3[1].setCS_Pattern("_" + "_" + n3 + "_" + n5 + n6 + n7 + n8);
			mismN3[2].setCS_Pattern("_" + "_" + n3 + n4 + "_" + n6 + n7 + n8);
			mismN3[3].setCS_Pattern("_" + "_" + n3 + n4 + n5 + "_" + n7 + n8);
			mismN3[4].setCS_Pattern("_" + "_" + n3 + n4 + n5 + n6 + "_" + n8);
			mismN3[5].setCS_Pattern("_" + "_" + n3 + n4 + n5 + n6 + n7 + "_");
			mismN3[6].setCS_Pattern("_" + n2 + "_" + "_" + n5 + n6 + n7 + n8);
			mismN3[7].setCS_Pattern("_" + n2 + "_" + n4 + "_" + n6 + n7 + n8);
			mismN3[8].setCS_Pattern("_" + n2 + "_" + n4 + n5 + "_" + n7 + n8);
			mismN3[9].setCS_Pattern("_" + n2 + "_" + n4 + n5 + n6 + "_" + n8);
			mismN3[10].setCS_Pattern("_" + n2 + "_" + n4 + n5 + n6 + n7 + "_");
			mismN3[11].setCS_Pattern("_" + n2 + n3 + "_" + "_" + n6 + n7 + n8);
			mismN3[12].setCS_Pattern("_" + n2 + n3 + "_" + n5 + "_" + n7 + n8);
			mismN3[13].setCS_Pattern("_" + n2 + n3 + "_" + n5 + n6 + "_" + n8);
			mismN3[14].setCS_Pattern("_" + n2 + n3 + "_" + n5 + n6 + n7 + "_");
			mismN3[15].setCS_Pattern("_" + n2 + n3 + n4 + "_" + "_" + n7 + n8);
			mismN3[16].setCS_Pattern("_" + n2 + n3 + n4 + "_" + n6 + "_" + n8);
			mismN3[17].setCS_Pattern("_" + n2 + n3 + n4 + "_" + n6 + n7 + "_");
			mismN3[18].setCS_Pattern("_" + n2 + n3 + n4 + n5 + "_" + "_" + n8);
			mismN3[19].setCS_Pattern("_" + n2 + n3 + n4 + n5 + "_" + n7 + "_");
			mismN3[20].setCS_Pattern("_" + n2 + n3 + n4 + n5 + n6 + "_" + "_");
			mismN3[21].setCS_Pattern(n1 + "_" + "_" + "_" + n5 + n6 + n7 + n8);
			mismN3[22].setCS_Pattern(n1 + "_" + "_" + n4 + "_" + n6 + n7 + n8);
			mismN3[23].setCS_Pattern(n1 + "_" + "_" + n4 + n5 + "_" + n7 + n8);
			mismN3[24].setCS_Pattern(n1 + "_" + "_" + n4 + n5 + n6 + "_" + n8);
			mismN3[25].setCS_Pattern(n1 + "_" + "_" + n4 + n5 + n6 + n7 + "_");
			mismN3[26].setCS_Pattern(n1 + "_" + n3 + "_" + "_" + n6 + n7 + n8);
			mismN3[27].setCS_Pattern(n1 + "_" + n3 + "_" + n5 + "_" + n7 + n8);
			mismN3[28].setCS_Pattern(n1 + "_" + n3 + "_" + n5 + n6 + "_" + n8);
			mismN3[29].setCS_Pattern(n1 + "_" + n3 + "_" + n5 + n6 + n7 + "_");
			mismN3[30].setCS_Pattern(n1 + "_" + n3 + n4 + "_" + "_" + n7 + n8);
			mismN3[31].setCS_Pattern(n1 + "_" + n3 + n4 + "_" + n6 + "_" + n8);
			mismN3[32].setCS_Pattern(n1 + "_" + n3 + n4 + "_" + n6 + n7 + "_");
			mismN3[33].setCS_Pattern(n1 + "_" + n3 + n4 + n5 + "_" + "_" + n8);
			mismN3[34].setCS_Pattern(n1 + "_" + n3 + n4 + n5 + "_" + n7 + "_");
			mismN3[35].setCS_Pattern(n1 + "_" + n3 + n4 + n5 + n6 + "_" + "_");
			mismN3[36].setCS_Pattern(n1 + n2 + "_" + "_" + "_" + n6 + n7 + n8);
			mismN3[37].setCS_Pattern(n1 + n2 + "_" + "_" + n5 + "_" + n7 + n8);
			mismN3[38].setCS_Pattern(n1 + n2 + "_" + "_" + n5 + n6 + "_" + n8);
			mismN3[39].setCS_Pattern(n1 + n2 + "_" + "_" + n5 + n6 + n7 + "_");
			mismN3[40].setCS_Pattern(n1 + n2 + "_" + n4 + "_" + "_" + n7 + n8);
			mismN3[41].setCS_Pattern(n1 + n2 + "_" + n4 + "_" + n6 + "_" + n8);
			mismN3[42].setCS_Pattern(n1 + n2 + "_" + n4 + n5 + "_" + "_" + n8);
			mismN3[43].setCS_Pattern(n1 + n2 + "_" + n4 + n5 + "_" + n7 + "_");
			mismN3[44].setCS_Pattern(n1 + n2 + "_" + n4 + n5 + n6 + "_" + "_");
			mismN3[45].setCS_Pattern(n1 + n2 + n3 + "_" + "_" + "_" + n7 + n8);
			mismN3[46].setCS_Pattern(n1 + n2 + n3 + "_" + "_" + n6 + "_" + n8);
			mismN3[47].setCS_Pattern(n1 + n2 + n3 + "_" + "_" + n6 + n7 + "_");
			mismN3[48].setCS_Pattern(n1 + n2 + n3 + "_" + n5 + "_" + "_" + n8);
			mismN3[49].setCS_Pattern(n1 + n2 + n3 + "_" + n5 + "_" + n7 + "_");
			mismN3[50].setCS_Pattern(n1 + n2 + n3 + "_" + n5 + n6 + "_" + "_");
			mismN3[51].setCS_Pattern(n1 + n2 + n3 + n4 + "_" + "_" + "_" + n8);
			mismN3[52].setCS_Pattern(n1 + n2 + n3 + n4 + "_" + "_" + n7 + "_");
			mismN3[53].setCS_Pattern(n1 + n2 + n3 + n4 + "_" + n6 + "_" + "_");
			mismN3[54].setCS_Pattern(n1 + n2 + n3 + n4 + n5 + "_" + "_" + "_");

			for (int i = 0; i < 55; i++) {
				mismN3[i].setCS_NorCterm("NTerm");
				mismN3[i].setCS_SubstrateSequence(nTerm);
			}
			mismC3[0].setCS_Pattern("_" + "_" + "_" + c4 + c5 + c6 + c7 + c8);
			mismC3[1].setCS_Pattern("_" + "_" + c3 + "_" + c5 + c6 + c7 + c8);
			mismC3[2].setCS_Pattern("_" + "_" + c3 + c4 + "_" + c6 + c7 + c8);
			mismC3[3].setCS_Pattern("_" + "_" + c3 + c4 + c5 + "_" + c7 + c8);
			mismC3[4].setCS_Pattern("_" + "_" + c3 + c4 + c5 + c6 + "_" + c8);
			mismC3[5].setCS_Pattern("_" + "_" + c3 + c4 + c5 + c6 + c7 + "_");
			mismC3[6].setCS_Pattern("_" + c2 + "_" + "_" + c5 + c6 + c7 + c8);
			mismC3[7].setCS_Pattern("_" + c2 + "_" + c4 + "_" + c6 + c7 + c8);
			mismC3[8].setCS_Pattern("_" + c2 + "_" + c4 + c5 + "_" + c7 + c8);
			mismC3[9].setCS_Pattern("_" + c2 + "_" + c4 + c5 + c6 + "_" + c8);
			mismC3[10].setCS_Pattern("_" + c2 + "_" + c4 + c5 + c6 + c7 + "_");
			mismC3[11].setCS_Pattern("_" + c2 + c3 + "_" + "_" + c6 + c7 + c8);
			mismC3[12].setCS_Pattern("_" + c2 + c3 + "_" + c5 + "_" + c7 + c8);
			mismC3[13].setCS_Pattern("_" + c2 + c3 + "_" + c5 + c6 + "_" + c8);
			mismC3[14].setCS_Pattern("_" + c2 + c3 + "_" + c5 + c6 + c7 + "_");
			mismC3[15].setCS_Pattern("_" + c2 + c3 + c4 + "_" + "_" + c7 + c8);
			mismC3[16].setCS_Pattern("_" + c2 + c3 + c4 + "_" + c6 + "_" + c8);
			mismC3[17].setCS_Pattern("_" + c2 + c3 + c4 + "_" + c6 + c7 + "_");
			mismC3[18].setCS_Pattern("_" + c2 + c3 + c4 + c5 + "_" + "_" + c8);
			mismC3[19].setCS_Pattern("_" + c2 + c3 + c4 + c5 + "_" + c7 + "_");
			mismC3[20].setCS_Pattern("_" + c2 + c3 + c4 + c5 + c6 + "_" + "_");
			mismC3[21].setCS_Pattern(c1 + "_" + "_" + "_" + c5 + c6 + c7 + c8);
			mismC3[22].setCS_Pattern(c1 + "_" + "_" + c4 + "_" + c6 + c7 + c8);
			mismC3[23].setCS_Pattern(c1 + "_" + "_" + c4 + c5 + "_" + c7 + c8);
			mismC3[24].setCS_Pattern(c1 + "_" + "_" + c4 + c5 + c6 + "_" + c8);
			mismC3[25].setCS_Pattern(c1 + "_" + "_" + c4 + c5 + c6 + c7 + "_");
			mismC3[26].setCS_Pattern(c1 + "_" + c3 + "_" + "_" + c6 + c7 + c8);
			mismC3[27].setCS_Pattern(c1 + "_" + c3 + "_" + c5 + "_" + c7 + c8);
			mismC3[28].setCS_Pattern(c1 + "_" + c3 + "_" + c5 + c6 + "_" + c8);
			mismC3[29].setCS_Pattern(c1 + "_" + c3 + "_" + c5 + c6 + c7 + "_");
			mismC3[30].setCS_Pattern(c1 + "_" + c3 + c4 + "_" + "_" + c7 + c8);
			mismC3[31].setCS_Pattern(c1 + "_" + c3 + c4 + "_" + c6 + "_" + c8);
			mismC3[32].setCS_Pattern(c1 + "_" + c3 + c4 + "_" + c6 + c7 + "_");
			mismC3[33].setCS_Pattern(c1 + "_" + c3 + c4 + c5 + "_" + "_" + c8);
			mismC3[34].setCS_Pattern(c1 + "_" + c3 + c4 + c5 + "_" + c7 + "_");
			mismC3[35].setCS_Pattern(c1 + "_" + c3 + c4 + c5 + c6 + "_" + "_");
			mismC3[36].setCS_Pattern(c1 + c2 + "_" + "_" + "_" + c6 + c7 + c8);
			mismC3[37].setCS_Pattern(c1 + c2 + "_" + "_" + c5 + "_" + c7 + c8);
			mismC3[38].setCS_Pattern(c1 + c2 + "_" + "_" + c5 + c6 + "_" + c8);
			mismC3[39].setCS_Pattern(c1 + c2 + "_" + "_" + c5 + c6 + c7 + "_");
			mismC3[40].setCS_Pattern(c1 + c2 + "_" + c4 + "_" + "_" + c7 + c8);
			mismC3[41].setCS_Pattern(c1 + c2 + "_" + c4 + "_" + c6 + "_" + c8);
			mismC3[42].setCS_Pattern(c1 + c2 + "_" + c4 + c5 + "_" + "_" + c8);
			mismC3[43].setCS_Pattern(c1 + c2 + "_" + c4 + c5 + "_" + c7 + "_");
			mismC3[44].setCS_Pattern(c1 + c2 + "_" + c4 + c5 + c6 + "_" + "_");
			mismC3[45].setCS_Pattern(c1 + c2 + c3 + "_" + "_" + "_" + c7 + c8);
			mismC3[46].setCS_Pattern(c1 + c2 + c3 + "_" + "_" + c6 + "_" + c8);
			mismC3[47].setCS_Pattern(c1 + c2 + c3 + "_" + "_" + c6 + c7 + "_");
			mismC3[48].setCS_Pattern(c1 + c2 + c3 + "_" + c5 + "_" + "_" + c8);
			mismC3[49].setCS_Pattern(c1 + c2 + c3 + "_" + c5 + "_" + c7 + "_");
			mismC3[50].setCS_Pattern(c1 + c2 + c3 + "_" + c5 + c6 + "_" + "_");
			mismC3[51].setCS_Pattern(c1 + c2 + c3 + c4 + "_" + "_" + "_" + c8);
			mismC3[52].setCS_Pattern(c1 + c2 + c3 + c4 + "_" + "_" + c7 + "_");
			mismC3[53].setCS_Pattern(c1 + c2 + c3 + c4 + "_" + c6 + "_" + "_");
			mismC3[54].setCS_Pattern(c1 + c2 + c3 + c4 + c5 + "_" + "_" + "_");

			for (int i = 0; i < 55; i++) {
				mismC3[i].setCS_NorCterm("CTerm");
				mismC3[i].setCS_SubstrateSequence(cTerm);
			}

			String prequery = "";
			String queryCleavagesite = "";
			String csSubstrateSequence = "";
			String nOrC = "";
			int rsSize = 0;
			int rsSizeC = 0;

			if (mismatch == 0) {
				int gaps = 0;

				// check CS in SQL 0 MM N term
				if (!nTerm.contains("----")) {
					for (int j = 0; j < numismN0; j++) {
						String cs = mismN0[j].getCS_Pattern();
						prequery = prequery
								+ " OR CleavageSite_Sequence LIKE '" + cs + "'";
					}
					prequery = prequery.replaceFirst(" OR", "");

					String queryprotspecies = "null";
					String querysubspecies = "null";

					if (proteasespecies.equals("Human")) {
						queryprotspecies = "P_Species = 'Human'";
					} else if (proteasespecies.equals("Mouse")) {
						queryprotspecies = "P_Species = 'Mouse'";
					} else if (proteasespecies.equals("Rat")) {
						queryprotspecies = "P_Species = 'Rat'";
					} else {
						queryprotspecies = "";
					}

					if (substratespecies.equals("Human")) {
						querysubspecies = "AND S_Species = 'Human' AND (";
					} else if (substratespecies.equals("Mouse")) {
						querysubspecies = "AND S_Species = 'Mouse' AND (";
					} else if (substratespecies.equals("Rat")) {
						querysubspecies = "AND S_Species = 'Rat' AND (";
					} else {
						querysubspecies = "(";
					}

					queryCleavagesite = "SELECT * FROM CLEAVAGESITE WHERE "
							+ queryprotspecies + querysubspecies + prequery
							+ ")  ORDER BY P_Symbol";
					System.out.println(queryCleavagesite);

					csSubstrateSequence = nTerm;
					nOrC = "NTerm";

					CsJava_1[] csjava = getCsMISMATCHinSql(queryCleavagesite,
							pepSubstrateId, csSubstrateSequence, nOrC, pepNumber, pepsequence);
					rsSize = csjava.length;
					System.out.println(rsSize + "apres");
					//
					if (rsSize > 0) {
						kIntN = rsSize + k;

					} else {
						kIntN = k;

					}
					// size the
					// array
					intermediatecapacityarrayNterm = new QueryOutput[kIntN];
					
					System.arraycopy(lastcapacityarray, 0,
							intermediatecapacityarrayNterm, 0, k);

					if (rsSize > 0) {
						int i = k;
						for (int l = 0; l < csjava.length; l++) {
							populateCsPERFECTSql(
									intermediatecapacityarrayNterm, substrate,
									csjava[l], i, l, pepSubstrateId,
									pepStart, pepEnd, pepNumber, gaps, pepsequence);
							i++;
						}
					}
				} else {
					kIntN = k;
					intermediatecapacityarrayNterm = new QueryOutput[kIntN];
					System.arraycopy(lastcapacityarray, 0,
							intermediatecapacityarrayNterm, 0, k);
				}
				
				// check CS in SQL 0 MM C term
				prequery = "";
				if (!cTerm.contains("----")) {
					for (int j = 0; j < numismC0; j++) {
						String cs = mismC0[j].getCS_Pattern();
						prequery = prequery
								+ " OR CleavageSite_Sequence LIKE '" + cs + "'";
					}
					prequery = prequery.replaceFirst(" OR", "");

					String queryprotspecies = "null";
					String querysubspecies = "null";

					if (proteasespecies.equals("Human")) {
						queryprotspecies = "P_Species = 'Human'";
					} else if (proteasespecies.equals("Mouse")) {
						queryprotspecies = "P_Species = 'Mouse'";
					} else if (proteasespecies.equals("Rat")) {
						queryprotspecies = "P_Species = 'Rat'";
					} else {
						queryprotspecies = "";
					}

					if (substratespecies.equals("Human")) {
						querysubspecies = "AND S_Species = 'Human' AND (";
					} else if (substratespecies.equals("Mouse")) {
						querysubspecies = "AND S_Species = 'Mouse' AND (";
					} else if (substratespecies.equals("Rat")) {
						querysubspecies = "AND S_Species = 'Rat' AND (";
					} else {
						querysubspecies = "(";
					}

					queryCleavagesite = "SELECT * FROM CLEAVAGESITE WHERE "
							+ queryprotspecies + querysubspecies + prequery
							+ ")  ORDER BY P_Symbol";
					System.out.println(queryCleavagesite);

					csSubstrateSequence = cTerm;
					nOrC = "CTerm";

					CsJava_1[] csjavaC = getCsMISMATCHinSql(queryCleavagesite,
							pepSubstrateId, csSubstrateSequence, nOrC, pepNumber, pepsequence);
					rsSizeC = csjavaC.length;
					System.out.println(rsSizeC + "apres");
					//
					if (rsSizeC > 0) {
						kIntC = rsSizeC + kIntN;
					} else {
						kIntC = kIntN;
					}
					// size the
					// array
					intermediatecapacityarrayCterm = new QueryOutput[kIntC];
					System.arraycopy(intermediatecapacityarrayNterm, 0,
							intermediatecapacityarrayCterm, 0, kIntN);

					if (rsSizeC > 0) {
						int i = kIntN;
						for (int l = 0; l < csjavaC.length; l++) {
							populateCsPERFECTSql(
									intermediatecapacityarrayCterm, substrate,
									csjavaC[l], i, l, pepSubstrateId,
									pepStart, pepEnd, pepNumber, gaps, pepsequence);
							i++;
							System.out.println("OK");
						}
					}
				} else {
					kIntC = kIntN;
					intermediatecapacityarrayCterm = new QueryOutput[kIntC];
					System.arraycopy(intermediatecapacityarrayCterm, 0,
							intermediatecapacityarrayCterm, 0, kIntN);
				}
					lastcapacityarray = intermediatecapacityarrayCterm;
					kLast = lastcapacityarray.length;
					System.out.println(lastcapacityarray.length + "ROULEMENT DE TAMBOUR");
					
					
			} else if (mismatch == 1) {
				int gaps = 1;

				// check CS in SQL 1 MM N term
				if (!nTerm.contains("----")) {
					for (int j = 0; j < numismN1; j++) {
						String cs = mismN1[j].getCS_Pattern();
						prequery = prequery
								+ " OR CleavageSite_Sequence LIKE '" + cs + "'";
					}
					prequery = prequery.replaceFirst(" OR", "");

					String queryprotspecies = "null";
					String querysubspecies = "null";

					if (proteasespecies.equals("Human")) {
						queryprotspecies = "P_Species = 'Human'";
					} else if (proteasespecies.equals("Mouse")) {
						queryprotspecies = "P_Species = 'Mouse'";
					} else if (proteasespecies.equals("Rat")) {
						queryprotspecies = "P_Species = 'Rat'";
					} else {
						queryprotspecies = "";
					}

					if (substratespecies.equals("Human")) {
						querysubspecies = "AND S_Species = 'Human' AND (";
					} else if (substratespecies.equals("Mouse")) {
						querysubspecies = "AND S_Species = 'Mouse' AND (";
					} else if (substratespecies.equals("Rat")) {
						querysubspecies = "AND S_Species = 'Rat' AND (";
					} else {
						querysubspecies = "(";
					}

					queryCleavagesite = "SELECT * FROM CLEAVAGESITE WHERE "
							+ queryprotspecies + querysubspecies + prequery
							+ ")  ORDER BY P_Symbol";
					System.out.println(queryCleavagesite);

					csSubstrateSequence = nTerm;
					nOrC = "NTerm";

					CsJava_1[] csjava = getCsMISMATCHinSql(queryCleavagesite,
							pepSubstrateId, csSubstrateSequence, nOrC, pepNumber, pepsequence);
					rsSize = csjava.length;
					System.out.println(rsSize + "apres");
					//
					if (rsSize > 0) {
						kIntN = rsSize + k;

					} else {
						kIntN = k;

					}
					// size the
					// array
					intermediatecapacityarrayNterm = new QueryOutput[kIntN];
					
					System.arraycopy(lastcapacityarray, 0,
							intermediatecapacityarrayNterm, 0, k);

					if (rsSize > 0) {
						int i = k;
						for (int l = 0; l < csjava.length; l++) {
							populateCsPERFECTSql(
									intermediatecapacityarrayNterm, substrate,
									csjava[l], i, l, pepSubstrateId,
									pepStart, pepEnd, pepNumber, gaps, pepsequence);
							i++;
						}
					}
				} else {
					kIntN = k;
					intermediatecapacityarrayNterm = new QueryOutput[kIntN];
					System.arraycopy(lastcapacityarray, 0,
							intermediatecapacityarrayNterm, 0, k);
				}
				
				// check CS in SQL 1 MM C term
				prequery = "";
				if (!cTerm.contains("----")) {
					for (int j = 0; j < numismC1; j++) {
						String cs = mismC1[j].getCS_Pattern();
						prequery = prequery
								+ " OR CleavageSite_Sequence LIKE '" + cs + "'";
					}
					prequery = prequery.replaceFirst(" OR", "");

					String queryprotspecies = "null";
					String querysubspecies = "null";

					if (proteasespecies.equals("Human")) {
						queryprotspecies = "P_Species = 'Human'";
					} else if (proteasespecies.equals("Mouse")) {
						queryprotspecies = "P_Species = 'Mouse'";
					} else if (proteasespecies.equals("Rat")) {
						queryprotspecies = "P_Species = 'Rat'";
					} else {
						queryprotspecies = "";
					}

					if (substratespecies.equals("Human")) {
						querysubspecies = "AND S_Species = 'Human' AND (";
					} else if (substratespecies.equals("Mouse")) {
						querysubspecies = "AND S_Species = 'Mouse' AND (";
					} else if (substratespecies.equals("Rat")) {
						querysubspecies = "AND S_Species = 'Rat' AND (";
					} else {
						querysubspecies = "(";
					}

					queryCleavagesite = "SELECT * FROM CLEAVAGESITE WHERE "
							+ queryprotspecies + querysubspecies + prequery
							+ ")  ORDER BY P_Symbol";
					System.out.println(queryCleavagesite);

					csSubstrateSequence = cTerm;
					nOrC = "CTerm";

					CsJava_1[] csjavaC = getCsMISMATCHinSql(queryCleavagesite,
							pepSubstrateId, csSubstrateSequence, nOrC, pepNumber, pepsequence);
					rsSizeC = csjavaC.length;
					System.out.println(rsSizeC + "apres");
					//
					if (rsSizeC > 0) {
						kIntC = rsSizeC + kIntN;
					} else {
						kIntC = kIntN;
					}
					// size the
					// array
					intermediatecapacityarrayCterm = new QueryOutput[kIntC];
					System.arraycopy(intermediatecapacityarrayNterm, 0,
							intermediatecapacityarrayCterm, 0, kIntN);

					if (rsSizeC > 0) {
						int i = kIntN;
						for (int l = 0; l < csjavaC.length; l++) {
							populateCsPERFECTSql(
									intermediatecapacityarrayCterm, substrate,
									csjavaC[l], i, l, pepSubstrateId,
									pepStart, pepEnd, pepNumber, gaps, pepsequence);
							i++;
							System.out.println("OK");
						}
					}
				} else {
					kIntC = kIntN;
					intermediatecapacityarrayCterm = new QueryOutput[kIntC];
					System.arraycopy(intermediatecapacityarrayCterm, 0,
							intermediatecapacityarrayCterm, 0, kIntN);
				}
					lastcapacityarray = intermediatecapacityarrayCterm;
					kLast = lastcapacityarray.length;
					System.out.println(lastcapacityarray.length + "ROULEMENT DE TAMBOUR");
				
	
			} else if (mismatch == 2) {
				int gaps = 2;

				// check CS in SQL 2 MM N term
				if (!nTerm.contains("----")) {
					for (int j = 0; j < numismN2; j++) {
						String cs = mismN2[j].getCS_Pattern();
						prequery = prequery
								+ " OR CleavageSite_Sequence LIKE '" + cs + "'";
					}
					prequery = prequery.replaceFirst(" OR", "");

					String queryprotspecies = "null";
					String querysubspecies = "null";

					if (proteasespecies.equals("Human")) {
						queryprotspecies = "P_Species = 'Human'";
					} else if (proteasespecies.equals("Mouse")) {
						queryprotspecies = "P_Species = 'Mouse'";
					} else if (proteasespecies.equals("Rat")) {
						queryprotspecies = "P_Species = 'Rat'";
					} else {
						queryprotspecies = "";
					}

					if (substratespecies.equals("Human")) {
						querysubspecies = "AND S_Species = 'Human' AND (";
					} else if (substratespecies.equals("Mouse")) {
						querysubspecies = "AND S_Species = 'Mouse' AND (";
					} else if (substratespecies.equals("Rat")) {
						querysubspecies = "AND S_Species = 'Rat' AND (";
					} else {
						querysubspecies = "(";
					}

					queryCleavagesite = "SELECT * FROM CLEAVAGESITE WHERE "
							+ queryprotspecies + querysubspecies + prequery
							+ ")  ORDER BY P_Symbol";
					System.out.println(queryCleavagesite);

					csSubstrateSequence = nTerm;
					nOrC = "NTerm";

					CsJava_1[] csjava = getCsMISMATCHinSql(queryCleavagesite,
							pepSubstrateId, csSubstrateSequence, nOrC, pepNumber, pepsequence);
					rsSize = csjava.length;
					System.out.println(rsSize + "apres");
					//
					if (rsSize > 0) {
						kIntN = rsSize + k;

					} else {
						kIntN = k;

					}
					// size the
					// array
					intermediatecapacityarrayNterm = new QueryOutput[kIntN];
					System.arraycopy(lastcapacityarray, 0,
							intermediatecapacityarrayNterm, 0, k);

					if (rsSize > 0) {
						int i = k;
						for (int l = 0; l < csjava.length; l++) {
							populateCsPERFECTSql(
									intermediatecapacityarrayNterm, substrate,
									csjava[l], i, l, pepSubstrateId,
									pepStart, pepEnd, pepNumber, gaps, pepsequence);
							i++;
						}
					}
				} else {
					kIntN = k ;
					intermediatecapacityarrayNterm = new QueryOutput[kIntN];
					System.arraycopy(lastcapacityarray, 0,
							intermediatecapacityarrayNterm, 0, k);
				
				}
				// check CS in SQL 2 MM C term
				prequery = "";
				if (!cTerm.contains("----")) {
					for (int j = 0; j < numismC2; j++) {
						String cs = mismC2[j].getCS_Pattern();
						prequery = prequery
								+ " OR CleavageSite_Sequence LIKE '" + cs + "'";
					}
					prequery = prequery.replaceFirst(" OR", "");

					String queryprotspecies = "null";
					String querysubspecies = "null";

					if (proteasespecies.equals("Human")) {
						queryprotspecies = "P_Species = 'Human'";
					} else if (proteasespecies.equals("Mouse")) {
						queryprotspecies = "P_Species = 'Mouse'";
					} else if (proteasespecies.equals("Rat")) {
						queryprotspecies = "P_Species = 'Rat'";
					} else {
						queryprotspecies = "";
					}

					if (substratespecies.equals("Human")) {
						querysubspecies = "AND S_Species = 'Human' AND (";
					} else if (substratespecies.equals("Mouse")) {
						querysubspecies = "AND S_Species = 'Mouse' AND (";
					} else if (substratespecies.equals("Rat")) {
						querysubspecies = "AND S_Species = 'Rat' AND (";
					} else {
						querysubspecies = "(";
					}

					queryCleavagesite = "SELECT * FROM CLEAVAGESITE WHERE "
							+ queryprotspecies + querysubspecies + prequery
							+ ")  ORDER BY P_Symbol";
					System.out.println(queryCleavagesite);

					csSubstrateSequence = cTerm;
					nOrC = "CTerm";

					CsJava_1[] csjavaC = getCsMISMATCHinSql(queryCleavagesite,
							pepSubstrateId, csSubstrateSequence, nOrC, pepNumber, pepsequence);
					rsSizeC = csjavaC.length;
					System.out.println(rsSizeC + "apres");
					//
					if (rsSizeC > 0) {
						kIntC = rsSizeC + kIntN;
					} else {
						kIntC = kIntN;

					}
					// size the
					// array
					intermediatecapacityarrayCterm = new QueryOutput[kIntC];
					System.arraycopy(intermediatecapacityarrayNterm, 0,
							intermediatecapacityarrayCterm, 0, kIntN);

					if (rsSizeC > 0) {
						int i = kIntN;
						for (int l = 0; l < csjavaC.length; l++) {
							populateCsPERFECTSql(
									intermediatecapacityarrayCterm, substrate,
									csjavaC[l], i, l, pepSubstrateId,
									pepStart, pepEnd, pepNumber, gaps, pepsequence);
							i++;
						}
					}
					
				} else {
					kIntC = kIntN;
					intermediatecapacityarrayCterm = new QueryOutput[kIntC];
					System.arraycopy(intermediatecapacityarrayNterm, 0,
							intermediatecapacityarrayCterm, 0, kIntN);
				}

				lastcapacityarray = intermediatecapacityarrayCterm;
				kLast = lastcapacityarray.length;
				System.out.println(lastcapacityarray.length
						+ "ROULEMENT DE TAMBOUR");
				
				
			} else if (mismatch == 3) {
				int gaps = 3;

				// check CS in SQL 3 MM N term
				if (!nTerm.contains("----")) {
					for (int j = 0; j < numismN3; j++) {
						String cs = mismN3[j].getCS_Pattern();
						prequery = prequery
								+ " OR CleavageSite_Sequence LIKE '" + cs + "'";
					}
					prequery = prequery.replaceFirst(" OR", "");

					String queryprotspecies = "null";
					String querysubspecies = "null";

					if (proteasespecies.equals("Human")) {
						queryprotspecies = "P_Species = 'Human'";
					} else if (proteasespecies.equals("Mouse")) {
						queryprotspecies = "P_Species = 'Mouse'";
					} else if (proteasespecies.equals("Rat")) {
						queryprotspecies = "P_Species = 'Rat'";
					} else {
						queryprotspecies = "";
					}

					if (substratespecies.equals("Human")) {
						querysubspecies = "AND S_Species = 'Human' AND (";
					} else if (substratespecies.equals("Mouse")) {
						querysubspecies = "AND S_Species = 'Mouse' AND (";
					} else if (substratespecies.equals("Rat")) {
						querysubspecies = "AND S_Species = 'Rat' AND (";
					} else {
						querysubspecies = "(";
					}

					queryCleavagesite = "SELECT * FROM CLEAVAGESITE WHERE "
							+ queryprotspecies + querysubspecies + prequery
							+ ")  ORDER BY P_Symbol";
					System.out.println(queryCleavagesite);

					csSubstrateSequence = nTerm;
					nOrC = "NTerm";

					CsJava_1[] csjava = getCsMISMATCHinSql(queryCleavagesite,
							pepSubstrateId, csSubstrateSequence, nOrC, pepNumber, pepsequence);
					rsSize = csjava.length;
					System.out.println(rsSize + "apres");
					//
					if (rsSize > 0) {
						kIntN = rsSize + k;

					} else {
						kIntN = k;

					}
					// size the
					// array
					intermediatecapacityarrayNterm = new QueryOutput[kIntN];
					System.arraycopy(lastcapacityarray, 0,
							intermediatecapacityarrayNterm, 0, k);

					if (rsSize > 0) {
						int i = k;
						for (int l = 0; l < csjava.length; l++) {
							populateCsPERFECTSql(
									intermediatecapacityarrayNterm, substrate,
									csjava[l], i, l, pepSubstrateId,
									pepStart, pepEnd, pepNumber, gaps, pepsequence);
							i++;
						}
					}
					
				} else {
					kIntN = k;
					intermediatecapacityarrayNterm = new QueryOutput[kIntN];
					System.arraycopy(lastcapacityarray, 0,
							intermediatecapacityarrayNterm, 0, k);
				}
				

				// check CS in SQL 3 MM C term
				prequery = "";
				if (!cTerm.contains("----")) {
					for (int j = 0; j < numismC3; j++) {
						String cs = mismC3[j].getCS_Pattern();
						prequery = prequery
								+ " OR CleavageSite_Sequence LIKE '" + cs + "'";
					}
					prequery = prequery.replaceFirst(" OR", "");

					String queryprotspecies = "null";
					String querysubspecies = "null";

					if (proteasespecies.equals("Human")) {
						queryprotspecies = "P_Species = 'Human'";
					} else if (proteasespecies.equals("Mouse")) {
						queryprotspecies = "P_Species = 'Mouse'";
					} else if (proteasespecies.equals("Rat")) {
						queryprotspecies = "P_Species = 'Rat'";
					} else {
						queryprotspecies = "";
					}

					if (substratespecies.equals("Human")) {
						querysubspecies = "AND S_Species = 'Human' AND (";
					} else if (substratespecies.equals("Mouse")) {
						querysubspecies = "AND S_Species = 'Mouse' AND (";
					} else if (substratespecies.equals("Rat")) {
						querysubspecies = "AND S_Species = 'Rat' AND (";
					} else {
						querysubspecies = "(";
					}

					queryCleavagesite = "SELECT * FROM CLEAVAGESITE WHERE "
							+ queryprotspecies + querysubspecies + prequery
							+ ")  ORDER BY P_Symbol";
					System.out.println(queryCleavagesite);

					csSubstrateSequence = cTerm;
					nOrC = "CTerm";

					CsJava_1[] csjavaC = getCsMISMATCHinSql(queryCleavagesite,
							pepSubstrateId, csSubstrateSequence, nOrC, pepNumber, pepsequence);
					rsSizeC = csjavaC.length;
					System.out.println(rsSizeC + "apres");
					//
					if (rsSizeC > 0) {
						kIntC = rsSizeC + kIntN;
					} else {
						kIntC = kIntN;

					}
					// size the
					// array
					intermediatecapacityarrayCterm = new QueryOutput[kIntC];
					System.arraycopy(intermediatecapacityarrayNterm, 0,
							intermediatecapacityarrayCterm, 0, kIntN);

					if (rsSizeC > 0) {
						int i = kIntN;
						for (int l = 0; l < csjavaC.length; l++) {
							populateCsPERFECTSql(
									intermediatecapacityarrayCterm, substrate,
									csjavaC[l], i, l, pepSubstrateId,
									pepStart, pepEnd, pepNumber, gaps, pepsequence);
							i++;
						}
					}
				} else {
					kIntC = kIntN;
					intermediatecapacityarrayCterm = new QueryOutput[kIntC];
					System.arraycopy(intermediatecapacityarrayNterm, 0,
							intermediatecapacityarrayCterm, 0, kIntN);
				}
				
				lastcapacityarray = intermediatecapacityarrayCterm;
				kLast = lastcapacityarray.length;
				System.out.println(lastcapacityarray.length
						+ "ROULEMENT DE TAMBOUR");

			
			}

			k = kLast;

		}
		System.out.println(lastcapacityarray.length);
		// return the array
		return lastcapacityarray;

	}

	private Document checkSubUniprot(String substrateuni) {
		String UniprotURL = "http://www.uniprot.org/uniprot/" + substrateuni
				+ ".xml";
		System.out.println(UniprotURL + "bbbbb");
		ParseUniprotPep parser = new ParseUniprotPep();
		Document xml = null;
		String xmlstring = parser.getXMLasstring(UniprotURL);
		xml = parser.getXML(UniprotURL);
		xml.getXmlVersion();
		return xml;

	}

	private void populateCsPERFECTSql(QueryOutput[] intermediatecapacityarray,
			SubstrateData substrate, CsJava_1 csJava_1,
			int i, int j, String substrateUni, int pepStart, int pepEnd,
			String pepNumber, int gaps, String pepSequence) throws SQLException, Throwable {

		ProteaseData protease = new ProteaseData();
		CleavageSiteData cleavagesite = new CleavageSiteData();
		PeptideData peptide = new PeptideData();
		intermediatecapacityarray[i] = new QueryOutput();

		peptide.Pep_Id = pepNumber;
		peptide.Pep_sequence = pepSequence;
		

		cleavagesite.CS_Externallink = csJava_1.CS_ExternalLink;
		cleavagesite.CS_Pmid = csJava_1.CS_Pmid;
		cleavagesite.CS_OuputMismatch = csJava_1.CS_mismatch;
		cleavagesite.CS_InputCS = csJava_1.CS_InSeq;
		cleavagesite.CS_OutputCS = csJava_1.CS_OutSeq;
		
		String[] splitcleavagein = csJava_1.CS_InSeq.split("");
		
		String one = splitcleavagein[1];
		String two = splitcleavagein[2];
		String three = splitcleavagein[3];
		String four = splitcleavagein[4];
		String five = splitcleavagein[5];
		String six = splitcleavagein[6];
		String seven = splitcleavagein[7];
		String eight = splitcleavagein[8];
	
		if (csJava_1.getCS_NorC().contains("C")) {
			cleavagesite.CS_NorCterm = "CTerm";
			cleavagesite.CS_P1 = pepEnd;
			cleavagesite.CS_P1prime = pepEnd + 1;
		} else if (csJava_1.getCS_NorC().contains("N")) {
			cleavagesite.CS_NorCterm = "NTerm";
			cleavagesite.CS_P1 = pepStart - 1;
			cleavagesite.CS_P1prime = pepStart;
		}
				
		String puni = csJava_1.P_UniprotId;
		protease.P_Uniprotid = puni;

		// CHECK PROTEASE IN SQL
		String queryProtease = "SELECT * FROM PROTEASE WHERE P_UniprotID = ?";
		String outputprotease = getProteaseSql(queryProtease, puni);
		String splitouputprotease[] = outputprotease.split("\n");
		protease.P_NL_Name = splitouputprotease[0];
		protease.P_Symbol = splitouputprotease[1];
		protease.P_Ecnumber = splitouputprotease[2];
		String necessaryP1 = splitouputprotease[4];
		String necessaryP2 = splitouputprotease[5];
		String necessaryP3 = splitouputprotease[6];
		String necessaryP1prime = splitouputprotease[7];
		String necessaryP2prime = splitouputprotease[8];
		String necessaryP3prime = splitouputprotease[9];
		String excludedP1 = splitouputprotease[10];
		String excludedP2 = splitouputprotease[11];
		String excludedP3 = splitouputprotease[12];
		String excludedP1prime = splitouputprotease[13];
		String excludedP2prime = splitouputprotease[14];
		String excludedP3prime = splitouputprotease[15];
		String[] necesP1array = necessaryP1.split(", ");
		String[] necesP2array = necessaryP2.split(", ");
		String[] necesP3array = necessaryP3.split(", ");
		String[] necesP1primearray = necessaryP1prime.split(", ");
		String[] necesP2primearray = necessaryP2prime.split(", ");
		String[] necesP3primearray = necessaryP3prime.split(", ");
		String[] excludP1array = excludedP1.split(", ");
		String[] excludP2array = excludedP2.split(", ");
		String[] excludP3array = excludedP3.split(", ");
		String[] excludP1primearray = excludedP1prime.split(", ");
		String[] excludP2primearray = excludedP2prime.split(", ");
		String[] excludP3primearray = excludedP3prime.split(", ");
		
		String necP3 = "";
		String necP2 = "";
		String necP1 = "";
		String necP3prime = "";
		String necP2prime = "";
		String necP1prime = "";
		String exclP3 = "";
		String exclP2 = "";
		String exclP1 = "";
		String exclP3prime = "";
		String exclP2prime = "";
		String exclP1prime = "";
		
		necP1 = getExpasyNecRestrictions(four, necesP1array, necP1);
		necP2 = getExpasyNecRestrictions(three, necesP2array, necP2);
		necP3 = getExpasyNecRestrictions(two, necesP3array, necP3);
		necP1prime = getExpasyNecRestrictions(five, necesP1primearray, necP1prime);
		necP2prime = getExpasyNecRestrictions(six, necesP2primearray, necP2prime);
		necP3prime = getExpasyNecRestrictions(seven, necesP3primearray, necP3prime);
		exclP1 = getExpasyExclRestrictions(four, excludP1array, exclP1);
		exclP2 = getExpasyExclRestrictions(three, excludP2array, exclP2);
		exclP3 = getExpasyExclRestrictions(two, excludP3array, exclP3);
		exclP1prime = getExpasyExclRestrictions(five, excludP1primearray, exclP1prime);
		exclP2prime = getExpasyExclRestrictions(six, excludP2primearray, exclP2prime);
		exclP3prime = getExpasyExclRestrictions(seven, excludP3primearray, exclP3prime);
			
		if (necP1.contains("OK") && necP2.contains("OK") && necP3.contains("OK") && necP1prime.contains("OK") && necP2prime.contains("OK") && necP3prime.contains("OK")
			&& exclP1.contains("OK") && exclP2.contains("OK") && exclP3.contains("OK") && exclP1prime.contains("OK") && exclP2prime.contains("OK") && exclP3prime.contains("OK")) {
			intermediatecapacityarray[i].setProtease(protease);
			intermediatecapacityarray[i].setSubstrate(substrate);
			intermediatecapacityarray[i].setPeptide(peptide);
			intermediatecapacityarray[i].setCleavagesite(cleavagesite);
		} else {
			peptide.Pep_Id = "clocloforever";
			intermediatecapacityarray[i].setProtease(protease);
			intermediatecapacityarray[i].setSubstrate(substrate);
			intermediatecapacityarray[i].setPeptide(peptide);
			intermediatecapacityarray[i].setCleavagesite(cleavagesite);
			
		}
		
	}

	private String getExpasyNecRestrictions(String aminoacid, String[] Expasyarray,
			String Expasyrestriction) {
		for (String string : Expasyarray) {
			if (string.equals(aminoacid) || string.equals("-")) {
				Expasyrestriction = Expasyrestriction + "OK";
			} 
		}
		return Expasyrestriction;
	}
	
	private String getExpasyExclRestrictions(String aminoacid, String[] Expasyarray,
			String Expasyrestriction) {
		for (String string : Expasyarray) {
			if (!string.equals(aminoacid) || string.equals("-")) {
				Expasyrestriction = Expasyrestriction + "OK";
			} 
		}
		return Expasyrestriction;
	}

}