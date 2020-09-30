package index;

import jdk.jfr.StackTrace;
import org.junit.Before;
import org.junit.Test;

import jdk.jfr.Timestamp;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;


public class OccurrenceIndexTest {
    OccurrenceIndex obj;

    @Before
    public void making_object(){
        obj = new OccurrenceIndex();
    }
    @Test
    public void test_getLocations(){
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(3);
        list.add(4);
        list.add(4);
        obj.add("the",3);
        obj.add("the",4);
        obj.add("the",4);
        obj.add("hi",10);
        assertEquals(list, obj.getLocations("the"));
    }
    @Test
    public void test_allWords(){
        ArrayList<String> list = new ArrayList<String>();
        list.add("the");
        list.add("hi");
        obj.add("the",3);
        obj.add("the",4);
        obj.add("the",4);
        obj.add("hi",10);
        assertEquals(list, obj.allWords());
    }
    @Test(expected=IndexOutOfBoundsException.class)
    public void test_IndexNegativeNumber() {
        obj.add("word",-1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_wordNull() {
        obj.add("",3);
    }

    @Test
    public void test_parseRegularFile() throws IOException {
        obj = OccurrenceIndex.parse("build/samples/regular.txt/");
        ArrayList<Integer> apple = new ArrayList<>();
        apple.add(0);
        apple.add(14);
        ArrayList<Integer> bat = new ArrayList<>();
        bat.add(6);
        bat.add(20);
        ArrayList<Integer> cat = new ArrayList<>();
        cat.add(10);
        cat.add(24);
        assertEquals(apple, obj.getLocations("apple"));
        assertEquals(bat, obj.getLocations("bat"));
        assertEquals(cat, obj.getLocations("cat"));
    }

    @Test
    public void test_parseBlankFile() throws IOException {
        obj = OccurrenceIndex.parse("build/samples/empty.txt/");
        ArrayList<String> words = new ArrayList<>();
        assertEquals(words, obj.allWords());
    }

    @Test
    public void test_parseSpaceFile() throws IOException {
        obj = OccurrenceIndex.parse("build/samples/spaces.txt/");
        ArrayList<String> words = new ArrayList<>();
        assertEquals(words, obj.allWords());
    }

    @Test
    public void test_saveAndLoad() throws IOException, ClassNotFoundException{
        obj.add("abc", 1);
        obj.add("def", 2);
        obj.add("abc", 3);
        OccurrenceIndex.save("build/samples/", "test1", obj);
        OccurrenceIndex afterLoad = OccurrenceIndex.load("build/samples/test1.idx");
        assertEquals(obj.getLocations("abc"), afterLoad.getLocations("abc"));
        assertEquals(obj.getLocations("def"), afterLoad.getLocations("def"));
    }

    @Test
    public void test_saveAndLoad_noTrailingSlash() throws IOException, ClassNotFoundException{
        obj.add("abc", 1);
        obj.add("def", 2);
        obj.add("abc", 3);
        OccurrenceIndex.save("build/samples", "test2", obj);
        OccurrenceIndex afterLoad = OccurrenceIndex.load("build/samples/test2.idx");
        assertEquals(obj.getLocations("abc"), afterLoad.getLocations("abc"));
        assertEquals(obj.getLocations("def"), afterLoad.getLocations("def"));
    }

    @Test(expected= FileNotFoundException.class)
    public void test_invalidSave() throws IOException{
        OccurrenceIndex.save("foo", "bar", obj);
    }

    @Test(expected= FileNotFoundException.class)
    public void test_invalidLoad() throws IOException, ClassNotFoundException{
        OccurrenceIndex.load("FUBAR");
    }
}

