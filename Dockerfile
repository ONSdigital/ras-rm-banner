FROM python:3.8

WORKDIR /banner

COPY requirements.txt .

RUN pip install -r requirements.txt

CMD ["uvicorn", "banner.main:app"]