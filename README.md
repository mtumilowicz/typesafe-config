# typesafe-config
The main goal of this project is to explore basic features of 
`com.typesafe:config`.

_Reference_: https://github.com/lightbend/config#overview
## project description
`com.typesafe:config` is a type-safe configuration library for
JVM languages.  
All we need to do to start using `com.typesafe:config` is:  
* add dependency
```
<dependency>
    <groupId>com.typesafe</groupId>
    <artifactId>config</artifactId>
    <version>1.3.3</version>
</dependency>
```
* add `application.conf` to `resources`  
* get `Config` class
```
Config conf = ConfigFactory.load();
```

## project content
* `test/resources/application.conf` - file with most popular 
use-cases. It's written in [HOCON](https://github.com/lightbend/config#features-of-hocon).  
* `test/java/ConfigTest` - we test if all lines from 
`application.conf` file was loaded successfully.  
* `test/java/Author` - util class for mapping specific entry 
in `application.conf` to the `JavaBean`.

## use cases
Assumptions:  
* `Config conf = ConfigFactory.load();`
* `application.conf` has structure:
```
predefined {
    ...
}

conf {
    ...
}
```
* basic example
```
conf {
    project_name = typesafe-config
}
```
```
conf.getString("conf.project_name") // typesafe-config
```
* substitutions (way of eliminating copy-paste)
```
predefined {
    version : 1.0-SNAPSHOT
}
conf {
    project_version : ${predefined.version}
    artifact_version : ${predefined.version}
}
```
```
conf.getString("conf.project_version") // 1.0-SNAPSHOT
conf.getString("conf.artifact_version") // 1.0-SNAPSHOT
```
* handling `JSON` objects
```
author : {name : michal, surname : tumilowicz}
```
```
ConfigObject author = conf.getObject("conf.author");
Config asConfig = author.toConfig(); // treat is as a Config
asConfig.getString("name"); // michal
asConfig.getString("surname"); // tumilowicz
```
* mapping `JSON` to `JavaBean`  
If you have a `Java` object that follows `JavaBean` conventions 
(zero-args constructor, getters and setters), you can 
automatically initialize it from a `Config`.
```
author : {name : michal, surname : tumilowicz}
```
```
Author author = ConfigBeanFactory.create(conf.getObject("conf.author").toConfig(), Author.class);
author.getName(); // michal
author.getSurname(); // tumilowicz
```
* merging  
    1. Values on the same line are concatenated (for strings and arrays) 
    or merged (for objects).
    1. If you duplicate a field with an object value, then the objects 
    are merged with last-one-wins.
```
persistence : {specification: JPA, provider : Hibernate, cache : true}
persistence : {provider : EclipseLink, cache : false, database : Oracle}
```
it's merged to:
```
persistence : {specification: JPA, provider : EclipseLink, cache : false, database : Oracle}
```
* merging + substitution  
Merging is especially useful with substitutions
```
headquarters : {name : "mtumilowicz holding"}
branch_east : ${predefined.headquarters} {branch_name : east}
```
it's merged to:
```
headquarters : {name : "mtumilowicz holding", branch_name : east}
```
