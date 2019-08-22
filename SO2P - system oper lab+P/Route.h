#ifndef ROUTE_H
#define ROUTE_H
using namespace std;
class Route{	//time which takes to reach point B from A
    private:
    const int lengthSec;
    public:
    Route(int _lengthSec);
    int getLengthSec();
};

#endif