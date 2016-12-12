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
	ServerNetworkThread.java \
	ClientNetworkThread.java \
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

rmi:
	rmiregistry &
kill:
	killall "rmiregistry"
#for pid in $(ps -f | grep "rmiregistry" | awk '{print $2}'); do kill -9 $pid; done;
clean:
	rm -f *.class
	rm -f *~
	rm -f *#

test: TestClass.class
	java TestClass
