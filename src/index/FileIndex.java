package index;

import java.util.Set;
import java.util.AbstractSet;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.lang.IllegalArgumentException;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.io.*;
import java.util.regex.*;

import index.*;



/**
 * Keeps track of files based on their filepaths along with the words
 * that are stored in each file.
 * 
 * This implementation relies on the OccurrenceIndex class.
 */
public class FileIndex {
  private Map<String, OccurrenceIndex> fileIndex;

  /**
   * Constructs a new empty file index.
   */
  public FileIndex() {
    fileIndex = new HashMap<>();
  }

  /**
   * Adds a new file to the file index, along with the associated OccurenceIndex
   * of words.
   * @param filePath A String that contains the full file path of the desired file.
   * @param index An occurrence index that maps each word in the file to the index the first character starts at.
   * @throws IllegalArgumentException if either the given file path or OccurrenceIndex are null.
   */
  public void add(String filePath, OccurrenceIndex index) {
    // Will overwrite previous file if already exists
    fileIndex.put(formatFilePath(filePath), index);
  }

  /**
   * Returns a set view of all files that contain a specified word.
   * @param word The word to search for.
   * @return A set of file paths that the word was found in.
   * @throws IllegalArgumentException if the word is null or an empty string.
   */
  public Set<String> listAll(String word) {
    if (word == null || word.equals("")) {
      throw new IllegalArgumentException("Word must not be null or blank!");
    }
    Set<String> fileList = new TreeSet<>();
    for (String fileName : fileIndex.keySet()) {
      if (!fileIndex.get(fileName).getLocations(word).isEmpty()) {
        fileList.add(fileName);
      }
    }
    return fileList;
  }

  /**
   * Returns a list view of all locations in the specified file that a given word was found in.
   * @param filePath A string that contains the full file path to search through.
   * @param word The word to search the specified file for.
   * @return A list of all indicies in which the word was found in the specified file.
   * @throws IllegalArgumentException if either the file path or word are null or empty strings.
   */
  public List<Integer> getLocations(String filePath, String word) {
    if(word == null || word.equals("")) {
      throw new IllegalArgumentException("Cannot search for a null or empty string!");
    }
    return fileIndex.get(formatFilePath(filePath)).getLocations(word);
  }

  /**
   * Returns a set view of all words that are found in all the files stored in the file index.
   * @return A set of all words that are found in all the files stored in the file index.
   */
  public Set<String> getAllWords() {
    // Iterates over all Occurrence Indexes to search for words rather than
    // storing a set of all words as a field.
    // This is so that if a remove method is ever implemented,
    // we would need to iterate over all indicies when removing to make sure
    // it doesn't appear again. So either this method or a future remove method
    // will be in O(n) time while the other is in O(1) time. I chose to have this one in O(n).
    Set<String> wordList = new TreeSet<>();
    for (String path : fileIndex.keySet()) {
      wordList.addAll(fileIndex.get(path).allWords());
    }
    return wordList;
  }

  /**
   * Creates a new FileIndex from the given directory, recursively adding OccurrenceIndexes based
   * on each .txt file found in the directory.
   * @param path file path of the directory to iterate over
   * @requires file at path exists and is a directory
   * @return FileIndex containing OccurranceIndexes of all .txt files within the given directory
   * @throws FileNotFoundException if the named directory cannot be accessed
   * @throws IOException if an I/O issue arises
   */
  public static FileIndex createIndexFrom(String path) throws IOException{
    FileIndex index = new FileIndex();
    File file = new File(path);
    if (!file.exists()) {
      throw new FileNotFoundException("Directory does not exist");

    } else if(!new File(path).isDirectory()) {
      throw new IllegalArgumentException("This is not a directory");

    } else { //file exists and is a directory
      Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<Path>() {
        //anonymous inner class for overriding a SimpleFileVisitor
        //when used with the Files.walkFileTree method, the SimpleFileVisitor
        //recursively iterates through files, depth-first,
        //and executes visitFile using each of them.
        //for more information on this structure, see: https://docs.oracle.com/javase/tutorial/essential/io/walk.html

        @Override
        public FileVisitResult visitFile(Path current, BasicFileAttributes attrs) throws IOException {
          if(current.toString().endsWith(".txt")) {
            String pathName = current.toString();
            index.add(formatFilePath(pathName), OccurrenceIndex.parse(pathName));
          }
          return FileVisitResult.CONTINUE;
        }

      });
    }
    return index;
  }

  // Formats the given file paths so that they always start and end with a "/".
  private static String formatFilePath(String filePath) {
    if (filePath == null || filePath.equals("")) {
      throw new IllegalArgumentException("File path cannot be null or blank");
    }
    String formattedFilePath = filePath;
    if (!filePath.startsWith("/")) {
      formattedFilePath = "/" + formattedFilePath;
    }
    if (!filePath.endsWith("/")) {
      formattedFilePath = formattedFilePath + "/";
    }
    return formattedFilePath;
  }
}