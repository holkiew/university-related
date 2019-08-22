import backtrader as bt
import datetime

from collections import OrderedDict



# https://www.quantconnect.com/tutorials/strategy-library/momentum-short-term-reversal-strategy


if __name__ == '__main__':
    cerebro = bt.Cerebro()
    cerebro.addstrategy(MStrategy, no_of_stocks=2)
    cerebro.broker.setcommission(commission=0.0025)
    # cerebro.addsizer(bt.sizers.FixedSize, stake=20)
    # cerebro.addsizer(bt.sizers.PercentSizer, percents=50)
    cerebro.broker.setcash(10000.0)
    cerebro.broker.set_coc(True)
    # cerebro.broker.set_coo(True)
    # stocks = "ORCL, TSLA"
    stocks = "TSLA, JPM, FB, TUWOY"

    # Handy dictionary for the argument timeframe conversion
    tframes = dict(
        daily=bt.TimeFrame.Days,
        weekly=bt.TimeFrame.Weeks,
        monthly=bt.TimeFrame.Months)

    # Add the resample data instead of the original

    for stockname in stocks.split(', '):
        data = bt.feeds.YahooFinanceCSVData(dataname=f"data/csv/{stockname}.csv",
                                         fromdate=datetime.datetime(2012, 5, 18),
                                         todate=datetime.datetime(2019, 3, 29))
        cerebro.resampledata(data,
                             timeframe=tframes["monthly"],
                             compression=1)

    cerebro.run()
    cerebro.plot()