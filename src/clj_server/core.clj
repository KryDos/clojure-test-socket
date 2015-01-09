(ns clj-server.core
  (:gen-class)
  (:use server.socket)
  (:require [clojure.data.json :as json]))
(import '[java.io BufferedReader InputStreamReader OutputStreamWriter])

(def port 8888)
(def connections-map (agent {}))

(defn process-message
  [msg]
  (let [message-map (json/read-str msg)]
    (send connections-map (fn [conn-map]
                            (assoc conn-map
                              (get message-map "id")
                              (get message-map "command"))))
    (if (= (get message-map "command")
           "show")
      (json/write-str @connections-map)
      (str (get message-map "command")))))


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


