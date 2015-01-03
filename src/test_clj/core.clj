(ns test-clj.core
  (:gen-class)
  (:require [clojure.java.io :as io])
  (:import [java.net ServerSocket]))

;; receives messsage
;; from the socket
(defn receive-msg
  [socket]
  (.readLine (io/reader socket)))

;; send message
;; to the socket
(defn send-msg
  [socket msg]
  (let [writer (io/writer socket)]
    (.write writer msg)
    (.flush writer)))

;; listen for connections
;; on [port]
(defn serv
  [port handler]
  (with-open [server-sock (ServerSocket. port)
              sock (.accept server-sock)]
    (let [msg-in (receive-msg sock)
          msg-out (handler msg-in)]
      (send-msg sock msg-out))))

;; listen for connections
;; and continue work of the server
(defn serv-always
  [port handler]
  (let [running (atom true)]
    (future
      (with-open [server-sock (ServerSocket. port)]
        (while @running
          (with-open [sock (.accept server-sock)]
            (let [msg-in (receive-msg sock)
                  msg-out (handler msg-in)]
              (send-msg sock msg-out))))))
    running))


(defn just-print
  [what-to-print]
  (str "Message => " what-to-print))

(defn -main
  []
  (serv-always 8888 #(just-print %1)))


