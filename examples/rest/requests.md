# REST API Examples

## Authentication types
### Get all authentication types
To get all available authentication types you can send the following GET:
`curl -v http://localhost:8080/authTypes`

Which will return the following JSON:
```
{
    "_embedded": {
        "authTypeList": [
            {
                "id": 1,
                "authType": "API_KEY",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/authTypes/1"
                    },
                    "authTypes": {
                        "href": "http://localhost:8080/authTypes"
                    }
                }
            },
            {
                "id": 2,
                "authType": "CERTIFICATE",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/authTypes/2"
                    },
                    "authTypes": {
                        "href": "http://localhost:8080/authTypes"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/authTypes"
        }
    }
}
```
### Get specific authentication type
To get a specific authentication type you can send the following GET:
`curl -v http://localhost:8080/authTypes/1`

Which will return the following JSON:
```
{
    "id": 1,
    "authType": "API_KEY",
    "_links": {
        "self": {
            "href": "http://localhost:8080/authTypes/1"
        },
        "authTypes": {
            "href": "http://localhost:8080/authTypes"
        }
    }
}
```
## Clients
### Get all clients
To get a list of all clients currently registered with the API, you can send the following GET:
`curl -v http://localhost:8080/clients`

Which will yield: 
```
{
    "_embedded": {
        "clientList": [
            {
                "id": 3,
                "name": "Test Key",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/clients/3"
                    },
                    "clients": {
                        "href": "http://localhost:8080/clients"
                    }
                }
            },
            {
                "id": 4,
                "name": "The Fellowship",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/clients/4"
                    },
                    "clients": {
                        "href": "http://localhost:8080/clients"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/clients"
        }
    }
}
```
### Get a specific client
To get a specific client, you can send the following GET:
`curl -v http://localhost:8080/clients/4`

Which will return the following JSON:
```
{
    "id": 4,
    "name": "The Fellowship",
    "_links": {
        "self": {
            "href": "http://localhost:8080/clients/4"
        },
        "clients": {
            "href": "http://localhost:8080/clients"
        }
    }
}
```

### Create a client
To create a new client you can POST the following:

`curl -X POST localhost:8080/clients -H 'Content-type:application/json' -d '{"name": "The Fellowship", "authType": {"id": 1, "authType": "API_KEY"}}'`

Which will return the following JSON the apiKey will be required for subsequent requests:
```
{
    "id": 4,
    "name": "The Fellowship",
    "authType": {
        "id": 1,
        "authType": "API_KEY"
    },
    "apiKey": "1mrmfvmcHpdY6kRMUyAmDfBB8ZRMAUqTeemM8t7K",
    "_links": {
        "self": {
            "href": "http://localhost:8080/clients/4"
        },
        "clients": {
            "href": "http://localhost:8080/clients"
        }
    }
}
```
### Update a client
To update a client you can PUT the following:

`curl -X PUT localhost:8080/clients/10 -H 'API_KEY: 1mrmfvmcHpd...' -H 'Content-type:application/json' -d '{"id": 4, "name": "The Fellowship of The Ring", "authType": {"id": 1, "authType": "API_KEY"}, "apiKey": "1mrmfvmcHpdY6kRMUyAmDfBB8ZRMAUqTeemM8t7K"}'`


Which will return the following JSON showing that updates have occurred:
```
{
    "id": 4,
    "name": "The Fellowship of The Ring",
    "authType": {
        "id": 1,
        "authType": "API_KEY"
    },
    "apiKey": "1mrmfvmcHpdY6kRMUyAmDfBB8ZRMAUqTeemM8t7K",
    "_links": {
        "self": {
            "href": "http://localhost:8080/clients/4"
        },
        "clients": {
            "href": "http://localhost:8080/clients"
        }
    }
}
```
### Delete a client
To delete a client you can DELETE with the following:`curl -X DELETE localhost:8080/clients/4 -H 'API_KEY: 1mrmfvmcHpd...'`
## Keystore
### Get keystores 
To get all keystores for a client you can use the following:
`curl localhost:8080/clients/4/keystores -H 'API_KEY: 1mrmfvmcHpd...'`

Which will return the following JSON.
```
{
    "_embedded": {
        "keystoreList": [
            {
                "id": 6,
                "name": "Frodo Baggins",
                "client": {
                    "id": 4,
                    "name": "The Fellowship of The Ring"
                },
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/clients/4/keystores/6"
                    },
                    "keystores": {
                        "href": "http://localhost:8080/clients/4/keystores"
                    }
                }
            },
            {
                "id": 7,
                "name": "Samwise Gamgee",
                "client": {
                    "id": 4,
                    "name": "The Fellowship of The Ring"
                },
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/clients/4/keystores/7"
                    },
                    "keystores": {
                        "href": "http://localhost:8080/clients/4/keystores"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/clients/4/keystores"
        }
    }
}
```
### Get a specific keystore
To get a specific keystore you can use the following:
`curl localhost:8080/clients/4/keystore/6 -H 'API_KEY: 1mrmfvmcHpd...'`

Which will return the following JSON:
```
{
    "id": 6,
    "name": "Frodo Baggins",
    "client": {
        "id": 4,
        "name": "The Fellowship of The Ring"
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/clients/4/keystores/6"
        },
        "keystores": {
            "href": "http://localhost:8080/clients/4/keystores"
        }
    }
}
```
### Add a keystore
To add a new entry into the keystore you can POST the following: 
`curl -X POST localhost:8080/clients/4/keystore -H 'API_KEY: 1mrmfvmcHpd...' -H 'Content-type:application/json' -d '{"name": "Frodo", "client": {"id": 4, "name": "The Fellowship of The Ring", "authType": {"id": 1, "authType": "API_KEY"}, "apiKey": "1mrmfvmcHpdY6kRMUyAmDfBB8ZRMAUqTeemM8t7K"}}'`

Which will return the following JSON:
```
{
    "id": 6,
    "name": "Frodo",
    "client": {
        "id": 4,
        "name": "The Fellowship of The Ring"
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/clients/4/keystores/6"
        },
        "keystores": {
            "href": "http://localhost:8080/clients/4/keystores"
        }
    }
}
```
### Update a keystore
To update a specific keystore you can PUT the following:
`curl -X PUT localhost:8080/clients/4/keystore/6 -H 'API_KEY: 1mrmfvmcHpd...' -H 'Content-type:application/json' -d '{"name": "Frodo Baggins", "client": {"id": 4, "name": "The Fellowship of The Ring", "authType": {"id": 1, "authType": "API_KEY"}, "apiKey": "1mrmfvmcHpdY6kRMUyAmDfBB8ZRMAUqTeemM8t7K"}}'`

Which will return the following JSON:
```
{
    "id": 6,
    "name": "Frodo Baggins",
    "client": {
        "id": 4,
        "name": "The Fellowship of The Ring"
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/clients/4/keystores/6"
        },
        "keystores": {
            "href": "http://localhost:8080/clients/4/keystores"
        }
    }
}
```
### Delete a keystore
To delete a keystore you can DELETE with the following:`curl -X DELETE localhost:8080/clients/4/keystore/11 -H 'API_KEY: 1mrmfvmcHpd...'`
## Public Key
### Get all public keys
To get all public keys for a keystore you can use: `curl localhost:8080/clients/4/keystores/6/keys -H 'API_KEY: 1mrmfvmcHpd...'`

Which will return a JSON object like so:
```
{
    "_embedded": {
        "publicKeyList": [
            {
                "id": 8,
                "keystore": {
                    "id": 6,
                    "name": "Frodo Baggins",
                    "client": {
                        "id": 4,
                        "name": "The Fellowship of The Ring"
                    }
                },
                "publicKey": "MyPublicKey",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/clients/4/keystores/6/keys/8"
                    },
                    "keystores": {
                        "href": "http://localhost:8080/clients/4/keystores/6/keys"
                    }
                }
            },
            {
                "id": 9,
                "keystore": {
                    "id": 6,
                    "name": "Frodo Baggins",
                    "client": {
                        "id": 4,
                        "name": "The Fellowship of The Ring"
                    }
                },
                "publicKey": "SecondPublicKey",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/clients/4/keystores/6/keys/9"
                    },
                    "keystores": {
                        "href": "http://localhost:8080/clients/4/keystores/6/keys"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/clients/4/keystores/6/keys"
        }
    }
}
```
### Get a specific public key
To get a specific keystore you can use the following:
`curl localhost:8080/clients/4/keystores/6/keys/9 -H 'API_KEY: 1mrmfvmcHpd...'`

Which will return the following JSON:
```
{
    "id": 9,
    "keystore": {
        "id": 6,
        "name": "Frodo Baggins",
        "client": {
            "id": 4,
            "name": "The Fellowship of The Ring"
        }
    },
    "publicKey": "SecondPublicKey",
    "_links": {
        "self": {
            "href": "http://localhost:8080/clients/4/keystores/6/keys/9"
        },
        "keystores": {
            "href": "http://localhost:8080/clients/4/keystores/6/keys"
        }
    }
}
```
### Add a public key
To add a new public key you can POST the following: 
`curl -X POST localhost:8080/clients/4/keystores/6/keys -H 'API_KEY: 1mrmfvmcHpd...' -H 'Content-type:application/json' -d '{"publicKey": "MyPublicKey", "keystore": {"id": 6, "name": "Frodo Baggins", "client": {"id": 4, "name": "The Fellowship of The Ring", "authType": {"id": 1, "authType": "API_KEY"}, "apiKey": "1mrmfvmcHpdY6kRMUyAmDfBB8ZRMAUqTeemM8t7K"}}}'`

Which will return the following JSON:
```
{
    "id": 8,
    "keystore": {
        "id": 6,
        "name": "Frodo Baggins",
        "client": {
            "id": 4,
            "name": "The Fellowship of The Ring"
        }
    },
    "publicKey": "MyPublicKey",
    "_links": {
        "self": {
            "href": "http://localhost:8080/clients/4/keystores/6/keys/8"
        },
        "keystores": {
            "href": "http://localhost:8080/clients/4/keystores/6/keys"
        }
    }
}
```
### Update a public key
To update a specific public key you can PUT the following:
`curl -X PUT localhost:8080/clients/10/keystore/11/key/12 -H 'API_KEY: 1mrmfvmcHpd...' -H 'Content-type:application/json' -d '{"publicKey": "UpdateSecondPublicKey", "keystore": {"id": 6, "name": "Frodo Baggins", "client": {"id": 4, "name": "The Fellowship of The Ring", "authType": {"id": 1, "authType": "API_KEY"}, "apiKey": "1mrmfvmcHpdY6kRMUyAmDfBB8ZRMAUqTeemM8t7K"}}}'`

Which will return the following JSON:
```
{
    "id": 9,
    "keystore": {
        "id": 6,
        "name": "Frodo Baggins",
        "client": {
            "id": 4,
            "name": "The Fellowship of The Ring"
        }
    },
    "publicKey": "UpdateSecondPublicKey",
    "_links": {
        "self": {
            "href": "http://localhost:8080/clients/4/keystores/6/keys/9"
        },
        "keystores": {
            "href": "http://localhost:8080/clients/4/keystores/6/keys"
        }
    }
}
```
### Delete a public key
To delete a keystore you can DELETE with the following:`curl -X DELETE localhost:8080/clients/4/keystores/6/keys/9 -H 'API_KEY: 1mrmfvmcHpd...'`