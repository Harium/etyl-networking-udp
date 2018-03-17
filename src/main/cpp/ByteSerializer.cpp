#include <iostream>
#include "ByteSerializer.h"

ByteSerializer::ByteSerializer() {
	// TODO Auto-generated constructor stub

}

ByteSerializer::~ByteSerializer() {
	// TODO Auto-generated destructor stub
}

void ByteSerializer::serialize(float in, byte* out, int offset) {
	byte * p = reinterpret_cast<byte*>(&in);

	for (std::size_t i = 0; i != sizeof(float); ++i)
	{
		out[offset + i] = p[i];
	}
}

void ByteSerializer::serialize(std::string in, byte* out, int offset) {
	for (unsigned i = 0; i < in.length(); ++i)
	{
		out[offset + i] = (byte)in.at(i);
	}
}

