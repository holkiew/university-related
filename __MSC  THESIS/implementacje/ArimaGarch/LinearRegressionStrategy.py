from __future__ import (absolute_import, division, print_function,
                        unicode_literals)

import datetime  # For datetime objects
import time
import json

# Import the backtrader platform
import backtrader as bt
import matplotlib.pyplot as plt

from MeanReversionSMA.data.data_descriptors import data as data_desc
from ArimaGarch.LinearRegressionIndicator import LinearRegressionIndicator

#https://www.investopedia.com/terms/g/goldencross.asp

class LinearRegressionStrategy(bt.Strategy):
    params = (
        ('maperiod', 1),
        ('printlog', False),
        ('rsi_period', 12),
        ('boll_period', 12),
        ('boll_devf', 10),
        ('rsi_bot', 40),
        ('lreg_period', 12),
        ('value', 0)
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
        self.orderMark = False
        self.orderCloseCounter = 0;
        self.linearReg = LinearRegressionIndicator(period=self.params.lreg_period)
        self.boll = bt.indicators.BollingerBands(period=self.params.boll_period, devfactor=(self.params.boll_devf/10))
        self.rsi = bt.indicators.RSI_EMA(period=self.params.rsi_period)

    def next(self):
        # Check if an order is pending ... if yes, we cannot send a 2nd one
        # wersja 1
        if not self.position:
            if self.linearReg[0] < self.boll.bot:
                self.orderMark = True
                self.log('BUY CREATE, %.2f' % self.dataclose[0])
                if self.rsi < self.params.rsi_bot:
                    size = self.broker.getvalue()*2 / self.dataclose[0]
                    self.buy(size=size)
                    self.orderMark = False
            else:
                if self.orderMark:
                    self.buy()
                    self.orderMark = False

        else:
            if self.linearReg[0] > self.boll.top:
                self.close()
                self.log('Close CREATE, %.2f' % self.dataclose[0])

        # if not self.position:
        #     if self.linearReg[0] < self.boll.bot:
        #         self.orderMark = True
        #         self.log('BUY CREATE, %.2f' % self.dataclose[0])
        #         if self.rsi < self.params.rsi_bot:
        #             size = self.broker.getvalue()*2 / self.dataclose[0]
        #             self.buy(size=size)
        #         else:
        #             self.buy()
        # else:
        #     if self.linearReg[0] > self.boll.top:
        #         self.close()

    def stop(self):
        self.params.value = self.broker.getvalue()
        if self.params.printlog:
            print(self.getdatanames()[0] + 'boll_period: %2d, boll_devf: %2d, rsi_bot: %2d, lreg_period: %2d, Value: %.2f' %(self.params.boll_period, self.params.boll_devf, self.params.rsi_bot, self.params.lreg_period, self.broker.getvalue()) )