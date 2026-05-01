#!/bin/bash

# Script para iniciar servicios individuales
# Uso: ./run_service.sh [eureka|productos|items|zuul]

SERVICE=$1
PROJECT_DIR="./1_springboot-servicio[ribbon]"

if [ -z "$SERVICE" ]; then
    echo "Uso: $0 [eureka|productos|productos2|items|items2|zuul]"
    echo ""
    echo "Ejemplos:"
    echo "  $0 eureka     - Iniciar Eureka Server (puerto 8761)"
    echo "  $0 productos  - Iniciar Servicio Productos (puerto 9001)"
    echo "  $0 productos2 - Iniciar Servicio Productos (puerto 9002)"
    echo "  $0 items      - Iniciar Servicio Items (puerto 8002)"
    echo "  $0 items2     - Iniciar Servicio Items (puerto 8003)"
    echo "  $0 zuul       - Iniciar Zuul Gateway (puerto 8080)"
    exit 1
fi

case $SERVICE in
    eureka)
        echo "▶ Iniciando Eureka Server en puerto 8761..."
        cd "$PROJECT_DIR/springboot-eureka-server"
        mvn spring-boot:run
        ;;
    productos)
        echo "▶ Iniciando Servicio Productos en puerto 9001..."
        cd "$PROJECT_DIR/springboot-servicio-productos"
        mvn spring-boot:run
        ;;
    productos2)
        echo "▶ Iniciando Servicio Productos en puerto 9002..."
        cd "$PROJECT_DIR/springboot-servicio-productos"
        mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=9002
        ;;
    items)
        echo "▶ Iniciando Servicio Items en puerto 8002..."
        cd "$PROJECT_DIR/springboot-servicio-item"
        mvn spring-boot:run
        ;;
    items2)
        echo "▶ Iniciando Servicio Items en puerto 8003..."
        cd "$PROJECT_DIR/springboot-servicio-item"
        mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8003
        ;;
    zuul)
        echo "▶ Iniciando Zuul Gateway en puerto 8090..."
        cd "$PROJECT_DIR/springboot-servicio-zuul-server"
        mvn spring-boot:run
        ;;
    *)
        echo "Error: Servicio desconocido '$SERVICE'"
        echo "Servicios disponibles: eureka, productos, items, zuul"
        exit 1
        ;;
esac
