# Banner Management Service
Creating *banner-management-service* api will allow banners to be stored, updated and deleted without response-ops dealing with a DB connection.  This service is a lightweight API written in Java using Spring boot 2 with the gcp datastore starter project where alert banners will be housed. This design will allow for a scheduler to be implemented, which is required for setting and removing banner at specific times.
This service will also be harnessed for upcoming work on ras-frontstage.

## Goals
1. Ability to create, delete, update (manage) alert banners from response OPS
1. Storage of banners within GCP Datastore
1. Good design for future work on ras-frontstage
1. Ability to integrate a scheduler

![](./images/api-design.png?raw=true)


## API endpoints

The API allows there to be multiple templates, but only one banner.

#### Management of Banners
* `GET /template` - Returns a JSON response of all the current templates stored in Datastore
* `GET /template/<banner-Id>` - returns a JSON response of the specified template
* `POST /template` - Stores a new template in the Datastore or updates current with same title
* `PUT /template/<banner-Id>` - Updates the template attributes accordingly.
* `DELETE /template/<banner-Id>` - Deletes a template in the Datastore with the corresponding key (Title)
  
* `GET /banner` - Returns the currently active banner
* `POST /banner` - Stores the text for the active banner
* `DELETE /banner` - Removes the text for the active banner, functionally deactivating it

## Data

### Kind: Banner
| Name          | Type          | Value
|---------------|---------------|----------------
| id            | String        | Banner id
| content       | String        | Banner message

### Kind: Templates
| Name          | Type          | Value
|---------------|---------------|----------------
| id            | Long          | Banner id
| title         | String        | Title of the banner
| content       | String        | Banner message


### Components
- **Datastore** - The Datastore will now house all the alert banners to make them easily manageable from response OPS. The data entities can be seen above and take into consideration the ability to schedule banners at specific times.
- **Scheduler** - Cronjob or Google cloud task To be investigated for scheduling banner activity.
