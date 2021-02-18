/*
* Kruti Shah(ks1511)
* Akshay Magam(akm152)
*/

package Song.view;

import Song.app.Song;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Controller {
    //add editable objects
    @FXML Button addButton;
    @FXML Button editButton;
    @FXML Button deleteButton;
    @FXML Text selectedname;
    @FXML Text selectedartist;
    @FXML Text selectedalbum;
    @FXML Text selectedyear;
    @FXML ListView<Song> listView;
    @FXML TextField nameinput;
    @FXML TextField artistinput;
    @FXML TextField albuminput;
    @FXML TextField yearinput;


    //private ObservableList<Song> obsList;
    private int selectedIndex;
    private File songLibrary;
    private ArrayList<Song> libList;

    public void start(final Stage primaryStage) throws FileNotFoundException {
        songLibrary = new File("songLibrary.txt");
        libList = FileConvert.read(songLibrary);

        for(Song x : libList){
            listView.getItems().add(x);
        }

        listView.getSelectionModel().select(0);
        selectSong();
        listView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> selectSong()); // temporary listener for ListView
    }

    public static boolean validYear(String str) {
        try {
            if(Integer.parseInt(str) <0){
                return false;
            }
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static boolean validNameArtist(String name, String artist){
        if(artist.isEmpty() || name.isEmpty()){
            Alert artistorname = new Alert(AlertType.ERROR);
            artistorname.setContentText("Please enter a name and/or artist.");
            artistorname.showAndWait();
            return false;
        }
        return true;
    }

    public void addButtonPushed(ActionEvent event){

        if(!validNameArtist(artistinput.getText(), nameinput.getText())){
            return;
        }

        if(!yearinput.getText().isEmpty() && !validYear(yearinput.getText())){
            Alert year = new Alert(AlertType.ERROR);
            year.setContentText("Please enter a valid year.");
            year.showAndWait();
            return;
        }

        Alert confirm = new Alert(AlertType.CONFIRMATION, "Add " + nameinput.getText() + " by " + artistinput.getText() + "?", ButtonType.OK, ButtonType.CANCEL);
        confirm.showAndWait();

        if(confirm.getResult() == ButtonType.OK){
            Song newSong = new Song();
            newSong.setName(nameinput.getText());
            newSong.setArtist(artistinput.getText());
            if(!albuminput.getText().isEmpty()) {newSong.setAlbum(albuminput.getText());}
            if(!yearinput.getText().isEmpty()) {newSong.setYear((yearinput.getText()));}
            //System.out.println(newSong);
            add(newSong);
        }
    }

    private void add(Song newEntry) {
        if (libList == null) {
            libList = new ArrayList<>();
        }
        if(libList.size() == 0){
            selectedIndex = 0;
            libList.add(newEntry);
        } else {
            int ind;
            for (ind = 0; ind < libList.size(); ind++) {
                Song curEntry = libList.get(ind);
                if(newEntry.getName().toLowerCase().compareTo(curEntry.getName().toLowerCase()) <= 0) {
                    break;
                }
            }

            if (ind == libList.size()) {
                libList.add(newEntry);
                selectedIndex = ind;
            } else if (newEntry.getName().toLowerCase().compareTo(libList.get(ind).getName().toLowerCase()) < 0) {
                libList.add(ind, newEntry);
                selectedIndex = ind;
            } else {
                boolean check = true;
                while (newEntry.getName().toLowerCase().compareTo(libList.get(ind).getName().toLowerCase()) == 0) {
                    if (newEntry.getArtist().toLowerCase().compareTo(libList.get(ind).getArtist().toLowerCase()) < 0) {
                        libList.add(ind, newEntry);
                        selectedIndex = ind;
                        check = false;
                        break;
                    } else if (newEntry.getArtist().toLowerCase().compareTo(libList.get(ind).getArtist().toLowerCase()) == 0){
                        Alert repeat = new Alert(AlertType.ERROR);
                        repeat.setContentText("Already in your List.");
                        repeat.showAndWait();
                        return;
                    } else if(ind == libList.size() - 1) {
                        libList.add(newEntry);
                        selectedIndex = ind + 1;
                        check = false;
                        break;
                    }
                    ind++;
                }
                if(check) {
                    libList.add(ind , newEntry);
                    selectedIndex = ind;
                }
            }
        }
        selectSong();

        listView.getItems().clear();
        for(Song x : libList){
            listView.getItems().add(x);
        }

        listView.getSelectionModel().select(selectedIndex);
        selectSong();
        if(songLibrary != null) {
            FileConvert.save(libList, songLibrary);
        }
    }


    public void editButtonPushed(ActionEvent event) {
        if(!validNameArtist(artistinput.getText(), nameinput.getText())){
            return;
        }

        if(!yearinput.getText().isEmpty() && !validYear(yearinput.getText())){
            Alert year = new Alert(AlertType.ERROR);
            year.setContentText("Please enter a valid year.");
            year.showAndWait();
            return;
        }

        Alert confirm = new Alert(AlertType.CONFIRMATION, "Edit " + listView.getSelectionModel().getSelectedItem().getName() + " by " + listView.getSelectionModel().getSelectedItem().getArtist() + "?", ButtonType.OK, ButtonType.CANCEL);
        confirm.showAndWait();

        if(confirm.getResult() == ButtonType.OK){
            Song currSong = new Song(nameinput.getText(), artistinput.getText(), albuminput.getText(), yearinput.getText());
            edit(currSong);
        }
    }

    private void edit(Song Entry) {
        for (int x = 0; x < libList.size(); x++) {
            Song curr = libList.get(x);
            if(Entry.getName().compareToIgnoreCase(curr.getName()) ==0 && Entry.getArtist().compareToIgnoreCase(curr.getArtist()) == 0) {
                if(!Entry.getYear().isEmpty() || !Entry.getAlbum().isEmpty()){
                    curr.setYear(Entry.getYear());
                    curr.setAlbum(Entry.getAlbum());
                }else {
                    Alert invalid = new Alert(AlertType.ERROR);
                    invalid.setContentText("This action cannot be done. This song already exists.");
                    invalid.showAndWait();
                    return;
                }
            }
        }

        selectedIndex = listView.getSelectionModel().getSelectedIndex();
        delete();
        add(Entry);
    }

    public void deleteButtonPushed(ActionEvent event) {
        selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if(selectedIndex < 0){
            Alert error = new Alert(AlertType.ERROR, "No songs to delete!");
            error.showAndWait();
            return;
        }else {
            selectedname.setText(libList.get(selectedIndex).getName());
            selectedartist.setText(libList.get(selectedIndex).getArtist());
        }

        Alert confirm = new Alert(AlertType.CONFIRMATION, "Delete " + selectedname.getText() + " by " + selectedartist.getText() + "?", ButtonType.OK, ButtonType.CANCEL);
        confirm.showAndWait();

        if(confirm.getResult() == ButtonType.OK){
            delete();
        }
    }


    private void delete() {
        libList.remove(selectedIndex);
        listView.getItems().clear();

        for(Song x : libList){
            listView.getItems().add(x);
        }

        if(libList.size() > selectedIndex) {
            listView.getSelectionModel().select(selectedIndex);
            selectSong();
        } else if(libList.size() == selectedIndex) {
            selectedIndex = selectedIndex - 1;
            listView.getSelectionModel().select(selectedIndex);
            selectSong();
        } else if(libList.size() == 0) {
            selectSong();
        }

        if(songLibrary != null) {
            FileConvert.save(libList, songLibrary);
        }
    }

    public void selectSong() {
        Song selectedSong = listView.getSelectionModel().getSelectedItem();
        if (selectedSong != null) {
            selectedname.setText(listView.getSelectionModel().getSelectedItem().getName());
            selectedartist.setText(listView.getSelectionModel().getSelectedItem().getArtist());
            selectedalbum.setText(listView.getSelectionModel().getSelectedItem().getAlbum());
            selectedyear.setText(listView.getSelectionModel().getSelectedItem().getYear());

            nameinput.setText(selectedname.getText());
            artistinput.setText(listView.getSelectionModel().getSelectedItem().getArtist());
            albuminput.setText(listView.getSelectionModel().getSelectedItem().getAlbum());
            yearinput.setText(listView.getSelectionModel().getSelectedItem().getYear());
        }else{
            selectedname.setText("(none selected)");
            selectedartist.setText("(none selected)");
            selectedalbum.setText("(none selected)");
            selectedyear.setText("(none selected)");

            nameinput.setText("");
            artistinput.setText("");
            albuminput.setText("");
            yearinput.setText("");
        }
    }
}
