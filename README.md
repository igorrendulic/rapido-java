# Rapido - Multipurpose Framework for Rapid Application Server Prototyping with Java 8

Deployment with Docker and Kubernetes on Google Cloud. [Blog post with more information](https://medium.com/p/3467275de025).

# Local Development Environment with Eclipse

```
git clone https://github.com/igorrendulic/rapido-java
```

- In Eclipse Import Maven Project.
- Debug As -> Java Application
- Open browser and go to: http://localhost:8080

# Deploying with Docker, Kuberenetes on Google Container Engine

Setup project variables first. Edit `configuration.sh` and change at least `RAPIDO_PROEJECT` variable to your projects name. 
In [Google Console](https://console.cloud.google.com/) Dashboard check Project Info card.

Configuration Variables:
- RAPIDO_MACHINE=g1-small (type of a machine: Shared-core machine type with 0.5 virtual CPU, 1.70 GB of memory)
- RAPIDO_PROJECT=myproject
- RAPIDO_CLUSTER=rapido (custom name of the cluster)
- RAPIDO_DISK=10 (10G disk space)
- RAPIDO_NUM_NODES=1 (number of machines we want to spin up)

More machine types [here](https://cloud.google.com/compute/docs/machine-types)

Run configuration file
```
source configuration.sh
```

## Build and deploy
Compiles Java project, builds Docker image and deploys it to Google Container Engine
```
make deployall
```

Expose our app to the world
```
make expose
```

Check what IP Google has assigned
```
make ip
```

Rebuild project, update docker image, redeploy to Google Container Engine
```
make update
```

### Convenient build commands

Building only a docker image
```
make docker
```

Switch to current Google Project
```
make setproject
```

See current Google Project and current Cluster
```
make list
```

Create new cluster
```
make newcluster
```

Switch to current project cluster
```
make setcluster
```

List all pods in a cluster
```
make podlist
```

See service and deployment IPs
```
make ip
```

Delete cluster
```
make deleteall
```


## CORS setup validation

```
curl -H "Origin: http://example.com" \
  -H "Access-Control-Request-Method: GET" \
  -H "Access-Control-Request-Headers: X-Requested-With" \
  -X OPTIONS --verbose \
  http://localhost:8080/api/ping?echo=corstest
```
