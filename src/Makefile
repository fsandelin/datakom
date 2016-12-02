#----------------------
#-------Variables------
#----------------------
JFLAGS = -g
JC = javac
BDIR = bin
CLASSES = \
	Server.java \
	PlayerInfo.java \
	Server_network.java \
	ServerThread.java \
	ClientThread.java \
	Client_network.java \



#	Player.java \
#	Board.java \
#	Game.java \
#	KeyboardController.java \
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

