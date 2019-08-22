from __future__ import (absolute_import, division, print_function,
                        unicode_literals)

import datetime  # For datetime objects
import json

import backtrader as bt
import matplotlib.pyplot as plt

import Momentum.data.data_descriptors as data_descriptors
import Momentum.MomentumStrategyAdj as MomentumStrategyAdj
import Momentum.MomentumStrategyAdjLongOnly as MomentumStrategyAdjLongOnly
import Momentum.MomentumStrategy as MomentumStrategy

params = {
    "customPlot": False,
    "customDumpDataJson": False,
    "btPlot": True
}


def allThreeTest(strategy, chosen_stock=None):
    for key, value in data_descriptors.data.items():
        # Preconfig
        if chosen_stock and key != chosen_stock:
            break
        # Invariables
        cerebro = bt.Cerebro()
        cerebro.addstrategy(strategy)
        # cerebro.broker.setcommission(commission=0.0025)
        # cerebro.broker.setcommission(commission=2.0)
        # cerebro.addsizer(bt.sizers.FixedSize, stake=20)
        cerebro.addsizer(bt.sizers.PercentSizer, percents=50)
        cerebro.broker.setcash(10000.0)

        plot = {'period_x': list(),
                'cash_y': list()
                }
        fd, td = value['fromdate'], value['todate']
        data = bt.feeds.YahooFinanceCSVData(dataname=f"data/csv/{key}.csv",
                                            # fromdate=datetime.datetime(td['y'], td['m'], td['d'])
                                            fromdate=datetime.datetime(2012, 1, 1),
                                            todate=datetime.datetime(td['y'], td['m'], td['d']), reverse=False)
                                            # todate=datetime.datetime(2018, 3, 28), reverse=False)
        cerebro.adddata(data)

        analyzedStats = cerebro.run()
        sharpe_analisys = analyzedStats[0].analyzers.sharpe.get_analysis()
        drawdown_analysis = analyzedStats[0].analyzers.drawdown.get_analysis()
        returns_analisys = analyzedStats[0].analyzers.returns.get_analysis()


        # plot['period_x'].append(period)
        # plot['cash_y'].append(cerebro.broker.getvalue())

        if params['btPlot']:
            cerebro.plot()
            # cerebro.plot(style='candlestick')
    if params['customDumpDataJson']:
        with open(f'data/results/periodD30/{key}D30.json', 'a+') as json_file:
            json.dump(plot, json_file)
    if params['customPlot']:
        plt.plot(plot['period_x'], plot['cash_y'])
        # plt.title(f'{key}: {datetime.datetime.now().strftime("(%H:%M:%S) %Y-%m-%d")}')
        plt.title(f'{key}: {datetime.datetime(2012, 1, 1).strftime("%Y-%m-%d")} - {datetime.datetime(td["y"], td["m"],td["d"]).strftime("%Y-%m-%d")}')
        plt.xlabel('Period')
        plt.ylabel('Value')
        plt.show()


if __name__ == '__main__':
    stocks = [
        "MMM",
              "ABT","ABMD", "ACN",
           #             "ATVI", "ADBE", "AMD", "AAP", "AES", "AMG",
           #    "AFL", "A", "APD", "AKAM", "ALK", "ALB", "ARE", "ALXN", "ALGN", "AGN", "ADS", "LNT",
           #    "ALL", "GOOGL", "GOOG", "MO", "AMZN", "AEE", "AAL", "AEP", "AXP", "AIG", "AMT", "AWK", "AMP",
           #    "ABC", "AME", "AMGN", "APH", "APC", "ADI", "ANSS", "AON", "AOS", "APA", "AIV", "AAPL",
           #    "AMAT", "APTV", "ADM", "AJG", "AIZ", "ATO", "T", "ADSK", "ADP", "AZO", "AVB",
           #    "AVY", "BLL", "BAC", "BK", "BAX", "BBT", "BDX", "BBY", "BIIB", "BLK", "HRB",
           #    "BA", "BKNG", "BWA", "BXP", "BSX", "BMY", "AVGO", "BR", "CHRW", "COG", "CDNS", "CPB"
           #       ,
           #    "COF", "CPRI", "CAH",
           #    "KMX", "CCL", "CAT", "CBOE", "CBRE", "CBS", "CE", "CELG", "CNC", "CNP", "CTL",
           #    "CERN", "CF", "SCHW", "CHTR", "CVX", "CMG", "CB", "CHD", "CI", "XEC", "CINF", "CTAS",
           #    "CSCO", "C","CFG","CTXS","CLX", "CME", "CMS", "KO", "CTSH", "CL", "CMCSA", "CMA",
           #    "CAG", "CXO", "COP", "ED", "STZ", "COO", "CPRT", "GLW", "COST", "COTY", "CCI", "CSX",
           #    "CMI", "CVS", "DHI", "DHR", "DRI", "DVA", "DE", "DAL", "XRAY", "DVN", "FANG", "DLR", "DFS",
           #     "DISCA", "DISCK", "DISH", "DG", "DLTR", "D", "DOV", "DTE", "DRE", "DUK",
           #    "ETFC", "EMN", "ETN", "EBAY", "ECL", "EIX", "EW", "EA", "EMR", "ETR", "EOG", "EFX", "EQIX", "EQR",
           #    "ESS", "EL", "ES", "RE", "EXC", "EXPE", "EXPD", "EXR", "XOM", "FFIV", "FB", "FAST", "FRT"
           #       ,"FDX", "FIS", "FITB", "FE", "FRC", "FISV", "FLT", "FLIR", "FLS", "FLR", "FMC", "FL", "F", "FTNT",
           #    "FTV", "FBHS", "FOXA", "FOX", "BEN", "FCX", "GPS", "GRMN", "IT", "GD", "GE", "GIS", "GM", "GPC",
           #    "GILD", "GPN", "GS", "GWW", "HAL", "HBI", "HOG", "HRS", "HIG", "HAS", "HCA", "HCP", "HP", "HSIC",
           #    "HSY", "HES", "HPE", "HLT", "HFC", "HOLX", "HD", "HON", "HRL", "HST", "HPQ", "HUM", "HBAN", "HII",
           # "IDXX", "INFO", "ITW", "ILMN", "IR", "INTC", "ICE", "IBM", "INCY", "IP", "IPG", "IFF", "INTU", "ISRG",
           #    "IVZ", "IPGP", "IQV", "IRM", "JKHY", "JEC", "JBHT", "JEF", "SJM", "JNJ", "JCI", "JPM", "JNPR",
           #    "KSU", "K", "KEY", "KMB", "KIM", "KMI", "KLAC", "KSS", "KHC", "KR", "LB", "LLL", "LH",
           #    "LRCX", "LW", "LEG", "LEN", "LLY", "LNC", "LKQ", "LMT", "L", "LOW", "LYB", "MTB", "MAC",
           #      "M", "MRO", "MPC", "MAR", "MMC", "MLM", "MAS", "MA", "MAT", "MKC", "MXIM", "MCD", "MCK", "MDT",
           #      "MRK", "MET", "MTD", "MGM", "MCHP", "MU", "MSFT", "MAA", "MHK", "TAP", "MDLZ", "MNST", "MCO",
           #    "MS", "MOS", "MSI", "MSCI", "MYL", "NDAQ", "NOV", "NKTR", "NTAP", "NFLX", "NWL", "NEM", "NWSA",
           #    "NWS", "NEE", "NLSN", "NKE", "NI", "NBL", "JWN", "NSC", "NTRS", "NOC", "NCLH", "NRG", "NUE",
           #    "NVDA", "ORLY", "OXY", "OMC", "OKE", "ORCL", "PCAR", "PKG", "PH", "PAYX", "PYPL", "PNR", "PBCT",
           #   "PEP", "PKI", "PRGO", "PFE", "PM", "PSX", "PNW", "PXD", "PNC", "RL", "PPG", "PPL", "PFG", "PG",
           #    "PGR", "PLD", "PRU", "PEG", "PSA", "PHM", "PVH", "QRVO", "PWR", "QCOM", "DGX", "RJF", "RTN", "O",
           #    "RHT", "REG", "REGN", "RF", "RSG", "RMD", "RHI", "ROK", "ROL", "ROP", "ROST", "RCL", "CRM", "SBAC",
           #    "SLB", "STX", "SEE", "SRE", "SHW", "SPG", "SWKS", "SLG", "SNA", "SO", "LUV", "SPGI", "SWK", "SBUX",
           #    "STT", "SYK", "STI", "SIVB", "SYMC", "SYF", "SNPS", "SYY", "TROW", "TTWO", "TGT", "TEL", "FTI",
           #    "TFX", "TXN", "TXT", "TMO", "TIF", "TWTR", "TJX", "TMK", "TSS", "TSCO", "TDG", "TRV", "TRIP", "TSN",
           #    "UDR", "ULTA", "USB", "UAA", "UA", "UNP", "UAL", "UNH", "UPS", "URI", "UTX", "UHS", "UNM", "VFC", "VLO",
           #    "VAR", "VTR", "VRSN", "VRSK", "VZ", "VRTX", "VIAB", "V", "VNO", "VMC", "WAB", "WMT", "WBA", "DIS", "WM",
           #    "WAT", "WEC", "WCG", "WFC", "WELL", "WDC", "WU", "WRK", "WY", "WHR", "WMB", "WLTW", "WYNN", "XEL", "XRX",
           #    "XLNX", "XYL", "YUM", "ZBH", "ZION", "ZTS"
              ]
    #allThreeTest(MStrategy.MStrategy, chosen_stock="FB")
    #allThreeTest(MStrategy.MStrategy)
    stocks = ["TSLA", "FB", "JPM", "CLUB", "TSLA", "TUWOY"]



    cerebro = bt.Cerebro()
    # cerebro.addstrategy(MomentumStrategyAdj.MomentumStrategyAdj,
    #                     no_of_stocks=2,
    #                     GAAR_wage = 5.0,
    #                     Beta_wage = 0.2,
    #                     inner_logs=False)
    cerebro.addstrategy(MomentumStrategy.MomentumStrategy,
                        no_of_stocks=1,
                        inner_logs=False)
    # cerebro.optstrategy(
    #     MomentumStrategyAdjLongOnly.MomentumStrategyAdj,
    #     no_of_stocks=[26,30],
    #     GAAR_wage = [0.2,0.5, 1.0, 1,5],
    #     Beta_wage = [0.2,0.5, 1.0, 1,5],
    #     inner_logs=False
    # )
    cerebro.broker.setcommission(commission=0.0025)
    # cerebro.broker.setcommission(commission=2.0)
    # cerebro.addsizer(bt.sizers.FixedSize, stake=20)
    # cerebro.addsizer(bt.sizers.PercentSizer, percents=50)
    cerebro.broker.setcash(10000.0)
    cerebro.broker.set_coc(True)
    # cerebro.broker.set_coo(True)


    cerebro.addanalyzer(bt.analyzers.SharpeRatio, _name='sharpe')
    cerebro.addanalyzer(bt.analyzers.DrawDown, _name='drawdown')
    cerebro.addanalyzer(bt.analyzers.Returns, _name='returns')


    # Handy dictionary for the argument timeframe conversion
    tframes = dict(
        daily=bt.TimeFrame.Days,
        weekly=bt.TimeFrame.Weeks,
        monthly=bt.TimeFrame.Months)

    # Add the resample data instead of the original

    for stockname in stocks:
        load = True
        with open(f"data/csv/{stockname}.csv", "r") as fp:
            content = fp.read()
            if content.find("2013-01-01") >= 0 and content.find("NaN") < 0:
                    load = True
            else:
                print (f"not loaded {stockname}")

        if load:
            data = bt.feeds.YahooFinanceCSVData(dataname=f"data/csv/{stockname}.csv",
                                                fromdate=datetime.datetime(2016, 10, 16),
                                                todate=datetime.datetime(2019, 3, 29),
                                                reversed=False)
            # cerebro.adddata(data)
        # data = bt.feeds.YahooFinanceData(dataname=f"{stockname}",
        #                                     fromdate=datetime.datetime(2012, 5, 18),
        #                                     todate=datetime.datetime(2019, 3, 29),
        #                                     reversed=False)
            cerebro.resampledata(data,
                                 timeframe=tframes["monthly"],
                                 compression=1)


    strats = cerebro.run(maxcpus=1)
    sharpe_analisys = strats[0].analyzers.sharpe.get_analysis()
    drawdown_analysis = strats[0].analyzers.drawdown.get_analysis()
    returns_analisys = strats[0].analyzers.returns.get_analysis()

    print ("Value: %.2f" % (cerebro.broker.get_value()))
    print (f'sharpeRatio: %.2f' % (sharpe_analisys["sharperatio"]))
    print (f'maxDrawdown: %.2f ' % (drawdown_analysis["max"]["drawdown"]))
    print (f'Annualized return: %.2f' % (returns_analisys["rnorm"]*100))
    print (f'total compound return: %.2f' % (returns_analisys["rtot"]*100))
    cerebro.plot()

    allStats = dict()

    for stratrun in strats:
        for strat in stratrun:
            args = strat.p._getkwargs()
            # print(args)
            allStats[args['value']] = args
        # args = stratrun.p._getkwargs()
        # allStats[args['value']] = args
    top = sorted(allStats.keys(), reverse=True)
    for x in top:
        print (allStats[x])
    print ("Stock len: ", len(stocks))

    # f = open(f'data/results/ADJ_LONG2_range30_len{len(stocks)}.txt',"w+")
    # for x in range(len(top)):
    #     f.write(f'Value : %.2f | {allStats[top[x]]}\n' % (allStats[top[x]]["value"]))
    # f.write (f'Average: {sum(top) / len(top)}')
