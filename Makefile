# Utility commands for deployment

ifeq ($(RAPIDO_PROJECT),)
  $(error RAPIDO_PROJECT is not set)
endif

ifeq ($(RAPIDO_MACHINE),)
  $(error RAPIDO_MACHINE is not set)
endif

ifeq ($(RAPIDO_CLUSTER),)
  $(error RAPIDO_CLUSTER is not set)
endif

ifeq ($(RAPIDO_DISK),)
  $(error RAPIDO_DISK is not set)
endif

ifeq ($(RAPIDO_NUM_NODES),)
  $(error RAPIDO_NUM_NODES is not set)
endif

# builds the project and pushes it to Googles registry
docker:
	docker build -t rapido .
	docker tag rapido gcr.io/zivorad-144201/rapido:latest
	gcloud docker -- push gcr.io/zivorad-144201/rapido:latest
	
# Sets the project
setproject:
	gcloud config set project $(RAPIDO_PROJECT)

# list project configuration
list:
	gcloud config list
	
# create new cluster
newcluster:
	gcloud container clusters create $(RAPIDO_CLUSTER) --machine-type=$(RAPIDO_MACHINE) --disk-size=$(RAPIDO_DISK) --num-nodes=$(RAPIDO_NUM_NODES)

# set cluster credentials
setcluster:
	gcloud config set container/cluster $(RAPIDO_CLUSTER)
	gcloud container clusters get-credentials $(RAPIDO_CLUSTER)
	
# first deploy
deploy:
	kubectl create -f 10deployment.yaml

# update existing deploy
update:
	mvn clean install
	docker build -t rapido .
	docker tag rapido gcr.io/$(RAPIDO_PROJECT)/rapido:latest
	gcloud docker -- push gcr.io/$(RAPIDO_PROJECT)/rapido:latest
	cat 10deployment.yaml | sed -e 's/\(gcr.io\/\).*\(\/rapido\)/\1'$(RAPIDO_PROJECT)'\2/g' | kubectl apply -f -

	
# list all pods
podlist:
	kubectl get pods
	
ip:
	kubectl get service rapido-service

# remote the cluster
deleteall:
	gcloud container clusters delete rapido
	
#expose a service to the internet
expose:
	kubectl expose deployment rapido --type=LoadBalancer --name=rapido-service
	
# building and deploying everything at once
deployall:
	mvn clean install
	gcloud config set project $(RAPIDO_PROJECT)
	gcloud config list
	gcloud container clusters create $(RAPIDO_CLUSTER) --machine-type=$(RAPIDO_MACHINE) --disk-size=$(RAPIDO_DISK) --num-nodes=$(RAPIDO_NUM_NODES)
	gcloud config set container/cluster $(RAPIDO_CLUSTER)
	gcloud container clusters get-credentials $(RAPIDO_CLUSTER)
	docker build -t rapido .
	docker tag rapido gcr.io/$(RAPIDO_PROJECT)/rapido:latest
	gcloud docker -- push gcr.io/$(RAPIDO_PROJECT)/rapido:latest
	cat 10deployment.yaml | sed -e 's/\(gcr.io\/\).*\(\/rapido\)/\1'$(RAPIDO_PROJECT)'\2/g' | kubectl create -f -
	