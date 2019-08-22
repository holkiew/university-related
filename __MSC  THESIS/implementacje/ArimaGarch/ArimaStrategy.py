from __future__ import (absolute_import, division, print_function,
                        unicode_literals)
from statsmodels.tsa.arima_model import ARIMA
from pandas.plotting import autocorrelation_plot
from sklearn.metrics import mean_squared_error
import datetime  # For datetime objects
import time
import math
import json

# Import the backtrader platform
import backtrader as bt
import matplotlib.pyplot as plt
from ArimaGarch.ArimaIndicator import ArimaIndicator
from MeanReversionSMA.data.data_descriptors import data as data_desc
from ArimaGarch.LinearRegressionIndicator import LinearRegressionIndicator

#https://www.investopedia.com/terms/g/goldencross.asp

class ArimaStrategy(bt.Strategy):
    params = (
        ('rsi_period', 12),
        ('boll_period', 12),
        ('boll_devf', 10),
        ('rsi_bot', 40),
        ('lreg_period', 12),
        ('printlog', True),
        ('value', 0)
    )

    def __init__(self):
        # Keep a reference to the "close" line in the data[0] dataseries
        self.dataclose = self.datas[0].close
        # self.params.maperiod = self.maperiod
        # To keep track of pending orders and buy price/commission
        self.order = None
        self.negativeTrades = 0; self.positiveTrades = 0;
        self.negativeTradesComm = 0; self.positiveTradesComm = 0;
        self.trendPositive = 0; self.trendNegative = 0;
        self.properlyGuessed = {"2.0": 0, "1.75": 0, "1.5": 0, "1.25": 0,"1.0": 0, "0.75": 0, "0.5": 0, "0.25": 0, "0.1": 0}
        self.properlyGuessedPositiveTrend = {"2.0": 0, "1.75": 0, "1.5": 0, "1.25": 0,"1.0": 0, "0.75": 0, "0.5": 0, "0.25": 0, "0.1": 0}
        self.days = 0
        self.arima = ArimaIndicator(learn_period=20, learn_all=True)
        self.trend = ""
        self.orderMark = False
        self.linearReg = LinearRegressionIndicator(period=self.params.lreg_period)
        self.boll = bt.indicators.BollingerBands(period=self.params.boll_period, devfactor=(self.params.boll_devf/10))
        self.rsi = bt.indicators.RSI_EMA(period=self.params.rsi_period)

    def next(self):
        if self.arima[0] == 0 or self.arima[-1] == 0 :
            return
        self.days += 1
        diff = abs(self.data[0] - self.arima[-1]) / self.arima[-1]
        if (self.data[-1] < self.data[0] and self.data[-1] < self.arima[-1]) or (self.data[-1] > self.data[0] and self.data[-1] > self.arima[-1]):
            self.trendPositive += 1
            for k, v in self.properlyGuessedPositiveTrend.items():
                if not math.isnan(self.arima[-1]) and diff < float(k)/100: self.properlyGuessedPositiveTrend[k] = v + 1
        else :
            self.trendNegative += 1

        if self.p.printlog: print ("Current value: %.2f, Predicted: %.2f, Next prediction: %.2f" % (self.data[0], self.arima[-1], self.arima[0]));
        for k, v in self.properlyGuessed.items():
            if not math.isnan(self.arima[-1]) and diff < float(k)/100: self.properlyGuessed[k] = v + 1

        # if self.order:
        #     if self.order.isbuy():
        #         if not self.arima[0] > self.data[0]:
        #             self.order = self.close()
        #     elif self.order.issell():
        #         if not self.arima[0] < self.data[0]:
        #             self.order = self.close()
        # else:
        #     if self.arima[0] > self.data[0]:
        #         self.order = self.buy()
        #     elif self.arima[0] < self.data[0]:
        #         self.order = self.sell()

        if  self.linearReg[0] > self.linearReg[-1] or (self.arima[0] > self.data[0] and self.arima[-1] > self.data[-1]):
            self.trend = "BULL"
        elif self.linearReg[0] < self.linearReg[-1] or (self.arima[0] < self.data[0] and self.arima[-1] < self.data[-1]):
            self.trend = "BEAR"
        else:
            self.trend = "VOLATILITY"
        if not self.order:
            if self.trend == "BULL":
                self.orderMark = True
                if self.rsi < self.params.rsi_bot:
                    if self.p.printlog: print('BUY CREATE, %.2f' % self.dataclose[0]);
                    size = self.broker.getvalue() / self.dataclose[0]
                    self.order = self.buy(size=size*2)
                    self.orderMark = False
            elif self.trend == "VOLATILITY":
                if self.orderMark and self.linearReg[0] < self.boll.bot:
                    if self.p.printlog: print('BUY CREATE VOLATIL, %.2f' % self.dataclose[0]);
                    size = self.broker.getvalue() / self.dataclose[0]
                    self.order = self.buy(size = size)
                    self.orderMark = False

        else:
            if self.linearReg[0] > self.boll.top :
                # or (self.arima[0] < self.data[0] and self.arima[-1] < self.data[-1])
                self.order = self.close()
                if self.p.printlog: print('Close CREATE, %.2f' % self.dataclose[0]);

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



    def notify_trade(self, trade):
        if trade.pnl > 0:
            self.positiveTrades += 1
        else:
            self.negativeTrades += 1
        if trade.pnlcomm > 0:
            self.positiveTradesComm += 1
        else:
            self.negativeTradesComm += 1


    def stop(self):
        self.p.value = self.broker.getvalue()
        if self.p.printlog:
            print(self.getdatanames()[0] + 'boll_period: %2d, boll_devf: %2d, rsi_bot: %2d, lreg_period: %2d, Value: %.2f' %(self.params.boll_period, self.params.boll_devf, self.params.rsi_bot, self.params.lreg_period, self.broker.getvalue()) )
            print ('Trades: positive: %2d = %.0f%% negative: %2d ' % (self.positiveTrades, self.positiveTrades/(self.positiveTrades+ self.negativeTrades) * 100, self.negativeTrades))
            print ('Trades after commision: positive: %2d = %.0f%% negative: %2d' % (self.positiveTradesComm,self.positiveTradesComm/(self.negativeTradesComm+self.positiveTradesComm)  * 100,self.negativeTradesComm ))
            print ('Trends prediction: positive: %2d = %.0f%% negative: %2d ' % (self.trendPositive, self.trendPositive/(self.trendPositive + self.trendNegative) * 100, self.trendNegative))
            print ("Properly guessed % prices ", self.properlyGuessed, "total:", self.days)
            for k,v in self.properlyGuessed.items():
                print (k, "%%= %.0f%%" % (int(v)/self.days*100))

            print ("Properly guessed % prices AND trends: ", self.properlyGuessedPositiveTrend, "total:", self.days)
            for k,v in self.properlyGuessedPositiveTrend.items():
                print (k, "%%= %.0f%%" % (int(v)/self.days*100))
