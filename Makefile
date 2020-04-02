JAVAC = /usr/bin/javac
.SUFFIXES: .java .class

SRCDIR=src
BINDIR=bin
DOCDIR=doc

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR):$(SRCDIR): $<

CLASSES=MersenneTwister.class KnapsackInstance.class ProblemConfiguration.class Chromosome.class Population.class GeneticAlgorithm.class  SimulatedAnnealing.class Application.class ReportGenerator.class Vector.class Particle.class ParticleSwarmOptimization.class
CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)
SRC_FILES=$(SRC:%.java=$(SRCDIR)/%.java)

default: $(CLASS_FILES)

runGA:
	java -cp $(BINDIR) Application -search_best_configuration ga

runSA:
	java -cp $(BINDIR) Application -search_best_configuration sa

runPSO:
	java -cp $(BINDIR) Application -search_best_configuration pso

clean:
	rm $(BINDIR)/*.class

cleanReports:
	rm reports/*.txt
