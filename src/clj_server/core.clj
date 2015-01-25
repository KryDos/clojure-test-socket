(ns clj-server.core
  (:gen-class)
  (:require [clj-server.message-handler :as mh]
            [clojure.java.io :as io])
  (:import [java.net ServerSocket]))

(def connections-list (agent []))

(defn update-connection-list [sock]
  "update our connections list with new value"
  (send connections-list #(conj % sock)))

(defn remove-from-connections [sock]
  "as we have connections list 
   we want to remove from this list our sock variable"
  (send connections-list 
        (fn [c-v]
         (filter 
           (fn [el]
             (if-not (= el sock)
               true
               false))
             c-v))))

(defn process 
  [msg]
  (str (.toUpperCase msg) "\n"))

(defn sock-receive
  [socket]
  (.readLine (io/reader socket)))

(defn sock-send
  [socket msg]
  (let [writer (io/writer socket)]
    (.write writer msg)
    (.flush writer)))

(defn add-to-connections-list-if-needed
  [sock]
  (if-not (some #{sock} @connections-list)
      (update-connection-list sock)
      nil))

(defn get-into-message-loop
  [sock]
  (println (count @connections-list))
  (let [msg (sock-receive sock)]
    (println "Received: " "\"" msg "\"")

    (add-to-connections-list-if-needed sock)

    (if (= msg nil) ;; received nil if connection was closed
      (do (println "connection closed.")
          (.close sock)
          (remove-from-connections sock))
      (sock-send sock (process msg))))
  (recur sock))

(defn accept-and-process
  [l-socket]
  (let [socket (.accept l-socket)]
    (future (get-into-message-loop socket)))
  (recur l-socket))

(defn -main 
  []
  (let [l-socket (ServerSocket. 8888)]
    (future (accept-and-process l-socket))
    (println "Server started...")))


