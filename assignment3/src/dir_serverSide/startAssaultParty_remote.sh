java -Djava.rmi.server.codebase="http://${3}/sd0104/classes/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.M_AssaultPartyServer $1 $2