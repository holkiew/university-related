#include "Skier.h"


Skier::Skier(int _money, vector<SkiLift*> &lifts, vector<SlopesStats*> &slopes) : money(_money), outOfMoney(false), queueSize({0,0,0}), stage( {false,false,false,true}), isThinkingOut(true) 
{
	vector<SkiLift*> *buf = &lifts;
	vector<SlopesStats*> *buf2 = &slopes;
	t=new thread([=] { brain(*buf, *buf2); });
	t->detach();
}
Skier::~Skier() 
{
	delete t;
}
int Skier::getMoney()
{
	return money;
}
bool Skier::substractMoney(int amount)
{
	if(money-amount<0) {
		outOfMoney=true;
		isThinkingOut=true;
		cv.notify_one();
		return false;
	} else {
		money-=amount;
		return true;
	}
}
void Skier::rememberQueue(int lift, int queue)
{
	queueSize[lift]=queue;
}
//returns which lift do we take
int Skier::chooseDirectionA()
{
	int min=queueSize[0], where=0;
	if(!outOfMoney)
	{
		for(int i=1; i<LIFTS_COUNT; i++)
		{
			if(queueSize[i]<min) 
			{
				min=queueSize[i];
				where=i;
			}
			else 
			if(queueSize[i]==min)
			{
				srand(time(NULL));
				if((rand() % 1000) > 500)
					where=i;
			}
		}
	}
	else
	{
		return BUS_STOP;
	}
	return where;
}
int Skier::chooseDirectionB(){
	if(!outOfMoney)
	{
		if(queueSize[LIFT_A]<queueSize[LIFT_B])
			return LIFT_A;
		else
			if(queueSize[LIFT_A]==queueSize[LIFT_B])
			{
				srand(time(NULL));
				if((rand() % 1000) > 500)
					return LIFT_A;
				else
					return LIFT_B;
			}
			else
				return LIFT_B;
	}else
	{
		return BUS_STOP;
	}
}
int Skier::chooseDirectionC(){
	if(!outOfMoney)
	{
		if(queueSize[LIFT_A]<queueSize[LIFT_C])
			return LIFT_A;
		else
			if(queueSize[LIFT_A]==queueSize[LIFT_C])
			{
				srand(time(NULL));
				if((rand() % 1000) > 500)
					return LIFT_A;
				else
					return LIFT_C;
			}
			else
				return LIFT_C;
	}else
	{
		return BUS_STOP;
	}
}
int Skier::chooseDirectionBUS(){
	if(!outOfMoney)
	{
		return LIFT_A;
	}else
	{
		return BUS_STOP;
	}
}
//returns current stage
int Skier::whereIsSkiier()
{
	int where=0;
	for(int i=0; i<STAGES_OF_SLOPE; i++) {
		if(stage[i]) {
			where=i;
			break;
		}
	}
	return where;
}
void Skier::setStage(int which){
	for(int i=0;i<STAGES_OF_SLOPE;i++){
		if(i==which)
		{
			stage[i]=true;
		}
		else
		{
			stage[i]=false;
		}
	}
}
void Skier::awakeSkier(int liftID){
	setStage(liftID);
	isThinkingOut=true;
	cv.notify_one();
}


void Skier::brain(vector<SkiLift*> &lifts, vector<SlopesStats*> &slopes)
{
	unique_lock<mutex> lk(brain_mutex);
	bool skiing=true;
	for(;skiing;)
	{
		cv.wait(lk, [=]{return isThinkingOut;});
		isThinkingOut=false;
		//cout<<"Gdzie jest narciarz: "<<whereIsSkiier()<<endl;
		if(whereIsSkiier()==LIFT_A)	///peak of current slope
		{
			switch (chooseDirectionA())	///possibilities from stage A
			{
				case LIFT_A:
					//cout<<"pojechal na dol:"<<money<<endl;
					slopes.at(SLOPE_A)->addSkierSlopesStatistics();
					this_thread::sleep_for(chrono::milliseconds(lifts.at(LIFT_A)->getSlopeSkiiTimeSec()*1000)); //skiing
					slopes.at(SLOPE_A)->substractSkierSlopesStatistics();
					setStage(BUS_STOP);
					lifts.at(LIFT_A)->addToQueue(this);
					break;
				case LIFT_B:
					lifts.at(LIFT_B)->addToQueue(this);
					break;
				case LIFT_C:
					lifts.at(LIFT_C)->addToQueue(this);
					break;
				case BUS_STOP:
					slopes.at(SLOPE_A)->addSkierSlopesStatistics();
					this_thread::sleep_for(chrono::milliseconds(lifts.at(LIFT_A)->getSlopeSkiiTimeSec()*1000));
					slopes.at(SLOPE_A)->substractSkierSlopesStatistics();
					skiing=false;
					lifts.at(LIFT_A)->addAwaitingForBus();
					break;
			}
		}
		else
		
			if(whereIsSkiier()==LIFT_B)
			{
				switch(chooseDirectionB())
				{
					case LIFT_A:
						slopes.at(SLOPE_B)->addSkierSlopesStatistics();
						this_thread::sleep_for(chrono::milliseconds(lifts.at(LIFT_B)->getSlopeSkiiTimeSec()*1000));
						slopes.at(SLOPE_B)->substractSkierSlopesStatistics();
						slopes.at(SLOPE_BA)->addSkierSlopesStatistics();
						this_thread::sleep_for(chrono::milliseconds(lifts.at(LIFT_A)->getSlopeSkiiTimeSec()*1000)); //skiing
						slopes.at(SLOPE_BA)->substractSkierSlopesStatistics();
						setStage(BUS_STOP);
						lifts.at(LIFT_A)->addToQueue(this);
						break;
					case LIFT_B:
						slopes.at(SLOPE_B)->addSkierSlopesStatistics();
						this_thread::sleep_for(chrono::milliseconds(lifts.at(LIFT_B)->getSlopeSkiiTimeSec()*1000)); //skiing
						slopes.at(SLOPE_B)->substractSkierSlopesStatistics();
						setStage(LIFT_A);
						lifts.at(LIFT_B)->addToQueue(this);
						break;
					case BUS_STOP:
						slopes.at(SLOPE_B)->addSkierSlopesStatistics();
						this_thread::sleep_for(chrono::milliseconds(lifts.at(LIFT_B)->getSlopeSkiiTimeSec()*1000));
						slopes.at(SLOPE_B)->substractSkierSlopesStatistics();
						slopes.at(SLOPE_BA)->addSkierSlopesStatistics();
						this_thread::sleep_for(chrono::milliseconds(lifts.at(LIFT_A)->getSlopeSkiiTimeSec()*1000)); //skiing
						slopes.at(SLOPE_BA)->substractSkierSlopesStatistics();
						skiing=false;
						lifts.at(LIFT_A)->addAwaitingForBus();
						break;
				}
			}
			else
			
				if(whereIsSkiier()==LIFT_C)
				{
					switch(chooseDirectionC())
					{
						case LIFT_A:
							slopes.at(SLOPE_C)->addSkierSlopesStatistics();
							this_thread::sleep_for(chrono::milliseconds(lifts.at(LIFT_C)->getSlopeSkiiTimeSec()*1000));
							slopes.at(SLOPE_C)->substractSkierSlopesStatistics();
							slopes.at(SLOPE_CA)->addSkierSlopesStatistics();
							this_thread::sleep_for(chrono::milliseconds(lifts.at(LIFT_A)->getSlopeSkiiTimeSec()*1000)); //skiing
							slopes.at(SLOPE_CA)->substractSkierSlopesStatistics();
							setStage(BUS_STOP);
							lifts.at(LIFT_A)->addToQueue(this);
							break;
						case LIFT_C:
							slopes.at(SLOPE_C)->addSkierSlopesStatistics();
							this_thread::sleep_for(chrono::milliseconds(lifts.at(LIFT_C)->getSlopeSkiiTimeSec()*1000)); //skiing
							slopes.at(SLOPE_C)->substractSkierSlopesStatistics();
							setStage(LIFT_A);
							lifts.at(LIFT_C)->addToQueue(this);
							break;
						case BUS_STOP:
							slopes.at(SLOPE_C)->addSkierSlopesStatistics();
							this_thread::sleep_for(chrono::milliseconds(lifts.at(LIFT_C)->getSlopeSkiiTimeSec()*1000));
							slopes.at(SLOPE_C)->substractSkierSlopesStatistics();
							slopes.at(SLOPE_CA)->addSkierSlopesStatistics();
							this_thread::sleep_for(chrono::milliseconds(lifts.at(LIFT_A)->getSlopeSkiiTimeSec()*1000)); //skiing
							slopes.at(SLOPE_CA)->substractSkierSlopesStatistics();
							skiing=false;
							lifts.at(LIFT_A)->addAwaitingForBus();
							break;
					}
				}
				else
					
					if(whereIsSkiier()==BUS_STOP)
					{
						switch(chooseDirectionBUS())
						{
							case LIFT_A:
								lifts.at(LIFT_A)->addToQueue(this);
								break;
							case BUS_STOP:
								skiing=false;
								lifts.at(LIFT_A)->addAwaitingForBus();
								break;
						}
					}
	}
	delete this;
}

