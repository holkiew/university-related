import math
from functools import reduce
import operator
from operator import itemgetter
from decimal import Decimal
import backtrader as bt
from statistics import mean
import pandas
import collections
from backtrader.utils.date import num2date

class BetaIndicator(bt.Indicator):
    lines = ('beta',)
    params = (('period', 12),)

    datafile = collections.OrderedDict()
    datafileInitialized = False


    def __init__(self):
        self.addminperiod(self.params.period + 1)
        if not BetaIndicator.datafileInitialized:
            df = pandas.read_csv('data/csv/W5000.csv')
            for index, row in df.iterrows():
                BetaIndicator.datafile[row.Date] = row.Close
            BetaIndicator.datafileInitialized = True

    def next(self):
        beta = 0
        try:
            prices = list()
            externalPrices = list()
            for i in range(self.params.period):
                prices.append(self.data.get(-i)[0])
                # if there is no corresponding date, we are looking for first previous existing
                counter = 0
                while num2date(self.data.datetime[-i - counter]).strftime('%Y-%m-%d') not in BetaIndicator.datafile:
                    counter+=1
                externalDataPrice = BetaIndicator.datafile[num2date(self.data.datetime[-i-counter]).strftime('%Y-%m-%d')]
                externalPrices.append(externalDataPrice)
            prices_mean = mean(prices)
            externalPrices_mean = mean(externalPrices)
            convariancePartials = list()
            variancePartials = list()
            for i in range(len(prices)):
                convariancePartials.append((prices[i] - prices_mean) * (externalPrices[i] - externalPrices_mean))
                variancePartials.append(externalPrices[i] - externalPrices_mean)

            covariance = sum(convariancePartials) / (self.params.period - 1)
            variance = ((pow(sum(variancePartials), 2) / self.params.period)**(1/2))

            beta = covariance/variance

        except Exception as e:

            if ("float division by zero" in str(e)):
                beta = 0
            else:
                print ("BETA INDICATOR ERROR:", e, self.data._dataname)
        self.lines.beta[0] = beta
        return beta