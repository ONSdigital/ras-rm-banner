all: test package
.PHONY: all
package:
	mvn package -D skipTests=true

test: clean
	mvn test

clean:
	mvn clean
