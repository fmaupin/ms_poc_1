## projet ms-poc-1

L'objectif de ce projet est de pouvoir développer un micro service communiquant avec un broker de messages.

Aucune IHM ne sera développée.

Les données seront injectées manuellement dans le broker de messages puis consommées par le micro service.

La thématique du micro-service est la détection de **phonogrammes** dans une séquence hiéroglyphique.

***

### Pré-requis

Installation de [JDK_Java](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).

***

### Exécution app

```
mvn spring-boot:run -Dspring-boot.run.arguments="--my.password=<my password>"
```

La valeur de <my password> va dépendre du password généré par le projet [ms_poc_1_rabbitMQ](https://github.com/fmaupin/ms_poc_1_rabbitMQ). 

***

### Auteur

Ce projet a été créé par Fabrice MAUPIN.

***

### License

GNU General Public License v3.0

See [LICENSE](https://github.com/fmaupin/ms_poc_1/blob/master/LICENSE) to see the full text.



