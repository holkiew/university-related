cmake_minimum_required(VERSION 3.12)
project(game_of_life C)

set(CMAKE_C_STANDARD 11)

include_directories(.)

add_executable(game_of_life
        config.c
        config.h
        game.c
        game.h
        main.c
        mem.c
        mem.h)