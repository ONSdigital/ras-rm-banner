all: test package
.PHONY: all
package:
	mvn package -D skipTests=true

test: clean
	mvn verify

unit-tests: clean
	mvn verify -DskipITs=true

integration-tests: clean
	mvn verify -DskipUTs=true

clean:
	mvn clean
