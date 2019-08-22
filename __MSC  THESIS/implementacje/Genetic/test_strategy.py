import datetime  # For datetime objects
import json

import backtrader as bt
import matplotlib.pyplot as plt

from Genetic.GeneticStrategy import GeneticStrategy
from Genetic import test_gen
import random
from deap import base, creator
from deap.base import Toolbox
from deap import tools

tframes = dict(
    daily=bt.TimeFrame.Days,
    weekly=bt.TimeFrame.Weeks,
    monthly=bt.TimeFrame.Months)

data = None
strategy = None

def evaluate(individual):
    cerebro = bt.Cerebro()
    cerebro.adddata(data)
    # cerebro.resampledata(data,
    #                     timeframe=tframes["monthly"],
    #                     compression=1)
    cerebro.addstrategy(strategy,
                        roc= individual[0],
                        rsi_change= individual[1],
                        percD= individual[2],
                        percK= individual[3],
                        osc_ad= individual[4],
                        macd= individual[5],
                        rsi_period= individual[6],
                        stoch_period= individual[7],
                        osc_ad_period= individual[8],
                        macd_me1_period= individual[9],
                        macd_me2_period= individual[10],
                        roc_period= individual[11],
                        )
    # optstrategy
    cerebro.broker.setcommission(commission=0.0025)
    cerebro.broker.setcash(10000.0)
    cerebro.addsizer(bt.sizers.PercentSizer, percents=99)
    try:
        strats = cerebro.run()
        # cerebro.plot()
    except:
        return -99999999, False
    return strats[0].value, True

def run_optimized(result):
    cerebro = bt.Cerebro()
    cerebro.adddata(data)
    # cerebro.resampledata(data,
    #                      timeframe=tframes["monthly"],
    #                      compression=1)



    cerebro.addstrategy(strategy,
                        roc= result[0],
                        rsi_change= result[1],
                        percD= result[2],
                        percK= result[3],
                        osc_ad= result[4],
                        macd= result[5],
                        rsi_period= result[6],
                        stoch_period= result[7],
                        osc_ad_period= result[8],
                        macd_me1_period= result[9],
                        macd_me2_period= result[10],
                        roc_period= result[11],
                        )
    # optstrategy
    configureCerebro(cerebro)
    analyzedStats = cerebro.run()
    printResults(analyzedStats,cerebro)
    cerebro.plot()

def configureCerebro(cerebro):
    cerebro.broker.setcommission(commission=0.0025)
    cerebro.broker.setcash(10000.0)
    cerebro.addsizer(bt.sizers.PercentSizer, percents=99)
    cerebro.addanalyzer(bt.analyzers.SharpeRatio, _name='sharpe')
    cerebro.addanalyzer(bt.analyzers.DrawDown, _name='drawdown')
    cerebro.addanalyzer(bt.analyzers.Returns, _name='returns')

def printResults(analyzedStats, cerebro):
    sharpe_analisys = analyzedStats[0].analyzers.sharpe.get_analysis()
    drawdown_analysis = analyzedStats[0].analyzers.drawdown.get_analysis()
    returns_analisys = analyzedStats[0].analyzers.returns.get_analysis()

    print ("Value: %.2f" % (cerebro.broker.get_value()))
    print (f'sharpeRatio: %.2f' % (sharpe_analisys["sharperatio"]))
    print (f'maxDrawdown: %.2f ' % (drawdown_analysis["max"]["drawdown"]))
    print (f'Annualized return: %.2f' % (returns_analisys["rnorm"]*100))
    print (f'total compound return: %.2f' % (returns_analisys["rtot"]*100))

def getAttributes():
            # roc rsi_change percD percK osc_ad macd
    return [random.randint(-99,99), random.randint(-99,99), random.uniform(0,100), random.uniform(0,100), random.uniform(0,100), random.uniform(0,400),
            # rsi_period stoch_period osc_ad_period macd_me1_period macd_me2_period roc_period
            random.randint(10,50), random.randint(10,50), random.randint(10,50), random.randint(10,50), random.randint(5,50), random.randint(5,50)]

if __name__ == '__main__':
    strategy = GeneticStrategy
    stocks = ["BTC-USD", "TUWOY", "TSLA", "JPM", "FB", "CLUB"]
    # toolbox = test_gen.setup(evaluate, getAttributes)
    # for stock in stocks:
    #     data = bt.feeds.YahooFinanceCSVData(dataname=f"data/csv/{stock}.csv",
    #                                         fromdate=datetime.datetime(2013, 1, 1),
    #                                         todate=datetime.datetime(2017, 10, 13),
    #                                         # todate=datetime.datetime(2013, 9, 24),
    #                                         reversed=False)
    #
    #
    #     population = 150
    #     NGEN = 3
    #
    #     results = test_gen.gen_optimistaion(toolbox, population=population, NGEN=NGEN, CXPB=0.7, MUTPB=0.1)
    #
    #     # while(sum(list(map(lambda x: x.fitness.values[0], results))) / len(results) < -10000):
    #     #     results = test_gen.gen_optimistaion(toolbox, population=population, NGEN=NGEN, CXPB=0.7, MUTPB=0.1)
    #     for i in results:
    #         print (f"{stock}: Value: %10.f, Params: {i}" % i.fitness.values)
    #
    #
    #     f = open(f'data/finalres/{stock}.txt',"w+")
    #     f.write("# roc rsi_change percD percK osc_ad macd rsi_period stoch_period osc_ad_period macd_me1_period macd_me2_period roc_period\n")
    #     for i in results:
    #         f.write(f"Value: %10.f, Params: {i}\n" % i.fitness.values)

        # run_optimized(results[0])


    # cerebro.adddata(data)
    # data = bt.feeds.YahooFinanceData(dataname=f"{stockname}",
    #                                     fromdate=datetime.datetime(2012, 5, 18),
    #                                     todate=datetime.datetime(2019, 3, 29),
    #                                     reversed=False)
    # cerebro.resampledata(data,
    #                      timeframe=tframes["monthly"],
    #                      compression=1)

    data = bt.feeds.YahooFinanceCSVData(dataname=f"data/csv/CLUB.csv",
                                        fromdate=datetime.datetime(2017, 10, 16),
                                        todate=datetime.datetime(2019, 3, 29),
                                        # todate=datetime.datetime(2013, 9, 24),
                                        reversed=False)
    run_optimized(
        [50, -50, 37.47157531160514, 80.494521236505, 55.12033103066902, 79.06050718188213, 29, 22, 48, 15.964106141435375, 34, 42]
    )