# Obstanovka API

Backend service for managing **locations** (concert venues, rentals, rehearsal bases, studios) and an **equipment catalog** with attachable equipment per profile.
Built with **Spring Boot (Kotlin + Hibernate)** and exposed via a REST API documented in OpenAPI 3.1.

## Features

* Cities and addresses management
* Locations with profiles:

    * Concert venue
    * Rental
    * Rehearsal base
    * Studio
* Equipment catalog
* Equipment links per profile (concert venues, rentals)

## API

**Authentication**
  All endpoints require **JWT Bearer authentication**:

  ```
  Authorization: Bearer <token>
  ```

**Pagination & Filtering**

Most list endpoints support:
* `page` (default `0`)
* `size` (default `20`, max `200`)
* `sort` (`property,asc|desc`)
* Full-text query via `q`
* Location filters (e.g. `cityId`, `nearLat`, `nearLon`, `radiusMeters`)

## Example Endpoints

* **Cities**

    * `POST /api/v1/cities` – create
    * `GET /api/v1/cities` – list (paged)
    * `DELETE /api/v1/cities/{id}` – delete

* **Addresses**

    * `POST /api/v1/addresses` – create
    * `GET /api/v1/addresses` – list (paged)
    * `GET /api/v1/addresses/{id}` – details
    * `DELETE /api/v1/addresses/{id}` – delete

* **Locations**

    * `POST /api/v1/locations` – create
    * `GET /api/v1/locations` – list (paged, filterable)
    * `GET /api/v1/locations/{id}` – details
    * `PATCH /api/v1/locations/{id}` – update
    * `DELETE /api/v1/locations/{id}` – delete

* **Profiles**

    * Attach, update, or remove **concert venue**, **rental**, **rehearsal base**, and **studio** profiles under `/api/v1/locations/{id}/<profile>`.

* **Equipment Catalog**

    * `POST /api/v1/equipment` – create
    * `GET /api/v1/equipment` – list (paged, optional search)
    * `GET /api/v1/equipment/{id}` – details
    * `PATCH /api/v1/equipment/{id}` – update
    * `DELETE /api/v1/equipment/{id}` – delete

* **Equipment per Profile**

    * Manage equipment collections for concert venues and rentals:

        * `GET /api/v1/locations/{id}/<profile>/equipment`
        * `PUT /api/v1/locations/{id}/<profile>/equipment`
        * `POST /api/v1/locations/{id}/<profile>/equipment`
        * `PATCH /api/v1/locations/{id}/<profile>/equipment/{equipmentId}`
        * `DELETE /api/v1/locations/{id}/<profile>/equipment/{equipmentId}`

## Error Handling

Standardized error response:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Location not found: 00000000-0000-0000-0000-000000000000",
  "path": "/api/v1/locations/...",
  "timestamp": "2025-09-25T22:00:00Z"
}
```