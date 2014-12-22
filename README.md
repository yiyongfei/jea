<p>
一直在金融行业里做软件项目，见识了各种形形色色的企业软件开发框架，就像见多了摄影的美景后自己也想去那走一走，所以JEA诞生了。<br>
JEA定位为面向服务的企业级分布式开发集成框架，要完全发挥JEA的各项特性，需要准备多台服务器分别部署应用和支撑系统，如果要商用，相对来说大中型企业可能会更适合些。主要特点如下：</br>
1、分布式远程过程调用：随着SOA越来越深入人心，软件的整体设计可能也会像传统工业社会一样，慢慢向流水线方向发展。曾经接触过很多类似这样的产品：它们独力完成了所有的业务工作，不需要和其它业务系统进行协同工作，随着时间的推移，它们越来越庞大，越来越难维护，而且还会发现一个很神奇的现象，产品里的很多业务功能总是能在同家公司的其它产品里发现。解决这个问题的有效办法是对业务系统进行拆解，根据不同的业务节点拆解成多个子系统，一笔交易通过远程调用的方式由各个子系统协同完成，这样可以很好的控制各子系统的复杂度，同时也能提升业务重用。JEA的分布式远程过程调用是通过Storm的DRPC来实现的，由Spring做IOC容器管理，很容易做横向扩展，开发方面符合Spring通用开发模式，通过Maven生成发布包发布到Storm上，之后即可通过dispatch(String callback, String facadeId, Object... objs)调用，JEA在处理时会首先通过Kryo将对象序列化，然后发送到DRPC服务器，转交给Topology后由Bolt进行反序列化并调用Facade来完成交易。而对于实效性要求不高的异步交易，JEA同样通过ActiveMQ队列方式提供了支持，JEA将对象序列化后发送到相应的队列后结束本次交易，而Storm服务器的MQTopology会定时从队列里轮询，发现有新消息后调用Facade来完成交易。</br>
2、二级缓存和双重缓存（很容易扩展成多级缓存和多重缓存）：鉴于分布式多服务器部署的考虑，JEA的缓存服务器只支持Memcached和Redis这二种，同时通过配置可使其支持二级缓存和双重缓存。双重缓存，在往L1缓存数据时会同时往L2缓存一份数据，此时L2将作为L1的备份缓存，如果L1缓存失效，会自动从L2缓存里去获取数据，避免缓存雪崩的发生。二级缓存，Memcached和Redis都是性能优越的缓存服务器，但它们都有各自的特色，相互之间并不能完全替代，有条件的情况下，建议打开JEA的L1缓存和L2缓存，然后往缓存服务器新增或更新缓存数据时，根据实际的业务情况制定缓存策略，决定是将业务数据缓存在L1缓存还是缓存在L2缓存，通过调用CacheContext.set(String cacheLevel, String key, Object value, int seconds)将数据缓存在指定级别的缓存器上。</br>
3、面向服务体系结构：每一个基于JEA进行构建的系统，它即是一个服务提供者，同时它也可以作为服务消费者。向外部系统提供服务时，除了通过Storm和ActiveMQ队列这二种不同时效要求的方式外，JEA还支持更通用的基于WebService的REST服务或者SOAP服务，鉴于性能、稳定性和横向扩展等需要，在企业内部构建SOA服务体系，建议基于Storm+MQ来构建整套服务体系，如果需要提供服务供外部机构调用，再在其基础上额外提供更通用的REST服务或SOAP服务。而向外部系统消费服务时，主要基于外部系统所提供的服务接口方式调用不同的消费方式来获得服务的请求结果，目前允许的消费方式有：ActiveMQ、Storm、REST、SOAP这四种，如果服务提供方提供的是REST服务，此时数据默认将按JSON的方式组织发送。</br>
4、JEA整体是基于Spring进行容器管理，通过Maven进行构建，同时又集成了Hibernate和Mybatis这二大开源ORM框架，所以通过JEA很容易构建出一套SpringMVC系统，系统分层结构：Web->Bridge->Facade->Service->Dao(Integration，对外消费服务)，其中Web可以单独部署成Web服务器，Bridge及之后各分层组成StormTopology服务发布到DRPC服务器上，另外在Dao层通过AbstractBaseDAO所提供的各个方法，很容易完成DB层面的CRUD操作，只是需要注意，JEA要求所有的DB事务管理统一交由Hibernate进行管理，所以所有涉及会引发DB数据变化的操作都由Hibernate来完成，而查询操作则交由Mybatis来完成（后续将引入自定义的缓存机制，使Hibernate和Mybatis能够共享缓存内容）。</br>
</br>
JEA使用手册（样例参看：https://github.com/yiyongfei/jea-demo）</br>
1、配置文件jea-core.properties，放在项目Classpath下</br>
#sync mode[SPRING or STORM.LOCAL or STORM.REMOTE]</br>
配置项：sync.mode</br>
说明：实时模式，1)SPRING，通过BeanFactory获取Bean对象，直接执行默认方法完成业务调用；2）STORM.LOCAL，开启STORM本地模式，通过Topology完成业务调用；3）STORM.REMOTE，开启STORM远程模式，通过调用远程注册的Topology完成业务调用；</br>
#db info</br>
配置项：db.driver</br>
说明：数据库驱动</br>
配置项：db.url</br>
说明：数据库连接</br>
配置项：db.username</br>
说明：数据库用户</br>
配置项：db.password</br>
说明：数据库密码</br>
配置项：db.validation</br>
说明：用于校验数据库连接是否有效的SQL（如select 1 from dual）</br>
配置项：db.hibernate.dialect</br>
说明：如org.hibernate.dialect.MySQLDialect</br>
#configure files</br>
配置项：storm.configure.file</br>
说明：指定storm的配置文件名（如storm.properties）</br>
配置项：mq.configure.file</br>
说明：指定MQ的配置文件名（如mq.properties）</br>
配置项：cache.configure.file</br>
说明：指定缓存的配置文件名（如cache.properties）</br>
配置项：topology.context.file</br>
说明：指定Appserver的配置文件名（如applicationContext-appserver.xml），该文件作为业务应用服务器的上下文配置文件</br>

2、配置文件storm.properties，放在项目Classpath下
[submit.definition]（Topology提交类定义，必须有该项内容）
配置项：local
说明：Storm本地模式的Submit提交类
配置项：remote
说明：Storm远程模式的Submit提交类

[drpc.server]（DRPC服务器定义，必须有该项内容）
配置项：host
说明：主机IP地址（如192.168.222.133）
配置项：port
说明：主机端口（如3772）
配置项：timeout
说明：连接超时时间，毫秒（如3000）

[topology.definition]（Topology定义区间，必须有该项内容）
drpc01Topology=com.architecture.example.topology.DrpcTopology
drpc02Topology=com.architecture.example.topology.DrpcTopology
mqTopology=com.architecture.example.topology.MQTopology
mqTopology，自定义Topology在Storm发布时的Topology名
com.architecture.example.topology.MQTopology，对应的Topology类

[facade.topology.mapping]（指定Facade的处理Topology映射，必须有该项内容）
default=drpc01Topology
exampleSaveFacade=drpc02Topology
exampleSaveFacade，在Appserver里定义的Facade对象，由Spring做IOC容器管理
drpc02Topology，在topology.definition定义的Topology名
default，默认处理Topology，如果指定处理的Facade没有映射Topology，则交由默认处理Topology来处理

3、配置文件cache.properties，放在项目Classpath下
[l1.cache]（L1缓存器，必须有该项内容）
配置项：type
说明：缓存类型，1）MEMCACHED；2）REDIS
配置项：activate
说明：是否激活，true表示该缓存器已经激活
配置项：servers
说明：缓存服务器，格式：IP:PORT IP:PORT IP:PORT
配置项：maxTotal
说明：控制一个pool可分配多少个实例
配置项：maxWaitMillis
说明：表示当引入一个实例时，最大的操作等待时间；
配置项：maxConnectMillis
说明：控制一个pool实例超时时间；（只用于Memcached）
配置项：enableHealSession
说明：实例修复开关（设成true时将自动修复失效的连接）；（只用于Memcached）
配置项：healSessionInterval
说明：实例修复间隔时间（milliseconds）；（只用于Memcached）
配置项：failureMode
说明：（只用于Memcached）
配置项：maxIdle
说明：控制一个pool最多有多少个状态为idle(空闲的)的实例；（只用于Redis）
配置项：testOnBorrow
说明：在引入一个实例时，是否提前进行validate操作；如果为true，则得到的实例均是可用的；（只用于Redis）
配置项：testOnReturn
说明：在return给pool时，是否提前进行validate操作；（只用于Redis）
配置项：lifo
说明：borrowObject返回对象时，是采用DEFAULT_LIFO（last in first out，即类似cache的最频繁使用队列），如果为False，则表示FIFO队列；（只用于Redis）

[l2.cache]（L2缓存器，必须有该项内容）
type=见l1.cache说明
activate=见l1.cache说明
servers=见l1.cache说明
maxTotal=见l1.cache说明
maxWaitMillis=见l1.cache说明
maxConnectMillis=见l1.cache说明
enableHealSession=见l1.cache说明
healSessionInterval=见l1.cache说明
failureMode=见l1.cache说明
maxIdle=见l1.cache说明
testOnBorrow=见l1.cache说明
testOnReturn=见l1.cache说明
lifo=见l1.cache说明

4、配置文件mq.properties，放在项目Classpath下
[mq.server]（MQ服务器定义，必须有该项内容）
配置项：host
说明：MQ服务器主机（如tcp://192.168.222.134:61616）
配置项：username
说明：MQ用户名
配置项：password
说明：MQ密码

[consume.queue.mapping]（指定MQ消费者的队列线程，必须有该项内容）
exampleQueue=com.ea.core.achieve.mq.consumer.DefaultConsumer
exampleQueue，自定义的队列名
com.ea.core.achieve.mq.consumer.DefaultConsumer，队列对应的消费线程

[facade.queue.mapping]（指定Facade的处理队列，表示由该队列处理该Facade，必须有该项内容）
default=exampleQueue
default，默认处理队列，如果指定的Facade没有映射队列，则交由默认队列来处理

5、配置文件applicationContext-appserver.xml，普通的Spring配置文件，唯一需要注意的是该上下文配置文件是应用于业务应用服务器，Web应用服务器将另外提供配置文件，样例如下：
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:context="http://www.springframework.org/schema/context" xmlns:jdbc="http://www.springframework.org/schema/jdbc"  
	xmlns:jee="http://www.springframework.org/schema/jee" xmlns:tx="http://www.springframework.org/schema/tx"
	xmlns:aop="http://www.springframework.org/schema/aop"
	xsi:schemaLocation="
		http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.0.xsd
		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.0.xsd
		http://www.springframework.org/schema/jdbc http://www.springframework.org/schema/jdbc/spring-jdbc-4.0.xsd
		http://www.springframework.org/schema/jee http://www.springframework.org/schema/jee/spring-jee-4.0.xsd
		http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd
		http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-4.0.xsd"
	default-lazy-init="true">
	<description>Spring公共配置 </description> 
	<!-- 使用annotation 自动注册bean, 并保证@Required、@Autowired的属性被注入 -->
	<context:component-scan base-package="com.architecture"></context:component-scan>
	<context:component-scan base-package="com.ea"></context:component-scan>
	<context:property-placeholder ignore-resource-not-found="true"
			location="classpath*:/jea-core.properties" />
    <!-- 配置数据源(连接池，druid) -->  
	<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close">
	    <property name="driverClassName" value="${db.driver}" />
		<property name="url" value="${db.url}" />
		<property name="username" value="${db.username}" />
		<property name="password" value="${db.password}" /> 
		<property name="initialSize" value="3" />
		<property name="minIdle" value="3" />
		<property name="maxActive" value="20" />
		<property name="maxWait" value="60000" />
		<property name="timeBetweenEvictionRunsMillis" value="60000" />
		<property name="minEvictableIdleTimeMillis" value="300000" />
		<property name="validationQuery" value="SELECT 'x'" />
		<property name="testWhileIdle" value="true" />
		<property name="testOnBorrow" value="false" />
		<property name="testOnReturn" value="false" />
		<property name="poolPreparedStatements" value="true" />
		<property name="maxPoolPreparedStatementPerConnectionSize" value="20" /> -->
		<property name="filters" value="stat" />
	</bean>
	<!-- 配置Session工厂 -->
	<bean id="hibernateSessionFactory"
	  class="org.springframework.orm.hibernate4.LocalSessionFactoryBean">
        <property name="dataSource" ref="dataSource" />
        <property name="packagesToScan">
            <list>
                <value>com.architecture</value>
            </list>
        </property>
        <property name="hibernateProperties">
            <props>
                <prop key="hibernate.dialect">${db.hibernate.dialect}</prop>
                <prop key="hibernate.show_sql">true</prop>
                <prop key="hibernate.cache.use_second_level_cache">false</prop>  
                <prop key="hibernate.current_session_context_class">org.springframework.orm.hibernate4.SpringSessionContext</prop>
            </props>
        </property>
	</bean>
    <bean id="mybatisSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource" />
        <property name="configLocation" value="classpath:mybatis.xml" />
        <property name="mapperLocations" value="classpath*:/sqlmap/**/*sqlmap-mapping.xml" />
    </bean>
    <!-- 配置SessionTemplate -->
    <bean id="mybatisSessionTemplate" class="org.mybatis.spring.SqlSessionTemplate">
        <constructor-arg name="sqlSessionFactory" ref="mybatisSessionFactory" />
    </bean>
    <!-- 配置Hibernate事务管理器 -->
    <bean id="hibernateTransactionManager"
        class="org.springframework.orm.hibernate4.HibernateTransactionManager">
      <property name="sessionFactory" ref="hibernateSessionFactory" />
    </bean>
    <!-- 使用annotation定义事务 -->  
    <tx:annotation-driven transaction-manager="hibernateTransactionManager" />
</beans>

6、Web应用服务器的具体配置见样例，遵循一般的SpringMVC配置即可，若结合Rest或Soap，增加相应的配置文件即可；

7、Web层Controller的开发
1）继承com.ea.core.web.controller.AbstractController，
2）Controller欲调用Facade的业务逻辑时，只需调用父类的方法dispatch(String callback, String facadeId, Object... models)即可；
dispatch方法说明：
参数：callback，调用类型，a）实时调用，WebConstant.CALL_BACK.SYNC；2）准实时调用，WebConstant.CALL_BACK.ASYNC
参数：facadeId，在业务应用服务器定义的Facade的BeanId名称
参数：models，要传递给Facade处理的0到多个的数据对象
返回：Facade的执行结果，如果Facade执行时抛出异常，则Dispatch方法同样抛出相同的异常

8、Facade层的开发，事务控制统一在Facade层处理
1）如果该Facade不做事务控制，直接继承com.ea.core.facade.AbstractFacade即可
2）如果由该Facade做事务控制，直接继承com.ea.core.facade.AbstractTransactionalFacade即可
3）实现Object perform(Object... obj)方法，在该方法里完成业务逻辑

9、Service层的开发，Storm必须要求每个注入的类实现序列化接口，定义了公共接口IService
1）实现接口com.ea.core.service.IService

10、DAO层的开发，完成与DB的交互
1）继承com.ea.core.orm.dao.AbstractBaseDAO
2）通过Hibernate完成单表新增，执行父类的save方法，参数为继承于BasePO的子对象，返回BasePK主键对象
3）通过Hibernate完成单表更新，执行父类的update方法，参数为继承于BasePO的子对象
4）通过Hibernate完成单表删除，执行父类的delete方法，参数为继承于BasePO的子对象
5）通过Hibernate完成单表查看，执行父类的load方法，参数为继承于BasePO的子对象
6）通过Mybatis完成单记录的查询，执行父类的queryOne方法，参数参照sqlmap-mapping.xml里的配置
7）通过Mybatis完成多记录的查询，执行父类的queryMany方法，参数参照sqlmap-mapping.xml里的配置
8）通过Hibernate完成单表单记录的新增、修改、删除，执行父类的executeSQL方法，第1个参数为SQL语句，SQL遵循对应数据库的标准写法，第2个参数为对象数组，按SQL要求的参数次序填充参数内容
9）通过Hibernate完成单表批量记录的新增、修改、删除，执行父类的batchExecuteSQL方法，第1个参数为SQL语句，SQL遵循对应数据库的标准写法，第2个参数为对象数组的集合，按SQL要求的参数次序填充参数内容

11、Integration层的开发，完成与外部系统的交互，不涉及外部系统的事务
1）如果外部系统提供AcviveMQ服务，继承com.ea.core.integration.bridge.MQIntegration
2）调用方法connect(String host, String username, String password, String queueName, String facadeId, Object... obj)
connect方法说明：
参数：host，远程MQ服务器的主机
参数：username，远程MQ的用户名
参数：password，远程MQ的密码
参数：queueName，要放置的队列名
参数：facadeId，对应的业务FacadeId名称
参数：obj，Facade要使用的参数

3）如果外部系统提供Storm服务，继承com.ea.core.integration.bridge.StormIntegration
4）调用方法connect(String host, String port, String timeout, String topologyName, String facadeId, Object... obj) 
connect方法说明：
参数：host，远程DRPC服务器的主机
参数：port，远程DRPC的端口
参数：timeout，超时时间
参数：topologyName，远程DRPC服务器注册的拓扑名称
参数：facadeId，对应的业务FacadeId名称
参数：obj，Facade要使用的参数

5）如果外部系统提供Rest服务，继承com.ea.core.integration.bridge.RestIntegration
6）调用方法connect(String host, String httpMethod, String method, Object... obj)
connect方法说明：
参数：host，远程提供REST服务的服务名，如http://127.0.0.1:8080/architecture-demo-web/ws/HelloREST
参数：httpMethod，POST或PUT或GET或DELETE
参数：method，Rest服务对应的方法，如@Path("/update/{id}")，则该方法为update
参数：obj，Rest要使用的参数

7）如果外部系统提供Soap服务，继承com.ea.core.integration.bridge.SoapIntegration
8）调用方法connect(String host, String method, Object... obj)
connect方法说明：
参数：host，远程提供SOAP服务的服务名，如http://127.0.0.1:8080/architecture-demo-web/ws/HelloSOAP?wsdl
参数：method，SOAP服务对应的方法，如update
参数：obj，SOAP要使用的参数

12、Bridge层的开发，涉及Storm的开发
1）在architecture-achieve下有部分Storm的默认实现，可以参考实现
2）DRPC的Spout有默认实现，无须开发
3）生成基于DRPC的Bolt，继承com.ea.core.storm.bolt.AbstractDrpcBolt，并实现方法perform(String facadeName, Object... models)
例：
protected Object perform(String facadeName, Object... models) throws Exception {
	IFacade facade = (IFacade) AppServerBeanFactory.getBeanFactory().getBean(facadeName);
	return facade.facade(models);
}
4）生成基于DRPC的Topology，继承com.ea.core.storm.topology.AbstractDRPCTopology，并实现方法AbstractDrpcBolt setBolt(TopologyBuilder builder, String upStreamId)
例：
protected AbstractDrpcBolt setBolt(TopologyBuilder builder, String upStreamId) {
	DefaultDrpcBolt bolt = new DefaultDrpcBolt();
	bolt.setBoltName("drpcBolt");
	builder.setBolt(bolt.getBoltName(), bolt).noneGrouping(upStreamId);
	return bolt;
}
5）非DRPC应用需开发Spout，继承com.ea.core.storm.spout.AbstractRichSpout
6）生成普通Bolt，继承com.ea.core.storm.bolt.AbstractRichBolt
7）生成普通Topology，继承com.ea.core.storm.topology.AbstractTopology，并实现方法AbstractRichBolt setBolt(TopologyBuilder builder, String upStreamId)和IRichSpout initSpout()
例：
protected AbstractRichBolt setBolt(TopologyBuilder builder, String upStreamId) {
	MQBolt bolt = new MQBolt();
	bolt.setBoltName("mqBolt");
	builder.setBolt(bolt.getBoltName(), bolt).noneGrouping(upStreamId);
	return bolt;
}
protected IRichSpout initSpout() {
	return new MQSpout();
}
8）生成本地模式和远程模式的提交类
本地模式提交类，继承com.ea.core.storm.main.AbstractLocalSubmitTopology
远程模式提交类，继承com.ea.core.storm.main.AbstractSubmitTopology
二者的主要差别在于构造函数：
本地模式：
super();
Config conf = new Config();
......
StormCluster cluster = new StormCluster(new LocalCluster());
super.init(cluster, conf);
远程模式：
super();
Config conf = new Config();
......
StormCluster cluster = new StormCluster(new StormSubmitter());
super.init(cluster, conf);
9）配置storm.properties文件
10）生成远程模式的Main类，如下
public class TopologySubmit {
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Class<?> className = TopologyDefinition.findSubmitMode(StormConstant.SUBMIT_MODE.REMOTE.getCode());
		((ISubmitTopology)className.newInstance()).submitTopology();
	}
}
11）通过Maven的maven-assembly-plugin（配置文件内容参见样例项目的assembly.xml），将其打成Jar包，找到以-topology-submit作为后缀的Jar包
12）将该Jar包上传到Storm服务器，进行Topology提交
./storm jar xxx-topology-submit.jar xxx.xxx.TopologySubmit

13、ActiveMQ队列消费者开发
1）继承com.ea.core.mq.consumer.AbstractConsumer，它是一个线程类，表示从队列里获取一条待处理的消息后，可以另起一个线程来处理，不堵塞
2）实现方法ReceiveDTO deserialize(byte[] aryByte)，完成对消息的反序列化工作，返回结果里的requestId，将作为perform的第1个参数，params将作为第2个参数
3）实现方法perform(String facadeId, Object... models)

14、二级缓存及双重缓存的使用，当前将缓存的上下文交由Spring管理
1）配置好cache.properties
2）通过BeanFactory获取上下文对象CacheContext
3）执行该上下文对象的缓存方法
方法：set(Map<String, Object> map, int seconds)
说明：根据Map对象设置缓存数据，如果Key存在，则会覆盖Value。所有Level的缓存器都会缓存数据。
方法：set(String cacheLevel, Map<String, Object> map, int seconds)
说明：根据Map对象设置缓存数据，如果Key存在，则会覆盖Value。指定Level及后续的缓存器会缓存数据，cacheLevel 缓存级别，如果设置成L2，表明L2及后续的缓存器将缓存数据，但L1不缓存
方法：set(String key, Object value, int seconds)
方法：set(String cacheLevel, String key, Object value, int seconds)

方法：add(Map<String, Object> map, int seconds)
说明：设置缓存数据，如果Key存在，则缓存不成功.所有Level的缓存器都会缓存数据
方法：add(String cacheLevel, Map<String, Object> map, int seconds)
说明：设置缓存数据，如果Key存在，则缓存不成功。指定Level及后续的缓存器会缓存数据
方法：add(String key, Object value, int seconds)
方法：add(String cacheLevel, String key, Object value, int seconds)

方法：replace(Map<String, Object> map, int seconds)
说明：设置缓存数据，如果Key不存在，则缓存不成功。所有Level的缓存器都会缓存数据。
方法：replace(String cacheLevel, Map<String, Object> map, int seconds) 
说明：设置缓存数据，如果Key不存在，则缓存不成功。指定Level及后续的缓存器会缓存数据
方法：replace(String key, Object value, int seconds)
方法：replace(String cacheLevel, String key, Object value, int seconds)

方法：Set<String> keys(String pattern, String regexp)
说明：从所有缓存服务器里查找所有指定表达式的Key，不同的缓存服务器里如果存在相同的Key，只返回其中一个。

方法：expire(String key, int seconds)
说明：设置失效时间，所有Level的缓存器都会设置

方法：Object get(String key)
说明：根据Key获得缓存数据，从所有Level的缓存器里查找是否符合Key的缓存数据，找到后中止查找直接返回结果

方法：Boolean exists(String key)
方法：判断Key是否存在

方法：Map<String, Object> getByRegexp(String pattern, String regexp)
说明：根据正则表达式从所有缓存服务器里获得符合条件的Key和缓存数据

方法：delete(String key) 
说明：根据Key删除缓存数据

方法：deleteByRegexp(String pattern, String regexp) 
说明：根据正则表达式删除符合条件的缓存数据
</p>
