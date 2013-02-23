#!/usr/local/bin/python
# coding: utf-8
# 
# Method: Submit Snow Pressure Data
# 
# This method is used to submit snow pressure data to the server API. 

import BasicHttp
import json
from time import localtime, strftime

# Parameters (change username and password to your API user)
api_url     = 'https://ip30.csse.tt.ltu.se/ssc/api/basic/'
api_fnc     = 'snow_pressure/submit'
api_usr     = 'admin'
api_pwd     = ''

# All parameters in each data set are REQUIRED in the submit.
# Data is sent as a JSON formatted array and it is allowed to submit data sets 
# from different sensors in the same request.
# Parameters:
#   serial      - Identifies the sensor generating the data set
#   shoveld     - Was the location cleaned from snow, 0=NO, 1=YES
#   weight      - The snow weight in grams
#   depth       - The snow depth in centimeters
#   temperature - The temperature in celcius degrees
#   humidity    - Percentage in RH (relative humidity)
#   info        - Free text information about this data set, max 100 chars
measurement =  {'measurements': \
                    [{ \
                        'serial':'12345', \
                        'shoveld':1, \
                        'weight':1100, \
                        'depth':488, \
                        'temperature':-1, \
                        'humidity':83, \
                        'info':'', \
                        'data_time':strftime('%Y-%m-%d %H:%M:%S', localtime()) \
                    }] \
                }

api_data = {'param': json.dumps(measurement)}
          
# Make the server request
bh = BasicHttp.BasicHttp(api_url)
bh.post(api_fnc,api_usr,api_pwd,api_data)
