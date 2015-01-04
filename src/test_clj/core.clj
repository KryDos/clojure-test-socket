(ns test-clj.core
  (:gen-class)
  (:use server.socket))
(import '[java.io BufferedReader InputStreamReader OutputStreamWriter])

(def port 8888)

(defn process-message
  [msg]
  (str "Message => " msg))

(defn echo-server
  [in out]
  (binding [*in* (BufferedReader. (InputStreamReader. in))
            *out* (OutputStreamWriter. out)]
    (loop []
      (println (process-message (read-line)))
      (recur))))


(defn -main
  []
  (create-server port echo-server))


