## 🏗️ Architecture & Scope
### 1. High-Performance Microservice Blueprint
The system is designed as a **read-heavy backend microservice**. Given that city data (names and coordinates) is essentially static, the service is optimized for **high-frequency read requests**. To maintain a focused MVP, write functionality was intentionally excluded.

### 2. Evolution of the Data Strategy
* **Initial Approach:** I considered building an in-memory balanced binary tree to partition US cities.
* **Final Decision:** I transitioned to a persistent **H2 Datastore**.
* **Reasoning:** While in-memory structures offer speed, an RDBMS brings the project closer to a **production-grade service**. It allows for standard SQL indexing and persistent storage while maintaining a "zero-infrastructure" footprint suitable for this exercise.

---

## ⚖️ Algorithmic Trade-offs
### 3. Spatial Search: Bounding Box vs. Circular Radius
* **The Approach:** I utilized a **Fixed Bounding Box** (±1.0 degree ≈ 111.32km).
* **The Trade-off:** I chose this for its simplicity and ease of explanation. However, a "square" search has limitations; it may miss cities that fall just outside the box corners but are technically closer than those at the diagonal extremes.
* **V2 Vision:** Future iterations will move toward a **circular radius search**. I plan to investigate **PostGIS** or **H2GIS** extensions to handle native spatial calculations at the database level.

### 4. Sorting: List.sort() vs. Priority Queue
* **The Approach:** Standard Java `List.sort()`.
* **The Decision:** I initially considered a `PriorityQueue` for $O(N \log K)$ performance. However, because the Bounding Box effectively limits the candidate pool to **~100-200 cities**, the performance gain was negligible.
* **Reasoning:** For small $N$, `List.sort()` (Timsort) is highly optimized and provides better cache locality. I chose **code simplicity** and maintainability over premature optimization.

---

## 🚀 The Road to V2 (Future Improvements)
### 5. Client-Driven Discovery
The current search radius is hardcoded. V2 will allow clients to request a specific **search radius**, providing a more flexible API.

### 6. Security & Caching
To transition to a public-facing API, the following are required:
* **Security:** Implementation of Authentication and Authorization (e.g., OAuth2).
* **Caching Layer:** For a high-performing read-heavy service, a caching strategy (like **Spring Cache** for local or **Redis** for distributed) is essential. We will investigate read patterns to determine the best eviction policy.

---

## 📚 Reference Resources
* [Haversine Formula (Wikipedia)](https://en.wikipedia.org/wiki/Haversine_formula) - Theoretical basis for great-circle distance.
* [Geographic Coordinate System (Wikipedia)](https://en.wikipedia.org/wiki/Geographic_coordinate_system) - Reference for Latitude and Longitude logic.

---

## 💡 Disclosure & Reflection
* **Tooling:** I utilized **Gemini** for architectural brainstorming and the **Gemini CLI** for coding assistance.
* **Least Confident Trade-off:** The fixed 1.0-degree offset. While safe for the US, it does not account for longitude compression near the poles—a key fix for a global rollout.
* **Biggest Surprise:** The significant modularity changes in **Spring Boot 4.0.3**, specifically the requirement for the explicit `spring-boot-h2console` dependency.