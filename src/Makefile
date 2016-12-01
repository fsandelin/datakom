#----------------------
#-------Variables------
#----------------------
JFLAGS = -g
JC = javac
BDIR = bin
CLASSES = \
	Player.java \
	Board.java \
	Game.java \
	Server.java \
	Server_network.java \
	Client_network.java \
	KeyboardController.java \
	GameGraphic.java \
	Obstruction.java \

#----------------------
#-------Modules--------
#----------------------
.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

default: all

all: classes

classes: $(CLASSES:.java=.class)

run: classes
	java Game

clean:
	rm -f *.class
	rm -f *~
	rm -f *#

