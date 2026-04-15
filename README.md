CRÉATION DU PROJET

Créer le projet Spring Boot avec Spring Initializr ou IntelliJ (voir le projet fourni)

Vérifier que les dépendances suivantes sont ajoutées :

Spring Web
Spring Data JPA
Spring Security
PostgreSQL Driver
Redis
Kafka
Swagger (springdoc)
JWT

CONFIGURATION DE LA BASE DE DONNÉES (PostgreSQL)

Créer la base de données PostgreSQL
Configurer le fichier application.yaml (voir le fichier application.yaml)
CONFIGURATION DE REDIS (CACHE)
Installer Redis (Docker)
Démarrer Redis :

<img width="846" height="43" alt="Capture d&#39;écran 2026-04-15 132845" src="https://github.com/user-attachments/assets/d53e2125-4066-42b6-a8cd-e69f191a58ca" />

Configurer dans application.yaml (voir le fichier)

CONFIGURATION DE KAFKA
Installer Kafka ou utiliser Docker
Démarrer Kafka :

<img width="878" height="53" alt="Capture d&#39;écran 2026-04-15 133033" src="https://github.com/user-attachments/assets/3c8a75ba-70e1-4aec-b2dd-b351bb1447a1" />

<img width="874" height="50" alt="Capture d&#39;écran 2026-04-15 133041" src="https://github.com/user-attachments/assets/a253acf9-7a5f-418a-b07c-44acd5bea098" />

Configurer dans application.yaml (Voir le fichier)

voici une apercu du fichier application.yaml

<img width="737" height="875" alt="Capture d&#39;écran 2026-04-15 225332" src="https://github.com/user-attachments/assets/3746762b-396b-4c6b-907e-e9e197a9ea9b" />

CONFIGURATION DE SWAGGER

Swagger est accessible via : http://localhost:8085/swagger-ui.html

Documentation API : http://localhost:8085/v3/api-docs

LANCEMENT DE L’APPLICATION

Compiler le projet : mvn clean package

Lancer le projet : mvn spring-boot:run

TEST DE L’AUTHENTIFICATION



