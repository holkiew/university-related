from __future__ import (absolute_import, division, print_function,
                        unicode_literals)

import datetime  # For datetime objects
import time
import json
import matplotlib.pyplot as plt
from MeanReversionSMA.data.data_descriptors import data as data_desc

# Import the backtrader platform
import backtrader as bt

#https://www.investopedia.com/terms/g/goldencross.asp

class StrategyAdj(bt.Strategy):
    params = (
        ('smaS', 50),
        ('smaL', 200),
        ('rsi_period', 14),
        ('osc_period', 4),
        ('osc_bot', -1),
        ('osc_top', 2),
        ('rsi_bot', 10),
        ('rsi_top', 90),
        ('value', 0),
        ('comm', 0),
        ('printlog', False),
        ('printstop', True),

    )

    maperiod = 1
    # state = H/L H - smaS is above smaL, L - smaS is below smaL
    state = ''

    def __init__(self):
        # Keep a reference to the "close" line in the data[0] dataseries
        self.dataclose = self.datas[0].close
        # self.params.maperiod = self.maperiod
        # To keep track of pending orders and buy price/commission
        self.order = None
        self.buyprice = None
        self.buycomm = None
        self.smaS = bt.indicators.MovingAverageSimple(period=self.params.smaS)
        self.smaL = bt.indicators.MovingAverageSimple(period=self.params.smaL)
        self.rsi = bt.indicators.RSI_SMA(self.data.close, period=self.params.rsi_period)
        self.osc = bt.indicators.accdecoscillator.AccelerationDecelerationOscillator(period=self.params.osc_period)

    def notify_order(self, order):
        if order.status in [order.Submitted, order.Accepted]:
            # Buy/Sell order submitted/accepted to/by broker - Nothing to do
            return
        # self.params.osc_top = self.data * 0.005
        # self.params.osc_bot = self.data * -0.025
        # Check if an order has been completed
        # Attention: broker could reject order if not enough cash
        if order.status in [order.Completed]:
            if order.isbuy() and self.params.printlog:
                print(
                    'BUY EXECUTED, Price: %.2f, Cost: %.2f, Comm %.2f' %
                    (order.executed.price,
                     order.executed.value,
                     order.executed.comm))

                self.buyprice = order.executed.price
                self.buycomm = order.executed.comm
            else:  # Sell
                if self.params.printlog:
                    print('SELL EXECUTED, Price: %.2f, Cost: %.2f, Comm %.2f' %
                             (order.executed.price,
                              order.executed.value,
                              order.executed.comm))

            self.bar_executed = len(self)

        elif order.status in [order.Canceled, order.Margin, order.Rejected] and self.params.printlog:
            print('Order Canceled/Margin/Rejected')

        # Write down: no pending order
        self.order = None

    def notify_trade(self, trade):
        if not trade.isclosed:
            return
        if self.params.printlog:
            print('OPERATION PROFIT, GROSS %.2f, NET %.2f' %
                 (trade.pnl, trade.pnlcomm))
        self.params.comm += trade.pnlcomm

    def next(self):
        # Check if an order is pending ... if yes, we cannot send a 2nd one
        if self.params.printlog:
            print (self.data.datetime.date())
        if self.order:
            return

    # jesli nastapi przeciecie linii, wychodzimy z aktualnych pozycji. Wewnetrze ify po to zeby nie triggerowac ciagle
        if self.smaS > self.smaL:
            if self.state == 'SELL' or self.state == '':
                self.state = 'BUY'
                if self.position:
                    if self.params.printlog:
                        print ('close 1:', self.data[0])
                    self.close()
        else:
            if self.state == 'BUY' or self.state == '':
                self.state = 'SELL'
                if self.position:
                    if self.params.printlog:
                        print ('close 2: ', self.data[0])
                    self.close()
        osc_top = self.data * self.params.osc_top/1000
        osc_bot = -self.data * self.params.osc_bot/1000
        if not self.position:
            if self.osc <= self.params.osc_bot:
                if self.state == 'BUY' :
                    if self.params.printlog:
                        print ('buy H: ', self.data[0])
                    self.order = self.buy()
                if self.state == 'SELL' :
                    if self.params.printlog:
                        print ('sell L: ', self.data[0])
                    self.order = self.sell()
        else:
            if (self.osc >= osc_top or self.rsi > self.params.rsi_top) and self.state == 'BUY': #or self.rsi >= self.params.rsi_top or self.rsi < 30 or self.osc <= self.params.osc_bot:
                    if self.params.printlog:
                        print ('close H: ', self.data[0])
                    self.order = self.close()
            if (self.osc <= osc_bot or self.rsi < self.params.rsi_bot) and self.state == 'SELL':
                self.order = self.close()

    def stop(self):
        self.params.value = self.broker.getvalue()
        if self.params.printstop:
            print(self.getdatanames()[0] + ' (Commisions Period %.2f) Value: %.2f' %
                    (self.params.comm, self.broker.getvalue()))
