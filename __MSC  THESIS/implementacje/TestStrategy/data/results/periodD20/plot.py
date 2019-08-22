from __future__ import (absolute_import, division, print_function,
                        unicode_literals)
import datetime
import json
import matplotlib.pyplot as plt

import TestStrategy.data.data_descriptors as data_descriptors

if __name__ == '__main__':
    for key, value in data_descriptors.data.items():
        fd, td = value['fromdate'], value['todate']
        with open(f'{key}.json', 'r') as json_file:
            plot = json.load(json_file);
            plt.plot(plot['period_x'], plot['cash_y'])
            plt.title(f'{key}: {datetime.datetime(2012, 1, 1).strftime("%Y-%m-%d")} - {datetime.datetime(td["y"], td["m"], td["d"]).strftime("%Y-%m-%d")}')
            plt.xlabel('Period')
            plt.ylabel('Money [$]')
            plt.show()
