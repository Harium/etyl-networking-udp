//============================================================================
// Name        : ClientExample.cpp
// Author      :
// Version     :
// Copyright   :
// Description : UDP Client example using boost asio
// Hint: Compile with -lboost_thread -lboost_system -lpthread
//============================================================================

#include <iostream>
#include "UDPClient.h"

using namespace std;

int main()
{
	string host = "127.0.0.1";
	int port = 8340;

	boost::asio::io_service io_service;
	UDPClient client(io_service, host, port);
}
