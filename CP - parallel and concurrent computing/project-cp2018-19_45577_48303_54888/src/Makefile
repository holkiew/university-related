CC=gcc
CFLAGS=-g -std=gnu11 -Wall -Werror -fcilkplus
LDFLAGS=-lcilkrts -lm

S=debug.c main.c patterns.c unit.c scanUtils.c farmUtils.c
O=$(patsubst %.c,%.o,$(S))

TARGET=main
all: $(TARGET)

main: $(O)
	$(CC) -o $@ $^ $(LDFLAGS)

all:
	rm -f ASMdump.txt
	objdump -d -M intel -S patterns.o >> ASMdump.txt

clean:
	rm -f $(TARGET) $(O)


debug.o: debug.c debug.h
main.o: main.c unit.h debug.h
patterns.o: patterns.c patterns.h
unit.o: unit.c patterns.h debug.h unit.h
scanUtils.o: scanUtils.c scanUtils.h
farmUtils.o: farmUtils.c farmUtils.h
