g++ -c ByteDef.cpp 
g++ -c ByteSerializer.cpp
g++ -c UDPClient.cpp -lboost_thread -lboost_system -lpthread
g++ -c ClientExample.cpp
g++ ByteDef.o ByteSerializer.o UDPClient.o ClientExample.o -lboost_thread -lboost_system -lpthread
