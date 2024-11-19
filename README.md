Install kafka in ubuntu
  1.download kafka_2.13-3.9.0.tgz from below link
https://www.apache.org/dyn/closer.cgi?path=/kafka/3.9.0/kafka_2.13-3.9.0.tgz

2.cd Downloads/
3.sudo tar xzf kafka_2.13-3.9.0.tgz 
4.sudo mv kafka_2.13-3.9.0 /opt/kafka
5.ll
6.sudo nano  /etc/systemd/system/zookeeper.service

Copy and paste in terminal file

[Unit]
Description=Apache Zookeeper service
Documentation=http://zookeeper.apache.org
Requires=network.target remote-fs.target
After=network.target remote-fs.target

[Service]
Type=simple
ExecStart=/opt/kafka/bin/zookeeper-server-start.sh /opt/kafka/config/zookeeper.properties
ExecStop=/opt/kafka/bin/zookeeper-server-stop.sh
Restart=on-abnormal

[Install]
WantedBy=multi-user.target


  7.sudo systemctl daemon-reload
  8.sudo nano /etc/systemd/system/kafka.service

Copy and paste in terminal file 

[Unit]
Description=Apache Kafka Service
Documentation=http://kafka.apache.org/documentation.html
Requires=zookeeper.service

[Service]
Type=simple
Environment="JAVA_HOME=/opt/jdk/jdk1.8.0_251"
ExecStart=/opt/kafka/bin/kafka-server-start.sh /opt/kafka/config/server.properties
ExecStop=/opt/kafka/bin/kafka-server-stop.sh

[Install]
WantedBy=multi-user.target

9.check java Path and change the path 
10.readlink -f $(which java)
	/usr/lib/jvm/java-8-openjdk-amd64

11.sudo systemctl daemon-reload
12.sudo systemctl start zookeeper
13.sudo systemctl start kafka

14.sudo systemctl status kafka
15.sudo systemctl status zookeeper

16.cd server/
17.touch kafkastart.sh
18.ll
19.vim kafkastart.sh 
20.sudo chmod +x kafkastart.sh
21.sh kafkastart.sh 
  
22.cd /opt/kafka

23.bin/kafka-topics.sh --version
24.netstat -plnt | grep 9092
25.sudo apt install net-tools
26.netstat -plnt | grep 9092
27.tail -500f /opt/kafka/logs
28.tail -500f /opt/kafka/logs/server.log 

28.sudo bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic topic-name


Solution above error fine below

The error indicates that the --zookeeper option is not recognized. This happens because recent versions of Apache Kafka (from version 2.8.0 onwards) have deprecated direct Zookeeper usage for managing topics, in favor of using the Kafka broker’s **KRaft mode** (Kafka Raft Metadata Mode).

Solution:
In newer Kafka versions, you must interact directly with the Kafka brokers instead of Zookeeper. Here's how you can fix your command:




Updated Command:
sudo bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 
--partitions 1 --topic topic-name

Explanation:
-bootstrap-server: Specifies the Kafka broker(s) to connect to, instead of using Zookeeper.
-localhost:9092: The default address where the Kafka broker listens.
*******************************************************************************
Verifying Your Setup:
1.Ensure Kafka Broker is Running:
   Verify that Kafka is running and listening on port 9092:
   netstat -plnt | grep 9092
 
   If Kafka is not running, check its logs (usually in /opt/kafka/logs) or restart the service.

2.Ensure Correct Kafka Version:
   Confirm your Kafka version:
   bin/kafka-topics.sh --version
  
   If it's 2.8.0 or higher, Zookeeper commands are no longer supported.





********************************************************************************
To work with Kafka topics and messages, follow these steps:

********************************************************************************

1. Check the List of Topics Created
Use the following command to list all topics:
sudo bin/kafka-topics.sh --list --bootstrap-server localhost:9092
********************************************************************************
2. Send Messages to a Topic
Use the Kafka producer tool to send messages to the created topic:

sudo bin/kafka-console-producer.sh --broker-list localhost:9092 --topic topic-name


- After running the above command, type your messages in the terminal.
- Press Enter after typing each message to send it to the topic.
- To exit, press Ctrl+C.

*******************************************************************************

3. View Messages in a Topic**
To consume and see the messages sent to the topic, use the Kafka consumer tool:

sudo bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic topic-name --from-beginning


from-beginning: Displays all messages from the start of the topic. Omit this flag to only view new messages.

*******************************************************************************

Example Workflow:
Create a Topic:

sudo bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic topic-name

Check Topics:
sudo bin/kafka-topics.sh --list --bootstrap-server localhost:9092

Produce Messages:
sudo bin/kafka-console-producer.sh --broker-list localhost:9092 --topic topic-name
- Input messages like:
  Hello, Kafka!
  Welcome to topic-name.
  
Consume Messages:
sudo bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic topic-name --from-beginning

- Output will display:
  Hello, Kafka!
  Welcome to topic-name.
  
Here’s an exhaustive guide to Kafka operations, covering **Create**, **Read**, **Update**, **Delete (CRUD)**, and other configurations with detailed examples and explanations of all values and parameters. 

*******************************************************************************

1. Create Operations

1.1 Create a Topic

Command:

sudo bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 2 --partitions 3 --topic MyTopic


Explanation of Values:
bootstrap-server localhost:9092: Address of the Kafka broker.
replication-factor 2: Each partition will have 2 copies for fault tolerance.
partitions 3: The topic will have 3 partitions.
topic MyTopic: Name of the topic being created.

Verify the Creation:

sudo bin/kafka-topics.sh --list --bootstrap-server localhost:9092

Output:
MyTopic


*******************************************************************************

1.2 Produce Messages to a Topic
Command:
sudo bin/kafka-console-producer.sh --broker-list localhost:9092 --topic MyTopic

Example Input:
Hello Kafka!
This is a test message.

Explanation:
broker-list localhost:9092: Address of the Kafka broker.
topic MyTopic: Topic to which messages will be sent.

*******************************************************************************

 2. Read Operations**
2.1 Consume Messages from a Topic**
Command:
sudo bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic MyTopic –from-beginning

Output:
Hello Kafka!
This is a test message.


Explanation:
from-beginning: Consumes all messages from the beginning of the topic.
topic MyTopic: The topic from which messages will be consumed.

*******************************************************************************
2.2 Describe a Topic
Command:
sudo bin/kafka-topics.sh --describe --bootstrap-server localhost:9092 --topic MyTopic

Output:
Topic: MyTopic
PartitionCount: 3
ReplicationFactor: 2
Configs:

Explanation:
- Displays metadata for the topic, including partition count, replication factor, and configuration.

*******************************************************************************
 3. Update Operations**
	3.1 Update Topic Configuration**

Example: Change Retention Time

sudo bin/kafka-configs.sh --bootstrap-server localhost:9092 --entity-type topics --entity-name MyTopic --alter --add-config retention.ms=3600000


Explanation of Values:
entity-type topics: Specifies that we are updating a topic.
entity-name MyTopic: Specifies the topic name.
alter: Indicates a modification.
add-config retention.ms=3600000: Sets the message retention time to 1 hour (3600000 ms).

Verify the Update:

sudo bin/kafka-configs.sh --bootstrap-server localhost:9092 --entity-type topics --entity-name MyTopic --describe

**Output:**

Configs for topic 'MyTopic': retention.ms=3600000


*******************************************************************************

3.2 Produce Keyed Messages

Command:

sudo bin/kafka-console-producer.sh --broker-list localhost:9092 --topic MyTopic --property "parse.key=true" --property "key.separator=:"


Example Input:

key1:Hello Kafka!
key2:This is a test message.

Explanation:
property "parse.key=true": Indicates messages will have keys.
property "key.separator=:": Specifies : as the key-value separator.

*******************************************************************************
4. Delete Operations
4.1 Delete a Topic

Command:
sudo bin/kafka-topics.sh --delete --bootstrap-server localhost:9092 --topic MyTopic

Explanation:
- Deletes the specified topic.
- Ensure delete.topic.enable=true is set in the Kafka broker configuration.

Verify the Deletion:
sudo bin/kafka-topics.sh --list --bootstrap-server localhost:9092


*******************************************************************************

 5. Other Administrative Operations
5.1 List All Consumer Groups

Command:
sudo bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list

Example Output:
group1
group2


*******************************************************************************

5.2 Describe a Consumer Group

Command:
sudo bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --group group1

Example Output:
TOPIC       PARTITION  CURRENT-OFFSET  LOG-END-OFFSET  LAG  CONSUMER-ID
MyTopic     0          10              15              5   consumer-1


*******************************************************************************************

5.3 Reset Consumer Offsets

Reset to the Beginning:
sudo bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group group1 --reset-offsets --to-earliest --execute --topic MyTopic


Reset to Latest:

sudo bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group group1 --reset-offsets --to-latest --execute --topic MyTopic


*******************************************************************************

5.4 Monitor Kafka Brokers*

Command:
sudo bin/kafka-broker-api-versions.sh --bootstrap-server localhost:9092

Example Output:
1 brokers:
Broker 0:
  Supported APIs:
    ApiKey Produce, MinVersion 0, MaxVersion 7, ...


*******************************************************************************

 6. Advanced Operations

6.1 Compact a Topic**

Command:
sudo bin/kafka-configs.sh --bootstrap-server localhost:9092 --entity-type topics --entity-name MyTopic --alter --add-config cleanup.policy=compact


*******************************************************************************

6.2 Reassign Partitions

Step 1: Generate Reassignment Plan
sudo bin/kafka-reassign-partitions.sh --bootstrap-server localhost:9092 --generate --topics-to-move-json-file topics.json --broker-list "0,1"


Step 2: Execute Reassignment

sudo bin/kafka-reassign-partitions.sh --bootstrap-server localhost:9092 --execute --reassignment-json-file reassignment.json


*******************************************************************************

6.3 Kafka Performance Test

Command:
sudo bin/kafka-producer-perf-test.sh --topic MyTopic --num-records 1000 --record-size 100 --throughput -1 --producer-props bootstrap.servers=localhost:9092

Output:
1000 records sent, 500 records/sec (2.5 MB/sec)


*******************************************************************************

 Summary of Possible Kafka Configurations

| **Operation**                | **Command**                                                                                              
| Create Topic                   | kafka-topics.sh --create                                                                              
| List Topics                      | kafka-topics.sh --list                                                                                
| Produce Messages           | kafka-console-producer.sh --broker-list ...                                                
| Consume Messages        | kafka-console-consumer.sh --topic ...                                                                |
| Describe Topic              | kafka-topics.sh --describe                                                                            
| Delete Topic                | kafka-topics.sh --delete                                                                              
| Update Retention Policy     | kafka-configs.sh --alter --add-config retention.ms=...                                               |
| Reset Offsets               | kafka-consumer-groups.sh --reset-offsets                                                             |
| Add Log Compaction          | kafka-configs.sh --add-config cleanup.policy=compact                                                 |
| Monitor Brokers             | kafka-broker-api-versions.sh                                                                         |
| Performance Testing         | kafka-producer-perf-test.sh --num-records ...                                                        |
| Reassign Partitions         | kafka-reassign-partitions.sh --generate + --execute                                                
Spring Boot Kafka CRUD Operations and Configuration
This document explains how to integrate Apache Kafka with Spring Boot to perform CRUD operations, message consumption, and advanced configurations.
1. Maven Dependency
Add the following dependencies in your Maven project's pom.xml file:

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>
</dependencies>

2. Kafka Configuration
Configure Kafka properties and define producer and consumer factories. Example:

@Configuration
public class KafkaConfig {
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}

3. Kafka Producer
Use KafkaProducer service to send messages to a topic. Example:

@Service
public class KafkaProducer {
    private static final String TOPIC = "MyTopic";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String key, String message) {
        kafkaTemplate.send(TOPIC, key, message);
        System.out.println("Message sent: Key = " + key + ", Value = " + message);
    }
}

4. Kafka Consumer
Set up a Kafka consumer to listen to messages from a topic. Example:

@Service
public class KafkaConsumer {
    @KafkaListener(topics = "MyTopic", groupId = "group_id")
    public void consume(String message) {
        System.out.println("Consumed message: " + message);
    }
}

5. CRUD Operations
5.1 Create/Update (Produce Messages)
Use KafkaController to expose a REST endpoint to publish messages. Example:

@RestController
@RequestMapping("/kafka")
public class KafkaController {
    @Autowired
    private KafkaProducer producer;

    @PostMapping("/publish")
    public ResponseEntity<String> publishMessage(
            @RequestParam("key") String key,
            @RequestParam("message") String message) {
        producer.sendMessage(key, message);
        return ResponseEntity.ok("Message published successfully!");
    }
}

5.2 Delete a Topic
Use KafkaAdminService to delete a topic programmatically. Example:

@Service
public class KafkaAdminService {
    public void deleteTopic(String topicName) {
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        try (AdminClient adminClient = AdminClient.create(properties)) {
            adminClient.deleteTopics(Collections.singletonList(topicName));
            System.out.println("Deleted topic: " + topicName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

5.3 Update Configuration
Update Kafka topic retention policy. Example:

public void updateRetention(String topicName, long retentionMs) {
    Properties properties = new Properties();
    properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

    try (AdminClient adminClient = AdminClient.create(properties)) {
        Map<String, String> configs = new HashMap<>();
        configs.put("retention.ms", String.valueOf(retentionMs));

        adminClient.alterConfigs(Collections.singletonMap(
            new ConfigResource(ConfigResource.Type.TOPIC, topicName),
            new Config(configs)
        ));

        System.out.println("Retention updated for topic: " + topicName);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

6. Testing Kafka Operations
Test the endpoints and Kafka setup using Postman or the Kafka CLI:

1. Produce Messages:
   Endpoint: /kafka/publish?key=myKey&message=HelloKafka

2. Consume Messages:
   Kafka CLI: bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic MyTopic --from-beginning

3. Delete Topics:
   Use the KafkaAdminService to programmatically delete a topic.

4. Update Retention Policy:
   Call the updateRetention method in KafkaAdminService.
