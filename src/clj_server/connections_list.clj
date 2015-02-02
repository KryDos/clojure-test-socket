(ns clj-server.connections_list
  (:gen-class))

(def current-connections (agent []))
(def devices (agent []))
(def clients (agent []))

(defn- get-agent-based-on-keyword
  [keyword]
  (if (= :devices keyword)
    devices
    (if (= :clients keyword)
      clients
      nil)))

(defn add!
  [to connection]
  (send (get-agent-based-on-keyword to) #(conj % connection)))

(defn remove!
  [from connection]
  (send (get-agent-based-on-keyword from) (fn [c] (filter #(not= % connection) c))))

(defn exist? 
  [where connection]
  (if (some #{connection} @(get-agent-based-on-keyword where))
    true
    false))

(defn count!
  [what]
  (count @(get-agent-based-on-keyword what)))
