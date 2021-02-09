package Song.app;

import java.util.*;

public class CustomComparator implements Comparator<Song>{
    @Override
    public int compare(Song song1, Song song2){
        return song1.getName().compareTo(song2.getName());
    }
}
