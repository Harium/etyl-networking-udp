#ifndef UDPCLIENT_H_
#define UDPCLIENT_H_

#include "ByteDef.cpp"
#include "ByteSerializer.h"
#include <boost/array.hpp>
#include <boost/asio.hpp>
#include <boost/thread.hpp>

using namespace std;
using boost::asio::ip::udp;

class UDPClient {
private:
	boost::asio::io_service& io_service_;
	udp::socket socket_;
	udp::endpoint endpoint_;
	boost::array<char, 128> recv_buffer;

public:
	UDPClient(boost::asio::io_service& io_service,
			const std::string& host,
			const int& port
	);

	~UDPClient();

	void send(const std::string& msg) {
		socket_.send_to(boost::asio::buffer(msg, msg.size()), endpoint_);
	}

	void do_receive()
	{
		cout <<  recv_buffer.c_array() << endl;
		socket_.async_receive_from(boost::asio::buffer(recv_buffer), endpoint_,
				boost::bind(&UDPClient::handle_receive, this,
						boost::asio::placeholders::error,
						boost::asio::placeholders::bytes_transferred));

	}

	void handle_receive(const boost::system::error_code& error, size_t bytes_transferred)
	{
		cout << "Message Received!" << endl;
		if (!error || error == boost::asio::error::message_size)
			do_receive();
	}

	void doSend()
	{
		while(true)
		{
			boost::thread::sleep(boost::get_system_time()+boost::posix_time::seconds(2));
			byte* a = new byte[5];
			ByteSerializer::serialize("Mars!", a, 0);

			//string str = "Mars?";
			//size_t leng = str.length();
			size_t leng = 5;
			socket_.async_send_to(boost::asio::buffer(a, leng),
					endpoint_,
					boost::bind(&UDPClient::handle_write,this,
							boost::asio::placeholders::error,
							boost::asio::placeholders::bytes_transferred
					)
			);
		}
	}

	void handle_write(const boost::system::error_code &error,size_t bytes_transferred)
	{
		cout << "Message Sent!" << endl;
	}

};

#endif /* UDPCLIENT_H_ */
