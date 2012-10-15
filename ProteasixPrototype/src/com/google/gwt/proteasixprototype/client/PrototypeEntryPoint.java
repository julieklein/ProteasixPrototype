package com.google.gwt.proteasixprototype.client;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.core.client.*;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.EntryPoint;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import java_cup.parse_action;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosureHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.DisclosurePanelImages;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWidgets.ForIsWidget;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.LoadListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.DecoratedTabBar;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.view.client.ListDataProvider;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.source.tree.NewClassTree;

public class PrototypeEntryPoint implements EntryPoint {

	// rpc init Var
	private DBConnectionAsync callProvider;
	// main widget panel

	private VerticalPanel mainTabPanel = new VerticalPanel();
	private HorizontalPanel searchPanelHor = new HorizontalPanel();
	private HorizontalPanel resultPanel = new HorizontalPanel();
	private VerticalPanel resultTablePanel = new VerticalPanel();
	private VerticalPanel resultBorderPanel = new VerticalPanel();
	private Label result = new Label("     Result View");
	
	private VerticalPanel searchPepIdPanel = new VerticalPanel();
	private TextArea pepIdArea = new TextArea();
	
	private VerticalPanel searchPepUniPanel = new VerticalPanel();
	private TextArea pepUniArea = new TextArea();
	
	private VerticalPanel searchPepStartEndPanel = new VerticalPanel();
	private TextArea pepStartEndArea = new TextArea();
	
	private VerticalPanel mismPanel = new VerticalPanel();
	private ListBox mismList = new ListBox();
	
	private VerticalPanel buttonPanel = new VerticalPanel();
	private Button exampleButton = new Button("Example");
	private Button deleteButton = new Button("Delete");
	private Button searchButton = new Button("Search");
	
	private VerticalPanel explanations = new VerticalPanel();
	
	private VerticalPanel bottompage = new VerticalPanel();
	
	// private Label loading = new Label("loading please wait...");
	private Image loading = new Image();
	private Label lbl = new Label("Please wait...");
	private PopupPanel popup = new PopupPanel();
	private VerticalPanel ploading = new VerticalPanel();
	private Label lResult = new Label("Result View");

	// new css for CSTABLE
		interface PTableResources extends CellTable.Resources {
			@Source({ CellTable.Style.DEFAULT_CSS, "CleavageSiteTable.css" })
			PTableStyle cellTableStyle();
		}

		interface PTableStyle extends CellTable.Style {
		}



	/**
	 * This is the entry point method.
	 */

	public void onModuleLoad() {

		// TODO Auto-generated method stub

		// Set up loading

		// Hook up a load listener, so that we can be informed if the image
		// fails
		// to load.
		loading.addLoadListener(new LoadListener() {
			public void onError(Widget sender) {
				lbl.setText("An error occurred while loading.");
			}

			public void onLoad(Widget sender) {
			}
		});

		
		
//		// Point the image at a real URL.
		ploading.setHeight("400px");
		ploading.setWidth("80%"); 
		ploading.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		ploading.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		ploading.add(new HTML("<div><br /><br /><div>"));
		loading.setUrl("/Images/ajax-loader(1).gif");
		ploading.add(loading);
		ploading.add(lbl);
		lbl.addStyleName("lResult");
		popup.add(ploading);
		popup.setSize("400px", "400px");
		popup.hide();
//

		// Set Up the PROTEINSEARCH WIDGET

		// Set Up the PEPTIDESEARCH Widget
		searchPepIdPanel.add(new HTML(
				"<div align=\"center\">Peptide ID<br />(optionnal)</div>"));
		searchPepIdPanel.add(pepIdArea);
		searchPepIdPanel.addStyleName("pSearchpanel");
		pepIdArea.setHeight("200px");
		pepIdArea.setWidth("200px");

		searchPepUniPanel.add(new HTML(
				"<div align=\"center\">Substrate<br />UniprotID</div>"));
		searchPepUniPanel.add(pepUniArea);
		searchPepUniPanel.addStyleName("pSearchpanel");
		pepUniArea.setHeight("200px");
		pepUniArea.setWidth("200px");

		searchPepStartEndPanel.add(new HTML(
				"<div align=\"center\">Peptide<br />Start-End</div>"));
		searchPepStartEndPanel.add(pepStartEndArea);
		searchPepStartEndPanel.addStyleName("pSearchpanel");
		pepStartEndArea.setHeight("200px");
		pepStartEndArea.setWidth("200px");

		mismPanel
				.add(new HTML(
						"<div align=\"center\">Number of mismatches<br />in cleavage sites</div>"));
		mismList.addItem("0 mismatch");
		mismList.addItem("up to 1 mismatch");
		mismList.addItem("up to 2 mismatches");
		mismList.addItem("up to 3 mismatches");
		mismPanel.add(mismList);
		mismPanel.addStyleName("pSearchpanel");
		
		mismPanel.add(new HTML("<div><br /><br /><div>"));
		mismPanel.add(exampleButton);
		mismPanel.add(new HTML("<div><br /><div>"));
		mismPanel.add(deleteButton);
		mismPanel.add(new HTML("<div><br /><div>"));
		mismPanel.add(searchButton);
		
		searchPanelHor.add(searchPepIdPanel);
		searchPanelHor.add(searchPepUniPanel);
		searchPanelHor.add(searchPepStartEndPanel);
		searchPanelHor.add(mismPanel);
		searchPanelHor.add(buttonPanel);
		explanations.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_JUSTIFY);
		explanations.add(new HTML ("<div><font color=#9FB9A8 size=2><dd>Proteasix is a tool designed to predict proteases involved in peptide generation. Proteasix database contains 3500 entries about human protease/cleavage sites combinations. Information was collected from CutDB, Uniprot and publications.<br /><dd>Each peptide sequence is aligned with the full-length SWISS-PROT sequence to retrieve N-term and C-term cleavage sites (P4P3P2P1-P1'P2'P3'P4', with the scissile bond between the P1 and P1' residues).<br /><dd>All the cleavage sites are processed through the database to return known associated proteases. Proteases exhibit varying binding affinities for amino-acid sequences, ranging from a cleavage that will be strictly restricted to one or few amino-acids in given positions, to generic binding with little discrimination between different amino acids. Such amino-acid restrictions, when described in ENZYME (Expasy) are taken into account.<br /><dd>To balance the high stringency of a prediction based on octapeptides, the search pattern can be relaxed by allowing up to three amino-acid mismatches.</font><div>"));
		searchPanelHor.add(explanations);
		searchPanelHor.addStyleName("pSearchpanel");

//		peptideTab_2.add(search_2);
//
//		peptideTab_2
//				.add(new HTML(
//						"<div><hr style=\"height:8px;;border-width:0;color:#9FB9A8;background-color:#9FB9A8;\"></div>"));
//		pResultPanel_2.addStyleName("pResultPanel");
//		peptideTab_2.add(pResultPanel_2);
//		pResultPanel_2.setWidth("1200px");

		// Set Up the MainPanel
		mainTabPanel.removeStyleName("gwt-TabPanelBottom");
		mainTabPanel.add(searchPanelHor);
		mainTabPanel
		.add(new HTML(
				"<div><hr style=\"height:8px;;border-width:0;color:#9FB9A8;background-color:#9FB9A8;\"></div>"));
		bottompage.add(new HTML(
				"<div><hr style=\"height:8px;;border-width:0;color:#FFFFFF;background-color:#FFFFFF;\"></div>"));
		resultBorderPanel.add(new HTML(
				"<div><hr style=\"height:8px;;border-width:0;color:#FFFFFF;background-color:#FFFFFF;\"></div>"));

		mainTabPanel.setSize("1300px", "200px");
		RootPanel rootPanel = RootPanel.get("protease");
		rootPanel.add(mainTabPanel);
		// // start the process on PEPTIDESEARCH WIDGET

		// Listen for mouse events on the Add button.
		exampleButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// keep search to setup prepared statement
				// init rpc
				pepIdArea.setText("P*1\nP*2\nP*3\nP*4\nP*5");
				pepUniArea.setText("O15021\nP62328\nP02461\nQ86Y22\nP02452");
				pepStartEndArea.setText("1189-1200\n7-18\n537-555\n235-256\n650-672");
				mismList.setItemSelected(3, true);

			}
		});

		// Listen for mouse events on the Add button.
		deleteButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// keep search to setup prepared statement
				// init rpc
				pepIdArea.setText("");
				pepUniArea.setText("");
				pepStartEndArea.setText("");
				mismList.setItemSelected(0, true);

			}
		});

		// Listen for mouse events on the Add button.
		searchButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				popup.show();
				popup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
			          public void setPosition(int offsetWidth, int offsetHeight) {
			            int left = (Window.getClientWidth() - offsetWidth) / 3;
			            int top = (Window.getClientHeight() - offsetHeight) / 3;
			            popup.setPopupPosition(left, top);
			          }
				});
				// keep search to setup prepared statement
				// init rpc
				rpcInit();
				try {
					// TODO modify here
					generateSearchRequest_2();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				 start the process

			}
		});

	}

	/**
	 * Init the RPC provider
	 * 
	 * NOTE: ./public/MySQLConn.gwt.xml - determines the servlet context read
	 * more of my gwtTomcat documentation in http://gwt-examples.googlecode.com
	 */
	private void rpcInit() {
		callProvider = (DBConnectionAsync) GWT.create(DBConnection.class);
		ServiceDefTarget target = (ServiceDefTarget) callProvider;

		// The path 'MySQLConnService' is determined in
		// ./public/MySQLConn.gwt.xml
		// This path directs Tomcat to listen for this context on the server
		// side,
		// thus intercepting the rpc requests.
		String moduleRelativeURL = GWT.getModuleBaseURL() + "MySQLConnection";
		target.setServiceEntryPoint(moduleRelativeURL);
	}

	/**
	 * Add protease to proteaseTable. Executed when the user clicks the
	 * searchButton or presses enter in the searchBox.
	 */

	private void generateSearchRequest_2() throws IOException {

		String subUni = pepUniArea.getText().toUpperCase().trim();
		String splitSearchUni[] = subUni.split("\n");
		LinkedList<String> setUni = new LinkedList<String>();
		for (String searchUni : splitSearchUni) {
			searchUni.toUpperCase().trim();
			setUni.add(searchUni);
		}
		int sizeUni = setUni.size();

		String pepID = pepIdArea.getText().toUpperCase().trim();
		LinkedList<String> setID = null;
		if ((!pepID.equalsIgnoreCase(""))) {
			String splitSearchID[] = pepID.split("\n");
			setID = new LinkedList<String>();
			for (String searchID : splitSearchID) {
				searchID.toUpperCase().trim();
				setID.add(searchID);
			}
		} else {
			setID = new LinkedList<String>();
			for (int i = 0; i < sizeUni; i++) {
				int j = i + 1;
				String searchID = "P*" + j;
				setID.add(searchID);
			}
		}

		String pepStartEnd = pepStartEndArea.getText().toUpperCase().trim()
				.toString();
		String splitSearchStartEnd[] = pepStartEnd.split("\n");
		LinkedList<String> setStartEnd = new LinkedList<String>();
		for (String searchStartEnd : splitSearchStartEnd) {
			searchStartEnd.toUpperCase().trim();
			setStartEnd.add(searchStartEnd);
		}

		int pepMismIndex = mismList.getSelectedIndex();
		String pepMism = mismList.getItemText(pepMismIndex);
		int pepMismNumber = 0;

		if (pepMism.contains("0")) {
			pepMismNumber = 0;
		} else if (pepMism.contains("1")) {
			pepMismNumber = 1;
		} else if (pepMism.contains("2")) {
			pepMismNumber = 2;
		} else if (pepMism.contains("3")) {
			pepMismNumber = 3;
		}

		QueryInput[] queryIn = new QueryInput[sizeUni];
		int i = 0;

		Iterator iteratorID = setID.iterator();
		while (iteratorID.hasNext()) {
			String id = iteratorID.next().toString();
			String valuesplitID[] = id.split("\n");
			for (String string : valuesplitID) {
				queryIn[i] = new QueryInput();
				queryIn[i].setPeptideInId(string);
				queryIn[i].setPeptideInMism(pepMismNumber);
				i++;
			}
		}

		i = 0;
		Iterator iteratorUni = setUni.iterator();
		while (iteratorUni.hasNext()) {
			String uni = iteratorUni.next().toString();
			String valuesplitUni[] = uni.split("\n");
			for (String string : valuesplitUni) {
				queryIn[i].setPeptideInUniprot(string);
				i++;
			}
		}

		i = 0;
		Iterator iteratorStartEnd = setStartEnd.iterator();
		while (iteratorStartEnd.hasNext()) {
			String startend = iteratorStartEnd.next().toString();
			String valuesplitStartEnd[] = startend.split("\n");
			for (String string : valuesplitStartEnd) {
				String splitStartEnd[] = string.split("-");
				int start = Integer.parseInt(splitStartEnd[0]);
				int end = Integer.parseInt(splitStartEnd[1]);
				queryIn[i].setPeptideInStart(start);
				queryIn[i].setPeptideInEnd(end);
				i++;
			}
		}

		getResultbyPeptideInfo_2(queryIn);

	}

	private void getResultbyPeptideInfo_2(QueryInput[] queryIn) {

		// empty if anything is there already
		resultTablePanel.clear();
		mainTabPanel.add(resultPanel);
		

		AsyncCallback callback = new AsyncCallback() {

			// fail
			public void onFailure(Throwable ex) {
				RootPanel.get().add(new HTML(ex.toString()));
			}

			// success
			public void onSuccess(Object result) {

				QueryOutput[] queryOut = (QueryOutput[]) result;

				// TODO modify here
				// draw PEPTIDE info
			
				drawResultbyPeptideInfo_2(queryOut);

			}
		};

		// remote procedure call to the server to get the bible info
		callProvider.getResultInfo(queryIn, callback);
		
		bottompage.setWidth("1300px");
		bottompage.setHeight("100px");
		mainTabPanel.add(bottompage);
	}

	private void drawResultbyPeptideInfo_2(QueryOutput[] queryOut) {

		// if null nothing to do, then exit
		// this will prevent errors from showing up
		if (queryOut == null) {
			return;
		}
		
		resultPanel.add(resultBorderPanel);
		resultTablePanel.add(result);
		result.addStyleName("lResult");
//		pResultPanel_2.add(peptideTabPanel_2);
//		peptideTabPanel_2.add(peptide_cs_2, "Cleavage sites");
//		peptideTabPanel_2.selectTab(0);
		popup.hide();
		
		 
		 
		int numbercs_2 = 0;
		int numberdispep_2 = 0;
		int numbersumm_2 = 0;

		for (QueryOutput queryOut2 : queryOut) {
			numbercs_2++;
		}
		
		QueryOutput[] resultcs_2 = new QueryOutput[numbercs_2];

		int k = 0;
		int l = 0;
		int m = 0;

		for (int i = 0; i < queryOut.length; i++) {
			System.out.println(queryOut[i]);
			resultcs_2[k] = queryOut[i];
			k++;
		}
//
		Map<String, List<Set<String>>> hmap = new HashMap<String, List<Set<String>>>();
		for (int i = 0; i < numbercs_2; i++) {
			String key = resultcs_2[i].peptide.Pep_Id
					+ resultcs_2[i].substrate.S_Symbol
					// + resultcs_2[i].CS_terminus
					+ resultcs_2[i].cleavagesite.CS_InputCS
					+ resultcs_2[i].protease.P_Symbol.toUpperCase();
			if (!(resultcs_2[i].cleavagesite.CS_OutputCS.equals("------")) && !(resultcs_2[i].peptide.Pep_Id.equals("clocloforever"))) {
				if (!hmap.containsKey(key)) {
					List value = new ArrayList<Set<String>>();
					for (int j = 0; j < 12; j++) {
						value.add(new HashSet<String>());
					}
					hmap.put(key, value);
				}
				hmap.get(key).get(0).add(resultcs_2[i].peptide.Pep_Id);
				hmap.get(key).get(1).add(resultcs_2[i].cleavagesite.CS_NorCterm);
				hmap.get(key).get(2).add(resultcs_2[i].substrate.S_Symbol);
				hmap.get(key).get(3).add(resultcs_2[i].peptide.Pep_sequence);
				hmap.get(key).get(4).add(resultcs_2[i].cleavagesite.CS_InputCS);
				hmap.get(key)
						.get(5)
						.add(resultcs_2[i].cleavagesite.CS_OutputCS);
				String mismatch = resultcs_2[i].cleavagesite.CS_OuputMismatch;
				hmap.get(key).get(6).add(mismatch);
				hmap.get(key).get(7)
						.add(resultcs_2[i].protease.P_Symbol.toUpperCase());
				hmap.get(key).get(8).add(resultcs_2[i].cleavagesite.CS_Externallink);
				hmap.get(key).get(9).add(resultcs_2[i].cleavagesite.CS_Pmid);
				hmap.get(key).get(10).add(resultcs_2[i].protease.P_Uniprotid);
				hmap.get(key).get(11).add(resultcs_2[i].substrate.S_Uniprotid);

			}
		}
		
		Iterator iterator = hmap.values().iterator();
		int numbercsSHORT_2 = hmap.size();
		QueryOutput[] resultcsSHORT_2 = new QueryOutput[numbercsSHORT_2];
		int i = 0;
		while (iterator.hasNext()) {
			SubstrateData substrate = new SubstrateData();
			ProteaseData protease = new ProteaseData();
			PeptideData peptide = new PeptideData();
			CleavageSiteData cleavagesite = new CleavageSiteData();
			resultcsSHORT_2[i] = new QueryOutput();
			String values = iterator.next().toString();
			String splitarray[] = values.split("\\], \\[");
			String input = splitarray[0];
			input = input.replaceAll("\\[", "");
			peptide.Pep_Id = input;
			cleavagesite.CS_NorCterm = splitarray[1];
			substrate.S_Symbol = splitarray[2];
			peptide.Pep_sequence = splitarray[3];
			cleavagesite.CS_InputCS = splitarray[4];
			cleavagesite.CS_OutputCS = splitarray[5];
			protease.P_Symbol = splitarray[7];
//			cleavagesite.CS_OuputMismatch = splitarray[6];
			
			String output = "";
			if (splitarray[6].contains(", ")) {
				String splitmultiple[] = splitarray[6]
						.split(", ");
				SingleCSOutput[] singleoutput = new SingleCSOutput[splitmultiple.length+1];
				singleoutput[0] = new SingleCSOutput();
				singleoutput[0].setGap(55);
				int k1 = 1;
				for (int i1 = 0; i1 < splitmultiple.length; i1++) {
				singleoutput[k1] = new SingleCSOutput();
				String sgap = splitmultiple[i1];
				int igap = Integer.parseInt(sgap);
				singleoutput[k1].setGap(igap);
				int gapnow = singleoutput[k1].getGap();
				int gapbefore = singleoutput[k1-1].getGap();
				
				if(gapnow<gapbefore) {
					singleoutput[k1].setGap(igap);
				}else {
					singleoutput[k1].setGap(gapbefore);
				}
									k1++;
			}
				int ioutput = singleoutput[splitmultiple.length].getGap();
				output = Integer.toString(ioutput);
				
			}else {
				output = splitarray[6];
				
			}
			
			cleavagesite.CS_OuputMismatch = output;
			
			String externallink = splitarray[8].toString();
			cleavagesite.CS_Externallink = externallink;
			String pmid = splitarray[9].toString();
			// pmid = pmid.replaceAll("\\]", "");
			// pmid = pmid.replaceFirst(", ", "");
			cleavagesite.CS_Pmid = pmid;
			String puni = splitarray[10].toString();
			String suni = splitarray[11].toString();
			suni = suni.replaceAll("\\]", "");
			protease.P_Uniprotid = puni;
			substrate.S_Uniprotid = suni;
			resultcsSHORT_2[i].setProtease(protease);
			resultcsSHORT_2[i].setSubstrate(substrate);
			resultcsSHORT_2[i].setPeptide(peptide);
			resultcsSHORT_2[i].setCleavagesite(cleavagesite);
			i++;
		}

		List<QueryOutput> resultcslist = Arrays
				.asList(resultcsSHORT_2);
		createCleavageSiteTable_2(resultcsSHORT_2, resultcslist);

//		ploading.setVisible(false);
//
	}
//
//	
	private void createCleavageSiteTable_2(QueryOutput[] resultcs,
			List<QueryOutput> resultcslist) {
//
		int rowscs = resultcs.length;
		System.out.println(rowscs);
		CellTable.Resources ptableresources = GWT.create(PTableResources.class);
		CellTable<QueryOutput> csTable = new CellTable<QueryOutput>(
				rowscs, ptableresources);
		csTable.setWidth("1300px");
//
//		// Create columns
//
		TextColumn<QueryOutput> inputNumberCol = new TextColumn<QueryOutput>() {
			@Override
			public String getValue(QueryOutput resultbySubstrateData) {
				String input = resultbySubstrateData.peptide.Pep_Id + " / "
						+ resultbySubstrateData.cleavagesite.CS_NorCterm;
				return input;
			}
		};

		Column<QueryOutput, SafeHtml> substrateCol = new Column<QueryOutput, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(QueryOutput resultbySubstrateData) {
				String substrate = resultbySubstrateData.substrate.S_Symbol;
				return new SafeHtmlBuilder().appendHtmlConstant(
						"<a href=\"http://www.uniprot.org/uniprot/"
								+ resultbySubstrateData.substrate.S_Uniprotid
								+ "\"target=\"_blank\">" + substrate + "</a>")
						.toSafeHtml();

			}

		};

//		TextColumn<ResultbySubstrateData> substrateSpecies = new TextColumn<ResultbySubstrateData>() {
//			@Override
//			public String getValue(ResultbySubstrateData resultbySubstrateData) {
//				String species = resultbySubstrateData.substrate.S_Taxon;
//				return species;
//			}
//		};

		Column<QueryOutput, SafeHtml> sequenceCol = new Column<QueryOutput, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(QueryOutput resultbySubstrateData) {
				String sequence = null;
				if (resultbySubstrateData.peptide.Pep_sequence.length() > 16) {
					if (resultbySubstrateData.cleavagesite.CS_NorCterm.contains("N")) {
						sequence = "<p><font size=\"1\"><font color= #82a38d><u>"
								+ resultbySubstrateData.peptide.Pep_sequence
										.substring(0, 3)
								+ "</u></font>"
								+ resultbySubstrateData.peptide.Pep_sequence
										.substring(3, 8)
								+ "[...]"
								+ resultbySubstrateData.peptide.Pep_sequence
										.substring(resultbySubstrateData.peptide.Pep_sequence
												.length() - 8) + "</font></p>";
					}
					if (resultbySubstrateData.cleavagesite.CS_NorCterm.contains("C")) {
						int length = resultbySubstrateData.peptide.Pep_sequence
								.length();
						sequence = "<p><font size=\"1\">"
								+ resultbySubstrateData.peptide.Pep_sequence
										.substring(0, 8)
								+ "[...]"
								+ resultbySubstrateData.peptide.Pep_sequence
										.substring(length - 8, length - 3)
								+ "<font color= #82a38d><u>"
								+ resultbySubstrateData.peptide.Pep_sequence
										.substring(length - 3)
								+ "</u></font></font></p>";
					}
				} else {
					if (resultbySubstrateData.cleavagesite.CS_NorCterm.contains("N")) {
						sequence = "<p><font size=\"1\"><font color= #82a38d><u>"
								+ resultbySubstrateData.peptide.Pep_sequence
										.substring(0, 3)
								+ "</u></font>"
								+ resultbySubstrateData.peptide.Pep_sequence
										.substring(3) + "</font></p>";
					}
					if (resultbySubstrateData.cleavagesite.CS_NorCterm.contains("C")) {
						int length = resultbySubstrateData.peptide.Pep_sequence
								.length();
						sequence = "<p><font size=\"1\">"
								+ resultbySubstrateData.peptide.Pep_sequence
										.substring(0, length - 3)
								+ "<font color= #82a38d><u>"
								+ resultbySubstrateData.peptide.Pep_sequence
										.substring(length - 3)
								+ "</u></font></font></p>";
					}

				}
				return new SafeHtmlBuilder().appendHtmlConstant(sequence)
						.toSafeHtml();
			}

		};

		Column<QueryOutput, SafeHtml> searchedCSCol = new Column<QueryOutput, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(QueryOutput resultbySubstrateData) {

				String cleavageSite = resultbySubstrateData.cleavagesite.CS_InputCS;
				String begin = cleavageSite.substring(0, 4);
				String end = cleavageSite.substring(4, 8);
				String cs = null;
				if (resultbySubstrateData.cleavagesite.CS_NorCterm.contains("N")) {
					cs = "<p>" + begin + "\u00A6<font color= #82a38d><u>" + end
							+ "</u></font></p>";
				}
				if (resultbySubstrateData.cleavagesite.CS_NorCterm.contains("C")) {
					cs = "<p><font color= #82a38d><u>" + begin
							+ "</u></font>\u00A6" + end + "</p>";
				}

				return new SafeHtmlBuilder().appendHtmlConstant(cs)
						.toSafeHtml();
			}

		};

//		Column<QueryOutput, SafeHtml> foundCSCol = new Column<QueryOutput, SafeHtml>(
//				new SafeHtmlCell()) {
//			@Override
//			public SafeHtml getValue(QueryOutput resultbySubstrateData) {
//				String splitmultiple[] = resultbySubstrateData.cleavagesite.CS_OutputCS
//						.split(", ");
//				String output = "";
//				SingleCSOutput[] singleoutput = new SingleCSOutput[splitmultiple.length+1];
//				int k = 1;
//				String one = null;
//				String two = null;
//				String three = null;
//				String four = null;
//				String five = null;
//				String six = null;
//				String seven = null;
//				String eight = null;
//				
//				String searched = resultbySubstrateData.cleavagesite.CS_InputCS;
//				String searchedsplit[] = searched.split("");
//				
//				if (resultbySubstrateData.cleavagesite.CS_OutputCS.contains(", ")) {
//				singleoutput[0] = new SingleCSOutput(); 
//				singleoutput[0].setGap(55);
//				singleoutput[0].setCs("xxxxxxx");
//				}
//				for (int i = 0; i < splitmultiple.length; i++) {					
//					String found = splitmultiple[i];				
//					String foundsplit[] = found.split("");
//					
//					if (resultbySubstrateData.cleavagesite.CS_OutputCS.contains(", ")) {
//					singleoutput[k] = new SingleCSOutput(); 
//					}
//					
//					int gap = 0;
//					
//					if (searchedsplit[1].equalsIgnoreCase(foundsplit[1])) {
//						one = searchedsplit[1];
//					} else {
//						one = foundsplit[1].toLowerCase();
//						gap++;
//					}
//					if (searchedsplit[2].equalsIgnoreCase(foundsplit[2])) {
//						two = searchedsplit[2];
//					} else {
//						two = foundsplit[2].toLowerCase();
//						gap++;
//					}
//					if (searchedsplit[3].equalsIgnoreCase(foundsplit[3])) {
//						three = searchedsplit[3];
//					} else {
//						three = foundsplit[3].toLowerCase();
//						gap++;
//					}
//					if (searchedsplit[4].equalsIgnoreCase(foundsplit[4])) {
//						four = searchedsplit[4];
//					} else {
//						four = foundsplit[4].toLowerCase();
//						gap++;
//					}
//					if (searchedsplit[5].equalsIgnoreCase(foundsplit[5])) {
//						five = searchedsplit[5];
//					} else {
//						five = foundsplit[5].toLowerCase();
//						gap++;
//					}
//					if (searchedsplit[6].equalsIgnoreCase(foundsplit[6])) {
//						six = searchedsplit[6];
//					} else {
//						six = foundsplit[6].toLowerCase();
//						gap++;
//					}
//					if (searchedsplit[7].equalsIgnoreCase(foundsplit[7])) {
//						seven = searchedsplit[7];
//					} else {
//						seven = foundsplit[7].toLowerCase();
//						gap++;
//					}
//					if (searchedsplit[8].equalsIgnoreCase(foundsplit[8])) {
//						eight = searchedsplit[8];
//					} else {
//						eight = foundsplit[8].toLowerCase();
//						gap++;
//					}
//					
//					if (resultbySubstrateData.cleavagesite.CS_OutputCS.contains(", ")) {
//					singleoutput[k].setGap(gap);
//					int gapi = singleoutput[k].getGap();
//					int gapimoins = singleoutput[k-1].getGap();
//					
//					if (gapi < gapimoins) {
//						singleoutput[k].setCs(found);
//					} else {
//						String before = singleoutput[k-1].getCs();
//						singleoutput[k].setCs(before);
//					}
//					}
//					k++;
//				
//				}
//			
//				if (resultbySubstrateData.cleavagesite.CS_OutputCS.contains(", ")) {
//				String singleoutputfinal = singleoutput[splitmultiple.length].getCs();
//				String singleoutputfinalsplit[] = singleoutputfinal.split("");
//				
//				if (searchedsplit[1].equals(singleoutputfinalsplit[1])) {
//					one = searchedsplit[1];
//				} else {
//					one = singleoutputfinalsplit[1].toLowerCase();
//				}
//				if (searchedsplit[2].equals(singleoutputfinalsplit[2])) {
//					two = searchedsplit[2];
//				} else {
//					two = singleoutputfinalsplit[2].toLowerCase();
//				}
//				if (searchedsplit[3].equals(singleoutputfinalsplit[3])) {
//					three = searchedsplit[3];
//				} else {
//					three = singleoutputfinalsplit[3].toLowerCase();
//				}
//				if (searchedsplit[4].equals(singleoutputfinalsplit[4])) {
//					four = searchedsplit[4];
//				} else {
//					four = singleoutputfinalsplit[4].toLowerCase();
//				}
//				if (searchedsplit[5].equals(singleoutputfinalsplit[5])) {
//					five = searchedsplit[5];
//				} else {
//					five = singleoutputfinalsplit[5].toLowerCase();
//				}
//				if (searchedsplit[6].equals(singleoutputfinalsplit[6])) {
//					six = searchedsplit[6];
//				} else {
//					six = singleoutputfinalsplit[6].toLowerCase();
//				}
//				if (searchedsplit[7].equals(singleoutputfinalsplit[7])) {
//					seven = searchedsplit[7];
//				} else {
//					seven = singleoutputfinalsplit[7].toLowerCase();
//				}
//				if (searchedsplit[8].equals(singleoutputfinalsplit[8])) {
//					eight = searchedsplit[8];
//				} else {
//					eight = singleoutputfinalsplit[8].toLowerCase();
//				}
//				output = "<p>" + one + two + three + four + ""
//						+ "\u00A6" + five + six + seven + eight + "</p>";
//				} else {
//					output = "<p>" + one + two + three + four + ""
//							+ "\u00A6" + five + six + seven + eight + "</p>";
//				}
//
//				return new SafeHtmlBuilder().appendHtmlConstant(output)
//						.toSafeHtml();
//
//			}
//
//		};
//
		Column<QueryOutput, SafeHtml> proteaseCol = new Column<QueryOutput, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(QueryOutput resultbySubstrateData) {
				String symbol = resultbySubstrateData.protease.P_Symbol
						.toUpperCase();
				return new SafeHtmlBuilder().appendHtmlConstant(
						"<a href=\"http://www.uniprot.org/uniprot/"
								+ resultbySubstrateData.protease.P_Uniprotid
								+ "\"target=\"_blank\">" + symbol + "</a>")
						.toSafeHtml();

			}

		};
//
//		TextColumn<ResultbySubstrateData> proteaseSpecies = new TextColumn<ResultbySubstrateData>() {
//			@Override
//			public String getValue(ResultbySubstrateData resultbySubstrateData) {
//				String species = resultbySubstrateData.protease.P_Taxon;
//				species = species.replaceAll("Human", "H");
//				species = species.replaceAll("Mouse", "M");
//				species = species.replaceAll("Rat", "R");
//
//				return species;
//			}
//		};

		Column<QueryOutput, SafeHtml> mismatchCSCol = new Column<QueryOutput, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(QueryOutput resultbySubstrateData) {
				String output = "";
				if (resultbySubstrateData.cleavagesite.CS_OuputMismatch.contains(", ")) {
					String splitmultiple[] = resultbySubstrateData.cleavagesite.CS_OuputMismatch
							.split(", ");
					SingleCSOutput[] singleoutput = new SingleCSOutput[splitmultiple.length+1];
					singleoutput[0] = new SingleCSOutput();
					singleoutput[0].setGap(55);
					int k = 1;
					for (int i = 0; i < splitmultiple.length; i++) {
					singleoutput[k] = new SingleCSOutput();
					String sgap = splitmultiple[i];
					int igap = Integer.parseInt(sgap);
					singleoutput[k].setGap(igap);
					int gapnow = singleoutput[k].getGap();
					int gapbefore = singleoutput[k-1].getGap();
					
					if(gapnow<gapbefore) {
						singleoutput[k].setGap(igap);
					}else {
						singleoutput[k].setGap(gapbefore);
					}
										k++;
				}
					int ioutput = singleoutput[splitmultiple.length].getGap();
					output = Integer.toString(ioutput);
					
				}else {
					output = resultbySubstrateData.cleavagesite.CS_OuputMismatch;
					
				}
				
				String mismatch = output;
				return new SafeHtmlBuilder().appendHtmlConstant(mismatch)
						.toSafeHtml();

			}

		};

		Column<QueryOutput, SafeHtml> extlinkCol = new Column<QueryOutput, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(QueryOutput resultbySubstrateData) {
				String external = resultbySubstrateData.cleavagesite.CS_Externallink;
				external = external.replaceAll(", ", "; ");
				String externallink = "";
				if (!(external == null)) {
					if (external.contains(";")) {
						String valuesplit[] = external.split(";");
						int i = 0;
						for (String string : valuesplit) {
							valuesplit[i].trim();
							if (valuesplit[i].contains("/show")) {
								String valuesplitsplit[] = valuesplit[i].split("show/");
								String cutdbentry = valuesplitsplit[1];
								externallink = externallink + "; " + 
										"<font size=\"1\"><a href=\"http://cutdb.burnham.org/relation/show/"
										+ cutdbentry
										+ "\"target=\"_blank\">" + cutdbentry + "</a></font>";
							} else if (valuesplit[i].contains("uniprot")) {
								String valuesplitsplit[] = valuesplit[i].split("uniprot/");
								String unientry = valuesplitsplit[1];
								externallink = externallink + "; "
										+ "<font size=\"1\"><a href=\"http://www.uniprot.org/uniprot/"
										+ unientry
										+ "\"target=\"_blank\">" + unientry + "</a></font>";
							}

							i++;
						}
						externallink = externallink.replaceFirst("; ", "");
						
					} else {
						if (external.contains("/show")) {
							String valuesplitsplit[] = external.split("show/");
							String cutdbentry = valuesplitsplit[1];
							externallink =  
									"<font size=\"1\"><a href=\"http://cutdb.burnham.org/relation/show/"
									+ cutdbentry
									+ "\"target=\"_blank\">" + cutdbentry + "</a></font>";
						} else if (external.contains("uniprot")) {
							String valuesplitsplit[] = external.split("uniprot/");
							String unientry = valuesplitsplit[1];
							externallink = "<font size=\"1\"><a href=\"http://www.uniprot.org/uniprot/"
									+ unientry
									+ "\"target=\"_blank\">" + unientry + "</a></font>";
						}
					}
				} else {
					externallink = "";
				}

				return new SafeHtmlBuilder().appendHtmlConstant(externallink)
						.toSafeHtml();

			}

		};

		Column<QueryOutput, SafeHtml> pmidCol = new Column<QueryOutput, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(QueryOutput resultbySubstrateData) {
				String external = resultbySubstrateData.cleavagesite.CS_Pmid;
				external = external.replaceAll(", ", "; ");
				String pmid = "";
				if (!(external == null)) {
					if (external.contains(";")) {
						String valuesplit[] = external.split(";");
						int i = 0;
						for (String string : valuesplit) {
							String pmidentry = string.trim();
							
							pmid =  pmid + "; " +
									"<font size=\"1\"><a href=\"http://www.ncbi.nlm.nih.gov/pubmed/"
									+ pmidentry
									+ "\"target=\"_blank\">" + pmidentry + "</a></font>";
						}
						pmid = pmid.replaceFirst("; ", "");

					} else {
						pmid = "<font size=\"1\"><a href=\"http://www.ncbi.nlm.nih.gov/pubmed/"
								+ external
								+ "\"target=\"_blank\">"
								+ external
								+ "</a></font>";
					}
				} else {
					pmid = "";
				}
				return new SafeHtmlBuilder().appendHtmlConstant(pmid)
						.toSafeHtml();

			}

		};

		inputNumberCol.setSortable(true);
		substrateCol.setSortable(true);
		mismatchCSCol.setSortable(true);
		proteaseCol.setSortable(true);
		searchedCSCol.setSortable(true);
//
		csTable.addColumn(inputNumberCol, "\u25B2ID\u25BC");
		csTable.addColumn(substrateCol, "\u25B2Substrate\u25BC");
//		csTable.addColumn(substrateSpecies, "S.Taxon");
		csTable.addColumn(sequenceCol, "Peptide Sequence");
//		csTable.addColumn(proteaseSpecies, "P.Taxon");
		csTable.addColumn(searchedCSCol, "\u25B2Searched CS\u25BC");
//		csTable.addColumn(foundCSCol, "Found CS");
		csTable.addColumn(mismatchCSCol, "\u25B2Mism.\u25BC.");
		csTable.addColumn(proteaseCol, "\u25B2Protease\u25BC");
		csTable.addColumn(extlinkCol, "External Link");
		csTable.addColumn(pmidCol, "Ref.");
//
		csTable.setColumnWidth(inputNumberCol, 10, Unit.EM);
		csTable.setColumnWidth(substrateCol, 5, Unit.EM);
		csTable.setColumnWidth(sequenceCol, 10, Unit.EM);
		csTable.setColumnWidth(searchedCSCol, 10, Unit.EM);
//		csTable.setColumnWidth(foundCSCol, 15, Unit.EM);
		csTable.setColumnWidth(proteaseCol, 5, Unit.EM);
//		csTable.setColumnWidth(extlinkCol, 5, Unit.EM);
//		csTable.setColumnWidth(pmidCol, 5, Unit.EM);
//
		resultTablePanel.add(csTable);
		resultPanel.add(resultTablePanel);
//
		// Create a data provider.
		ListDataProvider<QueryOutput> csdataProvider = new ListDataProvider<QueryOutput>();

		// Connect the table to the data provider.
		csdataProvider.addDataDisplay(csTable);

		// Add the data to the data provider, which automatically pushes it to
		// the
		// widget
		List<QueryOutput> cslist = csdataProvider.getList();
		for (QueryOutput csresultTable : resultcslist) {
			cslist.add(csresultTable);
		}

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<QueryOutput> inputNumberColSortHandler = new ListHandler<QueryOutput>(
				cslist);
		inputNumberColSortHandler.setComparator(inputNumberCol,
				new Comparator<QueryOutput>() {
					public int compare(QueryOutput o1,
							QueryOutput o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? o1.peptide.Pep_Id
									.compareTo(o2.peptide.Pep_Id) : 1;
						}
						return -1;
					}
				});
		csTable.addColumnSortHandler(inputNumberColSortHandler);

		// We know that the data is sorted alphabetically by default.
		csTable.getColumnSortList().push(inputNumberCol);

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<QueryOutput> substrateColSortHandler = new ListHandler<QueryOutput>(
				cslist);
		substrateColSortHandler.setComparator(substrateCol,
				new Comparator<QueryOutput>() {
					public int compare(QueryOutput o1,
							QueryOutput o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? o1.substrate.S_Symbol
									.compareTo(o2.substrate.S_Symbol) : 1;
						}
						return -1;
					}
				});
		csTable.addColumnSortHandler(substrateColSortHandler);

		// We know that the data is sorted alphabetically by default.
		csTable.getColumnSortList().push(substrateCol);

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<QueryOutput> mismatchColSortHandler = new ListHandler<QueryOutput>(
				cslist);
		mismatchColSortHandler.setComparator(mismatchCSCol,
				new Comparator<QueryOutput>() {
					public int compare(QueryOutput o1,
							QueryOutput o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? o1.cleavagesite.CS_OuputMismatch
									.compareTo(o2.cleavagesite.CS_OuputMismatch) : 1;
						}
						return -1;
					}
				});
		csTable.addColumnSortHandler(mismatchColSortHandler);

		// We know that the data is sorted alphabetically by default.
		csTable.getColumnSortList().push(mismatchCSCol);

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<QueryOutput> proteaseColSortHandler = new ListHandler<QueryOutput>(
				cslist);
		proteaseColSortHandler.setComparator(proteaseCol,
				new Comparator<QueryOutput>() {
					public int compare(QueryOutput o1,
							QueryOutput o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? o1.protease.P_Symbol
									.toUpperCase().compareTo(
											o2.protease.P_Symbol.toUpperCase())
									: 1;
						}
						return -1;
					}
				});
		csTable.addColumnSortHandler(proteaseColSortHandler);

		// We know that the data is sorted alphabetically by default.
		csTable.getColumnSortList().push(proteaseCol);
		
		// Add a ColumnSortEvent.ListHandler to connect sorting to the
				// java.util.List.
				ListHandler<QueryOutput> searchedCSColSortHandler = new ListHandler<QueryOutput>(
						cslist);
				searchedCSColSortHandler.setComparator(searchedCSCol,
						new Comparator<QueryOutput>() {
							public int compare(QueryOutput o1,
									QueryOutput o2) {
								if (o1 == o2) {
									return 0;
								}

								// Compare the symbol columns.
								if (o1 != null) {
									return (o2 != null) ? o1.cleavagesite.CS_InputCS
											.toUpperCase().compareTo(
													o2.cleavagesite.CS_InputCS.toUpperCase())
											: 1;
								}
								return -1;
							}
						});
				csTable.addColumnSortHandler(searchedCSColSortHandler);

				// We know that the data is sorted alphabetically by default.
				csTable.getColumnSortList().push(searchedCSCol);


	}

	
}