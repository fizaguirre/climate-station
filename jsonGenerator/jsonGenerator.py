#!/usr/bin/python

import json
import sys
import random
import time
from datetime import datetime
from datetime import timedelta

MESSAGE_STRING = 186

def generate(n, filename):
    daysBefore = n/24

    dateFormat = '%Y-%m-%d-%H-%M-%S'
    #today = time.time()
    lastPeriod = datetime.now() - timedelta(minutes=daysBefore)
    print("Number of minutes %i" % daysBefore)
    print("Start date %s" % lastPeriod.strftime(dateFormat))

    currentHour = lastPeriod
    with open(filename, 'w+') as f:
        for i in range(n):
            data ={}
            datetimeValues = {}
            datetimeValues['format'] = dateFormat
            datetimeValues['source'] = 'RTC_DS1307'
            #datetimeValues['value'] = time.strftime(dateFormat)
            datetimeValues['value'] = currentHour.strftime(dateFormat)
            currentHour = currentHour + timedelta(minutes=1)

            sensorsValues = {}
            sensorsValues['LDR'] = random.uniform(30.0,50.0)
            sensorsValues['DHT22_AH'] = random.uniform(50.0, 70.0)
            sensorsValues['BMP085_PRESSURE'] = random.uniform(90000.0, 110000.0)
            sensorsValues['DHT22_TEMP'] = random.uniform(10.0, 40.0)

            data['sensors'] = sensorsValues
            data['datetime'] = datetimeValues

            f.write(json.dumps(data)+'\n')
            #print json.dumps(data)

def main():
    if len(sys.argv) == 3:
        n = sys.argv[1]
        fileName = sys.argv[2]

        lenghFile = float(n)*1024*1024*1024; # GB
        nmessages = lenghFile / MESSAGE_STRING
        print "Messages %i" %(nmessages)

        generate(int(nmessages), fileName)


if __name__ == "__main__":
    main()
