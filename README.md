# Explanation of Knapsack Solver Application

-------------------------------------------------------------------------------------------------------
## Abbreviations we will use

* GA = Genetic Algorithm
* SA = Simulated Annealing 
* PSO = Particle Swarm Optimization

-------------------------------------------------------------------------------------------------------

## Please note the following:

* The GA algorithm at the moment uses a population size of 1024, and takes around 2 minutes to run 
  through all 28 configurations when searching for the best configuration (generally achieving
  solution qualities in the 110% range). This population size can be adjusted in the Genetic Algorithm
  class depending on whether speed or higher solution quality is prioritized.
* The SA algorithm is ended either when the temperature reaches a minimum of 1 (as specified in class),
  or when 10 000 iterations are reached, whichever comes first. 

-------------------------------------------------------------------------------------------------------

## Running the command-line application

<em> Note: the commands should be run from the base folder (the folder containing src, bin, configurations and data as subfolders) </em>

### Running the application on a specific configuration stored in a JSON file

GA:

> java -cp bin Application -configuration ga_[name].json

SA: 

> java -cp bin Application -configuration sa_[name].json

PSO

> java -cp bin Application -configuration pso_[name].json

<em> Note: [name] in these commands should be appropriately replaced, e.g. by 'default_01' (or 'best' if the below best search has already been performed) </em>

### Searching for the best configuration for a particular algorithm

GA:

> java -cp bin Application -search_best_configuration ga

SA: 

> java -cp bin Application -search_best_configuration sa

PSO

> java -cp bin Application -search_best_configuration pso

-------------------------------------------------------------------------------------------------------

## Classes pertaining to each application:

###   Base classes:

* <em>Application</em> –
        Main driver for the application. Either run the solver on a specific
        configuration or find the best configuration for a given algorithm

* <em>KnapsackInstance</em> – 
        Stores all the possible items that can be chosen for the knapsack
        (each with an associated weight and value) in a single object in memory.

* <em>ProblemConfiguration</em> –  
        Defines the key components defined by the Knapsack problem - including the
        maximum number of iterations for each algorithm (10000) and the maximum capacity
        of the knapsack (822).

* <em>ReportGenerator</em> – 
        Used for generating a report regarding important statistics regarding
        the evaluation of an algorithm - namely solution iterations, runtime,
        convergences and plateau statistics

* <em>MersenneTwister</em> – 
        Used for random number generation

###  Genetic Algorithm:

* <em>Chromosome</em> – 
    Defines a chromosome that will form part of the population in the genetic
    algorithm. Each chromosome encodes a given knapsack configuration - i.e.
    which items are chosen for the knapsack and which are not.  This is where 
    the Crossover and Mutation operations are defined.

* <em>Population</em> – 
    Class for the population of chromosomes that evolves as part of the genetic
    algorithm. This is where Roulette-Wheel Selection and Tournament Selection 
    are defined.

* <em>GeneticAlgorithm</em> – 
    Used for running a genetic algorithm solution to the knapsack problem,
    given specified parameters such as selection method, crossover method
    etc.

###   Simulated Annealing:

* <em>SimulatedAnnealing</em> – 
    Class used for running a simulated annealing solution to the knapsack
    problem, given the specified parameters of initial temperature and cooling
    rate.

###   Particle Swarm Optimization:

* <em>Vector</em> – 
    Class that encodes a vector with n dimensions to define position and velocity vectors for the
    PSO algorithm. By default, the vector will have 150 dimensions as per this knapsack problem.

* <em>Particle</em> – 
    Class that encodes a particle that participates in a swarm in PSO

* <em>ParticleSwarmOptimization</em> – 
    Class used for running a particle swarm optimization solution to the knapsack
    problem, given the specified parameters such as number of particles, maximum
    velocity etc.

-------------------------------------------------------------------------------------------------------

   
