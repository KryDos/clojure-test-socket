(ns clj-server.core
  (:gen-class)
  (:use server.socket)
  (:require [clj-server.message-handler :as mh]))
(import '[java.io BufferedReader InputStreamReader OutputStreamWriter])

(def port 8888)

(defn echo-server
  [in out]
  (binding [*in* (BufferedReader. (InputStreamReader. in))
            *out* (OutputStreamWriter. out)]
    (loop []
      (let [message (read-line)]
        (println (mh/process-message message)))
      (recur))))

(defn -main
  []
  (create-server port echo-server))
