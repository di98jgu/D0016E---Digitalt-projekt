#!/usr/local/bin/python
# coding: utf-8
# 
# Method: List Sensors
# 
# This method is used to list sensors in your domain (both public and
# private). It is also used to list sensors that are marked as public 
# but owned by other domains. 

import BasicHttp
import json

# Parameters (change username and password to your API user)
api_url     = 'https://ip30.csse.tt.ltu.se/ssc/api/basic/'
api_fnc     = 'sensor/list'
api_usr     = 'admin'
api_pwd     = ''

# All parameters are optional and omitting them all will result in 
# a list of all sensors in your domain.
#
# Parameters:
#   sensors  - JSON formatted list of serials sensors requested
#   public   - Set visibility filter, i.e. show sensors for other domains
#              Possible values: 'yes'-include public, 'no'-do not
#   format   - Defines the response format to 'json' or 'xml'

api_data = { \
             'sensors': json.dumps(['12345']), \
             'public':'yes', \
             'format':'xml' \
           }

# Make the server request
bh = BasicHttp.BasicHttp(api_url)
bh.get(api_fnc,api_usr,api_pwd,api_data)
