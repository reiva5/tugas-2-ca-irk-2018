all: run

run:
	python main.py 1.pdf 1
	mv 1.pdf 1_before.pdf
	python main.py 1.irk 2
	mv 1.pdf 1_after.pdf
	diff 1_after.pdf 1_before.pdf