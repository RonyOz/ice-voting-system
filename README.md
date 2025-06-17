# Ice Voting System

> A Distributed, Resilient, and Secure Voting Consultation Platform

[![Java](https://img.shields.io/badge/Java-11-blue)](https://adoptopenjdk.net/)
[![ZeroC Ice](https://img.shields.io/badge/ZeroC%20Ice-3.7.2-blue)](https://zeroc.com/ice)

---

## 🚀 Overview

> \[!IMPORTANT]
> This project was developed as the final assignment for the course "Software Engineering IV" at Universidad Icesi. It explores the principles of distributed systems architecture, design under non-functional requirements, and advanced service orchestration with ZeroC Ice.

`ice-voting-system` is a modular and distributed application designed to support resilient, scalable, and consistent consultation of election data across multiple nodes and services. It enables voters to consult their polling location and candidates, cast votes, and support secure aggregation of results.

---

## 📚 Contents

* [Overview](#-overview)
* [System Architecture](#-system-architecture)
* [Core Features](#-core-features)
* [Getting Started](#-getting-started)
* [Folder Structure](#-folder-structure)
* [Learnings & Thanks](#-learnings--thanks)

---

## 🌐 System Architecture

> \[!NOTE]
> This system uses a microservice-based model using ZeroC Ice + IceGrid for service discovery and deployment. Communication is achieved through direct proxies, event-based publishing with IceStorm, and message durability through a custom reliable messaging layer.

**Subsystems**:

* `VotingSite` (remote control + batching voter commands)
* `VotingNode` (vote emitter and mesa-level agent)
* `VotingService` (write-side command processing with sharding)
* `ConsultService` (read-side information provider)
* `ProxyCache` (smart proxy for read-optimizations)
* `AuthService` (voter authorization & deduplication)
* `IceStorm` (event broker for pub/sub delivery)
* `ReliableMessaging` (FIFO durable messaging via polling)

---

## ✨ Core Features

* Asynchronous command batching
* Resilient result collection with master-worker & thread pool strategies
* Shard-aware data processing
* IceGrid multi-node deployment with `on-demand` server activation
* Experimental evaluation tools for performance under load

> \[!TIP]
> You can explore the architecture diagrams in `doc/vpp/Deployment Diagram.pdf` and the experiment protocol in `doc/Diseño de experimento.pdf`

---

## ⚡ Getting Started

> \[!WARNING]
> This system requires Java 11 and ZeroC Ice 3.7.2 installed and configured.

```bash
# Clone and build
$ git clone https://github.com/RonyOz/ice-voting-system
$ cd src
$ ./gradlew clean build

# Launch IceBox with IceStorm
$ icebox --Ice.Config=icestorm-config/config.icebox

# Start IceGrid registry & nodes
$ icegridnode --Ice.Config=IceGrid/VotingGrid/src/config.grid
$ icegridnode --Ice.Config=IceGrid/QueryGrid/src/config.grid

# Have fun deployment clients
$ java -jar votingController.jar
$ java -jar VotingSite.jar
$ java -jar votingNode.jar
$ ...
```

---

## 🗂️ Folder Structure (Highlights)

```
src/
├── Contract/               # IDL & generated proxies
├── votingService/          # Write-side voting logic
├── consultService/         # Read-side consults and sharding
├── authService/            # Voter auth backend
├── votingNode/             # Vote emitter node
├── votingSite/             # External interface controller
├── proxyCache/             # Local smart proxy layer
├── reliableMessaging/      # Custom reliable messaging service
├── IceGrid/                # Grid configurations for nodes
├── icestorm-config/        # IceStorm IceBox and DB config
└── votingController/       # Client controller for testing & operations
```

---

## 🎓 Learnings & Thanks
We are deeply proud of what we built. This project taught us about:

* The intricacies of asynchronous communication
* Analysis of single points of failures
* Deployment across a real network
* ZeroC Ice internals & deployment via IceGrid, IceStorm

---

## 👥 Contributors

* [Rony Ordoñez](https://github.com/Rony7v7)
* [Juan José de la Pava](https://github.com/JuanJDlp)
* [David Artunduaga](https://github.com/David104087)
* [Diego Polanco](https://github.com/diegopolancolozano)

---

> This project represents countless hours of learning and problem-solving. We're proud of what we've built and hope this project provides a solid foundation or sparks new ideas for similar endeavors.  Final grade 4.8/5.0 ✨
