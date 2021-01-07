# Banner Management Service
Creating *banner-management-service* api will allow banners to be stored, updated and deleted without response-ops dealing with a DB connection.  This service is a lightweight API written in Java using Spring boot 2 with the gcp datastore starter project where alert banners will be housed. This design will allow for a scheduler to be implemented, which is required for setting and removing banner at specific times.
This service will also be harnessed for upcoming work on frontstage.

## Goals
1. Ability to create, delete, update (manage) alert banners from response OPS
1. Storage of banners within GCP Datastore
1. Good design for future work on frontstage
1. Ability to integrate a scheduler

![](./images/api-design.png?raw=true)


## API endpoints
#### Management of Banners
* `GET /banner` - Returns a JSON response of all the current alert banners stored in Datastore
* `GET /banner/<banner-Id>` - returns a JSON response of the specified alert banner
* `POST /banner` - Stores a new banner in the Datastore or updates current with same title
* `PUT /banner/<banner-Id>` - Updates the banner attributes accordingly.
* `DELETE /banner/<banner-Id>` - Deletes a banner in the Datastore with the corresponding key (Title)
* `PATCH /banner/<banner-Id>/active`- Makes a banner active (will deactive a banner that is currently active)
* `GET /banner/active`- Returns the currently active banner

## Data

### Kind: Banners
| Name          | Type          | Value
|---------------|---------------|----------------
| id            | Long          | Banner id
| title         | String        | Title of the banner
| content       | String        | Banner message
| active        | Boolean       | is this banner currently set


### Components
- **Datastore** - The Datastore will now house all the alert banners to make them easily manageable from response OPS. The data entities can be seen above and take into consideration the ability to schedule banners at specific times.
- **Scheduler** - Cronjob or Google cloud task To be investigated for scheduling banner activity.
