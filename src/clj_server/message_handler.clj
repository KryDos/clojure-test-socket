(ns clj-server.message_handler
  (:gen-class)
  (:require [clojure.data.json :as json]
            [clj-server.connections_list :as conn_l]))

(defn- is-message-from-devcie?
  [json-message]
  (if (:device json-message)
    true
    false))

(defn- is-message-from-client?
  [json-message]
  (if (:client json-message)
    true
    false))

(defn- process-message-from-device
  [sock json-message]
  
  (if-not (conn_l/exist? :devices sock)
    (conn_l/add! :devices sock))
  
  (str "this was message from device" "\n"))

(defn- process-message-from-client 
  [sock json-message]
  
  (if-not (conn_l/exist? :clients sock)
    (conn_l/add! :clients sock))
  
  (str "this was message from client" "\n"))

(defn process
  [sock msg]
  (let [json-message (try 
                       (json/read-str msg
                                      :key-fn keyword)
                       (catch Exception e (str (json/write-str {:error {:message (.getMessage e)}}) "\n")))]

    (println (string? json-message))
    (if (string? json-message)
      json-message
      (do
        (if (is-message-from-devcie? json-message)
          (process-message-from-device sock json-message)
          (if (is-message-from-client? json-message)
            (process-message-from-client sock json-message)
            nil))))))


    

