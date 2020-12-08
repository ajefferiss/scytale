# REST API Examples

## Clients
### Get all clients
To get a list of all clients currently registered with the API, you can send the following GET:
`curl http://localhost:8443/api/v1/clients -H 'X-API-Key: Hr9SQrWV9aytWUOxkvyPvngFf22rCmZ2V5wVNWKR' -H 'Content-Type: application/json'`

Which will yield: 
```
[
    {
        "id": 3,
        "name": "Test Client",
        "links": [
            {
                "rel": "self",
                "href": "http://localhost:8443/api/v1/clients/3"
            },
            {
                "rel": "clients",
                "href": "http://localhost:8443/api/v1/clients"
            }
        ]
    },
    {
        "id": 4,
        "name": "Demo Client",
        "links": [
            {
                "rel": "self",
                "href": "http://localhost:8443/api/v1/clients/4"
            },
            {
                "rel": "clients",
                "href": "http://localhost:8443/api/v1/clients"
            }
        ]
    },
    {
        "id": 5,
        "name": "Admin Client",
        "links": [
            {
                "rel": "self",
                "href": "http://localhost:8443/api/v1/clients/5"
            },
            {
                "rel": "clients",
                "href": "http://localhost:8443/api/v1/clients"
            }
        ]
    }
]
```
### Get a specific client
To get a specific client, you can send the following GET:
`curl http://localhost:8443/api/v1/clients/3 -H 'X-API-Key: Hr9SQrWV9aytWUOxkvyPvngFf22rCmZ2V5wVNWKR' -H 'Content-Type: application/json'`

Which will return the following JSON:
```
{
    "id": 3,
    "name": "Test Client",
    "authType": {
        "id": 1,
        "authType": "API_KEY"
    },
    "apiKey": "Hr9SQrWV9aytWUOxkvyPvngFf22rCmZ2V5wVNWKR",
    "roles": [
        "ROLE_USER"
    ],
    "links": [
        {
            "rel": "self",
            "href": "http://localhost:8443/api/v1/clients/3"
        },
        {
            "rel": "clients",
            "href": "http://localhost:8443/api/v1/clients"
        }
    ]
}
```
If the API provided is not the one associated with client information is being retrieved for then a HTTP 403 will be returned. 

### Create a client
To create a new client you can POST the following, as an admin user:

```
curl -X POST http://localhost:8443/api/v1/clients \
-H 'X-API-Key: 2qI5g1A654DebJpmBQ-eB72Sed_i9TmyS8njwL19' \
-H 'Content-Type: application/json' \
-d '{"name": "The Fellowship", "authType": {"id": 1, "authType": "API_KEY"}, "roles": ["ROLE_USER"]}'
```

Which will return the following JSON the apiKey will be required for subsequent requests:
```
{
    "id": 6,
    "name": "The Fellowship",
    "authType": {
        "id": 1,
        "authType": "API_KEY"
    },
    "apiKey": "xE6dBniLFbjpFPuXq9HXmSizkZzjMU1Z2Out1LGu",
    "roles": [
        "ROLE_USER"
    ],
    "links": [
        {
            "rel": "self",
            "href": "http://localhost:8443/api/v1/clients/6"
        },
        {
            "rel": "clients",
            "href": "http://localhost:8443/api/v1/clients"
        }
    ]
}
```
### Update a client
To update a client you can PUT the following:

```
curl -X PUT http://localhost:8443/api/v1/clients/3 \
    -H 'X-API-Key: Hr9SQrWV9aytWUOxkvyPvngFf22rCmZ2V5wVNWKR' \
    -H 'Content-Type: application/json' \
    -d '{"id": 3, "name": "Updated Client", "authType": {"id": 1, "authType": "API_KEY"}, "apiKey": "Hr9SQrWV9aytWUOxkvyPvngFf22rCmZ2V5wVNWKR", "roles": ["ROLE_USER"]}'
```

Which will return the following JSON showing that updates have occurred:
```
{
    "id": 3,
    "name": "Updated Client",
    "authType": {
        "id": 1,
        "authType": "API_KEY"
    },
    "apiKey": "Hr9SQrWV9aytWUOxkvyPvngFf22rCmZ2V5wVNWKR",
    "roles": [
        "ROLE_USER"
    ],
    "links": [
        {
            "rel": "self",
            "href": "http://localhost:8443/api/v1/clients/3"
        },
        {
            "rel": "clients",
            "href": "http://localhost:8443/api/v1/clients"
        }
    ]
}
```
### Delete a client
To delete a client you can DELETE with the following:`curl -X DELETE http://localhost:8443/api/v1/clients/4 -H 'X-API-Key: 2qI5g1A654DebJpmBQ-eB72Sed_i9TmyS8njwL19' -H 'Content-Type: application/json'`
## Keystore
### Get keystores 
To get all keystores for a client you can use the following:
`curl http://localhost:8443/api/v1/keystores -H 'X-API-Key: Hr9SQrWV9aytWUOxkvyPvngFf22rCmZ2V5wVNWKR' -H 'Content-Type: application/json'`

Which will return the following JSON.
```
[
    {
        "id": 9,
        "name": "Test Keystore",
        "links": [
            {
                "rel": "self",
                "href": "http://localhost:8443/api/v1/keystores/9"
            },
            {
                "rel": "keystores",
                "href": "http://localhost:8443/api/v1/keystores"
            }
        ]
    }
]
```
### Get a specific keystore
To get a specific keystore you can use the following:
`curl http://localhost:8443/api/v1/keystores/9 -H 'X-API-Key: Hr9SQrWV9aytWUOxkvyPvngFf22rCmZ2V5wVNWKR' -H 'Content-Type: application/json'`

Which will return the following JSON:
```
{
    "id": 9,
    "name": "Test Keystore",
    "links": [
        {
            "rel": "self",
            "href": "http://localhost:8443/api/v1/keystores/9"
        },
        {
            "rel": "keystores",
            "href": "http://localhost:8443/api/v1/keystores"
        }
    ]
}
```
### Add a keystore
To add a new entry into the keystore you can POST the following: 
```
curl -X POST http://localhost:8443/api/v1/keystores \
    -H 'X-API-Key: Hr9SQrWV9aytWUOxkvyPvngFf22rCmZ2V5wVNWKR' \
    -H 'Content-Type: application/json' \
    -d '{"name": "Test Keystore"}'
```

Which will return the following JSON:
```
{
    "id": 9,
    "name": "Test Keystore",
    "links": [
        {
            "rel": "self",
            "href": "http://localhost:8443/api/v1/keystores/9"
        },
        {
            "rel": "keystores",
            "href": "http://localhost:8443/api/v1/keystores"
        }
    ]
}
```
### Update a keystore
To update a specific keystore you can PUT the following:
```
curl -X PUT http://localhost:8443/api/v1/keystores/9 \
    -H 'X-API-Key: Hr9SQrWV9aytWUOxkvyPvngFf22rCmZ2V5wVNWKR' \
    -H 'Content-Type: application/json' \
    -d '{"id": 9, "name": "Updated Test Keystore"}'
```

Which will return the following JSON:
```
{
    "id": 9,
    "name": "Updated Test Keystore",
    "links": [
        {
            "rel": "self",
            "href": "http://localhost:8443/api/v1/keystores/9"
        },
        {
            "rel": "keystores",
            "href": "http://localhost:8443/api/v1/keystores"
        }
    ]
}
```
### Delete a keystore
To delete a keystore you can DELETE with the following:`curl -X DELETE http://localhost:8443/api/v1/keystores/10 -H 'X-API-Key: Hr9SQrWV9aytWUOxkvyPvngFf22rCmZ2V5wVNWKR' -H 'Content-Type: application/json'`
## Public Key
### Get all public keys
To get all public keys for a keystore you can use: `curl http://localhost:8443/api/v1/keystores/9/keys -H 'X-API-Key: Hr9SQrWV9aytWUOxkvyPvngFf22rCmZ2V5wVNWKR' -H 'Content-Type: application/json'`

Which will return a JSON object like so:
```
[
    {
        "id": 11,
        "keystore": {
            "id": 9,
            "name": "Updated Test Keystore"
        },
        "publicKey": "Test Public Key",
        "links": [
            {
                "rel": "self",
                "href": "http://localhost:8443/api/v1/keystores/9/keys/11"
            },
            {
                "rel": "keystores",
                "href": "http://localhost:8443/api/v1/keystores/9/keys"
            }
        ]
    },
    {
        "id": 12,
        "keystore": {
            "id": 9,
            "name": "Updated Test Keystore"
        },
        "publicKey": "Public Key",
        "links": [
            {
                "rel": "self",
                "href": "http://localhost:8443/api/v1/keystores/9/keys/12"
            },
            {
                "rel": "keystores",
                "href": "http://localhost:8443/api/v1/keystores/9/keys"
            }
        ]
    }
]
```
### Get a specific public key
To get a specific keystore you can use the following:
`curl http://localhost:8443/api/v1/keystores/9/keys/11 -H 'X-API-Key: Hr9SQrWV9aytWUOxkvyPvngFf22rCmZ2V5wVNWKR' -H 'Content-Type: application/json''`

Which will return the following JSON:
```
{
    "id": 11,
    "keystore": {
        "id": 9,
        "name": "Updated Test Keystore"
    },
    "publicKey": "Test Public Key",
    "links": [
        {
            "rel": "self",
            "href": "http://localhost:8443/api/v1/keystores/9/keys/11"
        },
        {
            "rel": "keystores",
            "href": "http://localhost:8443/api/v1/keystores/9/keys"
        }
    ]
}
```
### Add a public key
To add a new public key you can POST the following: 
```
curl -X POST http://localhost:8443/api/v1/keystores/9/keys \
    -H 'X-API-Key: Hr9SQrWV9aytWUOxkvyPvngFf22rCmZ2V5wVNWKR' \
    -H 'Content-Type: application/json' \
    -d '{"publicKey": "Test Public Key"}'
```

Which will return the following JSON:
```
{
    "id": 11,
    "keystore": {
        "id": 9,
        "name": "Updated Test Keystore"
    },
    "publicKey": "Test Public Key",
    "links": [
        {
            "rel": "self",
            "href": "http://localhost:8443/api/v1/keystores/9/keys/11"
        },
        {
            "rel": "keystores",
            "href": "http://localhost:8443/api/v1/keystores/9/keys"
        }
    ]
}
```
### Update a public key
To update a specific public key you can PUT the following:
``` 
curl -X PUT http://localhost:8443/api/v1/keystores/9/keys/11 \
    -H 'X-API-Key: Hr9SQrWV9aytWUOxkvyPvngFf22rCmZ2V5wVNWKR' \
    -H 'Content-Type: application/json' \
    -d '{"id": 11, "publicKey": "New Updated Public Key"}'
```

Which will return the following JSON:
```
{
    "id": 11,
    "keystore": {
        "id": 9,
        "name": "Updated Test Keystore"
    },
    "publicKey": "New Updated Public Key",
    "links": [
        {
            "rel": "self",
            "href": "http://localhost:8443/api/v1/keystores/9/keys/11"
        },
        {
            "rel": "keystores",
            "href": "http://localhost:8443/api/v1/keystores/9/keys"
        }
    ]
}
```
### Delete a public key
To delete a keystore you can DELETE with the following:`curl -X DELETE http://localhost:8443/api/v1/keystores/9/keys/11 -H 'X-API-Key: Hr9SQrWV9aytWUOxkvyPvngFf22rCmZ2V5wVNWKR' -H 'Content-Type: application/json'`