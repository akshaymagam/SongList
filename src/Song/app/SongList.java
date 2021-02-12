package Song.app;


import java.util.*;

public class SongList{

    private final Map<Integer,Song> songlist;
    private int counter = 0;

    public SongList() {
        songlist = new HashMap<>();
    }

//    public static void main(String[] args) { //test main
//        SongList test = new SongList();
//        Song song1 = new Song("a", "a", "a", 1999);
//        Song song2 = new Song("ab","b","b",1999);
//        Song song3 = new Song("b","b","b",1999);
//        Song song4 = new Song("bb","b","b",1999);
//        test.add(song1);
//        test.add(song2);
//        test.add(song3);
//        test.add(song4);
//        List<Song> output = test.getSonglist();
//        for(Song temp: output){
//            System.out.println(temp.toString());
//        }
//    }

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
        songlist.put(counter++, newsong);
    }

    public Integer find(Song target){
        Set <Integer> songListkeys = songlist.keySet();
        for(Integer temp: songListkeys){
            Song tempsong = songlist.get(temp);
            if(!target.getName().equals(tempsong.getName())) continue;
            if(!target.getArtist().equals(tempsong.getArtist())) continue;
            if(!target.getAlbum().equals(tempsong.getAlbum())) continue;
            if(!(target.getYear() == tempsong.getYear())) continue;
            return temp;
        }
        return null;
    }

    /*public void edit() {

    }*/
}
