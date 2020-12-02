all: build test
.PHONY: all
build:
	pip3 install -r requirements.txt

lint:
	flake8 ./banner ./tests

start:
	uvicorn banner.routes:app --reload

test: lint
	 coverage run -m pytest tests