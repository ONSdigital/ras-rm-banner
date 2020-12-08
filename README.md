<!-- Testing Github actions -->
# Banner Management Service
Creating *banner-management-service* api will allow banners to be stored, updated and deleted without response-ops dealing with a DB connection. The current implementation uses REDIS by setting a variable within response ops using a predefined list of alerts, which is then read by frontstage. This does not allow for multiple banners to be managed. This service will a lightweight API written in Python using FastAPI framework that connects to the GCP datastore where alert banners will be housed. This design also allows for a scheduler to be implemented, which is required for setting and removing banner at specific times.
This service will also be harnessed for upcoming work on frontstage.

## Goals
1. Remove use of REDIS for setting alert banners
1. Ability to create, delete, update (manage) alert banners from response OPS
1. Storage of banners within GCP Datastore
1. Good design for future work on frontstage
1. Ability to integrate a scheduler

![](./images/api-design.png?raw=true)


## API endpoints
#### Management of Banners
* `GET /banner` - Returns a JSON response of all the current alert banners stored in Datastore
* `GET /banner/<banner-title-Id>` - returns a JSON response of the specified alert banner
* `POST /banner` - Stores a new banner in the Datastore or updates current with same title
* `DELETE /banner/<banner-title-Id>` - Deletes a banner in the Datastore with the corresponding key (Title)

#### Posting of banners
* `PUT/PATCH /banner-active/<banner-title-Id>` - Updates the banner_active attributes accordingly.
* `POST /banner-active/<banner-title-Id>`- Posts an active banner change to frontstage

#### FastAPI Documentation
* `/docs` - automatic interactive API documentation (Swagger)
* `redocs` - alternative automatic documentation (ReDoc) 

## Data

### Kind: Banners
| Name          | Type          | Value
|---------------|---------------|----------------
| ID/name       | String        | Title of the banner
| title         | String        | Title of the banner
| value         | String        | Banner message
| set_time      | Date & Time   | Date and time to set banner
| remove_time   | Date & Time   | Data and time to remove banner
| banner_active | Boolean       | is this banner currently set


### Components
- **Datastore** - The Datastore will now house all the alert banners to make them easily manageable from response OPS. The data entities can be seen above and take into consideration the ability to schedule banners at specific times.
- **API** - The API will use the first 4 endpoints to manage the banners, giving the user the ability to Create, Update and Delete within Datastore. The 6th endpoint `PUT/PATCH /banner-active/<banner-title-Id` will update the `banner_active` attribute to corresponding banner selected in response Ops. Firstly, a check will be made on `set_date` and `remove_date` to see whether a scheduled event is required. If not, the active banner will be updated and posted to frontstage.
- **Scheduler** - Cronjob, Google cloud task or Python APScheduler. To be investigated


### Steps to take
1. Connect to GCP Datastore with microservice (Service account in sandbox at first)
1. Write API endpoints TDD
1. Change response OPS to call endpoints instead of reading off a JSON file
1. Implement the scheduler?????
1. Dockerfile and _infra files
