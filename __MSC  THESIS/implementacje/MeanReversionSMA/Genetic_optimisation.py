import datetime  # For datetime objects

import backtrader as bt
from Genetic import test_gen
from Genetic.GeneticStrategy import GeneticStrategy
from MeanReversionSMA.MeanReversion_SMA import Strategy as SMAStrategy
import random


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
                        smaS_period= individual[0],
                        smaL_period= individual[1],
                        rsi_period= individual[2],
                        rsi_top= individual[3],
                        rsi_bot= individual[4]
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
                        smaS_period= result[0],
                        smaL_period= result[1],
                        rsi_period= result[2],
                        rsi_top= result[3],
                        rsi_bot= result[4],
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
            # smaS_period smaL_period rsi_period rsi_top rsi_bot
    return [random.randint(15,200), random.randint(25,300), random.randint(10,100) , random.randint(60,90), random.randint(10,40)]

if __name__ == '__main__':
    strategy = SMAStrategy
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
    #     NGEN = 1
    #
    #     results = test_gen.gen_optimistaion(toolbox, population=population, NGEN=NGEN, CXPB=0.7, MUTPB=0.1)
    #
    #     # while(sum(list(map(lambda x: x.fitness.values[0], results))) / len(results) < -10000):
    #     #     results = test_gen.gen_optimistaion(toolbox, population=population, NGEN=NGEN, CXPB=0.7, MUTPB=0.1)
    #     for i in results:
    #         print (f"{stock}: Value: %10.f, Params: {i}" % i.fitness.values)
    #
    #
    #     f = open(f'data/results/extratests/{stock}_MAX.txt',"w+")
    #     f.write("# smaS_period smaL_period rsi_period rsi_top rsi_bot\n")
    #     for i in results:
    #         f.write(f"Value: %10.f, Params: {i}\n" % i.fitness.values)
    #
    #     # run_optimized(results[0])


    # cerebro.adddata(data)
    # data = bt.feeds.YahooFinanceData(dataname=f"{stockname}",
    #                                     fromdate=datetime.datetime(2012, 5, 18),
    #                                     todate=datetime.datetime(2019, 3, 29),
    #                                     reversed=False)
    # cerebro.resampledata(data,
    #                      timeframe=tframes["monthly"],
    #                      compression=1)

    data = bt.feeds.YahooFinanceCSVData(dataname=f"data/csv/TUWOY.csv",
                                        # fromdate=datetime.datetime(2013, 1, 1),

                                        fromdate=datetime.datetime(2017, 10, 16),
                                        todate=datetime.datetime(2019, 3, 29),
                                        # todate=datetime.datetime(2013, 9, 24),
                                        reversed=False)
    run_optimized(
        [131, 253, 20, 74, 32]
    )