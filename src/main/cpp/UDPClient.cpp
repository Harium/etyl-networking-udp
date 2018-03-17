#include "UDPClient.h"

UDPClient::UDPClient(
		boost::asio::io_service& io_service,
		const std::string& host,
		const int& port
) : io_service_(io_service), socket_(io_service, udp::endpoint(udp::v4(), 0)) {

	endpoint_ = boost::asio::ip::udp::endpoint(
			boost::asio::ip::address::from_string(host),
			port);
	do_receive();
	boost::function0< void> f = boost::bind(&UDPClient::doSend,this);
	boost::thread t(f);
	io_service.run();
}

UDPClient::~UDPClient() {
	socket_.close();
}

