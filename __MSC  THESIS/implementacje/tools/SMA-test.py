from __future__ import (absolute_import, division, print_function,
                        unicode_literals)

import datetime  # For datetime objects
import json
from statistics import mean
import backtrader as bt
import matplotlib.pyplot as plt

import MeanReversionSMA.data.data_descriptors as data_descriptors
from MeanReversionSMA.MeanReversion_SMA import Strategy
from TestStrategy.DummyStrategy import Strategy as DummyStrategy

params = {
    "customPlot": False,
    "customDumpDataJson": False,
    "btPlot": True
}

def monte_carlo_averages(strategy, representatives, stockname, no_of_csv, directory):
    cash = 10000.0
    for no in range(no_of_csv):
        print (f'----------CSV %3d----------' % no)
        data = bt.feeds.YahooFinanceCSVData(dataname=f"{directory}/{stockname}.csv",
                                            # fromdate=datetime.datetime(td['y'], td['m'], td['d'])
                                            fromdate=datetime.datetime(2017, 10, 16),
                                            # todate=datetime.datetime(td['y'], td['m'], td['d']), reverse=False)
                                            todate=datetime.datetime(2019, 3, 29), reverse=False)
        for params in representatives:
            cerebro = bt.Cerebro()
            cerebro.broker.setcommission(commission=0.0025)
            cerebro.addsizer(bt.sizers.PercentSizer, percents=98)
            cerebro.broker.setcash(cash)

            cerebro.addstrategy(strategy,
                                smaS_period= params[0],
                                smaL_period= params[1],
                                rsi_period= params[2],
                                rsi_top= params[3],
                                rsi_bot= params[4],

                                # printstop=True
                                printlog=False)
            cerebro.adddata(data)
            try:
                cerebro.run()
                print (f'Value %.2f, representative: {params}'% cerebro.broker.get_value())
                pnl = cerebro.broker.get_value() - cash
                params[5] += pnl
                params[6] = min (params[6], pnl)
                params[7] = max (params[7], pnl)
            except (Exception):
                print(f"Ominieto dla CSV {no}, dla parametrow {params}")

    try:
        f = open(f'{stockname}_results_of_realPrice.txt',"w+")
        try:
            for x in (representatives):
                f.write(f'Avg Value : %.2f | {x}\n' % (x[5]/no_of_csv))
        except (Exception):
            print("exceptio writing/reading stats while saving. ", Exception)
    except:
        print ("err")


def strategy_optimisation(strategy, stock, save_to_file=False, filename="optimisation"):
    cerebro = bt.Cerebro()
    cerebro.broker.setcommission(commission=0.0025)
    # cerebro.addsizer(bt.sizers.FixedSize, stake=20)
    cerebro.addsizer(bt.sizers.PercentSizer, percents=98)
    cerebro.broker.setcash(10000.0)
    cerebro.optstrategy(
        strategy,
        smaS_period = [25, 50, 100, 125],
        smaL_period = [50, 100, 150, 200],
        rsi_period = [14, 21, 40],
        rsi_top = [70, 80, 85],
        rsi_bot = [15, 20, 30],
        printlog=False,
        printstop=False
    )
    data = bt.feeds.YahooFinanceCSVData(dataname=f"csv/{stock}.csv",
                                        # fromdate=datetime.datetime(td['y'], td['m'], td['d'])
                                        fromdate=datetime.datetime(2012, 3, 1),
                                        todate=datetime.datetime(2017, 10, 13), reverse=False)
    cerebro.adddata(data)
    strats = cerebro.run()
    allStats = dict()

    for stratrun in strats:
        for strat in stratrun:
            args = strat.p._getkwargs()
            # print(args)
            allStats[args['value']] = args
    top = sorted(allStats.keys(), reverse=True)

    print ("--------------- TOP ---------------")
    for x in range(len(top)):
        print (f'Value : %.2f | {allStats[top[x]]}' % (allStats[top[x]]["value"]))
    try:
        print (f'Average: {sum(top) / len(top)}')
    except:
        print("exception")

    if save_to_file:
        f = open(f'{filename}.txt',"w+")
        for x in range(len(top)):
            f.write(f'Value : %.2f | {allStats[top[x]]}\n' % (allStats[top[x]]["value"]))
        try:
            f.write (f'Average: {sum(top) / len(top)}')
        except (Exception):
            print("exceptio writing/reading stats while saving. ", Exception)

if __name__ == '__main__':
    # strategy_optimisation(Strategy, "BTC-USD", save_to_file=True, filename="BTC-USD_opt_2013.01.01_2017_10_13")
    stocks = [
        # "BTC-USD", "CLUB",
        # "FB",
        # "JPM", "TSLA",
        # "TUWOY"
              ]
    # ('smaS_period', ), ('smaL_period', ), ('rsi_period', ), ('rsi_top', ), ('rsi_bot', ), VALUE, min , max
    representatives = [
            [25,50,21,85,30,0,0,0],
            [125,200,14,85,20,0,0,0],
            [125,150,14,85,30,0,0,0],
            [100,200,14,70,20,0,0,0],
            [25,200,21,70,30,0,0,0],
            [25,50,14,80,20,0,0,0],
            [100,150,14,70,20,0,0,0],
            [125,100,21,70,30,0,0,0],
            [50,100,21,70,30,0,0,0]
    ]
    stock = "FB"
    monte_carlo_averages(Strategy, representatives, stock, 1, f"csv")
# start = datetime.datetime.now()

    # for stock in stocks:
    # stock = "BTC-USD2"
    # strategy_optimisation(Strategy, stock, save_to_file=False, filename=f"test_optimization{stock}")
    # allThreeTest(Strategy, chosen_stock="BTC-USD2")
    # allThreeTest(Strategy)
    # end = datetime.datetime.now()
    # print (f'Start: {start}, end: {end}')
