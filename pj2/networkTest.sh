#!/bin/sh
START=$(($(date +'%s * 1000 + %-N / 1000000')))

for ((i = 0; i < 10; i++)); do
  java Network -q machine random
done

END=$(($(date +'%s * 1000 + %-N / 1000000')))
TOTAL=`expr $END - $START`
TOTAL=`expr $TOTAL / 1000`
echo $TOTAL
