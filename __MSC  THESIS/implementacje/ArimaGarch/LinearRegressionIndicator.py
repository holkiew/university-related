import math
from functools import reduce
import operator
from operator import itemgetter
from decimal import Decimal
from sklearn.linear_model import LinearRegression
import backtrader as bt
from pandas.tseries.offsets import BMonthEnd
import datetime as dt
import numpy as np
from backtrader.utils.date import num2date, date2num

class LinearRegressionIndicator(bt.Indicator):
    lines = ('LinearRegression1', 'LinearRegression5', 'LinearRegression10', 'LinearRegression20')
    params = (('period', 30), ('show_predictions', False))
    plotinfo = dict(subplot=False)

    def __init__(self):
        self.addminperiod(self.params.period + 1)

    def next(self):
        currentDate = num2date(self.data.datetime[0])
        #print(currentDate.strftime('%Y-%m-%d'))

        offset = BMonthEnd()

        #Last day of current month
        lastBussinessDay = offset.rollforward(currentDate)
        # print (lastBussinessDay)

        prices = list()
        dates = list()
        forecast_dates = list()
        for i in range(self.params.period):
            prices.append(self.data.get(-i))
            dates.append(self.data.datetime.get(-i))

        for i in range (20):
            forecast_dates.append((self.data.datetime.get(0)[0] + i + 1.0))

        # Convert to 1d Vector
        dates = np.reshape(dates, (len(dates), 1))
        prices = np.reshape(prices, (len(prices), 1))

        # Training
        lr_model = LinearRegression()
        lr_model.fit(dates, prices)
        forecast_prediction = lr_model.predict(np.reshape(forecast_dates, (len(forecast_dates),1)))
        self.lines.LinearRegression1[0] = forecast_prediction[0][0]
        if self.params.show_predictions:
            self.lines.LinearRegression5[0] = forecast_prediction[4][0]
            self.lines.LinearRegression10[0] = forecast_prediction[9][0]
            self.lines.LinearRegression20[0] = forecast_prediction[19][0]
        return (forecast_prediction[0][0], forecast_prediction)