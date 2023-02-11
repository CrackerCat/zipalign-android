cmake_minimum_required(VERSION 3.6)
project(zipalign VERSION 1.0.0 LANGUAGES C CXX)

set(CMAKE_CXX_STANDARD 17)

set(C_FLAGS "-Werror=format -fdata-sections -ffunction-sections -fno-exceptions -fno-rtti -fno-threadsafe-statics")
set(LINKER_FLAGS "-Wl,--hash-style=both")

if (NOT CMAKE_BUILD_TYPE STREQUAL "Debug")
    message("Builing Release...")

    set(C_FLAGS "${C_FLAGS} -O2 -fvisibility=hidden -fvisibility-inlines-hidden")
    set(LINKER_FLAGS "${LINKER_FLAGS} -Wl,-exclude-libs,ALL -Wl,--gc-sections")
    
    add_definitions(-DNDEBUG)
else()
    message("Builing Debug...")

    add_definitions(-DDEBUG)
endif ()

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} ${C_FLAGS}")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${C_FLAGS}")

set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} ${LINKER_FLAGS}")
set(CMAKE_MODULE_LINKER_FLAGS "${CMAKE_MODULE_LINKER_FLAGS} ${LINKER_FLAGS}")

add_definitions(-D_FILE_OFFSET_BITS=64)

find_library(android-lib android)
find_package(cxx REQUIRED CONFIG)

# zlib
add_library(zlib STATIC
    aosp/src/zlib/adler32.c
    aosp/src/zlib/compress.c
    aosp/src/zlib/cpu_features.c
    aosp/src/zlib/crc32.c
    aosp/src/zlib/deflate.c
    aosp/src/zlib/gzclose.c
    aosp/src/zlib/gzlib.c
    aosp/src/zlib/gzread.c
    aosp/src/zlib/gzwrite.c
    aosp/src/zlib/infback.c
    aosp/src/zlib/inffast.c
    aosp/src/zlib/inflate.c
    aosp/src/zlib/inftrees.c
    aosp/src/zlib/trees.c
    aosp/src/zlib/uncompr.c
    aosp/src/zlib/zutil.c)
target_compile_definitions(zlib PRIVATE -DHAVE_HIDDEN -DZLIB_CONST)
target_include_directories(zlib PRIVATE aosp/src/zlib)
target_include_directories(zlib PUBLIC aosp/src/zlib)

# zopfli
add_library(zopfli STATIC
    aosp/src/zopfli/src/zopfli/blocksplitter.c
    aosp/src/zopfli/src/zopfli/cache.c
    aosp/src/zopfli/src/zopfli/deflate.c
    aosp/src/zopfli/src/zopfli/gzip_container.c
    aosp/src/zopfli/src/zopfli/hash.c
    aosp/src/zopfli/src/zopfli/katajainen.c
    aosp/src/zopfli/src/zopfli/lz77.c
    aosp/src/zopfli/src/zopfli/squeeze.c
    aosp/src/zopfli/src/zopfli/tree.c
    aosp/src/zopfli/src/zopfli/util.c
    aosp/src/zopfli/src/zopfli/zlib_container.c
    aosp/src/zopfli/src/zopfli/zopfli_lib.c)
target_include_directories(zopfli PRIVATE aosp/src/zopfli/src)
target_include_directories(zopfli PUBLIC aosp/src/zopfli/src)
target_link_libraries(zopfli PRIVATE m)

include_directories(
    aosp/src/libsystem/include
    aosp/src/libutils/include
    aosp/src/libcutils/include)
    
link_libraries(cxx::cxx)

# base
add_library(base STATIC
    aosp/src/libbase/errors_unix.cpp
    aosp/src/libbase/abi_compatibility.cpp
    aosp/src/libbase/chrono_utils.cpp
	aosp/src/libbase/cmsg.cpp
	aosp/src/libbase/file.cpp
	aosp/src/libbase/hex.cpp
	aosp/src/libbase/logging.cpp
	aosp/src/libbase/mapped_file.cpp
	aosp/src/libbase/parsebool.cpp
	aosp/src/libbase/parsenetaddress.cpp
	aosp/src/libbase/posix_strerror_r.cpp
	aosp/src/libbase/process.cpp
	aosp/src/libbase/properties.cpp
	aosp/src/libbase/stringprintf.cpp
	aosp/src/libbase/strings.cpp
	aosp/src/libbase/threads.cpp
	aosp/src/libbase/test_utils.cpp)
target_include_directories(base PUBLIC
    aosp/src/libbase/include
    aosp/src/liblog/include)

# utils
add_library(utils STATIC
    aosp/src/libutils/Errors.cpp
	aosp/src/libutils/FileMap.cpp
	aosp/src/libutils/JenkinsHash.cpp
	aosp/src/libutils/LightRefBase.cpp
	aosp/src/libutils/NativeHandle.cpp
	aosp/src/libutils/Printer.cpp
	aosp/src/libutils/RefBase.cpp
	aosp/src/libutils/SharedBuffer.cpp
	aosp/src/libutils/StopWatch.cpp
	aosp/src/libutils/String8.cpp
	aosp/src/libutils/String16.cpp
	aosp/src/libutils/StrongPointer.cpp
	aosp/src/libutils/SystemClock.cpp
	aosp/src/libutils/Threads.cpp
	aosp/src/libutils/Timers.cpp
	aosp/src/libutils/Tokenizer.cpp
	aosp/src/libutils/Unicode.cpp
	aosp/src/libutils/VectorImpl.cpp
	aosp/src/libutils/misc.cpp)
target_include_directories(utils PUBLIC
    aosp/src/liblog/include 
    aosp/src/libbase/include)

# cutils
add_library(cutils STATIC
    aosp/src/libcutils/socket_inaddr_any_server_unix.cpp
	aosp/src/libcutils/socket_local_client_unix.cpp
	aosp/src/libcutils/socket_local_server_unix.cpp
	aosp/src/libcutils/socket_network_client_unix.cpp
	aosp/src/libcutils/sockets_unix.cpp
	aosp/src/libcutils/sockets.cpp
	aosp/src/libcutils/fs.cpp
	aosp/src/libcutils/hashmap.cpp
	aosp/src/libcutils/multiuser.cpp
	aosp/src/libcutils/str_parms.cpp
	aosp/src/libcutils/ashmem-host.cpp
	aosp/src/libcutils/canned_fs_config.cpp
	aosp/src/libcutils/fs_config.cpp
	aosp/src/libcutils/trace-host.cpp
	aosp/src/libcutils/config_utils.cpp
	aosp/src/libcutils/iosched_policy.cpp
	aosp/src/libcutils/load_file.cpp
	aosp/src/libcutils/native_handle.cpp
	aosp/src/libcutils/properties.cpp
	aosp/src/libcutils/record_stream.cpp
	aosp/src/libcutils/strlcpy.c
	aosp/src/libcutils/threads.cpp)
target_compile_definitions(cutils PRIVATE -D_GNU_SOURCE)
target_include_directories(cutils PUBLIC
    aosp/src/libbase/include
    aosp/src/liblog/include)

# log
add_library(log STATIC
    aosp/src/liblog/log_event_list.cpp
    aosp/src/liblog/log_event_write.cpp
    aosp/src/liblog/logger_name.cpp
    aosp/src/liblog/logger_read.cpp
    aosp/src/liblog/logger_write.cpp
    aosp/src/liblog/properties.cpp
    aosp/src/liblog/logprint.cpp)
target_compile_definitions(log PRIVATE -DLIBLOG_LOG_TAG=1006 -D_XOPEN_SOURCE=700 -DFAKE_LOG_DEVICE=1)
target_include_directories(log PUBLIC
    aosp/src/liblog/include
    aosp/src/libbase/include)

# ziparchive
add_library(ziparchive STATIC
    aosp/src/libziparchive/zip_archive.cc
    aosp/src/libziparchive/zip_archive_stream_entry.cc
    aosp/src/libziparchive/zip_cd_entry_map.cc
    aosp/src/libziparchive/zip_writer.cc
    aosp/src/libziparchive/zip_error.cpp
    aosp/src/libziparchive/incfs_support/signal_handling.cpp)
target_compile_options(ziparchive PRIVATE -std=c++20)
set_property(TARGET ziparchive PROPERTY CXX_STANDARD 20)
target_compile_definitions(ziparchive PRIVATE -DZLIB_CONST -DZIPARCHIVE_DISABLE_CALLBACK_API=1)
target_include_directories(ziparchive PUBLIC
    aosp/src/libziparchive/include
    aosp/src/libziparchive/incfs_support/include
    aosp/src/libbase/include 
    aosp/src/liblog/include
    aosp/src/googletest/googletest/include)

# zipalign
add_library(zipalign SHARED
    aosp/src/zipalign/ZipAlign.cpp
    aosp/src/zipalign/ZipEntry.cpp
    aosp/src/zipalign/ZipFile.cpp
    ZipAlign.cpp)
target_include_directories(zipalign PRIVATE aosp/src/zipalign/include)
target_link_libraries(zipalign ${android-lib} base utils cutils log ziparchive zopfli zlib)

if (NOT CMAKE_BUILD_TYPE STREQUAL "Debug")
    add_custom_command(TARGET zipalign POST_BUILD
            COMMAND ${CMAKE_STRIP} --remove-section=.comment "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/libzipalign.so")
endif()
