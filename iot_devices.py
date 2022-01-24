from kafka import KafkaProducer
import numpy as np
from sys import argv, exit
from time import time, sleep

DEVICE_PROFILES = {
	"boston": {"temp": (52, 18),
	           "humd": (77.4, 18.7),
	           "pres": (1019.0, 9.5)
	           },
    "chicago": {"temp": (0, 85.3),
	            "humd": (76, 23),
	            "pres": (1012.5, 11.2)
	            },	           
	"denver": {"temp": (50.2, 11.1),
	           "humd": (33.0, 13.7),
	           "pres": (1015.0, 10.5)
	           }
	}

if len(argv) != 2 or argv[1] not in DEVICE_PROFILES:
	print("Please provide a valid device name")
	for key in DEVICE_PROFILES:
		print(f"\t{key}")
	exit(1)

profile_name = argv[1]
profile = DEVICE_PROFILES[profile_name]

producer = KafkaProducer(bootstrap_servers="localhost:9092")

while True:
	temp = np.random.normal(profile['temp'][0], profile['temp'][1])
	humd = np.random.normal(profile['humd'][0], profile['humd'][1])
	pres = np.random.normal(profile['pres'][0], profile['pres'][1])

	msg = f"{time()},{profile_name},{temp},{humd},{pres}"

	producer.send('weather', bytes(msg, encoding='utf8'))
	print('Sending message to kafka...')
	sleep(0.5)