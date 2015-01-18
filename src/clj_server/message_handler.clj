(ns clj-server.message-handler
  (:gen-class)
  (:require [clojure.data.json :as json]))

(def connections-map (agent {}))

(defn get-connections-list-as-json
  "returns map-to-json"
  [map]
  (json/write-str @connections-map))

(defn update-connection-map
  [map k v]
  (send map #(assoc % k v)))

(defn process-message
  [msg]
  (let [message-map (json/read-str msg)]
    (if (= (get message-map "command")
           "show")
      (get-connections-list-as-json @connections-map)
      (do
        (update-connection-map connections-map (get message-map "id") (get message-map "command"))
        (str (get message-map "command"))))))

