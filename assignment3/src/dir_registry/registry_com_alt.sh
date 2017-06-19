java -Djava.rmi.server.codebase="file:///Users/nelsonreverendo/Documents/UA/SD/repositorio/assignment3/src/dir_registry/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     registry.ServerRegisterRemoteObject $1
