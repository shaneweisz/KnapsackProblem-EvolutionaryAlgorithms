JAVAC = /usr/bin/javac
.SUFFIXES: .java .class

SRCDIR=src
BINDIR=bin
DOCDIR=doc

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR):$(SRCDIR): $<

CLASSES=MersenneTwister.class KnapsackInstance.class ProblemConfiguration.class Chromosome.class Population.class GeneticAlgorithm.class Application.class
CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)
SRC_FILES=$(SRC:%.java=$(SRCDIR)/%.java)

default: $(CLASS_FILES)

run:
	java -cp $(BINDIR) Application -configuration ga_default_01

clean:
	rm $(BINDIR)/*.class
