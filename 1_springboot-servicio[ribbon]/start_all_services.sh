#!/bin/bash

# Script para ejecutar todos los servicios en el orden correcto
# Este script abre terminales separadas para cada servicio

set -e

PROJECT_DIR="/home/eyaelcr/Lic.Ciencias/Cloud/Tarea4/Tarea2-Nube/1_springboot-servicio[ribbon]"

echo "=========================================="
echo "Iniciando Servicios - Zuul Gateway Demo"
echo "=========================================="
echo ""

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Función para esperar a que un servicio esté listo
wait_for_service() {
    local port=$1
    local name=$2
    local max_attempts=30
    local attempt=0
    
    echo -e "${BLUE}Esperando a que ${name} esté listo en puerto ${port}...${NC}"
    
    while [ $attempt -lt $max_attempts ]; do
        if nc -z localhost $port 2>/dev/null; then
            echo -e "${GREEN}✓ ${name} está listo en puerto ${port}${NC}"
            return 0
        fi
        echo -n "."
        sleep 1
        ((attempt++))
    done
    
    echo -e "${RED}✗ ${name} no está listo (timeout)${NC}"
    return 1
}

# Compilar si es necesario
echo -e "${YELLOW}Compilando servicios...${NC}"

echo "Compilando Eureka Server..."
cd "$PROJECT_DIR/springboot-eureka-server"
mvn clean package -DskipTests -q

echo "Compilando Servicio Productos..."
cd "$PROJECT_DIR/springboot-servicio-productos"
mvn clean package -DskipTests -q

echo "Compilando Servicio Items..."
cd "$PROJECT_DIR/springboot-servicio-item"
mvn clean package -DskipTests -q

echo "Compilando Zuul Gateway..."
cd "$PROJECT_DIR/springboot-servicio-zuul-server"
mvn clean package -DskipTests -q

echo -e "${GREEN}✓ Compilación completada${NC}"
echo ""

# Iniciar Eureka Server
echo -e "${YELLOW}1/4 Iniciando Eureka Server (puerto 8761)...${NC}"
cd "$PROJECT_DIR/springboot-eureka-server"
java -jar target/springboot-eureka-server-0.0.1-SNAPSHOT.jar &
EUREKA_PID=$!
echo "PID: $EUREKA_PID"
wait_for_service 8761 "Eureka Server" || exit 1

sleep 2

# Iniciar Servicio Productos
echo -e "${YELLOW}2/4 Iniciando Servicio Productos (puerto 9001)...${NC}"
cd "$PROJECT_DIR/springboot-servicio-productos"
java -jar target/springboot-servicio-productos-0.0.1-SNAPSHOT.jar &
PRODUCTOS_PID=$!
echo "PID: $PRODUCTOS_PID"
wait_for_service 9001 "Servicio Productos" || exit 1

# Iniciar Servicio Items
echo -e "${YELLOW}3/4 Iniciando Servicio Items (puerto 8002)...${NC}"
cd "$PROJECT_DIR/springboot-servicio-item"
java -jar target/springboot-servicio-item-0.0.1-SNAPSHOT.jar &
ITEMS_PID=$!
echo "PID: $ITEMS_PID"
wait_for_service 8002 "Servicio Items" || exit 1

# Iniciar Zuul Gateway
echo -e "${YELLOW}4/4 Iniciando Zuul Gateway (puerto 8090)...${NC}"
cd "$PROJECT_DIR/springboot-servicio-zuul-server"
java -jar target/springboot-servicio-zuul-server-0.0.1-SNAPSHOT.jar &
ZUUL_PID=$!
echo "PID: $ZUUL_PID"
wait_for_service 8090 "Zuul Gateway" || exit 1

echo ""
echo -e "${GREEN}=========================================="
echo "✓ Todos los servicios están activos"
echo "==========================================${NC}"
echo ""
echo "Servicios disponibles:"
echo -e "  ${BLUE}Eureka Server${NC}:        http://localhost:8761/"
echo -e "  ${BLUE}Zuul Gateway${NC}:        http://localhost:8090/"
echo -e "  ${BLUE}Products API${NC}:        http://localhost:9001/listar"
echo -e "  ${BLUE}Items API${NC}:           http://localhost:8002/listar"
echo ""
echo "Endpoints de prueba del Gateway:"
echo -e "  ${BLUE}Productos${NC}:      http://localhost:8090/api/productos/listar"
echo -e "  ${BLUE}Items${NC}:          http://localhost:8090/api/items/listar"
echo -e "  ${BLUE}Producto detalle${NC}: http://localhost:8090/api/productos/ver/1"
echo -e "  ${BLUE}Item detalle${NC}:     http://localhost:8090/api/items/ver/1/cantidad/1"
echo ""
echo -e "${YELLOW}Para detener los servicios, usa: Ctrl+C${NC}"
echo ""

# Esperar a señal de término
trap "echo 'Terminando servicios...'; kill $EUREKA_PID $PRODUCTOS_PID $ITEMS_PID $ZUUL_PID 2>/dev/null; exit 0" INT TERM

wait
