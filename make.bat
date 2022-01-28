rm ./file-copier/target/file-copier.jar
javac -cp "E:/Diego/Programas/org.apache.commons.io.jar" -d ./file-copier/target ./file-copier/src/*.java
jar cvf ./file-copier/target/file-copier.jar ./file-copier/target/*
cp ./file-copier/target/file-copier.jar ./file-copier.jar