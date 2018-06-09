all: create run

create:
	gcc -o main main.c Compressed.c -std=gnu99 -O2 -g -lm

run:
	./main 1.pdf 1
	mv 1.pdf 1_before.pdf
	./main 1.irk 2
	mv 1.pdf 1_after.pdf
	diff 1_after.pdf 1_before.pdf