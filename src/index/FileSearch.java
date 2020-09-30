package index;

import index.*;
import java.io.*;
import java.util.*;
import java.nio.file.Path;

/**
 * A command line application that asks a word and a directory from the user one at a time,
 * it gives user the capability to search for the index of the word in the txt file within
 * that directory, and it also gives user the capability to see the nearest characters.
 * 
 * This implementation relies on the OccurrenceIndex and FileIndex class.
 */
public class FileSearch {

    /**
     * Search for indicies of a given word in a given directory while interacting
     * with the user.
     * @param args command-line arguments
     * @throws IOException if an I/O issue arises
     * @throws FileNotFoundException if the named file/directory cannot be accessed
    */
    public static void main(String args[]) throws IOException, FileNotFoundException { 
        Scanner console = new Scanner(System.in); 
        printIntro();
        System.out.print("Do you want to start file-search program? (yes/no) ");
        String dirAnswer = console.next(); 
        while (dirAnswer.equals("yes")) {
            String dirName = getDirectory(console);
            String word = getWord(console);
            List<String> fileList = getFiles(dirName, word);
            if (!fileList.isEmpty()) {
                System.out.print("Do you want to search for the index of [" + word + "] in the above file(s)? (yes/no) ");
                String fileAnswer = console.next();
                while (fileAnswer.equals("yes")) {
                    String fileName = getFile(console, word, fileList);
                    List<Integer> indexList = getIndicies(console, word, fileName);
                    System.out.print("Do you want to see the 100 nearest character around an index? (yes/no) ");
                    String indexAnswer = console.next();
                    while (indexAnswer.equals("yes")) {
                        printNearby(console, word, fileName, indexList);
                        System.out.print("Do you want to see the 100 nearest character around another index? (yes/no) ");
                        indexAnswer = console.next();
                    }
                    System.out.print("Do you want to search for the index of [" + word + "] in another file? (yes/no) ");
                    fileAnswer = console.next();
                }
            } else {
                System.out.println("No txt file contains the word [" + word + "].");
            }
            System.out.print("Do you want to start file-search program again? (yes/no) ");
            dirAnswer = console.next(); 
        }
        System.out.println("Bye <3");
    } 

    /**
     * Print the introduction of the program to the user.
    */
    public static void printIntro() {
        System.out.println("This command line application allows you to search for " + 
                           "the index of a provided word (case insensitive) in the " +
                           "txt file(s) of the provided directory. And it could also " + 
                           "provide the 100 characters (or less) nearest to the " + 
                           "provided index of the word.");      
        System.out.println();   
    }

    /**
     * Ask the user for a valid alphabetic word, and return that word.
     * @param console Scanner used for interacting with the user
     * @return a lowercase alphabetic word
    */
    public static String getWord(Scanner console) {
        System.out.print("Please give me a word (must be alphabetic): ");
        String word = console.next();
        while (!word.matches("^[a-zA-Z]+$")) {
            System.out.println("This is not a valid word.");
            System.out.print("Please try again: ");
            word = console.next();
        }
        return word.toLowerCase();
    }

    /**
     * Ask the user for a valid filename to search for the provided word,
     * and return the filename.
     * @param console Scanner used for interacting with the user
     * @param word provided word used for searching
     * @param fileList a list of txt files that contains the provided word
     * @return a filename
    */
    public static String getFile(Scanner console, String word, List<String> fileList) {
        System.out.print("Please give me a file to search for the word [" + word + "]: ");
        String fileName = console.next();
        while (!fileList.contains(fileName)) {
            System.out.println("This file does not contain the word.");
            System.out.print("Please try again: ");
            fileName = console.next();
        }
        return fileName;
    }

    /**
     * Return a list of txt files in the provided directory that contains the provided word.
     * @param dirName the name of the directory used for searching
     * @param word provided word used for searching
     * @throws IOException if an I/O issue arises
     * @requires dirName to be a valid existing directory
     * @requires word to be a lowercase alphabetic word
     * @return a list of txt files
    */
    public static List<String> getFiles(String dirName, String word) throws IOException {
        FileIndex fi = FileIndex.createIndexFrom(dirName);
        Set<String> fileSet = fi.listAll(word);
        List<String> fileList = new ArrayList<String>(fileSet); 
        Collections.sort(fileList);
        System.out.println(); 
        System.out.println("The word [" + word + "] is contained in: " + fileList);
        System.out.println();
        return fileList;
    }

    /**
     * Ask the user for a valid existing directory for searching,
     * and return the name of the directory.
     * @param console Scanner used for interacting with the user
     * @return the name of a valid existing directory
    */
    public static String getDirectory(Scanner console) {
        System.out.print("Please give me a directory (start from \"src\"): ");
        String dirName = console.next();
        File dir = new File(dirName);
        while (!dir.exists() || !dir.isDirectory()) {
            System.out.println("This directory does not exist.");
            System.out.print("Please try again: ");
            dirName = console.next();
            dir = new File(dirName);
        }
        return dirName;
    }

    /**
     * Return a list of indicies of the provided word in a provided txt file.
     * @param console Scanner used for interacting with the user
     * @param word provided word used for searching
     * @param fileName provided file used for searching
     * @throws IOException if an I/O issue arises
     * @throws FileNotFoundException if the named file/directory cannot be accessed
     * @requires word to be a lowercase alphabetic word
     * @requires fileName to be the name of a txt file that contains the provided word
     * @return a list of indicies
    */
    public static List<Integer> getIndicies(Scanner console, String word, String fileName)
                                throws IOException, FileNotFoundException {
        List<Integer> indexList = new ArrayList<Integer>();
        String filePath = new File(fileName).getPath().substring(1);
        OccurrenceIndex oi = OccurrenceIndex.parse(filePath);
        indexList = oi.getLocations(word);
        Collections.sort(indexList);
        System.out.println(); 
        System.out.println("Index of the word: " + indexList);
        System.out.println(); 
        return indexList;
    }

    /**
     * Print the nearby characters of the provided index of the provided word in 
     * a provided file.
     * @param console Scanner used for interacting with the user
     * @param word provided word used for searching
     * @param fileName provided file used for searching
     * @param indexList provided list of indicies of the word in the file
     * @throws IOException if an I/O issue arises
     * @throws FileNotFoundException if the named file/directory cannot be accessed
     * @requires fileName to be the name of a txt file that contains the provided word
    */
    public static void printNearby(Scanner console, String word, String fileName, List<Integer> indexList)
                       throws IOException, FileNotFoundException {
        System.out.print("Please give me an index to display nearby for the word [" + word + "]: ");
        int index = console.nextInt();
        while (!indexList.contains(index)) {
            System.out.println("This is not a valid index.");
            System.out.print("Please try again: ");
            index = console.nextInt();
        }
        File file = new File(fileName.substring(1));
        long fileLength = file.length();
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        byte[] before;
        byte[] after;
        int remainder;
        // If the file is less than 100 chacters, print the while file
        if (fileLength < 100) { 
            before = new byte[(int) fileLength];
            raf.seek(0);
            raf.read(before);
            after = new byte[0];
        } else if (index < 50) { // Print more characters after the index.
            remainder = 100 - index;
            before = new byte[index];
            raf.seek(0);
            raf.read(before);
            after = new byte[remainder];
            raf.seek(index);
            raf.read(after);
        } else if (fileLength - ((long) index) < 50) { // Print more characters before the index.
            remainder = (int) (fileLength - ((long) index));
            before = new byte[100 - remainder];
            raf.seek(index - (100 - remainder));
            raf.read(before);
            after = new byte[remainder];
            raf.seek(index);
            raf.read(after);
        } else { // Print 50 characters before the index, and 50 characters from the index.
            before = new byte[50];
            raf.seek(index - 50);
            raf.read(before);
            after = new byte[50];
            raf.seek(index);
            raf.read(after);
        }
        String output = (new String(before)) + (new String(after));
        System.out.println();
        System.out.println(output);
        System.out.println();
    }

}