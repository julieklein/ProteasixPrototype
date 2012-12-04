package com.google.gwt.proteasixprototype.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import com.google.gwt.proteasixprototype.client.DBConnection;
import com.google.gwt.proteasixprototype.client.ProteaseData;
import com.google.gwt.proteasixprototype.client.PrototypeEntryPoint;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.proteasixprototype.client.QueryInput;
import com.google.gwt.proteasixprototype.client.QueryOutput;

/**
 * server side class for Async calls
 * 
 * 
 * Make sure you add a reference library (external jar in build path) JDBC
 * Connector - You will see I put it in
 * /opt/classpath/mysql-connector-java-5.1.5/mysql-connector-java-5.1.5-bin.jar
 * 
 * @author branflake2267
 * 
 */

public class MySQLConnection extends RemoteServiceServlet implements
		DBConnection {
	/**
	 * constructor
	 */
	public MySQLConnection() {

	}

	private String SPSL = "SPSL";
	private String SS = "SS";
	private String HP = "HP";
	private String P = "P";
	private String mm1 = "1mm";
	private String mm2 = "2mm";
	private String mm3 = "3mm";

	@Override
	public QueryOutput[] getResultInfo(QueryInput[] queryIn) throws Throwable {

		// TODO Auto-generated method stub
		DB_Protease db = new DB_Protease();
		QueryOutput[] queryOut = db.getResultbySubstrateInfo(queryIn);
		return queryOut;
	}

	Properties props = null;

	/*
	 * creates the properties for the mail
	 */
	public void setProperties(String from, String host) {
		props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.from", from);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.allow8bitmime", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.port", "587");

	}

	Session session = null;

	/*
	 * creates a session for the mailer with specific properties
	 */
	public void setSession(final String user, final String pass, String from,
			String host) {
		session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, pass);
			}
		});
	}

	/*
	 * creates a session with the existing properties
	 */
	public void setSession(String user, String pass) {
		setSession(user, pass, props.getProperty("mail.from"),
				props.getProperty("mail.smtp.host"));
	}

	/*
	 * Sends a mail
	 */
	public void sendMail(String to, String subject, String body,
			QueryOutput[] queryCSOut, QueryOutput[] queryProtOut) {

	

		File file = new File("CleavageSiteView.txt");
		try {
			PrintStream attachment = new PrintStream(file);
			populateCSHeaders(attachment);
			for (QueryOutput queryOutput : queryCSOut) {
				populateCSData(attachment, queryOutput);
			}
			attachment.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		File file2 = new File("ProteaseView.txt");
		try {
			PrintStream attachment = new PrintStream(file2);
			populateProtHeaders(attachment);
			for (QueryOutput queryOutput : queryProtOut) {
				populateProtData(attachment, queryOutput);
			}
			attachment.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			// MimeMessage
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom();
			msg.setRecipients(Message.RecipientType.TO, to);
			msg.setSubject(subject);
			msg.setSentDate(new Date());

			// Build your Mulitpart Message
			MimeMultipart mmp = new MimeMultipart("alternative");

			// Create the Text
			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setContent(body, "text/html");
			mmp.addBodyPart(mbp1);

			// Create Attachment
			MimeBodyPart mbp2 = new MimeBodyPart();
			FileDataSource fds = new FileDataSource(file);
			mbp2.setDataHandler(new DataHandler(fds));
			mbp2.setFileName(fds.getName());
			mmp.addBodyPart(mbp2);

			// Create Attachment
			MimeBodyPart mbp3 = new MimeBodyPart();
			FileDataSource fds2 = new FileDataSource(file2);
			mbp3.setDataHandler(new DataHandler(fds2));
			mbp3.setFileName(fds2.getName());
			mmp.addBodyPart(mbp3);

			msg.setContent(mmp);

			// Send The Message
			Transport.send(msg);
			file.delete();
			file2.delete();

			if (debug)
				try {
					msg.writeTo(System.out);
				} catch (IOException e) {
					e.printStackTrace();
				}

		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}


	/*
	 * Debug Message
	 */
	boolean debug = false;

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void runMailer(String to, String subject, String body,
			QueryOutput[] queryCSOut, QueryOutput[] queryProtOut) {

		this.setProperties("proteasix@gmail.com", "smtp.gmail.com");
		this.setSession("proteasix@gmail.com", "proteasix");
		this.sendMail(to, subject, body, queryCSOut, queryProtOut);

	}

	private void populateCSHeaders(PrintStream csvWriter) {
		csvWriter.print("PeptideId");
		csvWriter.print("\t");
		csvWriter.print("Terminus");
		csvWriter.print("\t");
		csvWriter.print("SubstrateSymbol");
		csvWriter.print("\t");
		csvWriter.print("SubstrateUniprotID");
		csvWriter.print("\t");
		csvWriter.print("PeptideStart");
		csvWriter.print("\t");
		csvWriter.print("PeptideEnd");
		csvWriter.print("\t");
		csvWriter.print("PeptideSequence");
		csvWriter.print("\t");
		csvWriter.print("CleavageSite");
		csvWriter.print("\t");
		csvWriter.print("ProteaseSymbol");
		csvWriter.print("\t");
		csvWriter.print("Confidence +++");
		csvWriter.print("\t");
		csvWriter.print("Confidence ++");
		csvWriter.print("\t");
		csvWriter.print("Confidence +");
		csvWriter.print("\t");
		csvWriter.print("Confidence +-");
		csvWriter.print("\t");
		csvWriter.print("Confidence +--");
		csvWriter.print("\t");
		csvWriter.print("External Link");
		csvWriter.print("\t");
		csvWriter.print("Reference");
		csvWriter.print("\n");
	}

	private void populateCSData(PrintStream csvWriter, QueryOutput out) {
		// System.out.println(cleavageSiteDBEntry);

		String sSPSL = "";
		String sSS = "";
		// String sHP = "";
		// String sP = "";
		String smm1 = "";
		String smm2 = "";
		String smm3 = "";
		String id = out.peptide.Pep_Id;
		String norc = out.cleavagesite.CS_NorCterm;
		String substrateName = out.substrate.S_Symbol;
		String substrateUni = out.substrate.S_Uniprotid;
		int pepStart = out.peptide.Pep_Start;
		int pepEnd = out.peptide.Pep_End;
		String pepSequence = out.peptide.Pep_sequence;
		String cs = out.cleavagesite.CS_InputCS;
		String protease = out.protease.P_Symbol;
		if (out.confidence.equals("SPSL"))
			sSPSL = "x";
		if (out.confidence.equals("SS"))
			sSS = "x";
		// if (out.confidence.equals("HP"))
		// sHP = "x";
		// if (out.confidence.equals("P"))
		// sP = "x";
		if (out.confidence.equals("1mm"))
			smm1 = "x";
		if (out.confidence.equals("2mm"))
			smm2 = "x";
		if (out.confidence.equals("3mm"))
			smm3 = "x";

		csvWriter.print(id);
		csvWriter.print("\t");
		csvWriter.print(norc);
		csvWriter.print("\t");
		csvWriter.print(substrateName);
		csvWriter.print("\t");
		csvWriter.print(substrateUni);
		csvWriter.print("\t");
		csvWriter.print(pepStart);
		csvWriter.print("\t");
		csvWriter.print(pepEnd);
		csvWriter.print("\t");
		csvWriter.print(pepSequence);
		csvWriter.print("\t");
		csvWriter.print(cs);
		csvWriter.print("\t");
		csvWriter.print(protease);
		csvWriter.print("\t");
		csvWriter.print(sSPSL);
		csvWriter.print("\t");
		csvWriter.print(sSS);
		// csvWriter.print("\t");
		// csvWriter.print(sHP);
		// csvWriter.print("\t");
		// csvWriter.print(sP);
		csvWriter.print("\t");
		csvWriter.print(smm1);
		csvWriter.print("\t");
		csvWriter.print(smm2);
		csvWriter.print("\t");
		csvWriter.print(smm3);
		csvWriter.print("\t");
		csvWriter.print(out.cleavagesite.CS_Externallink);
		csvWriter.print("\t");
		csvWriter.print(out.cleavagesite.CS_Pmid);
		csvWriter.print("\n");
	}

	private int getTotalProteaseConfidence(int iConfidence,
			String resultConfidence) {
		if (!resultConfidence.equals("")) {
			String resultConfidenceArray[] = resultConfidence.split(", ");
			iConfidence = resultConfidenceArray.length;
		}
		return iConfidence;
	}
	
	private void populateProtData(PrintStream csvWriter, QueryOutput out) {
		// System.out.println(cleavageSiteDBEntry);

		
		String protease = out.protease.P_Symbol;

		csvWriter.print(protease);
		csvWriter.print("\t");
		csvWriter.print(out.protease.totalSPSL);
		csvWriter.print("\t");
		csvWriter.print(out.protease.totalSS);
//		csvWriter.print("\t");
//		csvWriter.print(out.protease.totalHP);
//		csvWriter.print("\t");
//		csvWriter.print(out.protease.totalP);
		csvWriter.print("\t");
		csvWriter.print(out.protease.total1mm);
		csvWriter.print("\t");
		csvWriter.print(out.protease.total2mm);
		csvWriter.print("\t");
		csvWriter.print(out.protease.total3mm);
		csvWriter.print("\t");
		csvWriter.print(out.protease.totalSPSL + out.protease.totalSS + out.protease.total1mm + out.protease.total2mm + out.protease.total3mm);
		csvWriter.print("\t");
		csvWriter.print("http://omim.org/entry/" + out.protease.P_OMIM);
		csvWriter.print("\t");
		csvWriter.print("http://www.ebi.ac.uk/gxa/gene/" + out.protease.P_Ensembl);
		csvWriter.print("\n");
	}
	
	private void populateProtHeaders(PrintStream csvWriter) {
		csvWriter.print("ProteaseSymbol");
		csvWriter.print("\t");
		csvWriter.print("Confidence +++");
		csvWriter.print("\t");
		csvWriter.print("Confidence ++");
		csvWriter.print("\t");
		csvWriter.print("Confidence +");
		csvWriter.print("\t");
		csvWriter.print("Confidence +-");
		csvWriter.print("\t");
		csvWriter.print("Confidence +--");
		csvWriter.print("\t");
		csvWriter.print("Total");
		csvWriter.print("\t");
		csvWriter.print("OMIM");
		csvWriter.print("\t");
		csvWriter.print("Gene Expression Atlas");
		csvWriter.print("\n");
	}
}
