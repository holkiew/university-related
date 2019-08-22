import backtrader as bt


class Strategy(bt.Strategy):
    params = (
        ("devfactor", 1),
        ("debug", True),
        ("buy", True)
    )

    period = 20

    def next(self):
            if self.position.size <= 0 and self.params.buy:
                self.buy()

    def stop(self):
        print(self.getdatanames()[0] + ' (MA Period %2d) Value: %.2f' %
              (self.period, self.broker.getvalue()))