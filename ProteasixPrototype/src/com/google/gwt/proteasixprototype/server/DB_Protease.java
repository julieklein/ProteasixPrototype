package com.google.gwt.proteasixprototype.server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.proteasixprototype.client.CleavageSiteData;
import com.google.gwt.proteasixprototype.client.Loop;
import com.google.gwt.proteasixprototype.client.XPathNodeUniprot;
import com.google.gwt.proteasixprototype.client.XPathUniprotPep;
import com.google.gwt.proteasixprototype.client.ParseUniprotPep;
import com.google.gwt.proteasixprototype.client.PeptideData;
import com.google.gwt.proteasixprototype.client.SubstrateData;
import com.google.gwt.proteasixprototype.client.ProteaseData;
import com.google.gwt.proteasixprototype.client.Mismatch;
import com.google.gwt.proteasixprototype.client.QueryInput;
import com.google.gwt.proteasixprototype.client.QueryOutput;
import com.sun.source.tree.NewClassTree;

import org.apache.commons.digester.rss.RSSDigester;
import org.apache.tools.ant.taskdefs.Exit;
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
	private LinkedList<QueryInput> queryIn = new LinkedList<QueryInput>();
	private LinkedList<QueryOutput> queryOut = new LinkedList<QueryOutput>();
	private String[] nmatrixall = new String[8];
	private int[] dvalue = new int[8];
	private int[] nmatrixindiv = new int[20];
	private double[] proba = new double[8];
	private String[] aa = { "G", "P", "A", "V", "L", "I", "M", "F", "Y", "W",
			"S", "T", "C", "N", "Q", "D", "E", "K", "R", "H" };
	private String[] incsarray = new String[8];
	private String[][] substitutionmatrix = new String[20][21];
	private String[] possibleposarray = new String[8];

	private boolean sequencevalidity = false;
	private String SPSL = "SPSL";
	private String SS = "SS";
	private String HP = "HP";
	private String P = "P";
	private String mm1 = "1mm";
	private String mm2 = "2mm";
	private String mm3 = "3mm";

	/**
	 * constructor - nothing to do
	 */
	public DB_Protease() {
		// nothing to do
	}

	public QueryOutput[] getResultbySubstrateInfo(QueryInput[] queryFromWebsite)
			throws Throwable {

		// // TODO modify here
		 for (QueryInput queryInput : queryFromWebsite) {
			String searchUni = queryInput.substrate.S_Uniprotid;
			String substratename = null;
			String substratesymbol = null;
			String fullsequence = null;
			String pepsequence = null;
			String cTerm = null;
			String nTerm = null;

			// Retrieve PEPTIDE SEQUENCE IN UNIPROT
			Document xml = checkSubUniprot(searchUni);

			XPathUniprotPep XPather3 = new XPathUniprotPep();
			String xpathQuery3 = "/uniprot/entry/accession/text()";
			NodeList getNodeListbyXPath3 = XPather3.getNodeListByXPath(
					xpathQuery3, xml);
			String accession = "";
			if (getNodeListbyXPath3.getLength() > 0) {
				XPathNodeUniprot XPathNoder3 = new XPathNodeUniprot();
				String xpathQueryNode3 = "/uniprot/entry/accession/text()";
				Loop l1 = new Loop();
				for (int j1 = 0; j1 < getNodeListbyXPath3.getLength(); j1++) {
					NodeList getNodeListByXPathNoder3 = XPathNoder3
							.getNodeListByXPath(xpathQueryNode3,
									getNodeListbyXPath3.item(j1));
					LinkedList<String> stringfromNodelist3 = l1
							.getStringfromNodelist(getNodeListByXPathNoder3);
					accession = stringfromNodelist3.getFirst();
				}
			}

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
					substratename = !(stringfromNodelist4.isEmpty()) ? stringfromNodelist4
							.getFirst() : "---";
				}
			}

			XPathUniprotPep XPather5 = new XPathUniprotPep();
			String xpathQuery5 = "/uniprot/entry/gene/name[@type='primary']/text()";
			NodeList getNodeListbyXPath5 = XPather5.getNodeListByXPath(
					xpathQuery5, xml);

			if (getNodeListbyXPath5.getLength() > 0) {
				XPathNodeUniprot XPathNoder5 = new XPathNodeUniprot();
				String xpathQueryNode5 = "/uniprot/entry/gene/name[@type='primary']/text()";
				Loop l1 = new Loop();
				for (int j1 = 0; j1 < getNodeListbyXPath4.getLength(); j1++) {
					NodeList getNodeListByXPathNoder5 = XPathNoder5
							.getNodeListByXPath(xpathQueryNode5,
									getNodeListbyXPath5.item(j1));
					LinkedList<String> stringfromNodelist5 = l1
							.getStringfromNodelist(getNodeListByXPathNoder5);
					substratesymbol = !(stringfromNodelist5.isEmpty()) ? stringfromNodelist5
							.getFirst() : substratename;

				}
			} else {
				substratesymbol = substratename;
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
					int start = 0;
					int end = 0;
					if (queryInput.peptide.Pep_sequence.equals("")) {
						start = queryInput.peptide.Pep_Start;
						end = queryInput.peptide.Pep_End;
						if (end <= fullsequence.length()) {
						pepsequence = fullsequence.substring(start - 1, end);
						nTerm = retrieveNCs(start, fullsequence);
						cTerm = !queryInput.onlyNtermcheckbox ? retrieveCCs(end, fullsequence) : "";
						sequencevalidity = true;
						} else {
							pepsequence = "";
							sequencevalidity = false;
						}
					} else {
						pepsequence = queryInput.peptide.Pep_sequence
								.toUpperCase().trim();
						sequencevalidity = fullsequence.indexOf(pepsequence) != -1 ? true
								: false;
						if (sequencevalidity) {
							start = fullsequence.indexOf(pepsequence) + 1;
							end = start + pepsequence.length() - 1;
							pepsequence = fullsequence
									.substring(start - 1, end);
							nTerm = retrieveNCs(start, fullsequence);
							cTerm = !queryInput.onlyNtermcheckbox ? retrieveCCs(end, fullsequence) : "";
						} else {
							start = 0;
							end = 0;
							pepsequence = queryInput.peptide.Pep_sequence
									.toUpperCase().trim();
							nTerm = "";
							cTerm = "";
						}
					}
					QueryInput searchRequestN = new QueryInput();
					QueryInput searchRequestC = new QueryInput();
					PeptideData peptide = new PeptideData();
					SubstrateData substrate = new SubstrateData();
					CleavageSiteData csN = new CleavageSiteData();
					CleavageSiteData csC = new CleavageSiteData();
					peptide.Pep_Id = queryInput.peptide.Pep_Id;
					peptide.Pep_Start = start;
					peptide.Pep_End = end;
					peptide.Pep_sequence = pepsequence;
					peptide.Pep_Seqvalidity = sequencevalidity;
					substrate.S_Uniprotid = accession;
					substrate.S_NL_Name = substratename;
					substrate.S_Symbol = substratesymbol;
					csN.CS_InputCS = nTerm;
					csN.CS_NorCterm = "NTerm";
					csC.CS_InputCS = cTerm;
					csC.CS_NorCterm = "CTerm";
					searchRequestN.setCleavagesite(csN);
					searchRequestN.setPeptide(peptide);
					searchRequestN.setSubstrate(substrate);
					queryIn.add(searchRequestN);
					if (!queryInput.onlyNtermcheckbox) {
					searchRequestC.setCleavagesite(csC);
					searchRequestC.setPeptide(peptide);
					searchRequestC.setSubstrate(substrate);		
					queryIn.add(searchRequestC);
					}
				}
			}
		}

		
		int rsSizeA = 0;
		String querySubMatrix = "SELECT * FROM SUBSTITUTIONMATRIX";
		Connection connectionA = getConn();
		Statement sA = connectionA.createStatement();
		try {
			ResultSet resultA = sA.executeQuery(querySubMatrix);
			rsSizeA = getResultSetSize(resultA);
			int i = 0;
			while (resultA.next()) {
				substitutionmatrix[i][0] = resultA.getString("Substituted_aa");
				String observedaa[] = resultA.getString("Observed_aa").split(
						", ");
				for (int k = 1; k < 21; k++) {
					substitutionmatrix[i][k] = observedaa[k - 1];
				}
				i++;
			}
			resultA.close();
			sA.close();
			connectionA.close();
		} catch (Throwable ignore) {
			System.err.println("Mysql Statement Error: " + querySubMatrix);
			ignore.printStackTrace();
		}

		int rsSize = 0;
		String queryProtease = "SELECT * FROM PROTEASE WHERE P_Taxon = 'Human'";
		Connection connection = getConn();
		Statement s = connection.createStatement();
		try {
			ResultSet result = s.executeQuery(queryProtease);
			rsSize = getResultSetSize(result);
			while (result.next()) {
				ProteaseData protease = new ProteaseData();
				protease.P_Symbol = result.getString("P_Symbol");
				protease.P_Uniprotid = result.getString("P_UniprotID");
				protease.P_OMIM = result.getString("Omim");
				protease.P_Ensembl = result.getString("P_EnsemblID");
				for (QueryInput queryInput : queryIn) {
					QueryOutput out = new QueryOutput();
					SubstrateData substrate2 = new SubstrateData();
					CleavageSiteData cs2 = new CleavageSiteData();
					PeptideData peptide2 = new PeptideData();
					substrate2 = queryInput.substrate;
					cs2.CS_NorCterm = queryInput.cleavagesite.CS_NorCterm;
					cs2.CS_InputCS = queryInput.cleavagesite.CS_InputCS;
					cs2.CS_InputCS = queryInput.cleavagesite.CS_InputCS;
					peptide2 = queryInput.peptide;
					if (queryInput.peptide.Pep_Seqvalidity == true) {
					out = process(s, result, protease, queryInput, out,
							substrate2, peptide2, cs2);
					} else {
						peptide2.Pep_Seqvalidity = queryInput.peptide.Pep_Seqvalidity;
						peptide2.Pep_sequence = queryInput.peptide.Pep_sequence;
						peptide2.Pep_Start = queryInput.peptide.Pep_Start;
						peptide2.Pep_End = queryInput.peptide.Pep_End;
						out.setPeptide(peptide2);
						substrate2.S_Symbol = queryInput.substrate.S_Symbol;
						substrate2.S_Uniprotid = queryInput.substrate.S_Uniprotid;
						out.setSubstrate(substrate2);
						out.confidence = "wrongseq";
					}					
					queryOut.add(out);
				}
			}
			result.close();
			s.close();
			connection.close();
		} catch (Throwable ignore) {
			System.err.println("Mysql Statement Error: " + queryProtease);
			ignore.printStackTrace();
		}

		int sizeoutfull = queryOut.size();
		LinkedList<QueryOutput> processedCSOutput = new LinkedList<QueryOutput>();
		for (QueryOutput queryOutput : queryOut) {
			if (!queryOutput.confidence.equals("discarded")) {
				QueryOutput processedOut = new QueryOutput();
				processedOut = queryOutput;
				processedOut.SPSL = queryOutput.confidence.equals(SPSL) ? "A"
						: "B";
				processedOut.SS = queryOutput.confidence.equals(SS) ? "A" : "B";
				processedOut.HP = queryOutput.confidence.equals(HP) ? "A" : "B";
				processedOut.P = queryOutput.confidence.equals(P) ? "A" : "B";
				processedOut.mm1 = queryOutput.confidence.equals(mm1) ? "A"
						: "B";
				processedOut.mm2 = queryOutput.confidence.equals(mm2) ? "A"
						: "B";
				processedOut.mm3 = queryOutput.confidence.equals(mm3) ? "A"
						: "B";
				String htmlcontent1 = getHtmlcontent(
						new URL(
								"http://matrixdb.ibcp.fr/cgi-bin/model/report/default?name=" + queryOutput.substrate.S_Uniprotid + "_" +  queryOutput.protease.P_Uniprotid + "&class=Association"))
						.toString();
				String htmlcontent2 = getHtmlcontent(
						new URL(
								"http://matrixdb.ibcp.fr/cgi-bin/model/report/default?name=" + queryOutput.protease.P_Uniprotid + "_" +  queryOutput.substrate.S_Uniprotid + "&class=Association"))
						.toString();
				if (!htmlcontent1.contains("not found")) {
					processedOut.MatrixDB = "http://matrixdb.ibcp.fr/cgi-bin/model/report/default?name=" + queryOutput.substrate.S_Uniprotid + "_" +  queryOutput.protease.P_Uniprotid + "&class=Association";
				} else if (!htmlcontent2.contains("not found")) {
					processedOut.MatrixDB = "http://matrixdb.ibcp.fr/cgi-bin/model/report/default?name=" + queryOutput.protease.P_Uniprotid + "_" +  queryOutput.substrate.S_Uniprotid + "&class=Association";
				} else {
					processedOut.MatrixDB = "-";
				}
				processedCSOutput.add(processedOut);
			}
		}
		int sizeoutprocessed = processedCSOutput.size();
		QueryOutput[] result = new QueryOutput[sizeoutprocessed];
		processedCSOutput.toArray(result);
		return result;
	}

	private StringBuilder getHtmlcontent(URL u) throws IOException {
		InputStream is = null;
		DataInputStream dis;
		String s = null;
		StringBuilder htmlcontent = new StringBuilder();

		is = u.openStream();
		dis = new DataInputStream(new BufferedInputStream(is));
		while ((s = dis.readLine()) != null) {
			s = s.replaceAll("\\?", "");
			htmlcontent.append(s);
		}
		is.close();
		return htmlcontent;
	}
	
	private QueryOutput process(Statement s, ResultSet result,
			ProteaseData protease, QueryInput queryInput, QueryOutput out,
			SubstrateData substrate, PeptideData peptide, CleavageSiteData cs)
			throws SQLException {

		String extLink = "";
		String pmid = "";
		String substrateUni = substrate.S_Uniprotid;
		String incs = cs.CS_InputCS;
		String norc = cs.CS_NorCterm;
		int end = peptide.Pep_End;
		int start = peptide.Pep_Start;
		int P1 = 0;
		P1 = norc.equals("NTerm") ? start - 1 : end;
		String confidence = null;

		String cssplit[] = incs.split("");
		for (int i = 0; i < 8; i++) {
			incsarray[i] = cssplit[i + 1];
		}

		int m = 0;
		for (String realpos : incsarray) {
			String possiblepos = "";
			for (int k = 0; k < 20; k++) {
				if (realpos.equals(substitutionmatrix[k][0])) {
					for (int j = 1; j < 21; j++) {
						if (!substitutionmatrix[k][j].equals("-")) {
							possiblepos = possiblepos + ", " + aa[j - 1];
						}
					}
				}
			}
			possiblepos = possiblepos.replaceFirst(", ", "");
			possibleposarray[m] = possiblepos;
			m++;
		}

		int rsSize2 = 0;
		String queryCSPerfectSameSubstrate = "SELECT * FROM CLEAVAGESITE WHERE P_UniprotId ='"
				+ protease.P_Uniprotid
				+ "' AND S_UniprotId = '"
				+ substrateUni
				+ "' AND CleavageSite_Sequence = '" + incs + "' AND P1 = " + P1;
		Connection connection2 = getConn();
		Statement s2 = connection2.createStatement();
		try {
			ResultSet result2 = s2.executeQuery(queryCSPerfectSameSubstrate);
			rsSize2 = getResultSetSize(result2);
			if (rsSize2 != 0) {
				return populateEntry(out, protease, queryInput, SPSL, result2,
						extLink, pmid, substrate, peptide, cs);
			}
			result2.close();
			s2.close();
			connection2.close();
		} catch (Throwable ignore) {
			System.err.println("Mysql Statement Error: "
					+ queryCSPerfectSameSubstrate);
			ignore.printStackTrace();
		}
		System.out.println("no SPSL");
		int rsSize3 = 0;
		String queryCSPerfect = "SELECT * FROM CLEAVAGESITE WHERE P_UniprotId ='"
				+ protease.P_Uniprotid
				+ "' AND CleavageSite_Sequence = '"
				+ incs
				+ "' AND NOT (P1 ="
				+ P1
				+ " AND S_UniprotId = '"
				+ substrateUni + "') AND S_Species ='Human'";
		Connection connection3 = getConn();
		Statement s3 = connection3.createStatement();
		try {
			ResultSet result3 = s3.executeQuery(queryCSPerfect);
			rsSize3 = getResultSetSize(result3);
			if (rsSize3 != 0) {
				return populateEntry(out, protease, queryInput, SS, result3,
						extLink, pmid, substrate, peptide, cs);
			}
			result3.close();
			s3.close();
			connection3.close();
		} catch (Throwable ignore) {
			System.err.println("Mysql Statement Error: " + queryCSPerfect);
			ignore.printStackTrace();
		}
		System.out.println("no SS");
		// UNCOMMENT
		// nmatrixall[0] = result.getString("Array_nP4");
		// nmatrixall[1] = result.getString("Array_nP3");
		// nmatrixall[2] = result.getString("Array_nP2");
		// nmatrixall[3] = result.getString("Array_nP1");
		// nmatrixall[4] = result.getString("Array_nP1prime");
		// nmatrixall[5] = result.getString("Array_nP2prime");
		// nmatrixall[6] = result.getString("Array_nP3prime");
		// nmatrixall[7] = result.getString("Array_nP4prime");
		// dvalue[0] = result.getInt("Int_dP4");
		// dvalue[1] = result.getInt("Int_dP3");
		// dvalue[2] = result.getInt("Int_dP2");
		// dvalue[3] = result.getInt("Int_dP1");
		// dvalue[4] = result.getInt("Int_dP1prime");
		// dvalue[5] = result.getInt("Int_dP2prime");
		// dvalue[6] = result.getInt("Int_dP3prime");
		// dvalue[7] = result.getInt("Int_dP4prime");
		// double Q75 = !result.getString("Q75").equals("-") ? Double
		// .parseDouble(result.getString("Q75")) : 0.0;
		// double Q50 = !result.getString("Q50").equals("-") ? Double
		// .parseDouble(result.getString("Q50")) : 0.0;
		// double Q25 = !result.getString("Q50").equals("-") ? Double
		// .parseDouble(result.getString("Q25")) : 0.0;
		//
		// if (!nmatrixall[0].contains("-") && dvalue[0] != 0) {
		// double probatotal = 0;
		// for (int i = 0; i < 8; i++) {
		// String nmatrixallsplit[] = nmatrixall[i].split(", ");
		// for (int k = 0; k < 20; k++) {
		// nmatrixindiv[k] = Integer
		// .parseInt(nmatrixallsplit[k]);
		// }
		// for (int k = 0; k < 20; k++) {
		// if (incsarray[i].equals(aa[k])) {
		// System.out.println(nmatrixindiv[k]);
		// proba[i] = nmatrixindiv[k] != 0 ? (double) (Math
		// .log(nmatrixindiv[k]) - Math
		// .log(dvalue[i])) : 100;
		// } else if (incsarray[i].equals("-")) {
		// proba[i] = (double) Math.log(1);
		// }
		// }
		// probatotal = probatotal + proba[i];
		// }
		// System.out.println(probatotal);
		// if (probatotal >= Q75 && !(probatotal > 0)) {
		// return populateEntryMEROPS(out, protease, queryInput,
		// HP, result, extLink, substrate, peptide, cs);
		// } else if (probatotal >= Q50 && !(probatotal > 0)) {
		// return populateEntryMEROPS(out, protease, queryInput,
		// P, result, extLink, substrate, peptide, cs);
		// } else if (probatotal < Q25 || probatotal > 0) {
		// confidence = "discarded";
		// extLink = result.getString("Merops_Url");
		// return populateEntryEmpty(out, protease, queryInput,
		// confidence, result, extLink, substrate, peptide, cs);
		// } else {
		// System.out.println("no HP ou P");
		// return processWithoutMeropsMatrix(result,
		// protease, queryInput, out,
		// incs, extLink, pmid, Q75,
		// Q50, Q25, substrate, peptide, cs);
		// }
		// } else {
		// System.out.println("no MEROPS matrix");
		// return processWithoutMeropsMatrix(result,
		// protease, queryInput, out,
		// incs, extLink, pmid, Q75,
		// Q50, Q25, substrate, peptide, cs);
		// }
		// COMMENT
		return processWithoutMeropsMatrix(result, protease, queryInput, out,
				incs, extLink, pmid, substrate, peptide, cs);
	}

	private QueryOutput processWithoutMeropsMatrix(ResultSet result,
			ProteaseData protease, QueryInput queryInput, QueryOutput out,
			String incs, String extLink, String pmid, SubstrateData substrate,
			PeptideData peptide, CleavageSiteData cs) throws SQLException {
		String confidence;

		String necessaryP1 = result.getString("Necessary_P1");
		String necessaryP2 = result.getString("Necessary_P2");
		String necessaryP3 = result.getString("Necessary_P3");
		String necessaryP1prime = result.getString("Necessary_P1prime");
		String necessaryP2prime = result.getString("Necessary_P2prime");
		String necessaryP3prime = result.getString("Necessary_P3prime");
		String excludedP1 = result.getString("Excluded_P1");
		String excludedP2 = result.getString("Excluded_P2");
		String excludedP3 = result.getString("Excluded_P3");
		String excludedP1prime = result.getString("Excluded_P1prime");
		String excludedP2prime = result.getString("Excluded_P2prime");
		String excludedP3prime = result.getString("Excluded_P3prime");
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

		boolean necP1 = getExpasyNecRestrictions(incsarray[3], necesP1array);
		boolean necP2 = getExpasyNecRestrictions(incsarray[2], necesP2array);
		boolean necP3 = getExpasyNecRestrictions(incsarray[1], necesP3array);
		boolean necP1prime = getExpasyNecRestrictions(incsarray[4],
				necesP1primearray);
		boolean necP2prime = getExpasyNecRestrictions(incsarray[5],
				necesP2primearray);
		boolean necP3prime = getExpasyNecRestrictions(incsarray[6],
				necesP3primearray);

		boolean exclP1 = getExpasyExclRestrictions(incsarray[3], excludP1array);
		boolean exclP2 = getExpasyExclRestrictions(incsarray[2], excludP2array);
		boolean exclP3 = getExpasyExclRestrictions(incsarray[1], excludP3array);
		boolean exclP1prime = getExpasyExclRestrictions(incsarray[4],
				excludP1primearray);
		boolean exclP2prime = getExpasyExclRestrictions(incsarray[5],
				excludP2primearray);
		boolean exclP3prime = getExpasyExclRestrictions(incsarray[6],
				excludP3primearray);

		boolean validity = (necP1 && necP2 && necP3 && necP2prime && necP3prime
				&& necP1prime && exclP1 && exclP2 && exclP3 && exclP1prime
				&& exclP2prime && exclP3prime) ? true : false;
		if (!validity) {
			System.out.println("no Expasy valid");
			confidence = "discarded";
			extLink = "-";
			return populateEntryEmpty(out, protease, queryInput, confidence,
					result, extLink, substrate, peptide, cs);
		}

		String possibleP4[] = possibleposarray[0].split(", ");
		String possibleP3[] = possibleposarray[1].split(", ");
		String possibleP2[] = possibleposarray[2].split(", ");
		String possibleP1[] = possibleposarray[3].split(", ");
		String possibleP1p[] = possibleposarray[4].split(", ");
		String possibleP2p[] = possibleposarray[5].split(", ");
		String possibleP3p[] = possibleposarray[6].split(", ");
		String possibleP4p[] = possibleposarray[7].split(", ");

		System.out.println("yes Expasy valid");
		Mismatch mismN3[] = new Mismatch[55];
		int numismN3 = 55;
		for (int i = 0; i < numismN3; i++) {
			mismN3[i] = new Mismatch();
		}

		mismN3[0].setCS_Pattern("_" + "_" + "_" + incsarray[3] + incsarray[4]
				+ incsarray[5] + incsarray[6] + incsarray[7]);
		mismN3[1].setCS_Pattern("_" + "_" + incsarray[2] + "_" + incsarray[4]
				+ incsarray[5] + incsarray[6] + incsarray[7]);
		mismN3[2].setCS_Pattern("_" + "_" + incsarray[2] + incsarray[3] + "_"
				+ incsarray[5] + incsarray[6] + incsarray[7]);
		mismN3[3].setCS_Pattern("_" + "_" + incsarray[2] + incsarray[3]
				+ incsarray[4] + "_" + incsarray[6] + incsarray[7]);
		mismN3[4].setCS_Pattern("_" + "_" + incsarray[2] + incsarray[3]
				+ incsarray[4] + incsarray[5] + "_" + incsarray[7]);
		mismN3[5].setCS_Pattern("_" + "_" + incsarray[2] + incsarray[3]
				+ incsarray[4] + incsarray[5] + incsarray[6] + "_");
		mismN3[6].setCS_Pattern("_" + incsarray[1] + "_" + "_" + incsarray[4]
				+ incsarray[5] + incsarray[6] + incsarray[7]);
		mismN3[7].setCS_Pattern("_" + incsarray[1] + "_" + incsarray[3] + "_"
				+ incsarray[5] + incsarray[6] + incsarray[7]);
		mismN3[8].setCS_Pattern("_" + incsarray[1] + "_" + incsarray[3]
				+ incsarray[4] + "_" + incsarray[6] + incsarray[7]);
		mismN3[9].setCS_Pattern("_" + incsarray[1] + "_" + incsarray[3]
				+ incsarray[4] + incsarray[5] + "_" + incsarray[7]);
		mismN3[10].setCS_Pattern("_" + incsarray[1] + "_" + incsarray[3]
				+ incsarray[4] + incsarray[5] + incsarray[6] + "_");
		mismN3[11].setCS_Pattern("_" + incsarray[1] + incsarray[2] + "_" + "_"
				+ incsarray[5] + incsarray[6] + incsarray[7]);
		mismN3[12].setCS_Pattern("_" + incsarray[1] + incsarray[2] + "_"
				+ incsarray[4] + "_" + incsarray[6] + incsarray[7]);
		mismN3[13].setCS_Pattern("_" + incsarray[1] + incsarray[2] + "_"
				+ incsarray[4] + incsarray[5] + "_" + incsarray[7]);
		mismN3[14].setCS_Pattern("_" + incsarray[1] + incsarray[2] + "_"
				+ incsarray[4] + incsarray[5] + incsarray[6] + "_");
		mismN3[15].setCS_Pattern("_" + incsarray[1] + incsarray[2]
				+ incsarray[3] + "_" + "_" + incsarray[6] + incsarray[7]);
		mismN3[16].setCS_Pattern("_" + incsarray[1] + incsarray[2]
				+ incsarray[3] + "_" + incsarray[5] + "_" + incsarray[7]);
		mismN3[17].setCS_Pattern("_" + incsarray[1] + incsarray[2]
				+ incsarray[3] + "_" + incsarray[5] + incsarray[6] + "_");
		mismN3[18].setCS_Pattern("_" + incsarray[1] + incsarray[2]
				+ incsarray[3] + incsarray[4] + "_" + "_" + incsarray[7]);
		mismN3[19].setCS_Pattern("_" + incsarray[1] + incsarray[2]
				+ incsarray[3] + incsarray[4] + "_" + incsarray[6] + "_");
		mismN3[20].setCS_Pattern("_" + incsarray[1] + incsarray[2]
				+ incsarray[3] + incsarray[4] + incsarray[5] + "_" + "_");
		mismN3[21].setCS_Pattern(incsarray[0] + "_" + "_" + "_" + incsarray[4]
				+ incsarray[5] + incsarray[6] + incsarray[7]);
		mismN3[22].setCS_Pattern(incsarray[0] + "_" + "_" + incsarray[3] + "_"
				+ incsarray[5] + incsarray[6] + incsarray[7]);
		mismN3[23].setCS_Pattern(incsarray[0] + "_" + "_" + incsarray[3]
				+ incsarray[4] + "_" + incsarray[6] + incsarray[7]);
		mismN3[24].setCS_Pattern(incsarray[0] + "_" + "_" + incsarray[3]
				+ incsarray[4] + incsarray[5] + "_" + incsarray[7]);
		mismN3[25].setCS_Pattern(incsarray[0] + "_" + "_" + incsarray[3]
				+ incsarray[4] + incsarray[5] + incsarray[6] + "_");
		mismN3[26].setCS_Pattern(incsarray[0] + "_" + incsarray[2] + "_" + "_"
				+ incsarray[5] + incsarray[6] + incsarray[7]);
		mismN3[27].setCS_Pattern(incsarray[0] + "_" + incsarray[2] + "_"
				+ incsarray[4] + "_" + incsarray[6] + incsarray[7]);
		mismN3[28].setCS_Pattern(incsarray[0] + "_" + incsarray[2] + "_"
				+ incsarray[4] + incsarray[5] + "_" + incsarray[7]);
		mismN3[29].setCS_Pattern(incsarray[0] + "_" + incsarray[2] + "_"
				+ incsarray[4] + incsarray[5] + incsarray[6] + "_");
		mismN3[30].setCS_Pattern(incsarray[0] + "_" + incsarray[2]
				+ incsarray[3] + "_" + "_" + incsarray[6] + incsarray[7]);
		mismN3[31].setCS_Pattern(incsarray[0] + "_" + incsarray[2]
				+ incsarray[3] + "_" + incsarray[5] + "_" + incsarray[7]);
		mismN3[32].setCS_Pattern(incsarray[0] + "_" + incsarray[2]
				+ incsarray[3] + "_" + incsarray[5] + incsarray[6] + "_");
		mismN3[33].setCS_Pattern(incsarray[0] + "_" + incsarray[2]
				+ incsarray[3] + incsarray[4] + "_" + "_" + incsarray[7]);
		mismN3[34].setCS_Pattern(incsarray[0] + "_" + incsarray[2]
				+ incsarray[3] + incsarray[4] + "_" + incsarray[6] + "_");
		mismN3[35].setCS_Pattern(incsarray[0] + "_" + incsarray[2]
				+ incsarray[3] + incsarray[4] + incsarray[5] + "_" + "_");
		mismN3[36].setCS_Pattern(incsarray[0] + incsarray[1] + "_" + "_" + "_"
				+ incsarray[5] + incsarray[6] + incsarray[7]);
		mismN3[37].setCS_Pattern(incsarray[0] + incsarray[1] + "_" + "_"
				+ incsarray[4] + "_" + incsarray[6] + incsarray[7]);
		mismN3[38].setCS_Pattern(incsarray[0] + incsarray[1] + "_" + "_"
				+ incsarray[4] + incsarray[5] + "_" + incsarray[7]);
		mismN3[39].setCS_Pattern(incsarray[0] + incsarray[1] + "_" + "_"
				+ incsarray[4] + incsarray[5] + incsarray[6] + "_");
		mismN3[40].setCS_Pattern(incsarray[0] + incsarray[1] + "_"
				+ incsarray[3] + "_" + "_" + incsarray[6] + incsarray[7]);
		mismN3[41].setCS_Pattern(incsarray[0] + incsarray[1] + "_"
				+ incsarray[3] + "_" + incsarray[5] + "_" + incsarray[7]);
		mismN3[42].setCS_Pattern(incsarray[0] + incsarray[1] + "_"
				+ incsarray[3] + incsarray[4] + "_" + "_" + incsarray[7]);
		mismN3[43].setCS_Pattern(incsarray[0] + incsarray[1] + "_"
				+ incsarray[3] + incsarray[4] + "_" + incsarray[6] + "_");
		mismN3[44].setCS_Pattern(incsarray[0] + incsarray[1] + "_"
				+ incsarray[3] + incsarray[4] + incsarray[5] + "_" + "_");
		mismN3[45].setCS_Pattern(incsarray[0] + incsarray[1] + incsarray[2]
				+ "_" + "_" + "_" + incsarray[6] + incsarray[7]);
		mismN3[46].setCS_Pattern(incsarray[0] + incsarray[1] + incsarray[2]
				+ "_" + "_" + incsarray[5] + "_" + incsarray[7]);
		mismN3[47].setCS_Pattern(incsarray[0] + incsarray[1] + incsarray[2]
				+ "_" + "_" + incsarray[5] + incsarray[6] + "_");
		mismN3[48].setCS_Pattern(incsarray[0] + incsarray[1] + incsarray[2]
				+ "_" + incsarray[4] + "_" + "_" + incsarray[7]);
		mismN3[49].setCS_Pattern(incsarray[0] + incsarray[1] + incsarray[2]
				+ "_" + incsarray[4] + "_" + incsarray[6] + "_");
		mismN3[50].setCS_Pattern(incsarray[0] + incsarray[1] + incsarray[2]
				+ "_" + incsarray[4] + incsarray[5] + "_" + "_");
		mismN3[51].setCS_Pattern(incsarray[0] + incsarray[1] + incsarray[2]
				+ incsarray[3] + "_" + "_" + "_" + incsarray[7]);
		mismN3[52].setCS_Pattern(incsarray[0] + incsarray[1] + incsarray[2]
				+ incsarray[3] + "_" + "_" + incsarray[6] + "_");
		mismN3[53].setCS_Pattern(incsarray[0] + incsarray[1] + incsarray[2]
				+ incsarray[3] + "_" + incsarray[5] + "_" + "_");
		mismN3[54].setCS_Pattern(incsarray[0] + incsarray[1] + incsarray[2]
				+ incsarray[3] + incsarray[4] + "_" + "_" + "_");

		String prequery = "";
		String queryCleavagesite = "";
		// check CS in SQL
		if (!incs.contains("----")) {
			for (int j = 0; j < numismN3; j++) {
				String pattern = mismN3[j].getCS_Pattern();
				prequery = prequery + " OR CleavageSite_Sequence LIKE '"
						+ pattern + "'";
			}
			prequery = prequery.replaceFirst(" OR", "");
			queryCleavagesite = "SELECT * FROM CLEAVAGESITE WHERE P_UniprotId = '"
					+ protease.P_Uniprotid
					+ "' AND S_Species = 'Human' AND ("
					+ prequery + ")";
			Connection connection4 = getConn();
			Statement s4 = connection4.createStatement();
			try {
				ResultSet result4 = s4.executeQuery(queryCleavagesite);
				int rsSize4 = getResultSetSize(result4);
				if (rsSize4 != 0) {
					System.out.println("yes mm");
					List<Integer> gaps = new ArrayList<Integer>();
					String[][] entry = new String[3][rsSize4];
					int k = 0;
					while (result4.next()) {
						String cleavageSite_Sequence = result4
								.getString("CleavageSite_Sequence");

						String found[] = cleavageSite_Sequence.split("");

						// UNCOMMENT
						// boolean validitysubP4 = getValiditysub(possibleP4,
						// found[1], incsarray[0]);
						// boolean validitysubP3 = getValiditysub(possibleP3,
						// found[2], incsarray[1]);
						// boolean validitysubP2 = getValiditysub(possibleP2,
						// found[3], incsarray[2]);
						// boolean validitysubP1 = getValiditysub(possibleP1,
						// found[4], incsarray[3]);
						// boolean validitysubP1p = getValiditysub(possibleP1p,
						// found[5], incsarray[4]);
						// boolean validitysubP2p = getValiditysub(possibleP2p,
						// found[6], incsarray[5]);
						// boolean validitysubP3p = getValiditysub(possibleP3p,
						// found[7], incsarray[6]);
						// boolean validitysubP4p = getValiditysub(possibleP4p,
						// found[8], incsarray[7]);
						// boolean validitytotal = (validitysubP4 &&
						// validitysubP3 && validitysubP2 && validitysubP1 &&
						// validitysubP1p && validitysubP2p && validitysubP3p &&
						// validitysubP4p) ? true : false;
						//
						// if (!validitytotal) {
						// confidence = "discarded";
						// extLink = "-";
						// return populateEntryEmpty(out, protease,
						// queryInput, confidence, result,
						// extLink, substrate, peptide, cs);
						// }
						int j = 0;
						j = getGaps(cleavageSite_Sequence, j);
						System.out.println("j" + j);
						gaps.add(j);
						entry[0][k] = Integer.toString(j);
						entry[1][k] = result4.getString("External_Link");
						entry[2][k] = result4.getString("PMID");
						k++;
					}
					Collections.sort(gaps);
					int min = gaps.get(0);
					System.out.println(min);
					if (min == 1) {
						for (int l = 0; l < rsSize4; l++) {
							if (entry[0][l].equals(Integer.toString(min))) {
								extLink = entry[1][l];
								extLink = extLink.replaceAll("\\[", "");
								extLink = extLink.replaceAll("\\]", "");
								extLink = "; " + extLink;
								pmid = entry[2][l];
								pmid = pmid.replaceAll("\\[", "");
								pmid = pmid.replaceAll("\\]", "");
								pmid = "; " + pmid;
								return populateEntry(out, protease, queryInput,
										mm1, result4, extLink, pmid, substrate,
										peptide, cs);
							}
						}
					} else if (min == 2) {
						for (int l = 0; l < rsSize4; l++) {
							System.out.println("rssize" + rsSize4);
							System.out.println("entry" + entry[0][l]);

							if (entry[0][l].equals(Integer.toString(min))) {
								extLink = entry[1][l];
								extLink = extLink.replaceAll("\\[", "");
								extLink = extLink.replaceAll("\\]", "");
								extLink = "; " + extLink;
								pmid = entry[2][l];
								pmid = pmid.replaceAll("\\[", "");
								pmid = pmid.replaceAll("\\]", "");
								pmid = "; " + pmid;
								return populateEntry(out, protease, queryInput,
										mm2, result4, extLink, pmid, substrate,
										peptide, cs);
							}
						}
					} else if (min == 3) {
						for (int l = 0; l < rsSize4; l++) {
							if (entry[0][l].equals(Integer.toString(min))) {
								extLink = entry[1][l];
								extLink = extLink.replaceAll("\\[", "");
								extLink = extLink.replaceAll("\\]", "");
								extLink = "; " + extLink;
								pmid = entry[2][l];
								pmid = pmid.replaceAll("\\[", "");
								pmid = pmid.replaceAll("\\]", "");
								pmid = "; " + pmid;
								return populateEntry(out, protease, queryInput,
										mm3, result4, extLink, pmid, substrate,
										peptide, cs);
							}
						}
					}
				} else {
					confidence = "discarded";
					extLink = "-";
					return populateEntryEmpty(out, protease, queryInput,
							confidence, result, extLink, substrate, peptide, cs);
				}
				result4.close();
				s4.close();
				connection4.close();
			} catch (Throwable ignore) {
				System.err.println("Mysql Statement Error: "
						+ queryCleavagesite);
				ignore.printStackTrace();
			}
		} else {
			confidence = "discarded";
			extLink = "-";
			return populateEntryEmpty(out, protease, queryInput, confidence,
					result, extLink, substrate, peptide, cs);
		}
		return out;
	}

	private int getGaps(String cleavageSite_Sequence, int j)
			throws SQLException {
		String foundsplit[] = cleavageSite_Sequence.split("");
		for (int i = 0; i < 8; i++) {
			if (!incsarray[i].equals(foundsplit[i + 1]))
				j++;
		}
		return j;
	}

	private boolean getValiditysub(String[] possibleP, String found,
			String queried) {
		if (found.equals(queried))
			return true;
		for (int i = 0; i < possibleP.length; i++) {
			if (found.equals(possibleP[i]))
				return true;
		}
		return false;
	}

	private QueryOutput populateEntry(QueryOutput out, ProteaseData protease,
			QueryInput queryInput, String confidence, ResultSet result2,
			String extLink, String pmid, SubstrateData substrate,
			PeptideData peptide, CleavageSiteData cs) throws SQLException {
		while (result2.next()) {
			pmid = pmid + "; " + result2.getString("PMID");
			extLink = extLink + "; " + result2.getString("External_Link");
		}
		pmid = pmid.replaceFirst("; ", "");
		extLink = extLink.replaceFirst("; ", "");
		cs.CS_Pmid = pmid;
		cs.CS_Externallink = extLink;
		out.setPeptide(peptide);
		out.setSubstrate(substrate);
		out.setCleavagesite(cs);
		out.setProtease(protease);
		out.confidence = confidence;
		result2.close();
		return out;
	}

	private QueryOutput populateEntryMEROPS(QueryOutput out,
			ProteaseData protease, QueryInput queryInput, String confidence,
			ResultSet result, String extLink, SubstrateData substrate,
			PeptideData peptide, CleavageSiteData cs) throws SQLException {
		extLink = result.getString("Merops_Url");
		cs.CS_Pmid = "-";
		cs.CS_Externallink = extLink;
		out.setPeptide(peptide);
		out.setSubstrate(substrate);
		out.setCleavagesite(cs);
		out.setProtease(protease);
		out.confidence = confidence;
		return out;
	}

	private QueryOutput populateEntryEmpty(QueryOutput out,
			ProteaseData protease, QueryInput queryInput, String confidence,
			ResultSet result, String extLink, SubstrateData substrate,
			PeptideData peptide, CleavageSiteData cs) throws SQLException {
		cs.CS_Pmid = "-";
		cs.CS_Externallink = extLink;
		out.setPeptide(peptide);
		out.setSubstrate(substrate);
		out.setCleavagesite(cs);
		out.setProtease(protease);
		out.confidence = confidence;
		return out;
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

	private boolean getExpasyNecRestrictions(String aminoacid,
			String[] Expasyarray) {
		for (String string : Expasyarray) {
			if (string.equals(aminoacid) || string.equals("-")) {
				return true;
			}
		}
		return false;
	}

	private boolean getExpasyExclRestrictions(String aminoacid,
			String[] Expasyarray) {
		for (String string : Expasyarray) {
			if (!string.equals(aminoacid) || string.equals("-")) {
				return true;
			}
		}
		return false;
	}

	private String retrieveCCs(int end, String fullsequence) {
		String cTerm = null;
		// Retrieve C-term CS
		if (!(end + 4 > fullsequence.length())) {
			cTerm = fullsequence.substring(end - 4, end + 4);
		} else if (end + 4 > fullsequence.length()) {
			cTerm = fullsequence.substring(end - 4, fullsequence.length());
			for (int i = fullsequence.length(); i < end + 4; i++) {
				cTerm = cTerm + "-";
			}
		}
		return cTerm;
	}

	private String retrieveNCs(int start, String fullsequence) {
		String nTerm = null;
		// Retrieve N-term CS
		if (!(start - 5 < 0)) {
			nTerm = fullsequence.substring(start - 5, start + 3);
		} else if (start - 5 < 0) {
			nTerm = fullsequence.substring(0, start + 3);
			for (int i = start - 5; i < 0; i++) {
				nTerm = "-" + nTerm;
			}
		}
		return nTerm;
	}

}