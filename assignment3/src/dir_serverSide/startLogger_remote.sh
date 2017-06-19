java -Djava.rmi.server.codebase="http://${2}/sd0104/classes/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.LoggerServer $1
