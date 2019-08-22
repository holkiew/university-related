import matplotlib.pyplot as plt

if __name__ == '__main__':

    pltx=[469,356,237,144,113, 68, 51, 39, 25, 11]
    plty=[21903,15957,14103,17901, 76602,78120, 17470, 20607, 23486, 18651]
    plt.plot(pltx, plty)
    plt.xlabel('Number of stocks')
    plt.ylabel('Value')
    plt.show()