🔹 Testing the API</br>

User Table Create Script: 

```
CREATE TABLE USER_TEST
(
  ID         NUMBER(10),
  USER_NAME  VARCHAR2(50 BYTE)
);
INSERT INTO USER_TEST(ID, USER_NAME) VALUES (1, 'Alice');
SELECT * FROM  USER_TEST;

```

✅ Save users: </br>
```
curl -X POST "http://localhost:8080/users/save?userId=1&name=Alice"
curl -X POST "http://localhost:8080/users/save?userId=2&name=Bob"
```

✅ Retrieve users: </br>
```
curl -X GET "http://localhost:8080/users/1"   # Should fetch from Shard 1
curl -X GET "http://localhost:8080/users/2"  # Should fetch from Shard 2
```


Important Points:</br>
```
✅ Sharding distributes data across multiple Oracle databases for scalability.
✅ Spring Boot's AbstractRoutingDataSource helps dynamically switch between shards.
✅ Application-level sharding is useful when Oracle's built-in sharding isn't available.
```

Swagger URI: </br>
```
http://localhost:8080/swagger-ui/index.html
http://localhost:8080/v3/api-docs
```

Additional Data Type Validations: </br>
![img.png](img.png)