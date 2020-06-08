package GameMenu;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.input.KeyCode;



public class Main extends Application {

	private GameMenu gameMenu;
	private MediaPlayer mediaPlayer;
	private Stage primary;
	private int spaceshipNumber = 0;
	private int space = 0;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Pane root = new Pane();
		primary = primaryStage;
		root.setPrefSize(1280, 720);
		music();

		// Read the image from the file
		InputStream is = Files.newInputStream(Paths.get("resources/space.jpg"));
		Image img = new Image(is);
		is.close();
		
		// Set the image as background for the menu, and resize it
		ImageView imgView = new ImageView(img);
		imgView.setFitWidth(1280);
		imgView.setFitHeight(720);

		gameMenu = new GameMenu();
		gameMenu.setVisible(true);

		root.getChildren().addAll(imgView, gameMenu);

		Scene scene = new Scene(root);
		scene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ESCAPE) {
				FadeTransition fade = new FadeTransition(Duration.seconds(0.5), gameMenu);
				fade.setFromValue(0);
				fade.setToValue(1);
				gameMenu.setVisible(true);
				fade.play();
			}
		});
		primaryStage.setScene(scene);
		primaryStage.show();

	}
		 
	private void music() {
		String path = "resources/you_can't_fail.mp3";
		Media song = new Media(Paths.get(path).toUri().toString());
		mediaPlayer = new MediaPlayer(song);
		mediaPlayer.setVolume(0.25);
		mediaPlayer.setOnEndOfMedia(new Runnable( ) {
			public void run() {
				mediaPlayer.seek(Duration.ZERO);
			}
		});
		mediaPlayer.play();
	}
	

	private static class MenuButton extends StackPane {
		private Text text;

		public MenuButton(String name) {
			text = new Text(name);
			text.setFont(text.getFont().font(20));
			text.setFill(Color.WHITE);

			Rectangle background = new Rectangle(250, 30);
			background.setOpacity(0.4);
			background.setFill(Color.BLACK);
			background.setEffect(new GaussianBlur(3.5));

			setAlignment(Pos.CENTER);
			setRotate(-0.3);
			getChildren().addAll(background, text);

			setOnMouseEntered(e -> {
				background.setTranslateX(10);
				text.setTranslateX(10);
				background.setFill(Color.WHITE);
				text.setFill(Color.BLACK);
			});

			setOnMouseExited(e -> {
				background.setTranslateX(0);
				text.setTranslateX(0);
				background.setFill(Color.BLACK);
				text.setFill(Color.WHITE);
			});

			DropShadow drop = new DropShadow(50, Color.WHITE);
			drop.setInput(new Glow());

			setOnMousePressed(e -> setEffect(drop));
			setOnMouseReleased(e -> setEffect(null));
		}
	}

	public class GameMenu extends Parent {

		private boolean alert(String username, String password) {
			boolean showAlert = true;
			boolean lowerCase = false, upperCase = false, digit = false;
			Alert alert = new Alert(AlertType.ERROR);

			for(int i = 0; i < password.length(); i++) {
				char ch = password.charAt(i);
				if(Character.isDigit(ch)) digit = true;
				else if(Character.isUpperCase(ch)) upperCase = true;
				else if(Character.isLowerCase(ch)) lowerCase = true;	
			}
			
			if (username.isEmpty() && password.isEmpty()) {
				alert.setTitle("Login Error");
				alert.setHeaderText("No User name and Password introduced");
				alert.setContentText("Please input a valid username and password");
				alert.show();
			} else if (username.isEmpty()) {
				alert.setTitle("Login Error");
				alert.setHeaderText("No username introduced");
				alert.setContentText("Please input a username");
				alert.show();
			} else if (password.isEmpty()) {
				alert.setTitle("Login Error");
				alert.setHeaderText("No Password introduced");
				alert.setContentText("Please input a valid password");
				alert.show();
			} else if (password.length() < 7) {
				alert.setTitle("Login Error");
				alert.setHeaderText("Invalid Password");
				alert.setContentText("Please input a Password longer than 6 characters.");
				alert.show();
			} else if (!digit) {
				alert.setTitle("Login Error");
				alert.setHeaderText("Invalid Password");
				alert.setContentText("Please input a Password that contains at least one digit.");
				alert.show();
			} else if (!lowerCase) {
				alert.setTitle("Login Error");
				alert.setHeaderText("Invalid Password");
				alert.setContentText("Please input a Password that contains at least one lower case letter.");
				alert.show();
			} else if (!upperCase) {
				alert.setTitle("Login Error");
				alert.setHeaderText("Invalid Password");
				alert.setContentText("Please input a Password that contains at least one upper case letter.");
				alert.show();
			} else
				showAlert = false;
			return showAlert;
		}

		private void transition(VBox mainMenu, VBox secondaryMenu, Boolean transitionType) {
			final int offset = 400;
			// transitionType is true when going from a main menu to a secondary one and is
			// false the other way
			if (transitionType) {
				getChildren().add(secondaryMenu);
				TranslateTransition t1 = new TranslateTransition(Duration.seconds(0.25), mainMenu);
				t1.setToX(mainMenu.getTranslateX() - offset);

				TranslateTransition t2 = new TranslateTransition(Duration.seconds(0.5), secondaryMenu);
				t2.setToX(mainMenu.getTranslateX());

				// Play the transition animations
				t1.play();
				t2.play();

				// Stop the animation
				t1.setOnFinished(event -> getChildren().remove(mainMenu));
			} else if (!transitionType) {
				getChildren().add(mainMenu);
				TranslateTransition t1 = new TranslateTransition(Duration.seconds(0.25), secondaryMenu);
				t1.setToX(secondaryMenu.getTranslateX() + offset);

				TranslateTransition t2 = new TranslateTransition(Duration.seconds(0.5), mainMenu);
				t2.setToX(secondaryMenu.getTranslateX());

				// Play the transition animations
				t1.play();
				t2.play();

				// Stop the animation
				t1.setOnFinished(event -> getChildren().remove(secondaryMenu));
			}
		}
		private void sceneXY(VBox menu, int x, int y) {
			//This method sets dimensions for the menus of type VBox
			menu.setTranslateX(x);
			menu.setTranslateY(y);
		}
		
		private Text userN = new Text();
		
		private void textFieldCreator(TextField text, int x, int y) {
			//This method creates custom Text Fields
			text.setTranslateX(x);
			text.setTranslateY(y);
			text.setOpacity(0.75);
			text.setFont(text.getFont().font(13));
			text.setMaxWidth(170);
			text.setMaxHeight(45);
		}
		
		private void labelCreator(Label label, int x, int y) {
			//This method creates custom labels
			label.setTextFill(Color.BLUE);
			label.setFont(label.getFont().font(19));
			label.setTranslateX(x);
			label.setTranslateY(y);
		}
		
		public GameMenu() throws IOException{
			VBox loginMenu = new VBox(10);
			VBox createAccountMenu = new VBox(10);
			VBox mainMenu = new VBox(10);
			VBox settingsMenu = new VBox(10);
			VBox soundMenu = new VBox(10);
			VBox accountMenu = new VBox(10);
			VBox playMenu = new VBox(10);
			VBox multiPlayerMenu = new VBox(10);
			VBox createRoom = new VBox(10);
			VBox joinRoom = new VBox(10);
			VBox passwordMenu = new VBox(10);
			VBox customizeMenu = new VBox(10);
			VBox levelMenu = new VBox(10);
			VBox helpMenu = new VBox(10);
			VBox controlMenu = new VBox(10);

			final int offset = 400;

			sceneXY(loginMenu,500, 200);
			sceneXY(createAccountMenu, 500, 200);
			sceneXY(mainMenu, 250, 190);
			sceneXY(settingsMenu, 400, 200);
			sceneXY(soundMenu, 400, 200);
			sceneXY(accountMenu, 400, 200);
			sceneXY(playMenu, 400, 200);
			sceneXY(multiPlayerMenu, 400, 200);
			sceneXY(createRoom, 400, 200);
			sceneXY(joinRoom, 400, 200);
			sceneXY(passwordMenu, 400, 200);
			sceneXY(customizeMenu, 350, 200);
			sceneXY(levelMenu, 400, 100);
			sceneXY(helpMenu, 400, 200);
			sceneXY(controlMenu, 400, 200);

			// Login Menu
			MenuButton bLogin = new MenuButton("Login");
			bLogin.setTranslateX(-120);
			bLogin.setTranslateY(140);

			MenuButton bCreateAcc = new MenuButton("Create Account");
			bCreateAcc.setTranslateX(145);
			bCreateAcc.setTranslateY(97);	
			bCreateAcc.setOnMouseClicked(e -> transition(loginMenu, createAccountMenu, true));

			Label lUserName = new Label("User Name:");
			labelCreator(lUserName, -10, -25);

			TextField userTextField = new TextField();
			textFieldCreator(userTextField, 110, -65);
			
			Label lPassword = new Label("Password:");
			labelCreator(lPassword, -10, -60);
			
			PasswordField pwBox = new PasswordField();		
			textFieldCreator(pwBox, 110, -100);
			
			bLogin.setOnMouseClicked(e -> {
				String checkUser = userTextField.getText().toString();
				String checkPassword = pwBox.getText();
				//Added the next 3 lines in order to display the user's name in the account sub-menu
				userN.setText("Welcome back, " + checkUser);
				userN.setFont(Font.font(null, FontWeight.BOLD, 27));
				userN.setFill(Color.WHITE);
				userN.setStroke(Color.BLACK);
				
				if (alert(checkUser, checkPassword) == false) {
					transition(loginMenu, mainMenu, true);
				}
			});	

			loginMenu.getChildren().addAll(bLogin, bCreateAcc, lUserName, userTextField, lPassword, pwBox);

			// Create Account Menu
			Label lRegisterUserName = new Label("User Name:");
			labelCreator(lRegisterUserName, 0, 35);
			
			TextField registerUserTextField = new TextField();
			textFieldCreator(registerUserTextField, 120, -5);

			Label lRegisterEmail = new Label("Email:");
			labelCreator(lRegisterEmail, 0, 0);
			
			TextField registerEmailTextField = new TextField();
			textFieldCreator(registerEmailTextField, 120, -40);

			Label lRegisterPassword = new Label("Password:");
			labelCreator(lRegisterPassword, 0, -35);
			
			PasswordField pwRegisterBox = new PasswordField();
			textFieldCreator(pwRegisterBox, 120, -75);
			
			MenuButton bRegister = new MenuButton("Register");
			bRegister.setTranslateY(-70);
			bRegister.setTranslateX(20);
			bRegister.setOnMouseClicked(e -> {
				String checkUser = registerUserTextField.getText().toString();
				String checkPassword = pwRegisterBox.getText();

				if (alert(checkUser, checkPassword) == false) {
					transition(createAccountMenu, mainMenu, true);
				}
			});
			
			MenuButton bRegisterBack = new MenuButton("Back");
			bRegisterBack.setTranslateY(-70);
			bRegisterBack.setTranslateX(20);
			bRegisterBack.setOnMouseClicked(e -> transition(loginMenu, createAccountMenu, false));

			createAccountMenu.getChildren().addAll(lRegisterUserName, registerUserTextField, lRegisterEmail,
					registerEmailTextField, lRegisterPassword, pwRegisterBox, bRegister, bRegisterBack);

			// Main Menu
			MenuButton bPlay = new MenuButton("Play");
			bPlay.setOnMouseClicked(e -> transition(mainMenu, playMenu, true));
			// Play Menu
			MenuButton bSinglePlayer = new MenuButton("Single Player");
			bSinglePlayer.setOnMouseClicked(e -> {
				FadeTransition fade = new FadeTransition(Duration.seconds(0.5), this);
				fade.setFromValue(1);
				fade.setToValue(0);
				fade.setOnFinished(event -> setVisible(false));
				fade.play();
				StartGame start = new StartGame();
				try {
					start.LevelOne(primary,space);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});

			MenuButton bMultAI = new MenuButton("Player vs AI");
			bMultAI.setOnMousePressed(e -> {
					FadeTransition fade = new FadeTransition(Duration.seconds(0.5), this);
					fade.setFromValue(1);
					fade.setToValue(0);
					fade.setOnFinished(event -> setVisible(false));
					fade.play();
					//Player vs ai game
			});
			
			MenuButton bMultiPlayer = new MenuButton("MultiPlayer");
			bMultiPlayer.setOnMouseClicked(e -> transition(playMenu, multiPlayerMenu, true));
		
			MenuButton bPlayBack = new MenuButton("Back");
			bPlayBack.setOnMouseClicked(e -> transition(mainMenu, playMenu, false));

				
			MenuButton bLevel = new MenuButton("Level");
			bLevel.setOnMouseClicked(e -> transition(mainMenu, levelMenu, true));
			//Level Menu
			MenuButton level1 = new MenuButton("Level 1");
			level1.setOnMousePressed(e-> {
				FadeTransition fade = new FadeTransition(Duration.seconds(0.5), this);
				fade.setFromValue(1);
				fade.setToValue(0);
				fade.setOnFinished(event -> setVisible(false));
				fade.play();
				StartGame start = new StartGame();
				try {
					start.LevelOne(primary,space);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			MenuButton level2 = new MenuButton("Level 2");
			level2.setOnMousePressed(e ->{
				FadeTransition fade = new FadeTransition(Duration.seconds(0.5), this);
				fade.setFromValue(1);
				fade.setToValue(0);
				fade.setOnFinished(event -> setVisible(false));
				fade.play();
				LevelTwo start = new LevelTwo();
		        	  try {
						start.LevelTwo(primary, 1, 0, 2,space);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			});
			MenuButton level3 = new MenuButton("Level 3");
			level3.setOnMousePressed(e -> {
				FadeTransition fade = new FadeTransition(Duration.seconds(0.5), this);
				fade.setFromValue(1);
				fade.setToValue(0);
				fade.setOnFinished(event -> setVisible(false));
				fade.play();
				LevelThree start = new LevelThree();
	        	  try {
					start.LevelThree(primary, 1, 0, 2,space);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			MenuButton level4 = new MenuButton("Level 4");
			level4.setOnMousePressed(e -> {
				FadeTransition fade = new FadeTransition(Duration.seconds(0.5), this);
				fade.setFromValue(1);
				fade.setToValue(0);
				fade.setOnFinished(event -> setVisible(false));
				fade.play();
				LevelFour start = new LevelFour();
	        	  try {
					start.LevelFour(primary, 1, 0, 2,space);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});
			MenuButton level5 = new MenuButton("Level 5");
			level5.setOnMousePressed(e -> {
				FadeTransition fade = new FadeTransition(Duration.seconds(0.5), this);
				fade.setFromValue(1);
				fade.setToValue(0);
				fade.setOnFinished(event -> setVisible(false));
				fade.play();
				LevelFive start = new LevelFive();
	        	  try {
					start.LevelFive(primary, 1, 0, 2,space);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			MenuButton level6 = new MenuButton("Level 6");
			level6.setOnMousePressed(e -> {
				FadeTransition fade = new FadeTransition(Duration.seconds(0.5), this);
				fade.setFromValue(1);
				fade.setToValue(0);
				fade.setOnFinished(event -> setVisible(false));
				fade.play();
				LevelSix start = new LevelSix();
		        	  try {
						start.LevelSix(primary , 1, 0, 2,space);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			});
			MenuButton level7 = new MenuButton("Level 7");
			level7.setOnMousePressed(e -> {
				FadeTransition fade = new FadeTransition(Duration.seconds(0.5), this);
				fade.setFromValue(1);
				fade.setToValue(0);
				fade.setOnFinished(event -> setVisible(false));
				fade.play();
				LevelSeven start = new LevelSeven();
	        	  try {
					start.LevelSeven(primary, 1, 0, 2,space);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});
			MenuButton level8 = new MenuButton("Level 8");
			level8.setOnMousePressed(e -> {
				FadeTransition fade = new FadeTransition(Duration.seconds(0.5), this);
				fade.setFromValue(1);
				fade.setToValue(0);
				fade.setOnFinished(event -> setVisible(false));
				fade.play();
				LevelEight start = new LevelEight();
		        	  try {
						start.LevelEight(primary, 1, 0, 2,space);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			});
			MenuButton level9 = new MenuButton("Level 9");
			level9.setOnMousePressed(e -> {
				FadeTransition fade = new FadeTransition(Duration.seconds(0.5), this);
				fade.setFromValue(1);
				fade.setToValue(0);
				fade.setOnFinished(event -> setVisible(false));
				fade.play();
				LevelNine start = new LevelNine();
	        	  try {
					start.LevelNine(primary, 1, 0, 2,space);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});
			MenuButton level10 = new MenuButton("Level 10");
			level10.setOnMousePressed(e -> {
				FadeTransition fade = new FadeTransition(Duration.seconds(0.5), this);
				fade.setFromValue(1);
				fade.setToValue(0);
				fade.setOnFinished(event -> setVisible(false));
				fade.play();
				BossLevel start = new BossLevel();
	        	  try {
					start.BossLevel(primary , 1, 0, 2,space);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});
			MenuButton bLevelBack = new MenuButton("Back");
			bLevelBack.setOnMouseClicked(e -> transition(mainMenu, levelMenu, false));
			levelMenu.getChildren().addAll(level1, level2, level3, level4, level5, level6, level7, level8, level9, level10, bLevelBack);
			
			MenuButton bCustomize = new MenuButton("Customize");
			bCustomize.setOnMouseClicked(e -> transition(mainMenu, customizeMenu, true));
			MenuButton bChooseCustomize = new MenuButton("Select");
			bChooseCustomize.setOnMousePressed(e ->{
				
				space = spaceshipNumber;
				transition(mainMenu, customizeMenu, false);
			});
			//bChooseCustomize.setOnMousePressed(e -> transition(mainMenu, customizeMenu, false));
			MenuButton bBackCustomize = new MenuButton("Back");
			bBackCustomize.setOnMousePressed(e -> transition(mainMenu, customizeMenu, false));
			InputStream is = Files.newInputStream(Paths.get("resources/sp1.png"));
	        Image sp = new Image(is);
	        is.close();
	        ImageView selectedImage = new ImageView(sp); 
	      
	        selectedImage.setTranslateX(180);
	        selectedImage.setTranslateY(-120);
	        selectedImage.setFitHeight(300);
	        selectedImage.setFitWidth(380);
	        
	        Text t = new Text ();
	        
	        t.setFill(Color.BLACK);
	        t.setFont(Font.font("Ariel", 14));
	        t.setTranslateX(-240);
	        t.setTranslateY(-60);
 
	        //list of the new ships 
	        Image ship_1 = new Image(Files.newInputStream(Paths.get("resources/sp1.png")));
			Image ship_2 = new Image(Files.newInputStream(Paths.get("resources/sp2.png")));
			Image ship_3 = new Image(Files.newInputStream(Paths.get("resources/sp3.png")));
			Image ship_4 = new Image(Files.newInputStream(Paths.get("resources/sp4.png")));
			Image ship_5 = new Image(Files.newInputStream(Paths.get("resources/sp5.png")));
			Image[] listOfImages = {ship_1,ship_2,ship_3,ship_4,ship_5};
			ListView<String> list = new ListView<String>();
			list.setStyle("-fx-background-color: transparent;");
			list.setPrefHeight(530);
			list.setPrefWidth(-80);
			list.setTranslateX(-400);
			list.setTranslateY(-500);
			 ObservableList<String> items =FXCollections.observableArrayList (
		                "Gallactica","The Wrangler", "General Fury", "Ram-C", "Appollo");
			
			list.setEditable(true);
			list.setItems(items);
			list.setCellFactory(param -> new ListCell<String>() {
	        ImageView imageView = new ImageView();
	            @Override
	            public void updateItem(String name, boolean empty) {
	                super.updateItem(name, empty); 
	                if (empty) {
	                    setText(null);
	                    setGraphic(null);
	                } else {
	                    if(name.equals("Gallactica")) {
	                    	imageView.setImage(listOfImages[0]);
	                    	imageView.setFitHeight(100);
	                    	imageView.setFitWidth(100);         	
	                    } else if(name.equals("The Wrangler")) {
	                    	imageView.setImage(listOfImages[1]);
	                    	imageView.setFitHeight(100);
	                    	imageView.setFitWidth(100);
	                    } else if(name.equals("General Fury")) {
	                    	imageView.setImage(listOfImages[2]);
	                    	imageView.setFitHeight(100);
	                    	imageView.setFitWidth(100);	
	                    } else if(name.equals("Ram-C")) {
	                    	imageView.setImage(listOfImages[3]);
	                    	imageView.setFitHeight(100);
	                    	imageView.setFitWidth(100);	
	                    } else if(name.equals("Appollo")) {
	                    	imageView.setImage(listOfImages[4]);
	                    	imageView.setFitHeight(100);
	                    	imageView.setFitWidth(100);	
	                    }
	                    setText(name);
	                    setGraphic(imageView);
	                }
	            }    
	        });	
			
			list.setOnMouseClicked(e -> {
                if(list.getSelectionModel().getSelectedItem()=="Gallactica") {
                	selectedImage.setImage(ship_1);
                	spaceshipNumber = 1;
                	t.setText("Ship1 description");
                	}
                else if(list.getSelectionModel().getSelectedItem()=="Appollo") {
                	selectedImage.setImage(ship_5);
                	spaceshipNumber = 5;
                	t.setText("Ship5 description");
                }
				else if(list.getSelectionModel().getSelectedItem()=="Ram-C") {
					selectedImage.setImage(ship_4);
					spaceshipNumber = 4;
					t.setText("Ship4 description");
				}
				else if(list.getSelectionModel().getSelectedItem()=="General Fury") {
					selectedImage.setImage(ship_3);
					spaceshipNumber = 3;
					t.setText("Ship3 descripstion");
				}
				else if(list.getSelectionModel().getSelectedItem()=="The Wrangler") {
					selectedImage.setImage(ship_2);
					spaceshipNumber = 2;
					t.setText("Ship2 description");
				}
				
			});
	        customizeMenu.getChildren().addAll(selectedImage,bChooseCustomize, bBackCustomize, t, list);

			MenuButton bSettings = new MenuButton("Settings");
			bSettings.setOnMouseClicked(e -> transition(mainMenu, settingsMenu, true));

			MenuButton bQuit = new MenuButton("Quit");
			bQuit.setOnMouseClicked(e -> System.exit(0));

			MenuButton bBackLogin = new MenuButton("Back");
			bBackLogin.setOnMouseClicked(e -> transition(loginMenu, mainMenu, false));

			// Settings Menu
			MenuButton bControls = new MenuButton("Controls");
			bControls.setOnMousePressed(e -> transition(settingsMenu, controlMenu, true));
			Text controlT = new Text();
			controlT.setText("Single Player Mode:\n    move around with arrow keys, shoot with spacebar."
					+ "\nMultiPlayer Mode: \n    Player 1: same as Singe Player, \n    PLayer 2: move around with W S A D, and shoot \n             with spacebar.");
			controlT.setFont(Font.font(null, FontWeight.BOLD, 40));
			controlT.setStroke(Color.BLACK);
			controlT.setFill(Color.FLORALWHITE);
			controlT.setTranslateX(-400);
			
			MenuButton bControlBack = new MenuButton("Back");
			bControlBack.setOnMousePressed(e -> transition(settingsMenu, controlMenu, false));
			bControlBack.setTranslateX(-400);
			controlMenu.getChildren().addAll(controlT, bControlBack);
			
			// Account Menu
			MenuButton bAccount = new MenuButton("Account");
			bAccount.setOnMouseClicked(e -> transition(settingsMenu, accountMenu, true));
			MenuButton bAccountBack = new MenuButton("Back");
			bAccountBack.setOnMouseClicked(e -> transition(settingsMenu, accountMenu, false));
			MenuButton bAccountPassword = new MenuButton("Change Password");
			bAccountPassword.setOnMouseClicked(e-> transition(accountMenu, passwordMenu, true));
			Label currentPwd = new Label("Current Password:");
			labelCreator(currentPwd, -50, 30);
			PasswordField currentPwdText = new PasswordField();
			textFieldCreator(currentPwdText, 110, -7);
			Label newPwd = new Label("New Password: ");
			labelCreator(newPwd, -50, -5);
			
			PasswordField newPwdText = new PasswordField();
			textFieldCreator(newPwdText, 110, -45);
			Label reNewPwd = new Label("Re-Enter the Password: ");
			labelCreator(reNewPwd, -90, -45);
			
			PasswordField reNewPwdText = new PasswordField();
			textFieldCreator(reNewPwdText, 110, -83);
			MenuButton bSaveAcc = new MenuButton("Save");
			bSaveAcc.setTranslateY(-70);
			MenuButton bBackAccPwd = new MenuButton("Back");
			bBackAccPwd.setTranslateY(-72);
			bBackAccPwd.setOnMouseClicked(e -> transition(accountMenu, passwordMenu, false));
			passwordMenu.getChildren().addAll(currentPwd, currentPwdText, newPwd, newPwdText, reNewPwd, reNewPwdText,bSaveAcc, bBackAccPwd);

			// Sound Menu
			MenuButton bSound = new MenuButton("Sound");
			MenuButton bSoundOn = new MenuButton("On");
			bSoundOn.setOnMouseClicked(e -> mediaPlayer.play());
			MenuButton bSoundOff = new MenuButton("Off");
			bSoundOff.setOnMouseClicked(e -> mediaPlayer.pause());
			bSound.setOnMouseClicked(e -> transition(settingsMenu, soundMenu, true));
			MenuButton bSoundBack = new MenuButton("Back");
			bSoundBack.setOnMouseClicked(e -> transition(settingsMenu, soundMenu, false));

			MenuButton bHelp = new MenuButton("Help");
			bHelp.setOnMousePressed(e -> transition(settingsMenu, helpMenu, true));
			Text helpT = new Text();
			helpT.setText("Welcome to the dark side. In order to survive, you shall make"
					+ "\n your way through the galaxy's most notorious and dangerous"
					+ "\n criminals. Shoot or be the one shot at.\n Good luck, soldier!");
			helpT.setFont(Font.font(null, FontWeight.BOLD, 40));
			helpT.setStroke(Color.BLACK);
			helpT.setFill(Color.FLORALWHITE);
			helpT.setTranslateX(-400);
		
			MenuButton bHelpBack = new MenuButton("Back");
			bHelpBack.setOnMousePressed(e -> transition(settingsMenu, helpMenu, false));
			bHelpBack.setTranslateX(-500);

			helpMenu.getChildren().addAll(helpT, bHelpBack);

			MenuButton bBackSettings = new MenuButton("Back");
			bBackSettings.setOnMouseClicked(e -> transition(mainMenu, settingsMenu, false));

			mainMenu.getChildren().addAll(bPlay, bLevel, bSettings, bCustomize, bBackLogin, bQuit);
			settingsMenu.getChildren().addAll(bAccount, bControls, bSound, bHelp, bBackSettings);
			soundMenu.getChildren().addAll(bSoundOn, bSoundOff, bSoundBack);
			accountMenu.getChildren().addAll(userN,bAccountPassword, bAccountBack);
			playMenu.getChildren().addAll(bSinglePlayer, bMultiPlayer, bMultAI, bPlayBack);

			Rectangle background = new Rectangle(1280, 720);
			background.setFill(Color.GREY);
			background.setOpacity(0.4);

			getChildren().addAll(background, loginMenu);

			// Multiplayer Menu
			MenuButton bCreateRoom = new MenuButton("Create Room");
			bCreateRoom.setOnMousePressed(e -> transition(multiPlayerMenu, createRoom, true));

			MenuButton bJoinRoom = new MenuButton("Join a Room");
			bJoinRoom.setOnMouseClicked(e -> transition(multiPlayerMenu, joinRoom, true));

			MenuButton bMultiPlayerMenuBack = new MenuButton("Back");
			bMultiPlayerMenuBack.setOnMousePressed(e -> transition(playMenu, multiPlayerMenu, false));

			multiPlayerMenu.getChildren().addAll(bCreateRoom, bJoinRoom, bMultiPlayerMenuBack);

			// create room menu
			Label nickName = new Label("Name:");
			labelCreator(nickName, -10, 70);
			
			TextField inputNickName = new TextField();
			textFieldCreator(inputNickName, 100, 30);
			
			Label roomNumber = new Label("Room Number:");
			labelCreator(roomNumber, -50, 30);

			TextField inputNumber = new TextField();
			textFieldCreator(inputNumber, 100, -7);

			MenuButton bStart = new MenuButton("Start");
			bStart.setOnMouseClicked(e -> {
				int portNumber = Integer.parseInt(inputNumber.getText().toString());
				String textNickName = inputNickName.getText();
				Stage stage = new Stage();
				MultiPlayerGame start = new MultiPlayerGame();
				start.initAndStart(stage, 0, textNickName, portNumber, "localhost",mediaPlayer);

			});

			MenuButton bCreateMenuButton = new MenuButton("Back");
			bCreateMenuButton.setOnMouseClicked(e -> transition(multiPlayerMenu, createRoom, false));

			createRoom.getChildren().addAll(nickName, inputNickName, roomNumber, inputNumber, bStart, bCreateMenuButton);

			// join room
			Label roomNumber2 = new Label("Room Number:");
			labelCreator(roomNumber2, -50, 30);

			TextField inputNumber2 = new TextField();
			textFieldCreator(inputNumber2, 100, -7);

			Label nickName2 = new Label("Name:");
			labelCreator(nickName2, -10, 70);

			TextField inputNickName2 = new TextField();
			textFieldCreator(inputNickName2, 100, 30);
			
			Label ipAddress = new Label("IP Address:");
			labelCreator(ipAddress, -10, -5);

			TextField inputIpAddress = new TextField();
			textFieldCreator(inputIpAddress, 100, -42);

			MenuButton bJoin = new MenuButton("Join");
			bJoin.setTranslateY(-46);
			bJoin.setOnMouseClicked(e -> {
				int portNumber = Integer.parseInt(inputNumber2.getText().toString());
				String textNickName = inputNickName2.getText();
				String textIpAddress = inputIpAddress.getText();
				Stage stage = new Stage();
				MultiPlayerGame start = new MultiPlayerGame();

				start.initAndStart(stage, 1, textNickName, portNumber, textIpAddress,mediaPlayer);
			});

			MenuButton bJoinRoomBack = new MenuButton("Back");
			bJoinRoomBack.setTranslateY(-47);
			bJoinRoomBack.setOnMouseClicked(e -> transition(multiPlayerMenu, joinRoom, false));

			joinRoom.getChildren().addAll(nickName2, inputNickName2, roomNumber2, inputNumber2, ipAddress, inputIpAddress, bJoin, bJoinRoomBack);

		}
	}

}
