# Documentación Técnica - Eureka + Hystrix

## Cambios realizados

### 1. Nuevo módulo: springboot-eureka-server

**Ubicación**: `1_springboot-servicio[ribbon]/springboot-eureka-server/`

**Archivos principales**:
- `pom.xml`: Configuración Maven con dependencia de Eureka Server
- `SpringbootEurekaServerApplication.java`: Clase principal con anotación `@EnableEurekaServer`
- `application.properties`: Configuración del servidor Eureka

**Configuración**:
```properties
spring.application.name=eureka-server
server.port=8761
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

---

### 2. Modificaciones a springboot-servicio-item

#### A) pom.xml
Se agregaron dependencias:
- `spring-cloud-starter-netflix-eureka-client`
- `spring-cloud-starter-netflix-hystrix`

#### B) SpringbootServicioItemApplication.java
Se agregaron anotaciones:
```java
@EnableCircuitBreaker  // Activa Hystrix
@EnableEurekaClient     // Registra el servicio en Eureka
```

#### C) application.properties
Se agregó:
```properties
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.instance.hostname=localhost
feign.hystrix.enabled=true
hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds=5000
```

#### D) ProductoClienteRest.java (interface Feign)
Cambio importante:
```java
@FeignClient(name = "servicio-productos", fallback = ProductoClienteRestFallback.class)
```

Antes usaba Ribbon puro, ahora usa Hystrix con fallback.

#### E) Nueva clase: ProductoClienteRestFallback.java
Implementa la interfaz ProductoClienteRest con métodos fallback:

```java
@Component
public class ProductoClienteRestFallback implements ProductoClienteRest {
    
    @Override
    public List<Producto> listar() {
        return new ArrayList<>();  // Lista vacía
    }
    
    @Override
    public Producto detalle(Long id) {
        // Retorna producto con datos predeterminados
        Producto producto = new Producto();
        producto.setMarca("No disponible");
        producto.setModelo("Serv. no disponible");
        return producto;
    }
    
    @Override
    public void eliminarProducto(Long id) {
        // Simplemente registra
    }
}
```

---

### 3. Modificaciones a springboot-servicio-productos

#### A) pom.xml
Se agregó:
- Spring Cloud version: Hoxton.SR12
- `spring-cloud-starter-netflix-eureka-client`

#### B) SpringbootServicioProductosApplication.java
Se agregó anotación:
```java
@EnableEurekaClient
```

#### C) application.properties
Se agregó:
```properties
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.instance.hostname=localhost
```

---

## Flujo de comunicación

1. **Inicio**: Eureka Server inicia en puerto 8761
2. **Registro**: Ambos servicios se registran en Eureka
3. **Discovery**: El servicio de Items descubre a Productos a través de Eureka
4. **Llamada normal**: Items → Products (funciona)
5. **Fallo**: Si Products no responde:
   - Hystrix detecta el fallo
   - Activa el circuit breaker
   - Ejecuta el método fallback
   - Retorna datos alternativos o vacíos

## Ventajas implementadas

| Característica | Beneficio |
|---|---|
| **Eureka Discovery** | Sin URLs hardcodeadas, fácil escalabilidad |
| **Hystrix Circuit Breaker** | Previene cascadas de fallos |
| **Fallback Strategy** | Degradación elegante del servicio |
| **Ribbon (existing)** | Load balancing automático |
| **Feign Client** | Comunicación REST simplificada |

---

## Testing del sistema

### Prueba 1: Servicios normales
```bash
curl http://localhost:8002/listar
# Retorna lista de items desde servicio-productos
```

### Prueba 2: Fallo de productos
1. Detener servicio de productos
2. `curl http://localhost:8002/listar`
3. Resultado: Lista vacía (fallback activo)
4. Consola: "Fallback ejecutado: listar()"

### Prueba 3: Check de Eureka
```
http://localhost:8761/
```
Muestra estado de los servicios registrados.

---

## Configuración Hystrix explicada

```properties
feign.hystrix.enabled=true
# Habilita Hystrix para clientes Feign

hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds=5000
# Timeout máximo de 5 segundos por comando

# Otros parámetros disponibles (opcional):
# hystrix.command.default.circuitBreaker.requestVolumeThreshold=20
# hystrix.command.default.circuitBreaker.sleepWindowInMilliseconds=5000
# hystrix.command.default.circuitBreaker.errorThresholdPercentage=50
```

---

## Próximas mejoras posibles

1. Agregar métricas con Hystrix Dashboard
2. Implementar retry logic
3. Agregar logging detallado
4. Configurar zuul-gateway para API Gateway
5. Implementar authentication/authorization
6. Agregar health checks
