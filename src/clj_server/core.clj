(ns clj-server.core
  (:gen-class)
  (:require [clj-server.message-handler :as mh]
            [clojure.java.io :as io])
  (:import [java.net ServerSocket]))

(def connections-list (agent []))

(defn update-connection-list [sock]
  (send connections-list #(conj % sock)))

(defn remove-from-connections [sock]
  (send connections-list (fn [c-v]
                           (filter (fn [el]
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

(defn get-into-message-loop
  [sock]
  (println (count @connections-list))
  (let [msg (sock-receive sock)]
    (println "Received: " "\"" msg "\"")
    (if-not (some #{sock} @connections-list)
      (do (println "Adding to the list")
          (update-connection-list sock))
      (println "Socket alraedy in the list"))

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


