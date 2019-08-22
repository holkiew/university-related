#include "SkiLift.h"

SkiLift::SkiLift(string _name, int _lengthSecSlope, int _lengthSecLift, int _singleLiftSeatCapacity, int _seatIntervalMilisec, int _price, int _liftID):
	name(_name),
	slope(_lengthSecSlope),
	lift(_lengthSecLift),
	singleLiftSeatCapacity(_singleLiftSeatCapacity),
	seatIntervalMilisec(_seatIntervalMilisec),
	price(_price),
	awaitingForBus(0),
	liftID(_liftID){};
int SkiLift::getSlopeSkiiTimeSec()
{
	return slope.getLengthSec();
}
void SkiLift::addToQueue(Skier *skier)
{
	lock_guard<mutex> guard(_queue_mutex);
	_queue.push(skier);
	cv_queue_printer_nc.notify_one();
	
}
void SkiLift::addAwaitingForBus(){
	lock_guard<mutex> guard(awaitingForBus_mutex);
	awaitingForBus++;
	cv_awaitingForBus_printer_nc.notify_one();
}
int SkiLift::getAwaitingForBus(int amount)
{
	lock_guard<mutex> guard(awaitingForBus_mutex);
	if(amount>=awaitingForBus)
        {
            int buffor=awaitingForBus;
            awaitingForBus=0;
			cv_awaitingForBus_printer_nc.notify_one();
            return buffor;
        }
        else
        {
			awaitingForBus-=amount;
			cv_awaitingForBus_printer_nc.notify_one();
			return amount;
		}
            
}
void SkiLift::liftSkiers()
{
	vector<Skier*> *seatWithSkiers;
	for(;;) 
	{
		seatWithSkiers=new vector<Skier*>();
		///load passengers
		for(int i=0; i<singleLiftSeatCapacity; i++) 
		{
			if(_queue.size()!=0) 
			{
				//cout<<"Znajduje sie :"<<_queue.size()<<" osob w kolejce ->"<<liftID<<endl;
				if(_queue.front()->substractMoney(price))
				{
					_queue.front()->rememberQueue(liftID,_queue.size());
					seatWithSkiers->push_back(_queue.front());
					addToLiftingSkiiersCount();
				}
				_queue.pop();
				cv_queue_printer_nc.notify_one();
			}
		}
		///send seat
		seats.push(new LiftSeat( seatWithSkiers, lift.getLengthSec() ));
		this_thread::sleep_for(chrono::milliseconds(seatIntervalMilisec));
	}

}
void SkiLift::checkIfLifted()	///lifted to end of lift
{
	vector<Skier*> *buffor;
	for(;;) {
		if(seats.size()>0 && seats.front()->isLifted()) { ///if seat ended his jurney
			if(seats.front()->amountOfSkiers()!=0)
			{
				buffor=seats.front()->passSkiers();
				for(int i=0; i<seats.front()->amountOfSkiers();i++)
				{
					buffor->at(i)->awakeSkier(liftID);
					substractLiftingSkiiersCount();
				}
				//cout<<"Odebrano narciarzy ->"<<liftID<<endl;
			}
			delete seats.front();
			seats.pop();
		}
		this_thread::sleep_for(chrono::milliseconds(seatIntervalMilisec-50));
	}
}

void SkiLift::addToLiftingSkiiersCount(){
	liftingSkiiersCount++;
	cv_occupiedSeats_printer_nc.notify_one();
}

void SkiLift::substractLiftingSkiiersCount(){
	liftingSkiiersCount--;
	cv_occupiedSeats_printer_nc.notify_one();
}

void SkiLift::queuePrinter_nc(){
	string str="Kolejka:";
	int y,x;
	switch (liftID)
	{
		case LIFT_A:
			y=18;
			x=30;
			break;
		case LIFT_B:
			y=9;
			x=60;
			break;
		case LIFT_C:
			y=9;
			x=5;
			break;
	}
	print_ts(y,x,str.c_str());
	x+=str.size();
	str="";
	unique_lock<mutex> lk(queuePrinter_mutex);
	for(;;){
		cv_queue_printer_nc.wait(lk);
		clr_line_ts(y,x,str.size());
		str=to_string(_queue.size());
		print_ts(y,x,str.c_str());
	}
}

void SkiLift::occupiedLiftSeatsPrinter_nc(){
	string str="Zajete miejsca";
	int y,x;
	switch (liftID)
	{
		case LIFT_A:
			y=13;
			x=45;
			break;
		case LIFT_B:
			y=4;
			x=41;
			break;
		case LIFT_C:
			y=4;
			x=20;
			break;
	}
	print_ts(y,x,str.c_str());
	x+=str.size()/2;
	y++;
	str="";
	unique_lock<mutex> lk(occupiedLiftSeatsPrinter_mutex);
	for(;;){
		cv_occupiedSeats_printer_nc.wait(lk);
		clr_line_ts(y,x,str.size());
		str=to_string(liftingSkiiersCount);
		print_ts(y,x,str.c_str());
	}
}
void SkiLift::awaitingForBusPrinter_nc()
{
	string str=":";
	int y=19,x=38;
	print_ts(y,x,str.c_str());
	x+=str.size();
	unique_lock<mutex> lk(awaitingForBusPrinter_mutex);
	for(;;){
		cv_awaitingForBus_printer_nc.wait(lk);
		clr_line_ts(y,x,str.size());
		str=to_string(awaitingForBus);
		print_ts(y,x,str.c_str());
	}
};


thread SkiLift::spawnThreadliftSkiers()
{
	return thread([=] { liftSkiers(); });
}
thread SkiLift::spawnThreadcheckIfLifted()
{
	return thread([=] { checkIfLifted(); });
}
thread SkiLift::spawnThreadqueuePrinter_nc()
{
	return thread([=] { queuePrinter_nc(); });
}
thread SkiLift::spawnThreadoccupiedLiftSeatsPrinter_nc()
{
	return thread([=] { occupiedLiftSeatsPrinter_nc(); });
}
thread SkiLift::spawnThreadawaitingForBusPrinter_nc()
{
	return thread([=] { awaitingForBusPrinter_nc(); });
}
