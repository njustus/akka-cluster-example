# Akka Cluster Example

Demo project for a simple akka-clustering app.

## Howto Run

## Lokal
```sh
PROVIDER=local gradle run
```

## Server
```sh
ROLE=SERVER;PORT=2552;SERVER=0.0.0.0:2552 gradle run
```

## Client
```sh
ROLE=PEER;PORT=2551;SERVER=0.0.0.0:2552 gradle run
```


## UI
```sh
ROLE=UI;PORT=2553;SERVER=0.0.0.0:2552 gradle run
```
