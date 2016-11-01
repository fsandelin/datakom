JFLAGS = -g
JC = javac
.SUFFIXES: .java .class

.java.class:
	$(JC) $(FLAGS) $*.java

CLASSES = \
	Game.java

default: all

all: $(CLASSES:.java=.class)

run:
	java Game

clean:
	rm -f *.class
	rm -f *~
	rm -f *#
