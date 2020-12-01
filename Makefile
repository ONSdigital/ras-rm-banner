all: build test
.PHONY: all
build:
	pip3 install -r requirements.txt

lint:
	flake8 ./banner-api ./tests

start:
	uvicorn main:app --reload

test: lint
	pytest tests
