# https://towardsdatascience.com/genetic-algorithm-implementation-in-python-5ab67bb124a6 - description

import random
from deap import base, creator
from deap.base import Toolbox
from deap import tools

IND_SIZE = 10

def gen_optimistaion(toolbox, population=100, CXPB=0.5, MUTPB=0.2, NGEN=40):
    pop = toolbox.population(n=population)

    # CXPB  is the probability with which two individuals
    #       are crossed
    #
    # MUTPB is the probability for mutating an individual

    # Evaluate the entire population
    fitnesses = list(map(toolbox.evaluate, pop))
    for ind, fit in zip(pop, fitnesses):
        ind.fitness.values = fit

    for g in range(NGEN):
        # Select the next generation individuals
        offspring = toolbox.select(pop, len(pop))

        # Clone the selected individuals
        offspring = list(map(toolbox.clone, offspring))

        # Apply crossover and mutation on the offspring
        for child1, child2 in zip(offspring[::2], offspring[1::2]):
            if random.random() < CXPB:
                toolbox.mate(child1, child2)
                del child1.fitness.values
                del child2.fitness.values

        for mutant in offspring:
            if random.random() < MUTPB:
                toolbox.mutate(mutant)
                del mutant.fitness.values

        # Evaluate the individuals with an invalid fitness
        invalid_ind = [ind for ind in offspring if not ind.fitness.valid]
        fitnesses = list(map(toolbox.evaluate, invalid_ind))
        for ind, fit in zip(invalid_ind, fitnesses):
            ind.fitness.values = fit

        # Gather all the fitnesses in one list and print the stats
        fits = [ind.fitness.values[0] for ind in pop]

        mean = sum(fits) / len(pop)
        sum2 = sum(x*x for x in fits)
        std = abs(sum2 / len(pop) - mean**2)**0.5

        print("%3d . Min: %10.2f, Max: %10.2f, Avg %10.2f, Std %3.2f" % (g, min(fits), max(fits), mean, std))

        # The population is entirely replaced by the offspring
        pop[:] = offspring

    return pop

def getAttributes():
    # (("roc", random.randint(-99,99)),
    #  ("rsi_change",random.randint(-99,99)),
    #  ("percD", random.uniform(0,100)),
    #  ("percK", random.uniform(0,100)),
    #  ("osc_ad", random.uniform(0,100)),
    #  ("macd", random.uniform(0,100)),
    #  ("rsi_period", random.randint(12,100)),
    #  ("stoch_period", random.randint(12,100)),
    #  ("osc_ad_period", random.randint(12,100)),
    #  ("macd_me1_period", random.randint(12,100)),
    #  ("macd_me2_period", random.randint(12,100)),
    #  ("roc_period", random.randint(12,36)))
    return [random.randint(-99,99), random.randint(-99,99), random.uniform(0,100), random.uniform(0,100), random.uniform(0,100), random.uniform(0,100),
            random.randint(12,100), random.randint(12,100), random.randint(12,100), random.randint(12,100), random.randint(12,100), random.randint(12,36)]

def populateIndividual(individual, gene_list_populator):
    x = individual()
    for i in gene_list_populator():
        x.append(i)
    return x

def setup(evaluationFun, gene_list_populator):
    toolbox = Toolbox()
    creator.create("FitnessMin", base.Fitness, weights=(1.0,))
    creator.create("Individual", list, fitness=creator.FitnessMin)

    toolbox.register("fillIndividual", populateIndividual, creator.Individual, gene_list_populator)
    # toolbox.register("individual", tools.initRepeat, creator.Individual,
    #                  toolbox.attribute, n=10)
    # toolbox.register("attribute", random.random)
    toolbox.register("population", tools.initRepeat, list, toolbox.fillIndividual)

    toolbox.register("mate", tools.cxTwoPoint)
    toolbox.register("mutate", tools.mutGaussian, mu=0, sigma=1, indpb=0.1)
    toolbox.register("select", tools.selTournament, tournsize=3)
    toolbox.register("evaluate", evaluationFun)

    return toolbox

if __name__ == '__main__':

    def evaluate(individual):
        # algorytm tutaj
        return random.randint(-100000,1000000),

    toolbox = setup(evaluate, getAttributes)
    x = gen_optimistaion(toolbox)
    print()



