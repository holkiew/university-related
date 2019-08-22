import pandas_datareader.data as web
from pandas import datetime, DataFrame, read_csv
import pandas as pd
import datetime
import numpy as np
import math
import scipy.stats as stats
from matplotlib import style
import matplotlib.pyplot as plt
import matplotlib.mlab as mlab

def parser(x):
    return datetime.datetime.strptime(x, '%Y-%m-%d')

class monte_carlo:

    def __init__(self, start, end):
        self.start = start
        self.end = end

    def get_asset(self, filename, nrows):
        #Dates
        start = self.start
        end = self.end

        # prices = web.DataReader(symbol, 'google',start, end)['Close']
        series = read_csv(f'csv/{filename}.csv', header=0,
                          parse_dates=[0], index_col=[0],
                          date_parser=parser, usecols=['Date', 'Close'],
                          nrows=nrows)

        returns = series.pct_change()

        self.returns = returns
        self.prices = series.values


    def monte_carlo_sim(self, num_simulations, predicted_days):
        returns = self.returns
        prices = self.prices

        last_price = prices[-1]

        simulation_df = pd.DataFrame()

        #Create Each Simulation as a Column in df
        for x in range(num_simulations):
            count = 0
            daily_vol = returns.std()

            price_series = []

            #Append Start Value
            price = last_price * (1 + np.random.normal(0, daily_vol))
            price_series.append(price)

            #Series for Preditcted Days
            for i in range(predicted_days):
                # if count == 251:
                #     break
                price = price_series[count] * (1 + np.random.normal(0, daily_vol))
                price_series.append(price)
                count += 1

            simulation_df[x] = price_series
            self.simulation_df = simulation_df
            self.predicted_days = predicted_days

    def brownian_motion(self, num_simulations, predicted_days):
        returns = self.returns
        prices = self.prices

        last_price = prices[-1]

        #Note we are assuming drift here
        simulation_df = pd.DataFrame()

        #Create Each Simulation as a Column in df
        for x in range(num_simulations):

            #Inputs
            count = 0
            avg_daily_ret = returns.mean()
            variance = returns.var()

            daily_vol = returns.std()
            daily_drift = avg_daily_ret - (variance/2)
            drift = daily_drift - 0.5 * daily_vol ** 2

            #Append Start Value
            prices = []

            shock = drift + daily_vol * np.random.normal()
            last_price * math.exp(shock)
            prices.append(last_price)

            for i in range(predicted_days):
                if count == 251:
                    break
                shock = drift + daily_vol * np.random.normal()
                price = prices[count] * math.exp(shock)
                prices.append(price)


                count += 1
            simulation_df[x] = prices
            self.simulation_df = simulation_df
            self.predicted_days = predicted_days

    def line_graph(self):
        prices = self.prices
        predicted_days = self.predicted_days
        simulation_df = self.simulation_df

        last_price = prices[-1]
        fig = plt.figure()
        style.use('bmh')

        # title = "Monte Carlo Simulation: " + str(predicted_days) + " Days"
        plt.plot(simulation_df)
        # fig.suptitle(title,fontsize=18, fontweight='bold')
        plt.xlabel('Day')
        plt.ylabel('Value')
        plt.grid(True,color='grey')
        plt.axhline(y=last_price, color='black', linestyle='-', linewidth=3)
        plt.show()

    def key_stats(self):
        simulation_df = self.simulation_df

        print ('#------------------Simulation Stats------------------#')
        count = 1
        for column in simulation_df:
            print ("Simulation", count, "Mean Price: ", simulation_df[column].mean())
            print ("Simulation", count, "Median Price: ", simulation_df[column].median())
            count += 1

        print ('\n')

        print ('#----------------------Last Price Stats--------------------#')
        print ("Mean Price: ", np.mean(simulation_df.iloc[-1,:]))
        print ("Maximum Price: ",np.max(simulation_df.iloc[-1,:]))
        print ("Minimum Price: ", np.min(simulation_df.iloc[-1,:]))
        print ("Standard Deviation: ",np.std(simulation_df.iloc[-1,:]))

        print ('\n')

        print ('#----------------------Descriptive Stats-------------------#')
        price_array = simulation_df.iloc[-1, :]
        print (price_array.describe())

        print ('\n')

        print ('#--------------Annual Expected Returns for Trials-----------#')
        count = 1
        future_returns = simulation_df.pct_change()
        for column in future_returns:
            print ("Simulation", count, "Annual Expected Return", "{0:.2f}%".format((future_returns[column].mean() * 252) * 100))
            print ("Simulation", count, "Total Return", "{0:.2f}%".format((future_returns[column].iloc[1] / future_returns[column].iloc[-1] - 1) * 100))
            count += 1

        print ('\n')

        #Create Column For Average Daily Price Across All Trials
        simulation_df['Average'] = simulation_df.mean(axis=1)
        ser = simulation_df['Average']

        print ('#----------------------Percentiles--------------------------------#')
        percentile_list = [5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95]
        for per in percentile_list:
            print ("{}th Percentile: ".format(per), np.percentile(price_array, per))

        print ('\n')

        print ('#-----------------Calculate Probabilities-------------------------#')
        print ("Probability price is between 30 and 40: ",  "{0:.2f}%".format((float(len(price_array[(price_array > 30) & (price_array < - 40)])) / float(len(price_array)) * 100)))
        print ("Probability price is &gt; 45: ", "{0:.2f}%".format((float(len(price_array[price_array > 45])) / float(len(price_array)))* 100))

if __name__== "__main__":

    start = datetime.datetime(2017, 1, 3)
    end = datetime.datetime(2017, 10, 4)
    sim = monte_carlo(start, end)


    nsim = 100
    npred = 527
    stock = "BTC-USD"
    train_l = 653
    sim.get_asset(f'{stock}_train', train_l)
    sim.monte_carlo_sim(nsim, npred)
    sim.line_graph()

    #sim.brownian_motion(100, 101)
    #sim.line_graph()
    # sim.key_stats()

    series = read_csv(f'csv/{stock}_pred.csv', header=0,
                       parse_dates=[0], index_col=[0],
                       date_parser=parser)

    for n_sim in range(nsim):
        open_list = sim.simulation_df[n_sim].tolist()
        open_list.insert(0, open_list[0])
        itOpen = iter(open_list)
        itClose = iter(sim.simulation_df[n_sim].tolist())
        itAdjClose = iter(sim.simulation_df[n_sim].tolist())
        series['Close'] = series['Close'].apply(lambda x: next(itClose)[0])
        series['Open'] = series['Open'].apply(lambda x: next(itOpen)[0])
        # series['Adj_Close'] = series['Adj_Close'].apply(lambda x: next(itAdjClose)[0])
        series.to_csv(f'{stock}/{stock}_{n_sim}.csv')

    # list = []
    # for x in range(999999):
    #     list.append(x)
    # it = iter(list)
    # series['Close'] = series['Close'].apply(lambda x: next(it))
    # print(series.head())
    #series.to_csv("dupa")