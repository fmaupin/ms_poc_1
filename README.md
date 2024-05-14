## projet ms-poc-1

L'objectif de ce projet est de développer un micro service (thématique gestion des hiéroglyphes) communiquant avec un broker de messages.

Aucune IHM ne sera développée.

Les données seront injectées manuellement dans le broker de messages (via sa [console Web](http://localhost:15672)) puis consommées par le micro service.

Le format des données à injecter : "[code Gardiner] + [code Gardiner] + ..."

Les codes Gardiner (Sir Gardiner) représentent la nomenclature des hiéroglyphiques.

La thématique du micro-service est la détection de **phonogrammes et de ses compléments phonétiques** dans une séquence hiéroglyphique.

Pour plus d'informations sur le paramètrage du broker veuillez consulter le projet [ms_poc_1_rabbitMQ](https://github.com/fmaupin/ms_poc_1_rabbitMQ).

***

### Pré-requis

Installation du [JDK Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).

Exécution du broker

***

### Exécution, test & install app

```
mvn spring-boot:run -Dspring-boot.run.arguments="--my.password=<my password>"
```

```
<<<<<<< Updated upstream
mvn test -Dmy.password=<my password>
=======
mvn jacoco:prepare-agent test -Dmy.password=<my password> install jacoco:report
>>>>>>> Stashed changes

mvn clean install -Dmy.password=<my password>
```

La valeur de "my password" va dépendre du password généré par le projet [ms_poc_1_rabbitMQ](https://github.com/fmaupin/ms_poc_1_rabbitMQ). 

***

### Auteur

Ce projet a été créé par Fabrice MAUPIN.

***

### License

GNU General Public License v3.0

See [LICENSE](https://github.com/fmaupin/ms_poc_1/blob/master/LICENSE) to see the full text.



