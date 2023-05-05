#!/bin/bash

echo "===== DEBUT DU DEPLOIEMENT ====="

# Récupérer la dernière version du code
echo "Récupération de la dernière version du projet"
git pull

# Construire le projet avec Maven
echo "Packaging de l'application en WAR"
./mvnw package -P prod -e

# Constuire l'image et redeployer le container
echo "Constuction de l'image Docker et déploiement des containers"
docker-compose up -d --build

echo "===== FIN DU DEPLOIEMENT ====="
