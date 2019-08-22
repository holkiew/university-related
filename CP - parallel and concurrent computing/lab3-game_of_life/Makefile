CC=gcc
CFLAGS=-Wall -g -ansi -pedantic -c -std=gnu11 -Werror  -fcilkplus
LDFLAGS=-lpcre -lpthread -lcilkrts


OUTPUT=glife
SRC=config.c game.c main.c mem.c
OBJ=$(patsubst %.c,%.o,$(SRC))

$(OUTPUT): $(OBJ)
	$(CC) -o $(OUTPUT) $(OBJ) $(LDFLAGS)

%.o: %.c
	$(CC) $(CFLAGS) $<

clean:
	rm -f $(OUTPUT) $(OBJ)
