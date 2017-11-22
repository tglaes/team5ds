/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * File:   newsimpletest.c
 * Author: zsisamci
 *
 * Created on 15. November 2017, 12:15
 */

#include <stdio.h>
#include <stdlib.h>

/*
 * Simple C Test Suite
 */

int get_data_from_user(char* data);

void testGet_data_from_user() {
    char* data;
    int result = get_data_from_user(data);
    if (1 /* result*/) {
        printf("%%TEST_FAILED%% time=0 testname=testGet_data_from_user (newsimpletest) message=error message sample\n");
    }
}

int main(int argc, char** argv) {
    printf("%%SUITE_STARTING%% newsimpletest\n");
    printf("%%SUITE_STARTED%%\n");

    printf("%%TEST_STARTED%%  testGet_data_from_user (newsimpletest)\n");
    testGet_data_from_user();
    printf("%%TEST_FINISHED%% time=0 testGet_data_from_user (newsimpletest)\n");

    printf("%%SUITE_FINISHED%% time=0\n");

    return (EXIT_SUCCESS);
}
