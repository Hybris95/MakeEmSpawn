@echo off
javac -Xlint:deprecation -cp "./class;./jars/craftbukkit.jar;./jars/Permissions.jar" -d "./class" "./src/com/hybris/bukkit/makeemspawn/MakeEmSpawn.java" "./src/com/hybris/bukkit/makeemspawn/MakeEmSpawnPlayerListener.java"
cd ./class
jar cvf "MakeEmSpawn.jar" ./plugin.yml ./com/
move /Y MakeEmSpawn.jar ../jars/
pause