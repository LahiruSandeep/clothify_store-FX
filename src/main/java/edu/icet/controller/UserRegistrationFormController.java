package edu.icet.controller;

import com.jfoenix.controls.*;
import edu.icet.util.CrudUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class UserRegistrationFormController implements Initializable {
    public JFXTextField txtAdminUserName;
    public JFXTextField txtOtp;
    public JFXTextField txtUserName;
    public JFXComboBox redUserType;
    public JFXTextField txtEmail;
    public JFXPasswordField txtAdminPassword;
    public JFXPasswordField txtUserPassword;
    public JFXPasswordField txtConformUserPassword;
    public JFXButton verifyBtn;
    public JFXButton btnSend;
    public JFXCheckBox checkBoxUserPassword;
    public JFXCheckBox checkBoxAdminPassword;
    public JFXComboBox cmbUserType;
    public JFXButton btnCreate;
    StringProperty variable = new SimpleStringProperty("");
    StringProperty variable2 = new SimpleStringProperty("");

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        txtUserPassword.textProperty().bindBidirectional(variable);
        txtConformUserPassword.textProperty().bindBidirectional(variable2);
        AtomicReference<String> val1= new AtomicReference<>("");
        AtomicReference<String> val2= new AtomicReference<>("");
        variable.addListener((observable, oldValue, newValue) -> {
            val1.set(newValue);
            conformPassword(val1,val2);
        });
        variable2.addListener((observable, oldValue, newValue) -> {
            val2.set(newValue);
            conformPassword(val1,val2);
        });


        cmbUserType.getItems().addAll("Admin", "User");

    }
    public void conformPassword(AtomicReference<String> newValueTxt1, AtomicReference<String> newValueTxt2) {
        if(txtUserPassword.getText().equals("") && txtConformUserPassword.getText().equals("")){
            btnCreate.setDisable(true);
        }else if (newValueTxt1.get().equals(newValueTxt2.get())) {
            btnCreate.setDisable(false);
        }else{
            btnCreate.setDisable(true);
        }
    }


    public void backBtnOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/login_form.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnAdminCheckOnAction(ActionEvent actionEvent) {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT user_name, password FROM user WHERE user_type = 'Admin' AND user_name = ? AND password = ?",
                    txtAdminUserName.getText(),
                    txtAdminPassword.getText()
            );
            if (resultSet.next()) {
                new Alert(Alert.AlertType.INFORMATION, "Admin user found.").show();
                txtUserName.setDisable(false);
                txtEmail.setDisable(false);
                txtOtp.setDisable(false);
                btnSend.setDisable(false);
            } else {
                new Alert(Alert.AlertType.ERROR, "Admin user not found !").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnCreateOnAction(ActionEvent actionEvent) {

    }

    public void btnOtpOnAction(ActionEvent actionEvent) {
        sendEmail();
    }

    public char[] getOtp(int len) {
        String numbers = "0123456789";
        Random rNd = new Random();
        char[] otp = new char[len];
        for (int i = 0; i < len; i++) {
            otp[i] = numbers.charAt(rNd.nextInt(numbers.length()));

        }
        return otp;
    }

    char[] otp;

    public void sendEmail() {

    }

    public void verifyBtnOnAction(ActionEvent actionEvent) {
        String OTP = "";
        for (char s : otp) {
            OTP += s;
        }
        if (txtOtp.getText().equals(OTP)) {
            new Alert(Alert.AlertType.INFORMATION, "OTP Matched :)").show();
            cmbUserType.setDisable(false);
            txtUserPassword.setDisable(false);
            txtConformUserPassword.setDisable(false);

        } else {
            new Alert(Alert.AlertType.ERROR, "invalid OTP :(").show();
        }
    }

    public void checkBoxUserPasswordOnAction(ActionEvent actionEvent) {
        System.out.println("checkBoxUserPasswordOnAction");
        checkBoxUserPassword.setVisible(true);
    }

    public void checkBoxAdminPasswordOnAction(ActionEvent actionEvent) {
        if (checkBoxAdminPassword.isSelected()) {
            System.out.println("selected");
            txtAdminPassword.setText(txtAdminPassword.getText());

        } else {
            System.out.println("un selected");
        }
    }
}
