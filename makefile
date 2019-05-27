JAVA = java
JAVAC = javac

SRC_PATH = src/com/lcs/
OUT_PATH = out/

all:complie

complie:
	$(JAVAC) $(SRC_PATH)*.java -d out
run:
	java out.Server 8080
