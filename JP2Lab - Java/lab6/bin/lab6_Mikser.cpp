#include"lab6_Mikser.h"
#include <cmath>
#include <cstdlib>
//tablica jednowymiarowa bez srodkowego(do wyliczenia)
JNIEXPORT jint JNICALL Java_lab6_Mikser_miksuj___3II_3DD
(JNIEnv * env, jobject obj, jintArray piksele, jint zakres_kolorow, jdoubleArray wartosciBeta, jdouble liczbaLosowa) {
	if (env->GetArrayLength(wartosciBeta) != 4 || env->GetArrayLength(piksele) != 8){
		return -1;
	}
	jint* kopiaPikseli = env->GetIntArrayElements(piksele, NULL);
	jdouble* bety = env->GetDoubleArrayElements(wartosciBeta,NULL);
	double* prawdopodobienstwa = new double[zakres_kolorow+1];
	//Sumowanie/roznica i wyliczenie exponenty
	for (int i = 0; i <= zakres_kolorow; i++) {
		if (kopiaPikseli[3] == i)
			prawdopodobienstwa[i] = -bety[0];
		else
			prawdopodobienstwa[i] = bety[0];
		if (kopiaPikseli[4] == i)
			prawdopodobienstwa[i] += -bety[0];
		else
			prawdopodobienstwa[i] += bety[0];
		if (kopiaPikseli[1] == i)
			prawdopodobienstwa[i] += -bety[1];
		else
			prawdopodobienstwa[i] += bety[1];
		if (kopiaPikseli[6] == i)
			prawdopodobienstwa[i] += -bety[1];
		else
			prawdopodobienstwa[i] += bety[1];
		if (kopiaPikseli[0] == i)
			prawdopodobienstwa[i] += -bety[3];
		else
			prawdopodobienstwa[i] += bety[3];
		if (kopiaPikseli[7] == i)
			prawdopodobienstwa[i] += -bety[3];
		else
			prawdopodobienstwa[i] += bety[3];
		if (kopiaPikseli[2] == i)
			prawdopodobienstwa[i] += -bety[2];
		else
			prawdopodobienstwa[i] += bety[2];
		if (kopiaPikseli[5] == i)
			prawdopodobienstwa[i] += -bety[2];
		else
			prawdopodobienstwa[i] += bety[2];
		prawdopodobienstwa[i] = exp(prawdopodobienstwa[i]);
	}
	//sumowanie wszystkich prawdopodobienstw (0-1)
	double prawdopodobienstwo = 0;
	for (int i = 0; i <= zakres_kolorow; i++) {
		prawdopodobienstwo += prawdopodobienstwa[i];
	}
	for (int i = 0; i <= zakres_kolorow; i++) {
		prawdopodobienstwa[i] /= prawdopodobienstwo;
	}

	int wynik = -1;
	double sumaPrawdopodobienstw = 0;
	//szukanie przedzialu w jakim znajduje sie liczba
	for (int i = 0; i <= zakres_kolorow; i++) {
		sumaPrawdopodobienstw += prawdopodobienstwa[i];
		if (sumaPrawdopodobienstw >= liczbaLosowa) {
			wynik = i-1;
			break;
		}
	}
	if (wynik == -1){}
		wynik = zakres_kolorow;
	}
	//obowiazkowe zwolnienie pamieci
	delete[] prawdopodobienstwa;
	env->ReleaseDoubleArrayElements(wartosciBeta,bety, NULL);
	env->ReleaseIntArrayElements(piksele,kopiaPikseli, NULL);
	return wynik;
}