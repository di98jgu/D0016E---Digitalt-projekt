#!/usr/local/bin/python
# coding: utf-8
# 
# Method: Request Sensor Data
# 
# This method is used to request sensor data stored in the Sense 
# Smart City server. All sensor type data (motion, temperature, etc.) 
# is reachable using the same type of request parameters. The only 
# difference is the method name used in the url (e.g. 'motion/request' 
# or 'temperature/request') and what fields are available to specify 
# for the result set.

import BasicHttp
import json
from time import localtime, strftime

# Parameters (change username and password to your API user)
api_url     = 'https://ip30.csse.tt.ltu.se/ssc/api/basic/'
api_fnc     = 'snow_pressure/request'
# api_fnc     = 'free_text/request'
api_usr     = 'snowtest'
api_pwd     = 'ltusnowtester'

# All parameters are OPTIONAL (except sensors array) in sensor update request.
# Parameters:
#   sensors         - JSON formatted list of serials to define result set sensors.
#   fields          - JSON formatted list of data fields requested for result set.
#                     (see submit script for possible fields to specify).
#   period          - Keyword for simple way to specify time period for data request.
#                     Possible values: last-hour, last-day, last-week, last-month, last-year
#   start           - Start date/time for data request (YYYY-MM-DD HH:MM:SS)
#   end             - End date/time for data request... not optional start time specified
#   limit           - Limit result set to this many records
#   offset          - Return data from this offset position in the possible result set
#   sort            - Sort data in 'desc' or 'asc' order.
           
api_data = { \
               'sensors': json.dumps(['12345']), \
               'fields':json.dumps(['shoveld', 'weight', 'depth', 'temperature', 'humidity', 'data_time', 'info']), \
               #'period':'last-month', \
               #'start':'2012-12-18 11:04:00', \
               #'end':'2012-12-18 16:04:30', \
               #'limit':'100', \
               #'offset':'0', \
               #'sort':'desc', \
           }

           
# Make the server request
bh = BasicHttp.BasicHttp(api_url)
bh.get(api_fnc, api_usr, api_pwd, api_data)
