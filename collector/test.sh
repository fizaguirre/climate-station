#!/bin/bash

mvn compile
mvn package
mvn exec:java -Dexec.mainClass="csc.collector.TopologyMain" -Dexec.args="data.json"

