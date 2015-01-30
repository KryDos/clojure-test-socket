(ns clj-server.connections_list
  (:gen-class))

(def current-connections (agent []))

(defn add!
  [connection]
  (send current-connections #(conj % connection)))

(defn remove!
  [connection]
  (send current-connections (fn [c] (filter #(not= % connection) c))))

(defn exist? 
  [connection]
  (if (some #{connection} @current-connections)
    true
    false))

(defn count!
  []
  (count @current-connections))
