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

from MeanReversionSMA.data.data_descriptors import data as data_desc
from ArimaGarch.LinearRegressionIndicator import LinearRegressionIndicator
from ML.OnBalanceVolumeIndicator import OnBalanceVolume

#https://www.investopedia.com/terms/g/goldencross.asp

class MLStrategy(bt.Strategy):
    params = (
        ('maperiod', 1),
        ('evaluator', None)
    )


    def __init__(self):
        self.order = None
        self.features = collections.OrderedDict()
        self.linearReg = LinearRegressionIndicator(period=20)
        self.boll = bt.indicators.BollingerBands(period=20, devfactor=1.5)
        self.rsi = bt.indicators.RSI_EMA(period=12)
        self.smaS = bt.indicators.MovingAverageSimple(period=25)
        self.smaL = bt.indicators.MovingAverageSimple(period=100)
        self.macd = bt.indicators.MACD(period_me1 =12, period_me2=26, period_signal = 9)
        self.obv = OnBalanceVolume()

    def next(self):
        feature = self.gatherFeature()
        action = self.params.evaluator(feature)

        try:
            if not self.order:
                if action == 1:
                    self.order = self.buy()
                elif action == -1:
                    self.order = self.sell()
            else:
                if self.order.isbuy() and action == -1:
                    self.close()
                    self.order = None
                elif self.order.issell() and action == 1:
                    self.close()
                    self.order = None

        except Exception as e:
            # print(str(e))
            self.close()


    def gatherFeature(self):
        # wersja dla tego samego stocka na nauczanie
        # feature = []
        # # price diffrence
        # # feature.append(self.data[0] - self.data[-1])
        # feature.append(self.obv.lines.obv[0])
        # #indicators
        # feature.append(self.linearReg[0])
        # feature.append(self.boll.top[0])
        # feature.append(self.boll.bot[0])
        # feature.append(self.rsi[0])
        # feature.append(self.smaS[0])
        # feature.append(self.smaL[0])
        # feature.append(self.macd.macd[0])
        # feature.append(self.macd.signal[0])
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
        return [feature]

    def stop(self):
        self.params.value = self.broker.getvalue()
        print(self.getdatanames()[0] + 'Value: %.2f' %(self.broker.getvalue()) )