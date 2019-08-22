import math
from functools import reduce
import operator
from operator import itemgetter
from decimal import Decimal
import backtrader as bt

class GARR(bt.Indicator):
    lines = ('GARR',)
    params = (('period', 12),)

    def __init__(self):
        self.addminperiod(self.params.period + 1)

    def next(self):
        try:
            returns = list()
            for i in range(self.params.period):
                close_price = self.data.get(-i)[0]
                open_price = self.data.get(-i-1)[0]
                ret = (close_price - open_price) / open_price
                ret = (1 + ret) ** (1/float(self.params.period))
                returns.append(Decimal(ret))

            GARR1 = returns[0] - 1
            GARRPeriod = reduce(operator.mul, returns, 1) - 1
            ratio = GARR1/GARRPeriod
            if ratio > 20:
                ratio = 20
            else:
                if ratio < -20:
                    ratio = -20
        except Exception as e:
            ratio = -21
            print (e, self.data._dataname)
        self.lines.GARR[0] = ratio
        return ratio