#----------------------
#-------Variables------
#----------------------
JFLAGS = -g
JC = javac
BDIR = bin
CLASSES = \
	Server.java \
	PlayerInfo.java \
	PlayerPosInfo.java \
	ServerNetwork.java \
	ServerThread.java \
	ClientThread.java \
	ClientNetwork.java \
	DatagramClientThread.java \
	DatagramServerThread.java \
	Player.java \
	Board.java \
	GameThread.java \
	KeyboardController.java \
	KeyboardController.java \
	Obstruction.java \
	Goal.java \
	Game.java \
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

