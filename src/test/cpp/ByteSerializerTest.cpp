#include "gtest/gtest.h"
#include "../../main/cpp/ByteSerializer.h"
#include "../../main/cpp/ByteDef.cpp"

TEST (ByteSerializer, DummyTest) { 
    byte b = (byte)42;
    EXPECT_EQ ((byte)42, b);
}


TEST (ByteSerializer, SerializeString) { 
    std::string hello = "Hello";
    byte* out = new byte[5];
    ByteSerializer::serialize(hello, out, 0);
    EXPECT_EQ ((byte)72, out[0]);
    EXPECT_EQ ((byte)101, out[1]);
    EXPECT_EQ ((byte)108, out[2]);
    EXPECT_EQ ((byte)108, out[3]);
    EXPECT_EQ ((byte)111, out[4]);
}

int main(int argc, char **argv) {
    ::testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}
