package Song.view;

import Song.app.Song;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.BufferedReader;
import java.io.FileReader;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;


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

        listView.getSelectionModel().clearSelection();
        listView.getSelectionModel().selectFirst();
        selectedIndex = 0;

        selectSong();

        EventHandler<MouseEvent> eventHandler = e -> {
            selectSong();
            selectedIndex = listView.getSelectionModel().getSelectedIndex();
            selectedname.setText(libList.get(selectedIndex).getName());
            selectedartist.setText(libList.get(selectedIndex).getArtist());
            selectedalbum.setText(libList.get(selectedIndex).getAlbum());
            selectedyear.setText(libList.get(selectedIndex).getYear());
        };

        nameinput.clear();
        artistinput.clear();
        albuminput.clear();
        yearinput.clear();

        listView.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    }

    public void addButtonPushed(ActionEvent event){

        if(artistinput.getText().isEmpty() || nameinput.getText().isEmpty()){
            Alert artistorname = new Alert(AlertType.ERROR);
            artistorname.setContentText("Please enter a name and/or artist.");
            artistorname.showAndWait();
            return;
        }

        if(!yearinput.getText().isEmpty() && Integer.parseInt(yearinput.getText()) < 0){
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
            System.out.println(newSong);
            add(newSong);
        }

    }

    private void add(Song newEntry) {
        System.out.println("add");
        if (libList == null) {
            libList = new ArrayList<>();
        }
        if(libList.size() == 0){
            selectedIndex = 0;
            libList.add(newEntry);
        } else { //for comparing, you can implement ArrayList.sort, as it is an easier method to use instead of writing it all out.
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
        if(artistinput.getText().isEmpty() || nameinput.getText().isEmpty()){
            Alert artistorname = new Alert(AlertType.ERROR);
            artistorname.setContentText("Please enter a name and/or artist.");
            artistorname.showAndWait();
            return;
        }

        if(!yearinput.getText().isEmpty() && Integer.parseInt(yearinput.getText()) < 0){
            Alert year = new Alert(AlertType.ERROR);
            year.setContentText("Please enter a valid year.");
            year.showAndWait();
            return;
        }

        Alert confirm = new Alert(AlertType.CONFIRMATION, "Edit " + nameinput.getText() + " by " + artistinput.getText() + "?", ButtonType.OK, ButtonType.CANCEL);
        confirm.showAndWait();

        if(confirm.getResult() == ButtonType.OK){
            if(!libList.isEmpty()) {
                Song currSong = new Song(nameinput.getText(), artistinput.getText(), albuminput.getText(), yearinput.getText());
                edit(currSong);
            }
        }
    }

    private void edit(Song Entry) {
        System.out.println("edit");
        for (int x = 0; x < libList.size(); x++) {
            Song curr = libList.get(x);
            if(curr.getName().compareTo(Entry.getName()) == 0 && curr.getArtist().compareTo(Entry.getArtist()) == 0) {
                libList.get(x).setAlbum(Entry.getAlbum());
                libList.get(x).setYear(Entry.getYear());
                System.out.println("done edit");
            }
        }

        listView.getItems().clear();
        for(Song x : libList){
            listView.getItems().add(x);
        }

        if(songLibrary != null) {
            FileConvert.save(libList, songLibrary);
        }
    }

    public void deleteButtonPushed(ActionEvent event) {
        selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if(selectedIndex < 0){
            //throw error;
            System.out.println("ERROR");
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
        System.out.println("delete");
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

    private void selectSong() {
        if (listView.getSelectionModel().getSelectedItem() != null) {
            selectedname.setText(listView.getSelectionModel().getSelectedItem().getName());
            selectedartist.setText(listView.getSelectionModel().getSelectedItem().getArtist());
            selectedalbum.setText(listView.getSelectionModel().getSelectedItem().getAlbum());
            selectedyear.setText(listView.getSelectionModel().getSelectedItem().getYear());
        }
    }
}
