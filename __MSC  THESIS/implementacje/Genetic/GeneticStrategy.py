import backtrader as bt


class GeneticStrategy(bt.Strategy):
    params = (
        ("roc", 30),
        ("rsi_change",60),
        ("percD", 51),
        ("percK", 75.9),
        ("osc_ad", 12.5),
        ("macd", 14.9),
        ("rsi_period", int(20)),
        ("stoch_period", int(20)),
        ("osc_ad_period", int(20)),
        ("macd_me1_period", int(12)),
        ("macd_me2_period", int(26)),
        ("roc_period", int(12)),

        ("macd_period_signal", int(9)),

        ("debug", False)
    )

    period = 20

    def __init__(self):
        self.rsi = bt.indicators.RSI(period=int(self.params.rsi_period))
        self.stoch = bt.indicators.Stochastic(period=int(self.params.stoch_period))
        self.osc_ad = bt.indicators.AccelerationDecelerationOscillator(period=int(self.params.osc_ad_period))
        self.macd = bt.indicators.MACD(period_me1 = int(self.params.macd_me1_period), period_me2 = int(self.params.macd_me2_period), period_signal = int(self.params.macd_period_signal))
        self.roc = bt.indicators.ROC100(period=int(self.params.roc_period))

        self.value = 0
        self.waitMoreDays = 0
        self.counter = 0
        self.order = None
        self.markedToBuy = False
        self.markedToSell = False
        self.out = False

    def next(self):
        if self.waitMoreDays < 1:
            self.waitMoreDays += 1
            return
        if self.out:
            self.close()
            return
        if self.broker.getvalue() < 0:
            self.close()
            self.out = True
            return

        size = self.broker.getvalue() * 0.99 / self.data[0]

        if self.markedToBuy or self.markedToSell:
            if self.markedToBuy:
                self.order = self.buy(size=size)
                self.markedToBuy = False
                # print("Close Bought. Value: %.2f" % self.broker.getvalue())
            else:
                self.order = self.sell(size=size)
                self.markedToSell = False
                # print("Close Sold. Value: %.2f" % self.broker.getvalue())
            return

        # print(self.data.datetime.date())
        rsi_change = abs(self.rsi[-1] - self.rsi[0])

        # self.order.ordtype "0 -  buy", "1, sell"
        if self.roc >= self.params.roc and rsi_change < self.params.rsi_change and self.stoch.percD[-1] < self.params.percD and self.osc_ad[-1] < self.params.osc_ad and self.macd.macd[-1] < self.params.macd and self.stoch.percK < self.params.percK:
            if not self.order:
                self.order = self.buy(size=size)
                # print("Bought. Value: %.2f" % self.broker.getvalue())
            elif self.order.ordtype == 1:
                self.close()
                self.markedToBuy = True

        else:
            if not self.order:
                self.order = self.sell(size=size)
                # print("Sold. Value: %.2f" % self.broker.getvalue())
            elif self.order.ordtype == 0:
                self.close()
                self.markedToSell = True

    def notify_trade(self, trade):
        if trade.status == 2:
            self.order = None

    def stop(self):
        self.value = self.broker.getvalue()
        if self.params.debug:
            print(self.getdatanames()[0] + ' (MA Period %2d) Value: %.2f' %
                  (self.period, self.broker.getvalue()))