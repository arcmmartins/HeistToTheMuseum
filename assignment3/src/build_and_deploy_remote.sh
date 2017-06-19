javac interfaces/*.java 
javac Datastructures/*.java
javac registry/*.java
javac clientSide/*.java
javac serverSide/*.java

cp interfaces/*.class dir_registry/interfaces/
cp registry/*.class dir_registry/registry/
mkdir -p dir_registry/Datastructures
cp Datastructures/*.class dir_registry/Datastructures/

mkdir -p dir_serverSide/serverSide/interfaces
mkdir -p dir_serverSide/serverSide/Datastructures
cp interfaces/*.class dir_serverSide/serverSide/interfaces/
cp serverSide/*.class dir_serverSide/serverSide/
cp Datastructures/*.class dir_serverSide/serverSide/Datastructures/

cp interfaces/*.class dir_clientSide/interfaces/
cp clientSide/*.class dir_clientSide/clientSide/
cp Datastructures/*.class dir_clientSide/Datastructures/

mkdir -p dir_registry/Datastructures
cp Datastructures/*.class dir_registry/Datastructures/
mkdir -p /home/sd0104/Public/classes
mkdir -p /home/sd0104/Public/classes/interfaces
mkdir -p /home/sd0104/Public/classes/clientSide
mkdir -p /home/sd0104/Public/classes/Datastructures
cp interfaces/*.class /home/sd0104/Public/classes/interfaces
cp clientSide/*.class /home/sd0104/Public/classes/clientSide
cp Datastructures/*.class /home/sd0104/Public/classes/Datastructures
cp set_rmiregistry.sh /home/sd0104/
cp set_rmiregistry_alt.sh /home/sd0104/
ln -s /var/www/html/sd0104/ ~/Public
