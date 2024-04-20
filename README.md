# Simple Mail Server

A simple mail server with API for user management.

## Usage:
### Requirements:
- Docker and Docker Compose must be available on the machine (https://docs.docker.com/compose/install)

### Installation
1. Download and unzip the Mail Server Release (https://github.com/JPula/simple-mail-server/releases/download/1.0.0/mail-server.zip)
2. Open a Terminal and Navigate to Mail Server Directory.
```
$ cd <path/to/mail/server/directory> 
```
3. Load the Mail Server TAR as docker Image
```
$ docker load -i mail-service-1.0.0.tar
```
4. Spin up Mail Server and Mail Catcher Containers
```
$ docker compose up
```

After installation, the Mail Server UI and Mail Catcher UI should be available.

### Uninstallation
Shutdown Mail Server and Mail Catcher Containers:
```
$ docker compose down
```


### Mail Server UI/API
The Mail Server API is documented at 
http://localhost:8080/swagger-ui.html

APIs can be run directly from the swagger page. Simply click the API item from the UI, and click "Try it out!".



### Mail Catcher UI (SMTP Server)
Emails sent by the Mail Server to the SMTP server can be viewed at http://localhost:1080

