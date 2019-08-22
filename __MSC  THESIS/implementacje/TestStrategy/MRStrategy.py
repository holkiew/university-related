from __future__ import (absolute_import, division, print_function,
                        unicode_literals)
import backtrader as bt


class MRStrategyAdj(bt.Strategy):
    params = (
        ("devfactor", 1),
        ("debug", True)
    )

    period = 20
    def __init__(self):
        self.boll = bt.indicators.BollingerBands(period=self.period, devfactor=self.p.devfactor)
        # self.sx = bt.indicators.CrossDown(self.data.close, self.boll.lines.top)
        # self.lx = bt.indicators.CrossUp(self.data.close, self.boll.lines.bot)

    def next(self):

        orders = self.broker.get_orders_open()

        # Cancel open orders so we can track the median line
        if orders:
            for order in orders:
                self.broker.cancel(order)
        # if we don't have opened position, assures that we have only one at time
        if self.data.close > self.boll.lines.top:
            self.sell()

        if self.data.close < self.boll.lines.bot:
            self.buy()
        # if one exists, set its sell/buy price on current middle line price
        else:
            if self.position.size > 0:
                self.sell(exectype=bt.Order.StopTrail, trailpercent=0.1)

            else:
                self.buy(exectype=bt.Order.StopTrail, trailpercent=0.1)

    def notify_trade(self, trade):
        if trade.isclosed & self.p.debug:
            dt = self.data.datetime.date()
            print('---------------------------- TRADE ---------------------------------')
            #print("1: Data Name:                            {}".format(trade.data._name))
            #print("2: Bar Num:                              {}".format(len(trade.data)))
            print("3: Current date:                         {}".format(dt))
            #print('4: Status:                               Trade Complete')
            #print('5: Ref:                                  {}'.format(trade.ref))
            #print(f'cash: {round(self.broker.get_cash(),2)} value: {round(self.broker.getvalue(),2)}' )
            print('6: PnL:                                  {}'.format(round(trade.pnl, 2)))
            print('--------------------------------------------------------------------')

    def stop(self):
        print(self.getdatanames()[0] + ' (MA Period %2d) Value: %.2f' %
              (self.period, self.broker.getvalue()))


class MRStrategy(bt.Strategy):
    params = (
        ("period", 20),
        ("devfactor", 2.5),
        ("debug", False)
    )

    period = 20

    def __init__(self):
        self.boll = bt.indicators.BollingerBands(period=self.period, devfactor=self.p.devfactor)
        # self.sx = bt.indicators.CrossDown(self.data.close, self.boll.lines.top)
        # self.lx = bt.indicators.CrossUp(self.data.close, self.boll.lines.bot)

    def next(self):

        orders = self.broker.get_orders_open()

        # Cancel open orders so we can track the median line
        if orders:
            for order in orders:
                self.broker.cancel(order)

        if not self.position:

            if self.data.close > self.boll.lines.top:
                self.sell(exectype=bt.Order.Stop, price=self.boll.lines.top[0])

            if self.data.close < self.boll.lines.bot:
                self.buy(exectype=bt.Order.Stop, price=self.boll.lines.bot[0])

        else:

            if self.position.size > 0:
                self.sell(exectype=bt.Order.Limit, price=self.boll.lines.mid[0])

            else:
                self.buy(exectype=bt.Order.Limit, price=self.boll.lines.mid[0])

    def notify_trade(self, trade):
        if trade.isclosed & self.p.debug:
            dt = self.data.datetime.date()
            print(f'cash: {self.broker.get_cash()} value: {self.broker.getvalue()}')
            print('6: PnL:                                  {}'.format(round(trade.pnl, 2)))

    def stop(self):
        print(self.getdatanames()[0] + ' (MA Period %2d) Value: %.2f' %
              (self.period, self.broker.getvalue()))
        return 1
