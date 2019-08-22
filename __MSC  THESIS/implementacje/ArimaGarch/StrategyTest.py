from __future__ import (absolute_import, division, print_function,
                        unicode_literals)

import datetime  # For datetime objects
import json
from statistics import mean
import numpy
import backtrader as bt
import matplotlib.pyplot as plt

import ArimaGarch.data.data_descriptors as data_descriptors
from ArimaGarch.LinearRegressionStrategy import LinearRegressionStrategy
from TestStrategy.DummyStrategy import Strategy as DummyStrategy
from ArimaGarch.ArimaStrategyTest import ArimaStrategyTest
from ArimaGarch.ArimaStrategy import ArimaStrategy
params = {
    "customPlot": False,
    "customDumpDataJson": False,
    "btPlot": True
}


def allThreeTest(strategy, chosen_stock=None):
    a_v = list(); a_sr = list();a_md = list();a_ar = list();a_tcr = list()
    for key, value in data_descriptors.data.items():
        # Preconfig
        if chosen_stock and key != chosen_stock:
            continue
        # Invariables
        cerebro = bt.Cerebro()

        cerebro.addstrategy(strategy,
                            # rsi_period=18,
                            # boll_period=50,
                            # boll_devf=10,
                            # rsi_bot=30,
                            # lreg_period=30,
                            # printlog=False
                            )
        cerebro.broker.setcommission(commission=0.0025,  leverage=2.1)
        # cerebro.addsizer(bt.sizers.FixedSize, stake=20)
        cerebro.addsizer(bt.sizers.PercentSizer, percents=98)
        cerebro.broker.setcash(10000.0)

        # Analyzers
        cerebro.addanalyzer(bt.analyzers.SharpeRatio, _name='sharpe')
        cerebro.addanalyzer(bt.analyzers.DrawDown, _name='drawdown')
        cerebro.addanalyzer(bt.analyzers.Returns, _name='returns')

        plot = {'period_x': list(),
                'cash_y': list()
                }
        fd, td = value['fromdate'], value['todate']
        data = bt.feeds.YahooFinanceCSVData(dataname=f"data/csv/{key}.csv",
                                            # fromdate=datetime.datetime(td['y'], td['m'], td['d'])
                                            fromdate=datetime.datetime(2016, 5, 18),
                                            # fromdate=datetime.datetime(2013, 1, 1),
                                            todate=datetime.datetime(td['y'], td['m'], td['d']), reverse=False)
                                            # todate=datetime.datetime(2018, 3, 28), reverse=False)
        cerebro.adddata(data)


        # LinearRegressionStrategy.period = period
        analyzedStats = cerebro.run()
        sharpe_analisys = analyzedStats[0].analyzers.sharpe.get_analysis()
        drawdown_analysis = analyzedStats[0].analyzers.drawdown.get_analysis()
        returns_analisys = analyzedStats[0].analyzers.returns.get_analysis()

        print(key)
        try:
            print ("Value: %.2f" % (cerebro.broker.get_value()))
            print (f'sharpeRatio: %.2f' % (sharpe_analisys["sharperatio"]))
            print (f'maxDrawdown: %.2f ' % (drawdown_analysis["max"]["drawdown"]))
            print (f'Annualized return: %.2f' % (returns_analisys["rnorm"]*100))
            print (f'total compound return: %.2f' % (returns_analisys["rtot"]*100))
            a_v.append(cerebro.broker.get_value());a_sr.append(sharpe_analisys["sharperatio"]);a_md.append(drawdown_analysis["max"]["drawdown"])
            a_ar.append(returns_analisys["rnorm"]*100);a_tcr.append(returns_analisys["rtot"]*100);

            # plot['period_x'].append(period)
            # plot['cash_y'].append(cerebro.broker.getvalue())
        except Exception as e:
            print (e)
        if params['btPlot']:
            cerebro.plot()
            # cerebro.plot(style='candlestick')
        if params['customDumpDataJson']:
            with open(f'data/results/periodD30/{key}D30.json', 'a+') as json_file:
                json.dump(plot, json_file)
        if params['customPlot']:
            plt.plot(plot['period_x'], plot['cash_y'])
            # plt.title(f'{key}: {datetime.datetime.now().strftime("(%H:%M:%S) %Y-%m-%d")}')
            plt.title(f'{key}: {datetime.datetime(2012, 1, 1).strftime("%Y-%m-%d")} - {datetime.datetime(td["y"], td["m"],td["d"]).strftime("%Y-%m-%d")}')
            plt.xlabel('Period')
            plt.ylabel('Value')
            plt.show()
    try:
        print ("----Averages----")
        print ("Value: %.2f" % (mean(a_v)))
        print ("sharpeRatio: %.2f" % (mean(a_sr)))
        print ("maxDrawdown: %.2f" % (mean(a_md)))
        print ("Annualized return: %.2f" % (mean(a_ar)))
        print ("total compound return: %.2f" % (mean(a_tcr)))
    except Exception as e:
        print (e)

def optimization():
    stocks = "JPM, FB, TUWOY, BTC-USD, CLUB, TSLA"
    # stocks = "BTC-USD"
    for stockname in stocks.split(', '):
        cerebro = bt.Cerebro()
        cerebro.optstrategy(ArimaStrategy,
                        rsi_period=[15,18],
                        boll_period=[30],
                        boll_devf=[10,12],
                        rsi_bot=[40],
                        lreg_period=[12,30],
                        printlog=False
        )
        cerebro.broker.setcommission(commission=0.0025, leverage=2.1)
        # cerebro.addsizer(bt.sizers.FixedSize, stake=20)
        cerebro.addsizer(bt.sizers.PercentSizer, percents=99.5)
        cerebro.broker.setcash(10000.0)
        # cerebro.broker.set_coc(True)
        # cerebro.broker.set_coo(True)
        # stocks = "ORCL, TSLA"
        # stocks = "JPM, FB, TUWOY, BTC-USD, CLUB"
        # stocks = "TSLA"
        # stocks = "TSLA"
        # Handy dictionary for the argument timeframe conversion
        tframes = dict(
            daily=bt.TimeFrame.Days,
            weekly=bt.TimeFrame.Weeks,
            monthly=bt.TimeFrame.Months)

        # Add the resample data instead of the original


        data = bt.feeds.YahooFinanceCSVData(dataname=f"data/csv/{stockname}.csv",
                                            fromdate=datetime.datetime(2016, 5, 18),
                                            todate=datetime.datetime(2019, 3, 29))
        cerebro.adddata(data)
        # cerebro.resampledata(data,
        #                      timeframe=tframes["monthly"],
        #                      compression=1)

        strats = cerebro.run()
        allStats = dict()

        for stratrun in strats:
            for strat in stratrun:
                args = strat.p._getkwargs()
                # print(args)
                allStats[args['value']] = args
        top = sorted(allStats.keys(), reverse=True)
        print(allStats)
        if True:
            f = open(f'data/results/ADJ_optBezWait{stockname}.txt',"w+")
            for x in range(len(top)):
                f.write(f'Value : %.2f | {allStats[top[x]]}\n' % (allStats[top[x]]["value"]))
                print (f'Value : %.2f | {allStats[top[x]]}\n' % (allStats[top[x]]["value"]))
            f.write (f'Average: {sum(top) / len(top)}')

    # cerebro.plot()

if __name__ == '__main__':
    allThreeTest(ArimaStrategy, chosen_stock="TUWOY")
    # allThreeTest(LinearRegressionStrategy)
    # optimization()


