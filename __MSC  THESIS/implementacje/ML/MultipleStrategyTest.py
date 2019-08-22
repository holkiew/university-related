import datetime  # For datetime objects
import json
from sklearn.ensemble import RandomForestClassifier
from sklearn.ensemble import GradientBoostingClassifier
from sklearn.datasets import make_classification
import backtrader as bt
import matplotlib.pyplot as plt
from statistics import mean
from ML.ML_feature_extraction_Strategy import Strategy
from ML.MLStrategy import MLStrategy
from Genetic import test_gen
import random
from deap import base, creator
from deap.base import Toolbox
from deap import tools
import collections

tframes = dict(
    daily=bt.TimeFrame.Days,
    weekly=bt.TimeFrame.Weeks,
    monthly=bt.TimeFrame.Months)

data = None
strategy = None
fileTxt = list()

def appendToTxt(txt, string):
    txt.append(string + "\n")

def printEnvelope(str, txt=None):
    if not txt == None: appendToTxt(txt, str);
    print (str)

def saveToFile(path, txt):
    f = open(path,"w+")
    f.write(txt)

def test_Strategy(evaluator, plot=True):
    cerebro = bt.Cerebro()
    cerebro.adddata(data)
    cerebro.addstrategy(strategy, evaluator=evaluator)
    configureCerebro(cerebro)
    analyzedStats = cerebro.run()
    printResults(analyzedStats, cerebro)
    if plot: cerebro.plot();
    return analyzedStats

def get_Features(plot=True):
    cerebro = bt.Cerebro()
    cerebro.adddata(data)
    # cerebro.resampledata(data,
    #                      timeframe=tframes["monthly"],
    #                      compression=1)



    cerebro.addstrategy(strategy)
    # optstrategy
    configureCerebro(cerebro)
    analyzedStats = cerebro.run()
    printEnvelope("---BASE RESULTS---", txt=fileTxt)
    printResults(analyzedStats,cerebro)
    if plot: cerebro.plot();
    return analyzedStats[0].features, analyzedStats[0]

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
    value_ = "Value: %.2f" % (cerebro.broker.get_value())
    printEnvelope(value_, txt=fileTxt)
    try:
        sharperatio_ = f'sharpeRatio: %.2f' % (sharpe_analisys["sharperatio"])
        drawdown_ = f'maxDrawdown: %.2f ' % (drawdown_analysis["max"]["drawdown"] )
        rnorm_ = f'Annualized return: %.2f' % (returns_analisys["rnorm"])
        rtot_ = f'total compound return: %.2f' % (returns_analisys["rtot"])
        printEnvelope(sharperatio_, txt=fileTxt)
        printEnvelope(drawdown_, txt=fileTxt)
        printEnvelope(rnorm_, txt=fileTxt)
        printEnvelope(rtot_, txt=fileTxt)
    except Exception as e:
        print(e)

def filterStocks(stocks):
    filteredStocks = list()
    for stock in stocks:
        with open(f"data/stocks/{stock}.csv", "r") as fp:
            content = fp.read()
            if content.find("2012-01-03") >= 0 and content.find("NaN") < 0:
                filteredStocks.append(stock)
            else:
                print (f"not loaded {stock}")
    return filteredStocks

if __name__ == '__main__':

    stocks = ["BTC-USD", "TUWOY", "CLUB", "FB", "JPM", "TSLA"]

    learningStocks = [ "ALGN", "ALL", "AGN", "ADS", "LNT",
                       "ALL", "GOOGL", "GOOG", "MO", "AMZN",
                       "SLB", "STX", "SEE", "SRE", "SHW",
                       "VAR", "VTR", "VRSN", "VRSK", "VZ", "VRTX"]
    learningStocks = filterStocks(learningStocks)
    for z in range(5):
        featureDictxy = collections.OrderedDict()
        baseValues = list()
        strategy = Strategy
        for learningStock in learningStocks:
            data = bt.feeds.YahooFinanceCSVData(dataname=f"data/stocks/{learningStock}.csv",
                                                fromdate=datetime.datetime(2012, 1, 3),
                                                todate=datetime.datetime(2016, 8, 9),
                                                # todate=datetime.datetime(2013, 9, 24),
                                                # todate=datetime.datetime(2019, 3, 29),
                                                reversed=False)
            features = get_Features(plot=False)
            xy = features[0]
            featureDictxy.update(xy)
            baseValues.append(features[1].broker.getvalue())

        X, y  = list(), list()
        for val in featureDictxy.values():
            # ficzersy
            X.append(val[0])
            # 0-nic, 1 buy, -1 sell
            y.append(val[1])

        strategy = MLStrategy
        for stock in stocks:
            data = bt.feeds.YahooFinanceCSVData(dataname=f"data/csv/{stock}.csv",
                                                # fromdate=datetime.datetime(2016, 8, 9),
                                                fromdate=datetime.datetime(2019, 1, 1),
                                                todate=datetime.datetime(2019, 3, 29),
                                                # todate=datetime.datetime(2013, 9, 24),
                                                reversed=False)

            a_v = list(); a_sr = list();a_md = list();a_ar = list();a_tcr = list()
            for i in range (50, 501, 50):
                printEnvelope(f"---ESTIMATORS: {i}---", txt=fileTxt)
                #clfRFR = RandomForestClassifier(n_estimators=i)
                clfRFR = GradientBoostingClassifier(n_estimators=i)
                clfRFR.fit(X, y)
                analyzedStats = test_Strategy(clfRFR.predict, plot=False)
                try:
                    sharpe_analisys = analyzedStats[0].analyzers.sharpe.get_analysis()
                    drawdown_analysis = analyzedStats[0].analyzers.drawdown.get_analysis()
                    returns_analisys = analyzedStats[0].analyzers.returns.get_analysis()
                    a_v.append(analyzedStats[0].broker.get_value());a_sr.append(sharpe_analisys["sharperatio"]);a_md.append(drawdown_analysis["max"]["drawdown"])
                    a_ar.append(returns_analisys["rnorm"]*100);a_tcr.append(returns_analisys["rtot"]*100)
                except Exception as e:
                    print(e)

            printEnvelope("----Averages----", txt=fileTxt)
            v_ = "Value: %.2f" % (mean(a_v))
            printEnvelope(v_, txt=fileTxt)
            try:
                sr_ = "sharpeRatio: %.2f" % (mean(a_sr))
                md_ = "maxDrawdown: %.2f" % (mean(a_md))
                ar_ = "Annualized return: %.2f" % (mean(a_ar))
                tcr_ = "total compound return: %.2f" % (mean(a_tcr))

                printEnvelope(sr_, txt=fileTxt)
                printEnvelope(md_, txt=fileTxt)
                printEnvelope(ar_, txt=fileTxt)
                printEnvelope(tcr_, txt=fileTxt)
            except Exception as e:
                print(e)
            appendToTxt(fileTxt, "Base values average %.2f" % mean(baseValues))
            saveToFile(f"data/results/multiple/{stock}/BRT_{stock}_R(50_501_50)RUN_%d_TREE_MAX.txt" % z, str().join(fileTxt))
            fileTxt.clear()

    # stocks = ["BTC-USD"]
    # toolbox = test_gen.setup(evaluate, getAttributes)
    # for stock in stocks:
    #     data = bt.feeds.YahooFinanceCSVData(dataname=f"data/csv/{stock}.csv",
    #                                         fromdate=datetime.datetime(2012, 1, 1),
    #                                         todate=datetime.datetime(2019, 3, 29),
    #                                         # todate=datetime.datetime(2013, 9, 24),
    #                                         reversed=False)
    #
    #
    #     population = 150
    #     NGEN = 25
    #
    #     results = test_gen.gen_optimistaion(toolbox, population=population, NGEN=NGEN, CXPB=0.7, MUTPB=0.1)
    #
    #     while(sum(list(map(lambda x: x.fitness.values[0], results))) / len(results) < -10000):
    #         results = test_gen.gen_optimistaion(toolbox, population=population, NGEN=NGEN, CXPB=0.7, MUTPB=0.1)
    #     for i in results:
    #         print (f"{stock}: Value: %10.f, Params: {i}" % i.fitness.values)
    #
    #
    #     f = open(f'data/results/{stock}_OPTIMIZATION3_{population}_{NGEN}.txt',"w+")
    #     f.write("# roc rsi_change percD percK osc_ad macd rsi_period stoch_period osc_ad_period macd_me1_period macd_me2_period roc_period\n")
    #     for i in results:
    #         f.write(f"Value: %10.f, Params: {i}\n" % i.fitness.values)
    #
    #     run_optimized(results[0])


