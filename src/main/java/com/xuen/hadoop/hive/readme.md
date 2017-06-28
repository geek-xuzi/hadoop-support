hive可直接使用jdbctemplate进行sql交互
<bean id="hiveDriver" class="org.apache.hive.jdbc.HiveDriver"/>

<bean id="hiveDataSource" class="org.springframework.jdbc.datasource.SimpleDriverDataSource">
    <constructor-arg name="driver" ref="hiveDriver"/>
    <constructor-arg name="url" value="${hive.url}"/>
    <!--<constructor-arg name="url" value="jdbc:hive2://10.32.64.15:10000/test"/>-->
</bean>

<bean id="hiveJdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
    <constructor-arg name="dataSource" ref="hiveDataSource"/>
</bean>