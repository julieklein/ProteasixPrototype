package com.google.gwt.proteasixprototype.client;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.core.client.*;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.EntryPoint;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
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
import java.util.concurrent.Semaphore;
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
import com.google.gwt.proteasixprototype.client.CleavageSiteData;
import com.google.gwt.proteasixprototype.client.DBConnection;
import com.google.gwt.proteasixprototype.client.DBConnectionAsync;
import com.google.gwt.proteasixprototype.client.PeptideData;
import com.google.gwt.proteasixprototype.client.ProteaseData;
import com.google.gwt.proteasixprototype.client.SubstrateData;
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
import com.mysql.jdbc.jdbc2.optional.SuspendableXAConnection;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.source.tree.NewClassTree;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.QEncoderStream;

public class PrototypeEntryPoint implements EntryPoint {

	// rpc init Var
	private DBConnectionAsync callProvider;
	// main widget panel

	private String SPSL = "SPSL";
	private String SS = "SS";
	private String HP = "HP";
	private String P = "P";
	private String mm1 = "1mm";
	private String mm2 = "2mm";
	private String mm3 = "3mm";

	private DecoratedTabPanel mainTabPanel = new DecoratedTabPanel();

	private FlowPanel aboutTabPanel = new FlowPanel();
	private VerticalPanel about = new VerticalPanel();

	private FlowPanel searchTabPanel = new FlowPanel();
	private HorizontalPanel searchPanel = new HorizontalPanel();
	private VerticalPanel searchbuttonandemailPanel = new VerticalPanel();
	private HorizontalPanel searchandbuttonPanel = new HorizontalPanel();
	private VerticalPanel searchPepIdPanel = new VerticalPanel();
	private TextArea pepIdArea = new TextArea();

	private VerticalPanel searchPepUniPanel = new VerticalPanel();
	private TextArea pepUniArea = new TextArea();

	private VerticalPanel searchPepStartEndPanel = new VerticalPanel();
	private TextArea pepStartEndArea = new TextArea();

	private HorizontalPanel emailaddressPanel = new HorizontalPanel();
	private MultiWordSuggestOracle oracle;
	private static long forgetMeIn = 1000 * 60 * 60 * 24 * 365 * 30; // 30 years
	private SuggestBox emailadressTextbox;
	private Label emailadressLabel = new Label(
			"Please enter email adress (optional): ");
	private VerticalPanel checkemailaddressPanel = new VerticalPanel();
	private CheckBox sendemailChexbox = new CheckBox(
			"Send result tables as email");
	private CheckBox rememberemailChexbox = new CheckBox(
			"Do not remember/forget my email adress");

	private VerticalPanel buttonPanel = new VerticalPanel();
	private CheckBox boxNTerm = new CheckBox("Only N-term CS");
	private Button exampleButton = new Button("Try the example!");
	private Button deleteButton = new Button("Delete");
	private Button searchButton = new Button("Search");

	private VerticalPanel explanation = new VerticalPanel();

	private VerticalPanel resultPanel = new VerticalPanel();
	private DecoratedTabPanel resultTabPanel = new DecoratedTabPanel();

	private FlowPanel Proteaseresult = new FlowPanel();
	private FlowPanel CSresult = new FlowPanel();
	private FlowPanel Errorresult = new FlowPanel();

	private VerticalPanel ErrorPanel = new VerticalPanel();

	private HorizontalPanel gross = new HorizontalPanel();
	private VerticalPanel gross1 = new VerticalPanel();
	private VerticalPanel gross2 = new VerticalPanel();

	private Label result = new Label("     Result View");

	private VerticalPanel bottompage = new VerticalPanel();

	// private Label loading = new Label("loading please wait...");
	private Image loading = new Image();
	private Label lbl = new Label("Please wait...");
	private DecoratedPopupPanel popup = new DecoratedPopupPanel();
	private VerticalPanel ploading = new VerticalPanel();
	private Label lResult = new Label("Result View");

	private DecoratedPopupPanel popuptoomuch = new DecoratedPopupPanel();
	private VerticalPanel ptoomuch = new VerticalPanel();
	private Label lbltoomuch = new Label(
			"Oups sorry, the search is limited to 100 peptides!");
	private Button closeButton = new Button("Close");
	private Image close = new Image();

	private DecoratedPopupPanel popupnoinput = new DecoratedPopupPanel();
	private VerticalPanel pinput = new VerticalPanel();

	private DecoratedPopupPanel popupemail = new DecoratedPopupPanel();
	private VerticalPanel pemail = new VerticalPanel();

	private Image close1 = new Image();
	private DecoratedPopupPanel popupforgetemail = new DecoratedPopupPanel();
	private VerticalPanel pforgetemail = new VerticalPanel();

	private DecoratedPopupPanel popupseqvalidity = new DecoratedPopupPanel();
	private VerticalPanel pseqvalidity = new VerticalPanel();

	private boolean emailvalidity = false;

	// new css for CSTABLE
	interface PTableResources extends CellTable.Resources {
		@Source({ CellTable.Style.DEFAULT_CSS, "ProteaseTable.css" })
		PTableStyle cellTableStyle();
	}

	// new css for CSTABLE
	interface CSTableResources extends CellTable.Resources {
		@Source({ CellTable.Style.DEFAULT_CSS, "CleavageSiteTable.css" })
		CSTableStyle cellTableStyle();
	}

	interface PTableStyle extends CellTable.Style {
	}

	interface CSTableStyle extends CellTable.Style {
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

		// // Point the image at a real URL.
		ploading.setHeight("400px");
		ploading.setWidth("500px");
		ploading.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		ploading.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		ploading.add(new HTML("<div><br /><br /><div>"));
		loading.setUrl(GWT.getModuleBaseURL() + "Images/ajax-loader.gif");
		ploading.add(loading);
		ploading.add(lbl);
		lbl.addStyleName("lResult");
		popup.add(ploading);
		popup.setSize("400px", "500px");
		popup.hide();

		ptoomuch.setHeight("400px");
		ptoomuch.setWidth("100%");
		close.setUrl(GWT.getModuleBaseURL() + "Images/1353518245_Close.png");
		close.addStyleName("close");
		ptoomuch.add(close);
		ptoomuch.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		ptoomuch.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		lbltoomuch.addStyleName("lResult");
		ptoomuch.add(lbltoomuch);
		popuptoomuch.add(ptoomuch);
		popuptoomuch.setSize("400px", "400px");
		popuptoomuch.hide();
		//
		pemail.setHeight("400px");
		pemail.setWidth("100%");
		pemail.setSize("600px", "400px");
		popupemail.hide();

		pseqvalidity.setHeight("400px");
		pseqvalidity.setWidth("100%");
		popupseqvalidity.setSize("600px", "400px");
		popupseqvalidity.hide();

		pforgetemail.setHeight("200px");
		pforgetemail.setWidth("100%");
		popupforgetemail.setSize("300px", "200px");
		popupforgetemail.hide();

		pinput.setHeight("200px");
		pinput.setWidth("100%");
		popupnoinput.setSize("300px", "200px");
		popupnoinput.hide();

		about.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		about.add(new HTML(
				"<div><font color=#737373 size=2><dd>Proteasix is a tool designed to predict proteases involved in peptide generation. Proteasix database contains 3500 entries about human protease/cleavage sites combinations. Information was collected from CutDB, Uniprot and publications.<br /><dd>Each peptide sequence is aligned with the full-length SWISS-PROT sequence to retrieve N-term and C-term cleavage sites (P4P3P2P1-P1'P2'P3'P4', with the scissile bond between the P1 and P1' residues).<br /><dd>All the cleavage sites are processed through the database to return known associated proteases. Proteases exhibit varying binding affinities for amino-acid sequences, ranging from a cleavage that will be strictly restricted to one or few amino-acids in given positions, to generic binding with little discrimination between different amino acids. Such amino-acid restrictions, when described in ENZYME (Expasy) are taken into account.<br /><dd>To balance the high stringency of a prediction based on octapeptides, the search pattern can be relaxed by allowing up to three amino-acid mismatches (still taking amino-acid restrictions into account).<br /><dd>If you have any question, please contact <a href=\"mailto:proteasix@gmail.com?subject=Proteasix\">us</a>!</font><div>"));
		aboutTabPanel.add(about);

		// Set Up the PEPTIDESEARCH Widget
		searchPepIdPanel.add(new HTML(
				"<div align=\"center\">Peptide ID<br />(optional)</div>"));
		searchPepIdPanel.add(pepIdArea);
		searchPepIdPanel.addStyleName("pSearchpanel");
		pepIdArea.setHeight("200px");
		pepIdArea.setWidth("70px");

		searchPepUniPanel
				.add(new HTML(
						"<div align=\"center\">Substrate<br />Uniprot ID or Name</div>"));
		searchPepUniPanel.add(pepUniArea);
		searchPepUniPanel.addStyleName("pSearchpanel");
		pepUniArea.setHeight("200px");
		pepUniArea.setWidth("150px");

		searchPepStartEndPanel
				.add(new HTML(
						"<div align=\"center\">Peptide<br />Start-End or Sequence</div>"));
		searchPepStartEndPanel.add(pepStartEndArea);
		searchPepStartEndPanel.addStyleName("pSearchpanel");
		pepStartEndArea.setHeight("200px");
		pepStartEndArea.setWidth("410px");
		buttonPanel.setHeight("200px");
		buttonPanel.setWidth("150px");
		buttonPanel.add(new HTML("<div><br /><br /><div>"));
		buttonPanel.add(boxNTerm);
		buttonPanel.add(new HTML("<div><br /><div>"));
		buttonPanel.add(exampleButton);
		buttonPanel.add(new HTML("<div><br /><div>"));
		buttonPanel.add(deleteButton);
		buttonPanel.add(new HTML("<div><br /><div>"));
		buttonPanel.add(searchButton);

		searchandbuttonPanel.add(searchPepIdPanel);
		searchandbuttonPanel.add(searchPepUniPanel);
		searchandbuttonPanel.add(searchPepStartEndPanel);
		searchandbuttonPanel.add(buttonPanel);

		searchbuttonandemailPanel.add(searchandbuttonPanel);
		checkemailaddressPanel.add(sendemailChexbox);
		checkemailaddressPanel.add(rememberemailChexbox);
		oracle = new MultiWordSuggestOracle();
		emailadressTextbox = new SuggestBox(oracle);
		updateSuggestBox();
		emailadressTextbox.addFocusListener(new FocusListener() {
			@Override
			public void onLostFocus(Widget sender) {
			}

			@Override
			public void onFocus(Widget sender) {
				emailadressTextbox.showSuggestionList();
			}
		});
		emailaddressPanel.add(emailadressLabel);
		emailaddressPanel.add(emailadressTextbox);
		emailaddressPanel.add(checkemailaddressPanel);
		emailaddressPanel.addStyleName("pSearchpanel");
		searchbuttonandemailPanel.add(emailaddressPanel);
		searchPanel.add(searchbuttonandemailPanel);

		explanation.setHeight("300px");
		// explanation.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_JUSTIFY);
		explanation
				.add(new HTML(
						"<p><font color=#e6cd78 size = 3><b>Search</b> (limited to 100 peptides): </font><br/>Enter your peptide list or \"Try the example\" and press search. The search might take some time, especially for long lists. If you don't want to wait, enter your email adress and check \"Send result tables as email\". As specified, your results will be sent when ready.<br/><font color=#e6cd78 size = 3><b>Result: </b></font><br/>Proteasix will return a list of predicted protease/cleavage site (CS) combinations, with different levels of confidence."
								+ "<dd><b><font color=#8EEAD5>&#186</font></b> Confidence +++: observed on the <i>same substrate</i>, at the <i>same position</i>."
								+ "<dd><b><font color=#8EEAD5>&#186</font></b> Confidence ++: observed on <i>other substrate(s)</i> or <i>other position(s)</i>."
								+
								// UNCOMMENT
								// "<dd><b><font color=#8EEAD5>&#186</font></b> Confidence ++++: already been shown on the <i>same substrate</i>, at the <i>same position</i>."
								// +
								// "<dd><b><font color=#8EEAD5>&#186</font></b> Confidence +++: already been shown on <i>other substrate(s)</i> or <i>other position(s)</i>."
								// +
								// "<dd><b><font color=#8EEAD5>&#186</font></b> Confidence ++: predicted using MEROPS weight matrix with <i>very high probability</i>."
								// +
								// "<dd><b><font color=#8EEAD5>&#186</font></b> Confidence +: predicted using MEROPS weight matrix with <i>high probability</i>."
								// +
								// "<dd><b><font color=#8EEAD5>&#186</font></b> Confidence +-: already been shown with <i>one amino acid mismatch</i>, respecting amino acid substitution constraints and protease restrictions."
								// +
								// "<dd><b><font color=#8EEAD5>&#186</font></b> Confidence +--: already been shown with <i>two amino acid mismatches</i>, respecting amino acid substitution constraints and protease restrictions."
								// +
								// "<dd><b><font color=#8EEAD5>&#186</font></b> Confidence +---: already been shown with <i>three amino acid mismatches</i>, respecting amino acid substitution constraints and protease restrictions.</p>"));

								// COMMENT
								"<dd><b><font color=#8EEAD5>&#186</font></b> Confidence +: observed with <i>one amino acid mismatch</i>, respecting protease restrictions."
								+ "<dd><b><font color=#8EEAD5>&#186</font></b> Confidence +-: observed with <i>two amino acid mismatches</i>, respecting protease restrictions."
								+ "<dd><b><font color=#8EEAD5>&#186</font></b> Confidence +--: observed with <i>three amino acid mismatches</i>, respecting protease restrictions.</p>"));

		searchPanel.add(explanation);
		searchTabPanel.add(searchPanel);
		// Set Up the MainPanel
		mainTabPanel.removeStyleName("gwt-TabPanelBottom");

		searchTabPanel
				.add(new HTML(
						"<div><hr style=\"height:10px;;border-width:0;color:#ccc;background-color:#ccc;\"></div>"));
		bottompage.setWidth("1300px");
		bottompage.setHeight("100px");
		searchTabPanel.add(bottompage);
		mainTabPanel.add(searchTabPanel, "Search");
		mainTabPanel.add(aboutTabPanel, "About");
		mainTabPanel.selectTab(0);

		bottompage
				.add(new HTML(
						"<div><hr style=\"height:8px;;border-width:0;color:#FFFFFF;background-color:#FFFFFF;\"></div>"));

		mainTabPanel.setSize("1300px", "200px");
		RootPanel rootPanel = RootPanel.get("protease");
		rootPanel.add(mainTabPanel);

		// Listen for mouse events on the Add button.
		exampleButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// keep search to setup prepared statement
				// init rpc
				pepIdArea.setText("P*1\nP*2\nP*3\nP*4");
				pepUniArea.setText("P02458\nCO1A1_HUMAN\nP02452\nOSTP_HUMAN");
				pepStartEndArea
						.setText("VGPSGAPGEDGRPGPPGPQG\n212-230\n517-539\n33-42");
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
				boxNTerm.setValue(false);
			}
		});

		// Listen for mouse events on the Add button.
		searchButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (pepUniArea.getText().equals("")
						|| pepStartEndArea.getText().equals("")) {
					close.setUrl(GWT.getModuleBaseURL()
							+ "Images/1354635243_Close.png");
					close.addStyleName("close");
					pinput.add(close);
					HTML lblniinput = new HTML("Please enter peptides");
					lblniinput.addStyleName("lResult");
					pinput.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					pinput.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
					pinput.add(lblniinput);
					popupnoinput.add(pinput);
					popupnoinput.setAnimationEnabled(true);
					popupnoinput.setGlassEnabled(true);
					popupnoinput.show();
					popupnoinput
							.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
								public void setPosition(int offsetWidth,
										int offsetHeight) {
									int left = (Window.getClientWidth() - offsetWidth) / 3;
									int top = (Window.getClientHeight() - offsetHeight) / 3;
									popupnoinput.setPopupPosition(left, top);
								}
							});
				} else if (sendemailChexbox.getValue() == true
						&& !emailadressTextbox.getText().contains("@")) {
					close1.setUrl(GWT.getModuleBaseURL()
							+ "Images/1354635243_Close.png");
					close1.addStyleName("close");
					pforgetemail.add(close1);
					HTML lblforgetemail = new HTML(
							"Please enter a valid email adress");
					lblforgetemail.addStyleName("lResult");
					pforgetemail
							.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					pforgetemail
							.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
					pforgetemail.add(lblforgetemail);
					popupforgetemail.add(pforgetemail);
					popupforgetemail.setAnimationEnabled(true);
					popupforgetemail.setGlassEnabled(true);
					popupforgetemail.show();
					popupforgetemail
							.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
								public void setPosition(int offsetWidth,
										int offsetHeight) {
									int left = (Window.getClientWidth() - offsetWidth) / 3;
									int top = (Window.getClientHeight() - offsetHeight) / 3;
									popupforgetemail
											.setPopupPosition(left, top);
								}
							});
				} else if (sendemailChexbox.getValue() == true
						&& emailadressTextbox.getText().contains("@")) {
					emailvalidity = true;
					String username = emailadressTextbox.getValue();
					if (username != null && !username.equals("")
							&& !cookieContains(username)
							&& rememberemailChexbox.getValue() == false) {
						addToCookie(username);
						emailadressTextbox.setValue("");
						updateSuggestBox();
					} else if (rememberemailChexbox.getValue() == true) {
						removeFromCookie(username);
						updateSuggestBox();
					}
					close.setUrl(GWT.getModuleBaseURL()
							+ "Images/1354635243_Close.png");
					close.addStyleName("close");
					pemail.add(close);
					HTML lblemail = new HTML(
							"Your results will be sent as soon as possible."
									+ "<br />Thank you for your search!");
					lblemail.addStyleName("lResult");
					pemail.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					pemail.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
					pemail.add(lblemail);
					popupemail.add(pemail);
					popupemail.setAnimationEnabled(true);
					popupemail.setGlassEnabled(true);
					popupemail.show();
					popupemail
							.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
								public void setPosition(int offsetWidth,
										int offsetHeight) {
									int left = (Window.getClientWidth() - offsetWidth) / 3;
									int top = (Window.getClientHeight() - offsetHeight) / 3;
									popupemail.setPopupPosition(left, top);
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
					// start the process
				} else if (sendemailChexbox.getValue() == false) {
					popup.setAnimationEnabled(true);
					popup.setGlassEnabled(true);
					popup.show();
					popup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
						public void setPosition(int offsetWidth,
								int offsetHeight) {
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
					// start the process
				}
			}
		});

		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// keep search to setup prepared statement
				// init rpc
				ptoomuch.clear();
				popuptoomuch.clear();
				popuptoomuch.hide();
				pseqvalidity.clear();
				popupseqvalidity.clear();
				popupseqvalidity.hide();
				pemail.clear();
				popupemail.clear();
				popupemail.hide();
				pinput.clear();
				popupnoinput.clear();
				popupnoinput.hide();
			}
		});

		close.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// keep search to setup prepared statement
				// init rpc
				ptoomuch.clear();
				popuptoomuch.clear();
				popuptoomuch.hide();
				pseqvalidity.clear();
				popupseqvalidity.clear();
				popupseqvalidity.hide();
				pemail.clear();
				popupemail.clear();
				popupemail.hide();
				pinput.clear();
				popupnoinput.clear();
				popupnoinput.hide();
			}
		});

		close1.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// keep search to setup prepared statement
				// init rpc
				pforgetemail.clear();
				popupforgetemail.clear();
				popupforgetemail.hide();
				emailadressTextbox.setFocus(true);
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
		String pepID = pepIdArea.getText().toUpperCase().trim();
		String pepStartEnd = pepStartEndArea.getText().toUpperCase().trim()
				.toString();

		String splitSearchUni[] = subUni.split("\n");
		String splitSearchStartEnd[] = pepStartEnd.split("\n");

		if (splitSearchUni.length > 100) {
			popup.hide();
			popuptoomuch.setAnimationEnabled(true);
			popuptoomuch.setGlassEnabled(true);
			popuptoomuch.show();
			popuptoomuch
					.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
						public void setPosition(int offsetWidth,
								int offsetHeight) {
							int left = (Window.getClientWidth() - offsetWidth) / 3;
							int top = (Window.getClientHeight() - offsetHeight) / 3;
							popuptoomuch.setPopupPosition(left, top);
						}
					});
		} else {

			LinkedList<QueryInput> input = new LinkedList<QueryInput>();

			for (int i = 0; i < splitSearchUni.length; i++) {
				QueryInput in = new QueryInput();
				PeptideData peptide = new PeptideData();
				SubstrateData substrate = new SubstrateData();
				in.onlyNtermcheckbox = boxNTerm.getValue() == true ? true
						: false;
				System.out.println(in.onlyNtermcheckbox);
				substrate.S_Uniprotid = splitSearchUni[i];
				String splitSearchID[] = pepID.split("\n");
				peptide.Pep_Id = !pepID.equals("") ? splitSearchID[i] : "ID"
						+ Integer.toString(i + 1);

				if (splitSearchStartEnd[i].contains("-")) {
					String splitsplitStartEnd[] = splitSearchStartEnd[i]
							.split("-");
					peptide.Pep_Start = Integer.parseInt(splitsplitStartEnd[0]);
					peptide.Pep_End = Integer.parseInt(splitsplitStartEnd[1]);
					peptide.Pep_sequence = "";
				} else if (splitSearchStartEnd[i].contains("\t")) {
					String splitsplitStartEnd[] = splitSearchStartEnd[i]
							.split("\t");
					peptide.Pep_Start = Integer.parseInt(splitsplitStartEnd[0]);
					peptide.Pep_End = Integer.parseInt(splitsplitStartEnd[1]);
					peptide.Pep_sequence = "";
				} else {
					peptide.Pep_sequence = splitSearchStartEnd[i];
				}
				in.setPeptide(peptide);
				in.setSubstrate(substrate);
				input.add(in);
			}
			QueryInput[] queryIn = new QueryInput[splitSearchUni.length];
			input.toArray(queryIn);
			if (sendemailChexbox.getValue() == false) {
				getResultHTML(queryIn);
			} else if (emailvalidity = true) {
				emailvalidity = false;
				getResultbyEmail(queryIn);
			}
		}
	}

	private void getResultHTML(QueryInput[] queryIn) {

		// empty if anything is there already
		Proteaseresult.clear();
		CSresult.clear();
		Errorresult.clear();
		ErrorPanel.clear();
		gross1.clear();
		gross2.clear();
		gross.clear();
		resultTabPanel.clear();
		resultPanel.clear();
		searchTabPanel.remove(bottompage);
		searchTabPanel.add(resultPanel);
		searchTabPanel.add(bottompage);

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
	}

	private void getResultbyEmail(QueryInput[] queryIn) {

		// empty if anything is there already
		Proteaseresult.clear();
		CSresult.clear();
		Errorresult.clear();
		ErrorPanel.clear();
		gross1.clear();
		gross2.clear();
		gross.clear();
		resultTabPanel.clear();
		resultPanel.clear();
		searchTabPanel.remove(bottompage);
		searchTabPanel.add(resultPanel);
		searchTabPanel.add(bottompage);

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
				try {
					sendEmail(queryOut);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		// remote procedure call to the server to get the bible info
		callProvider.getResultInfo(queryIn, callback);
	}

	private void sendEmail(QueryOutput[] queryCSOut) throws IOException {
		System.out.println("hello");

		LinkedList<QueryOutput> processedCSValidityOKList = getCSwithPeptideValidityOK(queryCSOut);
		QueryOutput[] CSOutputOK = new QueryOutput[processedCSValidityOKList
				.size()];
		processedCSValidityOKList.toArray(CSOutputOK);
		
		LinkedList<QueryOutput> processedNONOutput = getPeptidewithValidityNON(queryCSOut);
		QueryOutput[] queryNONOut = new QueryOutput[processedNONOutput.size()];
		processedNONOutput.toArray(queryNONOut);

		LinkedList<QueryOutput> processedProteaseOutput = getProteasewithPeptideValidityOK(CSOutputOK);
		QueryOutput[] queryProtOut = new QueryOutput[processedProteaseOutput
				.size()];
		processedProteaseOutput.toArray(queryProtOut);
		
		String subject = "Proteasix results";
		
		String body = "<p>Dear user, <br />Please find enclosed your results. The ProteaseView file contains a summary of observed or predicted"
				+ " cleavage site associations for each protease. The CleavageSiteView file contains the full list of "
				+ "observed or predicted protease/cleavage site associations.<br />";
		if (queryNONOut.length != 0) {
			body = body + "<br /><font><b>WARNING: some of your peptides could not be used for the search due to inconstitencies with the full-length parental protein:<br /></b></font>";
			String errormsg = "";
			for (QueryOutput queryOutput : queryNONOut) {
				if (queryOutput.peptide.Pep_sequence.equals("")) {
					errormsg = errormsg
							+ "<dd>- "
							+ queryOutput.peptide.Pep_Id
							+ ": the peptide end you specified for this peptide ("
							+ queryOutput.peptide.Pep_End
							+ ") exceeds the full-length of  "
							+ queryOutput.substrate.S_Symbol
							+ " (<a href=\"http://www.uniprot.org/uniprot/"
							+ queryOutput.substrate.S_Uniprotid
							+ "\"target=\"_blank\">"
							+ queryOutput.substrate.S_Uniprotid + "</a>).<br />";
				} else {
					errormsg = errormsg
							+ "<dd>- "
							+ queryOutput.peptide.Pep_Id
							+ ": the sequence you specified for this peptide ("
							+ queryOutput.peptide.Pep_sequence
							+ ") can not be found in "
							+ queryOutput.substrate.S_Symbol
							+ " (<a href=\"http://www.uniprot.org/uniprot/"
							+ queryOutput.substrate.S_Uniprotid
							+ "\"target=\"_blank\">"
							+ queryOutput.substrate.S_Uniprotid + "</a>).<br />";
				}
			}
			body = body + errormsg + "</dd>";
		}
		
		body = body + "<br />Enjoy your results!<br /><br/>The Proteasix team.<br /><br />"
				+ "This is an automatic email. Please do not reply.";
		
		// Initialize the service proxy.
		if (callProvider == null) {
			callProvider = GWT.create(DBConnection.class);
		}

		// Set up the callback object.
		AsyncCallback callback = new AsyncCallback() {
			public void onFailure(Throwable caught) {
				Window.alert("failure");
				Window.alert(caught.getMessage());
			}

			public void onSuccess(Void result) {
				Window.alert("Email sent");
			}

			@Override
			public void onSuccess(Object result) {
				// TODO Auto-generated method stub

			}
		};
		if (emailadressTextbox.getText().contains("@")) {
			String to = emailadressTextbox.getText();
			// Make the call to the stock price service.
			callProvider.runMailer(to, subject, body, CSOutputOK, queryProtOut, callback);
		}
	}

	private void drawResultbyPeptideInfo_2(QueryOutput[] queryCSOut) {

		// if null nothing to do, then exit
		// this will prevent errors from showing up
		if (queryCSOut == null) {
			return;
		}

		resultPanel.add(result);
		result.addStyleName("lResult");
		resultTabPanel.add(Proteaseresult, "Protease View");
		resultTabPanel.add(CSresult, "Cleavage Site View");
		resultTabPanel.selectTab(0);
		popup.hide();

		LinkedList<QueryOutput> processedCSValidityOKList = getCSwithPeptideValidityOK(queryCSOut);
		QueryOutput[] CSOutputOK = new QueryOutput[processedCSValidityOKList
				.size()];
		processedCSValidityOKList.toArray(CSOutputOK);

		// get total number of protease/CS combination
		int totalcombi = CSOutputOK.length;

		// get total number of peptides
		String subUni = pepUniArea.getText().toUpperCase().trim();
		String splitSearchUni[] = subUni.split("\n");
		int totalpep = splitSearchUni.length;

		// get total number of CS
		int totalCS = boxNTerm.getValue() ? totalpep : totalpep * 2;

		// get predicted unique peptides
		int predictedpep = 0;
		HashMap<String, String> uniquepep = new HashMap<String, String>();
		for (QueryOutput string : CSOutputOK) {
			String key = string.peptide.Pep_Id;
			if (!uniquepep.containsKey(key)) {
				String value = null;
				uniquepep.put(key, value);
			}
		}
		predictedpep = uniquepep.size();
		float percentretrievedpep = (float) predictedpep / totalpep * 100;

		// get predicted unique CS
		int predictedCS = 0;
		HashMap<String, String> uniquecs = new HashMap<String, String>();
		for (QueryOutput string : CSOutputOK) {
			String key = string.peptide.Pep_Id
					+ string.cleavagesite.CS_NorCterm;
			if (!uniquecs.containsKey(key)) {
				String value = null;
				uniquecs.put(key, value);
			}
		}
		predictedCS = uniquecs.size();
		float percentretrievedCS = (float) predictedCS / totalCS * 100;

		gross.add(new HTML(
				"<font><dd>Number of predicted protease/cleavage site associations: <b>"
						+ totalcombi + "</b></font>"));

		String optiononlyN = boxNTerm.getValue() ? "<dd>(only N-term CS)" : "";
		HTML hgross1 = new HTML(
				"<font><dd>Total number of submitted peptides: <b>"
						+ totalpep
						+ "</b><br /><dd>Total number of potential cleavage sites: <b>"
						+ totalCS + "</b><br />" + optiononlyN + "</font");
		gross1.add(hgross1);

		HTML hgross2 = new HTML(
				"<font><dd>Peptides with at least one result: <b>"
						+ predictedpep
						+ "</b> (<b>"
						+ percentretrievedpep
						+ "</b>% of total number of submitted peptides)<br /><dd>Cleavage sites with at least one result: <b>"
						+ predictedCS
						+ "</b> (<b>"
						+ percentretrievedCS
						+ "</b>% of total number of potential cleavage sites)</font>");
		gross2.add(hgross2);
		gross.add(gross1);
		gross.add(gross2);
		gross.addStyleName("pSearchpanel");
		resultPanel.add(gross);
		resultPanel.add(resultTabPanel);

		LinkedList<QueryOutput> processedNONOutput = getPeptidewithValidityNON(queryCSOut);
		QueryOutput[] queryNONOut = new QueryOutput[processedNONOutput.size()];
		processedNONOutput.toArray(queryNONOut);

		LinkedList<QueryOutput> processedProteaseOutput = getProteasewithPeptideValidityOK(CSOutputOK);
		QueryOutput[] queryProtOut = new QueryOutput[processedProteaseOutput
				.size()];
		processedProteaseOutput.toArray(queryProtOut);

		createCleavageSiteTable(CSOutputOK, processedCSValidityOKList);
		createProteaseTable(queryProtOut, processedProteaseOutput);
		if (queryNONOut.length != 0) {
			createErrorTable(queryNONOut, processedNONOutput);
			HTML lblseqvalidity = new HTML(
					"Some of your peptides could not be used for the search due to inconstitencies with respective parental protein. Please check the \"Error\" tab for report.");
			lblseqvalidity.addStyleName("lResult");
			close.setUrl(GWT.getModuleBaseURL() + "Images/1353518245_Close.png");
			close.addStyleName("close");
			pseqvalidity.add(close);
			pseqvalidity
					.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			pseqvalidity
					.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			popupseqvalidity.add(pseqvalidity);
			pseqvalidity.add(lblseqvalidity);
			popupseqvalidity.setAnimationEnabled(true);
			popupseqvalidity.setGlassEnabled(true);
			popupseqvalidity.show();
			popupseqvalidity
					.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
						public void setPosition(int offsetWidth,
								int offsetHeight) {
							int left = (Window.getClientWidth() - offsetWidth) / 3;
							int top = (Window.getClientHeight() - offsetHeight) / 3;
							popupseqvalidity.setPopupPosition(left, top);
						}
					});
		}

	}

	private LinkedList<QueryOutput> getProteasewithPeptideValidityOK(
			QueryOutput[] CSOutputOK) {
		Map<String, List<Set<String>>> processedProteasehmap = new HashMap<String, List<Set<String>>>();
		for (QueryOutput out : CSOutputOK) {
			String key = out.protease.P_Uniprotid;
			if (!processedProteasehmap.containsKey(key)) {
				List value = new ArrayList<Set<String>>();
				for (int j = 0; j < 11; j++) {
					value.add(new HashSet<String>());
				}
				processedProteasehmap.put(key, value);
			}
			processedProteasehmap.get(key).get(0).add(out.protease.P_Symbol);
			processedProteasehmap.get(key).get(1).add(out.protease.P_Uniprotid);
			if (out.confidence.equals(SPSL))
				processedProteasehmap.get(key).get(2)
						.add(out.peptide.Pep_Id + out.cleavagesite.CS_NorCterm);
			else if (out.confidence.equals(SS))
				processedProteasehmap.get(key).get(3)
						.add(out.peptide.Pep_Id + out.cleavagesite.CS_NorCterm);
			else if (out.confidence.equals(HP))
				processedProteasehmap.get(key).get(4)
						.add(out.peptide.Pep_Id + out.cleavagesite.CS_NorCterm);
			else if (out.confidence.equals(P))
				processedProteasehmap.get(key).get(5)
						.add(out.peptide.Pep_Id + out.cleavagesite.CS_NorCterm);
			else if (out.confidence.equals(mm1))
				processedProteasehmap.get(key).get(6)
						.add(out.peptide.Pep_Id + out.cleavagesite.CS_NorCterm);
			else if (out.confidence.equals(mm2))
				processedProteasehmap.get(key).get(7)
						.add(out.peptide.Pep_Id + out.cleavagesite.CS_NorCterm);
			else if (out.confidence.equals(mm3))
				processedProteasehmap.get(key).get(8)
						.add(out.peptide.Pep_Id + out.cleavagesite.CS_NorCterm);
			processedProteasehmap.get(key).get(9).add(out.protease.P_OMIM);
			processedProteasehmap.get(key).get(10).add(out.protease.P_Ensembl);
		}

		Iterator iterator = processedProteasehmap.values().iterator();
		LinkedList<QueryOutput> processedProteaseOutput = new LinkedList<QueryOutput>();
		while (iterator.hasNext()) {
			int iSPSL = 0;
			int iSS = 0;
			int iHP = 0;
			int iP = 0;
			int i1mm = 0;
			int i2mm = 0;
			int i3mm = 0;
			QueryOutput processedProtout = new QueryOutput();
			ProteaseData protease = new ProteaseData();
			String values = iterator.next().toString();
			String splitarray[] = values.split("\\], \\[");
			String protsymbol = splitarray[0];
			protsymbol = protsymbol.replaceAll("\\[", "");
			protease.P_Symbol = protsymbol;
			protease.P_Uniprotid = splitarray[1];
			String resultSPSL = splitarray[2];
			protease.totalSPSL = getTotalProteaseConfidence(iSPSL, resultSPSL);
			String resultSS = splitarray[3];
			protease.totalSS = getTotalProteaseConfidence(iSS, resultSS);
			String resultHP = splitarray[4];
			protease.totalHP = getTotalProteaseConfidence(iHP, resultHP);
			String resultP = splitarray[5];
			protease.totalP = getTotalProteaseConfidence(iP, resultP);
			String resultmm1 = splitarray[6];
			protease.total1mm = getTotalProteaseConfidence(i1mm, resultmm1);
			String resultmm2 = splitarray[7];
			protease.total2mm = getTotalProteaseConfidence(i2mm, resultmm2);
			String resultmm3 = splitarray[8];
			protease.total3mm = getTotalProteaseConfidence(i3mm, resultmm3);
			protease.P_OMIM = splitarray[9];
			String ensembl = splitarray[10];
			ensembl = ensembl.replaceAll("\\]", "");
			protease.P_Ensembl = ensembl;
			processedProtout.setProtease(protease);
			processedProteaseOutput.add(processedProtout);
		}
		return processedProteaseOutput;
	}

	private LinkedList<QueryOutput> getPeptidewithValidityNON(
			QueryOutput[] queryCSOut) {
		Map<String, List<Set<String>>> processedValidityNONhmap = new HashMap<String, List<Set<String>>>();
		for (QueryOutput out : queryCSOut) {
			if (out.peptide.Pep_Seqvalidity == false) {
				String keyNON = out.peptide.Pep_Id;
				if (!processedValidityNONhmap.containsKey(keyNON)) {
					List value = new ArrayList<Set<String>>();
					for (int j = 0; j < 6; j++) {
						value.add(new HashSet<String>());
					}
					processedValidityNONhmap.put(keyNON, value);
				}
				processedValidityNONhmap.get(keyNON).get(0)
						.add(out.peptide.Pep_Id);
				processedValidityNONhmap.get(keyNON).get(1)
						.add(out.peptide.Pep_sequence);
				processedValidityNONhmap.get(keyNON).get(2)
						.add(Integer.toString(out.peptide.Pep_Start));
				processedValidityNONhmap.get(keyNON).get(3)
						.add(Integer.toString(out.peptide.Pep_End));
				processedValidityNONhmap.get(keyNON).get(4)
						.add(out.substrate.S_Symbol);
				processedValidityNONhmap.get(keyNON).get(5)
						.add(out.substrate.S_Uniprotid);
			}
		}
		Iterator iteratorNON = processedValidityNONhmap.values().iterator();
		LinkedList<QueryOutput> processedNONOutput = new LinkedList<QueryOutput>();
		while (iteratorNON.hasNext()) {
			QueryOutput processedNonOut = new QueryOutput();
			PeptideData peptide = new PeptideData();
			SubstrateData substrate = new SubstrateData();
			String values = iteratorNON.next().toString();
			String splitarray[] = values.split("\\], \\[");
			String pepID = splitarray[0];
			pepID = pepID.replaceAll("\\[", "");
			peptide.Pep_Id = pepID;
			peptide.Pep_sequence = splitarray[1];
			peptide.Pep_Start = Integer.parseInt(splitarray[2]);
			peptide.Pep_End = Integer.parseInt(splitarray[3]);
			substrate.S_Symbol = splitarray[4];
			String subUniID = splitarray[5];
			subUniID = subUniID.replaceAll("\\]", "");
			substrate.S_Uniprotid = subUniID;
			processedNonOut.setPeptide(peptide);
			processedNonOut.setSubstrate(substrate);
			processedNONOutput.add(processedNonOut);
		}
		return processedNONOutput;
	}

	private LinkedList<QueryOutput> getCSwithPeptideValidityOK(
			QueryOutput[] queryCSOut) {
		LinkedList<QueryOutput> processedCSValidityOKList = new LinkedList<QueryOutput>();
		for (QueryOutput queryOutput : queryCSOut) {
			if (queryOutput.peptide.Pep_Seqvalidity == true) {
				QueryOutput out = new QueryOutput();
				out = queryOutput;
				processedCSValidityOKList.add(out);
			}
		}
		return processedCSValidityOKList;
	}

	
	private int getTotalProteaseConfidence(int iConfidence,
			String resultConfidence) {
		if (!resultConfidence.equals("")) {
			String resultConfidenceArray[] = resultConfidence.split(", ");
			iConfidence = resultConfidenceArray.length;
		}
		return iConfidence;
	}

	private void updateSuggestBox() {
		oracle.clear();
		List<String> usernames = getUsernames();
		oracle.setDefaultSuggestionsFromText(usernames);
		for (String username : usernames) {
			oracle.add(username);
		}
	}

	private List<String> getUsernames() {
		ArrayList<String> usernames = new ArrayList<String>();
		String users = Cookies.getCookie("usernames");
		if (users != null) {
			for (String username : users.split(":")) {
				usernames.add(username);
			}
		}
		return usernames;
	}

	private boolean cookieContains(String s) {
		boolean contains = false;
		String users = Cookies.getCookie("usernames");
		if (users != null) {
			for (String username : users.split(":")) {
				if (username.equals(s))
					contains = true;
			}
		}
		return contains;
	}

	private void addToCookie(String username) {
		String users = Cookies.getCookie("usernames");
		if (users != null)
			Cookies.setCookie("usernames", users + ":" + username, new Date(
					new Date().getTime() + forgetMeIn));
		else
			Cookies.setCookie("usernames", username,
					new Date(new Date().getTime() + forgetMeIn));
	}

	private void removeFromCookie(String username) {
		String users = Cookies.getCookie("usernames");
		if (users != null)
			Cookies.removeCookie("usernames", username);
		else
			Cookies.removeCookie("usernames", username);

	}

	private void createErrorTable(QueryOutput[] ErrorArray,
			LinkedList<QueryOutput> ErrorList) {

		String errorhead = "The following peptide(s) could not be used for the search: <br /><br />";
		String errormsg = "";
		for (QueryOutput queryOutput : ErrorList) {
			if (queryOutput.peptide.Pep_sequence.equals("")) {
				errormsg = errormsg
						+ "<dd><b><font color=#8EEAD5>&#186</font></b> "
						+ queryOutput.peptide.Pep_Id
						+ ": the peptide end you specified for this peptide ("
						+ queryOutput.peptide.Pep_End
						+ ") exceeds the full-length of  "
						+ queryOutput.substrate.S_Symbol
						+ " (<a href=\"http://www.uniprot.org/uniprot/"
						+ queryOutput.substrate.S_Uniprotid
						+ "\"target=\"_blank\">"
						+ queryOutput.substrate.S_Uniprotid + "</a>).<br />";
			} else {
				errormsg = errormsg
						+ "<dd><b><font color=#8EEAD5>&#186</font></b> "
						+ queryOutput.peptide.Pep_Id
						+ ": the sequence you specified for this peptide ("
						+ queryOutput.peptide.Pep_sequence
						+ ") can not be found in "
						+ queryOutput.substrate.S_Symbol
						+ " (<a href=\"http://www.uniprot.org/uniprot/"
						+ queryOutput.substrate.S_Uniprotid
						+ "\"target=\"_blank\">"
						+ queryOutput.substrate.S_Uniprotid + "</a>).<br />";
			}
		}
		HTML error = new HTML(errorhead + errormsg);
		ErrorPanel.addStyleName("pSearchpanel");
		ErrorPanel.add(error);
		ErrorPanel.setWidth("1280px");
		Errorresult.add(ErrorPanel);
		resultTabPanel.add(Errorresult, "Error");

	}

	private void createProteaseTable(QueryOutput[] ProteaseOutputArray,
			LinkedList<QueryOutput> ProteaseOutputList) {
		int rowsprot = ProteaseOutputArray.length;

		CellTable.Resources ptableresources = GWT.create(PTableResources.class);
		CellTable<QueryOutput> proteaseTable = new CellTable<QueryOutput>(
				rowsprot, ptableresources);
		proteaseTable.setWidth("1280px");

		Column<QueryOutput, SafeHtml> Prot_Protease = new Column<QueryOutput, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(QueryOutput result) {
				String protease = result.protease.P_Symbol;
				return new SafeHtmlBuilder().appendHtmlConstant(
						"<a href=\"http://www.uniprot.org/uniprot/"
								+ result.protease.P_Uniprotid
								+ "\"target=\"_blank\">" + protease + "</a>")
						.toSafeHtml();
			}
		};

		Column<QueryOutput, SafeHtml> Prot_ConfidenceSPSL = new Column<QueryOutput, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(QueryOutput result) {
				int input = result.protease.totalSPSL;
				String color = "white";
				if (input > 0 && input < 3)
					color = "#E0F8EC";
				if (input >= 3 && input < 10)
					color = "#A9F5D0";
				if (input >= 10 && input < 20)
					color = "#58FAAC";
				if (input >= 20 && input < 50)
					color = "#00FF80";
				if (input >= 50 && input < 100)
					color = "#04B45F";
				String sinput = Integer.toString(input);
				return new SafeHtmlBuilder().appendHtmlConstant(
						"<div style = background-color:" + color
								+ ";padding:2px 2px 2px 2px><p>" + sinput
								+ "</p>").toSafeHtml();
			}
		};

		Column<QueryOutput, SafeHtml> Prot_ConfidenceSS = new Column<QueryOutput, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(QueryOutput result) {
				int input = result.protease.totalSS;
				String color = "white";
				if (input > 0 && input < 3)
					color = "#E0F8EC";
				if (input >= 3 && input < 10)
					color = "#A9F5D0";
				if (input >= 10 && input < 20)
					color = "#58FAAC";
				if (input >= 20 && input < 50)
					color = "#00FF80";
				if (input >= 50 && input < 100)
					color = "#04B45F";
				String sinput = Integer.toString(input);
				return new SafeHtmlBuilder().appendHtmlConstant(
						"<div style = background-color:" + color
								+ ";padding:2px 2px 2px 2px><p>" + sinput
								+ "</p>").toSafeHtml();
			}
		};
		// Column<QueryOutput, SafeHtml> Prot_ConfidenceHP =new
		// Column<QueryOutput, SafeHtml>(
		// new SafeHtmlCell()) {
		// @Override
		// public SafeHtml getValue(QueryOutput result) {
		// int input = result.protease.totalHP;
		// String color = "white";
		// if (input>0 && input < 5)
		// color = "E9FFF7";
		// if (input>=5 && input <10)
		// color = "C2FDE7";
		// String sinput = Integer.toString(input);
		// return new SafeHtmlBuilder().appendHtmlConstant(
		// "<div style = background-color:" + color
		// +";padding:2px 2px 2px 2px><p>" + sinput + "</p>").toSafeHtml();
		// }
		// };
		//
		// Column<QueryOutput, SafeHtml> Prot_ConfidenceP =new
		// Column<QueryOutput, SafeHtml>(
		// new SafeHtmlCell()) {
		// @Override
		// public SafeHtml getValue(QueryOutput result) {
		// int input = result.protease.totalP;
		// String color = "white";
		// if (input>0 && input < 5)
		// color = "#E0F8EC";
		// if (input>=5 && input <10)
		// color = "#A9F5D0";
		// if (input>=10 && input <20)
		// color = "#58FAAC";
		// if (input>=20 && input <50)
		// color = "#00FF80";
		// if (input>=50 && input <100)
		// color = "#04B45F";
		// String sinput = Integer.toString(input);
		// return new SafeHtmlBuilder().appendHtmlConstant(
		// "<div style = background-color:" + color
		// +";padding:2px 2px 2px 2px><p>" + sinput + "</p>").toSafeHtml();
		// }
		// };

		Column<QueryOutput, SafeHtml> Prot_Confidencemm1 = new Column<QueryOutput, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(QueryOutput result) {
				int input = result.protease.total1mm;
				String color = "white";
				if (input > 0 && input < 3)
					color = "#E0F8EC";
				if (input >= 3 && input < 10)
					color = "#A9F5D0";
				if (input >= 10 && input < 20)
					color = "#58FAAC";
				if (input >= 20 && input < 50)
					color = "#00FF80";
				if (input >= 50 && input < 100)
					color = "#04B45F";
				String sinput = Integer.toString(input);
				return new SafeHtmlBuilder().appendHtmlConstant(
						"<div style = background-color:" + color
								+ ";padding:2px 2px 2px 2px><p>" + sinput
								+ "</p>").toSafeHtml();
			}
		};

		Column<QueryOutput, SafeHtml> Prot_Confidencemm2 = new Column<QueryOutput, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(QueryOutput result) {
				int input = result.protease.total2mm;
				String color = "white";
				if (input > 0 && input < 3)
					color = "#E0F8EC";
				if (input >= 3 && input < 10)
					color = "#A9F5D0";
				if (input >= 10 && input < 20)
					color = "#58FAAC";
				if (input >= 20 && input < 50)
					color = "#00FF80";
				if (input >= 50 && input < 100)
					color = "#04B45F";
				String sinput = Integer.toString(input);
				return new SafeHtmlBuilder().appendHtmlConstant(
						"<div style = background-color:" + color
								+ ";padding:2px 2px 2px 2px><p>" + sinput
								+ "</p>").toSafeHtml();
			}
		};

		Column<QueryOutput, SafeHtml> Prot_Confidencemm3 = new Column<QueryOutput, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(QueryOutput result) {
				int input = result.protease.total3mm;
				String color = "white";
				if (input > 0 && input < 3)
					color = "#E0F8EC";
				if (input >= 3 && input < 10)
					color = "#A9F5D0";
				if (input >= 10 && input < 20)
					color = "#58FAAC";
				if (input >= 20 && input < 50)
					color = "#00FF80";
				if (input >= 50 && input < 100)
					color = "#04B45F";
				String sinput = Integer.toString(input);
				return new SafeHtmlBuilder().appendHtmlConstant(
						"<div style = background-color:" + color
								+ ";padding:2px 2px 2px 2px><p>" + sinput
								+ "</p>").toSafeHtml();
			}
		};

		Column<QueryOutput, SafeHtml> Prot_TOTAL = new Column<QueryOutput, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(QueryOutput result) {
				int input = result.protease.totalSPSL + result.protease.totalSS
						+ result.protease.total1mm + result.protease.total2mm
						+ result.protease.total3mm;
				String color = "white";
				if (input > 0 && input < 3)
					color = "#E0F8EC";
				if (input >= 3 && input < 10)
					color = "#A9F5D0";
				if (input >= 10 && input < 20)
					color = "#58FAAC";
				if (input >= 20 && input < 50)
					color = "#00FF80";
				if (input >= 50 && input < 100)
					color = "#04B45F";
				String sinput = Integer.toString(input);
				return new SafeHtmlBuilder().appendHtmlConstant(
						"<div style = background-color:" + color
								+ ";padding:2px 2px 2px 2px><p>" + sinput
								+ "</p>").toSafeHtml();
			}
		};

		
		 Column<QueryOutput, SafeHtml> Prot_OMIM = new Column<QueryOutput,
		 SafeHtml>(
		 new SafeHtmlCell()) {
		 @Override
		 public SafeHtml getValue(QueryOutput result) {
		 String omim = result.protease.P_OMIM;
		 if (omim.equals("-"))
		 return new SafeHtmlBuilder().appendHtmlConstant("-").toSafeHtml();
		 return new SafeHtmlBuilder().appendHtmlConstant(
		 "<font size=\"1\"><a href=\"http://omim.org/entry/"
		 + omim
		 + "\"target=\"_blank\">" + omim + "</a>")
		 .toSafeHtml();
		 }
		 };
		 

		 Column<QueryOutput, SafeHtml> Prot_Ensembl = new Column<QueryOutput,
		 SafeHtml>(
		 new SafeHtmlCell()) {
		 @Override
		 public SafeHtml getValue(QueryOutput result) {
		 String ensembl = result.protease.P_Ensembl;
		 if (ensembl.equals("N/A") || ensembl.equals("") )
		 return new SafeHtmlBuilder().appendHtmlConstant("-").toSafeHtml();
		 return new SafeHtmlBuilder().appendHtmlConstant(
		 "<font size=\"1\"><a href=\"http://www.ebi.ac.uk/gxa/gene/"
		 + ensembl
		 + "\"target=\"_blank\">" + ensembl + "</a>")
		 .toSafeHtml();
		 }
		 };



		Prot_Protease.setSortable(true);
		Prot_ConfidenceSPSL.setSortable(true);
		Prot_ConfidenceSS.setSortable(true);
		// Prot_ConfidenceHP.setSortable(true);
		// Prot_ConfidenceP.setSortable(true);
		Prot_Confidencemm1.setSortable(true);
		Prot_Confidencemm2.setSortable(true);
		Prot_Confidencemm3.setSortable(true);
		Prot_TOTAL.setSortable(true);
		proteaseTable.addColumn(Prot_Protease, "\u25B2Protease\u25BC");
		proteaseTable.addColumn(Prot_ConfidenceSPSL,
				"\u25B2Confidence ++++\u25BC");
		proteaseTable
				.addColumn(Prot_ConfidenceSS, "\u25B2Confidence +++\u25BC");
		// proteaseTable.addColumn(Prot_ConfidenceHP,
		// "\u25B2Confidence ++\u25BC");
		// proteaseTable.addColumn(Prot_ConfidenceP,
		// "\u25B2Confidence +\u25BC");
		proteaseTable
				.addColumn(Prot_Confidencemm1, "\u25B2Confidence +-\u25BC");
		proteaseTable.addColumn(Prot_Confidencemm2,
				"\u25B2Confidence +--\u25BC");
		proteaseTable.addColumn(Prot_Confidencemm3,
				"\u25B2Confidence +---\u25BC");
		proteaseTable.addColumn(Prot_TOTAL, "\u25B2Total\u25BC");
		proteaseTable.addColumn(Prot_OMIM, "OMIM");
		proteaseTable.addColumn(Prot_Ensembl, "Gene Expression Atlas");
		proteaseTable.setColumnWidth(Prot_OMIM, 5, Unit.EM);
		proteaseTable.setColumnWidth(Prot_Ensembl, 5, Unit.EM);
		
		Proteaseresult.add(proteaseTable);
		// Create a data provider.
		ListDataProvider<QueryOutput> csdataProvider = new ListDataProvider<QueryOutput>();

		// Connect the table to the data provider.
		csdataProvider.addDataDisplay(proteaseTable);

		// Add the data to the data provider, which automatically pushes it to
		// the
		// widget
		List<QueryOutput> proteaselist = csdataProvider.getList();
		for (QueryOutput proteaseresultTable : ProteaseOutputList) {
			proteaselist.add(proteaseresultTable);
		}

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<QueryOutput> proteaseColSortHandler = new ListHandler<QueryOutput>(
				proteaselist);
		proteaseColSortHandler.setComparator(Prot_Protease,
				new Comparator<QueryOutput>() {
					public int compare(QueryOutput o1, QueryOutput o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? o1.protease.P_Symbol
									.compareTo(o2.protease.P_Symbol) : 1;
						}
						return -1;
					}
				});
		proteaseTable.addColumnSortHandler(proteaseColSortHandler);

		// We know that the data is sorted alphabetically by default.
		proteaseTable.getColumnSortList().push(Prot_Protease);

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<QueryOutput> SPSLColSortHandler = new ListHandler<QueryOutput>(
				proteaselist);
		SPSLColSortHandler.setComparator(Prot_ConfidenceSPSL,
				new Comparator<QueryOutput>() {
					public int compare(QueryOutput o1, QueryOutput o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? Integer.toString(
									o1.protease.totalSPSL).compareTo(
									Integer.toString(o2.protease.totalSPSL))
									: 1;
						}
						return -1;
					}
				});
		proteaseTable.addColumnSortHandler(SPSLColSortHandler);

		// We know that the data is sorted alphabetically by default.
		proteaseTable.getColumnSortList().push(Prot_ConfidenceSPSL);
		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.

		ListHandler<QueryOutput> SSColSortHandler = new ListHandler<QueryOutput>(
				proteaselist);
		SSColSortHandler.setComparator(Prot_ConfidenceSS,
				new Comparator<QueryOutput>() {
					public int compare(QueryOutput o1, QueryOutput o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? Integer.toString(
									o1.protease.totalSS).compareTo(
									Integer.toString(o2.protease.totalSS)) : 1;
						}
						return -1;
					}
				});
		proteaseTable.addColumnSortHandler(SSColSortHandler);

		// We know that the data is sorted alphabetically by default.
		proteaseTable.getColumnSortList().push(Prot_ConfidenceSS);

		// ListHandler<QueryOutput> HPColSortHandler = new
		// ListHandler<QueryOutput>(
		// proteaselist);
		// HPColSortHandler.setComparator(Prot_ConfidenceHP,
		// new Comparator<QueryOutput>() {
		// public int compare(QueryOutput o1, QueryOutput o2) {
		// if (o1 == o2) {
		// return 0;
		// }
		//
		// // Compare the symbol columns.
		// if (o1 != null) {
		// return (o2 != null) ? Integer.toString( o1.protease.totalHP)
		// .compareTo(Integer.toString(o2.protease.totalHP)) : 1;
		// }
		// return -1;
		// }
		// });
		// proteaseTable.addColumnSortHandler(HPColSortHandler);
		//
		// // We know that the data is sorted alphabetically by default.
		// proteaseTable.getColumnSortList().push(Prot_ConfidenceHP);
		//
		// ListHandler<QueryOutput> PColSortHandler = new
		// ListHandler<QueryOutput>(
		// proteaselist);
		// PColSortHandler.setComparator(Prot_ConfidenceP,
		// new Comparator<QueryOutput>() {
		// public int compare(QueryOutput o1, QueryOutput o2) {
		// if (o1 == o2) {
		// return 0;
		// }
		//
		// // Compare the symbol columns.
		// if (o1 != null) {
		// return (o2 != null) ? Integer.toString( o1.protease.totalP)
		// .compareTo(Integer.toString(o2.protease.totalP)) : 1;
		// }
		// return -1;
		// }
		// });
		// proteaseTable.addColumnSortHandler(PColSortHandler);
		//
		// // We know that the data is sorted alphabetically by default.
		// proteaseTable.getColumnSortList().push(Prot_ConfidenceP);

		ListHandler<QueryOutput> mm1ColSortHandler = new ListHandler<QueryOutput>(
				proteaselist);
		mm1ColSortHandler.setComparator(Prot_Confidencemm1,
				new Comparator<QueryOutput>() {
					public int compare(QueryOutput o1, QueryOutput o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? Integer.toString(
									o1.protease.total1mm).compareTo(
									Integer.toString(o2.protease.total1mm)) : 1;
						}
						return -1;
					}
				});
		proteaseTable.addColumnSortHandler(mm1ColSortHandler);

		// We know that the data is sorted alphabetically by default.
		proteaseTable.getColumnSortList().push(Prot_Confidencemm1);

		ListHandler<QueryOutput> mm2ColSortHandler = new ListHandler<QueryOutput>(
				proteaselist);
		mm2ColSortHandler.setComparator(Prot_Confidencemm2,
				new Comparator<QueryOutput>() {
					public int compare(QueryOutput o1, QueryOutput o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? Integer.toString(
									o1.protease.total2mm).compareTo(
									Integer.toString(o2.protease.total2mm)) : 1;
						}
						return -1;
					}
				});
		proteaseTable.addColumnSortHandler(mm2ColSortHandler);

		// We know that the data is sorted alphabetically by default.
		proteaseTable.getColumnSortList().push(Prot_Confidencemm2);

		ListHandler<QueryOutput> mm3ColSortHandler = new ListHandler<QueryOutput>(
				proteaselist);
		mm3ColSortHandler.setComparator(Prot_Confidencemm3,
				new Comparator<QueryOutput>() {
					public int compare(QueryOutput o1, QueryOutput o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? Integer.toString(
									o1.protease.total3mm).compareTo(
									Integer.toString(o2.protease.total3mm)) : 1;
						}
						return -1;
					}
				});
		proteaseTable.addColumnSortHandler(mm3ColSortHandler);

		// We know that the data is sorted alphabetically by default.
		proteaseTable.getColumnSortList().push(Prot_Confidencemm3);

		ListHandler<QueryOutput> totalColSortHandler = new ListHandler<QueryOutput>(
				proteaselist);
		totalColSortHandler.setComparator(Prot_TOTAL,
				new Comparator<QueryOutput>() {
					public int compare(QueryOutput o1, QueryOutput o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? Integer.toString(
									o1.protease.totalSPSL + o1.protease.totalSS
											+ o1.protease.total1mm
											+ o1.protease.total2mm
											+ o1.protease.total3mm).compareTo(
									Integer.toString(o2.protease.totalSPSL
											+ o2.protease.totalSS
											+ o2.protease.total1mm
											+ o2.protease.total2mm
											+ o2.protease.total3mm)) : 1;
						}
						return -1;
					}
				});
		proteaseTable.addColumnSortHandler(totalColSortHandler);

		// We know that the data is sorted alphabetically by default.
		proteaseTable.getColumnSortList().push(Prot_TOTAL);

	}

	private void createCleavageSiteTable(QueryOutput[] CsOutputArray,
			List<QueryOutput> CsOutputList) {

		int rowscs = CsOutputArray.length;

		CellTable.Resources cstableresources = GWT
				.create(CSTableResources.class);
		CellTable<QueryOutput> csTable = new CellTable<QueryOutput>(rowscs,
				cstableresources);
		csTable.setWidth("1280px");

		TextColumn<QueryOutput> CS_PepID = new TextColumn<QueryOutput>() {
			@Override
			public String getValue(QueryOutput result) {
				String input = result.peptide.Pep_Id;
				return input;
			}
		};

		TextColumn<QueryOutput> CS_CSNorCterm = new TextColumn<QueryOutput>() {
			@Override
			public String getValue(QueryOutput result) {
				String input = result.cleavagesite.CS_NorCterm;
				return input;
			}
		};

		Column<QueryOutput, SafeHtml> CS_Substrate = new Column<QueryOutput, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(QueryOutput result) {
				String substrate = result.substrate.S_Symbol;
				return new SafeHtmlBuilder().appendHtmlConstant(
						"<a href=\"http://www.uniprot.org/uniprot/"
								+ result.substrate.S_Uniprotid
								+ "\"target=\"_blank\">" + substrate + "</a>")
						.toSafeHtml();

			}

		};

		Column<QueryOutput, SafeHtml> CS_PepSeq = new Column<QueryOutput, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(QueryOutput result) {
				String sequence = null;
				int length = result.peptide.Pep_sequence.length();
				if (length > 16) {
					sequence = result.cleavagesite.CS_NorCterm.contains("N") ? sequence = "<p><font size=\"1\"><font color= #82a38d><u>"
							+ result.peptide.Pep_sequence.substring(0, 3)
							+ "</u></font>"
							+ result.peptide.Pep_sequence.substring(3, 8)
							+ "[...]"
							+ result.peptide.Pep_sequence.substring(length - 8)
							+ "</font></p>"
							: "<p><font size=\"1\">"
									+ result.peptide.Pep_sequence.substring(0,
											8)
									+ "[...]"
									+ result.peptide.Pep_sequence.substring(
											length - 8, length - 3)
									+ "<font color= #82a38d><u>"
									+ result.peptide.Pep_sequence
											.substring(length - 3)
									+ "</u></font></font></p>";
				} else {
					sequence = result.cleavagesite.CS_NorCterm.contains("N") ? "<p><font size=\"1\"><font color= #82a38d><u>"
							+ result.peptide.Pep_sequence.substring(0, 3)
							+ "</u></font>"
							+ result.peptide.Pep_sequence.substring(3)
							+ "</font></p>"
							: "<p><font size=\"1\">"
									+ result.peptide.Pep_sequence.substring(0,
											length - 3)
									+ "<font color= #82a38d><u>"
									+ result.peptide.Pep_sequence
											.substring(length - 3)
									+ "</u></font></font></p>";
				}
				return new SafeHtmlBuilder().appendHtmlConstant(sequence)
						.toSafeHtml();
			}

		};

		Column<QueryOutput, SafeHtml> CS_CSSeq = new Column<QueryOutput, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(QueryOutput result) {
				String cleavageSite = result.cleavagesite.CS_InputCS;
				String begin = cleavageSite.substring(0, 4);
				String end = cleavageSite.substring(4, 8);
				String cs = null;
				cs = result.cleavagesite.CS_NorCterm.contains("N") ? "<p>"
						+ begin + "\u00A6<font color= #82a38d><u>" + end
						+ "</u></font></p>" : "<p><font color= #82a38d><u>"
						+ begin + "</u></font>\u00A6" + end + "</p>";
				return new SafeHtmlBuilder().appendHtmlConstant(cs)
						.toSafeHtml();
			}
		};

		Column<QueryOutput, SafeHtml> CS_Protease = new Column<QueryOutput, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(QueryOutput result) {
				String symbol = result.protease.P_Symbol.toUpperCase();
				return new SafeHtmlBuilder().appendHtmlConstant(
						"<a href=\"http://www.uniprot.org/uniprot/"
								+ result.protease.P_Uniprotid
								+ "\"target=\"_blank\">" + symbol + "</a>")
						.toSafeHtml();
			}
		};
		
		Column<QueryOutput, SafeHtml> CS_MatrixDB= new Column<QueryOutput, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(QueryOutput result) {
				String symbol = result.MatrixDB;
				String interaction = null;
				interaction = !symbol.equals("-") ? "<a href=\""+ symbol
						+ "\"target=\"_blank\">Yes</a>" : "-";
				return new SafeHtmlBuilder().appendHtmlConstant(
						interaction)
						.toSafeHtml();
			}
		};

		Column<QueryOutput, SafeHtml> CS_ConfidenceSPSL = new Column<QueryOutput, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(QueryOutput result) {
				String input = result.confidence.equals(SPSL) ? "&#215" : "";
				return new SafeHtmlBuilder().appendHtmlConstant(
						"<font size = 3><b>" + input + "</b></font>")
						.toSafeHtml();
			}
		};

		Column<QueryOutput, SafeHtml> CS_ConfidenceSS = new Column<QueryOutput, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(QueryOutput result) {
				String input = result.confidence.equals(SS) ? "&#215" : "";
				return new SafeHtmlBuilder().appendHtmlConstant(
						"<font size = 3><b>" + input + "</b></font>")
						.toSafeHtml();
			}
		};

		// Column<QueryOutput, SafeHtml> CS_ConfidenceHP = new
		// Column<QueryOutput, SafeHtml>(
		// new SafeHtmlCell()) {
		// @Override
		// public SafeHtml getValue(QueryOutput result) {
		// String input = result.confidence.equals(HP) ? "&#215" : "";
		// return new SafeHtmlBuilder().appendHtmlConstant("<font size = 3><b>"
		// + input + "</b></font>")
		// .toSafeHtml();
		// }
		// };
		//
		// Column<QueryOutput, SafeHtml> CS_ConfidenceP = new
		// Column<QueryOutput, SafeHtml>(
		// new SafeHtmlCell()) {
		// @Override
		// public SafeHtml getValue(QueryOutput result) {
		// String input = result.confidence.equals(P) ? "&#215" : "";
		// return new SafeHtmlBuilder().appendHtmlConstant("<font size = 3><b>"
		// + input + "</b></font>")
		// .toSafeHtml();
		// }
		// };
		Column<QueryOutput, SafeHtml> CS_Confidencemm1 = new Column<QueryOutput, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(QueryOutput result) {
				String input = result.confidence.equals(mm1) ? "&#215" : "";
				return new SafeHtmlBuilder().appendHtmlConstant(
						"<font size = 3><b>" + input + "</b></font>")
						.toSafeHtml();
			}
		};
		Column<QueryOutput, SafeHtml> CS_Confidencemm2 = new Column<QueryOutput, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(QueryOutput result) {
				String input = result.confidence.equals(mm2) ? "&#215" : "";
				return new SafeHtmlBuilder().appendHtmlConstant(
						"<font size = 3><b>" + input + "</b></font>")
						.toSafeHtml();
			}
		};

		Column<QueryOutput, SafeHtml> CS_Confidencemm3 = new Column<QueryOutput, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(QueryOutput result) {
				String input = result.confidence.equals(mm3) ? "&#215" : "";
				return new SafeHtmlBuilder().appendHtmlConstant(
						"<font size = 3><b>" + input + "</b></font>")
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
								String valuesplitsplit[] = valuesplit[i]
										.split("show/");
								String cutdbentry = valuesplitsplit[1];
								externallink = externallink
										+ "; "
										+ "<font size=\"1\"><a href=\"http://cutdb.burnham.org/relation/show/"
										+ cutdbentry + "\"target=\"_blank\">"
										+ cutdbentry + "</a></font>";
							} else if (valuesplit[i].contains("uniprot")) {
								String valuesplitsplit[] = valuesplit[i]
										.split("uniprot/");
								String unientry = valuesplitsplit[1];
								externallink = externallink
										+ "; "
										+ "<font size=\"1\"><a href=\"http://www.uniprot.org/uniprot/"
										+ unientry + "\"target=\"_blank\">"
										+ unientry + "</a></font>";
							}

							i++;
						}
						externallink = externallink.replaceFirst("; ", "");

					} else {
						if (external.contains("/show")) {
							String valuesplitsplit[] = external.split("show/");
							String cutdbentry = valuesplitsplit[1];
							externallink = "<font size=\"1\"><a href=\"http://cutdb.burnham.org/relation/show/"
									+ cutdbentry
									+ "\"target=\"_blank\">"
									+ cutdbentry + "</a></font>";
						} else if (external.contains("uniprot")) {
							String valuesplitsplit[] = external
									.split("uniprot/");
							String unientry = valuesplitsplit[1];
							externallink = "<font size=\"1\"><a href=\"http://www.uniprot.org/uniprot/"
									+ unientry
									+ "\"target=\"_blank\">"
									+ unientry + "</a></font>";
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

							pmid = pmid
									+ "; "
									+ "<font size=\"1\"><a href=\"http://www.ncbi.nlm.nih.gov/pubmed/"
									+ pmidentry + "\"target=\"_blank\">"
									+ pmidentry + "</a></font>";
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

		CS_PepID.setSortable(true);
		CS_Substrate.setSortable(true);
		CS_Protease.setSortable(true);
		CS_MatrixDB.setSortable(true);
		CS_CSSeq.setSortable(true);
		CS_ConfidenceSPSL.setSortable(true);
		CS_ConfidenceSS.setSortable(true);
		// CS_ConfidenceHP.setSortable(true);
		// CS_ConfidenceP.setSortable(true);
		CS_Confidencemm1.setSortable(true);
		CS_Confidencemm2.setSortable(true);
		CS_Confidencemm3.setSortable(true);
		//
		csTable.addColumn(CS_PepID, "\u25B2ID\u25BC");
		csTable.addColumn(CS_CSNorCterm, "Term");
		csTable.addColumn(CS_Substrate, "\u25B2Substrate\u25BC");
		csTable.addColumn(CS_PepSeq, "Peptide Sequence");
		csTable.addColumn(CS_CSSeq, "\u25B2Searched\u25BC CS");
		csTable.addColumn(CS_Protease, "\u25B2Protease\u25BC");
		csTable.addColumn(CS_MatrixDB, "\u25B2Interaction\u25BC in MatrixDB");
		csTable.addColumn(CS_ConfidenceSPSL, "\u25B2Confidence\u25BC ++++");
		csTable.addColumn(CS_ConfidenceSS, "\u25B2Confidence\u25BC +++");
		// csTable.addColumn(CS_ConfidenceHP, "\u25B2Confidence\u25BC ++");
		// csTable.addColumn(CS_ConfidenceP, "\u25B2Confidence\u25BC +");
		csTable.addColumn(CS_Confidencemm1, "\u25B2Confidence\u25BC +-");
		csTable.addColumn(CS_Confidencemm2, "\u25B2Confidence\u25BC +--");
		csTable.addColumn(CS_Confidencemm3, "\u25B2Confidence\u25BC +---");
		csTable.addColumn(extlinkCol, "External Link");
		csTable.addColumn(pmidCol, "Ref.");
		//
		csTable.setColumnWidth(CS_PepID, 10, Unit.EM);
		csTable.setColumnWidth(CS_Substrate, 5, Unit.EM);
		csTable.setColumnWidth(CS_PepSeq, 10, Unit.EM);
		csTable.setColumnWidth(CS_CSSeq, 10, Unit.EM);
		csTable.setColumnWidth(CS_Protease, 5, Unit.EM);
		csTable.setColumnWidth(CS_MatrixDB, 5, Unit.EM);
		csTable.setColumnWidth(extlinkCol, 5, Unit.EM);
		csTable.setColumnWidth(pmidCol, 5, Unit.EM);
		//
		CSresult.add(csTable);

		//
		// Create a data provider.
		ListDataProvider<QueryOutput> csdataProvider = new ListDataProvider<QueryOutput>();

		// Connect the table to the data provider.
		csdataProvider.addDataDisplay(csTable);

		// Add the data to the data provider, which automatically pushes it to
		// the
		// widget
		List<QueryOutput> cslist = csdataProvider.getList();
		for (QueryOutput csresultTable : CsOutputList) {
			cslist.add(csresultTable);
		}

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<QueryOutput> inputNumberColSortHandler = new ListHandler<QueryOutput>(
				cslist);
		inputNumberColSortHandler.setComparator(CS_PepID,
				new Comparator<QueryOutput>() {
					public int compare(QueryOutput o1, QueryOutput o2) {
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
		csTable.getColumnSortList().push(CS_PepID);

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<QueryOutput> substrateColSortHandler = new ListHandler<QueryOutput>(
				cslist);
		substrateColSortHandler.setComparator(CS_Substrate,
				new Comparator<QueryOutput>() {
					public int compare(QueryOutput o1, QueryOutput o2) {
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
		csTable.getColumnSortList().push(CS_Substrate);

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<QueryOutput> proteaseColSortHandler = new ListHandler<QueryOutput>(
				cslist);
		proteaseColSortHandler.setComparator(CS_Protease,
				new Comparator<QueryOutput>() {
					public int compare(QueryOutput o1, QueryOutput o2) {
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
		csTable.getColumnSortList().push(CS_Protease);
		
		ListHandler<QueryOutput> matrixDBColSortHandler = new ListHandler<QueryOutput>(
				cslist);
		matrixDBColSortHandler.setComparator(CS_MatrixDB,
				new Comparator<QueryOutput>() {
					public int compare(QueryOutput o1, QueryOutput o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? o1.MatrixDB
									.toUpperCase().compareTo(
											o2.MatrixDB.toUpperCase())
									: 1;
						}
						return -1;
					}
				});
		csTable.addColumnSortHandler(matrixDBColSortHandler);

		// We know that the data is sorted alphabetically by default.
		csTable.getColumnSortList().push(CS_MatrixDB);

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<QueryOutput> searchedCSColSortHandler = new ListHandler<QueryOutput>(
				cslist);
		searchedCSColSortHandler.setComparator(CS_CSSeq,
				new Comparator<QueryOutput>() {
					public int compare(QueryOutput o1, QueryOutput o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? o1.cleavagesite.CS_InputCS
									.toUpperCase().compareTo(
											o2.cleavagesite.CS_InputCS
													.toUpperCase()) : 1;
						}
						return -1;
					}
				});
		csTable.addColumnSortHandler(searchedCSColSortHandler);

		// We know that the data is sorted alphabetically by default.
		csTable.getColumnSortList().push(CS_CSSeq);

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<QueryOutput> SPSLColSortHandler = new ListHandler<QueryOutput>(
				cslist);
		SPSLColSortHandler.setComparator(CS_ConfidenceSPSL,
				new Comparator<QueryOutput>() {
					public int compare(QueryOutput o1, QueryOutput o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? o1.SPSL.toUpperCase()
									.compareTo(o2.SPSL.toUpperCase()) : 1;
						}
						return -1;
					}
				});
		csTable.addColumnSortHandler(SPSLColSortHandler);

		// We know that the data is sorted alphabetically by default.
		csTable.getColumnSortList().push(CS_ConfidenceSPSL);

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<QueryOutput> SSColSortHandler = new ListHandler<QueryOutput>(
				cslist);
		SSColSortHandler.setComparator(CS_ConfidenceSS,
				new Comparator<QueryOutput>() {
					public int compare(QueryOutput o1, QueryOutput o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? o1.SS.toUpperCase()
									.compareTo(o2.SS.toUpperCase()) : 1;
						}
						return -1;
					}
				});
		csTable.addColumnSortHandler(SSColSortHandler);

		// We know that the data is sorted alphabetically by default.
		csTable.getColumnSortList().push(CS_ConfidenceSS);

		// // Add a ColumnSortEvent.ListHandler to connect sorting to the
		// // java.util.List.
		// ListHandler<QueryOutput> HPColSortHandler = new
		// ListHandler<QueryOutput>(
		// cslist);
		// HPColSortHandler.setComparator(CS_ConfidenceHP,
		// new Comparator<QueryOutput>() {
		// public int compare(QueryOutput o1, QueryOutput o2) {
		// if (o1 == o2) {
		// return 0;
		// }
		//
		// // Compare the symbol columns.
		// if (o1 != null) {
		// return (o2 != null) ? o1.HP
		// .toUpperCase().compareTo(
		// o2.HP
		// .toUpperCase()) : 1;
		// }
		// return -1;
		// }
		// });
		// csTable.addColumnSortHandler(HPColSortHandler);
		//
		// // We know that the data is sorted alphabetically by default.
		// csTable.getColumnSortList().push(CS_ConfidenceHP);
		//
		//
		// // Add a ColumnSortEvent.ListHandler to connect sorting to the
		// // java.util.List.
		// ListHandler<QueryOutput> PColSortHandler = new
		// ListHandler<QueryOutput>(
		// cslist);
		// PColSortHandler.setComparator(CS_ConfidenceP,
		// new Comparator<QueryOutput>() {
		// public int compare(QueryOutput o1, QueryOutput o2) {
		// if (o1 == o2) {
		// return 0;
		// }
		//
		// // Compare the symbol columns.
		// if (o1 != null) {
		// return (o2 != null) ? o1.P
		// .toUpperCase().compareTo(
		// o2.P
		// .toUpperCase()) : 1;
		// }
		// return -1;
		// }
		// });
		// csTable.addColumnSortHandler(PColSortHandler);
		//
		// // We know that the data is sorted alphabetically by default.
		// csTable.getColumnSortList().push(CS_ConfidenceP);

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<QueryOutput> mm1ColSortHandler = new ListHandler<QueryOutput>(
				cslist);
		mm1ColSortHandler.setComparator(CS_Confidencemm1,
				new Comparator<QueryOutput>() {
					public int compare(QueryOutput o1, QueryOutput o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? o1.mm1.toUpperCase()
									.compareTo(o2.mm1.toUpperCase()) : 1;
						}
						return -1;
					}
				});
		csTable.addColumnSortHandler(mm1ColSortHandler);

		// We know that the data is sorted alphabetically by default.
		csTable.getColumnSortList().push(CS_Confidencemm1);

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<QueryOutput> mm2ColSortHandler = new ListHandler<QueryOutput>(
				cslist);
		mm2ColSortHandler.setComparator(CS_Confidencemm2,
				new Comparator<QueryOutput>() {
					public int compare(QueryOutput o1, QueryOutput o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? o1.mm2.toUpperCase()
									.compareTo(o2.mm2.toUpperCase()) : 1;
						}
						return -1;
					}
				});
		csTable.addColumnSortHandler(mm2ColSortHandler);

		// We know that the data is sorted alphabetically by default.
		csTable.getColumnSortList().push(CS_Confidencemm2);

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<QueryOutput> mm3ColSortHandler = new ListHandler<QueryOutput>(
				cslist);
		mm3ColSortHandler.setComparator(CS_Confidencemm3,
				new Comparator<QueryOutput>() {
					public int compare(QueryOutput o1, QueryOutput o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? o1.mm3.toUpperCase()
									.compareTo(o2.mm3.toUpperCase()) : 1;
						}
						return -1;
					}
				});
		csTable.addColumnSortHandler(mm3ColSortHandler);

		// We know that the data is sorted alphabetically by default.
		csTable.getColumnSortList().push(CS_Confidencemm3);
	}

}