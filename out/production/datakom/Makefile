#----------------------
#-------Variables------
#----------------------
JFLAGS = -g
JC = javac
CLASSES = \
	Game.java


#----------------------
#-------Modules------
#----------------------
.SUFFIXES: .java .class

.java.class:
	$(JC) $(FLAGS) $*.java

default: all

all: $(CLASSES:.java=.class)

run: all
	java Game

clean:
	rm -f *.class
	rm -f *~
	rm -f *#
