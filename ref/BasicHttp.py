#!/usr/local/bin/python
# coding: utf-8
# 
# The BasicHttp Class
#
# To simplify code examples we start by defining the following class 
# to handle the actual communication with the server API using HTTP 
# Basic authentication. This class is then imported and used in all 
# the API method examples presented below. 
#
# IMPORTANT!
# Python package 'Requests' must be installed to use this example code.
# See http://docs.python-requests.org
#

import requests
import json
from requests.auth import HTTPBasicAuth

# 
# Communication class for Sense Smart City RESTful API
#
class BasicHttp():
    
    # Constructor initiating the server url
    def __init__(self,url):
        global server_url
        global verify_cert
        server_url = url
        verify_cert = False
    
    # Make a GET request to the API
    def get(self,api_fnc,api_usr,api_pwd,api_data):
        try:
            resp = requests.get(server_url+api_fnc, params=api_data, \
                    auth=HTTPBasicAuth(api_usr,api_pwd), verify=verify_cert)
            print("Response code:" + str(resp.status_code))
            print("Response text:" + resp.text)
        except Exception, e:
            print("Failed HTTP basic authentication request")
            print(e)
            
    # Make a POST request to the API
    def post(self,api_fnc,api_usr,api_pwd,api_data):
        try:
            resp = requests.post(server_url+api_fnc, data=api_data, \
                    auth=HTTPBasicAuth(api_usr,api_pwd), verify=verify_cert)
            print("Response code:" + str(resp.status_code))
            print("Response text:" + resp.text)
        except Exception, e:
            print("Failed HTTP basic authentication request")
            print(e)
