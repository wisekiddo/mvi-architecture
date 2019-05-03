
cmake_minimum_required(VERSION 3.6)

add_library( libso SHARED src/main/cpp/RSAEncrypt.cpp )

include_directories(src/main/jni/include/)

find_library( log-lib log )

target_link_libraries( libso
                       ${CMAKE_CURRENT_SOURCE_DIR}/src/main/jni/openssl/${ANDROID_ABI}/lib/libcrypto.a
                       ${CMAKE_CURRENT_SOURCE_DIR}/src/main/jni/openssl/${ANDROID_ABI}/lib/libssl.a
                       ${log-lib} )