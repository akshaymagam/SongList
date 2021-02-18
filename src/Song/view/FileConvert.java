package Song.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import Song.app.Song;


public class FileConvert {
    static ArrayList<Song> read(File songLibrary) throws FileNotFoundException {
        //Load saved user song library from previous session
        try{
            if(songLibrary.createNewFile()) {
                System.out.println("Created new songLibrary text file");
            } else {
                System.out.println("songLibrary.txt already exists");
            }
        } catch(NullPointerException | IOException e) {
            System.out.println("Error in file creation: Null pointer or IO exception");
            System.exit(1);
        }

        FileReader fileReader = new FileReader(songLibrary);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        ArrayList<Song> libList = new ArrayList<Song>();
        try{
            while((line = bufferedReader.readLine()) != null) {
                String entryProperties[] = line.split("\\|");

                if(entryProperties.length == 4) {
                    String year = entryProperties[3].substring(2);
                    Song entry = new Song(entryProperties[0], entryProperties[1],
                            entryProperties[2], year);
                    libList.add(entry);
                } else if(entryProperties.length == 3){
                    if(entryProperties[2].startsWith("##")){
                        String year = entryProperties[2].substring(2);
                        Song entry = new Song(entryProperties[0], entryProperties[1], "", year);
                    }
                    else{
                        Song entry = new Song(entryProperties[0], entryProperties[1],
                                entryProperties[2], "");
                    }
                } else if(entryProperties.length == 2){
                    Song entry = new Song(entryProperties[0], entryProperties[1], "", "");
                }
            }
            fileReader.close();
        } catch(IOException e) {
            System.out.println("Error in reading file");
            e.printStackTrace();
        }

        return libList;
    }

    static void save(ArrayList<Song> libList, File songLibrary) {
        String newLib = "";
        for(Song entry:libList) {
            newLib = newLib + entry.getName() + "|" + entry.getArtist() + "|" + entry.getAlbum() + "|##" + entry.getYear() + "\n";
        }

        try {
            FileWriter writer = new FileWriter(songLibrary, false);
            writer.write(newLib);
            writer.close();
        } catch(IOException e) {
            System.out.println("Error writing to song library text file");
            e.printStackTrace();
        }
    }
}
