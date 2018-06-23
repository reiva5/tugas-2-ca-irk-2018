all: create run

create:
	g++ -o main main.cpp Compressed.cpp -std=c++11 -O2 -g -lm

run:
	./main 1.pdf 1
	mv 1.pdf 1_before.pdf
	./main 1.irk 2
	mv 1.pdf 1_after.pdf
	diff 1_after.pdf 1_before.pdf