#ifndef BYTESERIALIZER_H_
#define BYTESERIALIZER_H_

#include <iostream>
#include "ByteDef.cpp"

class ByteSerializer {
public:
	ByteSerializer();
	virtual ~ByteSerializer();

	static void serialize(float in, byte* out, int offset);
	static void serialize(std::string in, byte* out, int offset);
};

#endif /* BYTESERIALIZER_H_ */
