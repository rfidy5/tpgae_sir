package fr.istic.gwt.devinette.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Devinette implements EntryPoint {

	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel visuPanel = new HorizontalPanel();
	private HorizontalPanel buttonPanel = new HorizontalPanel();
	private HorizontalPanel msgInstructionPanel = new HorizontalPanel();
	private TextBox visuDevinetteTextBox = new TextBox();
	private Button startButton = new Button("Demarrer");
	private Label label;

	private int randomNumber;
	private int entercompteur;

	private static int lowNumber;
	private static int highNumber;
	private static String defaultLabel;
	private final Label labelinstruction=new Label("Entrez un chiffre puis appuyez sur \"Entree\"");
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {	
		// initialisation des bornes
		initialiseLowHighNumber(0, 100);
		
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		label= new Label(defaultLabel);

		
		
		startButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				refresh();
				startButton.setEnabled(false);
				visuPanel.add(visuDevinetteTextBox);
				visuPanel.add(label);

				msgInstructionPanel.add(labelinstruction);
			}
		});

		buttonPanel.setSpacing(20);
		buttonPanel.add(startButton);

		mainPanel.add(visuPanel);
		mainPanel.add(buttonPanel);
		mainPanel.add(msgInstructionPanel);
		
		
		RootPanel.get("mainDevinette").add(mainPanel);

		//		 Listen for keyboard events in the input box.
		visuDevinetteTextBox.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {	
					entercompteur++;
//					System.out.println("enter" + entercompteur);    		  

					try {
						int entryVal = Integer.parseInt(visuDevinetteTextBox.getText());

//						System.out.println("return "+verifyNumber(entryVal));

						if(verifyNumber(entryVal)==1){
							label.setText("trop grand / "+ entercompteur +" coups effectues");
						}if(verifyNumber(entryVal)==-1){
							label.setText("trop petit / "+ entercompteur +" coups effectues");
						}if(verifyNumber(entryVal)==0){
							label.setText(" Vous avez gagne en "+entercompteur+" coups");
							new MessageInfoBox("Vous pouvez rejouer").afficher();
						}if(verifyNumber(entryVal)==2){
							label.setText(defaultLabel);
						}
					} catch (Exception e) {
//						System.out.println(e.getMessage());
						label.setText("Format non valide! Veuillez entrer un nombre entier!");
					}

				}
			}
		});
	}
	/** 
	 * fonction appelee a chaque reinitialisation
	 */
	private void refresh() {
		visuDevinetteTextBox.setEnabled(true);
		visuDevinetteTextBox.setValue("");
		createNumber();
		System.out.println(randomNumber);
		entercompteur=0;
		label.setText(defaultLabel);

	}
	/**
	 * creation du nombre aleatoire a deviner
	 * @return
	 */
	private int createNumber(){
		randomNumber = (int)(Math.random() * (highNumber-lowNumber)) + lowNumber;
		return randomNumber;
	}

	/**
	 * permet de definir l'intervalle de definition des nombres
	 * @param lower borne inferieure
	 * @param higher borne superieure
	 */
	private void initialiseLowHighNumber(int lower, int higher){
		lowNumber=lower;
		highNumber=higher;
		defaultLabel="Choisissez un nombre entre" +lowNumber+" et "+highNumber;
	}
	/**
	 * permet de verifier si le nombre a ete trouve ou pas
	 * @param num
	 * @return
	 */
	private int verifyNumber(int number){

		//le nombre entre est trop grand
		if(number>randomNumber && number<=highNumber){
			return 1;
		}//le nombre entre est trop petit
		if(number<randomNumber && number>=lowNumber){
			return -1;			
		}//le nombre a ete trouve
		if(number==randomNumber){
			return 0;
		}//le nombre entre n'est pas dans l'intervalle
		if(number>highNumber || number<lowNumber){ 
			return 2;
		}
		return 3;
	}

	/**
	 * classe pour creer une boite de dialogue
	 * @author 14009044
	 *
	 */
	private class MessageInfoBox extends DialogBox {
		/**
		 * constructeur de la dialogbox
		 * @param message 
		 */
		private MessageInfoBox(String message) {

			setText("Jeu de devinette");
			final DockPanel panel = new DockPanel();
			panel.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
			panel.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
			panel.setStyleName("alignement-gauche");
			panel.add(new Label(message), DockPanel.CENTER);      

			SimplePanel panelBouton = new SimplePanel();
			panelBouton.setStyleName("alignement-droite");

			Button boutonOk = new Button("Ok");
			boutonOk.setWidth("100px");
			boutonOk.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					MessageInfoBox.this.hide();
					startButton.setEnabled(true);
					visuDevinetteTextBox.setEnabled(false);
				}
			});

			panelBouton.add(boutonOk);
			panel.add(panelBouton, DockPanel.SOUTH);
			setWidget(panel);
		}
		/**
		 * pour afficher la boite de dialogue
		 */
		public void afficher() {
			this.center();
			this.setVisible(true);
			startButton.setText("Rejouer");
		}
	}

}
