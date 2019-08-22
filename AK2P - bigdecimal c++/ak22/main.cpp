#include <iostream>
#include <math.h>
#include <bitset>
#include <string>
#include <cstdlib>
#include <sstream>
#include <vector>
#include <climits>
#include <cstring>
#include <stdio.h>
#include <windows.h>
using namespace std;
#define MAX_LICZBA 999999999
int const  MAX_LICZBA_BIT_LENGTH = 32;
int const  MAX_LICZBA_STRING_LENGTH = 10;
///----------------------------------------------------
string print_vectorHex(vector<unsigned int>);
string print_vectorDec(vector<unsigned int>);
string print_vectorBin(vector<unsigned int>);
void addArrays(vector<unsigned int> &, vector<unsigned int> &);
void substractArrays(vector<unsigned int> &, vector<unsigned int> &);
bool isSubstractable(vector<unsigned int>, vector<unsigned int> );
int changeHexDigitToDec(char s);
void shiftArrayLeft(vector <unsigned int> &v, bool bit);
void shiftArrayRight(vector <unsigned int> &v);
bool readBit(unsigned int number, int position);

///----------------------------------------------------
bool logiOgolne = false;
bool logiSzczegolowe = false;
///----------------------------------------------------
uint64_t rdtsc(){
    unsigned int lo,hi;
    __asm__ __volatile__ ("rdtsc" : "=a" (lo), "=d" (hi));
    return ((uint64_t)hi << 32) | lo;
}
///----------------------------------------------------
class numberIEEE{
    string numberString;
    string exponentString;

public:
    char numberSign;
    char exponentSign;
    vector<unsigned int> number;
    vector<unsigned int> exponent;
    numberIEEE (){};
    numberIEEE (char number_sign, string number_string,char exponent_sign, string exponent_string ) :
        numberString(number_string),
        exponentString(exponent_string),
        numberSign(number_sign),
        exponentSign(exponent_sign)
        {
            string bufforStr=count_Exponent_And_Normalize(numberString);    ///wylapanie i skasowanie przecinka w liczbie, ilosc przecinkow zapisana w DECIMAL
            char sign = bufforStr.at(0);
            bufforStr=bufforStr.substr(1,bufforStr.size()-1);
            vector <unsigned int> bufforUint;                   ///kontener pod dodatkowa mantyse wynikajaca z wczytanego przecinka

            convertToBinArrayFromHEX(numberString,number);
            convertToBinArrayFromHEX(exponentString,exponent);
            if(exponent.size()==1 && exponent.at(0)==0)
                exponentSign='+';
            if(bufforStr!="0")
            {
                convertToBinArrayFromDEC(bufforStr, bufforUint);        ///max ilosc przesuniec przecinka = MAX_INT32/4

            }
            else
                bufforUint.push_back(0);
            for(int i=0; i<2;i++)                                   ///mnozenie binarne * 4 (100) -> shift o 2 zera
                {
                    shiftArrayLeft(bufforUint,false);
                    shiftArrayLeft(exponent,false);
                }

            ///dodajemy mantyse ktora przechwycilismy przy wczytywaniu i lapaniu przecinka
            if(int(exponentSign+sign)==int('+'+'-'))
            {
                if(isSubstractable(exponent,bufforUint))    ///patrzymy ktora z nich jest wieksza if(n1>=n2)
                {
                     substractArrays(exponent, bufforUint);
                     if(exponentSign=='+')
                        exponentSign='+';
                     else
                        exponentSign='-';
                }
                else
                 {
                     substractArrays(bufforUint,exponent);
                     if(exponentSign=='+')
                        exponentSign='-';
                     else
                        exponentSign='+';
                 }
            }
            else
            {
                if(int(exponentSign+sign)==int('-'+'-'))
                {
                    addArrays(exponent,bufforUint);
                    exponentSign='-';
                }
                else
                {
                    addArrays(exponent,bufforUint);
                    exponentSign='+';
                }
            }
        if(exponent.at(0)==0 && exponentString.size()==1)
                exponentSign='+';
        }
    string count_Exponent_And_Normalize(string &numberString)
    {
        stringstream ss;
        size_t found;
        string buffor=numberString.substr(0,2);
        if(!(buffor=="0," || buffor=="0."))          ///jesli jest to liczba np 12342342,23412391323
        {
            buffor="+";
            found = numberString.find('.');
            if (found != string::npos)
            {
               numberString.erase(numberString.begin()+found);
            }
            else
            {
               found = numberString.find(',');
               if(found != string::npos)
               {
                   numberString.erase(numberString.begin()+found);
               }
               else
               {
                   found = numberString.size();
               }
            }
        }
        else                                                       /// jesli jest to liczba np 0,000013423123
        {
            buffor="-";
            found = numberString.find_first_of("123456789abcedfABCDEF");
            numberString=numberString.substr(found,numberString.size()-found);
        }
        ss << found-1;
        buffor.append(ss.str());
        return buffor;
    }
    int convertToBinArrayFromHEX(string toConvert, vector<unsigned int> &number)
    {
        number.push_back(0);
        for(int i =0; i<=toConvert.size()-1;i++)
        {
            for(int j =0; j<4;j++)
                shiftArrayLeft(number,false);
            number.at(0)+=changeHexDigitToDec(toConvert.at(i));
        }
    }
    int convertToBinArrayFromDEC(string toConvert, vector<unsigned int> &number){
        vector<unsigned int> buffor;
        char * digits = new char [toConvert.length()+1];
        strcpy (digits, toConvert.c_str());
        int digit;
        for(int i=0; i<toConvert.length();i++)
        {

            if(i==0)
            {
                 number.push_back((int)digits[i]-'0');
            }
            else
            {   ///dodawanie
                digit = (int)digits[i]-'0';
                for(int j=0; j<number.size();j++)
                {
                    if(__builtin_uadd_overflow(number.at(j), digit,&number.at(j))) //fpermissive cos moze byc nie tak
                    {
                        digit=1;
                        if(j+1==number.size())///dodawanie OF
                        {
                              number.push_back(1);
                              break;
                        }
                        else
                            continue;
                    }
                    else
                    {
                        break;
                    }
                }
            }
            vector<unsigned int> buffor;
            unsigned int lower,higher;
            ///mnozenie
            for(int j=0; j<number.size() && i<toConvert.length()-1; j++) ///ostatni znak liczby nie jest mnozony, tylko dodawany
            {
                asm ("movl %2, %%eax;"
                "mull %3;"
                "movl %%eax, %0;"
                "movl %%edx, %1;"
                : "=r" ( lower ), "=r" ( higher ) /* output %0,1*/
                : "r" ( number.at(j) ), "r" ( 10 ) /* input %2,3*/
                :"eax","edx"/* clobbered register */
                );
                buffor.push_back(higher);///przesuniecie wzgledem liczby o j+1 (numery tablic do dodania)
                number.at(j)=lower;
            }
            if(number.size()==buffor.size())
                number.push_back(0);
            ///sumowanie OF powstalych przy mnozeniu
            for(int j=0; j<buffor.size(); j++)
            {
                digit=buffor.at(j);
                for(int z=j+1; z<number.size(); z++)
                {
                    if(__builtin_uadd_overflow(number.at(z), digit,&number.at(z))) //-fpermissive cos moze byc nie tak
                    {
                        digit=1;
                        if(z==number.size())///dodawanie OF
                        {
                            number.push_back(1);
                            break;
                        }
                        else
                            continue;
                    }
                    else
                    {
                            break;
                    }
                }
            }
            if(number.back()==0)
                number.pop_back();
        }
        if(logiSzczegolowe)
        {
           for(vector<unsigned int>::iterator it = number.begin(); it!=number.end(); it++)
            {
                cout<<"DEC: "<<*it<<" | ";
            }
            ///wyswietl
            cout<<"Finalne liczba BIN po konwersji: ";
            for(int i=number.size()-1; i>=0 ; i--)
            {
                cout<<bitset<32>(number[i]);
            }
        }

    }
    ///przesuwa kropke i oblicza exponente

    string toStringDEC(){
        string text;
        vector <unsigned int> exponent_buffor = exponent;
        vector <unsigned int> number_buffor = number;
        int rest = exponent_buffor.at(0) % 4 ;                  ///do mantysy
        for(int i=0;i<2;i++)
            shiftArrayRight(exponent_buffor);                  ///dzielenie przez 4, reszte mamy z modul
        for(int i=0;i<rest;i++)                         ///liczymy modulo z 4 (np 2^31= 16^7 + 2^3) wiec koncowy wynik trzeba 2^3 ->8 ->1000
            shiftArrayLeft(number_buffor,false);

        string numberBIN = print_vectorBin(number_buffor);//print_vectorHex(number);
        char * digits = new char [numberBIN.length()+1];
        strcpy (digits, numberBIN.c_str());
        int digit;
        vector<unsigned int> numberDEC;
        for(int i=0; i<numberBIN.length();i++)
        {
            if(i==0)
            {
                numberDEC.push_back(digits[i]-'0');
            }
            else
            {   ///dodawanie
                digit = digits[i]-'0';
                for(int j=0; j<numberDEC.size();j++)
                {
                    numberDEC.at(j)+=digit;
                    if(numberDEC.at(j)>MAX_LICZBA)
                    {
                        numberDEC.at(j)-=MAX_LICZBA;
                        digit=1;
                        if(j+1==numberDEC.size())///dodawanie OF
                        {
                              numberDEC.push_back(1);
                              break;
                        }
                        else
                            continue;
                    }
                    else
                    {
                        break;
                    }
                }
            }
            vector<unsigned int> buffor;
            unsigned int lower,higher;
            uint64_t buf2;
            ///mnozenie
            for(int j=0; j<numberDEC.size() && i<numberBIN.length()-1; j++) ///ostatni znak liczby nie jest mnozony, tylko dodawany
            {
                string txt;
                stringstream ss;
                asm ("movl %2, %%eax;"
                "mull %3;"
                "movl %%eax, %0;"
                "movl %%edx, %1;"
                : "=r" ( lower ), "=r" ( higher ) /* output %0,1*/
                : "r" ( numberDEC.at(j) ), "r" ( 2 ) /* input %2,3*/
                :"eax","edx"/* clobbered register */
                );
                buf2=(higher << 32)+lower;
                //cout << "buffor mnozenia : "<< buf2<<endl;
                ss << buf2;
                ss >> text;
                if(text.size()>9)
                {
                    buffor.push_back(stoul(text.substr( 0, text.size() % 9 )));         ///higher
                    numberDEC.at(j)=stoul((text.substr(text.size() % 9,9)));            ///lower
                    //cout<<"LOWER PO : " << numberDEC.at(j)<<endl;
                    //cout <<"HIGHER PO : "<< stoul(text.substr( 0, text.size() % 9 ))<<endl;
                }
                else{
                    numberDEC.at(j)=stoul(text);
                    buffor.push_back(0);
                    //cout<<"LOWER PO : " << stoul(text)<<endl;
                }
            }
            if(numberDEC.size()==buffor.size())
                numberDEC.push_back(0);
            ///sumowanie OF powstalych przy mnozeniu
            for(int j=0; j<buffor.size(); j++)
            {
                digit=buffor.at(j);
                for(int z=j+1; z<numberDEC.size(); z++)
                {
                    numberDEC.at(z)+=digit;
                    if(numberDEC.at(z)>MAX_LICZBA)
                    {
                        numberDEC.at(z)-=MAX_LICZBA;
                        digit=1;
                        if(z==numberDEC.size())///dodawanie OF
                        {
                            numberDEC.push_back(1);
                            break;
                        }
                        else
                            continue;
                    }
                    else
                    {
                            break;
                    }
                }
            }
            if(numberDEC.back()==0)
                numberDEC.pop_back();
        }
        return "|"+string(1,numberSign)+"|"+print_vectorDec(numberDEC)+"10^"+"|"+string(1,exponentSign)+"|"+print_vectorDec(exponent_buffor);
    }

    string toStringHEX(){

        string text,text_buffor;
        vector <unsigned int> exponent_buffor = exponent;
        vector <unsigned int> number_buffor = number;
        int end;
        int rest = exponent_buffor.at(0) % 4 ;                  ///do mantysy
        for(int i=0;i<2;i++)
            shiftArrayRight(exponent_buffor);                  ///dzielenie przez 4, reszte mamy z modulo
        for(int i=0;i<rest;i++)                         ///liczymy modulo z 4 (np 2^31= 16^7 + 2^3) wiec koncowy wynik trzeba 2^3 ->8 ->1000
            shiftArrayLeft(number_buffor,false);
        if(number_buffor.at(0)==0)
            return "0";

        if(numberSign=='-')
            text+="-";
        text_buffor=print_vectorHex(number_buffor);
        end = text_buffor.find_first_of("123456789abcdefABCDEF");
        text_buffor.erase(0, end);
        text_buffor.insert(1,",");
        if(text_buffor.size()==2)
            text_buffor.pop_back();;

        text+=text_buffor;
        if(!(exponent_buffor.size()==1 && exponent_buffor.at(0)==0))
            text+=" * 10^";
        if(exponentSign=='-')
            text+="-";
        text_buffor=print_vectorHex(exponent_buffor);

        end = text_buffor.find_first_of("123456789abcdefABCDEF");
        text_buffor.erase(0, end);
        text+=text_buffor;
        return text;
    }
};

int changeHexDigitToDec(char s){
    if(s>='0' && s<='9')
        return (s-'0');
    else if (s>='A' && s<='F')
        return (s-'A'+10);
    else
        return (s-'a'+10);
    return 0;
}

///counter - zeby wiedziec gdzie OF wrzucic, tablica[0] - najmniejsze 32b, counter - od 1 elementu
///Dodaje do siebie odpowiednie rzedy mnozenia, szuka OF i przerzuca go do nastepnego rzedu
void sum_up_result(int counter, unsigned int resultNumberArray[], vector <unsigned int> &result)
{
    if(counter == 0)
    {
        result.push_back(resultNumberArray[0]);
        result.push_back(resultNumberArray[1]);
    }
    else
    {
        result.push_back(resultNumberArray[1]);
        if(__builtin_uadd_overflow(result.at(counter), resultNumberArray[0],&result.at(counter)))
        {
            result.at(counter+1)+=1;
        }
    }
}
///dodaje do siebie rzedy wynikajae z mnozenia
///resultRow to zawsze pierwszy wiersz (counter-> nr rzedu dodawanego), counter - przesuniecie rzedu o counter elementow w lewo
void sum_up_number_rows(vector<unsigned int> &resultRow, vector<unsigned int> addRow,int counter)
{
    unsigned int digit;
    for(int i = 0;i<addRow.size();i++)
    {
        if(resultRow.size() > i+counter) ///czy istnieje element w resultRow do ktorego chcemy dodac
        {
                digit = addRow.at(i);
                for(int j=counter+i; j<resultRow.size();j++)
                {
                    if(__builtin_uadd_overflow(resultRow.at(j), digit, &resultRow.at(j))) //fpermissive cos moze byc nie tak
                    {
                        digit=1;
                        if(j+1==resultRow.size())///dodawanie OF
                        {
                              resultRow.push_back(1);
                              break;
                        }
                        else
                            continue;
                    }
                    else
                    {
                        break;
                    }
                }

        }
        else            ///jesli nie to tworzymy element do ktorego dodamy
            resultRow.push_back(addRow.at(i));
    }
}
void addArrays(vector<unsigned int> &number, vector<unsigned int> &adder)
{
    unsigned int digit;
    for(int i = 0; i<adder.size();i++)
    {
        digit = adder.at(i);
        if(i+1==number.size())
            number.push_back(0);
        for(int j = i; j<number.size();j++)
        {
            if(__builtin_uadd_overflow(number.at(j), digit ,&number.at(j)))
            {
                digit = 1;
                if(j+1==number.size())///dodawanie OF
                {
                    number.push_back(1);
                    break;
                }
                else
                    continue;
            }
            else
                break;
        }
    }
    ///wyrzucamy z number wszystkie zerowe tablice, ostatni element nie jest brany pod uwage bo cos musi zostac w tablicy
    for(int i=number.size()-1;i>0;i--)
    {
        if(number.at(i)>0)
            break;
        else
            number.pop_back();
    }

}
void substractArrays(vector<unsigned int> &number, vector<unsigned int> &subtractor)
{
    unsigned int digit;
    for(int i = 0; i<subtractor.size();i++)
    {
        digit = subtractor.at(i);
        for(int j = i; j<number.size();j++)
        {
            if(__builtin_usub_overflow(number.at(j), digit ,&number.at(j)))
            {
                digit = 1;                              ///'pozyczka' jesli wystapilo underflow
                number.at(j)+=0xFFFFFFFF;               ///dodajemy 'pozyczke'
                number.at(j)+=1;
                continue;
            }
            else
                break;
        }
    }
    ///wyrzucamy z number wszystkie zerowe tablice, ostatni element nie jest brany pod uwage bo cos musi zostac w tablicy
    for(int i=number.size()-1;i>0;i--)
    {
        if(number.at(i)>0)
            break;
        else
            number.pop_back();
    }
}
///czy liczba (w talibcach) jest wieksza od odejmnika
bool isSubstractable(vector<unsigned int> number, vector<unsigned int> subtractor )
{
    if(number.size()<subtractor.size())
        return false;
    if(number.size()>subtractor.size())
        return true;
    if( number.at(0)==0 && number.size()==1)
        return false;


    if(logiSzczegolowe)
    {
        for(int i=number.size()-1;i>=0;i--)
            cout<<"number: "<<bitset<32>(number.at(i))<<endl;
        for(int i=subtractor.size()-1;i>=0;i--)
            cout<<"subtractor: "<<bitset<32>(subtractor.at(i))<<endl;
    }

    for(int i=number.size()-1;i>=0;i--)
    {
      if ( number.at(i)>subtractor.at(i))
        return true;
      else
        if( number.at(i)==subtractor.at(i))
            continue;
        else
            return false;
    }
    return true;

}
string print_vectorDec(vector <unsigned int> v){
    string buffor;
    for(int i=v.size()-1; i>=0;i--)
    {
        stringstream ss;
        string text;
        ss << v.at(i);
        ss >> text;
        if(text.length() % 9 !=0)
        {
            text = string(9-(text.length() % 9), '0')+text;
        }
        buffor +=text;
    }
    return buffor;
}

string print_vectorHex(vector <unsigned int> v){
    string buffor;
    for(int i=v.size()-1; i>=0;i--)
    {
        stringstream ss;
        string text;
        ss <<hex<<v.at(i);
        ss >> text;
        if(text.length() % 8 !=0)
        {
            text = string(8-(text.length() % 8), '0')+text;
        }
        buffor +=text;
    }
    return buffor;
}

string print_vectorBin(vector <unsigned int> v){
    string buffor;
    for(int i=v.size()-1; i>=0;i--)
    {
        buffor +=bitset<32>(v.at(i)).to_string();
    }
    return buffor;
}

numberIEEE multiply(numberIEEE n1, numberIEEE n2)
{
    unsigned int resultNumberArray[2],l1,l2;              ///wynik mnozenia buffora, podzielony na dwie tablice 32b
    vector <unsigned int> result;                   ///tablica z wynnikiem JEDNEGO rzedu ( 32b|32b|32b|32b|32b....|32b)
    vector <vector<unsigned int>> resultsArray;    ///wektor z wynynikami mnozen WSZYSTKICH rzedow
    vector <unsigned int> finalResult;             ///wektor z koncowymm wynikiem
    numberIEEE number;

    if((n1.number.size()==1 && n1.number.at(0)==0) || (n2.number.size()==1 && n2.number.at(0)==0))
    {
        numberIEEE n('+',"0",'+',"0");
        return n;
    }

    ///wyliczenie dlugosci bitow zeby pozniej sprawdzyc czy po mnozeniu przesunal sie przecinek
    unsigned int numLength = n1.number.size()-1 + n2.number.size()-1;
    unsigned int bitsLengthLastContainer=0;
    for(int i =31; i>=0;i--)
    {
        if(readBit(n1.number.at(n1.number.size()-1), i))
        {
            bitsLengthLastContainer+=i+1;                       ///szukamy pierwszego wystapienia '1' w pierwszym kontenerze (najwyzsze bity)
            break;
        }
    }
    for(int i =31; i>=0;i--)
    {
        if(readBit(n2.number.at(n2.number.size()-1), i))
        {
            bitsLengthLastContainer+=i+1;                       ///szukamy pierwszego wystapienia '1' w pierwszym kontenerze (najwyzsze bity)
            break;                                              /// +1 bo ilosc bitow 1-32, ale funkcja przyjmuje zakres 0-31
        }
    }
    if(bitsLengthLastContainer / 32 > 0)
    {
        bitsLengthLastContainer = bitsLengthLastContainer % 32;
        numLength++;
    }



    for(int j = 0; j<n2.number.size();j++)
    {
        if(logiSzczegolowe || logiOgolne)
            cout<<endl<< "ROW : "<<j<<endl;
        vector <unsigned int> result;
        for(int i = 0; i<n1.number.size();i++)
        {
            l1=n1.number[i]; l2=n2.number[j];
            asm ("movl %2, %%eax;"
            "mull %3;"
            "movl %%eax, %0;"
            "movl %%edx, %1;"
            : "=r" ( resultNumberArray[0] ), "=r" (  resultNumberArray[1] ) /* output %0,1 [1] higher , [0] lower */
            : "r" ( l1 ), "r" ( l2 ) /* input %2,3*/
            :"eax","edx"/* clobbered register */
            );

            if(logiSzczegolowe)
                cout<<endl<<hex<<"Mnozenie bufforow(2x32b)"<<endl<<n1.number[i]<<"*"<<n2.number[j]<<"="<<resultNumberArray[1]<<" | "<<resultNumberArray[0]<<endl;
            sum_up_result(i, resultNumberArray, result);
        }
        if(result.back()==0)
            result.pop_back();
        resultsArray.push_back(result);                     ///wrzucamy jeden rzad do dodawania
        if(logiOgolne)
        {
            cout<<"----------------"<<endl<<"SUMA BUFFOROW"<<endl<<"----------------"<<endl;
             for(int i = 0; i<result.size();i++)
                cout<<"Wynik index: "<<i<<"-> "<<result.at(i)<<endl;
        }

    }
    if(logiOgolne)
    {
    cout <<endl<< "FINAL RESULT OF MULTIPLYING : -> "<<endl;
    cout << print_vectorHex(finalResult);
    }
    for(int i=0;i<resultsArray.size();i++)
        sum_up_number_rows(finalResult,resultsArray.at(i), i);

    numberIEEE newNumber;
    newNumber.number=finalResult;
    int bitSizeOfResult=31;
    for(; bitSizeOfResult>=0;bitSizeOfResult--)
    {
        if(readBit( newNumber.number.at( newNumber.number.size()-1), bitSizeOfResult))
        {
            bitSizeOfResult++;
            break;
        }
    }
    if(numLength!=finalResult.size()-1)
    cout<<"numLength:"<<numLength<<"ERR(((!=))) bitsLengthLastContainer: "<<bitsLengthLastContainer<<endl;

    ///znak liczby
    if(int(n1.numberSign+n2.numberSign)==int('+'+'-'))
            newNumber.numberSign='-';
        else if(int(n1.numberSign+n2.numberSign)==int('-'+'-'))
            newNumber.numberSign='+';
            else
                newNumber.numberSign='+';
    ///znak exponenty
    newNumber.exponent=n1.exponent;


    if(int(n1.exponentSign+n2.exponentSign)==int('+'+'-'))
    {
         if(isSubstractable(n1.exponent,n2.exponent))    ///patrzymy ktora z nich jest wieksza if(n1>=n2)
         {
             substractArrays(newNumber.exponent, n2.exponent);
             if(n1.exponentSign=='+')
                newNumber.exponentSign='+';
             else
                newNumber.exponentSign='-';
         }
         else
         {
             newNumber.exponent=n2.exponent;
             substractArrays(newNumber.exponent, n1.exponent);
             if(n2.exponentSign=='+')
                newNumber.exponentSign='+';
             else
                newNumber.exponentSign='-';
         }

    }
    else if(int(n1.exponentSign+n2.exponentSign)==int('-'+'-'))
        {
            addArrays(newNumber.exponent,n2.exponent);
            newNumber.exponentSign='-';
        }
        else
        {
            addArrays(newNumber.exponent,n2.exponent);
            newNumber.exponentSign='+';
        }

    if(bitSizeOfResult>bitsLengthLastContainer)
    {
        vector<unsigned int> b;
        b.push_back(1);
        if(newNumber.numberSign=='+')
            addArrays(newNumber.exponent, b);
        else
            substractArrays(newNumber.exponent, b);

    }
    if(newNumber.exponent.at(0)==0 && newNumber.exponent.size()==1)
        newNumber.exponentSign='+';
    return newNumber;
}
///czyta konkretny bit z danego wektora 0-31
bool readBit(unsigned int number, int position) //ranges 0-31
{
    unsigned int bitMask = 1 << position;
    if((number & bitMask)>0)
        return true;
    else
        return false;
}

///funkcja ktora shiftuje z przeniesieniem bity w danej tablicy, moze dodac bit na koniec
void shiftArrayLeft(vector <unsigned int> &v, bool bit)
{
    bool buffor31=false;
    bool buffor0=bit;
    int i = 0;
    for( i=0; i < v.size(); i++)
    {
        if(v.at(i)>=0x80000000)          /// czy zawiera ostatni bit w danym slowie
            buffor31 = true;
        else
            buffor31 = false;
        v.at(i)=v.at(i)<<1;
        if(buffor0)
            v.at(i)+=1;
        buffor0=buffor31;
    }
    if(i==v.size() && buffor0)           ///jesli trzeba przesunac na kolejna pozycje, a brak miesjca
            v.push_back(1);

}
void shiftArrayRight(vector <unsigned int> &v)
{
    bool buffor31=false;
    bool buffor0=false;
    for( int i=v.size()-1; i >=0; i--)
    {
        if((v.at(i) & 0x00000001) > 0)          /// czy zawiera ostatni bit w danym slowie
            buffor0 = true;
        else
            buffor0 = false;
        v.at(i)=v.at(i)>>1;
        if(buffor31)
            v.at(i)+=0x80000000;
        buffor31=buffor0;

    }
    if(v.at(v.size()-1)==0 && v.size()>1)           ///jesli trzeba przesunac na kolejna pozycje, a brak miesjca
           v.pop_back();
           //cout<<"v.size: "<<v.size()<<", v.at(size-1): "<<v.at(v.size()-1)<<endl;
}
///odejmuje wielotabliowo, brak kontroli kiedy ma wyjsc ujemny wynik! (np. dz   1m

numberIEEE divide(numberIEEE n, numberIEEE divisor){
    vector<unsigned int> residue;               ///reszta
    residue.push_back(0);
    numberIEEE result;
    result.number.push_back(0);

    if(divisor.number.size()==1 && divisor.number.at(0)==0)
    {
        cerr<<"BLAD!: DZIELENIE PRZEZ 0"<<endl;
        numberIEEE n('+',"0",'0',"0");
        return n;
    }
    for(int i = n.number.size()-1; i>=0;i--)   ///lecimy po tablicy (od najstarszych bitow)
    {
        for(int bit=31;bit>=0; bit--)
        {
            if(readBit(n.number.at(i),bit))
            {
                shiftArrayLeft(residue, true);                  ///true-1, false-0 <- czy dodajemy bit na koncu
            }
            else
            {
                shiftArrayLeft(residue, false);
            }
            if(logiSzczegolowe)
            {
                for(int i = residue.size()-1;i>=0;i--)
                    cout<<"divide residue: "<<bitset<32>(residue.at(i))<<endl;
                cout<<"---------------------------"<<endl;
            }

            if(isSubstractable(residue, divisor.number))
            {
                shiftArrayLeft(result.number,true);
                substractArrays(residue, divisor.number);
            }
            else
            {
                    shiftArrayLeft(result.number,false);
            }
        }
    }
    /// znak liczby
    if(int(n.numberSign+divisor.numberSign)==int('+'+'-'))
        result.numberSign='-';
    else if(int(n.numberSign+divisor.numberSign)==int('-'+'-'))
        result.numberSign='+';
    else
        result.numberSign='+';

    result.exponent=n.exponent;

    ///znak exponenty
    if(int(n.exponentSign+divisor.exponentSign)==int('+'+'-'))
    {
        addArrays(result.exponent,divisor.exponent);
        if(n.exponentSign=='+')
            result.exponentSign='+';
        else
            result.exponentSign='-';
    }
    else if(int(n.exponentSign+divisor.exponentSign)==int('-'+'-'))
        {
            if(isSubstractable(n.exponent,divisor.exponent))    ///patrzymy ktora z nich jest wieksza if(n1>=n2)
            {
                    substractArrays( result.exponent, divisor.exponent);
                    result.exponentSign='+';
            }
            else
            {
                    result.exponent=divisor.exponent;
                    substractArrays(result.exponent, n.exponent);
                    result.exponentSign='-';
            }
        }
        else
        {
            if(isSubstractable(n.exponent,divisor.exponent))    ///patrzymy ktora z nich jest wieksza if(n1>=n2)
            {
                    substractArrays( result.exponent, divisor.exponent);
                    result.exponentSign='+';
            }
            else
            {
                    result.exponent=divisor.exponent;
                    substractArrays(result.exponent, n.exponent);
                    result.exponentSign='-';
            }
        }

    if(result.exponent.at(0)==0 && result.exponent.size()==1)
            result.exponentSign='+';
    if(result.number.at(0)==0 && result.number.size()==1)
            cerr<<"BLAD! : DZIELENIE POZA ZAKRESEM (MNIEJSZA_LICZBA/WIEKSZA_LICZBA)"<<endl;
    return result;
}

bool is_hex_notation(std::string const& s)
{
  return s.find_first_not_of("0123456789abcdefABCDEF,.") == std::string::npos;
}
bool is_hex_notationExponenta(std::string const& s)
{
  return s.find_first_not_of("0123456789abcdefABCDEF") == std::string::npos;
}

int menu()
{
    int mode;
    bool wyjscie=true;
    string nm, ne;
    char znakM, znakE;
    char plus = '+';
    char minus = '-';
    bool isDataCorrect1 = false, isDataCorrect2 = false;
    numberIEEE n1, n2;
    uint64_t ticksStart;
    uint64_t ticksEnd;
    //https://defuse.ca/big-number-calculator.htm
    //0xFFFF FFFF = 4294967295 , 0xFFFF FFFF FFFF FFFF = 18446744073709551615,0xFFFF FFFF F = 68719476735
    //numberIEEE number1('-', "115123123123123123123123123432423443243432234432413411344132", '-', "15");
    //numberIEEE number2('-', "51231231231234343234432423423234"  , '-', "15");

    while(wyjscie)
    {
        //cout << "0. Wyjscie \n" << "1. Wprowadzanie wartosci zdenormalizowanej [liczba] \n" << "2. Wprowadzenie wartosci znormalizowanej 10^[exp] + [liczba] \n" << "3. tryb testowy \n";
        //cin >> mode;
        system("cls");
        cout << ticksStart << endl;
        cout << "\n #################################################################\n";
        cout << " ############### ARCHITEKTURA KOMPUTEROW 2 - PROJEKT #############\n";
        cout << " ############## Mnozenie i dzielenie liczb > 1024 bit ############\n";
        cout << " #################################################################\n";
        cout << " ###-----------------------------------------------------------###\n";
        cout << " ###-- MENU: --------------------------------------------------###\n";
        cout << " ###-- 1. Wczytaj pierwsza liczbe -----------------------------###\n";
        cout << " ###-- 2. Wczytaj druga liczbe --------------------------------###\n";
        cout << " ###-- 3. Wyswietl --------------------------------------------###\n";
        cout << " ###-- 4. Wykonaj mnozenie ------------------------------------###\n";
        cout << " ###-- 5. Wykonaj dzielenie -----------------------------------###\n";
        cout << " ###-- 6. Wyjdz -----------------------------------------------###\n";
        cout << " ###-----------------------------------------------------------###\n";
        cout << " #################################################################\n";
        cout << "\n Wybor: ";
        cin >> mode;
        if(mode==1){
            isDataCorrect1 = false;
            /***********************************/
            cout << " Mantysa: ";
            cin >> nm;
            if(!is_hex_notation(nm)){
                cout << " Wprowadzaj cyfry 0-9 lub litery a-f/A-F | tolerowany przecinek ',' lub '.'" << endl;
                getchar();getchar();
                continue;
            }
            cout << " Znak (+/-): ";
            cin >> znakM;
            if(znakM!='+' && znakM!='-'){
                cout << " Podano zly znak." << endl;
                getchar();getchar();
                continue;
            }
             /***********************************/
            cout << " Eksponenta: ";
            cin >> ne;
            if(!is_hex_notationExponenta(ne)){
                cout << " Wprowadzaj cyfry 0-9 lub litery a-f/A-F" << endl;
                getchar();getchar();
                continue;
            }
            cout << " Znak (+/-): ";
            cin >> znakE;
            if(znakE!='+' && znakE!='-'){
                cout << " Podano zly znak." << endl;
                getchar();getchar();
                continue;
            }

            numberIEEE number1(znakM, nm, znakE, ne);
            n1 = number1;
            isDataCorrect1 = true;
            cout << " Ok...";
            getchar();getchar();
        }
        else if(mode==2){
            isDataCorrect2 = false;
            /***********************************/
            cout << " Mantysa: ";
            cin >> nm;
            if(!is_hex_notation(nm)){
                cout << " Wprowadzaj cyfry 0-9 lub litery a-f/A-F | tolerowany przecinek ',' lub '.'" << endl;
                getchar();getchar();
                continue;
            }
            cout << " Znak (+/-): ";
            cin >> znakM;
            if(znakM!='+' && znakM!='-'){
                cout << " Podano zly znak." << endl;
                getchar();getchar();
                continue;
            }
             /***********************************/
            cout << " Eksponenta: ";
            cin >> ne;
            if(!is_hex_notationExponenta(ne)){
                cout << " Wprowadzaj cyfry 0-9 lub litery a-f/A-F" << endl;
                getchar();getchar();
                continue;
            }
            cout << " Znak (+/-): ";
            cin >> znakE;
            if(znakE!='+' && znakE!='-'){
                cout << " Podano zly znak." << endl;
                getchar();getchar();
                continue;
            }

            numberIEEE number2(znakM, nm, znakE, ne);
            n2 = number2;
            isDataCorrect2 = true;
            cout << " Ok...";
            getchar();getchar();
        }
        else if(mode==3){
            if(isDataCorrect1){
                cout << " Liczba1: " << n1.toStringHEX() << endl;
            }
            if(isDataCorrect2){
                cout << " Liczba2: " << n2.toStringHEX() << endl;
            }
            if(!isDataCorrect1 && !isDataCorrect2){
                cout << " Wprowadz poprawne dane." << endl;
            }
            getchar();getchar();
        }
        else if(mode==4){
            if(isDataCorrect1 && isDataCorrect2){
                cout << " Liczba1: " << n1.toStringHEX() << endl;
                cout << " Liczba2: " << n2.toStringHEX() << endl << endl;
                ticksStart = rdtsc();
                cout<<" MULTIPLY RESULT IN HEX:"<<endl<<multiply(n1, n2).toStringHEX()<<endl;
                ticksEnd = rdtsc();
                cout << (unsigned)((ticksEnd-ticksStart)/2494/1000) << " [ms]" << endl;
            }
            else{
                cout << " Wprowadz poprawne dane." << endl;
            }
            getchar();getchar();
        }
        else if(mode==5){
            if(isDataCorrect1 && isDataCorrect2){
                cout << " Liczba1: " << n1.toStringHEX() << endl;
                cout << " Liczba2: " << n2.toStringHEX() << endl << endl;
                ticksStart = rdtsc();
                cout<<" DIVIDE RESULT IN HEX:"<<endl<<divide(n1,n2).toStringHEX()<<endl;
                ticksEnd = rdtsc();
                cout << (double)((ticksEnd-ticksStart)/2494.3/1000.0) << " [ms]" << endl;
            }
            else{
                cout << " Wprowadz poprawne dane." << endl;
            }
            getchar();getchar();
        }
        else if(mode==6){
            wyjscie=false;
        }
        else{
            cout << " Zla opcja.";
            getchar();getchar();
        }
    }
    return mode;
}

void wyswietl(numberIEEE &number1, numberIEEE &number2)
{
    uint64_t ticksStart;
    uint64_t ticksEnd;
    cout<<"Liczby po normalizacji"<<endl;
    cout<<endl<<"Number 1:"<< number1.toStringHEX()<<endl;
    cout<<"Number 2:"<< number2.toStringHEX()<<endl;
    ticksStart = rdtsc();
    cout<<"MULTIPLY RESULT IN HEX:"<<endl<<multiply(number1, number2).toStringHEX()<<endl;
    ticksEnd = rdtsc();
    cout << (double)((ticksEnd-ticksStart)/2494.3/1000.0) << " [ms]" << endl;
    ticksStart = rdtsc();
    cout<<"DIVIDE RESULT IN HEX:"<<endl<<divide(number1,number2).toStringHEX()<<endl;
    ticksEnd = rdtsc();
    cout << (double)((ticksEnd-ticksStart)/2494.3/1000.0) << " [ms]" << endl;
}
int main()
{
    //menu();

    //numberIEEE n('+', "F", '+', "FFFFFFF");

    numberIEEE number1('+', "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff", '+', "0");
    numberIEEE number2('+', "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"  , '+', "0");
    wyswietl(number1, number2);




    //numberIEEE number12('+', "FEFDCA986FE67FE1FEFDCA986FE67FE1FEFDCA986FE67FE1FEFDCA986FE67FE1FEFDCA986FE67FE1FEFDCA986FE67FE1FEFDCA986FE67FE1FEFDCA986FE67FE1FEFDCA986FE67FE1FEFDCA986FE67FE1FEFDCA986FE67FE1FEFDCA986FE67FE1FEFDCA986FE67FE1FEFDCA986FE67FE1FEFDCA986FE67FE1FEFDCA986FE67FE1", '+', "0");
    //numberIEEE number3('+', "321739FE89ACD812321739FE89ACD812321739FE89ACD812321739FE89ACD812321739FE89ACD812321739FE89ACD812321739FE89ACD812321739FE89ACD812321739FE89ACD812321739FE89ACD812321739FE89ACD812321739FE89ACD812321739FE89ACD812321739FE89ACD812321739FE89ACD812"  , '+', "0");
    //wyswietl(number12, number3);

    /*
    numberIEEE number1('+', "115,123412342DDDDD8589548958985995", '+', "15");
    numberIEEE number2('+', "0.012344"  , '+', "15");
    wyswietl(number1, number2);

    numberIEEE number4('-', "115", '+', "15");
    numberIEEE number5('-', "5"  , '+', "15");
    wyswietl(number4, number5);

    numberIEEE number6('+', "115", '-', "15");
    numberIEEE number7('+', "5"  , '-', "15");
    wyswietl(number6, number7);

    numberIEEE number8('+', "115", '-', "15");
    numberIEEE number9('-', "5"  , '+', "15");
    wyswietl(number8, number9);

    numberIEEE number10('-', "115", '+', "15");
    numberIEEE number11('+', "5"  , '-', "15");
    wyswietl(number10, number11);
    */
    return 0;
}
