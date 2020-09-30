package index;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

import index.*;


public class FileIndexTest {

  @Test
  public void testAdd() {
    String reallyLongString = "Oh Youre approaching me Instead of running away " +
                            "youre coming right to me Even though your " + 
                            "grandfather Joseph told you the secret of Za Warudo " +
                            "like an exam student scrambling to finish the last " +
                            "problems on an exam until the last moments before " +
                            "the chime I cant beat the shit out of you without " +
                            "getting closer Oh ho Then come as close as you like " +
                            "Too slow too slow Za Warudo is the ultimate stand " +
                            "Even without his power to <spoiler> his speed and " +
                            "power far exceed that of your Star Platinum So its " +
                            "the same type of Stand as Star Platinum Not much " +
                            "range but immense power and precise movements I " +
                            "wanted to try a little test to find out just how " +
                            "powerful Za Warudo is was compared to your stand " +
                            "Though it appears a test was hardly necessary Test " +
                            "Is that what you call just patting me and not even " +
                            "enough to hurt Well I guess you ripped my 200000 " +
                            "yen pants though Why are you Joestars so stubborn " +
                            "about admitting defeat Hmph I suppose I shall rise " +
                            "to your silly provocation and test you just a bit " +
                            "more ORA ORA ORA ORA ORA ORA ORA ORA ORA ORA ORA " +
                            "ORA ORA ORA ORA ORA ORA ORA ORA ORA ORA ORA ORA ORA " +
                            "ORA ORA ORA ORA ORA ORA ORA ORA Shall we compare " +
                            "the speed of our attack rushes MUDA MUDA MUDA MUDA " +
                            "MUDA MUDA MUDA MUDA MUDA MUDA MUDA MUDA MUDA MUDA " +
                            "MUDA MUDA MUDA MUDA MUDA MUDA MUDA MUDA MUDA MUDA " +
                            "MUDA ORA ORA ORA ORA ORA ORA ORA ORA ORA ORA ORA " +
                            "ORA ORA ORA ORA ORA ORA ORA ORA ORA ORA ORA ORA ORA " +
                            "ORA ORA ORA ORA ORA ORA ORA ORA MUDA MUDA MUDA MUDA " +
                            "MUDA MUDA MUDA MUDA MUDA MUDA MUDA MUDA MUDA MUDA " +
                            "MUDA MUDA MUDA MUDA MUDA MUDA MUDA MUDA MUDA MUDA " +
                            "MUDA ORA ORA ORA ORA ORA ORA ORA ORA ORA ORA ORA " +
                            "ORA ORA ORA ORA ORA ORA ORA ORA ORA ORA ORA ORA ORA " +
                            "ORA ORA ORA ORA ORA ORA ORA ORA MUDA MUDA MUDA MUDA " +
                            "MUDA MUDA MUDA MUDA MUDA MUDA MUDA MUDA MUDA MUDA " +
                            "MUDA MUDA MUDA MUDA MUDA MUDA MUDA MUDA MUDA MUDA " +
                            "MUDA ORA ORA ORA ORA ORA ORA ORA ORA ORA ORA ORA " +
                            "ORA ORA ORA ORA ORA ORA ORA ORA ORA ORA ORA ORA ORA " +
                            "ORA ORA ORA ORA ORA ORA ORA ORA";
    String[] indexWords1 = reallyLongString.split(" ");
    String reallyLongString2 = "My name is Yoshikage Kira Im 33 years old " +
                                "My home is in the upscale neighborhood of " +
                                "northeast Morioh I am currently a bachelor " +
                                "I work for the Kameyu Market regional management " +
                                "office Everyday I work late and dont get home " +
                                "until 8 PM I dont smoke I only drink occasionally " +
                                "Im always in bed by 11 PM so i can get a full " +
                                "8 hours of sleep Before I sleep I drink a warm " +
                                "glass of milk and do my stretching exercises " +
                                "for about 20 minutes That way I fall asleep " +
                                "quickly and sleep soundly until morning Every " +
                                "morning I wake up refreshed and stress free like " +
                                "a baby During my yearly check ups my doctor " +
                                "always says everythings perfect I was just " +
                                "explaining what I do to go about my life quietly " +
                                "with a calm heart If you keep worrying about " +
                                "winning or losing itll just stick in your " +
                                "mind and youll be troubled You wont be able " +
                                "to sleep at night if youre worrying about " +
                                "your enemies My attitude towards society as " +
                                "a whole has made me a very content person " +
                                "But if anyone stands in my way I wont lose";
    String[] indexWords2 = reallyLongString2.split(" ");
    FileIndex fileIndex = new FileIndex();
    OccurrenceIndex occIndex1 = new OccurrenceIndex();
    OccurrenceIndex occIndex2 = new OccurrenceIndex();
    OccurrenceIndex occIndex3 = new OccurrenceIndex();
    OccurrenceIndex occIndex4 = new OccurrenceIndex();
    for (int i = 0; i < indexWords1.length; i++) {
      occIndex1.add(indexWords1[i], i);
    }
    for (int i = 0; i < indexWords2.length; i++) {
      occIndex2.add(indexWords2[i], i);
    }
    String manyA = "a";
    for (int i = 0; i < 1000; i++) {
      occIndex3.add(manyA, i);
      manyA = "a" + manyA + "a";
    }
    occIndex4.add("Kira", 0);
    String path1 = "this/is/a/path/name";
    String path2 = "this/is/also/a/path/name/";
    String path3 = "/this/is/also/also/a/path/name";
    String path4 = "/this/is/the/last/path/name/";
    fileIndex.add(path1, occIndex1);
    fileIndex.add(path2, occIndex2);
    fileIndex.add(path3, occIndex3);
    fileIndex.add(path4, occIndex4);
    assertTrue(fileIndex.listAll("MUDA").contains("/" + path1 + "/"));
    assertTrue(fileIndex.listAll("33").contains("/" + path2));
    assertTrue(fileIndex.listAll("aaa").contains(path3 + "/"));
    assertTrue(fileIndex.listAll("Kira").contains(path4) && fileIndex.listAll("Kira").contains("/" + path2));
    fileIndex.add(path1, occIndex4);
    assertFalse(fileIndex.listAll("MUDA").contains("/" + path1 + "/"));
    assertTrue(fileIndex.listAll("Kira").contains(path4) && 
               fileIndex.listAll("Kira").contains("/" + path2) &&
               fileIndex.listAll("Kira").contains("/" + path1 + "/"));
  }

  @Test
  public void testListAll() {
    FileIndex fileIndex = new FileIndex();
    String manyA = "a";
    for (int i = 0; i < 100; i++) {
      OccurrenceIndex occIndex = new OccurrenceIndex();
      occIndex.add("a", 0);
      fileIndex.add(manyA, occIndex);
      manyA = "a" + manyA + "a";
    }
    manyA = "a";
    Set<String> allPaths = fileIndex.listAll("a");
    for (int i = 0; i < 100; i++) {
      assertTrue(allPaths.contains("/" + manyA + "/"));
      manyA = "a" + manyA + "a";
    }
  }

  @Test
  public void testGetLocations() {
    FileIndex fileIndex = new FileIndex();
    OccurrenceIndex occIndex1 = new OccurrenceIndex();
    OccurrenceIndex occIndex2 = new OccurrenceIndex();
    for (int i = 0; i < 100; i++) {
      occIndex1.add(Integer.toString(i), i);
    }
    fileIndex.add("/path1/", occIndex1);
    fileIndex.add("/path2/", occIndex2);
    for (int i = 0; i < 100; i++) {
      List<Integer> indices = fileIndex.getLocations("/path1/", Integer.toString(i));
      assertTrue(indices.contains(i));
    }
    assertTrue(fileIndex.getLocations("/path2/", "foo").isEmpty());
  }

  @Test
  public void testGetAllWords() {
    FileIndex fileIndex = new FileIndex();
    OccurrenceIndex occIndex1 = new OccurrenceIndex();
    String manyA = "a";
    for (int i = 0; i < 1000; i++) {
      occIndex1.add(manyA, i);
      manyA = "a" + manyA + "a";
    }
    fileIndex.add("path", occIndex1);
    manyA = "a";
    Set<String> wordsList = fileIndex.getAllWords();
    for (int i = 0; i < 1000; i++) {
      assertTrue(wordsList.contains(manyA));
      manyA = "a" + manyA + "a";
    }
    OccurrenceIndex occIndex2 = new OccurrenceIndex();
    occIndex2.add("b", 12);
    fileIndex.add("path2", occIndex2);
    manyA = "a";
    wordsList = fileIndex.getAllWords();
    for (int i = 0; i < 1000; i++) {
      assertTrue(wordsList.contains(manyA));
      manyA = "a" + manyA + "a";
    }
    assertTrue(wordsList.contains("b"));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testAddNull() {
    FileIndex fileIndex = new FileIndex();
    fileIndex.add("test", null);
    fileIndex.add(null, new OccurrenceIndex());
  }

  @Test
  public void test_nestedIteration() throws IOException {
    FileIndex index = FileIndex.createIndexFrom("build/samples/level1");
    Set<String> wordsList = index.getAllWords();
    Set<String> masterWordsList = new TreeSet<>();
    masterWordsList.add("frog");
    masterWordsList.add("on");
    masterWordsList.add("a");
    masterWordsList.add("log");
    masterWordsList.add("bog");
    masterWordsList.add("dog");
    masterWordsList.add("and");
    masterWordsList.add("cat");
    ArrayList<Integer> dogLocationsList = new ArrayList<>();
    dogLocationsList.add(0);
    dogLocationsList.add(16);
    assertEquals(masterWordsList, wordsList);
    assertEquals(dogLocationsList, index.getLocations("build/samples/level1/branch1/text.txt", "dog"));
  }

  @Test
  public void test_emptyIteration() throws IOException {
    FileIndex index = FileIndex.createIndexFrom("build/samples/empty");
    Set<String> empty = new TreeSet<>();
    assertEquals(empty, index.getAllWords());
  }

}