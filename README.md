# Lab 11 - Entity and Message Driven Beans

Online Order Processing System, which simulates a real-world online shopping application. The system will use Entity Beans for persisting order data and MDBs for handling asynchronous messaging between different components of the system, all deployed to Wildfly.

# How to Run
1. Clone the repository
```

```

2. Build the artifacts


3. Configure MySql database (local)

Create new database
```mysql
CREATE DATABASE database_name;
```
Create table in the database
```mysql
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    status VARCHAR(20) DEFAULT 'Pending',
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2) NOT NULL
);
```
3. Configure WildFly server (local)

Config standalone.xml
```bash
[wildfly-directory]\standalone\configuration
```
Find data sources subsystem
```xml
<subsystem xmlns="urn:jboss:domain:datasources:7.2">
```
Add new data source in the tag
```xml
<datasource jndi-name="java:/jdbc/OrderProcessingDS" pool-name="OrderProcessingPool" enabled="true" use-ccm="true">
    <connection-url>jdbc:mysql://localhost:3306/[your_database]</connection-url>
    <driver>mysql</driver>
    <security user-name="[your_username]" password="[your_password]"/>
    <validation>
        <validate-on-match>false</validate-on-match>
        <background-validation>true</background-validation>
    </validation>
    <timeout>
        <set-tx-query-timeout>false</set-tx-query-timeout>
    </timeout>
    <statement>
        <prepared-statement-cache-size>32</prepared-statement-cache-size>
    </statement>
</datasource>
<drivers>
<driver name="h2" module="com.h2database.h2">
    <xa-datasource-class>org.h2.jdbcx.JdbcDataSource</xa-datasource-class>
</driver>
<driver name="mysql" module="com.mysql">
    <driver-class>com.mysql.cj.jdbc.Driver</driver-class>
    <xa-datasource-class>com.mysql.cj.jdbc.MysqlXADataSource</xa-datasource-class>
</driver>
</drivers>
```
Navigate to 
```bash
[wildfly-directory]\modules\system\layers\base\com
```
Create the directory if not exist
```bash
\mysql\main
```
Download mysql-connector-j-x.x.x.jar and put it in this folder

In the folder, create new file module.xml and add content
```xml
<module xmlns="urn:jboss:module:1.3" name="com.mysql">
    <resources>
        <resource-root path="mysql-connector-j-9.2.0.jar"/>
    </resources>
    <dependencies>
        <module name="jakarta.api"/>
        <module name="jakarta.transaction.api"/>
    </dependencies>
</module>
```

Deploy the ear artifact in WildFly and start the server

# Members
| Name             | N_ID      |
|------------------|-----------|
| Marina Carvalho  | N01606437 |
| Vitaly Sukhinin  | N01605938 |
| Kexin Zhu        | N01621302 |
| Samruddhi Chavan | N01604191 |
| Sruthi Pandiath  | N01618202 |



