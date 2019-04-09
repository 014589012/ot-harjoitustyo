
package ui;

import dao.FileEventDao;
import dao.FileUserDao;
import domain.Event;
import domain.EventService;

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
        Button button = new Button("done");
        button.setOnAction(e->{
            eventService.markDone(ev);
            redrawEventlist();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        box.setPadding(new Insets(0,5,0,5));

        box.getChildren().addAll(label, spacer, button);
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
        loginPane.setPadding(new Insets(10));
        Label loginLabel = new Label("Username");
        TextField usernameInput = new TextField();
        Label loginLabelPassword = new Label("Password");
        TextField passwordInput = new TextField();

        inputPane.getChildren().addAll(loginLabel, usernameInput);
        inputPane.getChildren().addAll(loginLabelPassword, passwordInput);
//        inputPane.getChildren().addAll(loginLabelPassword, passwordInput);
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

        loginPane.getChildren().addAll(loginMessage, inputPane, loginButton, createButton);

        loginScene = new Scene(loginPane, 300, 250);

        // new createNewUserScene

        VBox newUserPane = new VBox(10);

        HBox newUsernamePane = new HBox(10);
        newUsernamePane.setPadding(new Insets(10));
        TextField newUsernameInput = new TextField();
        Label newUsernameLabel = new Label("username");
        newUsernameLabel.setPrefWidth(100);
        newUsernamePane.getChildren().addAll(newUsernameLabel, newUsernameInput);

        HBox newNamePane = new HBox(10);
        newNamePane.setPadding(new Insets(10));
        TextField newNameInput = new TextField();
        Label newNameLabel = new Label("password");
        newNameLabel.setPrefWidth(100);
        newNamePane.getChildren().addAll(newNameLabel, newNameInput);

        Label userCreationMessage = new Label();

        Button createNewUserButton = new Button("create");
        createNewUserButton.setPadding(new Insets(10));

        createNewUserButton.setOnAction(e->{
            String username = newUsernameInput.getText();
            String password = newNameInput.getText();

            if ( username.length()<3 || password.length()<3 ) {
                userCreationMessage.setText("username or name too short");
                userCreationMessage.setTextFill(Color.RED);
            } else if ( eventService.createUser(username, password) ){
                userCreationMessage.setText("");
                loginMessage.setText("new user created");
                loginMessage.setTextFill(Color.GREEN);
                primaryStage.setScene(loginScene);
            } else {
                userCreationMessage.setText("username has to be unique");
                userCreationMessage.setTextFill(Color.RED);
            }

        });

        newUserPane.getChildren().addAll(userCreationMessage, newUsernamePane, newNamePane, createNewUserButton);

        newUserScene = new Scene(newUserPane, 300, 250);

        // Public scene

        ScrollPane eventScollbar = new ScrollPane();
        BorderPane mainPane = new BorderPane(eventScollbar);
        eventScene = new Scene(mainPane, 300, 250);

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

        HBox createForm = new HBox(10);
        Button createEvent = new Button("Create");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        TextField newEventInput = new TextField();
        createForm.getChildren().addAll(newEventInput, spacer, createEvent);

        eventNodes = new VBox(10);
        eventNodes.setMaxWidth(280);
        eventNodes.setMinWidth(280);
        redrawEventlist();

        eventScollbar.setContent(eventNodes);
        mainPane.setBottom(createForm);
        mainPane.setTop(menuPane);

        createEvent.setOnAction(e->{
            try {
                eventService.createEvent(newEventInput.getText());
            } catch (ParseException ex) {
                Logger.getLogger(Otkalenteri.class.getName()).log(Level.SEVERE, null, ex);
            }
            newEventInput.setText("");
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
    }

}

