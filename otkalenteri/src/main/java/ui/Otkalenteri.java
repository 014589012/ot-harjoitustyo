
package ui;

import dao.FileEventDao;
import dao.FileUserDao;
import domain.Event;
import domain.EventService;
import java.io.FileInputStream;
import java.text.ParseException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;




public class Otkalenteri extends Application{
    private EventService eventService;
    private boolean privv;

    private Scene eventScene;
    private Scene eventScenePrivate;
    private Scene newUserScene;
    private Scene loginScene;

    private VBox eventNodes;
    private VBox eventNodes2;
    private Label menuLabel = new Label();
    private Label menuLabel2 = new Label();

    public static void main(String[] args){
        launch(args);
    }


    @Override
    public void init() throws Exception {
        Properties properties = new Properties();

        properties.load(new FileInputStream("config.properties"));
        
        String userFile = properties.getProperty("userFile");
        String eventFile = properties.getProperty("eventFile");

        FileUserDao userDao = new FileUserDao(userFile);
        FileEventDao eventDao = new FileEventDao(eventFile, userDao);
        eventService = new EventService(eventDao, userDao);
        privv = false;
    }

    public Node createEventNode(Event ev) {
        HBox box = new HBox(10);
        Label label  = new Label(ev.getName());
        label.setMinHeight(28);
        Label dt  = new Label(ev.getDateAsString());
        Button button = new Button("Delete");
        Button infoButton = new Button("Info");
        button.setOnAction(e->{
            try {
                eventService.markDone(ev);
            } catch (Exception ex) {
                Logger.getLogger(Otkalenteri.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                redrawEventlist();
            } catch (Exception ex) {
                Logger.getLogger(Otkalenteri.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                redrawEventlistPrivate();
            } catch (Exception ex) {
                Logger.getLogger(Otkalenteri.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        infoButton.setOnAction(e->{
            method(ev);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        box.setPadding(new Insets(0,5,0,5));
        
        if(ev.getUser().equals(eventService.getLoggedUser())){
            box.getChildren().addAll(label, spacer,dt, infoButton, button);
        } else if(eventService.getLoggedUser()!=null){
            if(eventService.getLoggedUser().isAdmin()){
                box.getChildren().addAll(label, spacer,dt, infoButton, button);
            }else{
                box.getChildren().addAll(label,spacer,dt, infoButton);
            }
        }else{
            box.getChildren().addAll(label,spacer,dt,infoButton);
        }
        return box;
    }
    
    public void method(Event ev){
        if(privv){
            eventNodes2.getChildren().clear();
        }else{
            eventNodes.getChildren().clear();
        }
        TextArea textArea = new TextArea(ev.getDescription());
        textArea.setMaxSize(300, 100);
        textArea.setBorder(null);
        textArea.setEditable(false);
        if(ev.getUser().getUsername().equals(eventService.getLoggedUser().getUsername())){
            textArea.setEditable(true);
        }
        HBox htop = new HBox(10);
        Label eventNameLabel = new Label();
        eventNameLabel.setText("EVENT: " + ev.getName());
        Region spacer3 = new Region();
        HBox.setHgrow(spacer3, Priority.NEVER);
        Label eventCreatorLabel = new Label();
        eventCreatorLabel.setText("CREATOR: " + ev.getUser().getUsername());
        Button backOutButton = new Button("Exit Info-page");
        if(ev.getUser().getUsername().equals(eventService.getLoggedUser().getUsername())){
            backOutButton.setText("Save and Exit Info-page");
        }
        backOutButton.setOnAction(e->{
            try {
                eventService.addDescriptionToEvent(textArea.getText(), ev);
            } catch (Exception ex) {
                Logger.getLogger(Otkalenteri.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                redrawEventlist();
            } catch (Exception ex) {
                Logger.getLogger(Otkalenteri.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                redrawEventlistPrivate();
            } catch (Exception ex) {
                Logger.getLogger(Otkalenteri.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        htop.getChildren().addAll(eventNameLabel,spacer3,eventCreatorLabel,backOutButton);
        Label descriptionLabel = new Label();
        descriptionLabel.setText("Description");
        HBox hdown = new HBox(10);
        if(ev.getUser().getUsername().equals(eventService.getLoggedUser().getUsername())){
            Label hdownLabel = new Label();
            hdownLabel.setText("You created this event so you may edit the details.");
            hdownLabel.setTextFill(Color.GREEN);
            hdown.getChildren().addAll(hdownLabel);
        }
        if(privv){
            eventNodes2.getChildren().addAll(htop,descriptionLabel,textArea,hdown);
        }else{
            eventNodes.getChildren().addAll(htop,descriptionLabel,textArea,hdown);
        }
        
    }

    public void redrawEventlist() throws Exception {
        eventNodes.getChildren().clear();

        List<Event> upcomingEv = eventService.getUpcomingPublic();
        upcomingEv.forEach(event->{
            eventNodes.getChildren().add(createEventNode(event));
        });
    }

    public void redrawEventlistPrivate() throws Exception {
        eventNodes2.getChildren().clear();

        List<Event> upcomingEv = eventService.getUpcomingPrivate(eventService.getLoggedUser());
        upcomingEv.forEach(event->{
            eventNodes2.getChildren().add(createEventNode(event));
        });
    }

    @Override
    public void start(Stage primaryStage){
        // login scene

        VBox loginPane = new VBox(10);
        HBox inputPane = new HBox(10);
        HBox inputPane2 = new HBox(10);
        loginPane.setPadding(new Insets(10));
        Label loginLabel = new Label("Username");
        TextField usernameInput = new TextField();
        Label loginLabelPassword = new Label("Password");
        PasswordField passwordInput = new PasswordField();
        
        
        TextField passwordInputText = new TextField();
        CheckBox checkBox1 = new CheckBox("Show password");
        checkBox1.setPadding(new Insets(10));
        passwordInput.managedProperty().bind(checkBox1.selectedProperty());
        passwordInputText.visibleProperty().bind(checkBox1.selectedProperty());
        passwordInput.managedProperty().bind(checkBox1.selectedProperty().not());
        passwordInput.visibleProperty().bind(checkBox1.selectedProperty().not());
        passwordInputText.textProperty().bindBidirectional(passwordInput.textProperty());
        
        inputPane.getChildren().addAll(loginLabel, usernameInput);
        inputPane2.getChildren().addAll(loginLabelPassword, passwordInput, passwordInputText);
        Label loginMessage = new Label();

        Button loginButton = new Button("Login");
        Button createButton = new Button("Create new user");
        loginButton.setOnAction(e->{
            String username = usernameInput.getText();
            String password = passwordInput.getText();
            menuLabel.setText("welcome " + username);
            menuLabel2.setText("welcome " + username);
            if ( eventService.login(username,password) ){
                loginMessage.setText("");
                try {
                    redrawEventlist();
                } catch (Exception ex) {
                    Logger.getLogger(Otkalenteri.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(eventService.getLoggedUser().isAdmin()){
                    menuLabel.setText("ADMIN: " + username);
                    menuLabel2.setText("ADMIN: " + username);
                }
                primaryStage.setScene(eventScene);
                usernameInput.setText("");
                passwordInput.setText("");
            } else {
                loginMessage.setText("Username or password incorrect");
                loginMessage.setTextFill(Color.RED);
            }
        });

        createButton.setOnAction(e->{
            usernameInput.setText("");
            loginMessage.setText("");
            passwordInput.setText("");
            primaryStage.setScene(newUserScene);
        });

        loginPane.getChildren().addAll(loginMessage, inputPane, inputPane2, checkBox1, loginButton, createButton);

        loginScene = new Scene(loginPane, 500, 300);

        // new createNewUserScene

        VBox newUserPane = new VBox(5);
        newUserPane.setPadding(new Insets(10));

        HBox newUsernamePane = new HBox(10);
        newUsernamePane.setPadding(new Insets(10));
        TextField newUsernameInput = new TextField();
        Label newUsernameLabel = new Label("Username");
        newUsernameLabel.setPrefWidth(100);
        newUsernamePane.getChildren().addAll(newUsernameLabel, newUsernameInput);

        HBox newPasswordPane = new HBox(10);
        newPasswordPane.setPadding(new Insets(10));
        PasswordField newPasswordInput = new PasswordField();
        Label newPasswordLabel = new Label("Password");
        newPasswordLabel.setPrefWidth(100);
        
        TextField newPasswordInputText = new TextField();
        CheckBox checkBox = new CheckBox("Show password");
        checkBox.setPadding(new Insets(10));
        newPasswordInputText.managedProperty().bind(checkBox.selectedProperty());
        newPasswordInputText.visibleProperty().bind(checkBox.selectedProperty());
        newPasswordInput.managedProperty().bind(checkBox.selectedProperty().not());
        newPasswordInput.visibleProperty().bind(checkBox.selectedProperty().not());
        newPasswordInputText.textProperty().bindBidirectional(newPasswordInput.textProperty());
        
        newPasswordPane.getChildren().addAll(newPasswordLabel, newPasswordInput, newPasswordInputText);

        Label userCreationMessage = new Label();

        Button createNewUserButton = new Button("Create");
        Button goBackButton = new Button("Cancel");

        createNewUserButton.setOnAction(e->{
            String username = newUsernameInput.getText();
            String password = newPasswordInput.getText();
            try {
                if ( eventService.createUser(username, password) ){
                    userCreationMessage.setText("");
                    newUsernameInput.setText("");
                    newPasswordInput.setText("");
                    loginMessage.setText("new user created");
                    loginMessage.setTextFill(Color.GREEN);
                    primaryStage.setScene(loginScene);
                } else if ( username.length()<3 || password.length()<3 ) {
                    userCreationMessage.setText("username or password too short");
                    userCreationMessage.setTextFill(Color.RED);
                } else if ( username.length()>24 || password.length()>24 ) {
                    userCreationMessage.setText("username or password too long");
                    userCreationMessage.setTextFill(Color.RED);
                } else {
                    userCreationMessage.setText("username has to be unique");
                    userCreationMessage.setTextFill(Color.RED);
                }
            } catch (Exception ex) {
                Logger.getLogger(Otkalenteri.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        goBackButton.setOnAction(e->{
            userCreationMessage.setText("");
            loginMessage.setText("");
            newUsernameInput.setText("");
            newPasswordInput.setText("");
            primaryStage.setScene(loginScene);
        });
        

        newUserPane.getChildren().addAll(userCreationMessage, newUsernamePane, newPasswordPane, checkBox, createNewUserButton, goBackButton);

        newUserScene = new Scene(newUserPane, 500, 300);

        // Public scene
        TextField newEventInput2 = new TextField();
        Label status2 = new Label();
        DatePicker checkInDatePicker2 = new DatePicker();
        
        ScrollPane eventScollbar = new ScrollPane();
        BorderPane mainPane = new BorderPane(eventScollbar);
        eventScene = new Scene(mainPane, 500, 300);

        VBox messagebox = new VBox(10);
        messagebox.setAlignment(Pos.CENTER);
        Label whichSceneMessage = new Label();
        Label status = new Label();
        whichSceneMessage.setText("PUBLIC EVENTS");
        whichSceneMessage.setTextFill(Color.GREEN);
        messagebox.getChildren().addAll(whichSceneMessage,status);
        
        HBox menuPane = new HBox(10);
        Region menuSpacer = new Region();
        HBox.setHgrow(menuSpacer, Priority.ALWAYS);
        Button logoutButton = new Button("Logout");
        Button toPrivateButton = new Button("Private");
        menuPane.getChildren().addAll(menuLabel, menuSpacer, toPrivateButton, logoutButton);
        
        
        
        toPrivateButton.setOnAction(e->{
            privv=true;
            try {
                redrawEventlistPrivate();
            } catch (Exception ex) {
                Logger.getLogger(Otkalenteri.class.getName()).log(Level.SEVERE, null, ex);
            }
            primaryStage.setScene(eventScenePrivate);
        });
        
        VBox vbox = new VBox(20);
        vbox.setStyle("-fx-padding: 10;");
        DatePicker checkInDatePicker = new DatePicker();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        Label checkInlabel = new Label("Pick date");
        gridPane.add(checkInlabel, 0, 0);
        GridPane.setHalignment(checkInlabel, HPos.LEFT);
        gridPane.add(checkInDatePicker, 0, 1);

        HBox createForm = new HBox(10);
        Button createEvent = new Button("Create");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        TextField newEventInput = new TextField();
        Label eventName = new Label();
        eventName.setText("Event name");
        VBox eventNameField = new VBox(10);
        eventNameField.getChildren().addAll(eventName,newEventInput);
        createForm.setPadding(new Insets(0, 20, 10, 20)); 
        createForm.getChildren().addAll(eventNameField, gridPane, spacer, createEvent);
        
        logoutButton.setOnAction(e->{
            eventService.logout();
            eventNodes2.getChildren().clear();
            menuLabel.setText("");
            menuLabel2.setText("");
            newEventInput.setText("");
            newEventInput2.setText("");
            status.setText("");
            status2.setText("");
            checkInDatePicker.setValue(null);
            checkInDatePicker2.setValue(null);
            primaryStage.setScene(loginScene);
        });
        
        eventNodes = new VBox(10);
        eventNodes.setMaxWidth(500);
        eventNodes.setMinWidth(300);
        try {
            redrawEventlist();
        } catch (Exception ex) {
            Logger.getLogger(Otkalenteri.class.getName()).log(Level.SEVERE, null, ex);
        }

        eventScollbar.setContent(eventNodes);
        mainPane.setBottom(createForm);
        mainPane.setLeft(messagebox);
        mainPane.setTop(menuPane);

        createEvent.setOnAction(e->{
            try {
                boolean x = true;
                int y = 0;
                try {
                    y = eventService.getUpcomingPublic().size();
                    x =eventService.createEvent(newEventInput.getText(),checkInDatePicker.getValue().toString(),privv);
                } catch (ParseException ex) {
                    status.setText("creation failed");
                    status.setTextFill(Color.RED);
                    Logger.getLogger(Otkalenteri.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(!x){
                    status.setText("creation failed");
                    status.setTextFill(Color.RED);
                }else{
                    if(eventService.getUpcomingPublic().size()>y){
                        status.setText("");
                        status.setTextFill(Color.GREEN);
                        newEventInput.setText("");
                        checkInDatePicker.setValue(null);
                        redrawEventlist();
                    }else{
                        status.setText("Set future date");
                        status.setTextFill(Color.RED);
                    }
                }
            } catch (Exception ex) {
                status.setText("creation failed");
                status.setTextFill(Color.RED);
                Logger.getLogger(Otkalenteri.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        // Private scene

        ScrollPane eventScollbar2 = new ScrollPane();
        BorderPane mainPanePrive = new BorderPane(eventScollbar2);
        eventScenePrivate = new Scene(mainPanePrive, 500, 300);

        VBox messagebox2 = new VBox(10);
        messagebox2.setAlignment(Pos.CENTER);
        Label whichSceneMessage2 = new Label();

        whichSceneMessage2.setText("PRIVATE EVENTS");
        whichSceneMessage2.setTextFill(Color.GREEN);
        messagebox2.getChildren().addAll(whichSceneMessage2, status2);
        
        HBox menuPane2 = new HBox(10);
        Region menuSpacer2 = new Region();
        HBox.setHgrow(menuSpacer2, Priority.ALWAYS);
        Button logoutButton2 = new Button("Logout");
        Button toPublicButton = new Button("Public");
        menuPane2.getChildren().addAll(menuLabel2, menuSpacer2, toPublicButton, logoutButton2);
        
        toPublicButton.setOnAction(e->{
            privv=false;
            try {
                redrawEventlist();
            } catch (Exception ex) {
                Logger.getLogger(Otkalenteri.class.getName()).log(Level.SEVERE, null, ex);
            }
            primaryStage.setScene(eventScene);
        });

        VBox vbox2 = new VBox(20);
        vbox2.setStyle("-fx-padding: 10;");

        GridPane gridPane2 = new GridPane();
        gridPane2.setHgap(10);
        gridPane2.setVgap(10);
        Label checkInlabel2 = new Label("Pick date");
        gridPane2.add(checkInlabel2, 0, 0);
        GridPane.setHalignment(checkInlabel2, HPos.LEFT);
        gridPane2.add(checkInDatePicker2, 0, 1);
        
        HBox createForm2 = new HBox(10);
        Button createEvent2 = new Button("Create");
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        Label eventName2 = new Label();
        eventName2.setText("Event name");
        VBox eventNameField2 = new VBox(10);
        eventNameField2.getChildren().addAll(eventName2,newEventInput2);
        createForm2.setPadding(new Insets(0, 20, 10, 20)); 
        createForm2.getChildren().addAll(eventNameField2, gridPane2, spacer2, createEvent2);

        logoutButton2.setOnAction(e->{
            eventService.logout();
            eventNodes2.getChildren().clear();
            menuLabel.setText("");
            menuLabel2.setText("");
            newEventInput.setText("");
            newEventInput2.setText("");
            status.setText("");
            status2.setText("");
            checkInDatePicker.setValue(null);
            checkInDatePicker2.setValue(null);
            primaryStage.setScene(loginScene);
        });
        
        eventNodes2 = new VBox(10);
        eventNodes2.setMaxWidth(500);
        eventNodes2.setMinWidth(300);
        try {
            redrawEventlistPrivate();
        } catch (Exception ex) {
            Logger.getLogger(Otkalenteri.class.getName()).log(Level.SEVERE, null, ex);
        }

        eventScollbar2.setContent(eventNodes2);
        mainPanePrive.setBottom(createForm2);
        mainPanePrive.setLeft(messagebox2);
        mainPanePrive.setTop(menuPane2);

        createEvent2.setOnAction(e->{
            try {
                boolean x2 = true;
                int y2 = 0;
                try {
                    y2 = eventService.getUpcomingPrivate(eventService.getLoggedUser()).size();
                    x2 =eventService.createEvent(newEventInput2.getText(),checkInDatePicker2.getValue().toString(),privv);
                } catch (ParseException ex) {
                    status2.setText("creation failed");
                    status2.setTextFill(Color.RED);
                    Logger.getLogger(Otkalenteri.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(!x2){
                    status2.setText("creation failed");
                    status2.setTextFill(Color.RED);
                }else{
                    if(eventService.getUpcomingPrivate(eventService.getLoggedUser()).size()>y2){
                        status2.setText("");
                        status2.setTextFill(Color.GREEN);
                        newEventInput2.setText("");
                        checkInDatePicker2.setValue(null);
                        redrawEventlistPrivate();
                    }else{
                        status2.setText("Set future date");
                        status2.setTextFill(Color.RED);
                    }
                }
            } catch (Exception ex) {
                status2.setText("creation failed");
                status2.setTextFill(Color.RED);
                Logger.getLogger(Otkalenteri.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

//        // Info stage
//        TextArea textArea = new TextArea("hello world");
//        textArea.setEditable(false);
//        ScrollPane scrollPane3 = new ScrollPane(textArea);
//        BorderPane infoPane = new BorderPane(scrollPane3);
//        infoScene = new Scene(infoPane, 500, 300);
//        HBox htop = new HBox(10);
//        Label eventNameLabel = new Label();
//        eventNameLabel.setText("Event: " + "asd");
//        Region spacer3 = new Region();
//        HBox.setHgrow(spacer3, Priority.ALWAYS);
//        Label eventCreatorLabel = new Label();
//        eventCreatorLabel.setText("Creator: " + "dude");
//        htop.getChildren().addAll(eventNameLabel,spacer3,eventCreatorLabel);
//        infoPane.setTop(htop);
        
        // seutp primary stage

        primaryStage.setTitle("Kallen kalenteri");
        primaryStage.setScene(loginScene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e->{
            if (eventService.getLoggedUser()!=null) {
                System.out.print("Goodbye ");
                System.out.println(eventService.getLoggedUser().getUsername());
            }
            stop();
        });
    }

    @Override
    public void stop() {
      eventService.logout();
      System.out.println("Application closing");
      System.exit(0);
    }

}

