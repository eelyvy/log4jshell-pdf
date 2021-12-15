#!/usr/bin/env bash

curl -i -X POST http://127.0.0.1:8080/api/parse --data-binary "@$1"
