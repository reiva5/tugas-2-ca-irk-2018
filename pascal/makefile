all: create run

create:
	fpc main.pas -Ci -g -O2 -Sg

run:
	./main 1.pdf 1
	mv 1.pdf 1_before.pdf
	./main 1.irk 2
	mv 1.pdf 1_after.pdf
	diff 1_after.pdf 1_before.pdf