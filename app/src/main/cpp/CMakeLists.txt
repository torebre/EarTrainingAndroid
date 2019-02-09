cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -std=c99 -Wall")

add_library(native-audio SHARED
            native-audio.cpp)



# Include libraries needed for native-audio lib
target_link_libraries(native-audio
                      android
                      log
                      OpenSLES)


include_directories(${PROJECT_SOURCE_DIR}/../../../../module/include/)
add_library(libessentia SHARED IMPORTED)

# TODO: Use ABI variable

set_target_properties(libessentia
PROPERTIES IMPORTED_LOCATION
${PROJECT_SOURCE_DIR}/../../../../module/lib/x86_64/libessentia.so)



add_library(essentia-interface SHARED
            essentia-interface.cpp)

target_link_libraries(essentia-interface
log
libessentia)


