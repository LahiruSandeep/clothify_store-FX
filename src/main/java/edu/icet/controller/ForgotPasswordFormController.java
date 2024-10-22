package edu.icet.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
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
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class ForgotPasswordFormController implements Initializable {

    public JFXPasswordField txtPassword;
    public JFXPasswordField txtConfirmPassword;
    public JFXTextField txtEmail;
    public JFXTextField txtOtp;
    public JFXButton btnSaved;
    public JFXButton btnOk;

    StringProperty variable = new SimpleStringProperty("");
    StringProperty variable2 = new SimpleStringProperty("");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtPassword.textProperty().bindBidirectional(variable);
        txtConfirmPassword.textProperty().bindBidirectional(variable2);
        AtomicReference<String> val1 = new AtomicReference<>("");
        AtomicReference<String> val2 = new AtomicReference<>("");
        variable.addListener((observable, oldValue, newValue) -> {
            val1.set(newValue);
            conformPassword(val1, val2);
        });
        variable2.addListener((observable, oldValue, newValue) -> {
            val2.set(newValue);
            conformPassword(val1, val2);
        });

    }

    public void conformPassword(AtomicReference<String> newValueTxt1, AtomicReference<String> newValueTxt2) {
        if (txtPassword.getText().equals("") && txtConfirmPassword.getText().equals("")) {
            btnSaved.setDisable(true);
        } else if (newValueTxt1.get().equals(newValueTxt2.get())) {
            btnSaved.setDisable(false);
        } else {
            btnSaved.setDisable(true);
        }
    }

    public void backBtnOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/login_form.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            // Get the current window
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            // Close the previous window
            currentStage.close();
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnSentOtpOnAction(ActionEvent actionEvent) {

    }

    char[] otp;

    public void btnOkOtpOnAction(ActionEvent actionEvent) {
        String OTP = "";
        for (char s : otp) {
            OTP += s;
        }
        if (txtOtp.getText().equals(OTP)) {
            new Alert(Alert.AlertType.INFORMATION, "OTP Matched :)").show();
            txtPassword.setDisable(false);
            txtConfirmPassword.setDisable(false);


        } else {
            txtPassword.setDisable(true);
            txtConfirmPassword.setDisable(true);
            btnSaved.setDisable(true);
            new Alert(Alert.AlertType.ERROR, "invalid OTP :(").show();
        }
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


    public void sendEmail() {

    }

    public void btnSavedOnAction(ActionEvent actionEvent) {
        try {
            boolean isUpdate= CrudUtil.execute("UPDATE user\n" +
                    "SET password = ?\n" +
                    "WHERE email = ?;",txtConfirmPassword.getText(),txtEmail.getText());
            if (isUpdate){
                new Alert(Alert.AlertType.INFORMATION, "Your password change Success !").show();
            }else {
                new Alert(Alert.AlertType.ERROR, "Your password change Failed !").show();

            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
