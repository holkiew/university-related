var zmienStan = function (lampka) { 
    return !lampka;
}

var zmienStanLampek = function (reklama){
	var indexWylosowanejLampki = parseInt(Math.random()*reklama.getLampki().size());
	//print(indexWylosowanejLampki);
	var wylosowanaLampka = reklama.getLampki().get(indexWylosowanejLampki);
	
	var zmienionyStanLampki = zmienStan(wylosowanaLampka.czySwieci);
	//print(zmienionyStanLampki);
	
	reklama.getLampki().get(indexWylosowanejLampki).setCzySwieci(zmienionyStanLampki);
	
    //print(reklama.getLampki().get(indexWylosowanejLampki));
	return reklama.getLampki();
}

var wyswietlReklame = function(reklama){
	print(reklama.getEkran());
}
