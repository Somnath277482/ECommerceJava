package com.example.ecommerce;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Ecommerce extends Application {


    ProductList productList = new ProductList();

    private final int width = 500 , height = 600, headerLine = 50 ;
    Pane bodyPane ;

    Button signInButton ;
    Label welcomeLabel = new Label("Welcome Customer") ;

    Customer loggedInCustomer = null ;

    private GridPane headerBar(){
        GridPane header = new GridPane() ;

        TextField searchBar = new TextField() ;
        Button searchButton = new Button("Search") ;

        signInButton = new Button("Signin") ;

        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                bodyPane.getChildren().clear();
                bodyPane.getChildren().add(productList.getAllProducts());
            }
        });

        signInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                bodyPane.getChildren().clear();
                bodyPane.getChildren().add(loginPage()) ;
            }
        });
        header.setHgap(10);
        header.add(searchBar, 0,0);
        header.add(searchButton,1,0) ;
        header.add(signInButton,2,0);
        header.add(welcomeLabel,3,0) ;
        return header ;

    }

    private GridPane loginPage(){
        Label userLabel = new Label("User Name") ;
        Label passLabel = new Label("Password") ;

        TextField userName = new TextField() ;
        userName.setPromptText("Enter Username");

        PasswordField password = new PasswordField() ;
        password.setPromptText("Enter Password");

        Button loginButton = new Button("Login") ;
        Label messageLabel = new Label("Login - Message") ;

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String user = userName.getText();
                String pass = password.getText() ;

                loggedInCustomer = Login.customerLogin(user,pass) ;
                if(loggedInCustomer!= null){
                    messageLabel.setText("Login Successful");
                    welcomeLabel.setText("Welcome "+loggedInCustomer.name);
                }
                else{
                    messageLabel.setText("Login Failed");
                }
            }
        });

        GridPane loginPage = new GridPane();
        loginPage.setTranslateY(50);
        loginPage.setVgap(10);
        loginPage.setHgap(10);
        loginPage.add(userLabel, 0,0);
        loginPage.add(userName,1,0) ;

        loginPage.add(passLabel,0,1);
        loginPage.add(password,1,1);

        loginPage.add(loginButton,0,2);
        loginPage.add(messageLabel,1,2);

        return loginPage ;


    }

    private void showDialogue(String message){
        //Creating a dialog
        Dialog<String> dialog = new Dialog<String>();
        //Setting the title
        dialog.setTitle("Order Status");
        ButtonType type = new ButtonType("Ok", ButtonData.OK_DONE);
        //Setting the content of the dialog
        dialog.setContentText(message);
        //Adding buttons to the dialog pane
        dialog.getDialogPane().getButtonTypes().add(type);


            dialog.showAndWait();

    }
    private GridPane footerBar(){
        Button buyNowButton = new Button("Buy Now") ;
        buyNowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product = productList.getSelectedProduct();
                boolean orderStatus = false ;
                if(product!=null && loggedInCustomer!=null){
                    orderStatus = Order.placeOrder(loggedInCustomer, product) ;
                }

                if(orderStatus==true){
                    //
                    showDialogue("Order Successful");
                }
                else {
                    showDialogue("Please select a project");
                }
            }
        });

        GridPane footer = new GridPane() ;
        footer.setTranslateY(470);
        footer.add(buyNowButton,0,0) ;

        return footer ;
    }
    private Pane createContent (){
        Pane root = new Pane() ;
        root.setPrefSize(width+2*headerLine,height);

        bodyPane = new Pane() ;
        bodyPane.setPrefSize(width,height);
        bodyPane.setTranslateY(headerLine);
        bodyPane.setTranslateX(10);

        bodyPane.getChildren().add(loginPage()) ;
        root.getChildren().addAll(headerBar(),
        //        loginPage(),
        //        productList.getAllProducts(),
                bodyPane, footerBar());
        return root ;
    }
    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(Ecommerce.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(createContent());
        stage.setTitle("Ecommerce Website");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }
}