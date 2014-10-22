#! /usr/bin/env bash
for ((i = 0; i < 10; i++)); do
  java Network -q machine random
done

echo $SECONDS
