from __future__ import (absolute_import, division, print_function,
                        unicode_literals)

import datetime  # For datetime objects
import json
from statistics import mean
import backtrader as bt
import matplotlib.pyplot as plt

import MeanReversionSMA.data.data_descriptors as data_descriptors
from MeanReversionSMA.MeanReversion_SMAAdj import StrategyAdj as Strategy
from TestStrategy.DummyStrategy import Strategy as DummyStrategy

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
        # Analyzers
        cerebro.addanalyzer(bt.analyzers.SharpeRatio, _name='sharpe')
        cerebro.addanalyzer(bt.analyzers.DrawDown, _name='drawdown')
        cerebro.addanalyzer(bt.analyzers.Returns, _name='returns')

        # cerebro.addobserver(bt.observers.Value)

        cerebro.broker.setcommission(commission=0.0025)
        # cerebro.addsizer(bt.sizers.FixedSize, stake=20)
        cerebro.addsizer(bt.sizers.PercentSizer, percents=98)
        cerebro.broker.setcash(10000.0)

        # Value : 7533.63 | OrderedDict([('smaS_period', 80), ('smaL_period', 240), ('rsi_period', 14),
        # ('rsi_top', 65), ('rsi_bot', 45), ('value', 7533.627709381804), ('printlog', False), ('printstop', False)])

        cerebro.addstrategy(strategy,
                            smaS=50,
                            smaL=80,
                            rsi_period=60,
                            osc_period=5,
                            osc_bot=61,
                            osc_top=381,
                            rsi_bot=20,
                            rsi_top=90,

                            # printstop=True
                            printlog=False
                            )

        plot = {'period_x': list(),
                'cash_y': list()
                }
        fd, td = value['fromdate'], value['todate']
        data = bt.feeds.YahooFinanceCSVData(dataname=f"data/csv/{key}.csv",
                                            # fromdate=datetime.datetime(td['y'], td['m'], td['d'])
                                            fromdate=datetime.datetime(2017, 5, 1),
                                            # todate=datetime.datetime(td['y'], td['m'], td['d']), reverse=False)
                                            todate=datetime.datetime(2019, 5, 12), reverse=False)
        cerebro.adddata(data)


        analyzedStats = cerebro.run()
        sharpe_analisys = analyzedStats[0].analyzers.sharpe.get_analysis()
        drawdown_analysis = analyzedStats[0].analyzers.drawdown.get_analysis()
        returns_analisys = analyzedStats[0].analyzers.returns.get_analysis()
        cerebro.plot()
        print(key)
        try:
            print ("Value: %.2f" % (cerebro.broker.get_value()))
            print (f'sharpeRatio: %.2f' % (sharpe_analisys["sharperatio"]))
            print (f'maxDrawdown: %.2f ' % (drawdown_analysis["max"]["drawdown"]))
            print (f'Annualized return: %.2f' % (returns_analisys["rnorm"]*100))
            print (f'total compound return: %.2f' % (returns_analisys["rtot"]*100))
        except:
            print ('--------------EXCEPTION--------------: ', key)

        a_v.append(cerebro.broker.get_value());a_sr.append(sharpe_analisys["sharperatio"]);a_md.append(drawdown_analysis["max"]["drawdown"])
        a_ar.append(returns_analisys["rnorm"]*100);a_tcr.append(returns_analisys["rtot"]*100);

        # plot['period_x'].append(period)
        # plot['cash_y'].append(cerebro.broker.getvalue())
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
    for i in range(len(a_v)):
        if a_v[i] == None:
            a_v[i] = 0
        if a_sr[i] == None:
            a_sr[i] = 0
        if a_sr[i] == None:
            a_sr[i] = 0
        if a_md[i] == None:
            a_md[i] = 0
        if a_ar[i] == None:
            a_ar[i] = 0
        if a_tcr[i] == None:
            a_tcr[i] = 0
    try:
        print ("----Averages----")
        print ("Value: %.2f" % (mean(a_v)))
        print ("sharpeRatio: %.2f" % (mean(a_sr)))
        print ("maxDrawdown: %.2f" % (mean(a_md)))
        print ("Annualized return: %.2f" % (mean(a_ar)))
        print ("total compound return: %.2f" % (mean(a_tcr)))
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
        smaS=[10,30,50],
        smaL=[50,80,],
        rsi_period=[21, 40, 60],
        osc_period=[5],
        osc_bot=range(1,400, 20),
        osc_top=range(1,400, 20),
        rsi_bot=20,
        rsi_top=90,
        printlog=False,
        printstop=False

    )
    data = bt.feeds.YahooFinanceCSVData(dataname=f"data/csv/{stock}.csv",
                                        # fromdate=datetime.datetime(td['y'], td['m'], td['d'])
                                        fromdate=datetime.datetime(2017, 5, 1),
                                        todate=datetime.datetime(2019, 5, 12), reverse=False)
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
    stocks = [
        # "BTC-USD", "CLUB",
        # "FB",
        # "JPM", "TSLA",
        # "TUWOY"
              ]
    start = datetime.datetime.now()

    # for stock in stocks:
    # stock = "BTC-USD2"
    # strategy_optimisation(Strategy, stock, save_to_file=False, filename=f"test_optimization{stock}")
    allThreeTest(Strategy, chosen_stock="BTC-USD2")
    # allThreeTest(Strategy)
    end = datetime.datetime.now()
    print (f'Start: {start}, end: {end}')
