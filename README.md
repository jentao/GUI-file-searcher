**Description of the File Index**

One way to represent a collection of files is to think of it as a map from words to a map from filenames to a list of locations in  the file where the word occurs. This allows files and locations where a given word occurs to be looked up quickly.

**Description of the Occurrence Index**

One way to represent a file in memory is to think of it as a map from words to a list of locations in the file where the word occurs. This is useful because most text files are composed of a relatively small set of words that repeat many times, wasting  data. As a bonus, it is easy to find where words occur in a file, or whether the word occurs in the file at all.

* The command **~make~** compiles all classes into the build/classes/index/ directory. Creates the directory if it does not yet exist. Will also create the javadocs and "submission.jar" file in the respective directories explained in detail later.

* The command **~make occurrenceIndex~** compiles the OccurrenceIndex class into the build/classes/index/ directory. Creates the directory if it does not yet exist.

* The command **~make fileIndex~** compiles the FileIndex class into the build/classes/index/ directory. Creates the directory if it does not yet exist.

* The command **~make fileSearch~** compiles the FileSearch class into the build/classes/index/ directory. Creates the directory if it does not yet exist.

* The command **~make search~** runs the FileSearch command line application.

* The command **~make GUIsearch~** runs the FileSearch GUI application.

* The command **~make javadocs~** creates the "index.html" file at "build/docs/" directory. Creates the directory if it does not yet exist.

* The command **~make jar~** creates the "submission.jar" file at "build/" directory. Will compile all classes beforehand. Creates the directory if it does not yet exist.

* The command **~make compile~** compiles all classes found in the src/index/ directory into the build/classes/index/ directory.
Creates the directory if it does not yet exist.

* The command **~make tests~** compiles the test classes into the build/testClasses/index/ drectory. Creates the directory if it does not yet exist. As the tests rely on JUnit and Hamcrest, both will be downloaded via Maven if they have not been downloaded yet.

* The command **~make run~** runs the test classes to test the project, compiling requisite classes if needed.

* The command **~make clean~** deletes the files which were created by Makefile.
