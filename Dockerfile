FROM python:3.8-slim

COPY requirements.txt .

COPY banner .

RUN pip install -r requirements.txt

CMD ["uvicorn", "routes:app"]