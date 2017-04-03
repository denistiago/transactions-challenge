# transactions-challenge

## tests
```
mvn test
```

## running
```
mvn spring-boot:run
```

## adding transaction
```
curl -XPOST -H "Content-Type: application/json" http://localhost:8080/transactions -d '{"amount": 2.3, "timestamp": TIMESTAMP_IN_MILLISECONDS}'
```

## getting statistics
```
curl http://localhost:8080/statistics
```
