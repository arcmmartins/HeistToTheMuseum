javac interfaces/*.java registry/*.java serverSide/*.java clientSide/*.java Datastructures/*.java
cp interfaces/Register.class dir_registry/interfaces/
cp registry/*.class dir_registry/registry/
cp interfaces/*.class dir_serverSide/interfaces/
cp serverSide/*.class dir_serverSide/serverSide/
cp interfaces/*.class dir_clientSide/interfaces/
cp clientSide/*.class dir_clientSide/clientSide/
cp Datastructures/*.class dir_clientSide/Datastructures/
cp Datastructures/*.class dir_serverSide/Datastructures/
mkdir -p /Users/nelsonreverendo/Public/classes
mkdir -p /Users/nelsonreverendo/Public/classes/interfaces
mkdir -p /Users/nelsonreverendo/Public/classes/clientSide
cp interfaces/*.class /Users/nelsonreverendo/Public/classes/interfaces
cp clientSide/*.class /Users/nelsonreverendo/Public/classes/clientSide
cp set_rmiregistry.sh /Users/nelsonreverendo/
cp set_rmiregistry_alt.sh /Users/nelsonreverendo/
