package Song.view;

import Song.app.Song;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.BufferedReader;
import java.io.FileReader;


public class Controller {
    //add editable objects
    @FXML Button add;
    @FXML Button edit;
    @FXML Button delete;
    @FXML Text selectedname;
    @FXML Text selectedartist;
    @FXML Text selectedalbum;
    @FXML Text selectedyear;
    @FXML ListView<Song> songlist;
    @FXML TextField nameinput;
    @FXML TextField artistinput;
    @FXML TextField albuminput;
    @FXML TextField yearinput;

    private ObservableList<Song> obsList;

    public void addButtonPushed(ActionEvent event){
        Song newSong = new Song();
        newSong.setName(nameinput.getText());
        newSong.setArtist(artistinput.getText());
        newSong.setAlbum(albuminput.getText());
        newSong.setYear(Integer.parseInt(yearinput.getText()));
        System.out.println(newSong);
    }









}
