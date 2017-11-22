/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   socket_helper.h
 * Author: zsisamci
 *
 * Created on 22. November 2017, 22:02
 */

#ifndef SOCKET_HELPER_H
#define SOCKET_HELPER_H

int send_data(int sock, char* data);
int send_all(int sock, char *data, int length);
int recv_data(int sock, char* data);
int recv_all(int sock, char* data, int length);

#endif /* SOCKET_HELPER_H */

