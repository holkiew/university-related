from pandas import datetime, DataFrame, read_csv
from matplotlib import pyplot
from statsmodels.tsa.arima_model import ARIMA
from pandas.plotting import autocorrelation_plot
from sklearn.metrics import mean_squared_error
from sklearn.linear_model import LinearRegression
from arch import arch_model

import pyflux as pf
import numpy as np
import pyflux as pf
import pandas as pd
from pandas_datareader import DataReader
from datetime import datetime
from sklearn import preprocessing, svm
from sklearn.model_selection import train_test_split

import quandl
import pandas as pd
import numpy as np
import datetime

from sklearn.linear_model import LinearRegression
from sklearn import preprocessing, svm


# https://machinelearningmastery.com/arima-for-time-series-forecasting-with-python/
# http://www.blackarbs.com/blog/time-series-analysis-in-python-linear-models-to-garch/11/1/2016#GARCH
# https://barnesanalytics.com/garch-models-in-python
# https://www.researchgate.net/post/Can_we_use_ARIMA_approach_for_forecasting_high_volatility_data
# https://medium.com/fintechexplained/understanding-auto-regressive-model-arima-4bd463b7a1bb
# https://people.duke.edu/~rnau/411arim.htm
def parser(x):
    return datetime.datetime.strptime(x, '%Y-%m-%d')

if __name__ == '__main__':
    series = read_csv('data/csv/BTC-USD.csv', header=0,
                      parse_dates=[0], index_col=[0],
                      date_parser=parser, usecols=['Date', 'Close'],
                      nrows=20)
    # print(series.head())
    # 1
    # series.plot()
    # pyplot.show()
    # autocorrelation_plot(series)

    # 2
    # model = ARIMA(series, order=(5,1,0))
    # model_fit = model.fit(disp=0)
    # print(model_fit.summary())
    # # plot residual errors
    # residuals = DataFrame(model_fit.resid)
    # residuals.plot()
    # pyplot.show()
    # residuals.plot(kind='kde')
    # pyplot.show()
    # print(residuals.describe())

    # ARCH
    X = series.values
    size = int(len(X) * 0.66)
    train, test = X[0:size], X[size:len(X)]
    history = [x for x in train]
    predictions = list()
    for t in range(len(test)):
        model = ARIMA(history, order=(5,1,0)).fit(disp=0)
        output = model.forecast()
        yhat = output[0]
        predictions.append(yhat)
        obs = test[t]
        history.append(obs)
        print('predicted=%f, expected=%f' % (yhat, obs))
    error = mean_squared_error(test, predictions)
    print('Test MSE: %.3f' % error)
    # plot
    pyplot.plot(test)
    pyplot.plot(predictions, color='red')
    pyplot.show()

    # print("confidence: ", confidence)
    # print(forecast_prediction)
    # pyplot.plot(forecast_prediction, "red")
    # pyplot.plot(prices, "blue")
    # pyplot.plot(test)
    # pyplot.plot(predictions, color='red')
