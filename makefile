JAVA = java
JAVAC = javac

SRC_PATH = src/com/lcs/
OUT_PATH = out/

all:complie run

complie:
	$(JAVAC) $(SRC_PATH)*.java -d out
run:
	cd $(OUT_PATH)
	java Server 8080
