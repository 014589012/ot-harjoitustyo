
package ui;

import dao.FileEventDao;
import dao.FileUserDao;
import domain.Event;
import domain.EventService;
import java.text.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;




public class Otkalenteri extends Application{
    private EventService eventService;

    private Scene eventScene;
    private Scene eventScenePrivate;
    private Scene newUserScene;
    private Scene loginScene;

    private VBox eventNodes;
    private Label menuLabel = new Label();

    public static void main(String[] args){
        launch(args);
    }


    @Override
    public void init() throws Exception {
        FileUserDao userDao = new FileUserDao();
        FileEventDao eventDao = new FileEventDao();
        eventService = new EventService(eventDao, userDao);
    }

    public Node createEventNode(Event ev) {
        HBox box = new HBox(10);
        Label label  = new Label(ev.getName());
        label.setMinHeight(28);
        Label dt  = new Label(ev.getDateAsString());
        Button button = new Button("delete");
        button.setOnAction(e->{
            eventService.markDone(ev);
            redrawEventlist();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        box.setPadding(new Insets(0,5,0,5));
        
        if(ev.getUser().equals(eventService.getLoggedUser())){
            box.getChildren().addAll(label, spacer,dt, button);
        } else {
            box.getChildren().addAll(label,spacer,dt);
        }
        return box;
    }

    public void redrawEventlist() {
        eventNodes.getChildren().clear();

        List<Event> upcomingEv = eventService.getUpcomingPublic();
        upcomingEv.forEach(event->{
            eventNodes.getChildren().add(createEventNode(event));
        });
    }

    public void redrawEventlistPrivate() {
        eventNodes.getChildren().clear();

        List<Event> upcomingEv = eventService.getUpcomingPrivate(eventService.getLoggedUser());
        upcomingEv.forEach(event->{
            eventNodes.getChildren().add(createEventNode(event));
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
        TextField passwordInput = new TextField();

        inputPane.getChildren().addAll(loginLabel, usernameInput);
        inputPane2.getChildren().addAll(loginLabelPassword, passwordInput);
        Label loginMessage = new Label();

        Button loginButton = new Button("Login");
        Button createButton = new Button("Create new user");
        loginButton.setOnAction(e->{
            String username = usernameInput.getText();
            String password = passwordInput.getText();
            menuLabel.setText("welcome " + username);
            if ( eventService.login(username,password) ){
                loginMessage.setText("");
                redrawEventlist();
                primaryStage.setScene(eventScene);
                usernameInput.setText("");
            } else {
                loginMessage.setText("Username or password incorrect");
                loginMessage.setTextFill(Color.RED);
            }
        });

        createButton.setOnAction(e->{
            usernameInput.setText("");
            primaryStage.setScene(newUserScene);
        });

        loginPane.getChildren().addAll(loginMessage, inputPane, inputPane2, loginButton, createButton);

        loginScene = new Scene(loginPane, 300, 250);

        // new createNewUserScene

        VBox newUserPane = new VBox(10);

        HBox newUsernamePane = new HBox(10);
        newUsernamePane.setPadding(new Insets(10));
        TextField newUsernameInput = new TextField();
        Label newUsernameLabel = new Label("Username");
        newUsernameLabel.setPrefWidth(100);
        newUsernamePane.getChildren().addAll(newUsernameLabel, newUsernameInput);

        HBox newPasswordPane = new HBox(10);
        newPasswordPane.setPadding(new Insets(10));
        TextField newPasswordInput = new TextField();
        Label newPasswordLabel = new Label("Password");
        newPasswordLabel.setPrefWidth(100);
        newPasswordPane.getChildren().addAll(newPasswordLabel, newPasswordInput);

        Label userCreationMessage = new Label();

        Button createNewUserButton = new Button("Create");
        createNewUserButton.setPadding(new Insets(10));

        createNewUserButton.setOnAction(e->{
            String username = newUsernameInput.getText();
            String password = newPasswordInput.getText();

            if ( eventService.createUser(username, password) ){
                userCreationMessage.setText("");
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

        });

        newUserPane.getChildren().addAll(userCreationMessage, newUsernamePane, newPasswordPane, createNewUserButton);

        newUserScene = new Scene(newUserPane, 300, 250);

        // Public scene

        ScrollPane eventScollbar = new ScrollPane();
        BorderPane mainPane = new BorderPane(eventScollbar);
        eventScene = new Scene(mainPane, 300, 250);

        HBox messagebox = new HBox(10);
        Label whichSceneMessage = new Label();
        whichSceneMessage.setText("PUBLIC EVENTS");
        whichSceneMessage.setTextFill(Color.GREEN);
        messagebox.getChildren().addAll(whichSceneMessage);
        
        HBox menuPane = new HBox(10);
        Region menuSpacer = new Region();
        HBox.setHgrow(menuSpacer, Priority.ALWAYS);
        Button logoutButton = new Button("Logout");
        Button toPrivateButton = new Button("Private");
        menuPane.getChildren().addAll(menuLabel, menuSpacer, toPrivateButton, logoutButton);
        logoutButton.setOnAction(e->{
            eventService.logout();
            primaryStage.setScene(loginScene);
        });
        toPrivateButton.setOnAction(e->{
            primaryStage.setScene(eventScenePrivate);
        });
        
        VBox vbox = new VBox(20);
        vbox.setStyle("-fx-padding: 10;");
        DatePicker checkInDatePicker = new DatePicker();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        Label checkInlabel = new Label("Date");
        gridPane.add(checkInlabel, 0, 0);
        GridPane.setHalignment(checkInlabel, HPos.LEFT);
        gridPane.add(checkInDatePicker, 0, 1);

        HBox createForm = new HBox(10);
        Button createEvent = new Button("Create");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        TextField newEventInput = new TextField();
        createForm.getChildren().addAll(newEventInput, gridPane, spacer, createEvent);

        eventNodes = new VBox(10);
        eventNodes.setMaxWidth(400);
        eventNodes.setMinWidth(250);
        redrawEventlist();

        eventScollbar.setContent(eventNodes);
        mainPane.setBottom(createForm);
        mainPane.setLeft(messagebox);
        mainPane.setTop(menuPane);

        createEvent.setOnAction(e->{
            try {
                eventService.createEvent(newEventInput.getText(),checkInDatePicker.getValue().toString(),false);
            } catch (ParseException ex) {
                Logger.getLogger(Otkalenteri.class.getName()).log(Level.SEVERE, null, ex);
            }
            newEventInput.setText("");
            checkInDatePicker.setValue(null);
            redrawEventlist();
        });

//        // Private scene
//
//        ScrollPane eventScollbar2 = new ScrollPane();
//        BorderPane mainPanePrive = new BorderPane(eventScollbar2);
//        eventScenePrivate = new Scene(mainPanePrive, 300, 250);
//
//        HBox menuPane2 = new HBox(10);
//        Region menuSpacer2 = new Region();
//        HBox.setHgrow(menuSpacer, Priority.ALWAYS);
//        Button logoutButton2 = new Button("Logout");
//        Button toPublicButton = new Button("Public");
//        menuPane.getChildren().addAll(menuLabel, menuSpacer2, logoutButton2);
//        logoutButton.setOnAction(e->{
//            eventService.logout();
//            primaryStage.setScene(loginScene);
//        });
//        toPublicButton.setOnAction(e->{
//            primaryStage.setScene(eventScene);
//        });
//
//        HBox createForm2 = new HBox(10);
//        Button createEvent2 = new Button("Create");
//        Region spacer2 = new Region();
//        HBox.setHgrow(spacer2, Priority.ALWAYS);
//        TextField newEventInput2 = new TextField();
//        createForm.getChildren().addAll(newEventInput2, spacer2, createEvent2);
//
//        eventNodes = new VBox(10);
//        eventNodes.setMaxWidth(280);
//        eventNodes.setMinWidth(280);
//        redrawEventlistPrivate();
//
//        eventScollbar.setContent(eventNodes);
//        mainPane.setBottom(createForm);
//        mainPane.setTop(menuPane);
//
//        createEvent.setOnAction(e->{
//            try {
//                eventService.createEvent(newEventInput.getText());
//            } catch (ParseException ex) {
//                Logger.getLogger(Otkalenteri.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            newEventInput.setText("");
//            redrawEventlist();
//        });

        // seutp primary stage

        primaryStage.setTitle("otkalenteri");
        primaryStage.setScene(loginScene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e->{
            System.out.println("closing");
            System.out.println(eventService.getLoggedUser());
            if (eventService.getLoggedUser()!=null) {
                e.consume();
            }

        });
    }

    @Override
    public void stop() {
      eventService.logout();
      System.out.println("application closing");
      System.exit(0);
    }

}

