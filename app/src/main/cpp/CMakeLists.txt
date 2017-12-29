cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -std=c99 -Wall")

add_library(native-audio SHARED
            native-audio.cpp)

# Include libraries needed for native-audio lib
target_link_libraries(native-audio
                      android
                      log
                      OpenSLES)
