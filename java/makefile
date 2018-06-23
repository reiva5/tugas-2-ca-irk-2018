all: create run

create:
	javac main.java Compressed.java ObjectSizeCalculator.java

run:
	java --illegal-access=warn main 1.pdf 1 
	mv 1.pdf 1_before.pdf
	java --illegal-access=warn main 1.irk 2
	mv 1.pdf 1_after.pdf
	diff 1_after.pdf 1_before.pdf