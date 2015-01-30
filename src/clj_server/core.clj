(ns clj-server.core
  (:gen-class)
  (:require [clojure.java.io :as io]
            [clj-server.message_handler :as msg_h]
            [clj-server.connections_list :as conn_l])
  (:import [java.net ServerSocket]))

(def ^:const port 8888)

(def server-status (agent false))

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
  (if-not (conn_l/exist? sock)
    (conn_l/add! sock)
    nil))

(defn get-into-message-loop
  [sock]
  (println (conn_l/count!))
  (let [msg (sock-receive sock)]
    (println "Received: " "\"" msg "\"")

    (add-to-connections-list-if-needed sock)

    (if (= msg nil) ;; received nil if connection was closed
      (do (println "connection closed.")
          (.close sock)
          (conn_l/remove! sock))
      (sock-send sock (msg_h/process msg))))
  (recur sock))

(defn accept-and-process
  [l-socket]
  (let [socket (.accept l-socket)]
    (future (get-into-message-loop socket)))
  (recur l-socket))

(defn -main 
  []
  (let [l-socket (ServerSocket. port)]
    (future (accept-and-process l-socket))
    (println "Server started...")))

(defn start-server
  []
  (.start (Thread. -main)))
