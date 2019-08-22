#ifndef SKIER_H
#define SKIER_H
#include <mutex>
#include <condition_variable>
#include "SkiLift.h"
#include <thread>
#include "SlopesStats.h"
using namespace std;
const int LIFT_A=0,LIFT_B=1,LIFT_C=2, BUS_STOP=3;	///bus stop is not a lift,but it undergoes it!
const int LIFTS_COUNT=3;
const int STAGES_OF_SLOPE=4;
const int MONEY_RANGE=500;		///pesants' money

class SkiLift;

class Skier{
private:
    int money;
    bool outOfMoney;
    int queueSize[LIFTS_COUNT];      //last remembered queue size
    bool stage[STAGES_OF_SLOPE];        //point of slope
	condition_variable cv;		///... temporary
	bool isThinkingOut;					///where should i go now?
    mutex brain_mutex, addSlopesStatistics_mutex, substractSlopesStatistics_mutex;
	thread *t;
public:
	
    
    Skier(int _money, vector<SkiLift*> &lift, vector<SlopesStats*> &slopes);
    ~Skier();
    int getMoney();
    bool substractMoney(int amount);
    void rememberQueue(int lift, int queue);
    int chooseDirectionA();	//returns which lift do we take at STAGE A
	int chooseDirectionB();	//returns which lift do we take at STAGE A
	int chooseDirectionC();	//returns which lift do we take at STAGE A
	int chooseDirectionBUS();
    int whereIsSkiier(); 	//returns current stage
	void setStage(int which);
    void brain(vector<SkiLift*> &lifts, vector<SlopesStats*> &slopes);
	void awakeSkier(int liftID);
};

#endif // SKIER_H
