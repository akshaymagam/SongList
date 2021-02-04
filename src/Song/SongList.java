package Song;

import java.util.ArrayList;
import java.util.*;

public class SongList{

    private final Map<Integer,Song> songlist;

    public SongList() {
        songlist = new HashMap<>();
    }

    public static void main(String[] args) {
        SongList test = new SongList();
        Song song1 = new Song("a", "a", "a", 1999);
        Song song2 = new Song("ab","b","b",1999);
        Song song3 = new Song("b","b","b",1999);
        Song song4 = new Song("bb","b","b",1999);
        test.add(song1);
        test.add(song2);
        test.add(song3);
        test.add(song4);
        List<Song> output = test.getSonglist();
        for(Song temp: output){
            System.out.println(temp.toString());
        }
    }

    public List<Song> getSonglist() {
        Set <Integer> songListkeys = songlist.keySet();
        ArrayList<Song> sortedsongs = new ArrayList<>();
        for(Integer temp: songListkeys){
            sortedsongs.add(songlist.get(temp));
        }
        Collections.sort(sortedsongs, new CustomComparator());
        return sortedsongs;
    }

    public void add(Song newsong){
        songlist.put(songlist.size() + 1, newsong);
    }

    /*public Song find(Song target){
        for(Song temp : songlist){
            if(!target.getName().equals(temp.getName())) continue;
            if(!target.getArtist().equals())

        }
    }

    public void edit(Song current){

    }*/
}
