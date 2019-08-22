from __future__ import (absolute_import, division, print_function,
                        unicode_literals)

import datetime  # For datetime objects
import time
import json

# Import the backtrader platform
import backtrader as bt
import matplotlib.pyplot as plt

from MeanReversionSMA.data.data_descriptors import data as data_desc


# Create a Stratey
class TestStrategy(bt.Strategy):
    params = (
        ('maperiod', 1),
        ('printlog', False),
    )

    maperiod = 1

    def log(self, txt, dt=None, doprint=False):
        ''' Logging function fot this strategy'''
        if self.params.printlog or doprint:
            dt = dt or self.datas[0].datetime.date(0)
            print('%s, %s' % (dt.isoformat(), txt))

    def __init__(self):
        # Keep a reference to the "close" line in the data[0] dataseries
        self.dataclose = self.datas[0].close
        # self.params.maperiod = self.maperiod
        # To keep track of pending orders and buy price/commission
        self.order = None
        self.buyprice = None
        self.buycomm = None
        self.sma = bt.indicators.MovingAverageSimple(self.datas[0],
                                                     period=self.maperiod)

    def notify_order(self, order):
        if order.status in [order.Submitted, order.Accepted]:
            # Buy/Sell order submitted/accepted to/by broker - Nothing to do
            return

        # Check if an order has been completed
        # Attention: broker could reject order if not enough cash
        if order.status in [order.Completed]:
            if order.isbuy():
                self.log(
                    'BUY EXECUTED, Price: %.2f, Cost: %.2f, Comm %.2f' %
                    (order.executed.price,
                     order.executed.value,
                     order.executed.comm))

                self.buyprice = order.executed.price
                self.buycomm = order.executed.comm
            else:  # Sell
                self.log('SELL EXECUTED, Price: %.2f, Cost: %.2f, Comm %.2f' %
                         (order.executed.price,
                          order.executed.value,
                          order.executed.comm))

            self.bar_executed = len(self)

        elif order.status in [order.Canceled, order.Margin, order.Rejected]:
            self.log('Order Canceled/Margin/Rejected')

        # Write down: no pending order
        self.order = None

    def notify_trade(self, trade):
        if not trade.isclosed:
            return

        self.log('OPERATION PROFIT, GROSS %.2f, NET %.2f' %
                 (trade.pnl, trade.pnlcomm))

    def next(self):
        # Simply log the closing price of the series from the reference
        self.log('Close, %.2f' % self.dataclose[0])

        # Check if an order is pending ... if yes, we cannot send a 2nd one
        if self.order:
            return

        # Check if we are in the market
        if not self.position:

            # Not yet ... we MIGHT BUY if ...
            if self.dataclose[0] > self.sma[0]:
                # BUY, BUY, BUY!!! (with all possible default parameters)
                self.log('BUY CREATE, %.2f' % self.dataclose[0])

                # Keep track of the created order to avoid a 2nd order
                self.order = self.buy()

        else:

            if self.dataclose[0] < self.sma[0]:
                # SELL, SELL, SELL!!! (with all possible default parameters)
                self.log('SELL CREATE, %.2f' % self.dataclose[0])

                # Keep track of the created order to avoid a 2nd order
                self.order = self.sell()

    def stop(self):
        self.log(self.getdatanames()[0] + ' (MA Period %2d) Value: %.2f' %
                 (self.maperiod, self.broker.getvalue()), doprint=True)


if __name__ == '__main__':

    # data = bt.feeds.YahooFinanceCSVData(dataname="data/FB.csv",
    #                                     fromdate=datetime.datetime(2018, 5, 1),
    #                                     todate=datetime.datetime(2018, 12, 31), reverse=False)

    # Initialize cerebro

    # cerebro.run()

    for key, value in data_desc.items():
        # Preconfig
        cerebro = bt.Cerebro()
        cerebro.addstrategy(TestStrategy)
        cerebro.broker.setcommission(commission=0.0025)
        cerebro.addsizer(bt.sizers.FixedSize, stake=10)
        # cerebro.addsizer(bt.sizers.PercentSizer, percents=50)
        cerebro.broker.setcash(10000.0)

        plot = {'period_x': list(),
                'cash_y': list()
                }
        fd, td = value['fromdate'], value['todate']
        # data = bt.feeds.YahooFinanceCSVData(dataname=f"data/{key}.csv",
        #                                     fromdate=datetime.datetime(fd['y'], fd['m'], fd['d']),
        #                                     todate=datetime.datetime(td['y'], td['m'], td['d']), reverse=False)

        data = bt.feeds.YahooFinanceCSVData(dataname=f"data/{key}.csv",
                                            fromdate=datetime.datetime(2016, 1, 1),
                                            todate=datetime.datetime(td['y'], td['m'], td['d']), reverse=False)
        cerebro.adddata(data)

        for period in range(10, 500, 10):
            TestStrategy.maperiod = period
            cerebro.run()

            plot['period_x'].append(period)
            plot['cash_y'].append(cerebro.broker.getvalue())

            plt.plot(plot['period_x'], plot['cash_y'])

        # with open(f'data/{key}.json', 'a+') as json_file:
        #     json.dump(plot, json_file)
        plt.title(f'{key}: {datetime.datetime.now().strftime("(%H:%M:%S) %Y-%m-%d")}')
        plt.show()


    # plt.show()
    # Print out the final result
    # print('Final Portfolio Value: %.2f' % cerebro.broker.getvalue())

    # Plot the result
    # cerebro.plot()
