import logging
from datetime import datetime

from fastapi import FastAPI
from google.cloud import datastore
from pydantic import BaseModel

app = FastAPI()
datastore_client = datastore.Client()


class Banner(BaseModel):
    title: str
    value: str
    set_time: datetime
    remove_time: datetime
    banner_active: bool


@app.get('/')
def route():
    return {'FastAPI'}


# Gets all banners out of Datastore
@app.get('/banner')
def get_banner():
    try:
        query = datastore_client.query(kind='Banner')
        return list(query.fetch())
    except ValueError as error:
        logging.error(error, "Datastore query failed")


# Gets a specific banner out of Datastore
@app.get('/banner/{banner_title_id}')
def get_a_banner(banner_title_id: str):
    try:
        query = datastore_client.query(kind='Banner')
        query.add_filter('Title', '=', banner_title_id)
        query = list(query.fetch())
        if query:
            return query[0]
        else:
            raise IndexError('No corresponding banner in Datastore')
    except ValueError as error:
        logging.error(error, "Datastore query failed")


# Creates a new banner or replace a banner Datastore. Needs to be valid JSON
@app.post('/banner')
def create_banner(new_banner: Banner):
    try:
        entity_key = datastore_client.key('Banner', new_banner.title)
        entity = datastore.Entity(key=entity_key)
        entity.update(
            {
                "Title": new_banner.title,
                "Value": new_banner.value,
                "set_time": new_banner.set_time,
                "remove_time": new_banner.remove_time,
                "banner_active": new_banner.banner_active
            }
        )
        return datastore.Client().put(entity)
    except ValueError as error:
        logging.error(error, "Failed to enter a new banner into Datastore")


# Deletes a banner out of Datastore
@app.delete('/banner/{banner_title_id}')
def delete_banner(banner_title_id: str):
    try:
        query = datastore_client.query(kind='Banner')
        query.add_filter('Title', '=', banner_title_id)
        query = list(query.fetch())
        if query:
            return datastore_client.delete(query[0].key)
        else:
            raise IndexError('No corresponding banner in Datastore')
    except ValueError as error:
        logging.error(error, "Failed to remove the banner from Datastore")
