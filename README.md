# quarkus defensa

## 1. Crea los contenedores base del proyecto y realiza las modificaciones necesarias para ejecutar el proyecto.

PREPARAMOS jaeger, consul y la bd postgres

Ejecutamos los contenedores jaeger, consul, postgres
```
cd containers/base
docker-compose up -d
```

Comandos para ejecutar contenedores, cuando se inicia el equipo nuevamente.
```
docker start monster-db
docker start jaeger-co
docker start consul-co
```

## 2. Utiliza el api Gateway Kong para acceder  a las api del proyecto car y shop agrega los plugins de CORS y Cache.

PRERAMOS para el apigateway

Creamos la red para los contenedores
```
docker network create kong-net
```

Inspeccionamos si esta usando la red que creamos
```
docker inspect kong-net
```

Ejecutamos los contenedores
```
cd containers/api-gateway
docker-compose up -d
```

Credenciales para ingresar al administrador de kong en konga: http://localhost:1337
 - username: admin
 - password: p@ssw0rd

Comandos para ejecutar contenedores, cuando se inicia el equipo nuevamente.
```
docker start kong-database
docker start konga
docker start kong
```

## 3. Crea el contenedor para ejecutar el OIDC basado en keycloak y modifica el proyecto para que utilice el rol de autorización “user” para acceder a la apis del proyecto Maven car.

Creamos el contendor para keycloak
```
docker run -d -p 8181:8080 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin --name oidc jboss/keycloak
```

Vamos a la direccion: 192.168.0.9:8181/auth, con las siguientes credenciales para la Administration Console
 - username: admin
 - password: admin

Creamos un realm (Add realm), podemos crear de cero o importar, en nuestro  caso importamos (quarkus-realm.json).

Ingresamos a la direccion 192.168.0.9:8088/q/swagger-ui/, para hacer las pruebas correspondientes.

Para la autorizacion para el token ingresamos con algun usuario ejemplo:
 - username: alice 
 - password: alice

Para el uso del servicio ingresamos
 - client_id: frontend-service
 - secret_id: secret

Comandos para ejecutar contenedores, cuando se inicia el equipo nuevamente.
```
docker start oidc
```

## 4. Crea los contenedores de monitoreo para monitorear los contenedores docker con Grafana y prometheus.

Buscamos donde esta nuestro archivo daemon.json
```
$HOME/.docker/daemon.json
```

Configuramos el archivo, para que pueda ser monitoreado
```
nano /home/borisv/.docker/daemon.json
```
```
{
    "metrics-addr": "0.0.0.0:9323",
    "experimental": true
}
```

Ejecutamos los contenedos para prometheus y grafana:
```
cd containers/monitoring
docker-compose up -d
```

Si tenemos que parar gitlab porque cadvisor ocupa el puerto 8080
```
sudo gitlab-ctl stop
```

Nos dirigimos a la direccion, para el dashboard de prometheus: http://192.168.0.9:9090


Las metricas estan en la direccion: http://192.168.0.9:9323/metrics

Abrimos la interfaz de grafana: http://192.168.0.9:3000 con las credenciales para abrir el dashboard de grafana.
 - username: admin
 - password: password

Despues creamos datasource y dashboard para importar (docker-quest-prometheus.json)

Comandos para ejecutar contenedores, cuando se inicia el equipo nuevamente.
```
docker start prometheus
docker start grafana
docker start node-exporter
docker start pushgateway
docker start cadvisor
```