package MrEncrypter;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.*;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.management.openmbean.InvalidKeyException;
import org.apache.commons.codec.binary.Base64;
import java.net.URI;

public class Controller implements Initializable {

    @FXML
    public TextField encryptText;
    @FXML
    public TextField decryptText;
    @FXML
    private Pane Dashboard;
    @FXML
    private Pane EncryptFile;
    @FXML
    private Pane EncryptFolder;
    @FXML
    private Pane PasswordGen;
    @FXML
    private TextField textLenField;
    @FXML
    private TextField resultTextField;
    @FXML
    private CheckBox numbers;
    @FXML
    private CheckBox upperCase;
    @FXML
    private Pane EncryptText;
    private static final String initVector = "encryptionIntVec";
    private static String key = "";
    @FXML
    private PasswordField KeyPassword;
    @FXML
    private PasswordField keyFile;
    @FXML
    private Text EncryptFIleTxt;
    @FXML
    private Text DecrFileTxt;
    String ext = "";
    @FXML
    private PasswordField FolderKey;
    @FXML
    private Text EncryptFoldertxt;
    @FXML
    private Text DecryptFoldertxt;
    @FXML
    private ProgressBar FileProgress;
    Task encrypting;
    @FXML
    private ProgressBar FolderProgress;
    @FXML
    private ProgressBar TextProgress;

    @FXML
    public void swap(MouseEvent mouseEvent) {
        String encryptText = this.encryptText.getText();
        String decryptText = this.decryptText.getText();

        this.encryptText.setText(decryptText);
        this.decryptText.setText(encryptText);
    }

    @FXML
    private void loadDashboard(ActionEvent event) {
        Dashboard.setVisible(true);
        EncryptFile.setVisible(false);
        EncryptFolder.setVisible(false);
        EncryptText.setVisible(false);
        PasswordGen.setVisible(false);
    }

    @FXML
    private void loadText(ActionEvent event) {

        Dashboard.setVisible(false);
        EncryptFile.setVisible(false);
        EncryptFolder.setVisible(false);
        EncryptText.setVisible(true);
        PasswordGen.setVisible(false);
    }

    @FXML
    private void loadFile(ActionEvent event) {
        Dashboard.setVisible(false);
        EncryptFile.setVisible(true);
        EncryptFolder.setVisible(false);
        EncryptText.setVisible(false);
        PasswordGen.setVisible(false);
    }

    @FXML
    private void loadFolder(ActionEvent event) {

        Dashboard.setVisible(false);
        EncryptFile.setVisible(false);
        EncryptFolder.setVisible(true);
        EncryptText.setVisible(false);
        PasswordGen.setVisible(false);
    }

    @FXML
    private void loadPassoword(ActionEvent event) {
        Dashboard.setVisible(false);
        EncryptFile.setVisible(false);
        EncryptFolder.setVisible(false);
        EncryptText.setVisible(false);
        PasswordGen.setVisible(true);
    }

    @FXML
    private void btnOpenPDF(ActionEvent event) throws IOException {
        Desktop.getDesktop().open(new java.io.File("UserManual.pdf"));
    }

    @FXML
    private void btnOpenTut(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("tutorial.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        stage.setResizable(false);
        stage.getIcons().add(new Image("./MrEncrypter/logo.png"));

    }
    //This method is for the text encryption, it delays the process for 5 seconds an sets the value of the progress bar

    public Task simulate() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(500);
                    updateMessage("500 milliseconds");
                    updateProgress(i + 1, 10);
                }
                return true;
            }
        };
    }

    //Encrypt our text here
    public static String encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {

            ex.printStackTrace();
        }
        return null;
    }

    //Decrypt our text here:
    public static String decrypt(String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        } catch (Exception ex) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Incorrect Password!");
            alert.showAndWait();
            ex.printStackTrace();
        }

        return null;
    }
    String text1 = "";
    //this is the encrypt button function

    @FXML
    private void loadEncryptText(ActionEvent event) {

        if (KeyPassword.getText().length() != 16) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Your password should be 16 characters long");
            a.showAndWait();
        } else {
            encrypting = simulate();
            TextProgress.progressProperty().unbind();
            TextProgress.progressProperty().bind(encrypting.progressProperty());
            encrypting.messageProperty().addListener(new ChangeListener<String>() {
                public void changed(ObservableValue<? extends String> observable,
                        String oldValue, String newValue) {
                    key = KeyPassword.getText();
                    //calling the encrypt function to encrypt the text user entered
                    text1 = encrypt(encryptText.getText());
                }
            });
            new Thread(encrypting).start();
            encrypting.setOnSucceeded(w -> {
                //displaying success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Your text has been encrypted\n" + text1);
                alert.showAndWait();
                decryptText.setText(text1);
                KeyPassword.setText("");
                encryptText.setText("");
                TextProgress.progressProperty().unbind();
                TextProgress.setProgress(0);
            });
        }
    }
    //this is the decrypt button function

    @FXML
    private void loadDecryptText(ActionEvent event) {
        if (KeyPassword.getText().length() != 16) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Your password should be 16 characters long");
            a.showAndWait();
        } else {

            encrypting = simulate();
            TextProgress.progressProperty().unbind();
            TextProgress.progressProperty().bind(encrypting.progressProperty());
            encrypting.messageProperty().addListener(new ChangeListener<String>() {
                public void changed(ObservableValue<? extends String> observable,
                        String oldValue, String newValue) {
                    key = KeyPassword.getText();
                    text1 = decrypt(encryptText.getText());
//            System.out.println(newValue);
                }
            });
            new Thread(encrypting).start();
            encrypting.setOnSucceeded(w -> {
                if (text1 != null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Your text has been decrypted\n" + text1);
                    alert.showAndWait();
                    decryptText.setText(text1);
                    KeyPassword.setText("");
                    encryptText.setText("");
                    TextProgress.progressProperty().unbind();
                    TextProgress.setProgress(0);
                } else {

                    KeyPassword.setText("");
                    decryptText.setText("");
                    TextProgress.progressProperty().unbind();
                    TextProgress.setProgress(0);
                }
            });

        }
    }

    //decrypt file
    public Task fileProcessor(int cipherMode, String key, File inputFile, File outputFile) {
        return new Task() {
            @Override
            protected Object call() throws Exception {

                try {
                    Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
                    Cipher cipher = Cipher.getInstance("AES");
                    cipher.init(cipherMode, secretKey);
                    //getting ourr input file in bytes.
                    FileInputStream inputStream = new FileInputStream(inputFile);
                    byte[] inputBytes = new byte[(int) inputFile.length()];
                    inputStream.read(inputBytes);

                    byte[] outputBytes = cipher.doFinal(inputBytes);
                    //Writing out to our output file
                    FileOutputStream outputStream = new FileOutputStream(outputFile);
                    outputStream.write(outputBytes);
                    //clossing the input and output streams.
                    inputStream.close();
                    outputStream.close();
                    //This for loop is for our progress indicator, it sleeps for half a second for each loop
                    //Thus it takes it 5 secs to complete the loop and our task.
                    for (int i = 0; i < 10; i++) {
                        //sleep for 5 secs.
                        Thread.sleep(500);
                        updateMessage("500 milliseconds");
                        //update the progress.
                        updateProgress(i + 1, 10);
                    }

                } catch (NoSuchPaddingException | NoSuchAlgorithmException
                        | InvalidKeyException | BadPaddingException
                        | IllegalBlockSizeException | IOException e) {

                    e.printStackTrace();
                } catch (java.security.InvalidKeyException ex) {
                    //Dsiplaying error message if the kkey entered is wrong.
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Incorrect password!");
                    alert.showAndWait();
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                return true;
            }
        };
    }

    //upload
    @FXML
    private void loadUploadFileEncrypt(ActionEvent event) {
        //Here we are uploading the file to Encrypt
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            //setting the encrypt file text to the path of the file we just uploaded
            ext = file.getAbsolutePath();
            System.out.println(ext);
            EncryptFIleTxt.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void loadUploadFileDecrypt(ActionEvent event) {

        //Here we are uploading the file to decrypt
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            //setting the decrypt file text to the path of the file we just uploaded
            DecrFileTxt.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void loadEncryptFile(ActionEvent event) {
        if (keyFile.getText().length() != 16) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Your password should be 16 characters long");
            a.showAndWait();
        } else {
            //getting the name if the uploaded file
            File inputFile = new File(EncryptFIleTxt.getText());
            //giving the ouptout file a name, all our encrypted files will all start with encrypted_
            File encryptedFile = new File(ext);
            //setting the progress bar to an indeterminate state
            FileProgress.setProgress(-1);
            //This is our task
            encrypting = fileProcessor(Cipher.ENCRYPT_MODE, keyFile.getText(), inputFile, encryptedFile);
            //Unbinding our progressbar from any other task binding and binding it to our current task.
            FileProgress.progressProperty().unbind();
            FileProgress.progressProperty().bind(encrypting.progressProperty());
            encrypting.messageProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                    System.out.println(newValue);
                }
            });
            //starting the task
            new Thread(encrypting).start();
//            The line below will be executed only when our task has completed successfully
            encrypting.setOnSucceeded(eventw -> {
                //Displaying success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Your file has been encrypted\n Find " + ext + " in your folders.");
                alert.showAndWait();
                keyFile.setText("");
                EncryptFIleTxt.setText("");
                //unbinding our progress bar and setting its progress to zero
                FileProgress.progressProperty().unbind();
                FileProgress.setProgress(0);
            });

        }
    }

    @FXML
    private void loadDecryptFile(ActionEvent event) {
        //checking whether the key is 16 characters long
        if (keyFile.getText().length() != 16) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Your password should be 16 characters long");
            a.showAndWait();
        } else {
            File inputFile = new File(DecrFileTxt.getText());
            //setting our output file name, it will be the name of the encrypted file without the 'encrypted_' part
            File encryptedFile = new File(DecrFileTxt.getText().split("_")[0]);
            //setting the progress bar to an indeterminate state
            FileProgress.setProgress(-1);
            //This is the task that will decrypt the file
            encrypting = fileProcessor(Cipher.DECRYPT_MODE, keyFile.getText(), inputFile, encryptedFile);
            //Unbinding our progressbar from any other task binding and binding it to our current task.
            FileProgress.progressProperty().unbind();
            FileProgress.progressProperty().bind(encrypting.progressProperty());
            encrypting.messageProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                    System.out.println(newValue);
                }
            });
            //starting the task
            new Thread(encrypting).start();
//            The line below will be executed only when our task has completed successfully
            encrypting.setOnSucceeded(eventw -> {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Your file has been decrypted\n Find " + DecrFileTxt.getText().split("_")[0] + " in your folders.");
                alert.showAndWait();
                keyFile.setText("");
                DecrFileTxt.setText("");
                //unbinding our progress bar and setting its progress to zero
                FileProgress.progressProperty().unbind();
                FileProgress.setProgress(0);
            });

        }

    }

    @FXML
    private void loadEncryptFolder(ActionEvent event) {
        //Works the same as the file encryption only that here we are reading  files from a folder (basically its the same to reading
        // many files at once)
        //Read the folder that has been selected
        File dir = new File(EncryptFoldertxt.getText());
        File[] filelist = dir.listFiles();

        //Loop through all the files in the folder
        System.out.println(filelist.length);
        Arrays.asList(filelist).forEach(file -> {

            try {

                if (file.isDirectory()) {
                    File[] filelist2 = file.listFiles();

                    System.out.println(filelist2.length);
                    Arrays.asList(filelist2).forEach(file2 -> {

                        encrypting = fileProcessor(Cipher.ENCRYPT_MODE, FolderKey.getText(), file2, file2);
                        new Thread(encrypting).start();
                    });
                } else {
                    encrypting = fileProcessor(Cipher.ENCRYPT_MODE, FolderKey.getText(), file, file);

                    FolderProgress.progressProperty().unbind();
                    FolderProgress.progressProperty().bind(encrypting.progressProperty());
                    encrypting.messageProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                            System.out.println(newValue);
                        }
                    });
                    new Thread(encrypting).start();
                }
            } catch (InvalidKeyException e) {
                //Dsiplay error message if the key is wrong.
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Incorrect password!");
                alert.showAndWait();
                System.err.println("Couldn't encrypt " + file.getName() + ": " + e.getMessage());
            }
        });

        encrypting.setOnSucceeded(eventw -> {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Files encrypted successfully");
            alert.showAndWait();
            FolderKey.setText("");
            EncryptFoldertxt.setText("");
            FolderProgress.progressProperty().unbind();
            FolderProgress.setProgress(0);
        });
        System.out.println("Files encrypted successfully");

    }

    @FXML
    private void loadDecryptFolder(ActionEvent event) {
        //Read the folder that has been selected
        File dir = new File(DecryptFoldertxt.getText());
        File[] filelist = dir.listFiles();
        //Loop through all the files in the folder
        Arrays.asList(filelist).forEach(file -> {
            try {
                if (file.isDirectory()) {

                    File[] filelist2 = file.listFiles();
                    Arrays.asList(filelist2).forEach(file2 -> {
                        

                encrypting = fileProcessor(Cipher.DECRYPT_MODE, FolderKey.getText(), file2, file2);
                new Thread(encrypting).start();
                    });

                }
                else{

                encrypting = fileProcessor(Cipher.DECRYPT_MODE, FolderKey.getText(), file, file);

                FolderProgress.progressProperty().unbind();
                FolderProgress.progressProperty().bind(encrypting.progressProperty());
                encrypting.messageProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                        System.out.println(newValue);
                    }
                });

                new Thread(encrypting).start();
                }
            } catch (InvalidKeyException e) {
                //Dsiplay error message if the key is wrong.
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Incorrect password!");
                alert.showAndWait();
                System.err.println("Couldn't decrypt " + file.getName() + ": " + e.getMessage());
            }
        });

        encrypting.setOnSucceeded(eventw -> {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Files decrypted successfully");
            alert.showAndWait();
            FolderKey.setText("");
            DecryptFoldertxt.setText("No Folder Selected");
            FolderProgress.progressProperty().unbind();
            FolderProgress.setProgress(0);
        });
        System.out.println("Files decrypted successfully");

    }

    //This is the action for the upload folder to encrypt button it opens a window where we select a folder
    @FXML
    private void loadUploadFolderEncrypt(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(new Stage());
        if (file != null) {
            EncryptFoldertxt.setText(file.getAbsolutePath());
        }
    }
    //This is the action for the upload folder to decrypt button it opens a window where we select a folder

    @FXML
    private void loadUploadFolderDecrypt(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(new Stage());
        if (file != null) {
            DecryptFoldertxt.setText(file.getAbsolutePath());
        }
    }
    //This is the drag event to upload the file through drag and drop, it is basically the same for the drag and drop folders
    //we check if the drag has files, if it does, we set our text to the file path of the uploaded file.

    @FXML
    private void loadDragFileEncrypt(DragEvent event) {
        //This is our drag board
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            //This is the counter to check the number of files uploaded, we only accept one file
            int i = 0;
            success = true;
            String filePath = null;
            for (File file : db.getFiles()) {
                filePath = file.getAbsolutePath();
                EncryptFIleTxt.setText(filePath);
                ext = filePath;
                i++;
            }

            if (i > 1) {
                //Displaying error mesage if the files dragged and dropped is more than one.
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You can't select more than one file!");
                alert.setHeaderText("More Than One File Selected");
                alert.showAndWait();
                EncryptFIleTxt.setText("");
            }
        }
        //completing the drag event
        event.setDropCompleted(success);
        event.consume();
    }

    @FXML
    private void loadDragFileDecrypt(DragEvent event) {

        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            int i = 0;
            success = true;
            String filePath = null;
            for (File file : db.getFiles()) {
                filePath = file.getAbsolutePath();
                DecrFileTxt.setText(filePath);
                i++;
            }

            if (i > 1) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You can't select more than one file!");
                alert.setHeaderText("More Than One File Selected");
                alert.showAndWait();
                DecrFileTxt.setText("");
            }
        }
        event.setDropCompleted(success);
        event.consume();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Dashboard.setVisible(true);
        EncryptFile.setVisible(false);
        EncryptFolder.setVisible(false);
        EncryptText.setVisible(false);
        PasswordGen.setVisible(false);

        DecrFileTxt.setText("");
        EncryptFIleTxt.setText("");
        EncryptFoldertxt.setText("");
        DecryptFoldertxt.setText("");
    }

    @FXML
    private void FileEncryptDrag(DragEvent event) {
        //Checking if the drag has files
        Dragboard db = event.getDragboard();
        //If there are files, we accept the drag
        if (db.hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        } else {
            //mark drag as consumed to stop further propagation
            event.consume();
        }

    }

    @FXML
    private void loadDragFolderEncrypt(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            int i = 0;
            success = true;
            String filePath = null;
            for (File file : db.getFiles()) {
                filePath = file.getAbsolutePath();
                EncryptFoldertxt.setText(filePath);
                i++;
            }
            if (i > 1) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cant select more than one folder!");
                alert.setHeaderText("More Than One Folder Selected");
                alert.showAndWait();
                DecryptFoldertxt.setText("Or drag and drop Folder");
            }
        }
        event.setDropCompleted(success);
        event.consume();
    }

    @FXML
    private void loadDragFolderDecrypt(DragEvent event) {

        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            String filePath = null;
            int i = 0;
            for (File file : db.getFiles()) {
                filePath = file.getAbsolutePath();
                DecryptFoldertxt.setText(filePath);
                i++;
            }
            //if the selected folders are more than one, then throw an error message, folder should only be one.
            if (i > 1) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cant select more than one folder!");
                alert.setHeaderText("More Than One Folder Selected");
                alert.showAndWait();
                DecryptFoldertxt.setText("Or drag and drop Folder");
            }
        }
        event.setDropCompleted(success);
        event.consume();
    }

    //Password Generator
    private PasswordGenerator generator = new PasswordGenerator();

    public void btnClick(ActionEvent actionEvent) {
        String testStr = textLenField.getText();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < testStr.length(); i++) {
            if (testStr.charAt(i) < '0' || testStr.charAt(i) > '9') {
            } else {
                sb.append(testStr.charAt(i));
            }
        }

        if (sb.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("You input no numbers!");
            alert.setContentText("You have to input length of password");
            alert.showAndWait();
        } else {
            String text = generator.generate(Integer.parseInt(sb.toString()), numbers.isSelected(), upperCase.isSelected());
            textLenField.setText(sb.toString());
            resultTextField.setText(text);
        }

    }
}
