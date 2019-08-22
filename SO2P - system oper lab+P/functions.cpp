#include <string>
#include <ncurses.h>
#include <thread>
#include <mutex>
#include <iostream>
using namespace std;
///only _ts - thread safe
mutex mutex_print_ts,mutex_print_clr_ts,mutex_clr_line_ts;

void print_ts(int y,int x, string msg){
	lock_guard<mutex> guard(mutex_print_ts);
	mvprintw( y, x, msg.c_str());
	refresh();
}
void print_clr_ts(int y, int x, string msg, int lengthToClear)
{
	string buffor(lengthToClear,' ');
	lock_guard<mutex> guard(mutex_print_clr_ts);
	mvprintw( y, x, buffor.c_str());
	mvprintw( y, x, msg.c_str());
	refresh();
}
void clr_line_ts(int y,int x ,int lengthToClear){
	
	lock_guard<mutex> guard(mutex_clr_line_ts);
	string buffor(lengthToClear,' ');
	mvprintw(y, x, buffor.c_str());
}
void print_empty_rectangle(int y, int x, int h, int w)
{
	for (int row = 0; row < w; row++) 
	{
		for (int col = 0; col < h; col++) {
    if (row == 0 || row == w - 1) 
	{
      mvprintw(y+col,x+row,"#");//System.out.println(star);
    } else if (col == 0 || col == h - 1) {
      mvprintw(y+col,x+row,"#");
    } 
  }
}
refresh();
}
void print_line_y(int y,int x, int length){
	for (int i=0;i<length;i++)
		mvprintw(y+i,x,"*");
	refresh();
}