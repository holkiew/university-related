#ifndef SLOPESSTATS_H
#define SLOPESSTATS_H
#include <condition_variable>
#include <thread>
#include <mutex>
#include <string>
using namespace std;
extern void print_ts(int y,int x, string msg);
extern void clr_line_ts(int y, int x, int lengthToClear);

const int SLOPE_A=0,SLOPE_B=1,SLOPE_C=2,SLOPE_CA=3,SLOPE_BA=4;
const int SLOPES_COUNT=5;
class SlopesStats
{
private:
	const int slopeID;
	int slopeStats;
	condition_variable cv;
	thread t;
	mutex mtx;
	
public:
	
	SlopesStats(int _slopeID);
	SlopesStats();
	~SlopesStats();
	void addSkierSlopesStatistics();
	void substractSkierSlopesStatistics();
	void slopeStatistiscPrinter_nc();
	
   thread spawnThreadslopeStatisticsPrinter_nc();

};

#endif // SLOPESSTATS_H
