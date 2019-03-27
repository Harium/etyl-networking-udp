# etyl-networking-udp
Small UDP server/client pair

Server has only the Java option
Client has a Java and a C++ (Boost::ASIO) version

## Maven
```xml
<dependency>
    <groupId>com.harium.etyl.networking</groupId>
    <artifactId>udp</artifactId>
    <version>0.2.0</version>
</dependency>
```

## Building C++ Example

### Ubuntu
```
sudo apt-get install libboost-dev libgtest-dev
```
### Fedora
```
sudo dnf install boost-devel gtest-devel
```

### Compiling
```
g++ -c ByteDef.cpp 
g++ -c ByteSerializer.cpp
g++ -c UDPClient.cpp -lboost_thread -lboost_system -lpthread
g++ -c ClientExample.cpp
g++ ByteDef.o ByteSerializer.o UDPClient.o ClientExample.o -lboost_thread -lboost_system -lpthread

```

### Testing
```bash
cd /src/test/cpp/
sh test.sh
```
