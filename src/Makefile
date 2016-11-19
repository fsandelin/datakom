#----------------------
#-------Variables------
#----------------------
JFLAGS = -g -d ../$(BDIR)
JC = javac
BDIR = bin
CLASSES = \
	Player.java \
	Board.java \
	Game.java \
	Server.java \
	Server_network.java \
	Client_network.java \

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
	cd .. && cd $(BDIR) && make run

clean:
	rm -f *.class
	rm -f *~
	rm -f *#
	rm -f ../$(BDIR)/*.class
	rm -f ../$(BDIR)/*~
	rm -f ../$(BDIR)/*#

