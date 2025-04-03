# Lab 11 - Entity and Message Driven Beans

Online Order Processing System, which simulates a real-world online shopping application. The system will use Entity Beans for persisting order data and MDBs for handling asynchronous messaging between different components of the system, all deployed to Wildfly.

# How to Run
1. Clone the repository
```bash
https://github.com/desmondsyu/OnlineOrder.git
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
    order_date DATETIME NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL
);
```
3. Configure WildFly server (local)


Config standalone.xml
```bash
[wildfly-directory]\standalone\configuration
```
Add extension
```xml
<extension module="org.wildfly.extension.messaging-activemq"/>
```
Find ejb3 subsystem
```xml
<subsystem xmlns="urn:jboss:domain:ejb3:10.0"></subsystem>
```
Add below in the tag
```xml
<mdb>
    <resource-adapter-ref resource-adapter-name="${ejb.resource-adapter-name:activemq-ra.rar}"/>
    <bean-instance-pool-ref pool-name="mdb-strict-max-pool"/>
</mdb>
```
Add subsystem
```xml
 <subsystem xmlns="urn:jboss:domain:messaging-activemq:16.0">
    <server name="default">
        <security elytron-domain="ApplicationDomain"/>
        <statistics enabled="${wildfly.messaging-activemq.statistics-enabled:${wildfly.statistics-enabled:false}}"/>
        <security-setting name="#">
            <role name="guest" send="true" consume="true" create-non-durable-queue="true" delete-non-durable-queue="true"/>
        </security-setting>
        <address-setting name="#" dead-letter-address="jms.queue.DLQ" expiry-address="jms.queue.ExpiryQueue" max-size-bytes="10485760" page-size-bytes="2097152" message-counter-history-day-limit="10"/>
        <http-connector name="http-connector" socket-binding="http" endpoint="http-acceptor"/>
        <http-connector name="http-connector-throughput" socket-binding="http" endpoint="http-acceptor-throughput">
            <param name="batch-delay" value="50"/>
        </http-connector>
        <in-vm-connector name="in-vm" server-id="0">
            <param name="buffer-pooling" value="false"/>
        </in-vm-connector>
        <http-acceptor name="http-acceptor" http-listener="default"/>
        <http-acceptor name="http-acceptor-throughput" http-listener="default">
            <param name="batch-delay" value="50"/>
            <param name="direct-deliver" value="false"/>
        </http-acceptor>
        <in-vm-acceptor name="in-vm" server-id="0">
            <param name="buffer-pooling" value="false"/>
        </in-vm-acceptor>
        <jms-queue name="OrderProcessingQueue" entries="java:/queue/OrderProcessingQueue"/>
        <connection-factory name="InVmConnectionFactory" entries="java:/ConnectionFactory" connectors="in-vm"/>
        <connection-factory name="RemoteConnectionFactory" entries="java:jboss/exported/jms/RemoteConnectionFactory" connectors="http-connector"/>
        <pooled-connection-factory name="activemq-ra" entries="java:/JmsXA java:jboss/DefaultJMSConnectionFactory" connectors="in-vm" transaction="xa"/>
    </server>
</subsystem>
```
Find data sources subsystem
```xml
<subsystem xmlns="urn:jboss:domain:datasources:7.2"></subsystem>
```
Add new data source inside the tag 
```xml
<!-- Inside datasources -->
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

<!-- Inside drivers -->
<driver name="mysql" module="com.mysql">
    <driver-class>com.mysql.cj.jdbc.Driver</driver-class>
    <xa-datasource-class>com.mysql.cj.jdbc.MysqlXADataSource</xa-datasource-class>
</driver>
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
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.3" name="com.mysql">
    <resources>
        <resource-root path="mysql-connector-j-9.2.0.jar"/>
    </resources>
    <dependencies>
        <module name="javax.api"/>
        <module name="javax.transaction.api"/>
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



