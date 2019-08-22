from __future__ import (absolute_import, division, print_function,
                        unicode_literals)
import datetime  # For datetime objects
import time
import json
import matplotlib.pyplot as plt

# Import the backtrader platform
import backtrader as bt


class Strategy(bt.Strategy):
    params = (
        ('smaS_period', 50),
        ('smaL_period', 200),
        ('rsi_period', 14),
        ('rsi_top', 70),
        ('rsi_bot', 30),
        ('printlog', False),
        ('printstop', False),
        ('value', 0)
    )

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
        self.value = 0
        self.buyprice = None
        self.buycomm = None
        self.smaS = bt.indicators.MovingAverageSimple(period=self.params.smaS_period)
        self.smaL = bt.indicators.MovingAverageSimple(period=self.params.smaL_period)
        self.rsi = bt.indicators.RSI_SMA(self.data.close, period=self.params.rsi_period)

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
        # Check if an order is pending ... if yes, we cannot send a 2nd one
        if self.order:
            return

        # # Check if we are in the market
        # if not self.position:
        #
        #     # Not yet ... we MIGHT BUY if ...
        #     if self.dataclose[0] > self.smaS[0]:
        #         self.log('BUY CREATE, %.2f' % self.dataclose[0])
        #         self.order = self.buy()
        #
        # else:
        #
        #     if self.dataclose[0] < self.smaS[0]:
        #         self.log('SELL CREATE, %.2f' % self.dataclose[0])
        #         self.order = self.sell()
        # Check if we are in the market
        # Jesli mamy crossing to sie wypierdalamy i sprzedajemy. Goldencross na average + RMI
        if not self.position:
            if self.smaS > self.smaL:
                if self.rsi <= self.params.rsi_bot:
                    self.log('BUY CREATE, %.2f' % self.dataclose[0])
                    self.order = self.buy()

        else:
            if self.rsi >= self.params.rsi_top and self.smaS <= self.smaL:
                self.log('SELL CREATE, %.2f' % self.dataclose[0])
                self.order = self.close()

    def stop(self):
        self.p.value = self.broker.getvalue()
        self.value = self.broker.getvalue()
        if self.params.printstop:
            self.log(self.getdatanames()[0] + ' (RSI Period %2d smaL %3d smaS %3d rsi_top %2d rsi_bot %2d) Value: %.2f' %
                     (self.params.rsi_period, self.params.smaL_period, self.params.smaS_period, self.params.rsi_top, self.params.rsi_bot, self.broker.getvalue()), doprint=True)