g++ -c ../../main/cpp/ByteDef.cpp
g++ -c ../../main/cpp/ByteSerializer.cpp
g++ -c ByteSerializerTest.cpp -lgtest
g++ ByteDef.o ByteSerializer.o ByteSerializerTest.o -lgtest -o test

./test
