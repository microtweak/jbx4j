# Jbx4j - JSON Binding eXtension for JPA

[![][maven img]][maven]
[![][javadoc img]][javadoc]
[![][release img]][release]
[![][license img]][license]

[maven]:http://search.maven.org/#search|gav|1|g:"com.github.microtweak"%20AND%20a:"jbx4j"
[maven img]:https://maven-badges.herokuapp.com/maven-central/com.github.microtweak/jbx4j/badge.svg

[javadoc]:https://javadoc.io/doc/com.github.microtweak/jbx4j-core
[javadoc img]:https://javadoc.io/badge/com.github.microtweak/jbx4j-core.svg

[release]:https://github.com/microtweak/jbx4j/releases
[release img]:https://img.shields.io/github/release/microtweak/jbx4j.svg

[license]:LICENSE
[license img]:https://img.shields.io/badge/License-MIT-yellow.svg

A helper library for JPA Entity (de)serialization in JSON.

When we work with JPA and need to (de)serialize JPA entities to JSON (or any other format) we always encounter some problems:

* Relationship attributes (OneToOne, ManyToOne, OneToMany and ManyToMany) that are Lazy and the JSON Binding framework (like Jackson, GSON, etc) will cause the JPA Provider to make unnecessary queries to the database;
* Circular reference between JPA mapping;
* Use of DTOs and Mapping frameworks (like ModelMapper, MapStruct, Dozer, etc.) to bypass the above problems, making development more complex and verbose;
* Need to create DTOs and Mappers even for simple entities (with two or three attributes).

This library helps in the process of (de)serialization of the JPA entity, reducing the difficulties mentioned above.

## Support
Initially we are providing support to the framework below:

Framework       | Type | Minimal version | Note
----------------|:----:|----------------:|--------------------------------------------------------|
 Hibernate      | JPA  | 5.2             |                                                        |
 EclipseLink    | JPA  | 2.5             | Works only with Weaving enabled. Experimental support  |
 Jackson        | JSON | 2.9.4           |                                                        |

## Getting Started

1. You must add a dependency in your project:
    ```xml
    <dependency>
        <groupId>com.github.microtweak</groupId>
        <artifactId>jbx4j-hibernate</artifactId>
        <version>1.0.0</version>
    </dependency>
   
    <dependency>
        <groupId>com.github.microtweak</groupId>
        <artifactId>jbx4j-jackson</artifactId>
        <version>1.0.0</version>
    </dependency>
    ```

2. Now let's configure the application to use Jbx4j. Although the following examples are based on the CDI, it is not a requirement to use this library. Create a class that implements EntityResolver as in the example below. You can create multiple EntityResolvers as needed.

    ```java
    @ApplicationScoped
    public class GenericEntityResolver implements EntityResolver<Object> {
    
        @Inject
        private EntityManager entityManager;

        @Override
        public boolean canResolve(Object declaredAt, Class<Object> rawType, JpaEntityData<Object> data, List<Annotation> annotations) {
            return rawType.isAnnotationPresent(Entity.class);
        }
    
        @Override
        public Object resolve(Object declaredAt, Class<Object> rawType, JpaEntityData<Object> data, List<Annotation> annotations) {
            Object primaryKey = data.get("id");
        
            if (primaryKey == null) {
                return null;
            }
        
            // Logic to solve the entity. You can use a find or a complex query JPQL
            return entityManager.find(rawType, primaryKey);
        }    

    }
    ```

3. Instantiate the Jbx4j module and add all the EntityResolvers created in the module. After that, register the module in Jackson.

    ```java
    public class JacksonProducer {
    
        // Ask the CDI to find all the EntityResolver in the application
        @Inject
        private Instance<EntityResolver<?>> resolvers;
        
        @Produces
        @Singleton
        public ObjectMapper createJacksonObjectMapper() {
            ObjectMapper mapper = new ObjectMapper();
            
            // Instances the Jbx4j module for Jackson
            Jbx4jJacksonModule jbx4jModule = new Jbx4jJacksonModule();
    
            // Adds all EntityResolvers found by CDI in the module created earlier
            resolvers.forEach(er -> jbx4jModule.getResolverManager().add(er));
            
            // Register the module in Jackson
            mapper.registerModule(jpaJacksonModule);
            
            return mapper;
        }
        
    }
    ```

After these steps, simply ask Jackson's ObjectMapper to (de)serialize your JPA entity as you are already accustomed to doing with other Java classes.