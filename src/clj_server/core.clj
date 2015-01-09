(ns clj-server.core
  (:gen-class)
  (:use server.socket)
  (:require [clojure.data.json :as json]))
(import '[java.io BufferedReader InputStreamReader OutputStreamWriter])

(def port 8888)
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

(defn echo-server
  [in out]
  (binding [*in* (BufferedReader. (InputStreamReader. in))
            *out* (OutputStreamWriter. out)]
    (loop []
      (let [message (read-line)]
        (println (process-message message)))
      (recur))))

(defn -main
  []
  (create-server port echo-server))


