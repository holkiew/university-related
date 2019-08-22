# AK2P
IEEE mul/div =>1024b

ETAP 0 - opis struktury

Typ zmiennej: unsigned int 

IEEE -> 1b znak +/- | Exponenta (nie pamiętam nazwy, ale to tak leciało, że np. jest 5 bitów i pierwszy bit jest dodatni, cała reszta jest ujemna albo w druga strone np. 11010 = 16 - (8+2) = 10) 'bias' może? | Mantysa
|s|exp|mant|

//TODO Trzeba rozpisać jeszcze o co chodziło z liczba znor/denormalizowana i najlepiej przepisać tu wszystkie przypadki oznaczania wyjątków np. że exponenta 1111....1 to jakiś tam pierdolnik i zdecydować sie jak konwertujemy liczbe, ale wydaje mi sie, że najlogiczniejszym wyjściem jest zwyczajne zapychanie mantysy maksymalnie, żeby trzymać precyzje i dopiero zwiększać exponente wtedy kiedy już dalej się nie da

ETAP 1 - C++
1. Wprowadzenie przez użytkownika eksponenty + mantysy w postaci ilości zajmowanych bitów
	1.1 Dopełnienie mantysy do pełnych 32 bitów.
	1.2 Obliczenie max zakresów liczby
	1.3 Zapisanie w klasie ilości bitów zajętych przez Exp, Mant
2. Wprowadzenie liczby przez użytkownika
	2.1 Sprawdzenie czy mieści się w zakresie max/min
	2.2 Konwersja na bin do Stringa (biblioteki ????)
		2.2.1 Wypełnienie mantysy
		2.2.2 Konwersja exp do ?bias?
		2.2.3 Na koniec znak 
3. Wprowadzenie liczby do tablicy
	3.1 Podzielnie liczby na paczki 32b
	3.2 Zapisanie rozmiaru tablicy
4. Przekazanie danych do ASM
	4.1 Wskaźnik na pierwszy adres tablicy
	4.2 Rozmiar tablicy
	4.3 Ilość bitów na Mant, Exp

ETAP 2 - ASM
5. Mnożenie //TODO może jakieś fajne linki do przypomnienia sobie tych algorytmów :D
	5.1 Algorytm działający na 32bitach
	5.2 Algorytm działający na dowolnej długości słowa
6. Dzielenie
	6.1 Algorytm działający na 32bitach
	6.2 Algorytm działający na dowolnej długości słowa
