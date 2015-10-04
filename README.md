#JTX Timeline Maker

Ceci est la version 1.0 du JTX Timeline Maker, destiné à faciliter la construction des timelines des projs.

Son rôle est de numéroter les clips et d'intercaler des clips de "noir" entre chaque clip, en fonction d'un **fichier de playlist VLC (.xspf)**. Sa prise en main est très intuitive : il suffit de lui indiquer l'emplacement du fichier de playlist et l'emplacement du clip de noir. 

###Fonctionnement

Pour cela, on peut soit double cliquer sur "JTX Timeline Maker.jar" pour lancer l'interface graphique, soit, et c'est bien plus çordide, utiliser l'invite de commande de la façon suivante (ce qui a l'avantage de montrer les erreurs) :

```java -jar "JTX Timeline Maker.jar" chemin\vers\timeline.xspf chemin\vers\noir.mp4```

On peut aussi lancer l'interface graphique depuis l'invite de commande (et ainsi voir les erreurs) en ne précisant aucun paramètre :

```java -jar "JTX Timeline Maker.jar"``

###Remarques d'utilisation

* Il ne devrait pas y avoir de bug mais ça peut sûrement arriver, mieux vaut donc vérifier les messages d'erreurs de la console. 
* Le logiciel est néanmoins conçu pour pouvoir être lancé plusieurs fois à la suite sans poser de problèmes. Le logiciel retrouvera en effet les clips, même s'ils sont précédés d'un préfixe (ex: 024a_) absent ou différent de celui indiqué dans la playlist.
* Si des sous-titres ou des fichiers de noir sont insérés dans la playlist, ils seront ignorés. 
* Les sous-titres (.srt seulement) seront renommés, s'ils existent, en même temps que le clip auquel ils appartiennent.
* Pour être ignorés, les fichiers de noir doivent porter (sans compter le préfixe) le même nom que le fichier de noir indiqué au logiciel.

###Configuration requise

Il nécessite d'avoir Java (JRE > 1.7 a priori) d'installé sur l'ordinateur pour être utilisé.

*version du 04/10/2015 par Côme Weber.* 