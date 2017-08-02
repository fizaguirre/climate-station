#!/usr/bin/python

import json
import sys
import random
import time
import threading
from datetime import datetime
from datetime import timedelta

from azure.storage.queue import QueueService

MESSAGE_STRING = 186

def generate(id, n):
    global TERMINATE

    queue_service = QueueService(account_name='izastorm',
                    account_key='isUic1EEbXg8l53zUrl+o1Jmf8JPze/E8S5XQ3ActlrmpEmGqMSKdkSP/RTF4aFAdQmLeVy6DWT3pGJ1k/I2HA==')
    dateFormat = '%Y-%m-%d-%H-%M-%S'

    time.sleep(random.uniform(0.0, 5.0))

    while(True):
        data ={}
        datetimeValues = {}
        datetimeValues['format'] = dateFormat
        datetimeValues['source'] = 'RTC_DS1307'
        #datetimeValues['stationID'] = random.randint(0,10)
        datetimeValues['stationID'] = id
        datetimeValues['value'] = time.strftime(dateFormat)

        sensorsValues = {}
        sensorsValues['LDR'] = random.uniform(0.0,100.0)
        sensorsValues['DHT22_AH'] = random.uniform(0.0, 100.0)
        sensorsValues['BMP085_PRESSURE'] = random.uniform(90000.0, 110000.0)
        sensorsValues['DHT22_TEMP'] = random.uniform(0.0, 45.0)

        data['sensors'] = sensorsValues
        data['datetime'] = datetimeValues

        #print json.dumps(data).decode('utf-8')
        try:
            queue_service.put_message('stormtest', json.dumps(data).decode('utf-8'))
        except:
            time.sleep(random.uniform(0.0, 2.0))

        try:
            time.sleep(n)
        except KeyboardInterrupt:
            TERMINATE = False


        if TERMINATE:
            exit(0)

def main():
    global TERMINATE
    TERMINATE = False
    if len(sys.argv) == 4:
        interval = int(sys.argv[1])
        nstations = int(sys.argv[2])
        m = int(sys.argv[3])

        tsleep = float(60.0/float(interval))

        threads = []
        for i in range(nstations * m, (nstations * m) + nstations):
            t = threading.Thread(target=generate, args=(i, tsleep))
            #time.sleep(random.uniform(0.0,0.5))
            threads.append(t)
            t.start()

        try:
            while(True):
                time.sleep(1)
                #nothing
        except KeyboardInterrupt:
            print("Terminate")
            TERMINATE = True

        for t in threads:
            t.join()

        #print "Messages %s per second" %(n)

        #generate(float(60.0/float(interval)))


if __name__ == "__main__":
    main()
