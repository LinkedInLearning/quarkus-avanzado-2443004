                     ## Minikube en Mac
Instalar Virtual box es lo màs recomendado.

## Arrancar Minikube
```shell
minikube start --driver=virtualbox --cpus 4 --memory "8192mb" 
```
## Limpiar Kubernetes (Minikube)
```shell
eval $(minikube -p minikube docker-env)
kubectl delete service postgres    
kubectl delete service postgres-sales-service    
kubectl delete deployment postgres
kubectl delete deployment postgres-sales-service
kubectl delete service product-inventory-service      
kubectl delete deployment product-inventory-service
kubectl delete service sales-service      
kubectl delete deployment sales-service
kubectl delete service order-service      
kubectl delete deployment order-service
docker rmi (imageId)
kubectl delete configmap kineteco
kubectl delete secret kineteco-credentials
kubectl delete namespace kafka

# delete all pods
kubectl delete --all pods --namespace=default
kubectl delete --all pods --namespace=kafka


# delete all deployments
kubectl delete --all deployments --namespace=default
kubectl delete --all deployments --namespace=kafka
 
 # delete all services
kubectl delete --all services --namespace=default
kubectl delete --all services --namespace=kafka
```

## Creacion contenedores

* Crear secretros y configmap

```shell
kubectl create configmap kineteco
kubectl create secret generic kineteco-credentials --from-literal=username=kineteco --from-literal=password=kineteco --from-literal=infinispan-username=admin --from-literal=infinispan-password=secret

kubectl get configmaps
kubectl get configmaps kineteco -o yaml     
kubectl get secrets
```
* Crear Cluster Kafka
```shell

* Kafka

```shell
kubectl create namespace kafka
# creamos el operador
kubectl apply -f 'strimzi-cluster-operator-0.25.0.yaml' -n kafka
#creamos el cluster
kubectl apply -f kafka_cluster.yml -n kafka

#Esperamos kafka 
kubectl wait kafka/kineteco-cluster --for=condition=Ready --timeout=300s -n kafka

# Creamos los topics
kubectl apply -f kafka_topics.yml -n kafka
```
* Creamos las dos bases de datos

```shell
kubectl apply -f postgres.yaml
kubectl apply -f postgres-sales-service.yaml
```

* Install el Infinispan Operator e Infinispan
```shell
curl -sL https://github.com/operator-framework/operator-lifecycle-manager/releases/download/v0.19.1/install.sh | bash -s v0.19.1
kubectl create -f https://operatorhub.io/install/infinispan.yaml
kubectl get csv -n olm
kubectl create secret generic --from-file=identities.yaml connect-secret
kubectl apply -f infinispan.yaml   
```


* Deploy de las aplicaciones
```shell
cd product-inventory
./mvnw clean package -Dquarkus.kubernetes.deploy=true -DskipTests=true

cd sales-service
./mvnw clean package -Dquarkus.kubernetes.deploy=true -DskipTests=true

cd order-service
./mvnw clean package -Dquarkus.kubernetes.deploy=true -DskipTests=true
```
