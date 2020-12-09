all: test package
.PHONY: all
package:
	mvn package -D skipTests=true

test: check-env clean
	mvn verify

unit-tests: clean
	mvn verify -DskipITs=true

integration-tests: check-env clean
	mvn verify -DskipUTs=true

clean:
	mvn clean

check-env:
ifndef GOOGLE_APPLICATION_CREDENTIALS
	$(error GOOGLE_APPLICATION_CREDENTIALS must be set to run integration tests)
endif
