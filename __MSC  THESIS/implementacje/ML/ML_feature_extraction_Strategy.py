from __future__ import (absolute_import, division, print_function,
                        unicode_literals)

import datetime  # For datetime objects
import time
import json
import random
import collections
# Import the backtrader platform
import backtrader as bt
import matplotlib.pyplot as plt
from ML.OnBalanceVolumeIndicator import OnBalanceVolume

from MeanReversionSMA.data.data_descriptors import data as data_desc
from ArimaGarch.LinearRegressionIndicator import LinearRegressionIndicator

#https://www.investopedia.com/terms/g/goldencross.asp

class Strategy(bt.Strategy):
    params = (
        ('maperiod', 1),
    )


    def __init__(self):
        self.order = None
        self.daysIntoFuture = 20
        self.daysToClose = None
        self.dataclose = self.datas[0].close
        self.averageHoldDays = 4
        self.features = collections.OrderedDict()
        self.linearReg = LinearRegressionIndicator(period=20)
        self.boll = bt.indicators.BollingerBands(period=20, devfactor=1.5)
        self.rsi = bt.indicators.RSI_EMA(period=12)
        self.smaS = bt.indicators.MovingAverageSimple(period=25)
        self.smaL = bt.indicators.MovingAverageSimple(period=100)
        self.macd = bt.indicators.MACD(period_me1 =12, period_me2=26, period_signal = 9)
        self.obv = OnBalanceVolume(self.data)

    def next(self):
        if self.daysToClose:
            self.daysToClose -= 1
            if self.daysToClose <= 0:
                self.close()
                self.order = None
                self.daysToClose = None
            self.gatherFeature()
            return
        try:
            if not self.order:
                prices = []
                for i in range(self.averageHoldDays):
                    prices.append(self.data[i])
                average = sum(prices) / len(prices)
                diff = (average - self.data[0]) / self.data[0]
                if -0.02 > diff < 0.02:
                    return
                if self.data[self.daysIntoFuture] > self.data[0]:
                    self.order = self.buy()
                else:
                    self.order = self.sell()
            else:
                if self.data[self.daysIntoFuture] > self.data[0] and self.order.issell():
                    self.daysToClose = random.randint(int(self.daysIntoFuture*3/4), self.daysIntoFuture)
                if self.data[self.daysIntoFuture] < self.data[0] and self.order.isbuy():
                    self.daysToClose = random.randint(int(self.daysIntoFuture*3/4), self.daysIntoFuture)
        except Exception as e:
            # print(str(e))
            self.close()
        finally:
            self.gatherFeature()

    def gatherFeature(self):
        feature = []
        # price diffrence
        # feature.append(self.data[0] - self.data[-1])
        feature.append(self.obv.lines.obv[0])
        #indicators
        # feature.append(self.linearReg[0])
        # feature.append(self.boll.top[0])
        # feature.append(self.boll.bot[0])
        feature.append(self.rsi[0])
        # feature.append(self.smaS[0])
        # feature.append(self.smaL[0])
        feature.append(1 if self.smaS[0] > self.smaL[0] else -1)
        feature.append(self.macd.macd[0])
        feature.append(self.macd.signal[0])
        # long/short/nothing
        if self.order:
            y = 1 if self.order.isbuy() else -1
        else:
            y = 0
        self.features[self.data.datetime.date()] = (feature, y)



    def stop(self):
        self.params.value = self.broker.getvalue()
        print(self.getdatanames()[0] + 'Value: %.2f' %(self.broker.getvalue()) )