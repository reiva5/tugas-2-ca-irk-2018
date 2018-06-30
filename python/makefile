all: run

run:
	python main.py input1.txt 1
	mv input1.txt input1_before.txt
	python main.py input.irk 2
	mv input1.txt 1_after.pdf
	diff 1_after.pdf input1_before.txt
