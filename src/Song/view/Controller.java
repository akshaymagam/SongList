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


//I Commmented some thoughts on the code, please do not take this personally lol, it is just some ideas that I thought would make the code work more efficiently.
    private ObservableList<Song> obsList;
    private int selectedIndex;
    private File songLibrary;
    private ArrayList<Song> libList; //this doesn't need to be passed in, it is a global variable. we need to pass in the song objects into the methods, and reduce the number of arguments to avoid confusion.

    public void start(final Stage primaryStage) throws FileNotFoundException {
        songLibrary = new File("songLibrary.txt");
        libList = FileConvert.read(songLibrary);

        for(Song x : libList){
            listView.getItems().add(x);
        }
        //listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        listView.getSelectionModel().clearSelection();
        listView.getSelectionModel().selectFirst();
        selectedIndex = 0;

        selectSong(listView);

        EventHandler<MouseEvent> eventHandler = e -> {
            selectSong(listView);
            selectedIndex = listView.getSelectionModel().getSelectedIndex();
            selectedname.setText(libList.get(selectedIndex).getName());
            selectedartist.setText(libList.get(selectedIndex).getArtist());
            selectedalbum.setText(libList.get(selectedIndex).getAlbum());
            selectedyear.setText(libList.get(selectedIndex).getYear());

            editButton(libList);
            deleteButton(libList);
        };

        /* EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                add(listView, libList);
                System.out.println("Added it!");
            }
        };

        add.setOnAction(event);*/

        addButton.setOnAction(e -> add(listView, libList));
        editButton(libList);
        deleteButton(libList);

        nameinput.clear();
        artistinput.clear();
        albuminput.clear();
        yearinput.clear();

        listView.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    }

    public void addButtonPushed(ActionEvent event){
        System.out.println("add");

        if(artistinput.getText().isEmpty() || nameinput.getText().isEmpty()){
            Alert artistorname = new Alert(AlertType.ERROR);
            artistorname.setContentText("Please enter a name and/or artist.");
            artistorname.showAndWait();
        }
        Alert confirm = new Alert(AlertType.CONFIRMATION, "add " + nameinput.getText() + " by " + artistinput.getText() + "?", ButtonType.OK, ButtonType.CANCEL);
        confirm.showAndWait();

        if(confirm.getResult() == ButtonType.OK){
            Song newSong = new Song();
            newSong.setName(nameinput.getText());
            newSong.setArtist(artistinput.getText());
            if(!albuminput.getText().isEmpty()) {newSong.setAlbum(albuminput.getText());}
            if(!yearinput.getText().isEmpty()) {newSong.setYear((yearinput.getText()));}
            System.out.println(newSong);
        }





    }

//    public void editButtonPushed(ActionEvent event, Song selected){
//        selected.setName(nameinput.getText());
//        selected.setArtist(artistinput.getText());
//        selected.setAlbum(albuminput.getText());
//        selected.setYear(Integer.parseInt(yearinput.getText()));
//        System.out.println(newSong);
//
//
//    }
//
//    public void editButtonPushed(ActionEvent event, ){
//        Song newSong = new Song();
//        newSong.setName(nameinput.getText());
//        newSong.setArtist(artistinput.getText());
//        newSong.setAlbum(albuminput.getText());
//        newSong.setYear(Integer.parseInt(yearinput.getText()));
//        System.out.println(newSong);
//
//
//    }

    private void add(final ListView<Song> listView, final ArrayList<Song> libList) { //we should separate JavaFX methods and pull data from GUI First, then pass into this method once it is validated from the JavaFx functions. ListView can also be removed from arguments since it is a global variable.
        System.out.println("add");
        if(nameinput.equals("") || artistinput.equals("")) { //can be handled for frontend method
            //send error message
            return;
        }

        Song newEntry = new Song(nameinput.getText(), artistinput.getText(), albuminput.getText(), yearinput.getText()); //this should be front end
        if (libList.size() == 0) {
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
                        //Error
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
        selectSong(listView);
        FileConvert.save(libList, songLibrary);
    }

    private void editButton(ArrayList<Song> libList) { //should become an Eventhandler method, then call the backend.
        System.out.println("edit");
        editButton.setOnAction(f -> {
            if(!libList.isEmpty()) {
                edit(listView, libList, nameinput.getText(), artistinput.getText(), albuminput.getText(), yearinput.getText());
            }
        });
    }

    private void edit(final ListView<Song> listView, final ArrayList<Song> libList, final String song, final String artist, final String album, final String year) {
        System.out.println("edit");
        if(song.equals("") || artist.equals("")) { // should be handled in front end
            return;
        }

        for (int index = 0; index < libList.size(); index++) {
            Song entry = libList.get(index);
            if(entry.getName().compareTo(song) == 0 && entry.getArtist().compareTo(artist) == 0 && index != selectedIndex) {
                return;
            }
        }

        delete(listView, libList);
        add(listView, libList);
    }

    private void deleteButton(ArrayList<Song> libList) { //frontend javaFx code only, use Eventhandler here
        System.out.println("delete");
        deleteButton.setOnAction(g -> {
            if (!libList.isEmpty()) {
                delete(listView, libList);
            }
        });
    }

    private void delete(final ListView<Song> listView, final ArrayList<Song> libList) { //convert to backend once the JavaFX method is created.
        System.out.println("delete");
        libList.remove(selectedIndex);
        listView.getItems().clear();

        for(Song x : libList){
            listView.getItems().add(x);
        }

        if(libList.size() > selectedIndex) {
            listView.getSelectionModel().select(selectedIndex);
            selectSong(listView);
        } else if(libList.size() == selectedIndex) {
            selectedIndex = selectedIndex - 1;
            listView.getSelectionModel().select(selectedIndex);
            selectSong(listView);
        } else if(libList.size() == 0) {
            selectSong(listView);
        }

        FileConvert.save(libList, songLibrary);
    }

    private void selectSong(final ListView<Song> listView) {
        if (listView.getSelectionModel().getSelectedItem() != null) {
            selectedname.setText(listView.getSelectionModel().getSelectedItem().getName());
            selectedartist.setText(listView.getSelectionModel().getSelectedItem().getArtist());
            selectedalbum.setText(listView.getSelectionModel().getSelectedItem().getAlbum());
            selectedyear.setText(listView.getSelectionModel().getSelectedItem().getYear());
        }
    }
}
