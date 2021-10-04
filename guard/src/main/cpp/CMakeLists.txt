cmake_minimum_required(VERSION 3.10.2)

# Declares and names the project.

project("guard")

FILE(GLOB SRC_LIST_CPP ${PROJECT_SOURCE_DIR}/*.cpp)

add_library( # Sets the name of the library.
             guard

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             ${SRC_LIST_CPP} )



find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )


target_link_libraries( # Specifies the target library.
                       guard

                       # included in the NDK.
                       ${log-lib} )
