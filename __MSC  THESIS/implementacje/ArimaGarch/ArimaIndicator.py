import math
from functools import reduce
import operator
from operator import itemgetter
from decimal import Decimal
from sklearn.linear_model import LinearRegression
import backtrader as bt
from pandas.tseries.offsets import BMonthEnd
import datetime as dt
from statsmodels.tsa.arima_model import ARIMA
from pandas.plotting import autocorrelation_plot
from sklearn.metrics import mean_squared_error
import numpy as np
from backtrader.utils.date import num2date, date2num
from collections import deque

class ArimaIndicator(bt.Indicator):
    lines = ('prediction',)
    params = (('learn_period', 50), ('learn_all', True))
    plotinfo = dict(subplot=False)

    def __init__(self):
        self.addminperiod(self.params.learn_period)
        self.prices = deque()

    def prenext(self):
        self.prices.append(self.data.get(0))


    def next(self):
        # currentDate = num2date(self.data.datetime[0])
        # print(currentDate.strftime('%Y-%m-%d'))

        if not self.params.learn_all:
            self.prices.popleft()
        self.prices.append(self.data.get(0))

        prediction = None
        try:
            prices = list(map(lambda x: x[0], list(self.prices)))
            model = ARIMA(prices, order=(5,1,0)).fit(disp=0)
            output = model.forecast()
            prediction = output[0]
        except Exception as e:
            print ("eerr")

        self.lines.prediction[0] = prediction[0] if prediction is not None else 0
        return prediction[0] if prediction is not None else 0