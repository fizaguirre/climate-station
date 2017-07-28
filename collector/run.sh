#!/bin/bash

mvn exec:java -Dexec.mainClass="csc.collector.TopologyMain" -Dexec.args="data.json"
