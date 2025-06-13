# SpringBatch

http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:testdb
User: sa
Password: (blank)

SELECT * FROM product;

--spring.batch.job.name=importProducts inputResource=classpath:input/input.zip targetDirectory=./work/output/ targetFile=products.txt

