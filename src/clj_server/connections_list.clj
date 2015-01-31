(ns clj-server.connections_list
  (:gen-class))

(def current-connections (agent []))
(def devices (agent []))
(def clients (agent []))

(defn add!
  [to connection]
  (send (eval (symbol to)) #(conj % connection))
  ;;(send current-connections #(conj % connection)))

(defn remove!
  [from connection]
  (send (eval (symbol from)) (fn [c] (filter #(not= % connection) c))))
  ;;(send current-connections (fn [c] (filter #(not= % connection) c))))

(defn exist? 
  [where connection]
  ;;(if (some #{connection} @current-connections)
  (if (some #{connection} @(eval (symbol where)))
    true
    false))

(defn count!
  [what]
  ;;(count @current-connections))
  (count @(eval (symbol what))))
