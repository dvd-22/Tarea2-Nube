# Tarea Computación en la Nube

Equipo:

- David Alemán López
- Edgar Yael Cano Rodríguez

## Ejecución

1. Para ejecutar el proyecto, primero debes clonar el repositorio en tu máquina local. Puedes hacerlo utilizando el siguiente comando en tu terminal:

```bash
git clone https://github.com/dvd-22/Tarea2-Nube.git
```

1. Después de clonar el repositorio, hay que abrirlo con Eclipse Spring Tool Suite (STS) o cualquier IDE compatible con Spring Boot.

2. Dentro de Spring Tools, seleccionamos los 3 servicios y damos a correr para cada uno en el botón de hasta arriba
Los servicios que deben iniciarse son:

- springboot-eureka-server [:8761]
- springboot-servicio-item [:8002]
- springboot-servicio-productos [:9001]

Podemos acceder a cada servicio a través de los siguientes enlaces:

servicio-productos

- <http://localhost:9001/listar>
- <http://localhost:9001/ver/id/>
- <http://localhost:9001/eliminar/id/>

servicio-item

- <http://localhost:8002/listar>
- <http://localhost:8002/ver/id/cantidad/num>
- <http://localhost:8002/eliminar/id/>

Por alguna razón, a veces tarda un ratillo en iniciar bien.
