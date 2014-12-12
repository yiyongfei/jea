一直在金融行业里做软件项目，见识了各种形形色色的企业软件开发框架，由于金融行业本身的特殊性，它对于信息资源整合、软件可靠性、面向服务体系结构、面向业务的开发模式的支持提出了更高的要求。就像见多了摄影的美景后自己也想去那走一走，所以JEA诞生了，它有一个简陋的样例项目(https://github.com/yiyongfei/jea-demo)，该样例的项目分层结构还是可以借鉴的。<br>
JEA的定位点是针对于大中型企业的面向服务的企业开发集成框架，主要特点如下：<br>
1、DRPC，分布式远程过程调用，通过Storm实现，序列化由Kryo支持。<br>
2、L2二级缓存的支持，考虑到应用分布式部署的原因，数据只能通过Memcached和Redis进行缓存。<br>
3、Hibernate和Mybatis的集成，DB的事务管理由Hibernate提供支持，所以一般增删改的操作由Hibernate来完成，而查询则交由Mybatis。<br>
4、服务生产者，如果服务供企业内部系统使用，提供方式可以是Storm的实时模式或者ActiveMQ的队列模式，如果服务是提供给外部合作企业使用，提供方式可以是SOAP或者REST，通过CXF实现。<br>
5、服务消费者，调用外部系统的服务完成业务，依据外部系统提供的接口方式，提供以下方式调用：MQ、DRPC、REST、SOAP，其中REST的调用通过HttpClient实现。<br>

子项目的说明：<br>
1、web，用于Web层，封装了调用AppServer的序列化逻辑和调用逻辑<br>
2、integration，用于Integration层，封装了调用外部系统的（需优化）<br>
3、ws，提供MQ、REST、SOAP、STORM服务的一些封装<br>
4、orm，对于Hibernate和Mybatis的一些封装<br>
5、cache，对于Memcached和Redis的一些封装<br>
6、core，核心组件<br>
7、achieve，一些默认实现<br>
