# Networking

## i) Primitives

### 1) IP Address

- identity of a machine in a network.

- **Local Ips** are addresses that only work for your router and allows devices to "talk" to the router and all the other devices connected to it.

- **public ip** is the address that is seen by the everyone else on the internet.

### 2) Ports

- An endpoint that identifies a specific application, service or process on a computer network.

- similar to house number or room number, whereas building is the ip address.



### 3 TCP vs UDP

> Many applications are not affected even if some data is lost, hence udp is used.

| TCP                           | UDP                              |
| ----------------------------- | -------------------------------- |
| transmission control protocol | user datagram protocol           |
| sends everything in order     | misses and randomness is allowed |
| slow but reliable             | fast                             |
| simple                        | complex                          |
| resends lost packets          | no resending                     |



## ii) Chat Server

### 1) Socket

ONE connection between TWO programs


