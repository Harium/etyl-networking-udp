CC=gcc
CXX=g++
RM=rm -f
CPPFLAGS=-g -pthread -I/sw/include/root 
LDFLAGS=-g
LDLIBS=-lboost_thread -lboost_system -lpthread

SRCS=ByteDef.cpp ByteSerializer.cpp UDPClient.cpp ClientExample.cpp
OBJS=$(subst .cpp,.o,$(SRCS))

all: udplib

udplib: $(OBJS)
	$(CXX) $(LDFLAGS) -o udplib $(OBJS) $(LDLIBS)

ByteDef.o: ByteDef.cpp

ByteSerializer.o: ByteSerializer.h ByteSerializer.cpp ByteDef.cpp

UDPClient.o: UDPClient.h UDPClient.cpp ByteSerializer.h

ClientExample.o: ClientExample.cpp UDPClient.h

clean:
	$(RM) $(OBJS)

distclean: clean
	$(RM) udplib

