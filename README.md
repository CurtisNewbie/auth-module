# authmodule

Simple authentication module for internal use

## To use it:

1. Install jar to local repository as follows:

```
mvn clean install
```

2. Import it inside your pom:

```
<dependency>
    <groupId>com.curtisnewbie</groupId>
    <artifactId>authmodule</artifactId>
    <version>0.0.1</version>
</dependency>
```

3. Run DDL script in `/db-scripts/user.sql`

4. Configure following properties for MySql connection:

```
spring.datasource.url=jdbc:mysql://localhost:3306/yourDb
spring.datasource.username=yourUsername
spring.datasource.password=yourPassword
```
