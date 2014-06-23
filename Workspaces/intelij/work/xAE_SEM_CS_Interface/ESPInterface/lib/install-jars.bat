call mvn install:install-file -Dfile=jdbc-11.2.0.3.jar -DgroupId=net.aconite.affina -DartifactId=datasource.oracle11g -Dversion=11.2.0.3 -Dpackaging=jar

call mvn install:install-file -Dfile=com.ibm.mq.headers.jar -DgroupId=com.ibm -DartifactId=mq.headers -Dversion=7.5.0.0 -Dpackaging=jar

call mvn install:install-file -Dfile=com.ibm.mq.jmqi.jar -DgroupId=com.ibm -DartifactId=mq.jmqi -Dversion=7.5.0.0 -Dpackaging=jar

call mvn install:install-file -Dfile=com.ibm.mqjms.jar -DgroupId=com.ibm -DartifactId=mqjms -Dversion=7.5.0.0 -Dpackaging=jar

call mvn install:install-file -Dfile=dhbcore.jar -DgroupId=com.ibm -DartifactId=dhbcore -Dversion=7.5.0.0 -Dpackaging=jar

call mvn install:install-file -Dfile=jms.jar -DgroupId=com.ibm -DartifactId=jms -Dversion=7.5.0.0 -Dpackaging=jar