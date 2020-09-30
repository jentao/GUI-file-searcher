package index;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;
import java.util.regex.Pattern;



/**
 * This class maps from words to a list of locations which can be repesented as indexes.
 * Also, this class helps avoiding the repeatation of same words.
 */
public class OccurrenceIndex implements Serializable {

    private static final String EXTENSION = "idx"; //file extension to use for serialization

    /**
     * keeps the words and its locations in indexes.
     */
    private HashMap<String, ArrayList<Integer>> listMap; 

    /**
     * Constructor of OccurrenceIndex class.
     */
    public OccurrenceIndex(){
        this.listMap = new HashMap<String, ArrayList<Integer>>();
    }

    /**
     * Adds the word and the index into the list of locations in the hashMap.
     * @param word The word value
     * @param index The index value
     * @throws IllegalArgumentException if word == "" or word == null or index &lt; 0
     */
    public void add(String word, int index){
        if (word == "" || word == null) {
            throw new IllegalArgumentException("args was null!");
          }
        if (index < 0){
            throw new IndexOutOfBoundsException();
        }
        if (listMap.containsKey(word)){    // Checking if the hashMap contains the word
            listMap.get(word).add(index);
        } else {
            ArrayList<Integer> listOfIndex = new ArrayList<Integer>();
            listOfIndex.add(index);  
            listMap.put(word,listOfIndex); // adding the word and its positions
        } 
    }

    /**
     * Returns the list of locations in which the specified word occurs in the index.
     * @param word The word value
     * @throws IllegalArgumentException if word == "" or word == null
     * @return locations The locations of the words in index if the list contains the word, otherwise empty list
     */
    public ArrayList<Integer> getLocations(String word){
        if (word == "" || word == null) {
            throw new IllegalArgumentException("args was null!");
          }
        ArrayList<Integer> locations = new ArrayList<Integer>();  // This ArrayList keeps the locations of words in index
        if (listMap.containsKey(word)){     // Checking if the hashMap contains the word
            locations = listMap.get(word);
        }
        return locations;   
    }
    
    /**
     * Returns the list of all the words which occur.
     * @return words The list of the words in index if the list contains words, otherwise empty list
     */
    public ArrayList<String> allWords(){
        ArrayList<String> words = new ArrayList<String>();
        for(String keys: listMap.keySet()){    // finding the words from the hashMap
            words.add(keys);
        }
        return words;
    }

    /**
     * Parses the words in a file at the given path into a new occurence index.
     * @param filePath the path of the file to index
     * @return a new OccurrenceIndex for the designated file
     * @throws FileNotFoundException if the named file does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading.
     * @throws IOException If an I/O error occurs
     */
    public static OccurrenceIndex parse(String filePath) 
                                  throws java.io.IOException, FileNotFoundException {
        FileReader in = new FileReader(filePath);
        OccurrenceIndex index = new OccurrenceIndex();
        int current; //FileReaders read chars as integer representations
        int position = -1; //Current position in file
        while (in.ready()){ //while there is file input to read
            current = in.read();
            position++;
            if (Character.isAlphabetic(current)) {
                String word = "";
                int startPos = position;
                do { //start constructing the word
                    word += Character.toLowerCase((char) current);
                    current = in.read();
                    position++;
                } while (Character.isAlphabetic(current));
                index.add(word, startPos);
            }
        }
        return index;
        /*
        Create a new FileInputStream for reading the file. while input is ready, Save the read character,
        then check if an alphabetic character is present. If so, go into 'indexing' mode- initialize a 'word' string,
        and continue adding characters to the word until a non-alphabetic character is reached OR the end of the file
        is reached. When a non-alphabetic character or the end of the file is found, add the word to the index,
        leave indexing mode, and continue the loop.
         */
    }

    /**
     * Saves a given occurence index to the given directory, named the given fileName, with the current extension
     * @param directory path to save the OccurenceIndex to
     * @param fileName name to save the OccurenceIndex as, without extension
     * @param index the OccurrenceIndex to save
     * @requires filePath != null
     * @throws IOException If an I/O error occurs
     */
    public static void save(String directory, String fileName, OccurrenceIndex index) throws IOException {
        try {
            if(!directory.endsWith("/")) directory += "/"; //adds trailing slash if needed
            File file = new File(directory + fileName + "." + EXTENSION);
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(index);
            out.close();

        } catch (NullPointerException e) {
            throw new IllegalArgumentException("File path cannot be null");
        }
    }

    /**
     * Loads an OccurenceIndex from the given file.
     * @param path path to load from
     * @return OccurenceIndex contained in the file
     * @throws IOException If an I/O error occurs
     * @throws ClassNotFoundException Class of a serialized object cannot be found
     */
    public static OccurrenceIndex load(String path) throws IOException, ClassNotFoundException {
        File file = new File(path);
        ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file));
        return (OccurrenceIndex)stream.readObject();
    }
}