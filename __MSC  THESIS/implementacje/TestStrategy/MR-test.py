from __future__ import (absolute_import, division, print_function,
                        unicode_literals)

import datetime  # For datetime objects
import time
import json

import backtrader as bt
import matplotlib.pyplot as plt

import TestStrategy.data.data_descriptors as data_descriptors
import TestStrategy.MRStrategy as MRStrategy
import TestStrategy.DummyStrategy as DummyStrategy

params = {
    "customPlot": False,
    "customDumpDataJson": False,
    "btPlot": True
}


def allThreeTest(strategy, chosen_stock=None):
    for key, value in data_descriptors.data.items():
        # Preconfig
        if chosen_stock and key != chosen_stock:
            break
        # Invariables
        # cerebro = bt.Cerebro(stdstats=False)
        cerebro = bt.Cerebro()
        cerebro.addstrategy(strategy,

                            )
        cerebro.broker.setcommission(commission=0.0025)
        # cerebro.addsizer(bt.sizers.FixedSize, stake=20)
        cerebro.addsizer(bt.sizers.PercentSizer, percents=99)

        cerebro.broker.setcash(10000.0)

        plot = {'period_x': list(),
                'cash_y': list()
                }
        fd, td = value['fromdate'], value['todate']
        data = bt.feeds.YahooFinanceCSVData(dataname=f"data/csv/{key}.csv",
                                            # fromdate=datetime.datetime(td['y'], td['m'], td['d'])
                                            fromdate=datetime.datetime(2017, 10, 16),
                                            # todate=datetime.datetime(td['y'], td['m'], td['d']), reverse=False)
                                            todate=datetime.datetime(2019, 3, 29), reverse=False)
        cerebro.adddata(data)

        for period in range(119, 120, 1):
            MRStrategy.MRStrategyAdj.period = period
            cerebro.run()

            plot['period_x'].append(period)
            plot['cash_y'].append(cerebro.broker.getvalue())

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
            plt.ylabel('Money [$]')
            plt.show()

def allInOne(strategy, chosen_stock=None):
    # cerebro = bt.Cerebro(stdstats=False)
    cerebro = bt.Cerebro()
    cerebro.addstrategy(strategy)
    cerebro.broker.setcommission(commission=0.0025)
    cerebro.addsizer(bt.sizers.FixedSize, stake=20)
    # cerebro.addsizer(bt.sizers.PercentSizer, percents=10)
    cerebro.broker.setcash(10000.0)

    for key, value in data_descriptors.data.items():
        # Preconfig
        # if chosen_stock and key != chosen_stock:
        #     break
        # Invariables


        plot = {'period_x': list(),
                'cash_y': list()
                }
        fd, td = value['fromdate'], value['todate']
        data = bt.feeds.YahooFinanceCSVData(dataname=f"data/csv/{key}.csv",
                                            # fromdate=datetime.datetime(td['y'], td['m'], td['d'])
                                            fromdate=datetime.datetime(2012, 1, 1),
                                            todate=datetime.datetime(td['y'], td['m'], td['d']), reverse=False)
        # todate=datetime.datetime(2018, 3, 28), reverse=False)
        cerebro.adddata(data)

    for period in range(119, 120, 1):
        MRStrategy.MRStrategyAdj.period = period
        cerebro.run()

        plot['period_x'].append(period)
        plot['cash_y'].append(cerebro.broker.getvalue())

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
        plt.ylabel('Value [$]')
        plt.show()

if __name__ == '__main__':
    # allThreeTest(DummyStrategy.Strategy, chosen_stock="TSLA")
    allThreeTest(DummyStrategy.Strategy)
    # allInOne(DummyStrategy.Strategy)
