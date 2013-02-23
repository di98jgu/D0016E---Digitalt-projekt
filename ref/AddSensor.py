#!/usr/local/bin/python
# coding: utf-8
#
# Method: Add Sensor
# 
# This method is used to add a new sensor to the system. The new 
# sensor will automatically be added into the same domain as the API 
# user. 

import BasicHttp
import json

# Parameters (change username and password to your API user)
api_url     = 'https://ip30.csse.tt.ltu.se/ssc/api/basic/'
api_fnc     = 'sensor/add'
api_usr     = 'admin'
api_pwd     = ''

# All parameters are REQUIRED in the add request.
# Parameters:
#   serial         - Unique sensor serial number to identify the sensor with
#                    only alphanumeric characters allowed.
#   name           - User friendly sensor name string, max 50 chars
#   location       - User friendly information on the location, max 128 chars
#   latitude       - Latitude parameter, float
#   longitude      - Longitude parameter, float
#   type_name      - Information on sensor type
#                    Possible values:
#                    FreeText, Temperature, Motion, GeoLocation, SnowPressure
#   deployed_state - Information on sensor status
#                    Possible values: DEPLOYED, NOT_DEPLOYED
#   visibility     - Set visibility on sensor, i.e. visible for other domain users or not
#                    Possible values: 1-Public, 0-Private
#   info           - Additional information field for the sensor, max 255 chars
sensor = { \
          'serial':'12345', \
          'name':'', \
          'location':'', \
          'latitude':0.0, \
          'longitude':0.0, \
          'type_name':'FreeText', \
          'deployed_state':'DEPLOYED', \
          'visibility':0, \
          'info':'' \
          }
          
api_data = {'param': json.dumps(sensor)}
          
# Make the server request
bh = BasicHttp.BasicHttp(api_url)
bh.post(api_fnc,api_usr,api_pwd,api_data)
