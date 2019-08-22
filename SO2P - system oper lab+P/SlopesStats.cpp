#include "SlopesStats.h"




SlopesStats::SlopesStats(int _slopeID): slopeID(_slopeID){}	///for printing
							///default,only for adding and substracting statistics
SlopesStats::~SlopesStats()
{
}

void SlopesStats::addSkierSlopesStatistics(){
	lock_guard<mutex> guard(mtx);
	slopeStats++;
	cv.notify_one();
}
void SlopesStats::substractSkierSlopesStatistics(){
	lock_guard<mutex> guard(mtx);
	slopeStats--;
	cv.notify_one();
}

void SlopesStats::slopeStatistiscPrinter_nc(){
		string str="Jedzie";
		int y=0,x=0;
		switch (slopeID)
		{
			case SLOPE_A:
				y=13;
				x=32;
				break;
			case SLOPE_B:
				y=4;
				x=62;
				break;
			case SLOPE_C:
				y=4;
				x=7;
				break;
			case SLOPE_BA:
				y=15;
				x=8;
				break;
			case SLOPE_CA:
				y=15;
				x=61;
				break;
		}
		
		print_ts(y,x,str.c_str());
		y++;
		x+=str.size()/2;
		str="";
		unique_lock<mutex> lk(mtx);
		for(;;)
			{
			cv.wait(lk);
			clr_line_ts(y,x,str.size());
			str=to_string(slopeStats);
			print_ts(y,x,str.c_str());
		}
}
thread SlopesStats::spawnThreadslopeStatisticsPrinter_nc(){
	
		return thread([=] { slopeStatistiscPrinter_nc(); });
}