#!/bin/bash

#mvn exec:java -Dexec.mainClass="csc.collector.TopologyMain" -Dexec.args="data.json"

mvn exec:java -D storm.topology="csc.collector.TopologyMain" -Dexec.args="data.json"
