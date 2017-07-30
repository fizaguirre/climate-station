#!/usr/bin/python

import json
import sys
import random
import time
from datetime import datetime
from datetime import timedelta

from azure.storage.queue import QueueService

MESSAGE_STRING = 186

def generate(n):

    queue_service = QueueService(account_name='izastorm',
                    account_key='isUic1EEbXg8l53zUrl+o1Jmf8JPze/E8S5XQ3ActlrmpEmGqMSKdkSP/RTF4aFAdQmLeVy6DWT3pGJ1k/I2HA==')
    dateFormat = '%Y-%m-%d-%H-%M-%S'

    while(True):
        data ={}
        datetimeValues = {}
        datetimeValues['format'] = dateFormat
        datetimeValues['source'] = 'RTC_DS1307'
        datetimeValues['stationID'] = random.randint(0,10)
        datetimeValues['value'] = time.strftime(dateFormat)

        sensorsValues = {}
        sensorsValues['LDR'] = random.uniform(30.0,50.0)
        sensorsValues['DHT22_AH'] = random.uniform(50.0, 70.0)
        sensorsValues['BMP085_PRESSURE'] = random.uniform(90000.0, 110000.0)
        sensorsValues['DHT22_TEMP'] = random.uniform(10.0, 40.0)

        data['sensors'] = sensorsValues
        data['datetime'] = datetimeValues

        print json.dumps(data).decode('utf-8')
        queue_service.put_message('stormtest', json.dumps(data).decode('utf-8'))
        time.sleep(n)

def main():
    if len(sys.argv) == 2:
        n = int(sys.argv[1])

        print "Messages %s per second" %(n)

        generate(float(60.0/float(n)))


if __name__ == "__main__":
    main()
