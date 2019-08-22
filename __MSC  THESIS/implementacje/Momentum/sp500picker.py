import bs4 as bs
import json
import requests

def save_sp500_tickers():
    resp = requests.get('http://en.wikipedia.org/wiki/List_of_S%26P_500_companies')
    soup = bs.BeautifulSoup(resp.text, 'lxml')
    table = soup.find('table', {'class': 'wikitable sortable'})
    tickers = []
    for row in table.findAll('tr')[1:]:
        ticker = row.findAll('td')[0].text
        tickers.append(ticker.rstrip())

    with open('stocks.json', 'a+') as json_file:
        json.dump({'stocks': tickers}, json_file)


    return tickers

if __name__ == '__main__':
    save_sp500_tickers()