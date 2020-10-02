# Welcome to Quarkiverse!

Congratulations and thank you for creating a new Quarkus extension project in Quarkiverse!

Feel free to replace this content with the proper description of your new project and necessary instructions how to use and contribute to it.

You can find the basic info, Quarkiverse policies and conventions in [the Quarkiverse wiki](https://github.com/quarkiverse/quarkiverse/wiki).

Need to quickly create a new Quarkus extension Maven project? Just execute the command below replacing the template values with your preferred ones:
```
mvn io.quarkus:quarkus-maven-plugin:<QUARKUS_VERSION>:create-extension -N \
    -DgroupId=<EXTENSION_GROUP_ID> \ 
    -DartifactId=<EXTENSION_ARTIFACT_ID> \  
    -Dversion=<INITIAL_VERSION> \ 
    -Dquarkus.nameBase="<EXTENSION_SIMPLE_NAME>"
```

In case you are creating a Quarkus extension project for the first time, please follow [Building My First Extension](https://quarkus.io/guides/building-my-first-extension) guide.

Other useful articles related to Quarkus extension development can be found under the [Writing Extensions](https://quarkus.io/guides/#writing-extensions) guide category on the [Quarkus.io](http://quarkus.io) website.

Thanks again, good luck and have fun!
