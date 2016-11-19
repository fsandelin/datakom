#----------------------
#-------Variables------
#----------------------
JFLAGS = -g
JC = javac
CLASSES = \
	Player.java \
	Board.java \
	Game.java \

#----------------------
#-------Modules--------
#----------------------
.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

default: all

all: $(CLASSES:.java=.class)

run: all
	java Game

clean:
	rm -f *.class
	rm -f *~
	rm -f *#

