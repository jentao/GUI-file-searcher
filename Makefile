SOURCE_PATH	= src
CLASS_PATH	= build/classes
CLASS_TEST_PATH	= build/testClasses
PACKAGE		= index
PACKAGE_CLASS_PATH	= $(CLASS_PATH)/$(PACKAGE)

all: jar javadocs
.PHONY: all

search: fileSearch
	java -cp $(CLASS_PATH) index.FileSearch
.PHONY: search

GUIsearch: GUIFileSearch
	java -cp $(CLASS_PATH) index.GUIFileSearch
.PHONY: search

GUIFileSearch: occurrenceIndex fileIndex $(SOURCE_PATH)/$(PACKAGE)/GUIFileSearch.java
	javac -d $(CLASS_PATH) -cp $(CLASS_PATH) $(SOURCE_PATH)/$(PACKAGE)/GUIFileSearch.java
.PHONY: fileSearch

fileSearch: occurrenceIndex fileIndex $(SOURCE_PATH)/$(PACKAGE)/FileSearch.java
	javac -d $(CLASS_PATH) -cp $(CLASS_PATH) $(SOURCE_PATH)/$(PACKAGE)/FileSearch.java
.PHONY: fileSearch

fileIndex: occurrenceIndex $(SOURCE_PATH)/$(PACKAGE)/FileIndex.java
	javac -d $(CLASS_PATH) -cp $(CLASS_PATH) $(SOURCE_PATH)/$(PACKAGE)/FileIndex.java
.PHONY: fileIndex

occurrenceIndex: $(SOURCE_PATH)/$(PACKAGE)/OccurrenceIndex.java
	javac -d $(CLASS_PATH) $(SOURCE_PATH)/$(PACKAGE)/OccurrenceIndex.java
.PHONY: occurrenceIndex

compile: fileSearch GUIFileSearch
.PHONY: compile

javadocs: compile
	javadoc -d build/docs -sourcepath $(SOURCE_PATH) $(PACKAGE) -tag requires:a:"Requires:" -tag effects:a:"Effects:" -tag modifies:a:"Modifies:"
.PHONY: javadocs

jar: compile 
	jar cf build/submission.jar -C $(CLASS_PATH) .
.PHONY: jar

tests: build/lib/junit-4.13.jar build/lib/hamcrest-2.2.jar $(CLASS_TEST_PATH)/$(PACKAGE)/OccurrenceIndexTest.class $(CLASS_TEST_PATH)/$(PACKAGE)/FileIndexTest.class
.PHONY: tests

run: compile tests sampledocs
	java -cp $(CLASS_PATH):$(CLASS_TEST_PATH):build/lib/junit-4.13.jar:build/lib/hamcrest-2.2.jar org.junit.runner.JUnitCore $(PACKAGE).OccurrenceIndexTest
	java -cp $(CLASS_PATH):$(CLASS_TEST_PATH):build/lib/junit-4.13.jar:build/lib/hamcrest-2.2.jar org.junit.runner.JUnitCore $(PACKAGE).FileIndexTest
.PHONY: run

$(CLASS_TEST_PATH)/$(PACKAGE)/OccurrenceIndexTest.class: $(PACKAGE_CLASS_PATH)/OccurrenceIndex.class $(SOURCE_PATH)/test/OccurrenceIndexTest.java build/lib/junit-4.13.jar
	javac -d $(CLASS_TEST_PATH) -cp $(CLASS_PATH):build/lib/junit-4.13.jar $(SOURCE_PATH)/test/OccurrenceIndexTest.java

$(CLASS_TEST_PATH)/$(PACKAGE)/FileIndexTest.class: $(CLASS_TEST_PATH)/$(PACKAGE)/OccurrenceIndexTest.class $(PACKAGE_CLASS_PATH)/FileIndex.class $(SOURCE_PATH)/test/FileIndexTest.java
	javac -d $(CLASS_TEST_PATH) -cp $(CLASS_PATH):build/lib/junit-4.13.jar $(SOURCE_PATH)/test/FileIndexTest.java

$(SOURCE_PATH)/test/OccurrenceIndexTest.java:
	javac -d $(CLASS_TEST_PATH) -cp $(CLASS_PATH):build/lib/junit-4.13.jar $(SOURCE_PATH)/test/OccurrenceIndexTest.java

$(SOURCE_PATH)/test/FileIndexTest.java:
	javac -d $(CLASS_TEST_PATH) -cp $(CLASS_PATH):build/lib/junit-4.13.jar $(SOURCE_PATH)/test/FileIndex.java

build/lib/junit-4.13.jar: build/lib/hamcrest-2.2.jar
	mvn dependency:copy -Dartifact=junit:junit:4.13 -DoutputDirectory=build/lib

build/lib/hamcrest-2.2.jar:
	mvn dependency:copy -Dartifact=org.hamcrest:hamcrest:2.2 -DoutputDirectory=build/lib

build/samples:
	mkdir build/samples
	mkdir build/samples/level1
	mkdir build/samples/level1/branch1
	mkdir build/samples/level1/branch2
	mkdir build/samples/empty

sampledocs : build/samples
	touch build/samples/empty.txt; echo "" > build/samples/empty.txt
	touch build/samples/regular.txt; echo "apple bat cat Apple Bat Cat" > build/samples/regular.txt
	touch build/samples/spaces.txt; echo "               " > build/samples/spaces.txt
	touch build/samples/level1/branch1/text.txt; echo "dog and cat and dog" > build/samples/level1/branch1/text.txt
	touch build/samples/level1/branch2/text.txt; echo "frog on a log on a bog" > build/samples/level1/branch2/text.txt
.PHONY: sampledocs

clean:
	rm -rf build
.PHONY: clean
