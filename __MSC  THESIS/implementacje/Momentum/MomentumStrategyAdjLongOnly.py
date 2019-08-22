from __future__ import (absolute_import, division, print_function,
                        unicode_literals)
import backtrader as bt
import datetime

from collections import OrderedDict
from Momentum.GAARindicator import GARR
from Momentum.BetaIndicator import BetaIndicator

#we long top half and short bottom half. Grading is based on indicator wages.
class MomentumStrategyAdj(bt.Strategy):
    params = (
        ("period", 12),
        ("no_of_stocks", 2),
        # Bigger one = better one
        ("GAAR_wage", 1.0),
        ("Beta_wage", 1.0),
        ("debug", False),
        ("inner_logs", False),
        ("value", 0)
    )

    def __init__(self):
        self.indicatorsGARR = {}
        self.indicatorsBeta = {}
        self.best_stocks = list()
        self.orders = {}
        self.accumulatedCash = 0
        self.commisionCash = 0
        self.stockBuys = 0
        self.totalBuysPossibilities = 0
        for dataset in self.datas:
            self.indicatorsGARR[dataset._name] = GARR(dataset)
            self.indicatorsBeta[dataset._name] = BetaIndicator(dataset)
            self.orders[dataset._name] = None
        #only even
        self.params.no_of_stocks = self.params.no_of_stocks - self.params.no_of_stocks % 2

    def next(self):

        orders = self.broker.get_orders_open()
        # stock dict for temporary collection holder
        if self.params.inner_logs:
            print (self.data.datetime.date())
        stockDict1, stockDict2 = {}, {}

        for k in self.indicatorsGARR:
            stockDict1[k] = self.indicatorsGARR[k][0]
            stockDict2[k] = self.indicatorsBeta[k][0]

        orderedStocks1 = OrderedDict(sorted(stockDict1.items(), key=lambda x: x[1]))
        orderedStocks2 = OrderedDict(sorted(stockDict2.items(), key=lambda x: x[1]))

        gradedStocks = {}
        # we are grading positions, with wages, biggest one is first to buy
        grader = 1.0
        for key, val in orderedStocks1.items():
            gradedStocks[key] = grader * self.params.GAAR_wage
            grader += 1.0
        grader = 1.0
        for key, val in orderedStocks2.items():
            gradedStocks[key] += grader * self.params.Beta_wage
            grader += 1.0


        # ordered stocks by value
        #least = best
        new_best_stocks = list()
        orderedStocks = OrderedDict(sorted(gradedStocks.items(), key=lambda x: -x[1]), reversed=True)
        counter = 1
        for stock in orderedStocks:
            # get stock
            for dataset in self.datas:
                if dataset._name != stock:
                    continue
                # self.orders[dataset._name] = self.buy(data=dataset, size=10)
                # new stock list to hold
                new_best_stocks.append(dataset._name)

            # we are taking care only for desired amount of stocks
            counter += 1
            if counter > self.params.no_of_stocks:
                break

        stocksToCloseSet = set(self.best_stocks) - set(new_best_stocks)
        currentValueToClose = 0
        for sts in stocksToCloseSet:
            order = self.orders[sts]
            self.close(data=order.params.data)
            currentValueToClose += (order.executed.value + (order.params.data[0] - order.executed.price) * order.executed.size)
            #* (1 - self.broker.getcommissioninfo(order.params.data).params.commission) removed due to COC optimization
            if self.params.inner_logs:
                print("closed: ", sts)
        self.commisionCash += currentValueToClose * 0.005

        stocksToBuySet = set(new_best_stocks) - set(self.best_stocks)
        self.totalBuysPossibilities += self.params.no_of_stocks
        self.stockBuys += len(stocksToBuySet)
        # calculating cash for single stock available
        if(len(stocksToBuySet)) > 0:
            if len(stocksToCloseSet) > 0:
                self.accumulatedCash += self.broker.get_cash()
                cashForSingleStock = (currentValueToClose / len(stocksToBuySet)) * 0.995 + self.broker.get_cash()/len(stocksToBuySet)
                # this code was for testing how to waste(money lying on account) as low money as possible (rem due to COC optimization
                # cashForSingleStock2 = (self.broker.get_value() / self.params.no_of_stocks) * 0.995
                # if cashForSingleStock1 > cashForSingleStock2:
                #     cashForSingleStock = cashForSingleStock1 + self.broker.get_cash()/len(stocksToBuySet)
                # else:
                #     cashForSingleStock = cashForSingleStock2 + self.broker.get_cash()/len(stocksToBuySet)
            else:
                cashForSingleStock = (self.broker.get_value() / self.params.no_of_stocks) * 0.995
            if self.params.inner_logs:
                print (f"cash per stock for {len(stocksToBuySet)} stock(s): ", cashForSingleStock)
            for stb in stocksToBuySet:
                for dataset in self.datas:
                    if dataset._name != stb:
                        continue
                    buySize = cashForSingleStock / dataset[0]
                    self.orders[stb] = self.buy(data=dataset, size=buySize)
                if self.params.inner_logs:
                    print("bought: ", stb)

        self.best_stocks = new_best_stocks



    def notify_trade(self, trade):
        if trade.isclosed & self.p.debug:
            dt = self.data.datetime.date()
            print('6: PnL:                                  {}'.format(round(trade.pnl, 2)))

    # def notify_order(self, order):
    #     print ("########notify_order######## \n", order)
    #
    # def notify_cashvalue(self, cash, value):
    #     print ("^^^^^^^notify_cashvalue^^^^^^^ \n", cash, value)
    #
    # def notify_fund(self, cash, value, fundvalue, shares):
    #     print ("-------notify_fund------- \n", cash, value, fundvalue, shares)

    def stop(self):
        self.params.value = self.broker.getvalue()
        print(self.getdatanames()[0] + ' Value: %.2f' %
              ( self.broker.getvalue()))
        print ("Wasted cash: ", self.accumulatedCash)
        print ("Fees: ", self.commisionCash)
        print ("Total buy possibilities: %d, buy swaps: %d " % (self.totalBuysPossibilities, self.stockBuys))